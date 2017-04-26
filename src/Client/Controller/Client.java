package Client.Controller;

/**
 * Created by maciej on 22.03.17.
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    public static void main(String[] args) throws IOException {

        /*if (args.length != 1) {
            System.out.println("Usage: java Client <hostname>");
            return;
        }*/

        Scanner in = new Scanner(System.in);
        System.out.println("Choose your name: ");
        String name = in.nextLine();

        // get a datagram socket
        DatagramSocket socket = new DatagramSocket();
        MulticastSocket s = new MulticastSocket(9877);
        s.joinGroup(InetAddress.getByName("228.5.6.7"));

        // send request
        byte[] outBuffer = new byte[256];
        outBuffer = name.getBytes();
        InetAddress address = InetAddress.getByName("127.0.0.1");
        DatagramPacket packet = new DatagramPacket(outBuffer, outBuffer.length, address, 9876);
        socket.send(packet);

        // get response
        byte[] inBuffer = new byte [256];
        packet = new DatagramPacket(inBuffer, inBuffer.length);
        socket.receive(packet);

        // display response
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println(received);

        while(true)
        {
            // get response
            packet = new DatagramPacket(inBuffer, inBuffer.length);
            s.receive(packet);

            // display response
            received = new String(packet.getData(), 0, packet.getLength());
            System.out.println(received);
        }

        //socket.close();
    }
}
