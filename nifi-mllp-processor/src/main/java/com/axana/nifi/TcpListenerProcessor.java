package com.axana.nifi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.nifi.annotation.behavior.InputRequirement;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.annotation.lifecycle.OnStopped;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.util.StandardValidators;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v23.segment.MSH;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.validation.DefaultValidator;
import ca.uhn.hl7v2.validation.ValidationException;

@Tags({ "tcp-mllp", "listener", "network" })
@CapabilityDescription("Custom NiFi processor to listen for TCP connections and process HL7 messages.")
@InputRequirement(InputRequirement.Requirement.INPUT_FORBIDDEN)
public class TcpListenerProcessor extends AbstractProcessor {

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    String errorDescription = "";
    boolean isValid=false;

    public static final PropertyDescriptor PORT = new PropertyDescriptor.Builder()
            .name("Port")
            .description("Port on which the TCP listener will accept connections.")
            .required(true)
            .addValidator(StandardValidators.PORT_VALIDATOR)
            .build();

    public static final Relationship REL_SUCCESS = new Relationship.Builder()
            .name("success")
            .description("Successfully processed HL7 messages.")
            .build();

    public static final Relationship REL_FAILURE = new Relationship.Builder()
            .name("failure")
            .description("Error processing HL7 messages.")
            .build();

    @Override
    protected List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return List.of(PORT);
    }

    @Override
    public Set<Relationship> getRelationships() {
        Set<Relationship> relationships = new HashSet<>();
        relationships.add(REL_SUCCESS);
        relationships.add(REL_FAILURE);
        return relationships;
    }

    @OnScheduled
    public synchronized void start(final ProcessContext context) {
        int port = context.getProperty(PORT).asInteger();
        running.set(true);
        getLogger().info("Starting TCP Listener on port {}", port);

        executorService = Executors.newCachedThreadPool(); // Use cached thread pool for handling multiple clients

        executorService.submit(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                this.serverSocket = serverSocket;
                getLogger().info("TCP Listener started on port {}", port);
                while (running.get()) {
                    Socket clientSocket = serverSocket.accept();
                    executorService.submit(() -> handleClient(clientSocket)); // Submit to the pool
                }
            } catch (IOException e) {
                if (running.get()) {
                    getLogger().error("Error in TCP Listener", e);
                }
            }
        });
    }
    private void handleClient(Socket clientSocket) {
        boolean validate_result = false;
        try {
            byte[] buffer = new byte[1024];
            int read = clientSocket.getInputStream().read(buffer);
            if (read > 0) {
                String receivedData = new String(buffer, 0, read, StandardCharsets.UTF_8).trim();
                messageQueue.offer(receivedData);
                getLogger().info("Received HL7 Message: {}", receivedData);

                PipeParser parser = new PipeParser();
                try {
                    Message inboundMessage = parser.parse(receivedData);

                    // Message hl7Message = parser.parse(receivedData);
                    // MSH mshSegment = (MSH) inboundMessage.get("MSH");
                    // validate_result = isMSHValid(mshSegment);
                getLogger().info("ISVALID_HANDLECLIENT : " + isValid);
                    if (isValid == true) {
                        // Process the message and send ACK
                        sendAck(clientSocket, receivedData);

                    } else {
                        getLogger().info("ELSE_HANDLECLIENT : " + isValid);

                        // String messageControlID = extractMessageControlId(receivedData);
                        // getLogger().info("MSG_ID : " + messageControlID);
                        // String[] split_emsg = e.getMessage().split(":");
                        // String errorDescription = split_emsg[2];
                        sendNAck(clientSocket, receivedData, errorDescription);

                    }

                } catch (HL7Exception e) {
                    String messageControlID = extractMessageControlId(receivedData);
                    getLogger().info("MSG_ID : " + messageControlID);
                     String[] split_emsg = e.getMessage().split(":");
                     String errorDescription = split_emsg[2];
                    sendNAck(clientSocket, receivedData, errorDescription);
                }
            }
        } catch (IOException e) {
            getLogger().error("Error handling client connection", e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                getLogger().warn("Error closing client socket", e);
            }
        }
    }

    private void sendNAck(Socket clientSocket, String receivedMessage, String errorDescription) {
        String messageControlId = extractMessageControlId(receivedMessage);
        getLogger().info("NACK OUTPU : " + receivedMessage);
        // Wrap the ACK message with MLLP delimiters

        String nackMessage = "MSH|^~\\&|SendingSystem|SendingFacility|ReceivingSystem|ReceivingFacility|"
                + getCurrentDateTime() + "||ACK|" + messageControlId + "|P|2.3|\n"
                + "MSA|AE|" + messageControlId + "|" + errorDescription + "|\n"
                + "ERR|" + errorDescription + "|\n";
        getLogger().info("NackMethod : " + nackMessage);

        String ackWithMLLP = "\u000B" + nackMessage + "\u001C\r";

        // Send the ACK back to the client
        try {
            clientSocket.getOutputStream().write(ackWithMLLP.getBytes(StandardCharsets.UTF_8));
            clientSocket.getOutputStream().flush();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Log the ACK message being sent
        getLogger().info("Sent NACK message: {}", nackMessage);

    }


    private void sendAck(Socket clientSocket, String receivedData) throws HL7Exception {
        PipeParser parser = new PipeParser();

        Message inboundMessage = parser.parse(receivedData);
        String messageControlID = extractMessageControlId(receivedData);
        if (messageControlID == null) {
            getLogger().error("Incomplete MSH segment: ");

        } else {

            String ackMessage = generateAckMessage(inboundMessage, messageControlID);

            // Wrap the ACK message with MLLP delimiters
            String ackWithMLLP = "\u000B" + ackMessage + "\u001C\r";
            try {
                clientSocket.getOutputStream().write(ackWithMLLP.getBytes(StandardCharsets.UTF_8));
                clientSocket.getOutputStream().flush();
                getLogger().info("Sent ACK message: {}", ackMessage);
            } catch (IOException e) {
                getLogger().error("Error sending ACK", e);
            }
        }

    }

    private String generateAckMessage(Message inboundMessage, String messageControlID) {
        // Build your ACK message here based on inbound message and messageControlID
        // This is an example template, adjust as per your needs
        return "MSH|^~\\&|SendingSystem|SendingFacility|ReceivingSystem|ReceivingFacility|"
                + getCurrentDateTime() + "||ACK|" + messageControlID + "|P|2.3|\n"
                + "MSA|AA|" + messageControlID + "|\n";
    }

    private String generateNackMessage(String messageControlId, String errorDescription) {
        // Similar to ACK generation but for NACK
        return "MSH|^~\\&|SendingSystem|SendingFacility|ReceivingSystem|ReceivingFacility|"
                + getCurrentDateTime() + "||ACK|" + messageControlId + "|P|2.3|\n"
                + "MSA|AE|" + messageControlId + "|" + errorDescription + "|\n"
                + "ERR|" + errorDescription + "|\n";
    }


    @OnStopped
    public synchronized void stop() {
        running.set(false);
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                getLogger().warn("Error closing server socket", e);
            }
        }
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }

    @Override
    public void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {
        String message = messageQueue.poll();
        if (message != null) {
            FlowFile flowFile = session.create();
            try {
                flowFile = session.write(flowFile, out -> out.write(message.getBytes(StandardCharsets.UTF_8)));
                 isValid = validateHL7Message(message, flowFile, session);
                getLogger().info("IsValidated : " + isValid);
                if (isValid == true) {
                    getLogger().info("TRUE CONDITION : " + isValid);
                    session.transfer(flowFile, REL_SUCCESS);

                } else {
                    getLogger().info("ELSE-FALSE CONDITION : " + isValid);
                    flowFile = session.penalize(flowFile); // Penalize first
                    session.transfer(flowFile, REL_FAILURE); // Then transfer
                }
            } catch (Exception e) {
                getLogger().error("Failed to process or validate the message: " + message, e);
                if (flowFile != null) {
                    getLogger().error("FlowFile details: " + flowFile.getAttribute("uuid"));
                }
            }
        }
    }


    private boolean isMSHValid(MSH msh, FlowFile flowFile, ProcessSession session) {
        try {
            // Validate required fields in MSH segment
            String msh1 = msh.getMsh1_FieldSeparator().getValue();
            String msh2 = msh.getMsh2_EncodingCharacters().getValue();
            String msh3 = msh.getMsh3_SendingApplication().encode();
            String msh4 = msh.getMsh4_SendingFacility().encode();
            String msh5 = msh.getMsh5_ReceivingApplication().encode();
            String msh6 = msh.getMsh6_ReceivingFacility().encode();
            String msh7 = msh.getMsh7_DateTimeOfMessage().getTimeOfAnEvent().getValue();
            String msh9_1 = msh.getMsh9_MessageType().getCm_msg1_MessageType().getValue();
            String msh9_2 = msh.getMsh9_MessageType().getCm_msg1_MessageType().getValue();
            String msh10 = msh.getMsh10_MessageControlID().getValue();
            String msh11 = msh.getMsh11_ProcessingID().encode();
            String msh12 = msh.getMsh12_VersionID().encode();

            getLogger().info("msh_1: " + msh1 + "msg_id : " + msh10);
            if (msh1 == null) {
                flowFile = session.putAttribute(flowFile, "validation.error", "MSH_1 is empty");

                throw new IllegalArgumentException("MSH_1 is empty");
                // return false;
            } else if (msh2 == null) {
                flowFile = session.putAttribute(flowFile, "validation.error", "MSH_2 is empty");
                throw new IllegalArgumentException("MSH_2 is empty");
            } else if (msh3 == null) {
                flowFile = session.putAttribute(flowFile, "validation.error", "MSH_3 is empty");
                throw new IllegalArgumentException("MSH_3 is empty");

            } else if (msh4 == null) {
                flowFile = session.putAttribute(flowFile, "validation.error", "MSH_4 is empty");
                throw new IllegalArgumentException("MSH_4 is empty");

            } else if (msh5 == null) {
                flowFile = session.putAttribute(flowFile, "validation.error", "MSH_5 is empty");
                throw new IllegalArgumentException("MSH_5 is empty");

            } else if (msh6 == null) {
                flowFile = session.putAttribute(flowFile, "validation.error", "MSH_6 is empty");
                throw new IllegalArgumentException("MSH_6 is empty");

            } else if (msh7 == null) {
                flowFile = session.putAttribute(flowFile, "validation.error", "MSH_7 is empty");
                throw new IllegalArgumentException("MSH_7 is empty");

            } else if (msh9_1 == null) {
                flowFile = session.putAttribute(flowFile, "validation.error", "MSH_9_1 is empty");
                throw new IllegalArgumentException("MSH_9_1 is empty");

            } else if (msh9_2 == null) {
                flowFile = session.putAttribute(flowFile, "validation.error", "MSH_9_2 is empty");
                throw new IllegalArgumentException("MSH_9_2 is empty");

            }

            else if (msh11 == null) {
                flowFile = session.putAttribute(flowFile, "validation.error", "MSH_11 is empty");
                throw new IllegalArgumentException("MSH_11 is empty");

            } else if (msh12 == null) {
                flowFile = session.putAttribute(flowFile, "validation.error", "MSH_12 is empty");
                throw new IllegalArgumentException("MSH_12 is empty");

            } else if (msh10 == null) {
                flowFile = session.putAttribute(flowFile, "validation.error", "MSH_10 is empty");
                throw new IllegalArgumentException("MSH_10 is empty");
                

            } else {
                isValid=true;
                getLogger().info("TRUE : msh10: " + msh10);
                return true; // MSH is valid if all required fields are present

            }

        } catch (Exception e) {
            getLogger().error("Error validating MSH segment", e);
            errorDescription=e.getMessage();
            return false; // Return false if there is an exception during validation
        }
    }

    private boolean validateHL7Message(String message, FlowFile flowFile, ProcessSession session)
            throws ValidationException {
        boolean validate_result = false;
        try {
            HapiContext context = new DefaultHapiContext();
            PipeParser parser = context.getPipeParser();
            Message hl7Message = parser.parse(message);
            MSH mshSegment = (MSH) hl7Message.get("MSH");
            validate_result = isMSHValid(mshSegment, flowFile, session);
            getLogger().info("validate_result : " + validate_result);
            return validate_result;
            // DefaultValidator validator = new DefaultValidator(context);
            // validator.validate(hl7Message);
            // return true;
        } catch (Exception e) {
            flowFile = session.putAttribute(flowFile, "validation.error", e.getMessage());
            errorDescription=e.getMessage();
            // flowFile = session.penalize(flowFile);
            // session.transfer(flowFile, REL_FAILURE);
            getLogger().error("Error parsing or validating HL7 message: " + e.getMessage(), e);
            validate_result = false;
            return validate_result;
        }
    }

    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date());
    }

    public static String extractMessageControlId(String hl7Message) {
        // Extract Message Control ID from the MSH segment
        String[] segments = hl7Message.split("\n");
        for (String segment : segments) {
            if (segment.startsWith("MSH|")) {
                String[] fields = segment.split("\\|");
                if (fields.length > 9) {
                    return fields[9]; // Return the Message Control ID
                } else {
                    return null;
                }
            }
        }
        return "Error: MSH segment not found.";
    }

}
