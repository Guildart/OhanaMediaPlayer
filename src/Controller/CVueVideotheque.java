package Controller;

import Model.Account;
import Model.CategoriesDB;
import Model.MoviesDB;
import Model.Role;
import View.FilmDisplayByCategory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CVueVideotheque implements Initializable {

    public static Account actualUser;

    @FXML
    public MenuButton menu;
    public TextField barreRecherche;
    public AnchorPane topPanel;
    public VBox globalVBox;
    public AnchorPane myPane;
    public ScrollPane scrollPane;
    public VBox vboxDisplayMovie;
    public Button buttonAddMovie;
    public Button buttonHandleCategory;
    public Button buttonHandleUser;

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

        imageView.setFitHeight(menu.getPrefHeight());
        imageView.setFitWidth(menu.getPrefWidth());

        menu.setGraphic(imageView);
        ArrayList<String> forbidenCategory = this.actualUser.getForbiddenCategories();
        ArrayList<String> allCategory = CategoriesDB.getCategories();
        for(String forbiden : forbidenCategory){
            allCategory.remove(forbiden);
        }

        for(String allowd : allCategory){
            if(CategoriesDB.getMoviesOfCategory(allowd).size() != 0){
                //this.globalVBox.getChildren().add(new FilmDisplayByCategory(allowd));
                FilmDisplayByCategory filmDisplayByCategory = new FilmDisplayByCategory(allowd,this);
                filmDisplayByCategory.prefWidthProperty().bind(myPane.widthProperty().subtract(20)); /*Pour redimensionnement avec la fenêtre*/
                if (filmDisplayByCategory.getNumberOfMovies() > 0) {
                    this.vboxDisplayMovie.getChildren().add(filmDisplayByCategory);
                }
            }
        }

        if(!actualUser.getRole().equals(Role.admin)){
            this.buttonAddMovie.setVisible(false);
            this.buttonHandleCategory.setVisible(false);
            this.buttonHandleUser.setVisible(false);
        }

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
            String myResearch = this.barreRecherche.getText();

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

    public void startMovieFromButton(ActionEvent actionEvent) {
        Button sender = (Button) actionEvent.getSource();
        String movieToStart = sender.getId();
        System.out.println(movieToStart);
        try {
            CMediaPlayer.changeToMe(sender, this, movieToStart);
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println(e);
        }
    }
}
