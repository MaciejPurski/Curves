package client.controller;

import util.packet.*;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ClientSocket {
    private DatagramSocket socket;
    private MulticastSocket multicast;
    private InetAddress serverAddress;
    InetAddress groupAddres;
    private int serverPort;


    public ClientSocket() throws IOException {
        socket = new DatagramSocket(9878);
        multicast = new MulticastSocket(9877);
        groupAddres = InetAddress.getByName("224.0.0.3");
    }


    /**
     * Method sends login packet to the server and waits for the confirmation
     *
     * @param ip
     * @param port
     * @param name
     * @return index of the player
     * @throws IOException
     */
    public int connect(String ip, int port, String name) throws IOException {
        serverAddress = InetAddress.getByName(ip);
        serverPort = port;

        multicast.joinGroup(groupAddres);

        sendData(new LoginPacket(name).toString());
        Packet received = receiveUnicast();

        if (!(received instanceof IndexPacket))
            throw new InvalidObjectException("Wrong packet type" + received.getClass().getName());

        IndexPacket indexPacket = (IndexPacket) received;

        return indexPacket.getPlayerIndex();
    }

    void sendData(String message) {
        byte[] buf;
        buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddress, serverPort);
        try {
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    Packet receiveMulticast() throws IOException {
        byte[] buf = new byte[2056];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        multicast.receive(packet);
        String string = new String(packet.getData());

        return Packet.createPacket(string);
    }


    private Packet receiveUnicast() throws IOException {
        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        socket.setSoTimeout(2000);
        socket.receive(packet);
        String string = new String(packet.getData());

        return Packet.createPacket(string);
    }

    /**
     * Function called to leave the group. It is delayed in order to allow main game thread to finish
     * @throws IOException
     */

    void reset() throws IOException {
        final ScheduledExecutorService exec = Executors.newScheduledThreadPool(0);

        exec.schedule(() -> {
            try {
                multicast.leaveGroup(groupAddres);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }, 200, TimeUnit.MILLISECONDS);
    }


    public void leave() {
        try {
            multicast.leaveGroup(groupAddres);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
