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
import ca.uhn.hl7v2.model.v23.message.SIU_S12;
import ca.uhn.hl7v2.model.v23.message.SIU_S14;
import ca.uhn.hl7v2.model.v23.message.SIU_S17;
import ca.uhn.hl7v2.model.v23.segment.MSH;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;


@Tags({ "HL7 to Json Converter", "Extract HL7 v2.3 attributes to Json Format with Naming Convention",
        "It supports the following message types : ADT, ORU, SIU",
        "version compiled for NiFi 2.0.0-M4",
        "Set property based Mime_Type",
        "Set property based Segment mapping name eg:PID=patients,NK1=nextofkin,MSH=message_header" })
@CapabilityDescription("This custom process will do the conversion of HL7 message of v2.3 into JSON format with naming convention of segment,element data items as per HL7 structure and Group, it uses HAPI library for Reflection of HL7 standard v2.3")
@SeeAlso({})
@ReadsAttributes({ @ReadsAttribute(attribute = "", description = "") })
@WritesAttributes({ @WritesAttribute(attribute = "", description = "") })
@InputRequirement(InputRequirement.Requirement.INPUT_REQUIRED)
@SupportsBatching

public class AxanaHL7ToJsonExtracter_1_0_4 extends AbstractProcessor {
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
        this.logger = getLogger();  // Store the logger in an instance variable
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


        FlowFile flowFile = session.get();
        if (flowFile == null) {
            return;
        }

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

            getLogger().info("HL7 Message: " + hl7Message);
            // Convert HL7 message to JSON
            JsonObject jsonOutput = HL7toJson(hl7Message, context);
            // Check if jsonOutput is empty or null
            if (jsonOutput == null || jsonOutput.size() == 0) {
                throw new ProcessException("Conversion resulted in an empty JSON object.");
            }
            // Write the JSON output to the FlowFile
            flowFile = session.write(flowFile,
                    out -> out.write(jsonOutput.toString().getBytes(StandardCharsets.UTF_8)));

            // Retrieve the MIME type from the processor's properties
            String mimeType = context.getProperty(MIME_TYPE).getValue();

            // Set the MIME type attribute
            flowFile = session.putAttribute(flowFile, "mime.type", mimeType);

            // Transfer the FlowFile to the SUCCESS relationship
            session.transfer(flowFile, SUCCESS);
        } catch (IOException | FlowFileAccessException | ProcessException e) {
            getLogger().error("Error processing flow file: " + e.getMessage(), e);
            // logger.error("Failed to convert HL7 to JSON" , e);
            // Penalize the FlowFile and transfer it to FAILURE
            flowFile = session.penalize(flowFile);
            session.transfer(flowFile, FAILURE);

        } catch (ClassNotFoundException ex) {
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
    private Map<String, String> parseSegmentMapping(String mappingProperty) {
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

    public JsonObject HL7toJson(String hl7Message, ProcessContext context) throws ClassNotFoundException {
        Gson gson = new Gson();
        Map<String, JsonArray> jsonMap = new LinkedHashMap<>();

        // Retrieve the segment mapping property
        String segmentMappingProperty = context.getProperty(SEGMENT_MAPPING).getValue();
        Map<String, String> segmentMapping = parseSegmentMapping(segmentMappingProperty);

        getLogger().info("Source FlowFile Data : " + hl7Message +", context: " + context);

        try {
            Parser parser = new PipeParser();
            Message message = parser.parse(hl7Message);
            // Extract the version number
            String version = message.getVersion();
            version_no = "v" + version.replace(".", "");
            getLogger().info("version MSH : " + version_no);

            // Extract the MSH segment
            MSH mshSegment = (MSH) message.get("MSH");

            // Extract the message type from MSH-9
            String messageType = mshSegment.getMessageType().getMessageType().getValue();
            String triggerEvent = mshSegment.getMessageType().getTriggerEvent().getValue();
        //   Map<String, JSONArray> jsonMap = new LinkedHashMap<>();

            

            // Dynamically determine the class name based on the message type and trigger event
//            String packageName = "ca.uhn.hl7v2.model." + version_no + ".segment"; // Replace with your actual package

            String className = "ca.uhn.hl7v2.model."+version_no+".message." + messageType + "_" + triggerEvent;

            // Use reflection to instantiate the class dynamically
            Class<?> clazz = Class.forName(className);
            Message messageInstance = (Message) clazz.cast(message);

            JsonObject jsonOutput = new JsonObject();

      
            if (messageType.equals("SIU") && triggerEvent.equals("S14")) {
                SIU_S14 parsedMessage = (SIU_S14) message;
                processSegment(parsedMessage.getMSH(), "MSH", jsonOutput);
                processSegment(parsedMessage.getSCH(), "SCH", jsonOutput);
                processSegment(parsedMessage.getPATIENT().getPID(), "PID", jsonOutput);
            } else if (messageType.equals("SIU") && triggerEvent.equals("S12")) {
                SIU_S12 parsedMessage = (SIU_S12) message;
                processSegment(parsedMessage.getMSH(), "MSH", jsonOutput);
                processSegment(parsedMessage.getSCH(), "SCH", jsonOutput);
                processSegment(parsedMessage.getPATIENT().getPID(), "PID", jsonOutput);
            } else if (messageType.equals("SIU") && triggerEvent.equals("S17")) {
                SIU_S17 parsedMessage = (SIU_S17) message;
                processSegment(parsedMessage.getMSH(), "MSH", jsonOutput);
                processSegment(parsedMessage.getSCH(), "SCH", jsonOutput);
                processSegment(parsedMessage.getPATIENT().getPID(), "PID", jsonOutput);
            }



            // Process each structure in the message (segments or groups)
            for (String structureName : message.getNames()) {
                String customSegmentName = segmentMapping.getOrDefault(structureName, structureName);
                getLogger().info("Processing segment: " + customSegmentName);

                Structure[] structures = message.getAll(structureName);
                JsonArray structureArray = new JsonArray();

                for (Structure structure : structures) {
                    switch (structure) {
                        case Segment segment -> {
                            Class<?> segmentClass = getSegmentClass(segment.getName());
                            if (segmentClass != null) {
                                Map<String, Object> fieldNames = getFieldNames(segment, segmentClass);
                                JsonObject segmentJson = gson.toJsonTree(fieldNames).getAsJsonObject();
                                structureArray.add(segmentJson);
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
            }

            

//System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(jsonOutput));
            JsonObject resultJson = gson.toJsonTree(jsonMap).getAsJsonObject();
            return applyMappingRecursively(resultJson, segmentMapping);

            // return gson.toJsonTree(jsonMap).getAsJsonObject();
        } catch (HL7Exception e) {
            getLogger().error("Error processing HL7 message", e);
            return new JsonObject();
        }
    }
    private static void processSegment(Segment segment, String segmentName, JsonObject jsonObject) throws HL7Exception 
    {
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

        return mappedJson;
    }

    private JsonObject processGroup(Group group) {
        Gson gson = new Gson();
        JsonObject groupJson = new JsonObject();

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
            }
        } catch (HL7Exception e) {
            getLogger().error("Error processing group", e);
        }

        return groupJson;
    }

    // Function to retrieve field and subfield names from a segment

    private static  Map<String, Object> getFieldNames(Segment segment, Class<?> segmentClass) {
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


            //for (int i = 1; i <= numFields; i++) {
                //Type[] fields = segment.getField(i);
                String fieldIdentifier = Integer.toString(i).trim();
                // System.out.println("main fieldId no: " + fieldIdentifier);

                // System.out.println("fi:" + fieldIdentifier + " of " + segment.getName());
                // HL7Reflection hr = new HL7Reflection();
                // Retrieve the method name for the field
                String methodName = findMethodNameForSubfield(segmentClass, segment.getName(), fieldIdentifier);

                
                
                // .findMethodNameForSubfield(segmentClass, segment.getName(), fieldIdentifier);
                logger.info("Segment " + segment.getName() + " field " + i + " has " + fields.length + " subfields.");

                if (methodName != null && fields.length > 0) {
                    String key = methodName.split("_")[1];
                    logger.info("methodname : " + methodName +", obx-key : " + key + ", obx-5 value : " + fields[0].encode());
                    if(methodName.contains("ObservationValue")) {
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
                                    if(segment.getName().equals("SCH")) {
                                        logger.info("componentidentifer : " + componentIdentifier + ", value : " + value);
                                    }
                                    
                                    if ((primitiveName.contains("TS") || primitiveName.contains("DTM")
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
                        //    getLogger().info("Field " + i + " content: " + field.encode());
                            String primitiveName = primitive.getName();
                            String value = primitive.encode();
                            if ((primitiveName.contains("TS") || primitiveName.contains("DTM")
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
          //  getLogger().info("HL7 Exception : " + e.getMessage());

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

    // Method to dynamically get the segment class using reflection
    private static  Class<?> getSegmentClass(String segmentName) {
        
        try {
            // Construct the fully qualified class name
            String packageName = "ca.uhn.hl7v2.model." + version_no + ".segment"; // Replace with your actual package
            String className = packageName + "." + segmentName;
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
         //   getLogger().info(e.getMessage());
            
            
            return null;
        }
    }

    private static String findMethodNameForSubfield(Class<?> segmentClass, String segmentName, String fieldIdentifier) {
        try {
            Method[] methods = segmentClass.getDeclaredMethods();
            String[] parts = fieldIdentifier.split("\\.");
            String fieldNumber = parts[0];
            String subfieldNumber = parts.length > 1 ? parts[1] : null;

            for (Method method : methods) {
                if (method.getName().toLowerCase().contains(segmentName.toLowerCase() + fieldNumber + "_")) {
                    if (subfieldNumber != null) {
                        Class<?> returnType = method.getReturnType();
                        Method[] compositeMethods = returnType.getDeclaredMethods();

                        for (Method compositeMethod : compositeMethods) {
                            if (compositeMethod.getName().toLowerCase().contains(subfieldNumber + "_")) {
                                return compositeMethod.getName();
                            }
                        }
                    } else {
                        return method.getName();
                    }
                }
            }
        } catch (SecurityException e) {
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

}
