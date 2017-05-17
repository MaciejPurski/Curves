package Client.Controller;

/**
 * Created by maciej on 22.03.17.
 */
import Client.Model.ClientModel;
import Client.Model.ClientPlayer;
import Packets.GameStatePacket;
import Packets.MovePacket;
import Packets.Packet;
import Packets.PlayerPacket;
import Server.Model.Player;
import javafx.fxml.FXML;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.*;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;


import java.io.*;


public class ClientController extends Thread{

    private int playerIndex;
    private String playerName;
    private ClientThread client;
    private ClientModel model;
    private boolean isPressedRight;
    private boolean isPressedLeft;
    private int drawCounter;
    @FXML
    private Canvas map;


    public ClientController() {

    }

    public void init (ClientThread client, ClientModel model) {

        this.client = client;
        this.model = model;
    }

    public void run() {
        try {
            System.out.println("Entered");
            fillUsersList();
            initMap();
            while (true) {
                gameLoop();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void onKeyPressed(KeyEvent event) {

        switch (event.getCode()) {
            case LEFT:
                if (!isPressedLeft && !isPressedRight) {
                    client.sendData(new MovePacket(playerIndex, Player.Turn.LEFT).toString());
                    isPressedLeft = true;
                }
                break;
            case RIGHT:
                if(!isPressedRight && !isPressedLeft){
                    client.sendData(new MovePacket(playerIndex, Player.Turn.RIGHT).toString());
                    isPressedRight = true;
                }
                break;
            default:
                break;
        }

    }

    @FXML
    public void onKeyReleased(KeyEvent event) {
        isPressedRight = false;
        isPressedLeft = false;

        client.sendData(new MovePacket(playerIndex, Player.Turn.NONE).toString());
    }



    public void fillUsersList () throws IOException{
        while (true) {
            Packet received = client.receive();
            if (received instanceof PlayerPacket) { //information on new player
                PlayerPacket player = (PlayerPacket) received;
                if (player.getIndex() >= model.getnPlayers()) { // if it is a new player
                    model.addPlayer(2, player.getColor(), player.getName());
                    if (player.getName().equals(playerName)) {

                        playerIndex = player.getIndex();
                    }
                }
            }
            else if (received instanceof GameStatePacket) {
                GameStatePacket gameState = (GameStatePacket) received;

                model.update(gameState);

                return;
            }

            else
                throw new InvalidObjectException("Wrong packet received");

        }
    }

    public void gameLoop() throws IOException{
        long lastTime = System.nanoTime();
        final double amountOfTicks = 25.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        Packet received;
        GameStatePacket gameState;

        long timer = System.currentTimeMillis();

        received = client.receive();
        gameState = (GameStatePacket) received;
        initMap();
        model.update(gameState);
        model.initPlayers();
        drawModel();

        while (model.isGameInProgress()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {


                received = client.receive();

                if (received instanceof GameStatePacket) {
                    gameState = (GameStatePacket) received;
                    model.update(gameState);
                    drawModel();
                }

                delta--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }
        }
    }



    public void setPlayer(String string) {
        playerName = string;
    }

    public void initMap () {
        drawCounter = 0;

        map.setFocusTraversable(true);
        GraphicsContext gc = map.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(5);
        gc.fillRect(0,0,800,800);
    }

    public void drawModel() {

        GraphicsContext gc = map.getGraphicsContext2D();
        for (ClientPlayer it : model.getPlayers()) {
            Color playerColor;

            if (it.getColor() == Player.GameColor.BLUE)
                playerColor = Color.BLUE;
             else if (it.getColor() == Player.GameColor.RED)
                playerColor = Color.RED;
             else if (it.getColor() == Player.GameColor.GREEN)
                playerColor = Color.GREEN;
             else
                playerColor = Color.YELLOW;



            //TODO więcej kolorów
            if(it.isVisible()) {
                drawCounter = 0;

                gc.setFill(playerColor);
                gc.setStroke(playerColor);

                gc.setLineCap(StrokeLineCap.ROUND);
                gc.setLineJoin(StrokeLineJoin.ROUND);
                gc.setLineWidth(8);
                gc.strokeLine(it.getOx(), it.getOy(), it.getX()+2, it.getY()+2);
            }
                else {

                if(drawCounter>= 1) {
                    gc.setFill(Color.BLACK);

                }
                else {
                    gc.setFill(playerColor);
                    gc.setStroke(playerColor);
                }

                gc.fillRect(it.getOx(), it.getOy(), 2, 2);



                drawCounter ++;
            }
            gc.setFill(Color.YELLOW);
            gc.fillRect(it.getX(), it.getY(), 2, 2);
        }
    }
}
