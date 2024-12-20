package com.axana.nifi;

import java.util.Map;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.protocol.ReceivingApplication;
import ca.uhn.hl7v2.protocol.ReceivingApplicationException;

public class CustomReceivingApplication implements ReceivingApplication {
    @Override
    public boolean canProcess(Message message) {
        return true; // Determine if the message can be processed
    }

    @Override
    public Message processMessage(Message message, Map metadata) 
            throws ReceivingApplicationException {
        try {
            System.out.println("Received HL7 message: " + message.encode());
            return message.generateACK();
        } catch (Exception e) {
            throw new ReceivingApplicationException("Failed to process message", e);
        }
    }
}