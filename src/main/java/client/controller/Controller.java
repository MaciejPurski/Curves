package client.controller;

import client.model.ClientModel;
import client.view.ClientController;
import javafx.application.Platform;
import util.Turn;
import util.packet.*;

import java.io.IOException;
import java.io.InvalidObjectException;

public class Controller implements Runnable {

    private ClientController view;
    private int playerIndex;
    private ClientSocket socket;
    private ClientModel model;


    private boolean isConnected;
    private Thread gameThread;

    public Controller(ClientController view) throws IOException{
        this.view = view;
        socket = new ClientSocket();
        model = new ClientModel();
    }


    public void run() {
        try {
            while (true) {
                gameLoop();
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method called at each new connection
     * @throws IOException
     */
    public void fillUsersList () throws IOException{

        Packet received = socket.receiveMulticast();
        if (received instanceof PlayerPacket) { //information on new player
            PlayerPacket player = (PlayerPacket) received;
            for(PlayerPacket.PlayerInfo it: player.getPlayers())
                model.addPlayer(it.color, it.name);
        }
        else
            throw new InvalidObjectException("Wrong packet received: " + received.toString());
    }

    void gameLoop() throws IOException{
        long lastTime = System.nanoTime();
        final double amountOfTicks = 23.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        long timer = System.currentTimeMillis();

        if( !proceedPacket())
            return;

        while (model.isGameInProgress()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {

                if( !proceedPacket())
                    return;

                delta--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }

            if (Thread.currentThread().isInterrupted()) {
                break;
            }
        }
    }



    public void setPlayer(int index) {
        playerIndex = index;
    }

    public void exitGame() {
        socket.sendData(new ExitPacket(playerIndex).toString());
        setGameStop();
    }

    /**
     * Method waits for a new packet and checks if it is a GameStatePacket, if not it returns false if yes it updates positions and draws new model
     * @return true -if everything went ok and false if the game must be stopped
     */

    private boolean proceedPacket() {
        try {
            Packet received;

            GameStatePacket gameState;
            boolean isMapInitiated;
            // check if it is a first packet received
            if (!model.isGameInProgress()) {
                isMapInitiated = false;
            }
            else
                isMapInitiated = true;

            received = socket.receiveMulticast();

            if (received instanceof GameStatePacket) {
                gameState = (GameStatePacket) received;
                model.update(gameState);

                Platform.runLater(() -> {
                    if (!isMapInitiated){
                        model.initPlayers();
                        view.initMap();
                    }

                    view.drawModel(model);
                });

            } else if (received instanceof  ServerStopPacket){
                // game stopped
                setGameStop();
                return false;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public void send(Turn turn) {
        socket.sendData(new MovePacket(playerIndex, turn).toString());
    }

    public void startGame() {
        isConnected = true;
        gameThread = new Thread (this);
        gameThread.start();
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    /**
     * Method called when the game was stopped by user or by stopping the server.
     * It is synchronized because it might occur that two threads at the same time call
     * the method when the player leaves the game and he is the last player which results in
     * getting game stop packet from the server.
     */
    synchronized void setGameStop () {
        if (!isConnected)
            return;
        gameThread.interrupt();
        try {
            socket.reset();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        model.setGameInProgress(false);
        model.reset();
        Platform.runLater( () -> view.clearView());

        isConnected = false;
    }

    public ClientSocket getSocket() {
        return socket;
    }

    public ClientModel getModel() {
        return model;
    }
}
