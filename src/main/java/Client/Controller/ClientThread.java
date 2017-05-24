package Client.Controller;

import Util.Packet.*;

import java.io.IOException;
import java.io.InvalidObjectException;
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

    public void reset() {
        try {
            multicast.leaveGroup(InetAddress.getByName("224.0.0.3"));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
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

    public int connect(String ip, int port, String name) throws SocketTimeoutException, IOException{
        //TODO connection failed


            serverAddress = InetAddress.getByName(ip);
            serverPort = port;

            sendData(new LoginPacket(name).toString());
            Packet received = receiveUnicast();

            if (! (received instanceof IndexPacket) || received == null)
                throw new InvalidObjectException("Wrong packet type" + received.getClass().getName());

            IndexPacket indexPacket = (IndexPacket) received;

            multicast.joinGroup(InetAddress.getByName("224.0.0.3"));

        return indexPacket.getPlayerIndex();
    }

    public void sendData(String message) {
        byte [] buf = new byte [256];
        buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket (buf, buf.length, serverAddress, serverPort);
        try {
            socket.send(packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



    public Packet receiveMulticast() throws IOException{
        byte [] buf = new byte[512];
        DatagramPacket packet = new DatagramPacket (buf, buf.length);
        multicast.receive(packet);
        String string = new String(packet.getData());
        Packet received = Packet.createPacket(string);

        return received;
    }

    public Packet receiveUnicast() throws SocketTimeoutException, IOException{
        byte [] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket (buf, buf.length);

        socket.setSoTimeout(2000);
        socket.receive(packet);
        socket.setSoTimeout(0);
        String string = new String(packet.getData());


        return Packet.createPacket(string);
    }
}
