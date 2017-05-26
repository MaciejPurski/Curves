package client.view;


import client.controller.Controller;
import client.model.ClientModel;
import javafx.scene.control.Button;
import util.Player;
import util.Turn;
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


public class ClientController {

    private Controller controller;
    private int drawCounter;
    private boolean isPressedRight;
    private boolean isPressedLeft;

    @FXML
    private Canvas map;
    @FXML
    private Label text1, text2, text3, text4, text5, text6;
    @FXML
    private Circle circle1, circle2, circle3, circle4, circle5, circle6;
    @FXML
    private Button join, abandon;

    public ClientController() throws IOException {
        controller = new Controller(this);
    }

    @FXML
    public void onKeyPressed(KeyEvent event) {
        if (!controller.isConnected())
            return;

        switch (event.getCode()) {
            case LEFT:
                if (!isPressedLeft && !isPressedRight) {
                    controller.send(Turn.LEFT);
                    isPressedLeft = true;
                }
                break;
            case RIGHT:
                if (!isPressedRight && !isPressedLeft) {
                    controller.send(Turn.RIGHT);
                    isPressedRight = true;
                }
                break;
            default:
                break;
        }
    }

    @FXML
    public void onKeyReleased() {
        if (!controller.isConnected())
            return;
        isPressedRight = false;
        isPressedLeft = false;

        controller.send(Turn.NONE);
    }

    /**
     * Method creates login window in which all the logging procedure takes place
     *
     * @param event
     */
    @FXML
    void onJoinClick(MouseEvent event) {
        try {
            // set what to do when window is closed
            Stage stage = new Stage();
            ((Node) event.getSource()).getScene().getWindow().setOnCloseRequest(e -> {
                //TODO set exit packet
                if (!controller.isConnected())
                    System.exit(0);
                controller.exitGame();
            });

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = (Parent) loader.load();
            LoginController login = loader.getController();
            login.init(this, controller);
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
    void onAbandonClick() {
        join.setDisable(true);
        abandon.setDisable(false);
        controller.exitGame();
    }


    /**
     * Method initializes map, by painting it all black
     */
    public void initMap() {
        drawCounter = 0;

        map.setFocusTraversable(true);
        GraphicsContext gc = map.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(5);
        gc.fillRect(0, 0, 700, 700);
    }

    /**
     * Method draws all the players on canvas
     */
    public void drawModel(ClientModel model) {

        GraphicsContext gc = map.getGraphicsContext2D();
        for (Player it : model.getPlayers()) {
            Color playerColor = it.getColor().toColor();

            if (it.isInPlay()) {

                if (it.isVisible()) {
                    drawCounter = 0;

                    gc.setFill(playerColor);
                    gc.setStroke(playerColor);

                    gc.setLineCap(StrokeLineCap.ROUND);
                    gc.setLineJoin(StrokeLineJoin.ROUND);
                    gc.setLineWidth(8);
                    gc.strokeLine(it.getOx(), it.getOy(), it.getX() + 2, it.getY() + 2);
                } else { // there is a hole in a curve and we have to still keep track of the player position

                    if (drawCounter >= 1)
                        gc.setFill(Color.BLACK);
                    else {
                        gc.setFill(playerColor);
                        gc.setStroke(playerColor);
                    }

                    gc.fillRect(it.getOx(), it.getOy(), 4, 4);

                    drawCounter++;
                }
                gc.setFill(playerColor);
                gc.fillRect(it.getX(), it.getY(), 4, 4);
            }
        }
    }

    /**
     * Function draws players list - their colors and names
     */

    void showPlayers(ClientModel model) {
        for (int i = 0; i < model.getPlayers().size(); i++) {
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


    /**
     * Method starts a new game thread
     */
    public void clearView() {
        allowJoin();
        initMap();
        GraphicsContext gc = map.getGraphicsContext2D();

        gc.setLineWidth(3);
        gc.setStroke(Color.WHITE);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font(30));
        gc.strokeText("server stopped", 20, 20);

        isPressedLeft = false;
        isPressedRight = false;
        drawCounter = 0;
    }

    public void allowAbandon() {
        join.setDisable(true);
        abandon.setDisable(false);

    }

    public void allowJoin() {
        join.setDisable(false);
        abandon.setDisable(true);
    }


}
