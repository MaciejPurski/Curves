package Server.Controller;

/**
 * Created by maciej on 22.03.17.
 */
import Packets.MovePacket;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread extends Thread {


    /**
     *    @param socket Main server socket
     *    @param users list of all users connected to the server
     */



    private LinkedList<User> users;
    private DatagramSocket socket;
    private InetAddress group;
    private int nUsers;
    private final int serverPort = 9876;



    public ServerThread(String name, int nnUsers) throws IOException {
        super(name);
        socket = new DatagramSocket(serverPort);
        users = new LinkedList<User>();
        nUsers=nnUsers;
        group = InetAddress.getByName("228.5.6.7");
    }


    public void run() {
        byte[] buf = new byte [8];
        //TODO: przerywanie odbioru
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        try {
            while(true)
            {
                socket.receive(packet);
                MovePacket received = new MovePacket(buf.toString());
                users.get(received.getPlayer()).putPacket(received);
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        socket.close();
    }

    public void fillUsersList() throws IOException {
        /**
         * Function runs until the user list is filled with the number of users requested in the parameter
         * or the time runs out.
         */

        //TODO: Multicast do wszystkich

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

    public void multicastSend(String message) {

        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), group, 9877);
        try {
            socket.send(packet);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<MovePacket> getPackets() {
        ArrayList<MovePacket> result = new ArrayList<MovePacket> ();
        for(User it: users) {
            MovePacket temp = it.getPacket();
            if (temp != null)
                result.add(it.getPacket());
        }
        return result;
    }





}