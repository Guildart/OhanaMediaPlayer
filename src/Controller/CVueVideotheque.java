package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class CVueVideotheque implements Initializable {

    @FXML
    public MenuButton menu;
    public TextField barreRecherche;
    public AnchorPane topPanel;


    public void gererCategorie(ActionEvent actionEvent) {
    }

    public void gererUtilisateur(ActionEvent actionEvent) {
    }

    public void ajouterFilm(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream("D:\\Guild\\Université\\S6\\OhanaMediaPlayer\\movie.jpg");
            System.out.print(inputstream);
            ImageView imageView = new ImageView(new Image(inputstream));
            imageView.setFitHeight(topPanel.getPrefHeight());

            //todo : laisser code dur ?

            imageView.setFitHeight(menu.getPrefHeight());
            imageView.setFitWidth(menu.getPrefWidth());

            menu.setGraphic(imageView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void logOut(ActionEvent actionEvent) {
    }

    public void research(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            //todo recherche du film dans DB
        }
    }
}
