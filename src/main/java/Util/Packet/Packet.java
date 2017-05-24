package Util.Packet;

/**
 * Created by maciej on 10.05.17.
 * Class used to store and convert server and client messages
 */
public abstract class Packet {

    public abstract String toString ();
    public abstract void parse(String packet);

    /**
     * This method converts bare string received from the source to corresponding packet type
     * @param packet message received
     * @return instance of one of the Packet subclasses or null if the message was incorrect
     */
    public static Packet createPacket(String packet) {
        Packet result;
        try {
            if (packet.charAt(0) == 'M')
                result = new MovePacket(packet);
            else if (packet.charAt(0) == 'G')
                result = new GameStatePacket(packet);
            else if (packet.charAt(0) == 'P')
                result = new PlayerPacket(packet);
            else if (packet.charAt(0) == 'E')
                result = new ExitPacket(packet);
            else if (packet.charAt(0) == 'S')
                result = new ServerStopPacket(packet);
            else if (packet.charAt(0) == 'L')
                result = new LoginPacket(packet);
            else if (packet.charAt(0) == 'I')
                result = new IndexPacket(packet);
            else
                result = null;
        }
        catch (NumberFormatException ex) {
            System.out.println("Wrong data received " + packet);
            return null;
        }

        return result;
    }

    /**
     * Method used for testing packets
     */
    public abstract boolean equals(Object object);
}
