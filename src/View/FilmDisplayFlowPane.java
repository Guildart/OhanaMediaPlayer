package View;

import Controller.CVueVideotheque;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class FilmDisplayFlowPane extends FlowPane {


    public FilmDisplayFlowPane(ArrayList<String> films){
        for(String film : films){
            VBox filmRep = new VBox();
            Button movieBt = new Button();
            //movieBt.setOnAction(controller::startMovieFromButton);
            ImageView filmImgVw = new ImageView(CVueVideotheque.actualUser.getImage());
            filmImgVw.setFitWidth(64);
            filmImgVw.setFitHeight(64);
            movieBt.setGraphic(filmImgVw);
            filmRep.setAlignment(Pos.CENTER);
            Label movieLabel = new Label(film);
            movieLabel.getStyleClass().add("movieLabel");
            movieBt.setId(film);
            filmRep.getChildren().addAll(movieBt,movieLabel);
            this.getChildren().add(filmRep);
        }
    }
}