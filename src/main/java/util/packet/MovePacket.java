package util.packet;


import util.Turn;

/**
 * Class representing key pressed or released by a particular playerIndex,
 * which is sent to the server
 */


public class MovePacket extends Packet {

    private int playerIndex;
    private Turn turn;

    public MovePacket(int playerIndex, Turn event) {
        this.playerIndex = playerIndex;
        this.turn = event;
    }

    public MovePacket(String string) throws NumberFormatException {
        parse(string);
    }


    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public String toString() {
        return "M " + Integer.toString(playerIndex) + " " +
                turn.getValue() + " ";
    }

    /**
     * @param packet
     * @throws NumberFormatException thrown if the message was incorrectly coded
     */

    public void parse(String packet) throws NumberFormatException {
        String[] tab;
        tab = packet.split(" ");
        playerIndex = Integer.parseInt(tab[1]);
        turn = Turn.fromInt(Integer.parseInt(tab[2]));

        if (turn == null)
            throw new NumberFormatException("Wrong Turn enum value");
    }

    public Turn getTurn() {
        return turn;
    }

    public boolean equals(Object object) {
        MovePacket other = (MovePacket) object;
        return playerIndex == other.playerIndex && turn == other.turn;
    }
}
