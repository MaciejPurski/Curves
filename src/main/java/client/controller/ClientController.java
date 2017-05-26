package client.controller;

/**
 * Created by maciej on 22.03.17.
 */
import client.model.ClientModel;
import util.packet.*;
import util.Player;
import util.Turn;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.*;

import static java.lang.Thread.sleep;


public class ClientController implements Runnable{

    private int playerIndex;
    private ClientSocket client;
    private ClientModel model;
    private boolean isPressedRight;
    private boolean isPressedLeft;
    private int drawCounter;
    private boolean isConnected;
    private Thread gameThread;
    @FXML
    private Canvas map;
    @FXML
    private Label text1,text2,text3,text4, text5, text6;
    @FXML
    private Circle circle1, circle2, circle3, circle4, circle5, circle6;


    public ClientController() throws IOException{
            isConnected = false;
            client = new ClientSocket();
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

    @FXML
    public void onKeyPressed(KeyEvent event) {
        if(!isConnected)
            return;

        switch (event.getCode()) {
            case LEFT:
                if (!isPressedLeft && !isPressedRight) {
                    client.sendData(new MovePacket(playerIndex, Turn.LEFT).toString());
                    isPressedLeft = true;
                }
                break;
            case RIGHT:
                if(!isPressedRight && !isPressedLeft){
                    client.sendData(new MovePacket(playerIndex, Turn.RIGHT).toString());
                    isPressedRight = true;
                }
                break;
            default:
                break;
        }

    }

    @FXML
    public void onKeyReleased() {
        if(!isConnected)
            return;
        isPressedRight = false;
        isPressedLeft = false;

        client.sendData(new MovePacket(playerIndex, Turn.NONE).toString());
    }

    /**
     * Method creates login window in which all the logging procedure takes place
     * @param event
     */
    @FXML
    void onJoinClick(MouseEvent event) {
        try {

            if (isConnected)
                return;

            // set what to do when window is closed
            Stage stage = new Stage();
            ((Node) event.getSource()).getScene().getWindow().setOnCloseRequest(e -> {
                //TODO set exit packet
                if (!isConnected)
                    System.exit(0);
                if (gameThread != null) {
                    exitGame();
                    gameThread.interrupt();
                }
            });

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = (Parent) loader.load();
            LoginController login = loader.getController();
            login.init(client, this);
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node) event.getSource()).getScene().getWindow());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void onAbandonClick () {
        exitGame();
    }

    /**
     * Method called at each new connection
     * @throws IOException
     */

    void fillUsersList () throws IOException{

            Packet received = client.receiveMulticast();
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

    /**
     * Method initializes map, by painting it all black
     */

    void initMap () {
        drawCounter = 0;

        map.setFocusTraversable(true);
        GraphicsContext gc = map.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(5);
        gc.fillRect(0,0,800,800);
    }

    /**
     * Method draws all the players on canvas
     */
    private void drawModel() {

        GraphicsContext gc = map.getGraphicsContext2D();
        for (Player it : model.getPlayers()) {
            Color playerColor = it.getColor().toColor();


            if(it.isVisible()) {
                drawCounter = 0;

                gc.setFill(playerColor);
                gc.setStroke(playerColor);

                gc.setLineCap(StrokeLineCap.ROUND);
                gc.setLineJoin(StrokeLineJoin.ROUND);
                gc.setLineWidth(8);
                gc.strokeLine(it.getOx(), it.getOy(), it.getX()+2, it.getY()+2);
            }
                else { // there is a hole in a curve and we have to still keep track of the player position

                if(drawCounter>= 1)
                    gc.setFill(Color.BLACK);
                else {
                    gc.setFill(playerColor);
                    gc.setStroke(playerColor);
                }

                gc.fillRect(it.getOx(), it.getOy(), 4, 4);

                drawCounter ++;
            }
            gc.setFill(playerColor);
            gc.fillRect(it.getX(), it.getY(), 4, 4);
        }
    }

    /**
     * Method called when the game was stopped
     */

    void setGameStop () {
        System.out.println("Game stop");

        gameThread.interrupt();
        isConnected = false;
        model.setGameInProgress(false);
        initMap();
        GraphicsContext gc = map.getGraphicsContext2D();

        gc.setLineWidth(3);
        gc.setStroke(Color.WHITE);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font(30));
        gc.strokeText("server stopped", 20, 20);
        model.reset();
        isPressedLeft = false;
        isPressedRight = false;
        drawCounter=0;
        try {
            client.reset();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    void exitGame() {
        ExitPacket packet = new ExitPacket(playerIndex);
        client.sendData(packet.toString());

        isConnected = false;
        try {
            gameThread.interrupt();
            isConnected = false;
            sleep(500);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        setGameStop();
    }

    /**
     * Function draws players list - their colors and names
     */

    void showPlayers() {
        for (int i=0; i<model.getPlayers().size(); i++) {
            switch (i) {
                case 0:
                    text1.setText(model.getPlayers().get(i).getName());
                    circle1.setFill(model.getPlayers().get(i).getColor().toColor());
                    circle1.setVisible(true);
                    break;
                case 1:
                    text2.setText(model.getPlayers().get(i).getName());
                    circle2.setFill(model.getPlayers().get(i).getColor().toColor());
                    circle2.setVisible(true);
                    break;
                case 2:
                    text3.setText(model.getPlayers().get(i).getName());
                    circle3.setFill(model.getPlayers().get(i).getColor().toColor());
                    circle3.setVisible(true);
                    break;
                case 3:
                    text4.setText(model.getPlayers().get(i).getName());
                    circle4.setFill(model.getPlayers().get(i).getColor().toColor());
                    circle4.setVisible(true);
                    break;
                case 4:
                    text5.setText(model.getPlayers().get(i).getName());
                    circle5.setFill(model.getPlayers().get(i).getColor().toColor());
                    circle5.setVisible(true);
                    break;
                case 5:
                    text6.setText(model.getPlayers().get(i).getName());
                    circle6.setFill(model.getPlayers().get(i).getColor().toColor());
                    circle6.setVisible(true);
                    break;
            }
        }
    }


    void setConnected(boolean connected) {
        isConnected = connected;
    }

    /**
     * Method starts a new game thread
     */

    void start () {
        gameThread = new Thread (this);
        gameThread.start();
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

            received = client.receiveMulticast();

            if (received instanceof GameStatePacket) {
                gameState = (GameStatePacket) received;
                model.update(gameState);

                Platform.runLater(() -> {
                    if (!isMapInitiated){
                        model.initPlayers();
                        initMap();
                    }

                    drawModel();
                });

            } else {
                // game stopped
                System.out.println(received.getClass().getName());
                if (isConnected)
                setGameStop();
                return false;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return true;
    }

}
