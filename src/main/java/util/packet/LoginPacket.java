package util.packet;

/**
 * Message sent when new player logs to the server
 */
public class LoginPacket extends Packet {

    private String playerName;

    public LoginPacket(String playerName) {
        parse(playerName);
    }


    public String toString() {
        return "L " + playerName;
    }

    public void parse(String string) {
        String[] tab;
        tab = string.split(" ");
        if (tab.length == 2)
            playerName = tab[1];
        else
            playerName = tab[0];

    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean equals(Object object) {
        LoginPacket other = (LoginPacket) object;

        if (other.playerName != playerName)
            return false;

        return true;
    }
}
