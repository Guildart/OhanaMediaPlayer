package View;

import Controller.CVueVideotheque;
import Model.Account;
import Model.CategoriesDB;
import Model.MoviesDB;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class FilmDisplayByCategory extends HBox {
    private VBox vboxCat;
    private HBox filmDisplay;
    private String myCategory;
    private Label title;
    private Button next;
    private Button previous;
    private ScrollPane myScroolPane;


    public FilmDisplayByCategory(String category){

        next = new Button(">");
        previous = new Button("<");

        next.setMinSize(next.getPrefHeight(), next.getPrefWidth());

        next.setOnAction(this::onNext);
        previous.setOnAction(this::onPrevious);

        this.myCategory = category;
        this.myCategory = myCategory.substring(0, 1).toUpperCase() + myCategory.substring(1); //Première lettre en majuscule
        this.title = new Label(this.myCategory);
        this.title.getStyleClass().add("categoryLabel"); //Attribution class style pour le css

        Account actualUser = CVueVideotheque.actualUser;
        ArrayList<String> allMyFilm = MoviesDB.getAuthorizedMovies(actualUser.getUserName());

        myScroolPane = new ScrollPane();
        myScroolPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        myScroolPane.setMinSize(120,120);
        this.vboxCat = new VBox();
        this.setAlignment(Pos.CENTER_LEFT);
        this.filmDisplay = new HBox();
        this.filmDisplay.setSpacing(10);
        this.filmDisplay.setPadding(new Insets(10));
        myScroolPane.setContent(this.filmDisplay);
        myScroolPane.setPannable(true);

        this.getChildren().add(previous);
        vboxCat.getChildren().addAll(this.title, myScroolPane);
        this.getChildren().add(vboxCat);
        this.getChildren().add(next);

        for(String film : allMyFilm){
            ArrayList<String> currentCategory = MoviesDB.getMovieCategories(film);
            if(currentCategory.contains(this.myCategory.toLowerCase())){
                VBox filmRep = new VBox();
                ImageView filmImgVw = new ImageView(CVueVideotheque.actualUser.getImage());
                filmImgVw.setFitWidth(64);
                filmImgVw.setFitHeight(64);
                filmRep.setAlignment(Pos.CENTER);
                Label movieLabel = new Label(film);
                movieLabel.getStyleClass().add("movieLabel");
                filmRep.getChildren().addAll(filmImgVw,movieLabel);

                this.filmDisplay.getChildren().add(filmRep);
            }
        }

        vboxCat.widthProperty().addListener((observable, oldValue, newValue) -> {
            this.visible();
        });

    }


    private void visible() {
        if(this.filmDisplay.getWidth() > vboxCat.getWidth()){
            next.setVisible(true);
            previous.setVisible(true);
        }else{
            next.setVisible(false);
            previous.setVisible(false);
        }
    }

    public void onNext(ActionEvent actionEvent){
        ScrollBar myScrollBar = null;
        for(Node node : myScroolPane.lookupAll(".scroll-bar")){
            ScrollBar scroll = (ScrollBar) node;
            if(scroll.getOrientation() == Orientation.HORIZONTAL)
                myScrollBar = scroll;
        }
        //myScrollBar.setUnitIncrement(0.1);
        //System.out.println(myScrollBar.getUnitIncrement());
        myScrollBar.increment();
    }

    public void onPrevious(ActionEvent actionEvent){
        ScrollBar myScrollBar = null;
        for(Node node : myScroolPane.lookupAll(".scroll-bar")){
            ScrollBar scroll = (ScrollBar) node;
            if(scroll.getOrientation() == Orientation.HORIZONTAL)
                myScrollBar = scroll;
        }
        //System.out.println(myScrollBar.getUnitIncrement());
        System.out.println(this.filmDisplay.getWidth() > myScroolPane.getScene().getWidth());
        //myScrollBar.setUnitIncrement(0.1);
        myScrollBar.decrement();

    }
}
