package Util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import Util.Packet.Packet;


/**
 * Created by maciej on 13.05.17.
 * Class used to keep the received packets until they are proceeded by the main server loop
 */
public class SynchronizedQueue {
    private LinkedList<Packet> packets;

    public SynchronizedQueue () {
        packets = new LinkedList<Packet> ();
    }


    synchronized public void put (Packet packet) {
        packets.add(packet);
    }

    /**
     * This method returns all the packets from the queue and makes it empty
     * @return list of packets which are to be proceeded
     */

    synchronized public ArrayList<Packet> getPackets() {
        ArrayList<Packet> result = new ArrayList<Packet> ();
        while (!packets.isEmpty()) {
            result.add(packets.remove());
        }
        return result;
    }
}
