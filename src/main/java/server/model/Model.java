package server.model;


import util.GameColor;
import util.Turn;

import java.util.ArrayList;


public class Model {

    /**
     * Game map kept as a pixel table
     */
    private Map map;
    private ArrayList<ServerPlayer> players;

    /**
     * Determines if the game state should be updated. It is changed when a round finishes
     */
    private boolean gameInProgress;

    /**
     * Message that should be printed on the system logs. It concerns players being
     * knocked out.
     */
    private StringBuffer message;

    public Model() {
        gameInProgress = false;
        players = new ArrayList<>();
        map = new Map();
        message = new StringBuffer();
    }

    /**
     * Method called in order to erase all players info from the server
     */
    public void reset() {
        gameInProgress = false;
        players.clear();
    }


    /**
     * Method called on each game beginning. If a player disconnects, he always remains not in play
     */
    public void init() {
        map.init();
        gameInProgress = true;
        for (ServerPlayer it : players) {
            if (it.isConnected())
                it.init();
        }
    }

    /**
     * This method is called in the main game loop every new game frame
     */

    public void update() {
        updatePlayers();
        checkCollisions();
        updateMap();
        updateGameInProgress();
    }

    public void addPlayer(GameColor color, String name) {
        players.add(new ServerPlayer(color, name));
    }

    /**
     * Method checks if the new player position is out of borders or if it
     * interferes already existing line
     */
    private void checkCollisions() {
        for (ServerPlayer it : players) {
            if (it.isInPlay() && (it.getX() <= 0 || it.getY() <= 0 || it.getX() >= Map.SIZE_X || it.getY() >= Map.SIZE_Y ||
                    !map.isEmpty(it.getX(), it.getY()))) {//collision detected
                it.setInPlay(false);
                message.append("Player name ").append(it.getName()).append(" knocked out due to collision (").append(it.getX()).append(",").append(it.getY()).append(")\n");
            }
        }
    }

    /**
     * If no player is in game it changes gameInProgress to null
     */
    private void updateGameInProgress() {
        int counter = 0;
        for (ServerPlayer it : players) {
            if (it.isInPlay())
                counter++;
        }
        if (counter == 0)
            gameInProgress = false;
    }


    private void updatePlayers() {
        for (ServerPlayer it : players) {
            if (it.isInPlay()) {
                it.update();
            }
        }
    }

    /**
     * Method draws all the curves on the map
     */
    private void updateMap() {
        for (ServerPlayer it : players) {
            if (it.isInPlay() && it.isVisible()) {
                map.putLine(it.getOx(), it.getOy(), it.getX(), it.getY());
            }
        }
    }

    public String getMessage() {
        String result = message.toString();
        message.delete(0, message.length());
        return result;
    }


    public void changeDirection(int playerIndex, Turn nTurn) {
        players.get(playerIndex).setTurn(nTurn);
    }

    public ArrayList<ServerPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<ServerPlayer> players) {
        this.players = players;
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public void setGameInProgress(boolean gameInProgress) {
        this.gameInProgress = gameInProgress;
    }

}
