package Packets;

/**
 * Created by maciej on 10.05.17.
 */
public abstract class Packet {

    public abstract String toString ();
    public abstract void parse(String packet);

    public static Packet createPacket(String packet) {
        Packet result;
        if ( packet.charAt(0) == 'M') {
            result = new MovePacket (packet);
        }
        else if (packet.charAt(0) == 'G')
            result = new GameStatePacket(packet);
        else if (packet.charAt(0) == 'P')
            result = new PlayerPacket(packet);
        else
            result = null;
        //TODO other types of packets


        return result;

    }
    /**
     * Method used for testing packets
     */
    public abstract boolean equals(Object object);
}
