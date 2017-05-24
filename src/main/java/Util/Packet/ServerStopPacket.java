package Util.Packet;

/**
 * Created by maciej on 22.05.17.
 * Message sent if server stops
 */
public class ServerStopPacket extends Packet {

    public ServerStopPacket () {}

    public ServerStopPacket (String string) {
        parse(string);
    }

    public String toString () {

        StringBuffer result = new StringBuffer("S ");
        return result.toString();

    }

    public void parse (String string) {
    }


    public boolean equals (Object object) {
        return true;
    }
}
