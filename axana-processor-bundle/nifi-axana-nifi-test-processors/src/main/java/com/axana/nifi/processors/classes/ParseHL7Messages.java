package com.axana.nifi.processors.classes;

import java.util.Map;

import org.apache.nifi.processor.ProcessContext;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

public class ParseHL7Messages {

    public static JsonObject parseMessageToJson(String hl7message, ProcessContext context, String msg_type,
            String trigger_event,Map segmentMapping) throws ClassNotFoundException, HL7Exception {

        JsonObject jsonObject = new JsonObject();
        Parser parser = new PipeParser();
        Message message = parser.parse(hl7message);

    String msg_version = message.getVersion();
   // String segmentMappingProperty = context.getProperty(SEGMENT_MAPPING).getValue();
 //   Map<String, String> segmentMapping = parseSegmentMapping(segmentMappingProperty);


    for (String segmentName : message.getNames()) {
        Class<?> segmentClass = SegmentClassName.getSegmentClass(segmentName, msg_version, msg_type, trigger_event);
        JsonArray segmentArray = new JsonArray();
        
        if (segmentClass != null) {
            for (Structure structure : message.getAll(segmentName)) {
                if (structure instanceof Segment) {
                    Segment segment = (Segment) structure;
                    JsonObject segmentJson = new JsonObject();
                    boolean hasValues = false; // Track if this segment has any values

                    for (int fieldNum = 1; fieldNum <= segment.numFields(); fieldNum++) {
                        try {
                            Type[] fieldRepetitions = segment.getField(fieldNum);
                            String methodName = FieldNameExtraction.findMethodNameForSubfield(segmentClass,
                                    segmentName, Integer.toString(fieldNum));

                            // Iterate over field repetitions
                            for (Type field : fieldRepetitions) {
                                String fieldValue = field.encode().trim();
                                String dataType = field.getClass().getSimpleName();
                                JsonObject fieldObject = new JsonObject();

                                if (methodName != null && !fieldValue.isEmpty()) {
                                    // Split on '^' for subcomponents and handle them
                                    String[] subComponents = fieldValue.split("\\^");

                                    if(subComponents.length>1)
                                    {
                                    for (int i = 0; i < subComponents.length; i++) {

                                        String submethodName = FieldNameExtraction
                                                .findMethodNameForSubfield(segmentClass, segmentName,
                                                        fieldNum + "." + (i + 1));

                                        if (submethodName != null) {
                                          //  System.out.println(
                                           //         "SubMethod Name for " + (i + 1) + ": " + submethodName);
                                            dataType = field.getClass().getSimpleName();

                                            if (dataType.contains("TQ") || dataType.contains("TS")
                                            || dataType.contains("TSComponentOne")
                                            || dataType.contains("DTM") || dataType.contains("DT")) {
                                        fieldValue = DateTimeFormatConversion.convertTimestamp(fieldValue);
                                    }
                                            String subkey = HelperClass.camelToUnderscore(submethodName.split("_")[1]);
                                            fieldObject.addProperty(subkey,
                                                    subComponents[i]);
                                                    if (fieldObject.size() > 0) {
                                                        // System.out.println("sub object : " + fieldObject);
                                                            // fieldArray.add(fieldObject);
                                                        String key = HelperClass.camelToUnderscore(methodName.split("_")[1]);
                                                        segmentJson.add(key, fieldObject);
                                                    
        
                                                    }
                                        } else {
                                            // System.out.println("No method found for field: " + i);
                                        }
                                    }
                                }
                                else {
                                    dataType = field.getClass().getSimpleName();

                                    if (dataType.contains("TQ") || dataType.contains("TS")
                                    || dataType.contains("TSComponentOne")
                                    || dataType.contains("DTM") || dataType.contains("DT")) {
                                fieldValue = DateTimeFormatConversion.convertTimestamp(fieldValue);
                            }

                                    String key = HelperClass.camelToUnderscore(methodName.split("_")[1]);
                                    segmentJson.addProperty(key, fieldValue);
                                }
                                    
                                    // .add(methodName.split("_")[1], fieldValue);
                                }
                                  //  segmentJson.addProperty(methodName.split("_")[1], String.join(", ", subcomponents));
                                    hasValues = true; // Mark that we have values
                                }
                            
                        } catch (HL7Exception e) {
                            System.out.println("Error processing field " + fieldNum + " in segment " + segmentName);
                            break;
                        }
                    }

                    // Only add to array if there are values
                    if (hasValues) {
                        segmentArray.add(segmentJson);
                    }
                }
            }
        }

        // Determine if this segment should be an array or an object
        if (segmentArray.size() > 1) {
            // More than one instance, treat as an array
            jsonObject.add(segmentName, segmentArray);
        } else if (segmentArray.size() == 1) {
            // Only one instance, add as a single object
            jsonObject.add(segmentName, segmentArray.get(0).getAsJsonObject());
        }
        // If there are no instances, you can choose to skip adding the segment to jsonObject.
    }
    //JsonObject resultJson = gson.toJsonTree(jsonObject).getAsJsonObject();

    return applyMappingRecursively(jsonObject, segmentMapping);

   // return jsonObject;
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


    public static JsonObject oooparseMessageToJson(Message message, String msg_type, String trigger_event, String segmentMapping)
            throws HL7Exception {
        JsonObject jsonObject = new JsonObject();
        String msg_version = message.getVersion();
       // JsonObject segmentJson = new JsonObject();

        // System.out.println("All SegNames : " + message.getNames().toString());
        // SegmentClassName segmentclassname = new SegmentClassName();

        for (String segmentName : message.getNames()) {
            Class<?> segmentClass = SegmentClassName.getSegmentClass(segmentName, msg_version, msg_type, trigger_event);
            JsonArray segmentArray = new JsonArray();
            // System.out.println("segClass Name : " + segmentClass +", segName : "+
            // segmentName);

            if (segmentClass != null) {
                for (Structure structure : message.getAll(segmentName)) {
                    if (structure instanceof Segment) {
                        Segment segment = (Segment) structure;
                        JsonObject segmentJson = new JsonObject();
                        int fieldNum = 1;

                        while (fieldNum <= segment.numFields()) {
                            try {
                                Type[] fieldRepetitions = segment.getField(fieldNum);

                                String methodName = FieldNameExtraction.findMethodNameForSubfield(segmentClass,
                                        segmentName, Integer.toString(fieldNum));
                               // JsonArray fieldArray = new JsonArray();

                                if (methodName != null) {
                                    // System.out.println("Main Method Name for " + fieldNum + ": " + methodName);
                                } else {
                                    // System.out.println("No main method found for field: " + fieldNum);
                                }

                                for (Type field : fieldRepetitions) {
                                    String fieldValue = field.encode().trim();
                                    // Get the data type class name
                                    String dataType = field.getClass().getSimpleName();
                                    // if (dataType.contains("TS") || dataType.contains("TSComponentOne") ||
                                    // dataType.contains("DTM") || dataType.contains("DT")) {
                                    // fieldValue = convertTimestamp(fieldValue);
                                    // }

                                    JsonObject fieldObject = new JsonObject();
                                    if (methodName != null) {
                                        if (!fieldValue.isEmpty()) {
                                            if (fieldRepetitions.length == 1) {
                                                // Single value
                                                // System.out.println("M--Method Name for " + fieldNum + ": " +
                                                // methodName + "dataType :" + dataType);
                                                // if (dataType.contains("TS") || dataType.contains("TSComponentOne") ||
                                                // dataType.contains("DTM") || dataType.contains("DT")) {
                                                // fieldValue = convertTimestamp(fieldValue);
                                                // }
                                                dataType = field.getClass().getSimpleName();
                                                if (dataType.contains("TQ") || dataType.contains("TS")
                                                        || dataType.contains("TSComponentOne")
                                                        || dataType.contains("DTM") || dataType.contains("DT")) {
                                                    fieldValue = DateTimeFormatConversion.convertTimestamp(fieldValue);
                                                }

                                                segmentJson.addProperty(methodName.split("_")[1], fieldValue);

                                                // System.out.println("Repeated elem : " + fieldValue);
                                                // Extract components and subcomponents
                                                String[] subComponents = field.encode().split("\\^");
                                                for (int i = 0; i < subComponents.length; i++) {

                                                    String submethodName = FieldNameExtraction
                                                            .findMethodNameForSubfield(segmentClass, segmentName,
                                                                    fieldNum + "." + (i + 1));

                                                    if (submethodName != null) {
                                                      //  System.out.println(
                                                       //         "SubMethod Name for " + (i + 1) + ": " + submethodName);
                                                        dataType = field.getClass().getSimpleName();
                                                        if (dataType.contains("TQ") || dataType.contains("TS")
                                                                || dataType.contains("TSComponentOne")
                                                                || dataType.contains("DTM")
                                                                || dataType.contains("DT")) {
                                                                    fieldValue = DateTimeFormatConversion.convertTimestamp(fieldValue);

                                                        }

                                                        fieldObject.addProperty(submethodName.split("_")[1],
                                                                subComponents[i]);
                                                    } else {
                                                        // System.out.println("No method found for field: " + i);
                                                    }
                                                }

                                            }

                                            if (fieldObject.size() > 0) {
                                                // System.out.println("sub object : " + fieldObject);
                                                // fieldArray.add(fieldObject);
                                                segmentJson.add(methodName.split("_")[1], fieldObject);
                                            

                                            }

                                        } else {
                                        }

                                    }

                                }

                                // if (fieldArray.size() > 0) {
                                // segmentJson.add(methodName.split("_")[1], fieldArray);
                                // }

                                fieldNum++;
                            } catch (HL7Exception e) {
                                System.out.println("Error processing field " + fieldNum + " in segment " + segmentName);
                                break;
                            }
                            
                        }

                        segmentArray.add(segmentJson);
                    }
                    
                }
                
            }
            jsonObject.add(segmentName, segmentArray);
        }
        // System.out.println("jsonObject: " + jsonObject);
        return jsonObject;
    }



    public
    static void processSegment(Segment segment, String segmentName, JsonObject jsonObject) throws HL7Exception {
        JsonObject segmentJson = new JsonObject();
      //  SegmentClassName segmentclassname = new SegmentClassName();
      //  FieldNameExtraction fieldnameextraction = new FieldNameExtraction();

        // Retrieve segment class dynamically
        Class<?> segmentClass = SegmentClassName.getSegmentClass(segment.getName());
        if (segmentClass != null) {
            Map<String, Object> fieldNames = FieldNameExtraction.getFieldNames(segment, segmentClass);
            // System.out.println("Main-fieldName: " + fieldNames);

            for (Map.Entry<String, Object> entry : fieldNames.entrySet()) {
                if (entry.getValue() instanceof Map) {
                    JsonObject nestedObject = new JsonObject();
                    @SuppressWarnings("unchecked")
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



}
