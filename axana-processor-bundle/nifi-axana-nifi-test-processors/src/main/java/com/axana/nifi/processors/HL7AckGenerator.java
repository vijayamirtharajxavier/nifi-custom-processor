package com.axana.nifi.processors;

import java.io.IOException;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v23.message.ACK;
import ca.uhn.hl7v2.parser.Parser;

public class HL7AckGenerator {

    public String generateAck(String incomingMessage) throws HL7Exception {
        // Create a HAPI context
        DefaultHapiContext context = new DefaultHapiContext();
        Parser parser = context.getPipeParser();

        // Parse the incoming HL7 message
        Message message = parser.parse(incomingMessage);

        // Generate an ACK message
        ACK ack = null;
        try {
            ack = (ACK) message.generateACK();
        } catch (HL7Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Convert ACK message to a string
        return parser.encode(ack);
    }
}
