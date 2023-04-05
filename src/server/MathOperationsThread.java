package server;

import exceptions.InvalidCommandException;
import operations.MathOperation;
import operations.factory.MathOperationFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import static constants.ProtocolConstants.QUIT_COMMAND;
import static constants.ProtocolConstants.RESPONSE_END;

/**
 * MathOperationsThread class, designed to provide services for a single math-client-app
 * this class is the one the communicates de facto with math-app-client
 */
public class MathOperationsThread extends Thread {
    private final Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public MathOperationsThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * An override method of the Thread class,
     * first, run init() method to communicate with the math-client-app
     * and then, runs the eventLoop() that manage the main operations of the client requests
     */
    @Override
    public void run() {
        init();
        eventLoop();
    }

    /**
     * An init method that establishes and run the i/o commands
     * it also responsible to alert the scheduled task - runLivenessPing() to start running
     */
    private void init() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
            System.out.println("Established client connection");
            runLivenessPing();
        } catch (IOException e) {
            System.out.println("Failed to initialize connection");
        }
    }

    /**
     * This method starts a scheduled task that sends a liveness to the client every 60 seconds
     */
    private void runLivenessPing() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                System.out.println("Running liveness");
                sendInfo("Still alive");
            }
        }, 0, 60000);
    }

    /**
     * The eventLoop() method manage the operations received from the client:
     * runs in a loop and keeps getting requests from the client,
     * parsing them into commands,
     * performing operations
     * and sending a response
     */
    private void eventLoop() {
        while (true) {
            try {
                String unparsedCommand = handleRequest();
                System.out.println("Received command: " + unparsedCommand);
                if (QUIT_COMMAND.equalsIgnoreCase(unparsedCommand)) {
                    kill();
                }

                try {
                    final MathOperation mathOperation = MathOperationFactory.getOperationsFactory().getOperation(unparsedCommand);
                    sendResponse("your result is: " + mathOperation.perform(unparsedCommand));
                } catch (InvalidCommandException e) {
                    sendResponse(e.getMessage());
                }

            } catch (SocketException e) {
                this.kill();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Waits for requests from the client
     *
     * @return a command from the client
     */
    private String handleRequest() throws IOException {
        String line;
        while ((line = in.readLine()).equals("")) {
        }
        return line;
    }

    /**
     * @param messages handle responses sent to the math-client-app
     *                 has an indefinite number of messages to the client(depends on the Tomcat server)
     */
    private void sendResponse(String... messages) {
        Arrays.stream(messages).forEach(message -> out.println(message));
        out.println(RESPONSE_END);
        out.flush();
    }

    /**
     * @param messages handle the info sent by the runLivenessPing() method(scheduled task)
     *                 has an indefinite number of messages to the client(depends on the Tomcat server)
     */
    private void sendInfo(String... messages) {
        Arrays.stream(messages).forEach(message -> out.println("INFO: " + message));
        out.println(RESPONSE_END);
        out.flush();
    }

    /**
     * Kills the thread, ending the connection with the client
     */
    public void kill() {
        System.out.println("Client connection closed");
        this.interrupt();
    }
}
