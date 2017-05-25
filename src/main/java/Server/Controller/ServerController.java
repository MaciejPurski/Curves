package Server.Controller;

import Util.Packet.*;
import Util.GameColor;
import Server.Model.Model;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Thread.sleep;


/**
 * Created by maciej on 26.04.17.
 *
 */
public class ServerController implements Runnable {

    private int nPlayers;
    private int activePlayers;
    private ServerSock server;
    private Model model;
    private boolean isConnected;
    private Thread gameThread;
    private Thread serverThread;

    @FXML
    private TextArea textArea;
    @FXML
    private Slider slider;


    public ServerController() {

        model = new Model();
        server = new ServerSock();
    }


    @FXML
    void onStartPressed(MouseEvent event) {

        if (isConnected)
            return;

        // stop the game when stop is pressed

        ((Node) event.getSource()).getScene().getWindow().setOnCloseRequest(e -> {
            if (!isConnected)
                System.exit(0);
            setGameStop();
        });

        //read users number from the slider

        nPlayers = (int) slider.getValue();
        server.setnUsers(nPlayers);



        textArea.appendText("Game initialized with " + nPlayers + " players. Waiting for players to log in...\n");
        new Thread(() -> initConnection()).start();

    }



    @FXML
    void onStopPressed() {
        if(!isConnected)
            return;
        setGameStop();
    }

    public void run ()
    {
        try {

            while (true) {
                gameLoop();
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }
        }
        catch (InterruptedException e){

            Thread.currentThread().interrupt();
         }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void gameLoop() throws Exception {

        final double amountOfTicks = 23.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;


        model.init();
        appendLogs("Game ready to start\n");

        //Thread sleeps after first packet has been sent
        sendGameState();
        sleep(3000);

        appendLogs("Game started\n");

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

                appendLogs(model.getMessage()); // if someone was knocked out - show the message

                delta--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }

        }


        appendLogs("Game finished. Getting ready to start next game\n");

        sleep(3000);
    }

    /**
     * Method used to proceed packets that have been received by the server and contain players' moves or exit signals
     */

    private void proceedPackets() {
        ArrayList<Packet> tab = server.getPackets();

        for(Packet it: tab) {
            if (it instanceof MovePacket) {
                MovePacket received = (MovePacket) it;
                model.changeDirection(received.getPlayer(), received.getTurn());
            }
            else if (it instanceof ExitPacket) {
                ExitPacket received = (ExitPacket) it;
                model.getPlayers().get(received.getPlayerIndex()).setConnected(false);
                model.getPlayers().get(received.getPlayerIndex()).setInPlay(false);
                activePlayers--;
                appendLogs("Player with index " + ((ExitPacket) it).getPlayerIndex()+ " has disconnected\n");
                if (activePlayers == 0)
                {
                    appendLogs("All players left \n");
                    setGameStop();
                }

            }


        }
    }

    /**
     * Method called when the server is started. It waits for players to log in.
     * The number of players is specified when the server is started
     * @throws IOException
     */

    private void fillUsersList() {

        ArrayList <GameColor> colorsTable = getColorsTable();

        for(int i=0; i<nPlayers; i ++) {
            try {
                String name = server.addUser(i);
                model.addPlayer(colorsTable.get(i), name);
                appendLogs("Player with name: " + name + " index: " + i + " has connected. Current players: " + (i+1) + "/"
                            + nPlayers+ "\n");
            }
            catch (IOException ex) {
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
        try {
            sleep(1000);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        server.multicastSend(packet.toString());
    }

    /**
     * Method called when there is a need to stop the game. It interrupts running threads and resets the model
     */

    private void setGameStop() {
        gameThread.interrupt();
        serverThread.interrupt();
        server.sendStopPacket();
        nPlayers = 0;
        isConnected = false;
        model.reset();
        appendLogs("Game stopped");
    }

    /**
     * Method called to perform new connection
     */

    private void initConnection() {
        try {

            fillUsersList();
            sendPlayersInfo();
            serverThread = new Thread(server);
            gameThread = new Thread(this);
            serverThread.start();
            gameThread.start();
            isConnected = true;
            activePlayers = nPlayers;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Method used to shuffle colors
     * @return shuffled colors table
     */

    private ArrayList<GameColor> getColorsTable() {
        ArrayList<GameColor> colorsTable = new ArrayList<> ();
        for (int i=0; i<8; i++)
            colorsTable.add(GameColor.fromInt(i));

        Collections.shuffle(colorsTable);
        return colorsTable;
    }

    /**
     * Method used to add a message to logs list on the screen. It is called from different threads
     * @param string
     */

    private synchronized void appendLogs(String string) {
        if (string.isEmpty())
            return;
        Platform.runLater( () ->
        textArea.appendText(string));
    }

    private void sendGameState() {
        GameStatePacket toSend = new GameStatePacket(model);
        server.multicastSend(toSend.toString());
    }
}
