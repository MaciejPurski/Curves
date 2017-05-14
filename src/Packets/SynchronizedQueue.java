package Packets;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by maciej on 13.05.17.
 */
public class SynchronizedQueue {
    private LinkedList<Packet> packets;

    public SynchronizedQueue () {
        packets = new LinkedList<Packet> ();
    }
    synchronized public void put (Packet packet) {
        packets.add(packet);
    }

    synchronized public ArrayList<Packet> getPackets() {
        ArrayList<Packet> result = new ArrayList<Packet> ();
        while (!packets.isEmpty()) {
            result.add(packets.remove());
        }

        return result;
    }
}
