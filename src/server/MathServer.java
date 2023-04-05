package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static constants.ProtocolConstants.PORT;

/**
 * MathServer class designed to handle multiple math clients,
 * implements the singleton pattern as only 1 server should exist per application
 * this class instantiates different threads who handle different clients
 */
public class MathServer {
    private MathServer() {}

    private static MathServer instance = new MathServer();

    public static MathServer getInstance() {
        if (instance == null) {
            synchronized (MathServer.class) {
                if (instance == null) {
                    instance = new MathServer();
                }
            }
        }
        return instance;
    }

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private final List<MathOperationsThread> mathClientThreads = new ArrayList<>();

    /**
     * A start function that initializes the server in the specified port,
     * loops while waiting for clients and creates a new thread for each new client
     */
    public void start(int port) {
        try {
            //crate ServerSocket
            serverSocket = new ServerSocket(port);
            System.out.println("Math server is up on port: " + PORT);
        } catch (IOException e) {
            System.out.println("I/O error: " + e);
        }

        while (true) {
            try {
                //server listen to client:
                //(accept() method going to block until a client connects to the server)
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            MathOperationsThread socketWrappingThread = new MathOperationsThread(clientSocket);
            socketWrappingThread.start();
            mathClientThreads.add(socketWrappingThread);
        }
    }

    /**
     * A stop function that kills all threads in the server before shutting down the server
     */
    public void stop() {
        mathClientThreads.forEach(MathOperationsThread::kill);
    }
}
