package Util.Packet;

/**
 * Created by maciej on 24.05.17.
 */
public class IndexPacket extends Packet {
    private int playerIndex;

    public IndexPacket(int index) {
        playerIndex = index;
    }

    public IndexPacket (String string) throws NumberFormatException {
        parse(string);
    }

    public String toString () {

        StringBuffer result = new StringBuffer("I ");
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
        IndexPacket other = (IndexPacket) object;

        if (other.playerIndex != playerIndex)
            return false;

        return true;
    }

}
