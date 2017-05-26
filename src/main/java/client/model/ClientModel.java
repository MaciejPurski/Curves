package client.model;

import util.packet.GameStatePacket;
import util.GameColor;
import java.util.ArrayList;
import util.Player;

public class ClientModel {

    private int nPlayers;
    private ArrayList<Player> players;
    private boolean gameInProgress;

    public ClientModel() {
        nPlayers = 0;
        players = new ArrayList<>();
        gameInProgress = false;
    }

    /**
     * Called when client is leaving the game
     */
    public void reset() {
        nPlayers = 0;
        players.clear();
        gameInProgress = false;
    }

    public void addPlayer(GameColor color, String name) {
        nPlayers++;
        players.add(new Player(color, name));
    }

    /**
     * Function used when starting new game
     */
    public void initPlayers() {
        for (Player it : players)
            it.init(it.getX(), it.getY());
    }

    /**
     * Updates players positions based on new coordinates received from the server
     * @param packet Packet containing information on players positions
     */
    public void update(GameStatePacket packet) {
        gameInProgress = packet.isGameInProgress();
        for (int i = 0; i < nPlayers; i++) {
            players.get(i).setInPlay(packet.getPlayers().get(i).isInPlay);
            players.get(i).setVisible(packet.getPlayers().get(i).isVisible);
            if (players.get(i).isInPlay()) {
                players.get(i).setPosition(packet.getPlayers().get(i).x, packet.getPlayers().get(i).y);
            }
        }
    }

    public int getnPlayers() { return nPlayers; }

    public void setnPlayers(int nPlayers) {
        this.nPlayers = nPlayers;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public void setGameInProgress(boolean gameInProgress) {
        this.gameInProgress = gameInProgress;
    }
}
