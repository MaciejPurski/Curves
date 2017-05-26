package util.packet;

/**
 * Message sent if server stops
 */
public class ServerStopPacket extends Packet {

    public ServerStopPacket () {}

    public ServerStopPacket (String string) {
        parse(string);
    }

    public String toString () {
        return "S ";
    }

    public void parse (String string) {}

    public boolean equals (Object object) {
        return true;
    }
}
