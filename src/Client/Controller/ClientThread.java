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
        socket = new DatagramSocket();
        multicast = new MulticastSocket(9877);
    }

    public void run () {
        while (true) {

        }

    }

    public boolean connect(String ip, int port, String name) throws IOException{
        //TODO connection failed

        serverAddress = InetAddress.getByName(ip);
        serverPort = port;
        multicast.joinGroup(InetAddress.getByName("224.0.0.3"));
        sendData (name.getBytes());



        return true;

    }

    public void sendData(byte [] data) throws IOException{
        DatagramPacket packet = new DatagramPacket (data, data.length, serverAddress, serverPort);
        socket.send(packet);
    }

    public Packet receive () throws IOException{
        byte [] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket (buf, buf.length);
        multicast.receive(packet);
        String string = new String(packet.getData());
        System.out.println(string);
        Packet received = Packet.createPacket(string);

        return received;
    }
}
