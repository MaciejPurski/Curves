package Server.Controller;

import java.io.IOException;

/**
 * Created by maciej on 22.03.17.
 */
public class ServerApp {
    public static void main(String[] args) throws IOException {
        ServerThread server = new ServerThread("Server", Integer.parseInt(args[0]));
        server.run();
    }
}
