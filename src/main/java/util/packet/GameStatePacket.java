package util.packet;

import server.model.Model;

import java.util.ArrayList;

/**
 * This packet is used to send information about current positions,
 * visibility and state of all the players
 */
public class GameStatePacket extends Packet {

    /**
     * Structure used to keep information on one player
     */
    public class PlayerStruct {

        public PlayerStruct() {
        }

        public boolean isInPlay, isVisible;
        public int x, y;

        public boolean equals(Object object) {
            PlayerStruct other = (PlayerStruct) object;

            return x == other.x && y == other.y && isInPlay == other.isInPlay;
        }
    }

    private boolean gameInProgress;
    private int nPlayers;
    private ArrayList<PlayerStruct> players;


    public GameStatePacket(String string) throws NumberFormatException {
        players = new ArrayList<>();
        parse(string);
    }

    /**
     * Creates a packet directly from server.model
     *
     * @param model game state kept in the server.model object
     */
    public GameStatePacket(Model model) {
        players = new ArrayList<>();
        nPlayers = model.getPlayers().size();

        gameInProgress = model.isGameInProgress();
        for (int i = 0; i < model.getPlayers().size(); i++) {
            players.add(new PlayerStruct());
            players.get(i).isInPlay = model.getPlayers().get(i).isInPlay();
            players.get(i).isVisible = model.getPlayers().get(i).isVisible();
            players.get(i).x = model.getPlayers().get(i).getX();
            players.get(i).y = model.getPlayers().get(i).getY();
        }
    }

    /**
     * @param packet
     * @throws NumberFormatException if the message was incorrectly coded
     */
    public void parse(String packet) throws NumberFormatException {
        String[] tab;
        tab = packet.split(" ");

        nPlayers = Integer.parseInt(tab[1]);

        gameInProgress = tab[2].equals("1");

        int index = 4;
        for (int i = 0; i < nPlayers; i++) {
            players.add(new PlayerStruct());
            if (tab[index].equals("1")) {
                players.get(i).isInPlay = true;
                index++;
                players.get(i).isVisible = tab[index].equals("1");
                index++;

                players.get(i).x = Integer.parseInt(tab[index]);
                index++;
                players.get(i).y = Integer.parseInt(tab[index]);
                index += 2;
            } else {
                players.get(i).isInPlay = false;
                index += 2;
            }
        }
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("G ");
        buf.append(Integer.toString(nPlayers)).append(" ");
        if (gameInProgress)
            buf.append("1 ");
        else
            buf.append("0 ");

        for (int i = 0; i < nPlayers; i++) {
            buf.append(Integer.toString(i)).append(" ");
            if (players.get(i).isInPlay) {
                buf.append("1 ");
                if (players.get(i).isVisible)
                    buf.append("1 ");
                else
                    buf.append("0 ");

                buf.append(Integer.toString(players.get(i).x)).append(" ").append(Integer.toString(players.get(i).y)).append(" ");
            } else
                buf.append("0 ");
        }
        return buf.toString();
    }


    public boolean equals(Object object) {
        GameStatePacket other = (GameStatePacket) object;
        if (gameInProgress != other.gameInProgress)
            return false;
        if (nPlayers != other.nPlayers)
            return false;
        if (players.size() != other.players.size())
            return false;

        for (int i = 0; i < players.size(); i++) {
            if (!players.get(i).equals(other.players.get(i)))
                return false;
        }
        return true;
    }


    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public void setGameInProgress(boolean gameInProgress) {
        this.gameInProgress = gameInProgress;
    }

    public int getnPlayers() {
        return nPlayers;
    }

    public void setnPlayers(int nPlayers) {
        this.nPlayers = nPlayers;
    }

    public ArrayList<PlayerStruct> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<PlayerStruct> players) {
        this.players = players;
    }
}
