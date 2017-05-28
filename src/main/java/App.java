import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.Controller;

/**
 * Main app class
 */
public class App extends Application {
    private static String[] arguments;
    private static String fxmlName;
    private static int portNumber;

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader=new FXMLLoader(getClass().getResource(fxmlName));
        Parent root = loader.load();
        Controller cont= loader.getController();

        cont.setPort(portNumber);

        primaryStage.setTitle("Curves " + arguments[0]);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        arguments = args;
        if(arguments.length != 2) {
            System.out.println("Invalid arguments number. Arguments should be:\nserver|client <port number>");
            System.exit(-1);
        }

        try {
            portNumber = Integer.parseInt(args[1]);
        }
        catch(NumberFormatException ex) {
            System.out.println("Second argument is non-numerical. Arguments should be:\nserver|client <port number>");
            System.exit(-1);
        }

        if (arguments[0].equals("server")) {
            System.out.println("Launching server app");
            fxmlName = "/Server.fxml";
        } else if (arguments[0].equals("client")) {
            System.out.println("Launching client app");
            fxmlName = "/Game.fxml";
        } else {
            System.out.println("First argument is neither 'client' nor 'server'. Arguments should be:\nserver|client <port number>");
            System.exit(-1);
        }
        launch(args);
    }

}