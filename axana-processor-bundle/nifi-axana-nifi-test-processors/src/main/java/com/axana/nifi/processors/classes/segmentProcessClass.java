package com.axana.nifi.processors.classes;

import java.util.Map;

import com.google.gson.JsonObject;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Segment;

public class segmentProcessClass {
    
    @SuppressWarnings("unused")
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
