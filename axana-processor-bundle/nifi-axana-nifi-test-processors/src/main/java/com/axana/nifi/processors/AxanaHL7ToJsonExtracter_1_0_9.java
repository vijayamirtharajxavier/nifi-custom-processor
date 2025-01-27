package com.axana.nifi.processors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.nifi.annotation.behavior.InputRequirement;
import org.apache.nifi.annotation.behavior.ReadsAttribute;
import org.apache.nifi.annotation.behavior.ReadsAttributes;
import org.apache.nifi.annotation.behavior.SupportsBatching;
import org.apache.nifi.annotation.behavior.WritesAttribute;
import org.apache.nifi.annotation.behavior.WritesAttributes;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.SeeAlso;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.logging.ComponentLog;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.FlowFileAccessException;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.util.StandardValidators;

import com.axana.nifi.processors.classes.FieldNameExtraction;
import com.axana.nifi.processors.classes.SegmentClassName;
import com.axana.nifi.processors.classes.messageTypeClass;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Composite;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Primitive;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v23.segment.MSH;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

@Tags({ "HL7 to Json Converter", "Extract HL7 v2.3 attributes to Json Format with Naming Convention",
        "It supports the following message types : ADT,ORU^R21, ORU^W01, ORU^R01,ORU^R30,ORU^R32,ORU^R40,ORU^42, SIU^S12,SIU^S13,SIU^S14,SIU^S17, RDE^O11",
        "version compiled for NiFi 2.0.0-M4",
        "Set property based Mime_Type",
        "Set property based Segment mapping name eg:PID=patients,NK1=nextofkin,MSH=message_header" })
@CapabilityDescription("This custom process will do the conversion of HL7 message of v2.3 into JSON format with naming convention of segment,element data items as per HL7 structure and Group, it uses HAPI library for Reflection of HL7 standard v2.3")
@SeeAlso({})
@ReadsAttributes({ @ReadsAttribute(attribute = "", description = "") })
@WritesAttributes({ @WritesAttribute(attribute = "", description = "") })
@InputRequirement(InputRequirement.Requirement.INPUT_REQUIRED)
@SupportsBatching

public class AxanaHL7ToJsonExtracter_1_0_9 extends AbstractProcessor {
    static String version_no;
    private static ComponentLog logger;

    public static final Relationship SUCCESS = new Relationship.Builder()
            .name("success")
            .description("Successfully processed files")
            .build();

    public static final Relationship FAILURE = new Relationship.Builder()
            .name("fail")
            .description("Not processed files")
            .build();
    public static final PropertyDescriptor MIME_TYPE = new PropertyDescriptor.Builder()
            .name("Mime Type")
            .description("Specifies the MIME type of the output content, e.g., application/json")
            .required(false) // If you want to make it optional
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .defaultValue("application/json") // Default to application/json
            .build();

    public static final PropertyDescriptor SEGMENT_MAPPING = new PropertyDescriptor.Builder()
            .name("Segment Mapping")
            .description(
                    "A comma-separated list of HL7 segment names to custom names, e.g., PID=patients,NK1=nextofkin")
            .required(false)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .defaultValue("PID=patients,NK1=nextofkin") // Default mappings
            .build();

    @Override
    public void init(final ProcessorInitializationContext context) {
        final List<PropertyDescriptor> descriptors = new ArrayList<>();
        super.init(context);
        this.logger = getLogger(); // Store the logger in an instance variable
        descriptors.add(MIME_TYPE);
        descriptors.add(SEGMENT_MAPPING);
        Collections.unmodifiableList(descriptors);
    }

    @Override
    protected List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        final List<PropertyDescriptor> descriptors = new ArrayList<>();
        descriptors.add(MIME_TYPE);
        descriptors.add(SEGMENT_MAPPING);
        return Collections.unmodifiableList(descriptors);
    }

    @Override
    public Set<Relationship> getRelationships() {
        return Set.of(SUCCESS, FAILURE);
    }

    @Override
    public void onTrigger(ProcessContext context, final ProcessSession session) throws ProcessException {

        final JsonObject jsonOutput;
        String messageType;
        String triggerEvent;
        String incomingHL7Message = null;
        FlowFile flowFile = session.get();
        if (flowFile == null) {
            return;
        }
        // Generate ACK message

        // HL7AckGenerator ackGenerator = new HL7AckGenerator();

        try {

            // Get the mapping from the context
            String mappingStr = context.getProperty(SEGMENT_MAPPING).getValue();
            Map<String, String> segmentMapping = parseMappingString(mappingStr);

            // Read the content of the FlowFile
            String hl7Message = readFlowFileContent(session, flowFile);
            // Check if the message contains '\n' instead of '\r', which could happen due to
            // improper handling
            if (hl7Message.contains("<cr>")) {
                // && !hl7Message.contains("\n")) {
                // Replace newline with carriage return if necessary
                hl7Message = hl7Message.replace("<cr>", "\r");

            }

            try {
                Parser parser = new PipeParser();
                Message message = parser.parse(hl7Message);
                // Extract MSH segment
                MSH mshSegment = (MSH) message.get("MSH");

                // Get message type (MSH-9)
                messageType = mshSegment.getMessageType().getMessageType().getValue();
                triggerEvent = mshSegment.getMessageType().getTriggerEvent().getValue();
             //   logger.info("FlowFile in Else loop, MsgType : " + messageType + ", Event Trigger" + triggerEvent);
                jsonOutput = HL7toJson(hl7Message, context);
                if (jsonOutput == null || jsonOutput.size() == 0) {
                    throw new ProcessException("Conversion resulted in an empty JSON object.");
                }
                // Write the JSON output to the FlowFile
                flowFile = session.write(flowFile,
                        out -> out.write(jsonOutput.toString().getBytes(StandardCharsets.UTF_8)));
                logger.info("Common Json Output Generated Successfully...!!!");
                /*
                 * if (messageType.equals("SIU") || messageType.equals("ORM") ||
                 * (messageType.equals("ORU") && triggerEvent.equals("R01"))) {
                 * 
                 * logger.info("FlowFile in Else loop, MsgType : " + messageType +
                 * ", Event Trigger" + triggerEvent);
                 * jsonOutput = HL7toJson(hl7Message, context);
                 * // Check if jsonOutput is empty or null
                 * if (jsonOutput == null || jsonOutput.size() == 0) {
                 * throw new ProcessException("Conversion resulted in an empty JSON object.");
                 * }
                 * getLogger().info("HL7 Message: " + hl7Message);
                 * logger.info("JsonOut:" + jsonOutput);
                 * // Write the JSON output to the FlowFile
                 * flowFile = session.write(flowFile,
                 * out -> out.write(jsonOutput.toString().getBytes(StandardCharsets.UTF_8)));
                 * 
                 * } else {
                 * jsonOutput = ParseHL7Messages.parseMessageToJson(hl7Message, context,
                 * messageType, triggerEvent);
                 * 
                 * // Check if jsonOutput is empty or null
                 * if (jsonOutput == null || jsonOutput.size() == 0) {
                 * throw new ProcessException("Conversion resulted in an empty JSON object.");
                 * }
                 * // getLogger().info("HL7 Message: " + hl7Message);
                 * // Write the JSON output to the FlowFile
                 * flowFile = session.write(flowFile,
                 * out -> out.write(jsonOutput.toString().getBytes(StandardCharsets.UTF_8)));
                 * 
                 * }
                 */

                // Retrieve the MIME type from the processor's properties
                String mimeType = context.getProperty(MIME_TYPE).getValue();

                // Set the MIME type attribute
                flowFile = session.putAttribute(flowFile, "mime.type", mimeType);

                // Transfer the FlowFile to the SUCCESS relationship
                session.transfer(flowFile, SUCCESS);

            } catch (Exception e) {
                // In case of an error, transfer FlowFile to failure relationship
                if (flowFile != null) {
                    session.transfer(flowFile, FAILURE);
                }
                getLogger().error("Processing failed due to: ", e);
            }

        } catch (IOException | FlowFileAccessException | ProcessException e) {
            getLogger().error("Error processing flow file: " + e.getMessage(), e);
            // logger.error("Failed to convert HL7 to JSON" , e);
            // Penalize the FlowFile and transfer it to FAILURE
            flowFile = session.penalize(flowFile);
            session.transfer(flowFile, FAILURE);
            // In case of an error, transfer FlowFile to failure relationship
            if (flowFile != null) {
                session.transfer(flowFile, FAILURE);
            }
            getLogger().error("Processing failed due to: ", e);
        }
    }

    // Helper method to parse the mapping string
    private Map<String, String> parseMappingString(String mappingStr) {
        Map<String, String> map = new LinkedHashMap<>();
        String[] mappings = mappingStr.split(",");
        for (String mapping : mappings) {
            String[] parts = mapping.split("=");
            if (parts.length == 2) {
                map.put(parts[0].trim(), parts[1].trim());
            }
        }
        return map;
    }

    // Helper method to read content from a FlowFile
    private String readFlowFileContent(ProcessSession session, FlowFile flowFile) throws IOException {
        final StringBuilder sb = new StringBuilder();
        session.read(flowFile, in -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
        });
        return sb.toString();
    }

    // Helper method to parse segment mappings
    private static Map<String, String> parseSegmentMapping(String mappingProperty) {
        Map<String, String> mapping = new LinkedHashMap<>();
        if (mappingProperty != null && !mappingProperty.trim().isEmpty()) {
            String[] entries = mappingProperty.split(",");
            for (String entry : entries) {
                String[] keyValue = entry.split("=");
                if (keyValue.length == 2) {
                    mapping.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }
        }
        return mapping;
    }

    /*
     * private static JsonObject parseMessageToJson(String hl7message,
     * ProcessContext context, String msg_type,
     * String trigger_event) throws ClassNotFoundException, HL7Exception {
     * JsonObject jsonObject = new JsonObject();
     * Parser parser = new PipeParser();
     * Message message = parser.parse(hl7message);
     * // Extract MSH segment
     * // MSH mshSegment = (MSH) message.get("MSH");
     * // Retrieve the segment mapping property
     * String segmentMappingProperty =
     * context.getProperty(SEGMENT_MAPPING).getValue();
     * Map<String, String> segmentMapping =
     * parseSegmentMapping(segmentMappingProperty);
     * 
     * String msg_version = message.getVersion();
     * 
     * for (String segmentName : message.getNames()) {
     * String customSegmentName = segmentMapping.getOrDefault(segmentName,
     * segmentName);
     * logger.info("Processing segment: " + customSegmentName);
     * 
     * Class<?> segmentClass = getnewSegmentClass(segmentName, msg_type,
     * trigger_event, msg_version);
     * logger.info("ClassName Returned as : " + segmentClass);
     * JsonArray segmentArray = new JsonArray();
     * 
     * for (Structure structure : message.getAll(segmentName)) {
     * if (structure instanceof Segment) {
     * Segment segment = (Segment) structure;
     * JsonObject segmentJson = new JsonObject();
     * int fieldNum = 1;
     * 
     * while (fieldNum <= segment.numFields()) {
     * try {
     * Type[] fieldRepetitions = segment.getField(fieldNum);
     * logger.info("SegGlass " + segmentClass + ", segmentName: " + segmentName +
     * ", fieldNum "
     * + fieldNum);
     * String methodName = findMethodNameForSubfield(segmentClass, segmentName,
     * Integer.toString(fieldNum));
     * JsonArray fieldArray = new JsonArray();
     * 
     * if (methodName != null) {
     * logger.info("Main Method Name for " + fieldNum + ": " + methodName);
     * // System.out.println("Main Method Name for " + fieldNum + ": " +
     * methodName);
     * } else {
     * logger.info("No main method found for field: " + fieldNum);
     * // System.out.println("No main method found for field: " + fieldNum);
     * }
     * 
     * for (Type field : fieldRepetitions) {
     * String fieldValue = field.encode().trim();
     * // Get the data type class name
     * String dataType = field.getClass().getSimpleName();
     * // if (dataType.contains("TS") || dataType.contains("TSComponentOne") ||
     * // dataType.contains("DTM") || dataType.contains("DT")) {
     * // fieldValue = convertTimestamp(fieldValue);
     * // }
     * 
     * JsonObject fieldObject = new JsonObject();
     * if (methodName != null) {
     * if (!fieldValue.isEmpty()) {
     * if (fieldRepetitions.length == 1) {
     * // Single value
     * logger.info("M--Method Name for " + fieldNum + ": " + methodName
     * + "dataType :" + dataType);
     * // System.out.println("M--Method Name for " + fieldNum + ": " + methodName +
     * // "dataType :" + dataType);
     * // if (dataType.contains("TS") || dataType.contains("TSComponentOne") ||
     * // dataType.contains("DTM") || dataType.contains("DT")) {
     * // fieldValue = convertTimestamp(fieldValue);
     * // }
     * dataType = field.getClass().getSimpleName();
     * if (dataType.contains("TQ") || dataType.contains("TS")
     * || dataType.contains("TSComponentOne") || dataType.contains("DTM")
     * || dataType.contains("DT")) {
     * fieldValue = convertTimestamp(fieldValue);
     * }
     * String underscore = camelToUnderscore(methodName.split("_")[1]);
     * 
     * segmentJson.addProperty(underscore, fieldValue);
     * logger.info("Repeated elem : " + fieldValue);
     * // System.out.println("Repeated elem : " + fieldValue);
     * // Extract components and subcomponents
     * String[] subComponents = field.encode().split("\\^");
     * for (int i = 0; i < subComponents.length; i++) {
     * 
     * String submethodName = findMethodNameForSubfield(segmentClass,
     * segmentName, fieldNum + "." + (i + 1));
     * 
     * if (submethodName != null) {
     * System.out.println(
     * "SubMethod Name for " + (i + 1) + ": " + submethodName);
     * dataType = field.getClass().getSimpleName();
     * if (dataType.contains("TQ") || dataType.contains("TS")
     * || dataType.contains("TSComponentOne")
     * || dataType.contains("DTM") || dataType.contains("DT")) {
     * subComponents[i] = convertTimestamp(subComponents[i]);
     * }
     * 
     * // fieldObject.addProperty(submethodName.split("_")[1],
     * // subComponents[i]);
     * underscore = camelToUnderscore(submethodName.split("_")[1]);
     * fieldObject.addProperty(underscore, subComponents[i]);
     * 
     * } else {
     * // System.out.println("No method found for field: " + i);
     * }
     * }
     * 
     * }
     * 
     * if (fieldObject.size() > 0) {
     * logger.info("sub object : " + fieldObject);
     * System.out.println("sub object : " + fieldObject);
     * // fieldArray.add(fieldObject);
     * // segmentJson.add(methodName.split("_")[1], fieldObject);
     * String underscore = camelToUnderscore(methodName.split("_")[1]);
     * segmentJson.add(underscore, fieldObject);
     * 
     * }
     * 
     * } else {
     * }
     * 
     * }
     * 
     * }
     * 
     * // if (fieldArray.size() > 0) {
     * // segmentJson.add(methodName.split("_")[1], fieldArray);
     * // }
     * 
     * fieldNum++;
     * } catch (HL7Exception e) {
     * logger.info("Error processing field " + fieldNum + " in segment " +
     * segmentName);
     * // System.out.println("Error processing field " + fieldNum + " in segment " +
     * // segmentName);
     * break;
     * }
     * }
     * 
     * segmentArray.add(segmentJson);
     * }
     * }
     * 
     * jsonObject.add(customSegmentName, segmentArray);
     * }
     * return applyMappingRecursively(jsonObject, segmentMapping);
     * // return jsonObject;
     * }
     */

    public JsonObject HL7toJson(String hl7Message, ProcessContext context) throws ClassNotFoundException {
        Gson gson = new Gson();
        Map<String, JsonArray> jsonMap = new LinkedHashMap<>();

        // Retrieve the segment mapping property
        String segmentMappingProperty = context.getProperty(SEGMENT_MAPPING).getValue();
        Map<String, String> segmentMapping = parseSegmentMapping(segmentMappingProperty);

        // getLogger().info("Source FlowFile Data : " + hl7Message + ", context: " +
        // context);

        try {
            Parser parser = new PipeParser();
            Message message = parser.parse(hl7Message);
            // Extract the version number
            String version = message.getVersion();
            version_no = "v" + version.replace(".", "");
            // getLogger().info("version MSH : " + version_no);

            // Extract the MSH segment
            MSH mshSegment = (MSH) message.get("MSH");
            // Extract the message type from MSH-9
            String messageType = mshSegment.getMessageType().getMessageType().getValue();
            String triggerEvent = mshSegment.getMessageType().getTriggerEvent().getValue();

            String className = "ca.uhn.hl7v2.model." + version_no + ".message." + messageType + "_" + triggerEvent;

            // Use reflection to instantiate the class dynamically
            Class<?> clazz = Class.forName(className);
            Message messageInstance = (Message) clazz.cast(message);

            JsonObject jsonOutput = new JsonObject();

            JsonObject jsonObject = new JsonObject();

            if (messageType.equals("ADT")) {
                jsonOutput = (JsonObject) messageTypeClass.ADT(messageType, triggerEvent, message);

            }

            if (messageType.equals("SIU")) {
                jsonOutput = (JsonObject) messageTypeClass.SIU(messageType, triggerEvent, message);
               // logger.info("JSONOUT : " + jsonObject);

            }

            if (messageType.equals("ORM")) {
                jsonOutput = (JsonObject) messageTypeClass.ORM(messageType, triggerEvent, message);
               // logger.info("JSONOUT : " + jsonObject);

            }

            if (messageType.equals("ORU")) {
                jsonOutput = (JsonObject) messageTypeClass.ORU(messageType, triggerEvent, message);
               // logger.info("JSONOUT : " + jsonObject);

            }

            if (messageType.equals("MFN")) {
                jsonOutput = (JsonObject) messageTypeClass.MFN(messageType, triggerEvent, message);

            }

            if (messageType.equals("DFT")) {
                jsonOutput = (JsonObject) messageTypeClass.DFT(messageType, triggerEvent, message);

            }

            // Process each structure in the message (segments or groups)
            for (String structureName : message.getNames()) {
                String customSegmentName = segmentMapping.getOrDefault(structureName, structureName);
                // getLogger().info("Processing segment: " + customSegmentName);

                Structure[] structures = message.getAll(structureName);
                JsonArray structureArray = new JsonArray();

                for (Structure structure : structures) {
                    
                    switch (structure) {
                        case Segment segment -> {
                            Class<?> segmentClass = SegmentClassName.getSegmentClass(segment.getName());
                            if (segmentClass != null) {
                                Map<String, Object> fieldNames = FieldNameExtraction.getFieldNames(segment,
                                        segmentClass);
                                JsonObject segmentJson = gson.toJsonTree(fieldNames).getAsJsonObject();
                                structureArray.add(segmentJson);
                                //jsonObject.add("MSH", segmentJson);
                                ///logger.info("JSONINFO : " +  jsonObject);

                            }
                        }
                        case Group group -> {

                            JsonObject groupJson = processGroup(group);
                            structureArray.add(groupJson);

                        }
                        default -> {
                        }

                    }

                }
                jsonMap.put(customSegmentName, structureArray);

                // jsonMap.put(structureName, structureArray);

                // If there are no instances, you can choose to skip adding the segment to
                // jsonObject.
            }
            JsonObject resultJson = gson.toJsonTree(jsonMap).getAsJsonObject();
        //    logger.info("Each Element Array Object : " + resultJson);

            return applyMappingRecursively(resultJson, segmentMapping);

            // return gson.toJsonTree(jsonMap).getAsJsonObject();
        } catch (HL7Exception e) {
            getLogger().error("Error processing HL7 message", e);
            return new JsonObject();
        }
    }

    // Method to dynamically get the segment class using reflection
    private static Class<?> getSegmentClass(String segmentName) {
        try {
            // Construct the fully qualified class name
            String packageName = "ca.uhn.hl7v2.model.v23.segment"; // Replace with your actual package
            String className = packageName + "." + segmentName;
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to dynamically get the segment class using reflection
    private static Class<?> getnewSegmentClass(String segmentName, String msg_type, String event_trigger,
            String msgversion) {
        try {
            String packageName;

        //    logger.info("In GetSement_Class -> msg_type : " + msg_type + ", triggerevent :  " + event_trigger
        //            + ", Version No::::: " + msgversion);

            // Construct the fully qualified class name
            if (msg_type.equals("ORU") && event_trigger.equals("R30")
                    || msg_type.equals("ORU") && event_trigger.equals("R32")
                    || msg_type.equals("ORU") && event_trigger.equals("R40")
                    || msg_type.equals("ORU") && event_trigger.equals("R42")
                    || msg_type.equals("MFN") && event_trigger.equals("M02")
                    || msg_type.equals("ORU") && event_trigger.equals("R01") || msg_type.equals("DFT")) {
                if (msgversion.equals("23")) {
                    packageName = "ca.uhn.hl7v2.model.v23.segment"; // Replace with your actual package
                } else {
                    packageName = "ca.uhn.hl7v2.model.v" + msgversion.replace(".", "") + ".segment";
                }
            //    logger.info("R30 - Pacakage Selected as : " + packageName);
                String className = packageName + "." + segmentName;
                // String className = packageName + ".MSH";

            //    logger.info("Final - ClassName Selected as : " + className);
                return Class.forName(className);
            } else {
                packageName = "ca.uhn.hl7v2.model.v" + msgversion.replace(".", "") + ".segment"; // Replace with your
                                                                                                 // actual package
            //    logger.info("Else - Pacakage Selected as : " + packageName);
                String className = packageName + "." + segmentName;

            //    logger.info("ELSE Final - ClassName Selected as : " + className);
                return Class.forName(className);

            }

        } catch (ClassNotFoundException e) {
            logger.error("Class not found for: " + e);
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unused")
    public static void processSegment(Segment segment, String segmentName, JsonObject jsonObject)
            throws HL7Exception {
        JsonObject segmentJson = new JsonObject();
        // SegmentClassName segmentclassname = new SegmentClassName();
        // FieldNameExtraction fieldnameextraction = new FieldNameExtraction();

        // Retrieve segment class dynamically
        Class<?> segmentClass = getSegmentClass(segment.getName());
        if (segmentClass != null) {
            Map<String, Object> fieldNames = FieldNameExtraction.getFieldNames(segment, segmentClass);
            // System.out.println("Main-fieldName: " + fieldNames);

            for (Map.Entry<String, Object> entry : fieldNames.entrySet()) {
                if (entry.getValue() instanceof Map) {
                    JsonObject nestedObject = new JsonObject();
                    @SuppressWarnings("unchecked")
                    Map<String, Object> nestedMap = (Map<String, Object>) entry.getValue();
                    for (Map.Entry<String, Object> nestedEntry : nestedMap.entrySet()) {
                        String key = nestedEntry.getKey();

                        // String underscore = HelperClass.camelToUnderscore(key);

                        // logger.info("UnderScore Key : " + underscore);

                        nestedObject.addProperty(nestedEntry.getKey(), (String) nestedEntry.getValue());

                        // nestedObject.addProperty(HelperClass.camelToUnderscore(nestedEntry.getKey()),
                        // (String) nestedEntry.getValue());
                    }

                    segmentJson.add(entry.getKey(), nestedObject);
                    // logger.info("IF - Segment : " + entry.getKey() + ", Value : " +
                    // nestedObject);

                } else {
                    // logger.info("ELSE - Segment : " + entry.getKey() + ", Value : " +
                    // entry.getValue());
                    segmentJson.addProperty(entry.getKey(), (String) entry.getValue());
                }
            }
            jsonObject.add(segmentName, segmentJson);

        }

        // jsonObject.add(segmentName, segmentJson);

        // logger.info("Each Element Array Object : " + jsonObject);
        // return jsonObject;
    }

    private static void OLLLL_processSegment(Segment segment, String segmentName, JsonObject jsonObject)
            throws HL7Exception {
        JsonObject segmentJson = new JsonObject();

        // Retrieve segment class dynamically
        Class<?> segmentClass = getSegmentClass(segment.getName());
        if (segmentClass != null) {
            Map<String, Object> fieldNames = getFieldNames(segment, segmentClass);
            for (Map.Entry<String, Object> entry : fieldNames.entrySet()) {
                if (entry.getValue() instanceof Map) {
                    JsonObject nestedObject = new JsonObject();
                    Map<String, Object> nestedMap = (Map<String, Object>) entry.getValue();
                    for (Map.Entry<String, Object> nestedEntry : nestedMap.entrySet()) {
                        nestedObject.addProperty(nestedEntry.getKey(), (String) nestedEntry.getValue());
                    }
                    segmentJson.add(entry.getKey(), nestedObject);
                } else {
                    segmentJson.addProperty(entry.getKey(), (String) entry.getValue());
                }
            }
        }

        jsonObject.add(segmentName, segmentJson);
    }

    // Recursive method to apply mapping to nested JSON objects
    private static JsonObject applyMappingRecursively(JsonObject jsonObject, Map<String, String> segmentMapping) {
        JsonObject mappedJson = new JsonObject();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            String mappedKey = segmentMapping.getOrDefault(key, key);

            if (value.isJsonObject()) {
                mappedJson.add(mappedKey, applyMappingRecursively(value.getAsJsonObject(), segmentMapping));
            } else if (value.isJsonArray()) {
                JsonArray mappedArray = new JsonArray();
                for (JsonElement element : value.getAsJsonArray()) {
                    if (element.isJsonObject()) {
                        mappedArray.add(applyMappingRecursively(element.getAsJsonObject(), segmentMapping));
                    } else {
                        mappedArray.add(element);
                    }
                }
                mappedJson.add(mappedKey, mappedArray);
            } else {
                mappedJson.add(mappedKey, value);
            }
        }
     //   logger.info("mappedJson : " + mappedJson);
        return mappedJson;
    }

    private JsonObject processGroup(Group group) {
        Gson gson = new Gson();
        JsonObject groupJson = new JsonObject();
        boolean hasValues = false; // Track if this segment has any values

        try {
            // Iterate over the structures in the group
            for (String structureName : group.getNames()) {
                Structure[] structures = group.getAll(structureName);
                JsonArray structureArray = new JsonArray();

                for (Structure structure : structures) {
                    switch (structure) {
                        case Segment segment -> {
                            Class<?> segmentClass = getSegmentClass(segment.getName());
                            if (segmentClass != null) {
                                Map<String, Object> fieldNames = getFieldNames(segment, segmentClass);
                                JsonObject segmentJson = gson.toJsonTree(fieldNames).getAsJsonObject();
                                structureArray.add(segmentJson);
                                // logger.info("structureArray : " + structureArray );
                            }
                        }
                        case Group group1 -> {
                            // Recursively process nested groups
                            JsonObject nestedGroupJson = processGroup(group1);
                            structureArray.add(nestedGroupJson);
                        }
                        default -> {
                        }
                    }
                }

                groupJson.add(structureName, structureArray);
                hasValues = true; // Mark that we have values

            }
        } catch (HL7Exception e) {
            getLogger().error("Error processing group", e);
        }

        return groupJson;
    }

    // Function to retrieve field and subfield names from a segment

    private static Map<String, Object> getFieldNames(Segment segment, Class<?> segmentClass) {
        Map<String, Object> fieldNames = new LinkedHashMap<>();

        try {
            int numFields = segment.numFields();
            // System.out.println("Segment " + segment.getName() + " has " + numFields + "
            // fields.");

            for (int i = 1; i <= numFields; i++) {
                Type[] fields = segment.getField(i);
                if (fields.length == 0) {
                    continue;
                }

                // for (int i = 1; i <= numFields; i++) {
                // Type[] fields = segment.getField(i);
                String fieldIdentifier = Integer.toString(i).trim();
                String methodName = findMethodNameForSubfield(segmentClass, segment.getName(), fieldIdentifier);

                // .findMethodNameForSubfield(segmentClass, segment.getName(), fieldIdentifier);
            //    logger.info("Segment " + segment.getName() + " field " + i + " has " + fields.length + " subfields.");

                if (methodName != null && fields.length > 0) {
                    String key = methodName.split("_")[1];
                //    logger.info("methodname : " + methodName + ", obx-key : " + key + ", obx-5 value : "
                  //          + fields[0].encode());
                    if (methodName.contains("ObservationValue")) {
                        String underscore = camelToUnderscore(methodName.split("_")[1]);
                        fieldNames.put(underscore, fields[0].encode());

                    }
                } else {
                    // Handle the case where the methodName is null or fields array is empty
                    logger.warn("No method found for field: " + fieldIdentifier + " or fields array is empty");
                }

                for (Type field : fields) {
                    switch (field) {
                        case Composite composite -> {
                            Map<String, Object> compositeFields = new LinkedHashMap<>();
                            Type[] components = composite.getComponents();
                            for (int k = 0; k < components.length; k++) {
                                Type component = components[k];
                                String componentIdentifier = Integer.toString(i) + "." + Integer.toString(k + 1);
                                // System.out.println("comp no: " + componentIdentifier);

                                String submethodName = findMethodNameForSubfield(segmentClass, segment.getName(),
                                        componentIdentifier);
                                if (component instanceof Primitive primitive) {
                                    String primitiveName = primitive.getName();
                                    String value = primitive.encode();
                                    if (segment.getName().equals("SCH")) {
                                    //    logger.info(
                                      //          "componentidentifer : " + componentIdentifier + ", value : " + value);
                                    }

                                    if ((primitiveName.contains("DT") || primitiveName.contains("TQ")
                                            || primitiveName.contains("TS") || primitiveName.contains("DTM")
                                            || primitiveName.contains("DT"))) {
                                        value = convertTimestamp(value);
                                    }
                                    // compositeFields.put(submethodName.split("_")[1] , value);
                                    // compositeFields.put(componentIdentifier + " (" + primitiveName + ")" + "["+
                                    // submethodName.split("_")[1] + "]", value);
                                    if (submethodName != null) {
                                        String underscore = camelToUnderscore(submethodName.split("_")[1]);
                                        compositeFields.put(underscore, value);
                                    }
                                }
                            }
                            if (methodName != null) {
                                String underscore = camelToUnderscore(methodName.split("_")[1]);
                                fieldNames.put(underscore, compositeFields);
                            }
                            // fieldNames.put(methodName.split("_")[1], compositeFields);
                        }
                        case Primitive primitive -> {
                            // getLogger().info("Field " + i + " content: " + field.encode());
                            String primitiveName = primitive.getName();
                            String value = primitive.encode();
                            if ((primitiveName.contains("DT") || primitiveName.contains("TQ")
                                    || primitiveName.contains("TS") || primitiveName.contains("DTM")
                                    || primitiveName.contains("DT"))) {
                                value = convertTimestamp(value);
                            }
                            if (methodName != null) {
                                String underscore = camelToUnderscore(methodName.split("_")[1]);
                                fieldNames.put(underscore, value);
                            }
                            // fieldNames.put(methodName.split("_")[1], value);
                        }
                        default -> {
                        }
                    }
                }
            }
        } catch (HL7Exception e) {
            // getLogger().info("HL7 Exception : " + e.getMessage());

        }

        return fieldNames;
    }

    // Convert CamelCase to Underscore
    public static String camelToUnderscore(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }

        // Replace capital letters with an underscore followed by the lowercase letter
        String underscore = camelCase.replaceAll("([a-z])([A-Z])", "$1_$2");

        // Convert the entire string to uppercase
        return underscore.toLowerCase();
    }

    static String findMethodNameForSubfield(Class<?> segmentClass, String segName, String fieldIdentifier) {
        try {
            Method[] methods = segmentClass.getDeclaredMethods();

            // Split the identifier to find field and subfield numbers
            String[] parts = fieldIdentifier.split("\\.");
            String fieldNumber = parts[0];
            String subfieldNumber = parts.length > 1 ? parts[1] : null;

            for (Method method : methods) {
                // Check if the method corresponds to the field
            //    logger.info("methods" + method + ", fieldid :  " + fieldIdentifier);
                if (method.getName().toLowerCase().contains(segName.toLowerCase() + fieldNumber + "_")) {
                    // If it's a composite, search for the subfield method
                    if (subfieldNumber != null) {
                        // Check if the return type is another class representing the composite
                        Class<?> returnType = method.getReturnType();
                        Method[] compositeMethods = returnType.getDeclaredMethods();

                        for (Method compositeMethod : compositeMethods) {
                            if (compositeMethod.getName().toLowerCase().contains(subfieldNumber + "_")) {
                                return compositeMethod.getName();
                            }
                        }
                    } else {
                        // Return the method name if there's no subfield
                        return method.getName();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Function to convert HL7 timestamp to desired format
    private static String convertTimestamp(String hl7Timestamp) {
        SimpleDateFormat hl7Format = new SimpleDateFormat("yyyyMMddHHmm");
        SimpleDateFormat hl7DtFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat desiredFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat desiredDtFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (hl7Timestamp.length() != 0) {
                if (hl7Timestamp.length() > 8) {
                    Date date = hl7Format.parse(hl7Timestamp);
                    return desiredFormat.format(date);
                } else {
                    Date date = hl7DtFormat.parse(hl7Timestamp);
                    return desiredDtFormat.format(date);
                }
            }

        } catch (ParseException e) {

            return hl7Timestamp; // Return original if parsing fails
        }
        return hl7Timestamp;
    }

    public static String getMethodFieldNames(Class<?> segmentClass, String segmentName, String fieldIdentifier) {
        try {
            // String mainFieldName = null;
            Method[] methods = segmentClass.getDeclaredMethods();

            // String[] parts = fieldIdentifier.split('\\.');
            String[] parts = fieldIdentifier.split("\\.");
            String fieldNumber = parts[0];
            String subfieldNumber = parts.length > 1 ? parts[1] : null;
            // System.out.println("Field Number " + fieldNumber + ", SubField Number " +
            // subfieldNumber );
            for (Method method : methods) {
                // mainFieldName = method.getName();
                if (method.getName().toLowerCase().contains(segmentName.toLowerCase() + fieldNumber + "_")) {
                    if (subfieldNumber != null) {
                        // Class<?> dataType = method.getReturnType();
                        Class<?> returnType = method.getReturnType();
                        Method[] compositeMethods = returnType.getDeclaredMethods();

                        for (Method compositeMethod : compositeMethods) {
                            if (compositeMethod.getName().toLowerCase().contains(subfieldNumber + "_")) {
                                // System.out.println("Subfield Name " + compositeMethod.getName() );
                                // String subfieldName = compositeMethod.getName();
                                // String compmethod = compositeMethod.getName() != null ?
                                // compositeMethod.getName().split("_")[1]: "Field_";

                                return compositeMethod.getName();
                                // return compmethod;
                            }
                        }
                        // return method.getName();
                    } else {
                        // System.out.println("Field Name " + method.getName());
                        // String mainmethod = method.getName() != null ? method.getName().split("_")[1]
                        // : "Field_";
                        return method.getName();
                        // return mainmethod;

                    }

                    // System.out.println("FieldName_Method : " + method.getName());
                }

            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

}
