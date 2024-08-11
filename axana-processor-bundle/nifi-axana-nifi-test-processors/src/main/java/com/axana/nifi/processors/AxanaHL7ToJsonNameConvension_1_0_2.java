package com.axana.nifi.processors;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.io.OutputStreamCallback;
import org.apache.nifi.processor.util.StandardValidators;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Composite;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Primitive;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

@Tags({"HL7 to Json Converter","Extract HL7 attributes to Json Format with Naming Convention", "Set property based Mime_Type","Set property based Segment mapping name eg:PID=patients,NK1=nextofkin,MSH=message_header"})
@CapabilityDescription("This custom process will do the conversion of HL7 message of v2.3 into JSON format with naming convention of segment,element data items as per HL7 structure and Group, it uses HAPI library for Reflection of HL7 standard v2.3")
@SeeAlso({})
@ReadsAttributes({@ReadsAttribute(attribute="", description="")})
@WritesAttributes({@WritesAttribute(attribute="", description="")})
@InputRequirement(InputRequirement.Requirement.INPUT_REQUIRED)
@SupportsBatching

public class AxanaHL7ToJsonNameConvension_1_0_2 extends AbstractProcessor {




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
        .description("A comma-separated list of HL7 segment names to custom names, e.g., PID=patients,NK1=nextofkin")
        .required(false)
        .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
        .defaultValue("PID=patients,NK1=nextofkin") // Default mappings
        .build();
    
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
    protected void init(final ProcessorInitializationContext context) {
        super.init(context);
        // Initialization code here
    }

    @Override
    public void onTrigger(ProcessContext context, ProcessSession session) throws ProcessException {
        FlowFile flowFile = session.get();
        if (flowFile == null) {
            return;
        }

        try {
            // Read the content of the FlowFile
             String hl7Message = readFlowFileContent(session, flowFile);
            // Check if the message contains '\n' instead of '\r', which could happen due to improper handling
if (hl7Message.contains("<cr>")) {
     //&& !hl7Message.contains("\n")) {
    // Replace newline with carriage return if necessary
    hl7Message = hl7Message.replace("<cr>", "\r");
    

}
            getLogger().info("HL7 Message: " + hl7Message);
            // Convert HL7 message to JSON
            JsonObject jsonOutput = HL7toJson(hl7Message, context);
    
            // Write the JSON output to the FlowFile
            flowFile = session.write(flowFile, new OutputStreamCallback() {
                @Override
                public void process(OutputStream out) throws IOException {
                    out.write(jsonOutput.toString().getBytes(StandardCharsets.UTF_8));
                }
            });

        // Retrieve the MIME type from the processor's properties
        String mimeType = context.getProperty(MIME_TYPE).getValue();

        // Set the MIME type attribute
        flowFile = session.putAttribute(flowFile, "mime.type", mimeType);
            
            // Transfer the FlowFile to the SUCCESS relationship
            session.transfer(flowFile, SUCCESS);
        } catch (Exception e) {
            
            getLogger().error("Failed to convert HL7 to JSON" , e);
            session.transfer(flowFile, FAILURE);
        }
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

public JsonObject HL7toJson(String hl7Message,ProcessContext context) {
    Gson gson = new Gson();
    Map<String, JsonArray> jsonMap = new LinkedHashMap<>();
   
    // Retrieve the segment mapping property
    String segmentMappingProperty = context.getProperty(SEGMENT_MAPPING).getValue();
    Map<String, String> segmentMapping = parseSegmentMapping(segmentMappingProperty);

    try {
        Parser parser = new PipeParser();
        Message message = parser.parse(hl7Message);

        // Process each structure in the message (segments or groups)
        for (String structureName : message.getNames()) {
            String customSegmentName = segmentMapping.getOrDefault(structureName, structureName);
            getLogger().info("Processing segment: " + customSegmentName);

            Structure[] structures = message.getAll(structureName);
            JsonArray structureArray = new JsonArray();

            for (Structure structure : structures) {
                if (structure instanceof Segment) {
                    Segment segment = (Segment) structure;
                    Class<?> segmentClass = getSegmentClass(segment.getName());
                    if (segmentClass != null) {
                        Map<String, Object> fieldNames = getFieldNames(segment, segmentClass);
                        JsonObject segmentJson = gson.toJsonTree(fieldNames).getAsJsonObject();
                        structureArray.add(segmentJson);
                    }
                } else if (structure instanceof Group) {
                    Group group = (Group) structure;
                    JsonObject groupJson = processGroup(group);
                    structureArray.add(groupJson);
                }
            }
            jsonMap.put(customSegmentName, structureArray);
//            jsonMap.put(structureName, structureArray);
        }

        return gson.toJsonTree(jsonMap).getAsJsonObject();
    } catch (Exception e) {
        getLogger().error("Error processing HL7 message", e);
        return new JsonObject(); 
    }
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
                if (structure instanceof Segment) {
                    Segment segment = (Segment) structure;
                    Class<?> segmentClass = getSegmentClass(segment.getName());
                    if (segmentClass != null) {
                        Map<String, Object> fieldNames = getFieldNames(segment, segmentClass);
                        JsonObject segmentJson = gson.toJsonTree(fieldNames).getAsJsonObject();
                        structureArray.add(segmentJson);
                    }
                } else if (structure instanceof Group) {
                    // Recursively process nested groups
                    JsonObject nestedGroupJson = processGroup((Group) structure);
                    structureArray.add(nestedGroupJson);
                }
            }

            groupJson.add(structureName, structureArray);
        }
    } catch (HL7Exception e) {
        getLogger().error("Error processing group", e);
    }

    return groupJson;
}


/*
public JsonObject HL7toJson(String hl7Message) {
    Gson gson = new Gson();
    Map<String, JsonArray> jsonMap = new LinkedHashMap<>();
    try {
        
        // Replace newline with carriage return if necessary
        hl7Message = hl7Message.replace("<cr>", "\r");
        hl7Message = hl7Message.replace("\n", "\r");

        Parser parser = new PipeParser();
    
        // Validate the HL7 version and segment structure before parsing
        if (hl7Message == null || !hl7Message.startsWith("MSH")) {
            throw new HL7Exception("Invalid HL7 message format.");
        }
    
        Message message = parser.parse(hl7Message);
        
        for (String segmentName : message.getNames()) {
            getLogger().info("Processing segment: " + segmentName);
    
            Structure[] structures = message.getAll(segmentName);
            JsonArray segmentArray = new JsonArray();
    
            for (int index = 0; index < structures.length; index++) {
                if (structures[index] instanceof Segment) {
                    Segment segment = (Segment) structures[index];
                    Class<?> segmentClass = getSegmentClass(segment.getName());
    
                    if (segmentClass != null) {
                        Map<String, Object> fieldNames = getFieldNames(segment, segmentClass);
                        JsonObject segmentJson = gson.toJsonTree(fieldNames).getAsJsonObject();
                        segmentArray.add(segmentJson);
                    } else {
                        getLogger().warn("Segment class not found for segment: " + segmentName);
                    }
                } else {
                    getLogger().warn("Structure is not a Segment: " + structures[index].getClass().getName());
                }
            }
    
            jsonMap.put(segmentName, segmentArray);
        }
    
        return gson.toJsonTree(jsonMap).getAsJsonObject();
    } catch (HL7Exception e) {
        getLogger().error("HL7 Exception occurred: " + e.getMessage(), e);
        return new JsonObject(); 
    } catch (Exception e) {
        getLogger().error("Error processing HL7 message", e);
        return new JsonObject(); 
    }
        
}

*/
    // Function to retrieve field and subfield names from a segment
    private  Map<String, Object> getFieldNames(Segment segment, Class<?> segmentClass) {
        Map<String, Object> fieldNames = new LinkedHashMap<>();

        try {
            int numFields = segment.numFields();
          //  System.out.println("Segment " + segment.getName() + " has " + numFields + " fields.");

            for (int i = 1; i <= numFields; i++) {
                Type[] fields = segment.getField(i);
                String fieldIdentifier = Integer.toString(i).trim();
             //   System.out.println("main fieldId no: " + fieldIdentifier);

             //   System.out.println("fi:" + fieldIdentifier + " of " + segment.getName());
//                HL7Reflection hr = new HL7Reflection();
                // Retrieve the method name for the field
                String methodName = findMethodNameForSubfield(segmentClass, segment.getName(), fieldIdentifier);
                
                // .findMethodNameForSubfield(segmentClass, segment.getName(), fieldIdentifier);

                if (methodName != null) {
//                    System.out.println("Method Name for " + fieldIdentifier + ": " + methodName);
                } else {
  //                  System.out.println("No method found for field: " + fieldIdentifier);
                }

                for (int j = 0; j < fields.length; j++) {
                    if (fields[j] instanceof Composite) {
                        Composite composite = (Composite) fields[j];
                        Map<String, Object> compositeFields = new LinkedHashMap<>();
                        Type[] components = composite.getComponents();
                        
                        for (int k = 0; k < components.length; k++) {
                            Type component = components[k];
                            String componentIdentifier = Integer.toString(i) + "." + Integer.toString(k + 1);
                            System.out.println("comp no: " + componentIdentifier);
                            
                            String submethodName = findMethodNameForSubfield(segmentClass, segment.getName(), componentIdentifier);
                            if (component instanceof Primitive) {
                                Primitive primitive = (Primitive) component;
                                String primitiveName = primitive.getName();
                                String value = primitive.encode();
                                
                                if ((primitiveName.contains("TS") || primitiveName.contains("DTM") || primitiveName.contains("DT"))) {
                                    value = convertTimestamp(value);
                                }
                              //  compositeFields.put(submethodName.split("_")[1] , value);
                                //compositeFields.put(componentIdentifier + " (" + primitiveName + ")" + "["+ submethodName.split("_")[1] + "]", value);
                                String underscore = camelToUnderscore(submethodName.split("_")[1]);
                                compositeFields.put(underscore, value);
                            }
                        }
                        String underscore = camelToUnderscore(methodName.split("_")[1]);
                        fieldNames.put(underscore, compositeFields);

//                        fieldNames.put(methodName.split("_")[1], compositeFields);
                    } else if (fields[j] instanceof Primitive) {
                        Primitive primitive = (Primitive) fields[j];
                        
                        getLogger().info("Field " + i + " content: " + fields[j].encode());
                        String primitiveName = primitive.getName();
                        String value = primitive.encode();

                        if ((primitiveName.contains("TS") || primitiveName.contains("DTM") || primitiveName.contains("DT"))) {
                            value = convertTimestamp(value);
                        }
                        String underscore = camelToUnderscore(methodName.split("_")[1]);
                        fieldNames.put(underscore, value);

                        //fieldNames.put(methodName.split("_")[1], value);
                    }
                }
            }
        } catch (HL7Exception e) {
            e.printStackTrace();
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

    private static String findMethodNameForSubfield(Class<?> segmentClass, String segmentName, String fieldIdentifier) {
        try {
            Method[] methods = segmentClass.getDeclaredMethods();
            String[] parts = fieldIdentifier.split("\\.");
            String fieldNumber = parts[0];
            String subfieldNumber = parts.length > 1 ? parts[1] : null;
            
            for (Method method : methods) {
                if (method.getName().toLowerCase().contains(segmentName.toLowerCase() + fieldNumber)) {
                    if (subfieldNumber != null) {
                        Class<?> returnType = method.getReturnType();
                        Method[] compositeMethods = returnType.getDeclaredMethods();

                        for (Method compositeMethod : compositeMethods) {
                            if (compositeMethod.getName().toLowerCase().contains(subfieldNumber)) {
                                return compositeMethod.getName();
                            }
                        }
                    } else {
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
        SimpleDateFormat desiredFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat desiredDtFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if(hl7Timestamp.length()!=0)
            {
            if(hl7Timestamp.length()>8  )
            {
            Date date = hl7Format.parse(hl7Timestamp);
            return desiredFormat.format(date);
            }
            else
            {
                Date date = hl7DtFormat.parse(hl7Timestamp);
                return desiredDtFormat.format(date);
            }
        }
            
        } catch (ParseException e) {
            e.printStackTrace();
            return hl7Timestamp; // Return original if parsing fails
        }
        return hl7Timestamp;
    }





}
