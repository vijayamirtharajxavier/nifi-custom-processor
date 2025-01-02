package com.axana.nifi;

import org.apache.nifi.processor.AbstractProcessor;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.model.v23.segment.MSH;
import ca.uhn.hl7v2.model.v23.message.ACK;
import ca.uhn.hl7v2.validation.ValidationException;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.io.StreamCallback;
import org.apache.nifi.processor.util.StandardValidators;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.annotation.lifecycle.OnStopped;
import org.apache.nifi.annotation.lifecycle.OnUnscheduled;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.logging.ComponentLog;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class HL7TcpListenerProcessor extends AbstractProcessor {

    private ExecutorService executorService;
    private static final int MAX_THREADS = 10; // Max number of concurrent threads for processing
    private static final String ACKNOWLEDGMENT_MESSAGE = "MSH segment validated, sending ACK response.";
    private static final String ERROR_MESSAGE = "MSH segment validation failed.";
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private ServerSocket serverSocket;
    boolean isValid = false;
    String errorMessage="";

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
        FlowFile flowFile = session.get();

        if (flowFile == null) {
            getLogger().info("FlowFile is Empty");
            return;
        }
        String message = messageQueue.poll();
        if (message != null) {
            getLogger().info(">>>Message Recerived on Trigger : " + message);
//FlowFile flowFile = session.create();
            try {
                getLogger().info("IS_VALID ? : " + isValid + ", Error Mesage : " + errorMessage);
                // Process the FlowFile
                if(isValid)
                {
                flowFile = session.write(flowFile, out -> out.write(message.getBytes(StandardCharsets.UTF_8)));
                // If the flow file is marked for transfer, check if it’s already processed
                session.transfer(flowFile, REL_SUCCESS);
                }
                else {
                         flowFile = session.putAttribute(flowFile, "validation:error",errorMessage);
                         flowFile = session.penalize(flowFile);
                         session.transfer(flowFile, REL_FAILURE);
                }
            } catch (Exception e) {
                flowFile = session.putAttribute(flowFile, "validation:error",errorMessage);
                getLogger().error("Error processing FlowFile", e);
                session.transfer(flowFile, REL_FAILURE);
            }
        }
    }




    private void handleClient(Socket clientSocket) {
        try {
            byte[] buffer = new byte[1024];
            int read = clientSocket.getInputStream().read(buffer);
            if (read > 0) {
                String receivedData = new String(buffer, 0, read, StandardCharsets.UTF_8).trim();
                messageQueue.offer(receivedData);
                getLogger().info(">>>>Received >>>>HL7 Message: {}", receivedData);

                PipeParser parser = new PipeParser();
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

    public static String extractMessageControlId(String hl7Message) {
        // Extract Message Control ID from the MSH segment
        String[] segments = hl7Message.split("\n");
        for (String segment : segments) {
            if (segment.startsWith("MSH|")) {
                String[] fields = segment.split("\\|");
                if (fields.length > 9) {
                    return fields[9]; // Return the Message Control ID
                } else {
                    // Log and return an error message if the MSH segment is malformed
                    return "Error: MSH segment does not contain enough fields.";
                }
            }
        }
        return "Error: MSH segment not found.";
    }

    private void handleClientConnection(Socket socket, ProcessSession session, FlowFile flowFile) {
        try (InputStream inputStream = socket.getInputStream()) {
            // Read incoming data from the input stream
            byte[] data = inputStream.readAllBytes();  // Reads the data sent by the client
    
            // Log the size of the data received
            getLogger().info("Received {} bytes of data", data.length);
    
            // Write the incoming data to the FlowFile
            session.write(flowFile, (OutputStream out) -> {
                out.write(data);  // Write the incoming data to the FlowFile
            });
    
            // Transfer the FlowFile to the success relationship
            session.transfer(flowFile, REL_SUCCESS);
            getLogger().info("FlowFile created and transferred successfully.");
        } catch (IOException e) {
            getLogger().error("Error processing client data", e);
            // Transfer to failure if there's an error
            session.transfer(flowFile, REL_FAILURE);
        } finally {
            try {
                socket.close();  // Close the socket connection after processing
            } catch (IOException e) {
                getLogger().error("Error closing socket", e);
            }
        }
    }
    

    @OnUnscheduled
    public void onUnscheduled(ProcessContext context) {
        running.set(false); // Set the flag to stop the listener loop
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close(); // Properly close the socket
                getLogger().info("ServerSocket closed successfully.");
            }
        } catch (IOException e) {
            getLogger().error("Error closing the server socket", e);
        }
    }

    // Helper method to convert InputStream to String
    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        }
        return stringBuilder.toString();
    }

    private boolean isMSHValid(MSH msh) {
        try {
            // Validate required fields in MSH segment
            String msh1 = msh.getMsh1_FieldSeparator().getValue();
            String msh2 = msh.getMsh2_EncodingCharacters().getValue();
            String msh3 = msh.getMsh3_SendingApplication().encode();
            String msh4 = msh.getMsh4_SendingFacility().encode();

            // Ensure required fields are not null or empty
            if (msh1 == null || msh2 == null || msh3 == null || msh4 == null) {
                return false; // MSH validation fails if any required field is missing
            }

            return true; // MSH is valid if all required fields are present
        } catch (Exception e) {
            getLogger().error("Error validating MSH segment", e);
            return false; // Return false if there is an exception during validation
        }
    }
}
