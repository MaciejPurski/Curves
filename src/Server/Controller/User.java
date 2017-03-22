package Server.Controller;
import java.io.*;
import java.net.*;

/**
 * Created by maciej on 22.03.17.
 * Class representing informations about one connected client
 */
public class User {
    private DatagramSocket server;
    private int port;
    private String name;
    private InetAddress address;

    public User (DatagramSocket nserv, int nport, String nname, InetAddress naddress) {
        server = nserv; //copy reference
        port = nport;
        name = nname;
        address = naddress;
        System.out.println("User: " + name + " with address: " + address.toString() + " connected!");
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

}
