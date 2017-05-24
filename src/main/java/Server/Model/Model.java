package Server.Model;


import Util.GameColor;
import Util.Turn;

import java.util.ArrayList;

/**
 * Created by maciej on 26.04.17.
 */


public class Model {

    /**
     * @param message Message which will be printed on the server log
     */

    private Map map;
    private ArrayList<ServerPlayer> players;
    private boolean gameInProgress;
    private StringBuffer message;

    public Model() {
        gameInProgress = false;
        players = new ArrayList<ServerPlayer> ();
        map = new Map ();
        message = new StringBuffer();
    }

    /**
     * Method called in order to erase all players info from the server
     */

    public void reset () {
        gameInProgress = false;
        players.clear();
    }


    /**
     * Method called on each game beginning
     */

    public void init () {
        map.init();
        gameInProgress = true;
        for(ServerPlayer it : players) {
            it.init();
        }
    }

    public void update() {

        updatePlayers();

        checkCollisions();

        updateMap();

        updateGameInProgress();

    }

    public void addPlayer (GameColor color, String name) {
        players.add(new ServerPlayer(color, name));
    }

    /**
     * Method checks if the new player position is out of borders or if it interferes already existing line
     */

    private void checkCollisions(){
        for (ServerPlayer it : players) {
            if(it.isInPlay() && (it.getX()<=0 || it.getY() <= 0 || it.getX()>=Map.SIZE_X || it.getY()>=Map.SIZE_Y ||
                    !map.isEmpty(it.getX(),it.getY()) )) {//collision detected
                it.setInPlay(false);
                message.append("Player name " + it.getName() + " knocked out due to collision (" + it.getX() + "," + it.getY()+ ")\n");
            }
        }
    }

    /**
     * If no player is in game it changes gameInProgress to null
     */
    private void updateGameInProgress() {
        int counter = 0;
        for (ServerPlayer it: players) {
            if (it.isInPlay())
                counter++;
        }

        if (counter == 0)
            gameInProgress = false;
    }


    private void updatePlayers() {
        for ( ServerPlayer it : players) {
            if(it.isInPlay()) {
                it.update();
            }
        }
    }

    /**
     * Method draws all the curves on the map
     */

    private void updateMap() {
        for (ServerPlayer it: players) {
            if(it.isInPlay() && it.isVisible()) {
                map.putLine(it.getOx(), it.getOy(), it.getX(), it.getY());
            }
        }
    }

    public String getMessage () {
        String result = message.toString();
        message.delete(0, message.length());
        return result;
    }


    public void changeDirection(int playerIndex, Turn nturn) {
        players.get(playerIndex).setTurn(nturn);
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
