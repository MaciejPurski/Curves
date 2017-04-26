package Packets;

/**
 * Created by maciej on 08.04.17.
 * Class representing moves of a particular player which is sent to the server
 */
public class Packet {
    private int player;
    private enum Event {RIGHT, LEFT, NONE, EXIT};
    Event event;

    public Packet(int player, Event event){
        this.player=player;
        this.event=event;
    }

    public Packet (String string)
    {
        parse(string);
    }

    public Packet () {

    }

    public String toString() {
        StringBuffer result = new StringBuffer(Integer.toString(player));
        switch (event) {
            case RIGHT:
                result.append(" R");
                break;
            case LEFT:
                result.append(" L");
                break;
            case NONE:
                result.append(" N");
                break;
            case EXIT:
                result.append(" X");
                break;
        }

        return result.toString();
    }

    public void parse(String packet) {
        String [] tab = new String [2];
        tab = packet.split(" ");
        player = Integer.parseInt(tab[0]);

        if (tab[1].equals("R"))
            event = Event.RIGHT;
        else if (tab[1].equals("L"))
            event = Event.LEFT;
        else if (tab[1].equals("N"))
            event = Event.NONE;
        else if (tab[1].equals("X"))
            event = Event.EXIT;
    }
}
