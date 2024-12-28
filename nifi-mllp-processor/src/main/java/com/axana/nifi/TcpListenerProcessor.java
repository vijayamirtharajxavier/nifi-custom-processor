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
import ca.uhn.hl7v2.model.v23.segment.MSA;
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
        getLogger().info("Starting TCP Listener on port {}", new Object[] { port });

        executorService = Executors.newSingleThreadExecutor();

        executorService.submit(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                this.serverSocket = serverSocket;
                getLogger().info("TCP Listener started on port {}", new Object[] { port });
                while (running.get()) {
                    Socket clientSocket = serverSocket.accept();
                    handleClient(clientSocket);
                }
            } catch (IOException e) {
                if (running.get()) {
                    getLogger().error("Error in TCP Listener", e);
                }
            }
        });
    }

    private void handleClient(Socket clientSocket) {
        try {
            byte[] buffer = new byte[1024];
            int read = clientSocket.getInputStream().read(buffer);
            if (read > 0) {
                String receivedData = new String(buffer, 0, read, StandardCharsets.UTF_8).trim();
                messageQueue.offer(receivedData);
                getLogger().info("Received HL7 Message: {}", receivedData);

                PipeParser parser = new PipeParser();
                try {
                    Message inboundMessage;
                    inboundMessage = parser.parse(receivedData);
                    // Create a default ACK message dynamically for the inbound message

                    // Send ACK
                    sendAck(clientSocket, receivedData);

                } catch (HL7Exception e) {
                     String messageControlID = extractMessageControlId(receivedData);
                     getLogger().info("MSG_ID : " + messageControlID);
                     String[] split_emsg = e.getMessage().split(":");
                     String errorDescription = split_emsg[2];
                   sendNAck(clientSocket, receivedData,messageControlID,errorDescription);
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

    private void sendNAck(Socket clientSocket, String receivedMessage, String messageControlId, String errorDescription) {
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

    private void sendAck(Socket clientSocket, String receivedMessage) {
        String messageControlID = "";

        try {
            // boolean isValid = validateHL7Message(receivedMessage);
            // if (isValid) {
            // throw new IllegalArgumentException("HL7 validation failed for the message.");

            // Parse the incoming HL7 message dynamically
            PipeParser parser = new PipeParser();
            Message inboundMessage = parser.parse(receivedMessage);

            // Create a default ACK message dynamically for the inbound message
            Message ackMessage = inboundMessage.generateACK();

            // Extract the MSH fields from the inbound message
            MSH inboundMSH = (MSH) inboundMessage.get("MSH");
            String fieldSeparator = inboundMSH.getFieldSeparator().getValue();
            String encodingCharacters = inboundMSH.getEncodingCharacters().getValue();
            messageControlID = inboundMSH.getMessageControlID().getValue();
            String versionID = inboundMSH.getVersionID().getValue();

            // Set ACK message MSH fields dynamically based on the inbound message
            MSH ackMSH = (MSH) ackMessage.get("MSH");
            ackMSH.getFieldSeparator().setValue(fieldSeparator);
            ackMSH.getEncodingCharacters().setValue(encodingCharacters);
            ackMSH.getVersionID().setValue(versionID);
            ackMSH.getMessageControlID().setValue(messageControlID);

            // Set MSA fields dynamically
            MSA msa = (MSA) ackMessage.get("MSA");
            msa.getMessageControlID().setValue(messageControlID);

            // Encode the ACK message to a string
            String ackMessageString = parser.encode(ackMessage);

            // Wrap the ACK message with MLLP delimiters
            String ackWithMLLP = "\u000B" + ackMessageString + "\u001C\r";

            // Send the ACK back to the client
            clientSocket.getOutputStream().write(ackWithMLLP.getBytes(StandardCharsets.UTF_8));
            clientSocket.getOutputStream().flush();

            // Log the ACK message being sent
            getLogger().info("Sent ACK message: {}", ackMessageString);

        } catch (Exception e) {

            // Log the ACK message being sent
            getLogger().error("Error generating or sending NACK", e);

        }
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
                // Write the message to the FlowFile
                flowFile = session.write(flowFile, out -> out.write(message.getBytes(StandardCharsets.UTF_8)));

                // Validate the message
                 boolean isValid = validateHL7Message(message,flowFile,session);
                
                 if (!isValid) {
                 
                 throw new IllegalArgumentException("HL7 validation failed for the message.");
                 }
                 

                // Transfer the FlowFile to the success relationship
                session.transfer(flowFile, REL_SUCCESS);
            } catch (Exception e) {
               // getLogger().error("Failed to process or validate the message: " + message, e);
            }
        }
    }

    /**
     * Validates an HL7 message.
     * 
     * @param message The HL7 message to validate.
     * @return true if the message is valid, false otherwise.
     */
    private boolean validateHL7Message(String message, FlowFile flowFile, ProcessSession session)
            throws ValidationException {
        try {
            // Create HAPI context and parser
            HapiContext context = new DefaultHapiContext();
            PipeParser parser = context.getPipeParser();

            // Parse the HL7 message
            Message hl7Message = parser.parse(message);

            // Create the validator
            DefaultValidator validator = new DefaultValidator(context);

            // Perform the validation
            validator.validate(hl7Message);
            flowFile = session.write(flowFile, out -> out.write(message.getBytes(StandardCharsets.UTF_8)));

            // If no exceptions are thrown, the message is valid
            return true;
        } catch (Exception e) {
            // Handle other exceptions, like HL7Exception
            if (flowFile != null) {
                // ca.uhn.hl7v2.validation.ValidationException: Validation failed: Primitive
                // value 'F' requires to be empty or a HL7 datetime string at OBR-22(0)
                String error_msg = e.getMessage().toString();
                String[] errorMessage = error_msg.split(":");
                getLogger().info("ErrMSG >>-0 : " + errorMessage[0]);
                getLogger().info("ErrMSG -1 : " + errorMessage[1]);
                getLogger().info("ErrMSG>>> -2 : " + errorMessage[2]);

                flowFile = session.putAttribute(flowFile, "validation.error", errorMessage[2]);
                flowFile = session.penalize(flowFile);

                session.transfer(flowFile, REL_FAILURE);
            }
            getLogger().error("Error parsing or validating HL7 message: " + e.getMessage(), e);
            return false;
        }
    }

    private String createNackMessage(String messageControlId, String errorDescription) {
        String nackMessage = "MSH|^~\\&|SendingSystem|SendingFacility|ReceivingSystem|ReceivingFacility|"
                + getCurrentDateTime() + "||ACK|" + messageControlId + "|P|2.3|\n"
                + "MSA|AE|" + messageControlId + "|" + errorDescription + "|\n"
                + "ERR|" + errorDescription + "|\n";
        getLogger().info("NackMethod : " + nackMessage);
        return nackMessage;
    }

    private String getCurrentDateTime() {
        // Return the current timestamp in HL7 format (e.g., "yyyyMMddHHmmss")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date());
    }

    public static String extractMessageControlId(String hl7Message) {
        // Split the message into segments using the newline character
        String[] segments = hl7Message.split("\n");

        // Find the MSH segment (typically the first segment)
        for (String segment : segments) {
            if (segment.startsWith("MSH|")) {
                // Split the MSH segment into fields using the pipe character "|"
                String[] fields = segment.split("\\|");

                // Ensure there are enough fields in the MSH segment (10th field is the Message
                // Control ID)
                if (fields.length >= 10) {
                    // MSH-10 is the Message Control ID (10th field, index 9)
                    return fields[9]; // Return the Message Control ID
                } else {
                    // If there aren't enough fields, return an error message
                    return "Error: MSH-10 field is missing or invalid.";
                }
            }
        }

        // If no MSH segment is found
        return "Error: MSH segment not found.";
    }

}
