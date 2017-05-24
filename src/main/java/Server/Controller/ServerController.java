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
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Thread.sleep;


/**
 * Created by maciej on 26.04.17.
 *
 */
public class ServerController implements Runnable {

    private int nPlayers;
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

        try {
            ((Node)event.getSource()).getScene().getWindow().setOnCloseRequest(e -> {
                gameThread.interrupt();
                serverThread.interrupt();
                server.sendStopPacket();
                setGameStop();
            });
            nPlayers = (int) slider.getValue();
            server.setnUsers(nPlayers);

            isConnected = true;
            server.init();

            textArea.appendText("Game initialized with " + nPlayers + " players. Waiting for players to log in...\n");
            new Thread ( () -> {
                try {
                    fillUsersList();
                    sendPlayersInfo();

                    serverThread = new Thread (server);
                    gameThread = new Thread ( this);
                    serverThread.start();
                    gameThread.start();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();


        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void onStopPressed() {
        if(!isConnected)
            return;
        gameThread.interrupt();
        serverThread.interrupt();
        server.sendStopPacket();
        setGameStop();

        textArea.appendText("Game stopped \n");



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

    public void gameLoop() throws Exception {

        final double amountOfTicks = 25.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;


        model.init();
        //first packet

        Platform.runLater(() -> {
            textArea.appendText("Game ready to start\n");
        });

        GameStatePacket toSend = new GameStatePacket(model);
        server.multicastSend(toSend.toString());

        sleep(3000);

        Platform.runLater(() -> {
            textArea.appendText("Game started\n");
        });

        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();

        while (model.isGameInProgress()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                saveSignals();
                model.update();

                toSend = new GameStatePacket(model);
                server.multicastSend(toSend.toString());
                Platform.runLater(() -> {
                    String message = model.getMessage();
                    if (message.length()!= 0)
                        textArea.appendText(message);
                });


                delta--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
        }

        Platform.runLater(() -> {
            textArea.appendText("Game finished. Getting ready to start next game\n");
        });

        sleep(3000);
    }

    public void saveSignals() {
        ArrayList<Packet> tab = server.getPackets();

        for(Packet it: tab) {
            if (it instanceof MovePacket) {
                MovePacket received = (MovePacket) it;
                model.changeDirection(received.getPlayer(), received.getTurn());
            }
            //TODO inne pakiety

        }
    }

    /**
     * Method called when the server is started. It waits for players to log in. The number of players is specified when the server is started
     * @throws IOException
     */

    private void fillUsersList() throws IOException{
        ArrayList<GameColor> colorsTable = new ArrayList<GameColor> ();

        for (int i=0; i<8; i++)
            colorsTable.add(GameColor.fromInt(i));

        Collections.shuffle(colorsTable);

        for(int i=0; i<nPlayers; i ++) {

            String name = server.addUser(i);
            model.addPlayer(colorsTable.get(i), name);
            int index = i;
            Platform.runLater(() -> {
                textArea.appendText("Player with name: " + name + " index: " + index + " has connected. Current players: " + (index+1) + "/"
                        + nPlayers+ "\n");
            });

        }
    }

    /**
     * Method sends to all the players info on other players
     */

    private void sendPlayersInfo() throws Exception{
        PlayerPacket packet = new PlayerPacket(model.getPlayers());
        System.out.println(packet.toString());
        server.multicastSend(packet.toString());
    }

    public void setGameStop() {
        nPlayers = 0;
        isConnected = false;
        model.reset();
    }
}
