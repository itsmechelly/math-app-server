import server.MathServer;

import java.lang.*;

import static constants.ProtocolConstants.PORT;

/**
 * This class is the starting point of the math-server-app,
 * this method reference to MathServer class that will be generated an instance of a ServerSocket
 */
public class App {
    public static void main(String[] args) {
        MathServer server = MathServer.getInstance();
        server.start(PORT);
    }
}
