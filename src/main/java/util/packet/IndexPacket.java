package util.packet;

/**
 * Packet used to send player's index after login
 */
public class IndexPacket extends Packet {
    private int playerIndex;

    public IndexPacket(int index) {
        playerIndex = index;
    }

    public IndexPacket(String string) throws NumberFormatException {
        parse(string);
    }

    public String toString() {
        return "I " + Integer.toString(playerIndex);
    }

    public void parse(String string) throws NumberFormatException {
        String[] tab;
        tab = string.split(" ");
        String str = tab[1].substring(0, 1);
        playerIndex = Integer.parseInt(str);
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public boolean equals(Object object) {
        IndexPacket other = (IndexPacket) object;

        return other.playerIndex == playerIndex;
    }
}
