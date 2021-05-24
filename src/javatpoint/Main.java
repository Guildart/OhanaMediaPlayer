package javatpoint;

import Model.AccountManagement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        String[] usernames = AccountManagement.getUsernames();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/VueConnexion.fxml")); // on charge la première page fxml
        Parent root = loader.load(); // on charge le parent
        primaryStage.setTitle("Ohana Media Player");
        primaryStage.setScene(new Scene(root, 1000, 800));// on set la scene de la fenêtre
        primaryStage.show(); // on affiche le stage

    }


    public static void main(String[] args) {
        launch(args);
    }


}
