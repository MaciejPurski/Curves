package Util.Packet;

/**
 * Created by maciej on 22.05.17.
 * Sent when a player leaves the game
 */
public class ExitPacket extends Packet {

    private int playerIndex;

    public ExitPacket(int index) {
        playerIndex = index;
    }

    public ExitPacket (String string) throws NumberFormatException {
        parse(string);
    }

    public String toString () {

        StringBuffer result = new StringBuffer("E ");
        result.append(Integer.toString(playerIndex));

        return result.toString();

    }

    public void parse (String string) throws NumberFormatException{
        String [] tab;
        tab = string.split(" ");
        String str = tab[1].substring(0,1);
        playerIndex = Integer.parseInt(str);

    }
    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public boolean equals (Object object) {
        ExitPacket other = (ExitPacket) object;

        if (other.playerIndex != playerIndex)
            return false;

        return true;
    }

}
