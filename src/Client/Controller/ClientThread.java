package Client.Controller;

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

    public boolean connect() {
        //TODO connection failed
        Scanner in = new Scanner(System.in);
        System.out.println("Ip address of the server: ");
        try {
            serverAddress = InetAddress.getByName(in.nextLine());
            System.out.println("Port of the server: ");
            serverPort = in.nextInt();
            System.out.println("Choose your name: ");
            String name = in.nextLine();

            sendData (name.getBytes());

            byte[] inBuffer = new byte [256];
            packet = new DatagramPacket(inBuffer, inBuffer.length);
            socket.receive(packet);

            // display response
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println(received);

        }
        catch (Exception e) {
            e.printStackTrace();
        }


        return true;
    }

    public void sendData(byte [] data) throws IOException{
        DatagramPacket packet = new DatagramPacket (data, data.length, serverAddress, serverPort);
        socket.send(packet);
    }

    public String recieve () {

    }
}
