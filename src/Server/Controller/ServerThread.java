package Server.Controller;

/**
 * Created by maciej on 22.03.17.
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread extends Thread {


    /**
     *    @param socket Main server socket
     *    @param isWaiting server is waiting for new packets
     *    @param users list of all users connected to the server
     */



    private LinkedList<User> users = null;
    private DatagramSocket socket = null;
    private int nUsers;
    private boolean isWaiting = true;


    public ServerThread(String name, int nnUsers) throws IOException {
        super(name);
        socket = new DatagramSocket(9876);
        users = new LinkedList<User>();
        nUsers=nnUsers;

    }

    public void run() {
        try {
            fillUsersList();
        } catch (IOException e)
        {
            e.printStackTrace();
        }


        socket.close();
    }

    private void fillUsersList() throws IOException {
        /**
         * Function runs until the user list is filled with the number of users requested in the parameter
         * or the time runs out.
         */

        byte[] buf = new byte [256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        String name;

        while(users.size() < nUsers) {

            socket.receive(packet);
            InetAddress address = packet.getAddress(); // reading information on the packet sender
            int port = packet.getPort();
            name = new String (buf, 0, packet.getLength());

            boolean found = false;      // find if the packet comes from a new user
            for (User element : users) {
                if(address.equals(element.getAddress()))
                    found = true;
            }

            if(found == false)
                users.add(new User(socket, port, name, address));


        }

    }



}