package Server;

import java.io.IOException;
import java.util.Scanner;
import Server.Controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by maciej on 22.03.17.
 */
public class ServerApp extends Application {

    public static void Main(String [] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Server.fxml"));
        primaryStage.setTitle("CurvesServer");
        primaryStage.setScene(new Scene(root));

        primaryStage.show();

    }
    /*public static void main(String[] args) throws IOException {

        int nplayers;
        Scanner in = new Scanner(System.in);

        System.out.println("Give number of players: ");
        nplayers = in.nextInt();
        /*if (args.length != 1) {
            System.out.println("Usage: java Server <number of players>");
            return;
        }

        ServerController serverController = new ServerController (nplayers);
        serverController.start();
    }*/
}
