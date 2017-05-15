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
     *    @param users list of all users connected to the server
     */


    private SynchronizedQueue queue;
    private DatagramSocket socket;
    private InetAddress group;
    private int nUsers;
    private final int serverPort = 9876;



    public ServerThread(String name, int nnUsers) throws IOException {
        super(name);
        socket = new DatagramSocket(serverPort);
        nUsers=nnUsers;
        group = InetAddress.getByName("224.0.0.3");
        queue = new SynchronizedQueue ();
    }


    public void run() {
        byte[] buf = new byte [256];
        //TODO: przerywanie odbioru
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        try {
            while(true)
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


    public String addUser () throws IOException {
        /**
         * Functions which adds one user to the list of users and return the name of the user
         */
        byte[] buf = new byte [256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        String name;
        socket.receive(packet);
        name = new String (buf, 0, packet.getLength());

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
            //System.out.println(message);
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



}