/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.axana.nifi.processors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
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
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.FlowFileAccessException;
import org.apache.nifi.processor.exception.ProcessException;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;

@Tags({ "HL7 Message Validator", "Parse HL7 segment validator", "Segment attributes validator" })
@CapabilityDescription("Provide a description")
@SeeAlso({})
@ReadsAttributes({ @ReadsAttribute(attribute = "", description = "") })
@WritesAttributes({ @WritesAttribute(attribute = "", description = "") })
@InputRequirement(InputRequirement.Requirement.INPUT_REQUIRED)
@SupportsBatching

public class AxanaHL7Validator_1_0_1 extends AbstractProcessor {

    public static final Relationship REL_SUCCESS = new Relationship.Builder()
            .name("SUCCESS")
            .description("Successfully processed FlowFiles")
            .build();

    public static final Relationship REL_FAILURE = new Relationship.Builder()
            .name("FAILURE")
            .description("FlowFiles that failed validation or processing")
            .build();

    @Override
    public Set<Relationship> getRelationships() {
        Set<Relationship> relationships = new HashSet<>();
        relationships.add(REL_SUCCESS);
        relationships.add(REL_FAILURE); // Ensure this is being added
        return relationships;
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
            // Check if the message contains '\n' instead of '\r', which could happen due to
            // improper handling
            if (hl7Message.contains("<cr>")) {
                // && !hl7Message.contains("\n")) {
                // Replace newline with carriage return if necessary
                hl7Message = hl7Message.replace("<cr>", "\r");
                // getLogger().info("CR-HL7: " + hl7Message);

            }
            try {
                // Validate HL7 message
                String isValid = validateHL7Message(hl7Message);
                // getLogger().info("HL7 message is valid: {}", flowFile);
                // session.transfer(flowFile, REL_SUCCESS); // Transfer FlowF
                // Transfer to success or failure based on validation result
                // Modify FlowFile attributes before transferring it
                getLogger().info("is it Valid : " + isValid);
                if (isValid != "true") {
                    flowFile = session.putAttribute(flowFile, "validation.error", "Invalid HL7 message");
                    // getLogger().info("isValid : " + flowFile);
                }

                // Transfer FlowFile to appropriate relationship based on validation result
                if (isValid == "true") {
                    session.transfer(flowFile, REL_SUCCESS);
                } else {
                    session.transfer(flowFile, REL_FAILURE);
                }
            }

            catch (Exception e) {

                getLogger().error("HL7 message validation failed", e);
                session.transfer(flowFile, REL_FAILURE);
            }

            // getLogger().info("HL7 Message: " + hl7Message);
            // Convert HL7 message to JSON

            // Transfer the FlowFile to the SUCCESS relationship
        } catch (IOException | FlowFileAccessException | ProcessException e) {
            getLogger().error("HL7 message validation failed", e);
            // Add error details to FlowFile (optional)
            flowFile = session.putAttribute(flowFile, "validation.error", e.getMessage());
            // session.transfer(flowFile, REL_FAILURE); // Transfer Flow
        }
    }

    private String validateHL7Message(String hl7Message) throws HL7Exception, IOException {
        String isSuccess = "false";
        PipeParser pipeParser = new PipeParser();

        Message message = pipeParser.parse(hl7Message); // Parse the HL7 message
        Terser terser = new Terser(message);
        if (message == null) {
            isSuccess = "false";
            throw new IllegalArgumentException("HL7 message is invalid");
        }
        try {
            // PV1 segment are ADT^A01, ADT^A03, ADT^A05, ADT^A08, ADT^A12, ADT^A13, and
            // ADT^A14.
            // No PV1 segment is included in ADT trigger events like A04 (Register a
            // Patient), A11 (Cancel Registration), A15 (Update Patient Information), and
            // sometimes in A16 (Transfer a Patient) if the transfer doesn’t involve
            // visit-related information.


            String messageType = terser.get("MSH-9-1");
            String eventTrigger = terser.get("MSH-9-2");
            getLogger().info("messageType : " + messageType);
            getLogger().info("eventTrigger : " + eventTrigger);

            if(terser.get("MSH-7")==null)
            {
                getLogger().info("Invalid MSH-7 of eventTrigger : "+ eventTrigger);
                throw new NullPointerException("Value is null for field MSH-7 Date/Time of Message ");
            }
            if(terser.get("MSH-10")==null)
            {
                getLogger().info("Invalid MSH-10 of eventTrigger : " + eventTrigger);
                throw new NullPointerException("Value is null for field MSH-10 Mesage Control ID" + eventTrigger);
            }
            if(terser.get("MSH-12")==null)
            {
                getLogger().info("Invalid MSH-12 of eventTrigger : " + eventTrigger);
                throw new NullPointerException("Value is null for field MSH-12 Version ID" + eventTrigger);
            }


            if (messageType.equals("ADT")) {
                /*
                 * MSH (Message Header)
                 * EVN (Event Type)
                 * PID (Patient Identification)
                 * PV1 (Patient Visit)
                 */
                getLogger().info("We are in messageType of : " + messageType);
                // A01,A03,A05,A08,A12,A13 & A14
                if ((eventTrigger.equals("A01"))) {
                    String adt_a01 = adt_with_pv1_checkValidate(terser, isSuccess, eventTrigger);
                    return adt_a01;
                }
                // A03 - Discharge Patient
                if (eventTrigger.equals("A03")) {
                    String adt_a03 = adt_with_pv1_checkValidate(terser, isSuccess, eventTrigger);
                    return adt_a03;

                }
                // A05 - Pre-admission
                if (eventTrigger.equals("A05")) {
                    String adt_a05 = adt_with_pv1_checkValidate(terser, isSuccess, eventTrigger);
                    return adt_a05;

                }
                // A08 - Update Patient Information
                if (eventTrigger.equals("A08")) {
                    String adt_a08 = adt_with_pv1_checkValidate(terser, isSuccess, eventTrigger);
                    return adt_a08;

                }
                // A12 - Transfer Patient
                if (eventTrigger.equals("A12")) {
                    String adt_a12 = adt_with_pv1_checkValidate(terser, isSuccess, eventTrigger);
                    return adt_a12;

                }
                // A13 - Cancel Patient Admission
                if (eventTrigger.equals("A13")) {
                    String adt_a13 = adt_with_pv1_checkValidate(terser, isSuccess, eventTrigger);
                    return adt_a13;

                }
                // A14 - Pending Admit
                if (eventTrigger.equals("A14")) {
                    String adt_a14 = adt_with_pv1_checkValidate(terser, isSuccess, eventTrigger);
                    return adt_a14;

                }


                // A04 - Register a Patient
                if (eventTrigger.equals("A04")) {
                    String adt_a04 = adt_without_pv1_checkValidate(terser, isSuccess, eventTrigger);
                    return adt_a04;

                
                } // A04
                  // A11 - Cancel Registration
                if (eventTrigger.equals("A11")) {
                    String adt_a11 = adt_without_pv1_checkValidate(terser, isSuccess, eventTrigger);
                    return adt_a11;
                } // A11
                  // A15 - Update Patient Information
                if (eventTrigger.equals("A15")) {
                    // PID
                    String adt_a15 = adt_without_pv1_checkValidate(terser, isSuccess, eventTrigger);
                    return adt_a15;
                }
                // A16 - Transfer a Patient
                if (eventTrigger.equals("A16")) {
                    String adt_a04 = adt_with_pv1_checkValidate(terser, isSuccess, eventTrigger);
                    return adt_a04;
                } // A16
                getLogger().info("I am in  : " + messageType);
            } // ADT




            if (messageType.equals("ORM")) {
                getLogger().info("I am in  : " + messageType);
            } // ORM
            if (messageType.equals("ORU")) {
                getLogger().info("I am in  : " + messageType);
            } // ORU
            if (messageType.equals("SIU")) {
                getLogger().info("I am in  : " + messageType);
            } // SIU
            if (messageType.equals("MDM")) {
                getLogger().info("I am in  : " + messageType);
            } // MDM

            getLogger().info("Final isSuccess Value :  " + isSuccess);
            return isSuccess;

        } catch (FlowFileAccessException | ProcessException e) {
            {
                isSuccess = "false";
                throw new IllegalArgumentException("HL7 message >>>> is invalid : " + e);

            }
        }

    }

    private String adt_with_pv1_checkValidate(Terser terser, String isSuccess, String eventTrigger) {
        // PID
        try {
            if (terser.get("PID-3-1")!=null) {
                getLogger().info("it is a valid PID-3-1 of eventTrigger : " + eventTrigger);
                isSuccess = "true";
            } else {
                isSuccess = "false";
                getLogger().info("Invalid PID-3-1 of eventTrigger : " + eventTrigger);
                throw new NullPointerException("Value is null for field PID-3-1");
                

            }
        } catch (HL7Exception e) {
            e.printStackTrace();
            isSuccess = "false";

            getLogger().error("Invalid PID-3-1 of eventTrigger : " + e);

        }
        try {
            if (terser.get("PID-5-1")!=null) {
                getLogger().info("it is a valid PID-5-1 of eventTrigger : " + eventTrigger);
                isSuccess = "true";

            } else {
                getLogger().info("Invalid PID-5-1 of eventTrigger : " + eventTrigger);
                isSuccess = "false";
                throw new NullPointerException("Value is null for field PID-5-1");

            }
        } catch (HL7Exception e) {
            e.printStackTrace();
        }

        try {
            if (terser.get("PID-5-2")!=null) {
                getLogger().info("it is a valid PID-5-2 of eventTrigger : " + eventTrigger);
                isSuccess = "true";

            } else {
                getLogger().info("Invalid PID-5-2 of eventTrigger : " + eventTrigger);
                isSuccess = "false";
                throw new NullPointerException("Value is null for field PID-5-2");

            }
        } catch (HL7Exception e) {
            e.printStackTrace();
        }

        try {
            if (terser.get("PID-7")!=null) {
                getLogger().info("it is a valid PID-7 of eventTrigger : " + eventTrigger);
                isSuccess = "true";

            } else {
                getLogger().info("Invalid PID-7 of eventTrigger : " + eventTrigger);
                isSuccess = "false";
                throw new NullPointerException("Value is null for field PID-7");

            }
        } catch (HL7Exception e) {
            e.printStackTrace();
        }

        try {
            if (terser.get("PID-8")!=null) {
                getLogger().info("it is a valid PID-8 of eventTrigger : " + eventTrigger);
                isSuccess = "true";

            } else {
                getLogger().info("Invalid PID-8 of eventTrigger : " + eventTrigger);
                isSuccess = "false";
                throw new NullPointerException("Value is null for field PID-8");

            }
        } catch (HL7Exception e) {
            e.printStackTrace();
        }

        // PV1
        /*
         * Summary of Required Fields:
         * 
         * PV1-1: Set ID
         * PV1-2: Patient Class
         * PV1-3: Assigned Patient Location
         * PV1-4: Admission Type
         * PV1-7: Attending Physician
         * PV1-19: Visit Number
         * PV1-22: Visit Indicator
         */
        try {
            if (terser.get("PV1-2")!=null) {
                getLogger().info("it is a valid PV1 of eventTrigger : " + eventTrigger);
                isSuccess = "true";
            } else {
                getLogger().info("Invalid PV1-2 of eventTrigger : " + eventTrigger);
                isSuccess = "false";
                throw new NullPointerException("Value is null for field PV1-2");
            }
        } catch (HL7Exception e) {
            e.printStackTrace();
        }
        try {
            if (terser.get("PV1-19")!=null) {
                getLogger().info("it is a valid PV1 of eventTrigger : " + eventTrigger);
                isSuccess = "true";
            } else {
                isSuccess = "false";
                getLogger().info("Invalid PV1-19 of eventTrigger : " + eventTrigger);
                throw new NullPointerException("Value is null for field PV1-19");
            }
        } catch (HL7Exception e) {
            e.printStackTrace();
        }
return isSuccess;
    }


    private String adt_without_pv1_checkValidate(Terser terser, String isSuccess, String eventTrigger) {
        // PID
        try {
            if (terser.get("PID-3-1")!=null) {
                getLogger().info("it is a valid PID-3-1 of eventTrigger : " + eventTrigger);
                isSuccess = "true";
            } else {
                isSuccess = "false";
                getLogger().info("Invalid PID-3-1 of eventTrigger : " + eventTrigger);
                throw new NullPointerException("Value is null for field PID-3-1");
                

            }
        } catch (HL7Exception e) {
            e.printStackTrace();
            isSuccess = "false";

            getLogger().error("Invalid PID-3-1 of eventTrigger : " + e);

        }
        try {
            if (terser.get("PID-5-1")!=null) {
                getLogger().info("it is a valid PID-5-1 of eventTrigger : " + eventTrigger);
                isSuccess = "true";

            } else {
                getLogger().info("Invalid PID-5-1 of eventTrigger : " + eventTrigger);
                isSuccess = "false";
                throw new NullPointerException("Value is null for field PID-5-1");

            }
        } catch (HL7Exception e) {
            e.printStackTrace();
        }

        try {
            if (terser.get("PID-5-2")!=null) {
                getLogger().info("it is a valid PID-5-2 of eventTrigger : " + eventTrigger);
                isSuccess = "true";

            } else {
                getLogger().info("Invalid PID-5-2 of eventTrigger : " + eventTrigger);
                isSuccess = "false";
                throw new NullPointerException("Value is null for field PID-5-2");

            }
        } catch (HL7Exception e) {
            e.printStackTrace();
        }

        try {
            if (terser.get("PID-7")!=null) {
                getLogger().info("it is a valid PID-7 of eventTrigger : " + eventTrigger);
                isSuccess = "true";

            } else {
                getLogger().info("Invalid PID-7 of eventTrigger : " + eventTrigger);
                isSuccess = "false";
                throw new NullPointerException("Value is null for field PID-7");

            }
        } catch (HL7Exception e) {
            e.printStackTrace();
        }

        try {
            if (terser.get("PID-8")!=null) {
                getLogger().info("it is a valid PID-8 of eventTrigger : " + eventTrigger);
                isSuccess = "true";

            } else {
                getLogger().info("Invalid PID-8 of eventTrigger : " + eventTrigger);
                isSuccess = "false";
                throw new NullPointerException("Value is null for field PID-8");

            }
        } catch (HL7Exception e) {
            e.printStackTrace();
        }
return isSuccess;
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

}
