package Controller;

import Model.Account;
import Model.CategoriesDB;
import Model.MoviesDB;
import Model.Role;
import View.FilmDisplayByCategory;
import View.FilmDisplayFlowPane;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Struct;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public Button stopSearchingButton;

    public FilmDisplayFlowPane flowPaneDisplayMovie;
    public ToggleButton displayButton;
    public ComboBox comboBox;
    public Button searchButton;
    public HBox searchHBox;

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
        this.stopSearchingButton.setVisible(false);
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

        if(CategoriesDB.getMoviesWithoutCategory().size()!=0){
            FilmDisplayByCategory filmDisplayByCategory = new FilmDisplayByCategory(null,this);
            filmDisplayByCategory.prefWidthProperty().bind(myPane.widthProperty().subtract(20)); /*Pour redimensionnement avec la fenêtre*/
            if (filmDisplayByCategory.getNumberOfMovies() > 0) {
                this.vboxDisplayMovie.getChildren().add(filmDisplayByCategory);
            }
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

        ArrayList<String>  allCategories = CategoriesDB.getCategories();
        comboBox.getItems().add(0,"Toutes catégories");
        comboBox.getItems().addAll(CategoriesDB.getCategories());
        comboBox.getSelectionModel().select("Toutes catégories");
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
            searchButton.fire();
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
            CMediaPlayer.playVideo(sender, this, movieToStart);
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public void stopSearch(ActionEvent e){
        this.scrollPane.setContent(this.vboxDisplayMovie);
        this.stopSearchingButton.setVisible(false);
    }

    public void changeLayout(ActionEvent e){
        if(this.scrollPane.getContent() == this.vboxDisplayMovie){
            this.flowPaneDisplayMovie = new FilmDisplayFlowPane(MoviesDB.getAuthorizedMovies(this.actualUser.getUserName()));
            this.flowPaneDisplayMovie.prefWidthProperty().bind(scrollPane.widthProperty().subtract(20));
            this.scrollPane.setContent(this.flowPaneDisplayMovie);
            displayButton.setStyle("-fx-background-image:url(file:res/list.png);");
        }
        else{
            this.scrollPane.setContent(this.vboxDisplayMovie);
            displayButton.setStyle("-fx-background-image:url(file:res/grid.png);");

        }
    }

    public void goHome(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) menu.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/VueVideotheque.fxml"));
        Parent root = loader.load();
        stage.setMaximized(true);
        Scene scene = new Scene(root, menu.getScene().getWidth(), menu.getScene().getHeight());
        stage.setScene(scene);
        stage.show();
    }

    public void selectCategory(ActionEvent actionEvent) {
        String getSelection = (String) comboBox.getSelectionModel().getSelectedItem();
        if(getSelection.equals("Toutes catégories")){
            this.scrollPane.setContent(this.vboxDisplayMovie);
            displayButton.setStyle("-fx-background-image:url(file:res/grid.png);");
            //displayButton.setVisible(true);
            displayButton.setDisable(false);
        }else{
            this.flowPaneDisplayMovie = new FilmDisplayFlowPane(CategoriesDB.getMoviesOfCategory(getSelection));
            this.flowPaneDisplayMovie.prefWidthProperty().bind(scrollPane.widthProperty().subtract(20));
            this.scrollPane.setContent(this.flowPaneDisplayMovie);
            //displayButton.setVisible(false);
            displayButton.setDisable(true);
        }
    }


    private ArrayList<String> searchingAlgorithm(String recherche){
        recherche = recherche.toLowerCase();
        final String SEPARATEUR = " ";
        String mots[] = recherche.split(SEPARATEUR);
        HashMap<String, Integer> movies = new HashMap<String, Integer>();
        ArrayList<String> test = new ArrayList<>();

        for(String movie : MoviesDB.getTitles()) {
            String movieLowerCase = movie.toLowerCase();
            int compteur = mots.length;
            for (String s : mots) {
                if(movieLowerCase.contains(s))
                    compteur-=1;
            }
            if(compteur != mots.length) {
                movies.put(movie, compteur);
                System.out.println(true);
            }
        }

        Comparator<Map.Entry<String, Integer>> valueComparator =
                Comparator.comparing(Map.Entry::getValue);
        Map<String, Integer> sortedMap =
                movies.entrySet().stream().
                        sorted(valueComparator).
                        collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new));
        for(String key : sortedMap.keySet())
            System.out.println(key + ":" +sortedMap.get(key));
        return new ArrayList<String>(sortedMap.keySet());
    }

    public void onClickSearch(ActionEvent actionEvent) {
        if(searchHBox.getChildren().contains(barreRecherche)){
            this.stopSearchingButton.setVisible(true);
            String myResearch = this.barreRecherche.getText();
            ArrayList<String> searchFilm = this.searchingAlgorithm(myResearch);
            this.flowPaneDisplayMovie = new FilmDisplayFlowPane(searchFilm);
            this.flowPaneDisplayMovie.prefWidthProperty().bind(scrollPane.widthProperty().subtract(20));
            this.scrollPane.setContent(this.flowPaneDisplayMovie);
        }
    }
}
