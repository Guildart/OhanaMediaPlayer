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
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {


    @FXML public HBox hbox; /* = new HBox();*/

    static String relativePath = File.separator + "movie.jpg";
    private static String imgFile = System.getProperty("user.dir") + relativePath;
    //private static String imgFile = "movie.jpg";

    @Override
    public void start(Stage primaryStage) throws Exception{
        String[] usernames = AccountManagement.getUsernames();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vue/VueConnexion.fxml")); // on charge la première page fxml
        HBox root = loader.load(); // on charge le parent
        primaryStage.setTitle("Ohana Media Player");

        //Group root = new Group();
        //HBox root = new HBox();
        //HBox root = hbox;
        //HBox hb = new HBox();
        //root.getChildren().add(new HBox());

        for(int i = 0; i < 3; i++){
            root.getChildren().add(new TileUser(usernames[i], imgFile));
        }

        //root.getChildrenUnmodifiable().add(new TileUser("toto", imgFile));
        primaryStage.setScene(new Scene(root, 1000, 800));// on set la sec à la fenêtre


        primaryStage.show(); // on affiche la taille

    }


    public static void main(String[] args) {
        launch(args);
    }


}
