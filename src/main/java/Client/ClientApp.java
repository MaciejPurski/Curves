package Client;

import Client.Controller.ClientController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

/**
 * Created by maciej on 12.05.17.
 */
public class ClientApp extends Application {

    public static void Main(String [] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Game.fxml"));
        primaryStage.setTitle("Curves");
        primaryStage.setScene(new Scene(root));

        primaryStage.show();

    }

}
