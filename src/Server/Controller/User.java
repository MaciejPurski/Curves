package Server.Controller;
import Packets.Packet;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by maciej on 22.03.17.
 * Class representing informations about one connected client
 */
public class User {
    private DatagramSocket server;
    private int port;
    private String name;
    private InetAddress address;
    private ConcurrentLinkedDeque<Packet> packetsDeque;

    public User (DatagramSocket nserv, int nport, String nname, InetAddress naddress) {
        server = nserv; //copy reference
        port = nport;
        name = nname;
        address = naddress;
        System.out.println("User: " + name + " with address: " + address.toString() + " connected!");
        packetsDeque = new ConcurrentLinkedDeque<Packet> ();
        try {
            sendData(new String("Connected!\n").getBytes()); }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed sending data");
        }
    }

    public String getName() {
        return name;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void sendData(byte[] data) throws IOException{

        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        server.send(packet);
    }


    public Packet getPacket() {
        return packetsDeque.poll();
    }

    public void putPacket(Packet elem) {
        packetsDeque.add(elem);
    }


}
