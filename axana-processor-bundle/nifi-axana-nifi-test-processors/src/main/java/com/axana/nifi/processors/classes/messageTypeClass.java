package com.axana.nifi.processors.classes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v23.message.ADT_A01;
import ca.uhn.hl7v2.model.v23.message.ADT_A02;
import ca.uhn.hl7v2.model.v23.message.ADT_A03;
import ca.uhn.hl7v2.model.v23.message.ADT_A04;
import ca.uhn.hl7v2.model.v23.message.ADT_A05;
import ca.uhn.hl7v2.model.v23.message.ADT_A06;
import ca.uhn.hl7v2.model.v23.message.ADT_A07;
import ca.uhn.hl7v2.model.v23.message.ADT_A08;
import ca.uhn.hl7v2.model.v23.message.ADT_A09;
import ca.uhn.hl7v2.model.v23.message.ADT_A10;
import ca.uhn.hl7v2.model.v23.message.ADT_A11;
import ca.uhn.hl7v2.model.v23.message.ADT_A12;
import ca.uhn.hl7v2.model.v23.message.ADT_A13;
import ca.uhn.hl7v2.model.v23.message.ADT_A14;
import ca.uhn.hl7v2.model.v23.message.ADT_A15;
import ca.uhn.hl7v2.model.v23.message.ADT_A16;
import ca.uhn.hl7v2.model.v23.message.ADT_A17;
import ca.uhn.hl7v2.model.v23.message.ADT_A18;
import ca.uhn.hl7v2.model.v23.message.ADT_A20;
import ca.uhn.hl7v2.model.v23.message.ADT_A21;
import ca.uhn.hl7v2.model.v23.message.ADT_A22;
import ca.uhn.hl7v2.model.v23.message.ADT_A23;
import ca.uhn.hl7v2.model.v23.message.ADT_A24;
import ca.uhn.hl7v2.model.v23.message.ADT_A25;
import ca.uhn.hl7v2.model.v23.message.ADT_A26;
import ca.uhn.hl7v2.model.v23.message.ADT_A27;
import ca.uhn.hl7v2.model.v23.message.ADT_A28;
import ca.uhn.hl7v2.model.v23.message.ADT_A29;
import ca.uhn.hl7v2.model.v23.message.ADT_A30;
import ca.uhn.hl7v2.model.v23.message.ADT_A31;
import ca.uhn.hl7v2.model.v23.message.ADT_A32;
import ca.uhn.hl7v2.model.v23.message.ADT_A33;
import ca.uhn.hl7v2.model.v23.message.ADT_A34;
import ca.uhn.hl7v2.model.v23.message.ADT_A35;
import ca.uhn.hl7v2.model.v23.message.ADT_A36;
import ca.uhn.hl7v2.model.v23.message.ADT_A37;
import ca.uhn.hl7v2.model.v23.message.ADT_A38;
import ca.uhn.hl7v2.model.v23.message.ADT_A39;
import ca.uhn.hl7v2.model.v23.message.ADT_A40;
import ca.uhn.hl7v2.model.v23.message.ADT_A41;
import ca.uhn.hl7v2.model.v23.message.ADT_A42;
import ca.uhn.hl7v2.model.v23.message.ADT_A43;
import ca.uhn.hl7v2.model.v23.message.ADT_A44;
import ca.uhn.hl7v2.model.v23.message.ADT_A45;
import ca.uhn.hl7v2.model.v23.message.ADT_A46;
import ca.uhn.hl7v2.model.v23.message.ADT_A47;
import ca.uhn.hl7v2.model.v23.message.ADT_A48;
import ca.uhn.hl7v2.model.v23.message.ADT_A49;
import ca.uhn.hl7v2.model.v23.message.ADT_A50;
import ca.uhn.hl7v2.model.v23.message.ADT_A51;
import ca.uhn.hl7v2.model.v23.message.MFN_M01;
import ca.uhn.hl7v2.model.v23.message.MFN_M02;
import ca.uhn.hl7v2.model.v23.message.MFN_M03;
import ca.uhn.hl7v2.model.v23.message.MFN_M04;
import ca.uhn.hl7v2.model.v23.message.MFN_M05;
import ca.uhn.hl7v2.model.v23.message.MFN_M06;
import ca.uhn.hl7v2.model.v23.message.ORM_O01;
import ca.uhn.hl7v2.model.v23.message.ORU_R01;
import ca.uhn.hl7v2.model.v23.message.SIU_S12;
import ca.uhn.hl7v2.model.v23.message.SIU_S13;
import ca.uhn.hl7v2.model.v23.message.SIU_S14;
import ca.uhn.hl7v2.model.v23.message.SIU_S15;
import ca.uhn.hl7v2.model.v23.message.SIU_S16;
import ca.uhn.hl7v2.model.v23.message.SIU_S17;
import ca.uhn.hl7v2.model.v23.message.SIU_S18;
import ca.uhn.hl7v2.model.v23.message.SIU_S19;
import ca.uhn.hl7v2.model.v23.message.SIU_S20;
import ca.uhn.hl7v2.model.v23.message.SIU_S21;
import ca.uhn.hl7v2.model.v23.message.SIU_S22;
import ca.uhn.hl7v2.model.v23.message.SIU_S23;
import ca.uhn.hl7v2.model.v23.message.SIU_S24;
import ca.uhn.hl7v2.model.v23.message.SIU_S26;

public class messageTypeClass {

    // ADT Message Type
    public static Object ADT(String messageType, String triggerEvent, Message parsedMessage) throws HL7Exception {
        // HL7 v2.3 - ADT_A01 - Admit/visit notification
        JsonObject jsonOutput = new JsonObject();

        if (messageType.equals("ADT") && triggerEvent.equals("A01")) {
            ADT_A01 message = (ADT_A01) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);
            segmentProcessClass.processSegment(message.getNK1(), "NK1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            segmentProcessClass.processSegment(message.getDRG(), "DRG", jsonOutput);

            // segmentProcessClass.processSegment(message.getDB1(), "DB1",jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

            // AL1-Repeat
            for (int i = 0; i < message.getAL1Reps(); i++) {

                JsonObject al1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getAL1(i), "AL1", al1SegmentJson);

                al1Array.add(al1SegmentJson);
            }
            jsonOutput.add("Allergy", al1Array);

            // DG1-Repeat
            for (int i = 0; i < message.getDG1Reps(); i++) {

                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDG1(i), "DG1", dg1SegmentJson);

                dg1Array.add(dg1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            // PROCEDURE
            // PR1
            segmentProcessClass.processSegment(message.getPROCEDURE().getPR1(), "PR1", jsonOutput);

            // ROL - Repeat

            for (int i = 0; i < message.getPROCEDUREReps(); i++) {

                JsonObject rolSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPROCEDURE().getROL(i), "ROL", rolSegmentJson);

                rolArray.add(rolSegmentJson);
            }
            jsonOutput.add("ROLE", rolArray);

            // GT1 - Repeat
            for (int i = 0; i < message.getGT1Reps(); i++) {

                JsonObject gt1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getGT1(i), "DG1", gt1SegmentJson);

                gt1Array.add(gt1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            // INSURANCE
            // IN1
            segmentProcessClass.processSegment(message.getINSURANCE().getIN1(), "IN1", jsonOutput);
            // IN2
            segmentProcessClass.processSegment(message.getINSURANCE().getIN2(), "IN2", jsonOutput);
            // IN3
            segmentProcessClass.processSegment(message.getINSURANCE().getIN3(), "IN3", jsonOutput);

            // ACC
            segmentProcessClass.processSegment(message.getACC(), "ACC", jsonOutput);

            // UB1
            segmentProcessClass.processSegment(message.getUB1(), "UB1", jsonOutput);

            // UB2
            segmentProcessClass.processSegment(message.getUB2(), "UB2", jsonOutput);

        }

        // HL7 v2.3 - ADT_A02 - Transfer a patient

        else if (messageType.equals("ADT") && triggerEvent.equals("A02")) {
            ADT_A02 message = (ADT_A02) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();

            // DB1 - Repeat
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

            // OBX - Repeat
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);
        }

        // HL7 v2.3 - ADT_A03 - Discharge/end visit

        else if (messageType.equals("ADT") && triggerEvent.equals("A03")) {
            ADT_A03 message = (ADT_A03) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            segmentProcessClass.processSegment(message.getDRG(), "DRG", jsonOutput);

            // segmentProcessClass.processSegment(message.getDB1(), "DB1",jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

            // DG1-Repeat
            for (int i = 0; i < message.getDG1Reps(); i++) {

                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDG1(i), "DG1", dg1SegmentJson);

                dg1Array.add(dg1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            // PROCEDURE
            // PR1
            segmentProcessClass.processSegment(message.getPROCEDURE().getPR1(), "PR1", jsonOutput);

            // ROL - Repeat

            for (int i = 0; i < message.getPROCEDUREReps(); i++) {

                JsonObject rolSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPROCEDURE().getROL(i), "ROL", rolSegmentJson);

                rolArray.add(rolSegmentJson);
            }
            jsonOutput.add("ROLE", rolArray);

        }

        // HL7 v2.3 - ADT_A04 - Register a patient

        else if (messageType.equals("ADT") && triggerEvent.equals("A04")) {

            ADT_A04 message = (ADT_A04) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);
            segmentProcessClass.processSegment(message.getNK1(), "NK1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            segmentProcessClass.processSegment(message.getDRG(), "DRG", jsonOutput);

            // segmentProcessClass.processSegment(message.getDB1(), "DB1",jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

            // AL1-Repeat
            for (int i = 0; i < message.getAL1Reps(); i++) {

                JsonObject al1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getAL1(i), "AL1", al1SegmentJson);

                al1Array.add(al1SegmentJson);
            }
            jsonOutput.add("Allergy", al1Array);

            // DG1-Repeat
            for (int i = 0; i < message.getDG1Reps(); i++) {

                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDG1(i), "DG1", dg1SegmentJson);

                dg1Array.add(dg1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            // PROCEDURE
            // PR1
            segmentProcessClass.processSegment(message.getPROCEDURE().getPR1(), "PR1", jsonOutput);

            // ROL - Repeat

            for (int i = 0; i < message.getPROCEDUREReps(); i++) {

                JsonObject rolSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPROCEDURE().getROL(i), "ROL", rolSegmentJson);

                rolArray.add(rolSegmentJson);
            }
            jsonOutput.add("ROLE", rolArray);

            // GT1 - Repeat
            for (int i = 0; i < message.getGT1Reps(); i++) {

                JsonObject gt1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getGT1(i), "DG1", gt1SegmentJson);

                gt1Array.add(gt1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            // INSURANCE
            // IN1
            segmentProcessClass.processSegment(message.getINSURANCE().getIN1(), "IN1", jsonOutput);
            // IN2
            segmentProcessClass.processSegment(message.getINSURANCE().getIN2(), "IN2", jsonOutput);
            // IN3
            segmentProcessClass.processSegment(message.getINSURANCE().getIN3(), "IN3", jsonOutput);

            // ACC
            segmentProcessClass.processSegment(message.getACC(), "ACC", jsonOutput);

            // UB1
            segmentProcessClass.processSegment(message.getUB1(), "UB1", jsonOutput);

            // UB2
            segmentProcessClass.processSegment(message.getUB2(), "UB2", jsonOutput);

        }
        // HL7 v2.3 - ADT_A05 - Pre-admit a patient

        else if (messageType.equals("ADT") && triggerEvent.equals("A05")) {

            ADT_A05 message = (ADT_A05) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);
            segmentProcessClass.processSegment(message.getNK1(), "NK1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            segmentProcessClass.processSegment(message.getDRG(), "DRG", jsonOutput);

            // segmentProcessClass.processSegment(message.getDB1(), "DB1",jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

            // AL1-Repeat
            for (int i = 0; i < message.getAL1Reps(); i++) {

                JsonObject al1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getAL1(i), "AL1", al1SegmentJson);

                al1Array.add(al1SegmentJson);
            }
            jsonOutput.add("Allergy", al1Array);

            // DG1-Repeat
            for (int i = 0; i < message.getDG1Reps(); i++) {

                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDG1(i), "DG1", dg1SegmentJson);

                dg1Array.add(dg1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            // PROCEDURE
            // PR1
            segmentProcessClass.processSegment(message.getPROCEDURE().getPR1(), "PR1", jsonOutput);

            // ROL - Repeat

            for (int i = 0; i < message.getPROCEDUREReps(); i++) {

                JsonObject rolSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPROCEDURE().getROL(i), "ROL", rolSegmentJson);

                rolArray.add(rolSegmentJson);
            }
            jsonOutput.add("ROLE", rolArray);

            // GT1 - Repeat
            for (int i = 0; i < message.getGT1Reps(); i++) {

                JsonObject gt1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getGT1(i), "DG1", gt1SegmentJson);

                gt1Array.add(gt1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            // INSURANCE
            // IN1
            segmentProcessClass.processSegment(message.getINSURANCE().getIN1(), "IN1", jsonOutput);
            // IN2
            segmentProcessClass.processSegment(message.getINSURANCE().getIN2(), "IN2", jsonOutput);
            // IN3
            segmentProcessClass.processSegment(message.getINSURANCE().getIN3(), "IN3", jsonOutput);

            // ACC
            segmentProcessClass.processSegment(message.getACC(), "ACC", jsonOutput);

            // UB1
            segmentProcessClass.processSegment(message.getUB1(), "UB1", jsonOutput);

            // UB2
            segmentProcessClass.processSegment(message.getUB2(), "UB2", jsonOutput);

        }

        // HL7 v2.3 - ADT_A06 - Change an outpatient to an inpatient
        else if (messageType.equals("ADT") && triggerEvent.equals("A06")) {

            ADT_A06 message = (ADT_A06) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);
            segmentProcessClass.processSegment(message.getNK1(), "NK1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            segmentProcessClass.processSegment(message.getDRG(), "DRG", jsonOutput);

            // segmentProcessClass.processSegment(message.getDB1(), "DB1",jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

            // AL1-Repeat
            for (int i = 0; i < message.getAL1Reps(); i++) {

                JsonObject al1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getAL1(i), "AL1", al1SegmentJson);

                al1Array.add(al1SegmentJson);
            }
            jsonOutput.add("Allergy", al1Array);

            // DG1-Repeat
            for (int i = 0; i < message.getDG1Reps(); i++) {

                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDG1(i), "DG1", dg1SegmentJson);

                dg1Array.add(dg1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            // PROCEDURE
            // PR1
            segmentProcessClass.processSegment(message.getPROCEDURE().getPR1(), "PR1", jsonOutput);

            // ROL - Repeat

            for (int i = 0; i < message.getPROCEDUREReps(); i++) {

                JsonObject rolSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPROCEDURE().getROL(i), "ROL", rolSegmentJson);

                rolArray.add(rolSegmentJson);
            }
            jsonOutput.add("ROLE", rolArray);

            // GT1 - Repeat
            for (int i = 0; i < message.getGT1Reps(); i++) {

                JsonObject gt1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getGT1(i), "DG1", gt1SegmentJson);

                gt1Array.add(gt1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            // INSURANCE
            // IN1
            segmentProcessClass.processSegment(message.getINSURANCE().getIN1(), "IN1", jsonOutput);
            // IN2
            segmentProcessClass.processSegment(message.getINSURANCE().getIN2(), "IN2", jsonOutput);
            // IN3
            segmentProcessClass.processSegment(message.getINSURANCE().getIN3(), "IN3", jsonOutput);

            // ACC
            segmentProcessClass.processSegment(message.getACC(), "ACC", jsonOutput);

            // UB1
            segmentProcessClass.processSegment(message.getUB1(), "UB1", jsonOutput);

            // UB2
            segmentProcessClass.processSegment(message.getUB2(), "UB2", jsonOutput);

        }

        // HL7 v2.3 - ADT_A07 - Change an inpatient to an outpatient
        else if (messageType.equals("ADT") && triggerEvent.equals("A07")) {

            ADT_A07 message = (ADT_A07) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);
            segmentProcessClass.processSegment(message.getNK1(), "NK1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            segmentProcessClass.processSegment(message.getDRG(), "DRG", jsonOutput);

            // segmentProcessClass.processSegment(message.getDB1(), "DB1",jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

            // AL1-Repeat
            for (int i = 0; i < message.getAL1Reps(); i++) {

                JsonObject al1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getAL1(i), "AL1", al1SegmentJson);

                al1Array.add(al1SegmentJson);
            }
            jsonOutput.add("Allergy", al1Array);

            // DG1-Repeat
            for (int i = 0; i < message.getDG1Reps(); i++) {

                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDG1(i), "DG1", dg1SegmentJson);

                dg1Array.add(dg1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            // PROCEDURE
            // PR1
            segmentProcessClass.processSegment(message.getPROCEDURE().getPR1(), "PR1", jsonOutput);

            // ROL - Repeat

            for (int i = 0; i < message.getPROCEDUREReps(); i++) {

                JsonObject rolSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPROCEDURE().getROL(i), "ROL", rolSegmentJson);

                rolArray.add(rolSegmentJson);
            }
            jsonOutput.add("ROLE", rolArray);

            // GT1 - Repeat
            for (int i = 0; i < message.getGT1Reps(); i++) {

                JsonObject gt1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getGT1(i), "DG1", gt1SegmentJson);

                gt1Array.add(gt1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            // INSURANCE
            // IN1
            segmentProcessClass.processSegment(message.getINSURANCE().getIN1(), "IN1", jsonOutput);
            // IN2
            segmentProcessClass.processSegment(message.getINSURANCE().getIN2(), "IN2", jsonOutput);
            // IN3
            segmentProcessClass.processSegment(message.getINSURANCE().getIN3(), "IN3", jsonOutput);

            // ACC
            segmentProcessClass.processSegment(message.getACC(), "ACC", jsonOutput);

            // UB1
            segmentProcessClass.processSegment(message.getUB1(), "UB1", jsonOutput);

            // UB2
            segmentProcessClass.processSegment(message.getUB2(), "UB2", jsonOutput);

        }
        // HL7 v2.3 - ADT_A08 - Update patient information
        else if (messageType.equals("ADT") && triggerEvent.equals("A08")) {

            ADT_A08 message = (ADT_A08) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);
            segmentProcessClass.processSegment(message.getNK1(), "NK1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            segmentProcessClass.processSegment(message.getDRG(), "DRG", jsonOutput);

            // segmentProcessClass.processSegment(message.getDB1(), "DB1",jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

            // AL1-Repeat
            for (int i = 0; i < message.getAL1Reps(); i++) {

                JsonObject al1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getAL1(i), "AL1", al1SegmentJson);

                al1Array.add(al1SegmentJson);
            }
            jsonOutput.add("Allergy", al1Array);

            // DG1-Repeat
            for (int i = 0; i < message.getDG1Reps(); i++) {

                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDG1(i), "DG1", dg1SegmentJson);

                dg1Array.add(dg1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            // PROCEDURE
            // PR1
            segmentProcessClass.processSegment(message.getPROCEDURE().getPR1(), "PR1", jsonOutput);

            // ROL - Repeat

            for (int i = 0; i < message.getPROCEDUREReps(); i++) {

                JsonObject rolSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPROCEDURE().getROL(i), "ROL", rolSegmentJson);

                rolArray.add(rolSegmentJson);
            }
            jsonOutput.add("ROLE", rolArray);

            // GT1 - Repeat
            for (int i = 0; i < message.getGT1Reps(); i++) {

                JsonObject gt1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getGT1(i), "DG1", gt1SegmentJson);

                gt1Array.add(gt1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            // INSURANCE
            // IN1
            segmentProcessClass.processSegment(message.getINSURANCE().getIN1(), "IN1", jsonOutput);
            // IN2
            segmentProcessClass.processSegment(message.getINSURANCE().getIN2(), "IN2", jsonOutput);
            // IN3
            segmentProcessClass.processSegment(message.getINSURANCE().getIN3(), "IN3", jsonOutput);

            // ACC
            segmentProcessClass.processSegment(message.getACC(), "ACC", jsonOutput);

            // UB1
            segmentProcessClass.processSegment(message.getUB1(), "UB1", jsonOutput);

            // UB2
            segmentProcessClass.processSegment(message.getUB2(), "UB2", jsonOutput);

        }

        // HL7 v2.3 - ADT_A09 - Patient departing - tracking
        else if (messageType.equals("ADT") && triggerEvent.equals("A09")) {

            ADT_A09 message = (ADT_A09) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            // segmentProcessClass.processSegment(message.getDB1(), "DB1",jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

            // DG1-Repeat
            for (int i = 0; i < message.getDG1Reps(); i++) {

                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDG1(i), "DG1", dg1SegmentJson);

                dg1Array.add(dg1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

        }

        // HL7 v2.3 - ADT_A10 - Patient arriving - tracking
        else if (messageType.equals("ADT") && triggerEvent.equals("A10")) {

            ADT_A10 message = (ADT_A10) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            // segmentProcessClass.processSegment(message.getDB1(), "DB1",jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

            // DG1-Repeat
            for (int i = 0; i < message.getDG1Reps(); i++) {

                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDG1(i), "DG1", dg1SegmentJson);

                dg1Array.add(dg1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

        }

        // HL7 v2.3 - ADT_A11 - Cancel admit/visit notification
        else if (messageType.equals("ADT") && triggerEvent.equals("A11")) {

            ADT_A11 message = (ADT_A11) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            // segmentProcessClass.processSegment(message.getDB1(), "DB1",jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

            // DG1-Repeat
            for (int i = 0; i < message.getDG1Reps(); i++) {

                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDG1(i), "DG1", dg1SegmentJson);

                dg1Array.add(dg1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

        }

        // HL7 v2.3 - ADT_A12 - Cancel transfer
        else if (messageType.equals("ADT") && triggerEvent.equals("A12")) {

            ADT_A12 message = (ADT_A12) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            // segmentProcessClass.processSegment(message.getDB1(), "DB1",jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

            // DG1-Repeat
            segmentProcessClass.processSegment(message.getDG1(), "DG1", jsonOutput);

        }

        // HL7 v2.3 - ADT_A13 - Cancel discharge/end visit
        else if (messageType.equals("ADT") && triggerEvent.equals("A13")) {
            ADT_A13 message = (ADT_A13) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);
            segmentProcessClass.processSegment(message.getNK1(), "NK1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            segmentProcessClass.processSegment(message.getDRG(), "DRG", jsonOutput);

            // segmentProcessClass.processSegment(message.getDB1(), "DB1",jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

            // AL1-Repeat
            for (int i = 0; i < message.getAL1Reps(); i++) {

                JsonObject al1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getAL1(i), "AL1", al1SegmentJson);

                al1Array.add(al1SegmentJson);
            }
            jsonOutput.add("Allergy", al1Array);

            // DG1-Repeat
            for (int i = 0; i < message.getDG1Reps(); i++) {

                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDG1(i), "DG1", dg1SegmentJson);

                dg1Array.add(dg1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            // PROCEDURE
            // PR1
            segmentProcessClass.processSegment(message.getPROCEDURE().getPR1(), "PR1", jsonOutput);

            // ROL - Repeat

            for (int i = 0; i < message.getPROCEDUREReps(); i++) {

                JsonObject rolSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPROCEDURE().getROL(i), "ROL", rolSegmentJson);

                rolArray.add(rolSegmentJson);
            }
            jsonOutput.add("ROLE", rolArray);

            // GT1 - Repeat
            for (int i = 0; i < message.getGT1Reps(); i++) {

                JsonObject gt1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getGT1(i), "DG1", gt1SegmentJson);

                gt1Array.add(gt1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            // INSURANCE
            // IN1
            segmentProcessClass.processSegment(message.getINSURANCE().getIN1(), "IN1", jsonOutput);
            // IN2
            segmentProcessClass.processSegment(message.getINSURANCE().getIN2(), "IN2", jsonOutput);
            // IN3
            segmentProcessClass.processSegment(message.getINSURANCE().getIN3(), "IN3", jsonOutput);

            // ACC
            segmentProcessClass.processSegment(message.getACC(), "ACC", jsonOutput);

            // UB1
            segmentProcessClass.processSegment(message.getUB1(), "UB1", jsonOutput);

            // UB2
            segmentProcessClass.processSegment(message.getUB2(), "UB2", jsonOutput);

        }

        // HL7 v2.3 - ADT_A14 - Pending admit
        else if (messageType.equals("ADT") && triggerEvent.equals("A14")) {
            ADT_A14 message = (ADT_A14) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);
            segmentProcessClass.processSegment(message.getNK1(), "NK1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            segmentProcessClass.processSegment(message.getDRG(), "DRG", jsonOutput);

            // segmentProcessClass.processSegment(message.getDB1(), "DB1",jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

            // AL1-Repeat
            for (int i = 0; i < message.getAL1Reps(); i++) {

                JsonObject al1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getAL1(i), "AL1", al1SegmentJson);

                al1Array.add(al1SegmentJson);
            }
            jsonOutput.add("Allergy", al1Array);

            // DG1-Repeat
            for (int i = 0; i < message.getDG1Reps(); i++) {

                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDG1(i), "DG1", dg1SegmentJson);

                dg1Array.add(dg1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            // PROCEDURE
            // PR1
            segmentProcessClass.processSegment(message.getPROCEDURE().getPR1(), "PR1", jsonOutput);

            // ROL - Repeat

            for (int i = 0; i < message.getPROCEDUREReps(); i++) {

                JsonObject rolSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPROCEDURE().getROL(i), "ROL", rolSegmentJson);

                rolArray.add(rolSegmentJson);
            }
            jsonOutput.add("ROLE", rolArray);

            // GT1 - Repeat
            for (int i = 0; i < message.getGT1Reps(); i++) {

                JsonObject gt1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getGT1(i), "DG1", gt1SegmentJson);

                gt1Array.add(gt1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            // INSURANCE
            // IN1
            segmentProcessClass.processSegment(message.getINSURANCE().getIN1(), "IN1", jsonOutput);
            // IN2
            segmentProcessClass.processSegment(message.getINSURANCE().getIN2(), "IN2", jsonOutput);
            // IN3
            segmentProcessClass.processSegment(message.getINSURANCE().getIN3(), "IN3", jsonOutput);

            // ACC
            segmentProcessClass.processSegment(message.getACC(), "ACC", jsonOutput);

            // UB1
            segmentProcessClass.processSegment(message.getUB1(), "UB1", jsonOutput);

            // UB2
            segmentProcessClass.processSegment(message.getUB2(), "UB2", jsonOutput);

        }

        // HL7 v2.3 - ADT_A15 - Pending transfer
        else if (messageType.equals("ADT") && triggerEvent.equals("A15")) {

            ADT_A15 message = (ADT_A15) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

            // DG1-Repeat
            for (int i = 0; i < message.getDG1Reps(); i++) {

                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDG1(i), "DG1", dg1SegmentJson);

                dg1Array.add(dg1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

        }

        // HL7 v2.3 - ADT_A16 - Pending discharge
        else if (messageType.equals("ADT") && triggerEvent.equals("A16")) {

            ADT_A16 message = (ADT_A16) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);
            segmentProcessClass.processSegment(message.getDG1(), "DG1", jsonOutput);

            segmentProcessClass.processSegment(message.getDRG(), "DRG", jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

        }

        // HL7 v2.3 - ADT_A17 - Swap patients

        else if (messageType.equals("ADT") && triggerEvent.equals("A17")) {

            ADT_A17 message = (ADT_A17) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

        }

        // HL7 v2.3 - ADT_A18 - Merge patient information

        else if (messageType.equals("ADT") && triggerEvent.equals("A18")) {

            ADT_A18 message = (ADT_A18) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getMRG(), "MRG", jsonOutput);
            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

        }

        // HL7 v2.3 - ADT_A20 - Bed status update
        else if (messageType.equals("ADT") && triggerEvent.equals("A20")) {

            ADT_A20 message = (ADT_A20) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getNPU(), "NPU", jsonOutput);
        }

        // HL7 v2.3 - ADT_A21 - Patient goes on a "leave of absence"
        else if (messageType.equals("ADT") && triggerEvent.equals("A21")) {

            ADT_A21 message = (ADT_A21) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

        }

        // HL7 v2.3 - ADT_A22 - Patient returns from a "leave of absence"

        else if (messageType.equals("ADT") && triggerEvent.equals("A22")) {

            ADT_A22 message = (ADT_A22) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

        }

        // HL7 v2.3 - ADT_A23 - Delete a patient record
        else if (messageType.equals("ADT") && triggerEvent.equals("A23")) {

            ADT_A23 message = (ADT_A23) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

        }

        // HL7 v2.3 - ADT_A24 - Link patient information
        else if (messageType.equals("ADT") && triggerEvent.equals("A24")) {

            ADT_A24 message = (ADT_A24) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

        }

        // HL7 v2.3 - ADT_A25 - Cancel pending discharge

        else if (messageType.equals("ADT") && triggerEvent.equals("A25")) {

            ADT_A25 message = (ADT_A25) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

        }

        // HL7 v2.3 - ADT_A26 - Cancel pending transfer
        else if (messageType.equals("ADT") && triggerEvent.equals("A26")) {

            ADT_A26 message = (ADT_A26) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

        }

        // HL7 v2.3 - ADT_A27 - Cancel pending admit
        else if (messageType.equals("ADT") && triggerEvent.equals("A27")) {

            ADT_A27 message = (ADT_A27) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

        }

        // HL7 v2.3 - ADT_A28 - Add person information

        else if (messageType.equals("ADT") && triggerEvent.equals("A28")) {
            ADT_A28 message = (ADT_A28) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);
            segmentProcessClass.processSegment(message.getNK1(), "NK1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            segmentProcessClass.processSegment(message.getDRG(), "DRG", jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

            // AL1-Repeat
            for (int i = 0; i < message.getAL1Reps(); i++) {

                JsonObject al1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getAL1(i), "AL1", al1SegmentJson);

                al1Array.add(al1SegmentJson);
            }
            jsonOutput.add("Allergy", al1Array);

            // DG1-Repeat
            for (int i = 0; i < message.getDG1Reps(); i++) {

                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDG1(i), "DG1", dg1SegmentJson);

                dg1Array.add(dg1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            // PROCEDURE
            // PR1
            segmentProcessClass.processSegment(message.getPROCEDURE().getPR1(), "PR1", jsonOutput);

            // ROL - Repeat

            for (int i = 0; i < message.getPROCEDUREReps(); i++) {

                JsonObject rolSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPROCEDURE().getROL(i), "ROL", rolSegmentJson);

                rolArray.add(rolSegmentJson);
            }
            jsonOutput.add("ROLE", rolArray);

            // GT1 - Repeat
            for (int i = 0; i < message.getGT1Reps(); i++) {

                JsonObject gt1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getGT1(i), "DG1", gt1SegmentJson);

                gt1Array.add(gt1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            // INSURANCE
            // IN1
            segmentProcessClass.processSegment(message.getINSURANCE().getIN1(), "IN1", jsonOutput);
            // IN2
            segmentProcessClass.processSegment(message.getINSURANCE().getIN2(), "IN2", jsonOutput);
            // IN3
            segmentProcessClass.processSegment(message.getINSURANCE().getIN3(), "IN3", jsonOutput);

            // ACC
            segmentProcessClass.processSegment(message.getACC(), "ACC", jsonOutput);

            // UB1
            segmentProcessClass.processSegment(message.getUB1(), "UB1", jsonOutput);

            // UB2
            segmentProcessClass.processSegment(message.getUB2(), "UB2", jsonOutput);

        }

        // HL7 v2.3 - ADT_A29 - Delete person information

        else if (messageType.equals("ADT") && triggerEvent.equals("A29")) {

            ADT_A29 message = (ADT_A29) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

        }

        // HL7 v2.3 - ADT_A30 - Merge person information

        else if (messageType.equals("ADT") && triggerEvent.equals("A30")) {

            ADT_A30 message = (ADT_A30) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);
            segmentProcessClass.processSegment(message.getMRG(), "MRG", jsonOutput);

        }

        // HL7 v2.3 - ADT_A31 - Update person information
        else if (messageType.equals("ADT") && triggerEvent.equals("A31")) {
            ADT_A31 message = (ADT_A31) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);
            segmentProcessClass.processSegment(message.getNK1(), "NK1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            segmentProcessClass.processSegment(message.getDRG(), "DRG", jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

            // AL1-Repeat
            for (int i = 0; i < message.getAL1Reps(); i++) {

                JsonObject al1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getAL1(i), "AL1", al1SegmentJson);

                al1Array.add(al1SegmentJson);
            }
            jsonOutput.add("Allergy", al1Array);

            // DG1-Repeat
            for (int i = 0; i < message.getDG1Reps(); i++) {

                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDG1(i), "DG1", dg1SegmentJson);

                dg1Array.add(dg1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            // PROCEDURE
            // PR1
            segmentProcessClass.processSegment(message.getPROCEDURE().getPR1(), "PR1", jsonOutput);

            // ROL - Repeat

            for (int i = 0; i < message.getPROCEDUREReps(); i++) {

                JsonObject rolSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPROCEDURE().getROL(i), "ROL", rolSegmentJson);

                rolArray.add(rolSegmentJson);
            }
            jsonOutput.add("ROLE", rolArray);

            // GT1 - Repeat
            for (int i = 0; i < message.getGT1Reps(); i++) {

                JsonObject gt1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getGT1(i), "DG1", gt1SegmentJson);

                gt1Array.add(gt1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            // INSURANCE
            // IN1
            segmentProcessClass.processSegment(message.getINSURANCE().getIN1(), "IN1", jsonOutput);
            // IN2
            segmentProcessClass.processSegment(message.getINSURANCE().getIN2(), "IN2", jsonOutput);
            // IN3
            segmentProcessClass.processSegment(message.getINSURANCE().getIN3(), "IN3", jsonOutput);

            // ACC
            segmentProcessClass.processSegment(message.getACC(), "ACC", jsonOutput);

            // UB1
            segmentProcessClass.processSegment(message.getUB1(), "UB1", jsonOutput);

            // UB2
            segmentProcessClass.processSegment(message.getUB2(), "UB2", jsonOutput);

        }

        // HL7 v2.3 - ADT_A32 - Cancel patient arriving - tracking

        else if (messageType.equals("ADT") && triggerEvent.equals("A32")) {

            ADT_A32 message = (ADT_A32) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

        }

        // HL7 v2.3 - ADT_A33 - Cancel patient departing - tracking

        else if (messageType.equals("ADT") && triggerEvent.equals("A33")) {

            ADT_A33 message = (ADT_A33) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

        }

        // HL7 v2.3 - ADT_A34 - Merge patient information - patient id only

        else if (messageType.equals("ADT") && triggerEvent.equals("A34")) {

            ADT_A34 message = (ADT_A34) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getMRG(), "MRG", jsonOutput);

        }

        // HL7 v2.3 - ADT_A35 - Merge patient information - account number only

        else if (messageType.equals("ADT") && triggerEvent.equals("A35")) {

            ADT_A35 message = (ADT_A35) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getMRG(), "MRG", jsonOutput);

        }

        // HL7 v2.3 - ADT_A36 - Merge patient information - patient id and account
        // number
        else if (messageType.equals("ADT") && triggerEvent.equals("A36")) {

            ADT_A36 message = (ADT_A36) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getMRG(), "MRG", jsonOutput);

        }

        // HL7 v2.3 - ADT_A37 - Unlink patient information

        else if (messageType.equals("ADT") && triggerEvent.equals("A37")) {

            ADT_A37 message = (ADT_A37) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

        }

        // HL7 v2.3 - ADT_A38 - Cancel pre-admit

        else if (messageType.equals("ADT") && triggerEvent.equals("A38")) {
            ADT_A38 message = (ADT_A38) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPV2(), "PV2", jsonOutput);

            segmentProcessClass.processSegment(message.getDRG(), "DRG", jsonOutput);

            // segmentProcessClass.processSegment(message.getDB1(), "DB1",jsonOutput);

            JsonArray db1Array = new JsonArray();
            JsonArray obxArray = new JsonArray();
            JsonArray al1Array = new JsonArray();
            JsonArray dg1Array = new JsonArray();
            JsonArray gt1Array = new JsonArray();
            JsonArray rolArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DB1- Repeats
            for (int i = 0; i < message.getDB1Reps(); i++) {

                JsonObject db1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDB1(i), "DB1", db1SegmentJson);

                db1Array.add(db1SegmentJson);
            }
            jsonOutput.add("Disability", db1Array);

            // DG1-Repeat
            for (int i = 0; i < message.getDG1Reps(); i++) {

                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getDG1(i), "DG1", dg1SegmentJson);

                dg1Array.add(dg1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

        }

        // HL7 v2.3 - ADT_A39 - Merge person - external ID

        else if (messageType.equals("ADT") && triggerEvent.equals("A39")) {
            ADT_A39 message = (ADT_A39) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPATIENT().getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getMRG(), "MRG", jsonOutput);

        }

        // HL7 v2.3 - ADT_A40 - Merge patient - internal ID
        else if (messageType.equals("ADT") && triggerEvent.equals("A40")) {
            ADT_A40 message = (ADT_A40) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPATIENT().getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getMRG(), "MRG", jsonOutput);

        }

        // HL7 v2.3 - ADT_A41 - Merge account - patient account number

        else if (messageType.equals("ADT") && triggerEvent.equals("A39")) {
            ADT_A39 message = (ADT_A39) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPATIENT().getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getMRG(), "MRG", jsonOutput);

        }

        // HL7 v2.3 - ADT_A41 - Merge account - patient account number
        else if (messageType.equals("ADT") && triggerEvent.equals("A41")) {
            ADT_A41 message = (ADT_A41) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPATIENT().getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getMRG(), "MRG", jsonOutput);

        }

        // HL7 v2.3 - ADT_A42 - Merge visit - visit number

        else if (messageType.equals("ADT") && triggerEvent.equals("A42")) {
            ADT_A42 message = (ADT_A42) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPATIENT().getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getMRG(), "MRG", jsonOutput);

        }

        // HL7 v2.3 - ADT_A43 - Move patient information - internal ID

        else if (messageType.equals("ADT") && triggerEvent.equals("A43")) {
            ADT_A43 message = (ADT_A43) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getPATIENT().getMRG(), "MRG", jsonOutput);

        }

        // HL7 v2.3 - ADT_A44 - Move account information - patient account number

        else if (messageType.equals("ADT") && triggerEvent.equals("A44")) {
            ADT_A44 message = (ADT_A44) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPD1(), "PD1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getMRG(), "MRG", jsonOutput);

        }

        // HL7 v2.3 - ADT_A45 - Move visit information - visit number

        else if (messageType.equals("ADT") && triggerEvent.equals("A45")) {
            ADT_A45 message = (ADT_A45) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);
            segmentProcessClass.processSegment(message.getMERGE_INFO().getMRG(), "MRG", jsonOutput);
            segmentProcessClass.processSegment(message.getMERGE_INFO().getPV1(), "PV1", jsonOutput);

        }

        // HL7 v2.3 - ADT_A46 - Change external ID

        else if (messageType.equals("ADT") && triggerEvent.equals("A46")) {
            ADT_A46 message = (ADT_A46) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getMRG(), "MRG", jsonOutput);
        }

        // HL7 v2.3 - ADT_A47 - Change internal ID

        else if (messageType.equals("ADT") && triggerEvent.equals("A47")) {
            ADT_A47 message = (ADT_A47) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getMRG(), "MRG", jsonOutput);
        }

        // HL7 v2.3 - ADT_A48 - Change alternate patient id

        else if (messageType.equals("ADT") && triggerEvent.equals("A48")) {
            ADT_A48 message = (ADT_A48) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getMRG(), "MRG", jsonOutput);
        }

        // HL7 v2.3 - ADT_A49 - Change patient account number

        else if (messageType.equals("ADT") && triggerEvent.equals("A49")) {
            ADT_A49 message = (ADT_A49) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getMRG(), "MRG", jsonOutput);
        }

        // HL7 v2.3 - ADT_A50 - Change visit number
        else if (messageType.equals("ADT") && triggerEvent.equals("A50")) {
            ADT_A50 message = (ADT_A50) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getMRG(), "MRG", jsonOutput);
            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
        }

        // HL7 v2.3 - ADT_A51 - Change alternate visit id
        else if (messageType.equals("ADT") && triggerEvent.equals("A51")) {
            ADT_A51 message = (ADT_A51) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getEVN(), "EVN", jsonOutput);

            segmentProcessClass.processSegment(message.getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPD1(), "PD1", jsonOutput);

            segmentProcessClass.processSegment(message.getMRG(), "MRG", jsonOutput);
            segmentProcessClass.processSegment(message.getPV1(), "PV1", jsonOutput);
        }

        // HL7 v2.3 - MFN_M01 - Master file not otherwise specified
        else if (messageType.equals("MFN") && triggerEvent.equals("M01")) {
            MFN_M01 message = (MFN_M01) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getMFI(), "MFI", jsonOutput);
            segmentProcessClass.processSegment(message.getMF().getMFE(), "MFE", jsonOutput);
        }

        // HL7 v2.3 - MFN_M02 - Master file - staff practitioner

        else if (messageType.equals("MFN") && triggerEvent.equals("M02")) {
            MFN_M02 message = (MFN_M02) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getMFI(), "MFI", jsonOutput);
            segmentProcessClass.processSegment(message.getMF_STAFF().getMFE(), "MFE", jsonOutput);
            segmentProcessClass.processSegment(message.getMF_STAFF().getSTF(), "STF", jsonOutput);
            segmentProcessClass.processSegment(message.getMF_STAFF().getPRA(), "PRA", jsonOutput);

        }

        // HL7 v2.3 - MFN_M03 - Master file - Test/Observation

        else if (messageType.equals("MFN") && triggerEvent.equals("M03")) {
            MFN_M03 message = (MFN_M03) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getMFI(), "MFI", jsonOutput);
            segmentProcessClass.processSegment(message.getMF_TEST().getMFE(), "MFE", jsonOutput);

        }

        // HL7 v2.3 - MFN_M04 - Master files charge description

        else if (messageType.equals("MFN") && triggerEvent.equals("M04")) {
            MFN_M04 message = (MFN_M04) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getMFI(), "MFI", jsonOutput);
            segmentProcessClass.processSegment(message.getMF_CDM().getMFE(), "MFE", jsonOutput);
            segmentProcessClass.processSegment(message.getMF_CDM().getCDM(), "CDM", jsonOutput);
            segmentProcessClass.processSegment(message.getMF_CDM().getPRC(), "PRC", jsonOutput);

        }

        // HL7 v2.3 - MFN_M05 - Patient location master file

        else if (messageType.equals("MFN") && triggerEvent.equals("M05")) {
            MFN_M05 message = (MFN_M05) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getMFI(), "MFI", jsonOutput);
            segmentProcessClass.processSegment(message.getMF_LOCATION().getMFE(), "MFE", jsonOutput);
            segmentProcessClass.processSegment(message.getMF_LOCATION().getLOC(), "LOC", jsonOutput);

            JsonArray lchArray = new JsonArray();
            JsonArray lrlArray = new JsonArray();
            JsonArray lccArray = new JsonArray();

            // LCH-Repeats
            for (int i = 0; i < message.getMF_LOCATION().getLCHReps(); i++) {
                JsonObject lchSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getMF_LOCATION().getLCH(i), "LCH", lchSegmentJson);

                lchArray.add(lchSegmentJson);

            }
            jsonOutput.add("LocationCharacteristic", lchArray);

            // LRL-Repeats
            for (int i = 0; i < message.getMF_LOCATION().getLRLReps(); i++) {
                JsonObject lrlSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getMF_LOCATION().getLRL(i), "LRL", lrlSegmentJson);

                lrlArray.add(lrlSegmentJson);

            }

            jsonOutput.add("LocationRelationship", lrlArray);

            // MF LOC DEPT
            segmentProcessClass.processSegment(message.getMF_LOCATION().getMF_LOC_DEPT().getLDP(), "LDP", jsonOutput);

            // LCH-Repeats
            for (int i = 0; i < message.getMF_LOCATION().getMF_LOC_DEPT().getLCHReps(); i++) {
                JsonObject lchSegmentJson = new JsonObject();
                // LCH-Repeats

                segmentProcessClass.processSegment(message.getMF_LOCATION().getMF_LOC_DEPT().getLCH(i), "LCH",
                        lchSegmentJson);

                lchArray.add(lchSegmentJson);

            }
            jsonOutput.add("LocationCharacteristic", lchArray);

            // LCC-Repeats
            for (int i = 0; i < message.getMF_LOCATION().getMF_LOC_DEPT().getLCCReps(); i++) {
                JsonObject lccSegmentJson = new JsonObject();
                segmentProcessClass.processSegment(message.getMF_LOCATION().getMF_LOC_DEPT().getLCC(i), "LCC",
                        lccSegmentJson);

                lccArray.add(lccSegmentJson);

            }
            jsonOutput.add("LocationChargeCode", lccArray);

        }

        // HL7 v2.3 - MFN_M06 - Clinical study with phases and schedules master file
        else if (messageType.equals("MFN") && triggerEvent.equals("M06")) {
            MFN_M06 message = (MFN_M06) parsedMessage;
            JsonArray clnstudyArray = new JsonArray();

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getMFI(), "MFI", jsonOutput);

            for (int i = 0; i < message.getMF_CLIN_STUDYReps(); i++) {
                JsonObject clnstudySegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getMF_CLIN_STUDY(i).getMFE(), "MFE", clnstudySegmentJson);
                segmentProcessClass.processSegment(message.getMF_CLIN_STUDY(i).getCM0(), "CM0", clnstudySegmentJson);

                clnstudyArray.add(clnstudySegmentJson);

            }
            jsonOutput.add("MfClinStudy", clnstudyArray);
            JsonArray clnstudyphaseArray = new JsonArray();

            for (int i = 0; i < message.getMF_CLIN_STUDY().getMF_PHASE_SCHED_DETAILReps(); i++) {
                JsonObject clnstudyphaseSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getMF_CLIN_STUDY().getMF_PHASE_SCHED_DETAIL(i).getCM1(),
                        "CM1", clnstudyphaseSegmentJson);

                for (int j = 0; j < message.getMF_CLIN_STUDY().getMF_PHASE_SCHED_DETAILReps(); j++) {
                    segmentProcessClass.processSegment(message.getMF_CLIN_STUDY().getMF_PHASE_SCHED_DETAIL(j).getCM2(),
                            "CM2", clnstudyphaseSegmentJson);

                }

                clnstudyphaseArray.add(clnstudyphaseSegmentJson);

            }
            jsonOutput.add("MfClinStudyPhase", clnstudyphaseArray);

        }
        return jsonOutput;

    }

    public static Object SIU(String messageType, String triggerEvent, Message parsedMessage) throws HL7Exception {

        JsonObject jsonOutput = new JsonObject();
        if (messageType.equals("SIU") && triggerEvent.equals("S14")) {
            SIU_S14 message = (SIU_S14) parsedMessage;
            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getSCH(), "SCH", jsonOutput);
            segmentProcessClass.processSegment(message.getNTE(), "NTE", jsonOutput);

            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV2(), "PV2", jsonOutput);

            JsonArray obxArray = new JsonArray();

            JsonArray dg1Array = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getPATIENT().getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DG1-Repeats
            for (int i = 0; i < message.getPATIENT().getDG1Reps(); i++) {
                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getDG1(i), "DG1", dg1SegmentJson);

                dg1Array.add(dg1SegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getRGS(), "RGS", jsonOutput);

            segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getAIS(), "AIS", jsonOutput);
            // NTE-Repeats
            JsonArray nteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject nteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getNTE(i), "NTE",
                        nteSegmentJson);

                nteArray.add(nteSegmentJson);

            }
            jsonOutput.add("Notes", nteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getAIG(), "AIG",
                    jsonOutput);

            JsonArray gnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject gnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getNTE(i), "NTE",
                        gnteSegmentJson);

                gnteArray.add(gnteSegmentJson);

            }
            jsonOutput.add("Notes", gnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getAIL(), "AIL",
                    jsonOutput);

            JsonArray lnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getLOCATION_RESOURCE().getNTEReps(); i++) {
                JsonObject lnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getNTE(i), "NTE",
                        lnteSegmentJson);

                gnteArray.add(lnteSegmentJson);

            }
            jsonOutput.add("Notes", lnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getAIP(), "AIL",
                    jsonOutput);

            JsonArray pnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getPERSONNEL_RESOURCE().getNTEReps(); i++) {
                JsonObject pnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getNTE(i), "NTE",
                        pnteSegmentJson);

                pnteArray.add(pnteSegmentJson);

            }
            jsonOutput.add("Notes", pnteArray);

        } else if (messageType.equals("SIU") && triggerEvent.equals("S12")) {
          //  System.out.print("SIU Segment :");
            SIU_S12 message = (SIU_S12) parsedMessage;
            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getSCH(), "SCH", jsonOutput);
            segmentProcessClass.processSegment(message.getNTE(), "NTE", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV2(), "PV2", jsonOutput);

            JsonArray obxArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getPATIENT().getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DG1-Repeats
            for (int i = 0; i < message.getPATIENT().getDG1Reps(); i++) {
                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getDG1(i), "DG1", dg1SegmentJson);

                obxArray.add(dg1SegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getRGS(), "RGS", jsonOutput);
            segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getAIS(), "AIS", jsonOutput);
            // NTE-Repeats
            JsonArray nteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject nteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getNTE(i), "NTE",
                        nteSegmentJson);

                nteArray.add(nteSegmentJson);

            }
            jsonOutput.add("Notes", nteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getAIG(), "AIG",
                    jsonOutput);

            JsonArray gnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject gnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getNTE(i), "NTE",
                        gnteSegmentJson);

                gnteArray.add(gnteSegmentJson);

            }
            jsonOutput.add("Notes", gnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getAIL(), "AIL",
                    jsonOutput);

            JsonArray lnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getLOCATION_RESOURCE().getNTEReps(); i++) {
                JsonObject lnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getNTE(i), "NTE",
                        lnteSegmentJson);

                gnteArray.add(lnteSegmentJson);

            }
            jsonOutput.add("Notes", lnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getAIP(), "AIL",
                    jsonOutput);

            JsonArray pnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getPERSONNEL_RESOURCE().getNTEReps(); i++) {
                JsonObject pnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getNTE(i), "NTE",
                        pnteSegmentJson);

                pnteArray.add(pnteSegmentJson);

            }
            jsonOutput.add("Notes", pnteArray);

        } else if (messageType.equals("SIU") && triggerEvent.equals("S17")) {
            SIU_S17 message = (SIU_S17) parsedMessage;
            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getSCH(), "SCH", jsonOutput);
            segmentProcessClass.processSegment(message.getNTE(), "NTE", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV2(), "PV2", jsonOutput);

            JsonArray obxArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getPATIENT().getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DG1-Repeats
            for (int i = 0; i < message.getPATIENT().getDG1Reps(); i++) {
                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getDG1(i), "DG1", dg1SegmentJson);

                obxArray.add(dg1SegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getRGS(), "RGS", jsonOutput);
            segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getAIS(), "AIS", jsonOutput);
            // NTE-Repeats
            JsonArray nteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject nteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getNTE(i), "NTE",
                        nteSegmentJson);

                nteArray.add(nteSegmentJson);

            }
            jsonOutput.add("Notes", nteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getAIG(), "AIG",
                    jsonOutput);

            JsonArray gnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject gnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getNTE(i), "NTE",
                        gnteSegmentJson);

                gnteArray.add(gnteSegmentJson);

            }
            jsonOutput.add("Notes", gnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getAIL(), "AIL",
                    jsonOutput);

            JsonArray lnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getLOCATION_RESOURCE().getNTEReps(); i++) {
                JsonObject lnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getNTE(i), "NTE",
                        lnteSegmentJson);

                gnteArray.add(lnteSegmentJson);

            }
            jsonOutput.add("Notes", lnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getAIP(), "AIL",
                    jsonOutput);

            JsonArray pnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getPERSONNEL_RESOURCE().getNTEReps(); i++) {
                JsonObject pnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getNTE(i), "NTE",
                        pnteSegmentJson);

                pnteArray.add(pnteSegmentJson);

            }
            jsonOutput.add("Notes", pnteArray);
        }

        else if (messageType.equals("SIU") && triggerEvent.equals("S13")) {
            SIU_S13 message = (SIU_S13) parsedMessage;
            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getSCH(), "SCH", jsonOutput);
            segmentProcessClass.processSegment(message.getNTE(), "NTE", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV2(), "PV2", jsonOutput);

            JsonArray obxArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getPATIENT().getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DG1-Repeats
            for (int i = 0; i < message.getPATIENT().getDG1Reps(); i++) {
                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getDG1(i), "DG1", dg1SegmentJson);

                obxArray.add(dg1SegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getRGS(), "RGS", jsonOutput);
            segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getAIS(), "AIS", jsonOutput);
            // NTE-Repeats
            JsonArray nteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject nteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getNTE(i), "NTE",
                        nteSegmentJson);

                nteArray.add(nteSegmentJson);

            }
            jsonOutput.add("Notes", nteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getAIG(), "AIG",
                    jsonOutput);

            JsonArray gnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject gnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getNTE(i), "NTE",
                        gnteSegmentJson);

                gnteArray.add(gnteSegmentJson);

            }
            jsonOutput.add("Notes", gnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getAIL(), "AIL",
                    jsonOutput);

            JsonArray lnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getLOCATION_RESOURCE().getNTEReps(); i++) {
                JsonObject lnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getNTE(i), "NTE",
                        lnteSegmentJson);

                gnteArray.add(lnteSegmentJson);

            }
            jsonOutput.add("Notes", lnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getAIP(), "AIL",
                    jsonOutput);

            JsonArray pnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getPERSONNEL_RESOURCE().getNTEReps(); i++) {
                JsonObject pnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getNTE(i), "NTE",
                        pnteSegmentJson);

                pnteArray.add(pnteSegmentJson);

            }
            jsonOutput.add("Notes", pnteArray);
        }

        else if (messageType.equals("SIU") && triggerEvent.equals("S15")) {
            SIU_S15 message = (SIU_S15) parsedMessage;
            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getSCH(), "SCH", jsonOutput);
            segmentProcessClass.processSegment(message.getNTE(), "NTE", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV2(), "PV2", jsonOutput);

            JsonArray obxArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getPATIENT().getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DG1-Repeats
            for (int i = 0; i < message.getPATIENT().getDG1Reps(); i++) {
                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getDG1(i), "DG1", dg1SegmentJson);

                obxArray.add(dg1SegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getRGS(), "RGS", jsonOutput);
            segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getAIS(), "AIS", jsonOutput);
            // NTE-Repeats
            JsonArray nteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject nteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getNTE(i), "NTE",
                        nteSegmentJson);

                nteArray.add(nteSegmentJson);

            }
            jsonOutput.add("Notes", nteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getAIG(), "AIG",
                    jsonOutput);

            JsonArray gnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject gnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getNTE(i), "NTE",
                        gnteSegmentJson);

                gnteArray.add(gnteSegmentJson);

            }
            jsonOutput.add("Notes", gnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getAIL(), "AIL",
                    jsonOutput);

            JsonArray lnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getLOCATION_RESOURCE().getNTEReps(); i++) {
                JsonObject lnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getNTE(i), "NTE",
                        lnteSegmentJson);

                gnteArray.add(lnteSegmentJson);

            }
            jsonOutput.add("Notes", lnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getAIP(), "AIL",
                    jsonOutput);

            JsonArray pnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getPERSONNEL_RESOURCE().getNTEReps(); i++) {
                JsonObject pnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getNTE(i), "NTE",
                        pnteSegmentJson);

                pnteArray.add(pnteSegmentJson);

            }
            jsonOutput.add("Notes", pnteArray);
        }

        else if (messageType.equals("SIU") && triggerEvent.equals("S16")) {
            SIU_S16 message = (SIU_S16) parsedMessage;
            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getSCH(), "SCH", jsonOutput);
            segmentProcessClass.processSegment(message.getNTE(), "NTE", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV2(), "PV2", jsonOutput);

            JsonArray obxArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getPATIENT().getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DG1-Repeats
            for (int i = 0; i < message.getPATIENT().getDG1Reps(); i++) {
                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getDG1(i), "DG1", dg1SegmentJson);

                obxArray.add(dg1SegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getRGS(), "RGS", jsonOutput);
            segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getAIS(), "AIS", jsonOutput);
            // NTE-Repeats
            JsonArray nteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject nteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getNTE(i), "NTE",
                        nteSegmentJson);

                nteArray.add(nteSegmentJson);

            }
            jsonOutput.add("Notes", nteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getAIG(), "AIG",
                    jsonOutput);

            JsonArray gnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject gnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getNTE(i), "NTE",
                        gnteSegmentJson);

                gnteArray.add(gnteSegmentJson);

            }
            jsonOutput.add("Notes", gnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getLOCATIONL_RESOURCE().getAIL(), "AIL",
                    jsonOutput);

            JsonArray lnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getLOCATIONL_RESOURCE().getNTEReps(); i++) {
                JsonObject lnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getLOCATIONL_RESOURCE().getNTE(i), "NTE",
                        lnteSegmentJson);

                gnteArray.add(lnteSegmentJson);

            }
            jsonOutput.add("Notes", lnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getAIP(), "AIL",
                    jsonOutput);

            JsonArray pnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getPERSONNEL_RESOURCE().getNTEReps(); i++) {
                JsonObject pnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getNTE(i), "NTE",
                        pnteSegmentJson);

                pnteArray.add(pnteSegmentJson);

            }
            jsonOutput.add("Notes", pnteArray);
        }

        else if (messageType.equals("SIU") && triggerEvent.equals("S18")) {
            SIU_S18 message = (SIU_S18) parsedMessage;
            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getSCH(), "SCH", jsonOutput);
            segmentProcessClass.processSegment(message.getNTE(), "NTE", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV2(), "PV2", jsonOutput);

            JsonArray obxArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getPATIENT().getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DG1-Repeats
            for (int i = 0; i < message.getPATIENT().getDG1Reps(); i++) {
                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getDG1(i), "DG1", dg1SegmentJson);

                obxArray.add(dg1SegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getRGS(), "RGS", jsonOutput);
            segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getAIS(), "AIS", jsonOutput);
            // NTE-Repeats
            JsonArray nteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject nteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getNTE(i), "NTE",
                        nteSegmentJson);

                nteArray.add(nteSegmentJson);

            }
            jsonOutput.add("Notes", nteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getAIG(), "AIG",
                    jsonOutput);

            JsonArray gnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject gnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getNTE(i), "NTE",
                        gnteSegmentJson);

                gnteArray.add(gnteSegmentJson);

            }
            jsonOutput.add("Notes", gnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getAIL(), "AIL",
                    jsonOutput);

            JsonArray lnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getLOCATION_RESOURCE().getNTEReps(); i++) {
                JsonObject lnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getNTE(i), "NTE",
                        lnteSegmentJson);

                gnteArray.add(lnteSegmentJson);

            }
            jsonOutput.add("Notes", lnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getAIP(), "AIL",
                    jsonOutput);

            JsonArray pnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getPERSONNEL_RESOURCE().getNTEReps(); i++) {
                JsonObject pnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getNTE(i), "NTE",
                        pnteSegmentJson);

                pnteArray.add(pnteSegmentJson);

            }
            jsonOutput.add("Notes", pnteArray);
        }

        else if (messageType.equals("SIU") && triggerEvent.equals("S19")) {
            SIU_S19 message = (SIU_S19) parsedMessage;
            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getSCH(), "SCH", jsonOutput);
            segmentProcessClass.processSegment(message.getNTE(), "NTE", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV2(), "PV2", jsonOutput);

            JsonArray obxArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getPATIENT().getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DG1-Repeats
            for (int i = 0; i < message.getPATIENT().getDG1Reps(); i++) {
                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getDG1(i), "DG1", dg1SegmentJson);

                obxArray.add(dg1SegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getRGS(), "RGS", jsonOutput);
            segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getAIS(), "AIS", jsonOutput);
            // NTE-Repeats
            JsonArray nteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject nteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getNTE(i), "NTE",
                        nteSegmentJson);

                nteArray.add(nteSegmentJson);

            }
            jsonOutput.add("Notes", nteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getAIG(), "AIG",
                    jsonOutput);

            JsonArray gnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject gnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getNTE(i), "NTE",
                        gnteSegmentJson);

                gnteArray.add(gnteSegmentJson);

            }
            jsonOutput.add("Notes", gnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getAIL(), "AIL",
                    jsonOutput);

            JsonArray lnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getLOCATION_RESOURCE().getNTEReps(); i++) {
                JsonObject lnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getNTE(i), "NTE",
                        lnteSegmentJson);

                gnteArray.add(lnteSegmentJson);

            }
            jsonOutput.add("Notes", lnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getAIP(), "AIL",
                    jsonOutput);

            JsonArray pnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getPERSONNEL_RESOURCE().getNTEReps(); i++) {
                JsonObject pnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getNTE(i), "NTE",
                        pnteSegmentJson);

                pnteArray.add(pnteSegmentJson);

            }
            jsonOutput.add("Notes", pnteArray);
        }

        else if (messageType.equals("SIU") && triggerEvent.equals("S20")) {
            SIU_S20 message = (SIU_S20) parsedMessage;
            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getSCH(), "SCH", jsonOutput);
            segmentProcessClass.processSegment(message.getNTE(), "NTE", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV2(), "PV2", jsonOutput);

            JsonArray obxArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getPATIENT().getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DG1-Repeats
            for (int i = 0; i < message.getPATIENT().getDG1Reps(); i++) {
                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getDG1(i), "DG1", dg1SegmentJson);

                obxArray.add(dg1SegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getRGS(), "RGS", jsonOutput);
            segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getAIS(), "AIS", jsonOutput);
            // NTE-Repeats
            JsonArray nteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject nteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getNTE(i), "NTE",
                        nteSegmentJson);

                nteArray.add(nteSegmentJson);

            }
            jsonOutput.add("Notes", nteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getAIG(), "AIG",
                    jsonOutput);

            JsonArray gnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject gnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getNTE(i), "NTE",
                        gnteSegmentJson);

                gnteArray.add(gnteSegmentJson);

            }
            jsonOutput.add("Notes", gnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getAIL(), "AIL",
                    jsonOutput);

            JsonArray lnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getLOCATION_RESOURCE().getNTEReps(); i++) {
                JsonObject lnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getNTE(i), "NTE",
                        lnteSegmentJson);

                gnteArray.add(lnteSegmentJson);

            }
            jsonOutput.add("Notes", lnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getAIP(), "AIL",
                    jsonOutput);

            JsonArray pnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getPERSONNEL_RESOURCE().getNTEReps(); i++) {
                JsonObject pnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getNTE(i), "NTE",
                        pnteSegmentJson);

                pnteArray.add(pnteSegmentJson);

            }
            jsonOutput.add("Notes", pnteArray);
        }

        else if (messageType.equals("SIU") && triggerEvent.equals("S21")) {
            SIU_S21 message = (SIU_S21) parsedMessage;
            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getSCH(), "SCH", jsonOutput);
            segmentProcessClass.processSegment(message.getNTE(), "NTE", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV2(), "PV2", jsonOutput);

            JsonArray obxArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getPATIENT().getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DG1-Repeats
            for (int i = 0; i < message.getPATIENT().getDG1Reps(); i++) {
                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getDG1(i), "DG1", dg1SegmentJson);

                obxArray.add(dg1SegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getRGS(), "RGS", jsonOutput);
            segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getAIS(), "AIS", jsonOutput);
            // NTE-Repeats
            JsonArray nteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject nteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getNTE(i), "NTE",
                        nteSegmentJson);

                nteArray.add(nteSegmentJson);

            }
            jsonOutput.add("Notes", nteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getAIG(), "AIG",
                    jsonOutput);

            JsonArray gnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject gnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getNTE(i), "NTE",
                        gnteSegmentJson);

                gnteArray.add(gnteSegmentJson);

            }
            jsonOutput.add("Notes", gnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getAIL(), "AIL",
                    jsonOutput);

            JsonArray lnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getLOCATION_RESOURCE().getNTEReps(); i++) {
                JsonObject lnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getNTE(i), "NTE",
                        lnteSegmentJson);

                gnteArray.add(lnteSegmentJson);

            }
            jsonOutput.add("Notes", lnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getAIP(), "AIL",
                    jsonOutput);

            JsonArray pnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getPERSONNEL_RESOURCE().getNTEReps(); i++) {
                JsonObject pnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getNTE(i), "NTE",
                        pnteSegmentJson);

                pnteArray.add(pnteSegmentJson);

            }
            jsonOutput.add("Notes", pnteArray);
        }

        else if (messageType.equals("SIU") && triggerEvent.equals("S22")) {
            SIU_S22 message = (SIU_S22) parsedMessage;
            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getSCH(), "SCH", jsonOutput);
            segmentProcessClass.processSegment(message.getNTE(), "NTE", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV2(), "PV2", jsonOutput);

            JsonArray obxArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getPATIENT().getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DG1-Repeats
            for (int i = 0; i < message.getPATIENT().getDG1Reps(); i++) {
                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getDG1(i), "DG1", dg1SegmentJson);

                obxArray.add(dg1SegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getRGS(), "RGS", jsonOutput);
            segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getAIS(), "AIS", jsonOutput);
            // NTE-Repeats
            JsonArray nteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject nteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getNTE(i), "NTE",
                        nteSegmentJson);

                nteArray.add(nteSegmentJson);

            }
            jsonOutput.add("Notes", nteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getAIG(), "AIG",
                    jsonOutput);

            JsonArray gnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject gnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getNTE(i), "NTE",
                        gnteSegmentJson);

                gnteArray.add(gnteSegmentJson);

            }
            jsonOutput.add("Notes", gnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getAIL(), "AIL",
                    jsonOutput);

            JsonArray lnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getLOCATION_RESOURCE().getNTEReps(); i++) {
                JsonObject lnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getNTE(i), "NTE",
                        lnteSegmentJson);

                gnteArray.add(lnteSegmentJson);

            }
            jsonOutput.add("Notes", lnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getAIP(), "AIL",
                    jsonOutput);

            JsonArray pnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getPERSONNEL_RESOURCE().getNTEReps(); i++) {
                JsonObject pnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getNTE(i), "NTE",
                        pnteSegmentJson);

                pnteArray.add(pnteSegmentJson);

            }
            jsonOutput.add("Notes", pnteArray);
        }

        else if (messageType.equals("SIU") && triggerEvent.equals("S23")) {
            SIU_S23 message = (SIU_S23) parsedMessage;
            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getSCH(), "SCH", jsonOutput);
            segmentProcessClass.processSegment(message.getNTE(), "NTE", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV2(), "PV2", jsonOutput);

            JsonArray obxArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getPATIENT().getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DG1-Repeats
            for (int i = 0; i < message.getPATIENT().getDG1Reps(); i++) {
                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getDG1(i), "DG1", dg1SegmentJson);

                obxArray.add(dg1SegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getRGS(), "RGS", jsonOutput);
            segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getAIS(), "AIS", jsonOutput);
            // NTE-Repeats
            JsonArray nteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject nteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getNTE(i), "NTE",
                        nteSegmentJson);

                nteArray.add(nteSegmentJson);

            }
            jsonOutput.add("Notes", nteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getAIG(), "AIG",
                    jsonOutput);

            JsonArray gnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject gnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getNTE(i), "NTE",
                        gnteSegmentJson);

                gnteArray.add(gnteSegmentJson);

            }
            jsonOutput.add("Notes", gnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getAIL(), "AIL",
                    jsonOutput);

            JsonArray lnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getLOCATION_RESOURCE().getNTEReps(); i++) {
                JsonObject lnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getNTE(i), "NTE",
                        lnteSegmentJson);

                gnteArray.add(lnteSegmentJson);

            }
            jsonOutput.add("Notes", lnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getAIP(), "AIL",
                    jsonOutput);

            JsonArray pnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getPERSONNEL_RESOURCE().getNTEReps(); i++) {
                JsonObject pnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getNTE(i), "NTE",
                        pnteSegmentJson);

                pnteArray.add(pnteSegmentJson);

            }
            jsonOutput.add("Notes", pnteArray);
        }

        else if (messageType.equals("SIU") && triggerEvent.equals("S24")) {
            SIU_S24 message = (SIU_S24) parsedMessage;
            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getSCH(), "SCH", jsonOutput);
            segmentProcessClass.processSegment(message.getNTE(), "NTE", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV2(), "PV2", jsonOutput);

            JsonArray obxArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getPATIENT().getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DG1-Repeats
            for (int i = 0; i < message.getPATIENT().getDG1Reps(); i++) {
                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getDG1(i), "DG1", dg1SegmentJson);

                obxArray.add(dg1SegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getRGS(), "RGS", jsonOutput);
            segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getAIS(), "AIS", jsonOutput);
            // NTE-Repeats
            JsonArray nteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject nteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getNTE(i), "NTE",
                        nteSegmentJson);

                nteArray.add(nteSegmentJson);

            }
            jsonOutput.add("Notes", nteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getAIG(), "AIG",
                    jsonOutput);

            JsonArray gnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject gnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getNTE(i), "NTE",
                        gnteSegmentJson);

                gnteArray.add(gnteSegmentJson);

            }
            jsonOutput.add("Notes", gnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getAIL(), "AIL",
                    jsonOutput);

            JsonArray lnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getLOCATION_RESOURCE().getNTEReps(); i++) {
                JsonObject lnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getNTE(i), "NTE",
                        lnteSegmentJson);

                gnteArray.add(lnteSegmentJson);

            }
            jsonOutput.add("Notes", lnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getAIP(), "AIL",
                    jsonOutput);

            JsonArray pnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getPERSONNEL_RESOURCE().getNTEReps(); i++) {
                JsonObject pnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getNTE(i), "NTE",
                        pnteSegmentJson);

                pnteArray.add(pnteSegmentJson);

            }
            jsonOutput.add("Notes", pnteArray);
        }

        else if (messageType.equals("SIU") && triggerEvent.equals("S26")) {
            SIU_S26 message = (SIU_S26) parsedMessage;
            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getSCH(), "SCH", jsonOutput);
            segmentProcessClass.processSegment(message.getNTE(), "NTE", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPV2(), "PV2", jsonOutput);

            JsonArray obxArray = new JsonArray();

            // OBX-Repeats
            for (int i = 0; i < message.getPATIENT().getOBXReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getOBX(i), "OBX", obxSegmentJson);

                obxArray.add(obxSegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            // DG1-Repeats
            for (int i = 0; i < message.getPATIENT().getDG1Reps(); i++) {
                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getPATIENT().getDG1(i), "DG1", dg1SegmentJson);

                obxArray.add(dg1SegmentJson);

            }
            jsonOutput.add("Observations", obxArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getRGS(), "RGS", jsonOutput);
            segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getAIS(), "AIS", jsonOutput);
            // NTE-Repeats
            JsonArray nteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject nteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getSERVICE().getNTE(i), "NTE",
                        nteSegmentJson);

                nteArray.add(nteSegmentJson);

            }
            jsonOutput.add("Notes", nteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getAIG(), "AIG",
                    jsonOutput);

            JsonArray gnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getSERVICE().getNTEReps(); i++) {
                JsonObject gnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getGENERAL_RESOURCE().getNTE(i), "NTE",
                        gnteSegmentJson);

                gnteArray.add(gnteSegmentJson);

            }
            jsonOutput.add("Notes", gnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getAIL(), "AIL",
                    jsonOutput);

            JsonArray lnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getLOCATION_RESOURCE().getNTEReps(); i++) {
                JsonObject lnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getLOCATION_RESOURCE().getNTE(i), "NTE",
                        lnteSegmentJson);

                gnteArray.add(lnteSegmentJson);

            }
            jsonOutput.add("Notes", lnteArray);

            segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getAIP(), "AIL",
                    jsonOutput);

            JsonArray pnteArray = new JsonArray();
            for (int i = 0; i < message.getRESOURCES().getPERSONNEL_RESOURCE().getNTEReps(); i++) {
                JsonObject pnteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESOURCES().getPERSONNEL_RESOURCE().getNTE(i), "NTE",
                        pnteSegmentJson);

                pnteArray.add(pnteSegmentJson);

            }
            jsonOutput.add("Notes", pnteArray);
        }

        return jsonOutput;

    }

    public static Object ORM(String messageType, String triggerEvent, Message parsedMessage) throws HL7Exception {
        // HL7 v2.3 - ADT_A01 - Admit/visit notification
        JsonObject jsonOutput = new JsonObject();
        // HL7 v2.3 - ORM_O01 - Order message
        if (messageType.equals("ORM") && triggerEvent.equals("O01")) {
            ORM_O01 message = (ORM_O01) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);
            segmentProcessClass.processSegment(message.getNTE(), "NTE", jsonOutput);

            segmentProcessClass.processSegment(message.getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPD1(), "PD1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getNTE(), "NTE", jsonOutput);

            segmentProcessClass.processSegment(message.getPATIENT().getPATIENT_VISIT().getPV1(), "PV1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getPATIENT_VISIT().getPV2(), "PV2", jsonOutput);

            segmentProcessClass.processSegment(message.getPATIENT().getINSURANCE().getIN1(), "IN1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getINSURANCE().getIN2(), "IN2", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getINSURANCE().getIN3(), "IN3", jsonOutput);

            segmentProcessClass.processSegment(message.getPATIENT().getGT1(), "GT1", jsonOutput);
            segmentProcessClass.processSegment(message.getPATIENT().getAL1(), "AL1", jsonOutput);

            segmentProcessClass.processSegment(message.getORDER().getORC(), "ORC", jsonOutput);

            segmentProcessClass.processSegment(message.getORDER().getORDER_DETAIL().getOBR(), "OBR", jsonOutput);

            segmentProcessClass.processSegment(message.getORDER().getORDER_DETAIL().getRQD(), "RQD", jsonOutput);
            segmentProcessClass.processSegment(message.getORDER().getORDER_DETAIL().getRQ1(), "RQ1", jsonOutput);
            segmentProcessClass.processSegment(message.getORDER().getORDER_DETAIL().getRXO(), "RXO", jsonOutput);
            segmentProcessClass.processSegment(message.getORDER().getORDER_DETAIL().getODS(), "ODS", jsonOutput);
            segmentProcessClass.processSegment(message.getORDER().getORDER_DETAIL().getODT(), "ODT", jsonOutput);

            segmentProcessClass.processSegment(message.getORDER().getCTI(), "CTI", jsonOutput);
            segmentProcessClass.processSegment(message.getORDER().getBLG(), "BLG", jsonOutput);

            JsonArray dg1Array = new JsonArray();
            JsonArray nteArray = new JsonArray();

            for (int i = 0; i < message.getORDER().getORDER_DETAIL().getNTEReps(); i++) {
                JsonObject nteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getORDER().getORDER_DETAIL().getNTE(i), "NTE",
                        nteSegmentJson);

                nteArray.add(nteSegmentJson);

            }
            jsonOutput.add("Notes", nteArray);

            // DG1-Repeat
            for (int i = 0; i < message.getORDER().getORDER_DETAIL().getDG1Reps(); i++) {

                JsonObject dg1SegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getORDER().getORDER_DETAIL().getDG1(i), "DG1",
                        dg1SegmentJson);

                dg1Array.add(dg1SegmentJson);
            }
            jsonOutput.add("Diagnosis", dg1Array);

            segmentProcessClass.processSegment(message.getORDER().getORDER_DETAIL().getOBSERVATION().getOBX(), "OBX",
                    jsonOutput);

            // OBX-Repeats
            for (int i = 0; i < message.getORDER().getORDER_DETAIL().getOBSERVATION().getNTEReps(); i++) {
                JsonObject nteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getORDER().getORDER_DETAIL().getOBSERVATION().getNTE(i),
                        "NTE", nteSegmentJson);

                nteArray.add(nteSegmentJson);

            }
            jsonOutput.add("Notes", nteArray);

        }

        return jsonOutput;
    }

    public static Object ORU(String messageType, String triggerEvent, Message parsedMessage) throws HL7Exception {
        // HL7 v2.3 - ADT_A01 - Admit/visit notification
        JsonObject jsonOutput = new JsonObject();
        if (messageType.equals("ORU") && triggerEvent.equals("R01")) {
            ORU_R01 message = (ORU_R01) parsedMessage;

            segmentProcessClass.processSegment(message.getMSH(), "MSH", jsonOutput);

            segmentProcessClass.processSegment(message.getRESPONSE().getPATIENT().getPID(), "PID", jsonOutput);
            segmentProcessClass.processSegment(message.getRESPONSE().getPATIENT().getPD1(), "PD1", jsonOutput);
            segmentProcessClass.processSegment(message.getRESPONSE().getPATIENT().getNTE(), "NTE", jsonOutput);

            segmentProcessClass.processSegment(message.getRESPONSE().getPATIENT().getVISIT().getPV1(), "PV1",
                    jsonOutput);
            segmentProcessClass.processSegment(message.getRESPONSE().getPATIENT().getVISIT().getPV2(), "PV2",
                    jsonOutput);

            segmentProcessClass.processSegment(message.getRESPONSE().getORDER_OBSERVATION().getORC(), "ORC",
                    jsonOutput);

            segmentProcessClass.processSegment(message.getRESPONSE().getORDER_OBSERVATION().getOBR(), "OBR",
                    jsonOutput);
            // segmentProcessClass.processSegment(message.getRESPONSE().getORDER_OBSERVATION().getNTE(),
            // "NTE",jsonOutput);
            JsonArray nteArray = new JsonArray();

            for (int i = 0; i < message.getRESPONSE().getORDER_OBSERVATION().getNTEReps(); i++) {
                JsonObject nteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(message.getRESPONSE().getORDER_OBSERVATION().getNTE(i), "NTE",
                        nteSegmentJson);

                nteArray.add(nteSegmentJson);

            }
            jsonOutput.add("Notess", nteArray);

            segmentProcessClass.processSegment(message.getRESPONSE().getORDER_OBSERVATION(0).getCTI(), "CTI",
                    jsonOutput);

            segmentProcessClass.processSegment(message.getDSC(), "DSC", jsonOutput);

            JsonArray obxArray = new JsonArray();
            for (int i = 0; i < message.getRESPONSE().getORDER_OBSERVATION().getOBSERVATIONReps(); i++) {
                JsonObject obxSegmentJson = new JsonObject();
                segmentProcessClass.processSegment(
                        message.getRESPONSE().getORDER_OBSERVATION().getOBSERVATION(i).getOBX(), "OBX",
                        obxSegmentJson);
                obxArray.add(obxSegmentJson);
            }
            jsonOutput.add("Observations", obxArray);

            JsonArray onteArray = new JsonArray();

            for (int i = 0; i < message.getRESPONSE().getORDER_OBSERVATION().getOBSERVATION().getNTEReps(); i++) {
                JsonObject nteSegmentJson = new JsonObject();

                segmentProcessClass.processSegment(
                        message.getRESPONSE().getORDER_OBSERVATION().getOBSERVATION().getNTE(i), "NTE", nteSegmentJson);

                nteArray.add(nteSegmentJson);

            }
            jsonOutput.add("Notes", nteArray);

            segmentProcessClass.processSegment(message.getDSC(), "DSC", jsonOutput);
        }

        return jsonOutput;
    }

    public static Object DFT(String messageType, String triggerEvent, Message parsedMessage) throws HL7Exception {
        // HL7 v2.3 - ADT_A01 - Admit/visit notification
        JsonObject jsonOutput = new JsonObject();

        return jsonOutput;
    }

    public static Object MFN(String messageType, String triggerEvent, Message parsedMessage) throws HL7Exception {
        // HL7 v2.3 - ADT_A01 - Admit/visit notification
        JsonObject jsonOutput = new JsonObject();

        return jsonOutput;
    }

}
