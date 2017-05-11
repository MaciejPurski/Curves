package Server;

import java.io.IOException;
import java.util.Scanner;
import Server.Controller.*;
/**
 * Created by maciej on 22.03.17.
 */
public class ServerApp {
    public static void main(String[] args) throws IOException {

        int nplayers;
        Scanner in = new Scanner(System.in);

        System.out.println("Give number of players: ");
        nplayers = in.nextInt();
        /*if (args.length != 1) {
            System.out.println("Usage: java Server <number of players>");
            return;
        }*/

        ServerController serverController = new ServerController (nplayers);
        serverController.start();
    }
}
