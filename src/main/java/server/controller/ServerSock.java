package server.controller;

import util.packet.IndexPacket;
import util.packet.LoginPacket;
import util.packet.Packet;
import util.packet.ServerStopPacket;
import util.SynchronizedQueue;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Class responsible for server connection with the clients.
 * When put in a new Thread it allows continous packets receiving
 */
public class ServerSock implements Runnable {

    private final int serverPort = 9876;
    private final String groupAddress = "224.0.0.3";

    private SynchronizedQueue queue;
    private DatagramSocket socket;
    private InetAddress group;
    private int nUsers;

    ServerSock() {
        try {
            group = InetAddress.getByName(groupAddress);
            queue = new SynchronizedQueue();
            socket = new DatagramSocket(serverPort);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * In this method packets are received and put on the queue. If the game is finished the running Thread should be interrupted and the infinite loop should
     * be broken. Then message is sent to all clients that the game is finished
     */
    public void run() {
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                multicastSend(new ServerStopPacket().toString());
                break;
            }
            Packet received = receive();
            queue.put(received);
        }
    }

    /**
     * Method which adds one user to the list of users and returns the name of the user. It sends to the new player his index
     *
     * @param index index of the player who should be added
     * @return playerName
     */
    String addUser(int index) throws IOException {
        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        String string = new String(packet.getData());
        Packet received = Packet.createPacket(string);

        if (!(received instanceof LoginPacket))
            throw new InvalidObjectException("Wrong packet received");

        LoginPacket loginPacket = (LoginPacket) received;

        //resend index
        unicastSend(packet.getAddress(), packet.getPort(), new IndexPacket(index).toString());

        return loginPacket.getPlayerName();
    }

    /**
     * Function which sends a message to all users who belong to the mutlicast group
     */
    void multicastSend(String message) {
        try {
            byte[] buf;
            buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, message.length(), group, 9877);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void unicastSend(InetAddress address, int port, String message) {
        try {
            byte[] buf;
            buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, message.length(), address, port);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return all packets which should be proceeded
     */
    ArrayList<Packet> getPackets() {
        return queue.getPackets();
    }

    private Packet receive() {
        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(packet);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String string = new String(packet.getData());

        return Packet.createPacket(string);
    }

    /**
     * Method called when the game is about to be stopped and the receiving thread should be stopped. It sends a packet to the server
     * in order to unblock the infinite loop
     */
    void sendStopPacket() {
        try {
            InetAddress address = InetAddress.getByName("localhost");
            int port = 9876;
            byte[] buf = new byte[256];

            DatagramPacket packet = new DatagramPacket(buf, 1, address, port);
            socket.send(packet);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    int getnUsers() {
        return nUsers;
    }

    void setnUsers(int nUsers) {
        this.nUsers = nUsers;
    }
}
