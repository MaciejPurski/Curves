package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApp extends Application {

    public static void main(String [] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Game.fxml"));
            primaryStage.setTitle("Curves");
            primaryStage.setScene(new Scene(root));

            primaryStage.show();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

    }

}
