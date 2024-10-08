package com.axana.nifi.processors.classes;

public class SegmentClassName {

    // Method to dynamically get the segment class using reflection
    public static Class<?> getSegmentClass(String segmentName, String msgversion, String msg_type,
            String event_trigger) {
        try {
            String className = null;
            String packageName;
            // Construct the fully qualified class name

            if (msg_type.equals("ORU") && event_trigger.equals("R30") ||
                    msg_type.equals("ORU") && event_trigger.equals("R40") ||
                    msg_type.equals("ORU") && event_trigger.equals("R32")) {
                packageName = "ca.uhn.hl7v2.model.v23.segment"; // Replace with your actual
                // package
            } else if (msg_type.equals("MFN") && event_trigger.equals("M02") ||
                    msg_type.equals("DFT") && event_trigger.equals("P03")) {
                packageName = "ca.uhn.hl7v2.model.v23.segment"; // Replace with your actual
                // package
            }
            /*
             * else if(msg_type.equals("SIU") ) {
             * packageName = "ca.uhn.hl7v2.model.v23.segment"; // Replace with your actual
             * //package
             * }
             */
            else {
                packageName = "ca.uhn.hl7v2.model.v" + msgversion.replace(".", "")
                        + ".segment"; // Replace with your actual package
                // System.out.println("Else - SegmentName : " + segmentName);
            }

            // System.out.println("SegmentName : " + segmentName);
            // System.out.println("pkg version: " + packageName);

            packageName = "ca.uhn.hl7v2.model.v23.segment";
            // System.out.println("Class Name - Pacakage Selected as : " + className);
            className = packageName + "." + segmentName;

            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.err.println("Exception SegmentClass Error : " + e);
            e.printStackTrace();
            return null;

        }
    }

    // Method to dynamically get the segment class using reflection
    public static Class<?> getSegmentClass(String segmentName) {
        String packageName;
        try {
            // Construct the fully qualified class name
            packageName = "ca.uhn.hl7v2.model.v23.segment"; // Replace with your actual package
            String className = packageName + "." + segmentName;

            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
