package javatpoint;

import Modele.AccountManagement;
import Vue.TileUser;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        String[] usernames = AccountManagement.getUsernames();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vue/FilmCreationView.fxml")); // on charge la première page fxml
        Parent root = loader.load(); // on charge le parent
        primaryStage.setTitle("Ohana Media Player");
        primaryStage.setScene(new Scene(root, 1000, 800));// on set la sec à la fenêtre
        primaryStage.show(); // on affiche la taille

    }


    public static void main(String[] args) {
        launch(args);
    }


}
