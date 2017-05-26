package server.controller;

import server.model.Model;
import server.view.ServerController;
import util.GameColor;
import util.packet.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class Controller implements Runnable {
    private int nPlayers;
    private Thread gameThread;
    private Thread serverThread;

    /**
     * Variable keeps track of number of players who are still in game.
     * It is decremented if someone leaves the game
     */
    private int activePlayers;
    private ServerSock socket;
    private Model model;

    /**
     * Informs if connection is established
     */
    private boolean isConnected;
    private ServerController view;

    public Controller(ServerController view) {
        this.view = view;
        socket = new ServerSock();
        model = new Model();
    }


    public void run() {
        try {
            while (true) {
                gameLoop();
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }
        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void gameLoop() throws Exception {
        final double amountOfTicks = 23.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        model.init();
        view.appendLogs("Game ready to start\n");

        //Thread sleeps after first packet has been sent
        sendGameState();
        Thread.sleep(3000);

        view.appendLogs("Game started\n");

        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();

        while (model.isGameInProgress()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {

                proceedPackets(); // get players signals
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                model.update(); // update game state based on signals received
                sendGameState(); // send new player positions and their state

                view.appendLogs(model.getMessage()); // if someone was knocked out - show the message

                delta--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }

        }

        view.appendLogs("Game finished. Getting ready to start next game\n");

        Thread.sleep(3000);
    }

    /**
     * Method used to proceed packets that have been received by the server and contains
     * players' moves or exit signals
     */
    private void proceedPackets() {
        ArrayList<Packet> tab = socket.getPackets();

        for (Packet it : tab) {
            if (it instanceof MovePacket) {
                MovePacket received = (MovePacket) it;
                model.changeDirection(received.getPlayerIndex(), received.getTurn());
            } else if (it instanceof ExitPacket) {
                ExitPacket received = (ExitPacket) it;
                kickPlayer(received.getPlayerIndex());
            }
        }
    }


    /**
     * Method called when the server is started. It waits for players to log in.
     * The number of players is specified when the server is started
     *
     * @throws IOException
     */
    private void fillUsersList() {
        ArrayList<GameColor> colorsTable = getColorsTable();

        for (int i = 0; i < nPlayers; i++) {
            try {
                String name = socket.addUser(i);
                model.addPlayer(colorsTable.get(i), name);
                view.appendLogs("Player with name: " + name + " index: " + i + " has connected. Current players: " + (i + 1) + "/"
                        + nPlayers + "\n");
            } catch (IOException ex) {
                ex.printStackTrace();
                i--; //ignore packet
            }
        }
    }

    /**
     * Method sends to all the players info on other players
     */
    private void sendPlayersInfo() {
        PlayerPacket packet = new PlayerPacket(model.getPlayers());
        for (int i=0; i<2; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            socket.multicastSend(packet.toString());
        }
    }

    /**
     * Method called when there is a need to stop the game. It interrupts running threads and resets the model
     */
    public void setGameStop() {
        view.allowStart();
        gameThread.interrupt();
        serverThread.interrupt();
        socket.sendStopPacket();
        nPlayers = 0;
        isConnected = false;
        model.reset();
        view.appendLogs("Game stopped");
    }

    /**
     * Method called to perform new connection
     */
    public void initConnection() {
        try {
            fillUsersList();
            sendPlayersInfo();
            serverThread = new Thread(socket);
            gameThread = new Thread(this);
            serverThread.start();
            gameThread.start();
            isConnected = true;
            activePlayers = nPlayers;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Method used to shuffle colors
     *
     * @return shuffled colors table
     */
    private ArrayList<GameColor> getColorsTable() {
        ArrayList<GameColor> colorsTable = new ArrayList<>();
        for (int i = 0; i < 8; i++)
            colorsTable.add(GameColor.fromInt(i));

        Collections.shuffle(colorsTable);
        return colorsTable;
    }


    private void sendGameState() {
        GameStatePacket toSend = new GameStatePacket(model);
        socket.multicastSend(toSend.toString());
    }

    /**
     * Method called when a player leaves the game
     *
     * @param index
     */
    private void kickPlayer(int index) {
        model.getPlayers().get(index).setConnected(false);
        model.getPlayers().get(index).setInPlay(false);
        activePlayers--;
        view.appendLogs("Player with index " + index + " has disconnected\n");

        if (activePlayers == 0) {
            view.appendLogs("All players left \n");
            setGameStop();  // if there are no active players, stop the game
        }
    }

    public void setNPlayers(int nPlayers) {
        this.nPlayers = nPlayers;
        socket.setnUsers(nPlayers);
    }

    public ServerSock getSocket() {
        return socket;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
