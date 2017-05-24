package Util.Packet;

/**
 * Created by maciej on 24.05.17.
 */
public class LoginPacket extends Packet {
    /**
     * Message sent when new player logs to the server
     */
    private String playerName;

    public LoginPacket(String playerName) {
        parse(playerName);
    }


    public String toString () {

        StringBuffer result = new StringBuffer("L ");
        result.append(playerName);

        return result.toString();
    }

    public void parse (String string) {
        String [] tab;
        tab = string.split(" ");
        if (tab.length == 2)
            playerName = tab[1];
        else
            playerName = tab[0];

    }
    public String getPlayerName() {
        return playerName;
    }


    public boolean equals (Object object) {
        LoginPacket other = (LoginPacket) object;

        if (other.playerName!= playerName)
            return false;

        return true;
    }
}
