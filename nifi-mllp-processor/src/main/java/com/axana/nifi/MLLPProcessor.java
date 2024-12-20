package com.axana.nifi;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.nifi.annotation.behavior.WritesAttribute;
import org.apache.nifi.annotation.behavior.WritesAttributes;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.util.StandardValidators;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

@Tags({ "HL7", "MLLP", "Custom", "NiFi" })
@CapabilityDescription("Processes HL7 messages over MLLP")
@WritesAttributes({
        @WritesAttribute(attribute = "hl7.message", description = "The HL7 message content."),
        @WritesAttribute(attribute = "hl7.ack", description = "The HL7 acknowledgment message."),
        @WritesAttribute(attribute = "error.message", description = "Error message if any.")
})
public class MLLPProcessor extends AbstractProcessor {

    public static final PropertyDescriptor PORT = new PropertyDescriptor.Builder()
            .name("MLLP Port")
            .description("The port on which the processor listens for incoming HL7 messages.")
            .required(true)
            .addValidator(StandardValidators.POSITIVE_INTEGER_VALIDATOR)
            .build();

    public static final Relationship REL_SUCCESS = new Relationship.Builder()
            .name("success")
            .description("Successfully processed HL7 messages.")
            .build();

    public static final Relationship REL_FAILURE = new Relationship.Builder()
            .name("failure")
            .description("Failed to process HL7 messages.")
            .build();

    private volatile boolean running = true;

    @Override
    public Set<Relationship> getRelationships() {
        final Set<Relationship> relationships = new HashSet<>();
        relationships.add(REL_SUCCESS);
        relationships.add(REL_FAILURE);
        return relationships;
    }

    @Override
    public List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return Collections.singletonList(PORT);
    }

    @Override
    public void onTrigger(ProcessContext context, ProcessSession session) {
        int port = context.getProperty(PORT).asInteger();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            getLogger().info("MLLP Processor started on port " + port);

            while (running) {
                try (Socket socket = serverSocket.accept()) {
                    getLogger().info("Connection received.");

                    // Receive HL7 Message
                    byte[] buffer = new byte[1024];
                    int read = socket.getInputStream().read(buffer);
                    String hl7Message = new String(buffer, 0, read).trim();
                    // Ensure the message starts with the VT character (ASCII 11)
                    String hl7_Message = "\u000B" + hl7Message;

                    getLogger().info("Received HL7 Message: " + hl7Message);

                    // Parse HL7 Message
                    Parser parser = new PipeParser();
                    Message message = parser.parse(hl7Message);

                    // Generate HL7 ACK
                    Message ackMessage = message.generateACK();
                    String ackMessageString = ackMessage.encode();
                    String messageWithVT = "\u000B" + ackMessageString + "\u001C\r";
                    try {
                        // Create the FlowFile
                        FlowFile flowFile = session.create();

                        // Set the content of the FlowFile to the HL7 message
                        flowFile = session.write(flowFile, (OutputStream out) -> {
                            out.write(hl7Message.getBytes(StandardCharsets.UTF_8)); // Write message to FlowFile content
                        });

                        // Optionally set the HL7 message as an attribute as well (for metadata)
                        flowFile = session.putAttribute(flowFile, "hl7.message", hl7Message);

                        // Transfer the FlowFile to the 'success' relationship
                        session.transfer(flowFile, REL_SUCCESS);

                        // Commit the session
                        session.commit();
                        getLogger().info("FlowFile with HL7 message successfully created and transferred.");

                    } catch (Exception e) {
                        getLogger().error("Error processing HL7 message: " + e.getMessage(), e);
                        session.rollback();
                    }
                    // Create FlowFile
                    /*
                     * FlowFile flowFile = session.create();
                     * flowFile = session.putAttribute(flowFile, "hl7.message", hl7Message);
                     * session.transfer(flowFile, REL_SUCCESS);
                     * session.commit(); // Ensure the session is committed
                     * 
                     * FlowFile flowFile = session.create();
                     * flowFile = session.putAttribute(flowFile, "hl7.message", hl7Message);
                     * flowFile = session.putAttribute(flowFile, "hl7.ack", ackMessage.encode());
                     * 
                     * // Write HL7 Message to FlowFile
                     * flowFile = session.write(flowFile, out -> out.write(hl7Message.getBytes()));
                     * 
                     * session.transfer(flowFile, REL_SUCCESS);
                     */

                    // getLogger().info("FlowFile transferred to success relationship: " +
                    // flowFile);
                    // Send ACK back to client
                    socket.getOutputStream().write(messageWithVT.getBytes());
                    socket.getOutputStream().flush();

                    getLogger().info("ACK---- sent.");
                }
            }
        } catch (Exception e) {
            getLogger().error("Error processing HL7 message: " + e.getMessage(), e);

            FlowFile flowFile = session.create();
            flowFile = session.putAttribute(flowFile, "error.message", e.getMessage());
            session.transfer(flowFile, REL_FAILURE);
        }
    }

}
