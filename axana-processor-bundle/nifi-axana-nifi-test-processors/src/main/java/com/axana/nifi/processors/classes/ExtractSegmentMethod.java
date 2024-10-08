package com.axana.nifi.processors.classes;

public class ExtractSegmentMethod {


    public static Class<?> getSegmentClass(String segmentName) {
        try {
            // Construct the fully qualified class name
            String packageName = "ca.uhn.hl7v2.model.v23.segment"; // Replace with your actual package
            String className = packageName + "." + segmentName;
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
