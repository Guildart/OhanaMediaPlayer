package View;

import Controller.CVueVideotheque;
import Model.Account;
import Model.CategoriesDB;
import Model.MoviesDB;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class FilmDisplayByCategory extends VBox {
    private HBox filmDisplay;
    private String myCategory;
    private Label title;
    private CVueVideotheque controller;

    private int numberOfMovies = 0;


    public FilmDisplayByCategory(String category, CVueVideotheque controller){
        this.myCategory = category;
        this.myCategory = myCategory.substring(0, 1).toUpperCase() + myCategory.substring(1); //Première lettre en majuscule
        this.title = new Label(this.myCategory);
        this.title.getStyleClass().add("categoryLabel"); //Attribution class style pour le css
        this.controller = controller;

        Account actualUser = CVueVideotheque.actualUser;
        ArrayList<String> allMyFilm = MoviesDB.getAuthorizedMovies(actualUser.getUserName());

        ScrollPane myScroolPane = new ScrollPane();
        myScroolPane.setMinSize(120,120);
        this.filmDisplay = new HBox();
        this.filmDisplay.setSpacing(10);
        this.filmDisplay.setPadding(new Insets(10));
        myScroolPane.setContent(this.filmDisplay);
        myScroolPane.setPannable(true);

        this.getChildren().addAll(this.title, myScroolPane);

        for(String film : allMyFilm){
            ArrayList<String> currentCategory = MoviesDB.getMovieCategories(film);
            if(currentCategory.contains(this.myCategory.toLowerCase())){
                VBox filmRep = new VBox();
                Button movieBt = new Button();
                movieBt.setOnAction(controller::startMovieFromButton);
                ImageView filmImgVw = new ImageView(CVueVideotheque.actualUser.getImage());
                filmImgVw.setFitWidth(64);
                filmImgVw.setFitHeight(64);
                movieBt.setGraphic(filmImgVw);
                filmRep.setAlignment(Pos.CENTER);
                Label movieLabel = new Label(film);
                movieLabel.getStyleClass().add("movieLabel");
                movieBt.setId(film);
                filmRep.getChildren().addAll(movieBt,movieLabel);

                this.filmDisplay.getChildren().add(filmRep);
                numberOfMovies +=1;
            }
        }
    }

    public int getNumberOfMovies(){
        return numberOfMovies;
    }


}
