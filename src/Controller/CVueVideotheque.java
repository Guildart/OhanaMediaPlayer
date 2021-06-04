package Controller;

import Model.Account;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CVueVideotheque implements Initializable {

    public static Account actualUser;

    @FXML
    public MenuButton menu;
    public TextField barreRecherche;
    public AnchorPane topPanel;

    public void gererCategorie(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) menu.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/CategoryManagerView.fxml"));
        Parent root = loader.load();
        stage.setMaximized(true);
        Scene scene = new Scene(root, menu.getScene().getWidth(), menu.getScene().getHeight());
        stage.setScene(scene);
        stage.show();
    }

    public void gererUtilisateur(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) menu.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/UserManagerView.fxml"));
        Parent root = loader.load();
        stage.setMaximized(true);
        Scene scene = new Scene(root, menu.getScene().getWidth(), menu.getScene().getHeight());
        stage.setScene(scene);
        stage.show();
    }

    public void ajouterFilm(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) menu.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/FilmCreationView.fxml"));
        Parent root = loader.load();
        stage.setMaximized(true);
        Scene scene = new Scene(root, menu.getScene().getWidth(), menu.getScene().getHeight());
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ImageView imageView = new ImageView(actualUser.getImage());
        imageView.setFitHeight(topPanel.getPrefHeight());
        imageView.setFitHeight(menu.getPrefHeight());
        imageView.setFitWidth(menu.getPrefWidth());
        menu.setGraphic(imageView);


    }

    public void logOut(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) menu.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/VueConnexion.fxml"));
        Parent root = loader.load();
        stage.setMaximized(true);
        Scene scene = new Scene(root, menu.getScene().getWidth(), menu.getScene().getHeight());
        stage.setScene(scene);
        stage.show();
    }

    public void research(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            //todo recherche du film dans DB
        }
    }

    @FXML
    public static void changeToMe(Node anySceneNode, Object controller) throws IOException {
        Stage stage = (Stage) anySceneNode.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(controller.getClass().getResource("/View/VueVideotheque.fxml"));
        Parent root = loader.load();
        stage.setMaximized(true);
        Scene scene = new Scene(root, anySceneNode.getScene().getWidth(), anySceneNode.getScene().getHeight());
        stage.setScene(scene);
        stage.show();
    }
}
