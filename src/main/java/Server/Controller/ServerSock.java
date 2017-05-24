package Server.Controller;

/**
 * Created by maciej on 22.03.17.
 */
import Util.Packet.IndexPacket;
import Util.Packet.LoginPacket;
import Util.Packet.Packet;
import Util.Packet.ServerStopPacket;
import Util.SynchronizedQueue;

import java.io.*;
import java.net.*;
import java.util.*;



public class ServerSock implements Runnable {
    /**
     * Class resposinble for server connection with the clients. When put in a new Thread it allows continous packets receiving
     *    @param socket Main server socket
     *    @param queue contains all packets received which are ready to read
     *    @param group address of the multicast group
     */

    private final int serverPort = 9876;

    private SynchronizedQueue queue;
    private DatagramSocket socket;
    private InetAddress group;
    private int nUsers;



    public ServerSock() {
        try {
            group = InetAddress.getByName("224.0.0.3");
            queue = new SynchronizedQueue();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * In this method packets are received and put on the queue. If the game is finished the running Thread should be interrupted and the infinite loop should
     * be broken. Then message is sent to all clients that the game is finished
     */

    public void run() {

        try {
            while(true)
            {
                if (Thread.currentThread().isInterrupted()) {
                    multicastSend(new ServerStopPacket().toString());
                    socket.close();
                    break;
                }
                Packet received = receive();
                queue.put(received);
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }


    }

    /**
     * Method which adds one user to the list of users and returns the name of the user. It sends to the new player his index
     * @return playerName
     * @param index index of the player who should be added
     */

    public String addUser (int index) throws IOException {

        byte[] buf = new byte [256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String string = new String(packet.getData());
        Packet received = Packet.createPacket(string);

        if (! (received instanceof LoginPacket) || received == null)
            throw new InvalidObjectException("Wrong packet received");

        LoginPacket loginPacket = (LoginPacket) received;

        //resend index

        unicastSend(packet.getAddress(), packet.getPort(), new IndexPacket(index).toString());


        return loginPacket.getPlayerName();
    }

    /**
     * Function which sends a message to all users who belong to the mutlicast group
     */

    public void multicastSend(String message) {

        try {
            byte [] buf = new byte [256];
            buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, message.length(), group, 9877);
            socket.send(packet);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void unicastSend(InetAddress address, int port, String message) {
        try {
            byte[] buf;
            buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, message.length(), address, port);
            socket.send(packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @return all packets which should be proceeded
     */

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

    /**
     * Method called when the game is about to be stopped and the receiving thread should be stopped. It sends a packet to the server
     * in order to unblock the infinite loop
     */
    public void sendStopPacket () {
        try {
            InetAddress address = InetAddress.getByName("127.0.0.1");
            int port = 9876;
            byte[] buf = new byte[256];

            DatagramPacket packet = new DatagramPacket(buf, 1, address, port);
            socket.send(packet);

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method called when server is about to start
     */
    public void init () {
        try {
            socket = new DatagramSocket(serverPort);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getnUsers() {
        return nUsers;
    }

    public void setnUsers(int nUsers) {
        this.nUsers = nUsers;
    }


}