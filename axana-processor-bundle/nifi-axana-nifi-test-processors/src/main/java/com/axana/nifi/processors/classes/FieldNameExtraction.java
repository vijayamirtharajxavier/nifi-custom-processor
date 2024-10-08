package com.axana.nifi.processors.classes;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Composite;
import ca.uhn.hl7v2.model.Primitive;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Type;

public class FieldNameExtraction {
    // Function to retrieve field and subfield names from a segment

    public static Map<String, Object> getFieldNames(Segment segment, Class<?> segmentClass) {
        Map<String, Object> fieldNames = new LinkedHashMap<>();
        Type[] fields;
        // this.logger = getLogger(); // Store the logger in an instance variable

        // ExtractSegmentMethod extratsegmentMethod = new ExtractSegmentMethod();
        try {
            int numFields = segment.numFields();
            for (int i = 1; i <= numFields; i++) {
                fields = segment.getField(i);
                if (fields.length == 0) {
                    continue;
                }
                String fieldIdentifier = Integer.toString(i).trim();
                // if(segment.getName().equals("SCH") && fieldIdentifier.equals("11"))
                // {
                String methodName = getMethodFieldNames(segmentClass, segment.getName(),
                        fieldIdentifier);
                // System.out.println("Processing field for segment " + segment.getName() + " :
                // " + i + " , FieldName : "
                // + methodName + ", Value : " + fields[0].encode());

                // if(methodName.equals("getObx5_ObservationValue"))
                // {
                /// System.out.println("ccccc - " + segment.getName() + " : " + i + " ,
                /// FieldName : " + methodName
                // + ", Value : " + fields[0].encode());
                // String key = methodName != null ? methodName.split("_")[1] : "Field_" + i;
                String key = methodName != null ? methodName.split("_")[1] : "Field_" + i;
                String underscore = HelperClass.camelToUnderscore(key);

                fieldNames.put(underscore, fields[0].encode());

                // }

                // String methodName = findMethodNameForSubfield(segmentClass,
                // segment.getName(), Integer.toString(i).trim());
                String primitiveName = null;
                String value = null;
                Primitive primitive = null;
                // DateTimeFormatConversion datetimeformatconversion = new
                // DateTimeFormatConversion();
                for (Type field : fields) {
                    if (field instanceof Composite) {
                        Composite composite = (Composite) field;
                        Map<String, Object> compositeFields = new LinkedHashMap<>();
                        Type[] components = composite.getComponents();
                        for (int k = 0; k < components.length; k++) {
                            // Type comp_val = composite.getComponent(k);
                            // composite.data[3]
                            Type component = components[k];
                            String componentIdentifier = Integer.toString(i) + "." + Integer.toString(k + 1);
                            // String submethodName = findMethodNameForSubfield(segmentClass,
                            // segment.getName(), componentIdentifier);
                            // String submethodName = getMethodFieldNames(segmentClass, segment.getName(),
                            // k+1);
                            String submethodName = getMethodFieldNames(segmentClass,
                                    segment.getName(), componentIdentifier);

                            if (component instanceof Primitive) {
                                primitive = (Primitive) component;
                                primitiveName = primitive.getName();
                                value = primitive.encode();
                                // String ele_val = primitive.getValue();

                                // System.out.println("primitive: " + primitive + ", primitiveName : " +
                                // primitiveName
                                // + ", value : " + value);

                                if (primitiveName.contains("TS") || primitiveName.contains("TSComponentOne")
                                        || primitiveName.contains("DTM") || primitiveName.contains("DT")) {
                                    value = DateTimeFormatConversion.convertTimestamp(value);
                                }
                                // System.out.println("aaaa :" + componentIdentifier);
                                // System.out.println(
                                // "aaaa :" + componentIdentifier + ", Sub Comp : " +
                                // submethodName.split("_")[1]
                                // + ", Values : " + value + ", DataType : " + primitiveName);

                                // System.out.println("methodname : " + methodName +",componentIdentifier: " +
                                // componentIdentifier + ", value : " + value);
                                String subkey = submethodName != null ? submethodName.split("_")[1] : "";
                                underscore = HelperClass.camelToUnderscore(subkey);
                                compositeFields.put(underscore, value);
                                // compositeFields.put(submethodName != null ? submethodName.split("_")[1] : "",
                                // value);
                                // compositeFields.put(componentIdentifier + " (" + primitiveName + ")" +
                                // (submethodName != null ? "[" + submethodName.split("_")[1] + "]" : ""),
                                // value);
                            } else {
                                Type comp_val = composite.getComponent(k);
                                if (comp_val.getName().contains("TS") || comp_val.getName().contains("TSComponentOne")
                                        || comp_val.getName().contains("DTM") || comp_val.getName().contains("DT")) {
                                    value = DateTimeFormatConversion.convertTimestamp(comp_val.encode());
                                }

                                // System.out.println(
                                // "bbb :" + componentIdentifier + ", Sub Comp : " + submethodName.split("_")[1]
                                // + "Values : " + comp_val.encode() + ", DataType : " + primitiveName);
                                String subkey = submethodName != null ? submethodName.split("_")[1] : "";
                                underscore = HelperClass.camelToUnderscore(subkey);
                                compositeFields.put(underscore, value);
                                // compositeFields.put(submethodName != null ? submethodName.split("_")[1] : "",
                                // value);
                                // compositeFields.put(componentIdentifier + " (" + primitiveName + ")" +
                                // (submethodName != null ? "[" + submethodName.split("_")[1] + "]" : ""),
                                // value);
                            }
                        }

                        if (!compositeFields.isEmpty()) {
                            // System.out.println("IF -Processing field for segment " + segment.getName() +
                            // " : " + i
                            // + " , FieldName : " + methodName + ", Value : " + fields[0].encode());

                            key = methodName != null ? methodName.split("_")[1] : "Field_" + i;
                            underscore = HelperClass.camelToUnderscore(key);
                            fieldNames.put(underscore, compositeFields);

                        } else {
                            // System.out.println("ELS-Processing field for segment " + segment.getName() +
                            // " : " + i
                            // + " , FieldName : " + methodName + ", Value : " + fields[0]);
                            key = methodName != null ? methodName.split("_")[1] : "Field_" + i;
                            // System.out.println("obx-val :" + key);
                            underscore = HelperClass.camelToUnderscore(key);

                            fieldNames.put(underscore, fields[0].encode());

                        }
                    } else if (field instanceof Primitive) {
                        primitive = (Primitive) field;
                        primitiveName = primitive.getName();
                        value = primitive.encode();

                        if (primitiveName.contains("TS") || primitiveName.contains("DTM")
                                || primitiveName.contains("DT") || primitiveName.contains("CM_EIP")) {
                            value = DateTimeFormatConversion.convertTimestamp(value);
                        }

                        key = methodName != null ? methodName.split("_")[1] : "Field_" + i;
                        /// System.out.println("oooo :" + key);
                        underscore = HelperClass.camelToUnderscore(key);

                        fieldNames.put(underscore, value);

                    }
                }

            }
        } catch (HL7Exception e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Index out of bounds: " + e.getMessage());
        }
        
        return fieldNames;
    }

    public static String findMethodNameForSubfield(Class<?> segmentClass, String segName, String fieldIdentifier) {
        try {
            Method[] methods = segmentClass.getDeclaredMethods();

            // Split the identifier to find field and subfield numbers
            if (fieldIdentifier.equals("5") && segName.equals("OBX")) {
                // System.out.println("Obx-val : " + fieldIdentifier);
            }
            String[] parts = fieldIdentifier.split("\\.");
            String fieldNumber = parts[0];
            String subfieldNumber = parts.length > 1 ? parts[1] : null;

            for (Method method : methods) {
                // Check if the method corresponds to the field
                if (method.getName().toLowerCase().contains(segName.toLowerCase() + fieldNumber + "_")) {
                    // If it's a composite, search for the subfield method
                    if (subfieldNumber != null) {
                        // Check if the return type is another class representing the composite
                        Class<?> returnType = method.getReturnType();
                        Method[] compositeMethods = returnType.getDeclaredMethods(); // .getDeclaredMethods();

                        for (Method compositeMethod : compositeMethods) {
                            if (compositeMethod.getName().toLowerCase().contains(subfieldNumber + "_")) {
                                return compositeMethod.getName();
                            }
                        }
                    } else {
                        // Return the method name if there's no subfield
                        return method.getName();
                    }
                } else {
                    // System.out.println("Obx-VAL : " + method.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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
