package com.axana.nifi.processors;
import java.io.IOException;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v23.message.ACK;
import ca.uhn.hl7v2.parser.PipeParser;






public class HL7AckGenerator {

    public String generateAck(String incomingMessage) throws HL7Exception, IOException {
    //Create a HAPI context
    DefaultHapiContext context = new DefaultHapiContext();
    PipeParser parser = context.getPipeParser();
    

    //Parse  the incoming HL7 message
    Message message = parser.parse(incomingMessage);

    //Generate an ACK message
    ACK ack = null;
    try {
        ack = (ACK) message.generateACK();
    } catch (Exception e) {
        // TODO: handle exception
        e.printStackTrace();
    }

    return parser.encode(ack);
    }


}
