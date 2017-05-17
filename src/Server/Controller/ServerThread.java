package Server.Controller;

/**
 * Created by maciej on 22.03.17.
 */
import Packets.MovePacket;
import Packets.Packet;
import Packets.SynchronizedQueue;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread extends Thread {


    /**
     *    @param socket Main server socket
     *    @param queue contains all packets which are ready to read
     *    @param group address of the multicast group
     */


    private SynchronizedQueue queue;
    private DatagramSocket socket;
    private InetAddress group;
    private int nUsers;
    private final int serverPort = 9876;
    private boolean isServerOn;



    public ServerThread(String name, int nnUsers) throws IOException {
        super(name);
        socket = new DatagramSocket(serverPort);
        nUsers=nnUsers;
        group = InetAddress.getByName("224.0.0.3");
        queue = new SynchronizedQueue ();
        isServerOn = true;
    }

    /**
     * In this method packets get received and put on the queue
     */

    public void run() {

        try {
            while(isServerOn)
            {
                Packet received = receive();
                queue.put(received);
                System.out.println(received.toString());
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        socket.close();
    }

    /**
     * Functions which adds one user to the list of users and return the name of the user. It resends player index
     */

    public String addUser (int index) throws IOException {

        byte[] buf = new byte [256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        String name;
        socket.receive(packet);
        name = new String (buf, 0, packet.getLength());

        //resend index
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        buf = Integer.toString(index).getBytes();

        packet = new DatagramPacket(buf, 1, address, port);
        socket.send(packet);


        //TODO packet recognition

        return name;
    }



    public void multicastSend(String message) {
        /**
         * Function which sends a message to all users who belong to the mutlicast group
         */

        byte [] buf = new byte [256];
        buf = message.getBytes();

        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 9877);
        try {
            socket.send(packet);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<Packet> getPackets () {
        return queue.getPackets();
    }


    public Packet receive () throws IOException{
        byte [] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket (buf, buf.length);
        socket.receive(packet);
        String string = new String(packet.getData());

        Packet received = Packet.createPacket(string);

        return received;
    }


    public void setServerOn(boolean arg) {
        isServerOn = arg;
    }


}