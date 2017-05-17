package Client.Controller;

import Packets.Packet;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

/**
 * Created by maciej on 12.05.17.
 */
public class ClientThread extends Thread{
    private DatagramSocket socket;
    private MulticastSocket multicast;
    private InetAddress serverAddress;
    private int serverPort;


    public ClientThread (String name)  throws IOException {
        super(name);
        socket = new DatagramSocket(9878);
        multicast = new MulticastSocket(9877);
    }

    public void run () {
        while (true) {

        }

    }

    /**
     * Method sends login packet to the server and waits for the confirmation
     * @param ip
     * @param port
     * @param name
     * @return index of the player
     * @throws IOException
     */

    public int connect(String ip, int port, String name) throws IOException{
        //TODO connection failed

        serverAddress = InetAddress.getByName(ip);
        serverPort = port;
        multicast.joinGroup(InetAddress.getByName("224.0.0.3"));
        sendData (name);
        String string = receiveUnicast();
        string= string.substring(0, 1);

        int index = Integer.parseInt(string);

        return index;
    }

    public void sendData(String message) {
        byte [] buf = new byte [256];
        buf = message.getBytes();
        System.out.println(message);
        DatagramPacket packet = new DatagramPacket (buf, buf.length, serverAddress, serverPort);
        try {
            socket.send(packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



    public Packet receiveMulticast() throws IOException{
        byte [] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket (buf, buf.length);
        multicast.receive(packet);
        String string = new String(packet.getData());

        Packet received = Packet.createPacket(string);

        return received;
    }
    //TODO inny typ pakietu

    public String receiveUnicast() throws IOException{
        byte [] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket (buf, buf.length);
        socket.receive(packet);
        String string = new String(packet.getData());

        return string;
    }
}
