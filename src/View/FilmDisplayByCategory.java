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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class FilmDisplayByCategory extends HBox {
    private VBox vboxCat;
    public HBox filmDisplay;
    private String myCategory;
    private Label title;
    private CVueVideotheque controller;
    private Button next;
    private Button previous;
    public ScrollPane myScroolPane;

    private int numberOfMovies = 0;


    public FilmDisplayByCategory(String category, CVueVideotheque controller) {

        Background back;
        next = new Button();
        previous = new Button();

        next.setPrefSize(25, 25);

        next.setOnAction(this::onNext);
        previous.setOnAction(this::onPrevious);

        //next.getStyleClass().add("button-next");

        Image img = new Image("file:res/next.png");
        ImageView view = new ImageView(img);
        view.setFitHeight(50);
        view.setPreserveRatio(true);
        next.setGraphic(view);

        img = new Image("file:res/previous.png");
        view = new ImageView(img);
        view.setFitHeight(50);
        view.setPreserveRatio(true);
        previous.setGraphic(view);

        this.myCategory = category;
        if(category == null){
            this.myCategory = "Sans categorie";
        }
        this.myCategory = myCategory.substring(0, 1).toUpperCase() + myCategory.substring(1); //Première lettre en majuscule
        this.title = new Label(this.myCategory);
        this.title.getStyleClass().add("categoryLabel"); //Attribution class style pour le css
        this.controller = controller;

        Account actualUser = CVueVideotheque.actualUser;
        ArrayList<String> allMyFilm = MoviesDB.getAuthorizedMovies(actualUser.getUserName());
        allMyFilm.sort(Comparator.naturalOrder());

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
            if(currentCategory.contains(this.myCategory.toLowerCase()) || (category == null && currentCategory.isEmpty())){
                VBox filmRep = new VBox();
                Button movieBt = new Button();
                movieBt.setOnAction(controller::startMovieFromButton);
                //ImageView filmImgVw = new ImageView(CVueVideotheque.actualUser.getImage());
                ImageView filmImgVw = new ImageView(MoviesDB.getImagePath(film));
                filmImgVw.setFitWidth(171);
                filmImgVw.setFitHeight(96);
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

        vboxCat.widthProperty().addListener((observable, oldValue, newValue) -> {
            this.visible();
        });

        filmDisplay.widthProperty().addListener((observable, oldValue, newValue) -> {
            this.visible();
        });
    }


    public void visible() {
        System.out.println(this.filmDisplay.getWidth() + " : " + vboxCat.getWidth());
        if(this.filmDisplay.getWidth() > vboxCat.getWidth()){
            next.setVisible(true);
            previous.setVisible(true);
        }else{
            next.setVisible(false);
            previous.setVisible(false);
        }
    }

    public int getNumberOfMovies(){
        return numberOfMovies;
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
