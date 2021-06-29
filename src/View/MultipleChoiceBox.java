package View;

import Model.CategoriesDB;
import Model.MoviesDB;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.ArrayList;

public class MultipleChoiceBox {
    private static ArrayList<String> categorySelected = new ArrayList<>();
    private static ArrayList<String> filmSelected = new ArrayList<>();
    private static String oneFilm;




    //parite film creation
    public static ArrayList<String> displayCategory(String title, ArrayList<String> alreadyAdd){
        categorySelected.clear();
        for(String alredy : alreadyAdd){
            categorySelected.add(alredy);
        }

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        VBox ultraRoot = new VBox();

        FlowPane root = new FlowPane();
        root.setHgap(10);
        root.setVgap(20);
        root.setPadding(new Insets(15,15,15,15));


        ArrayList<String> allCategory = CategoriesDB.getCategories();
        for(String category : allCategory){
            Button categoryButton = new Button(category);
            if(categorySelected.contains(category)){
                categoryButton.setStyle("-fx-background-color: #00ff00");
            }
            categoryButton.setOnAction(e -> categoryPressed(e) );
            root.getChildren().add(categoryButton);
        }

        ultraRoot.getChildren().add(root);

        Button validate = new Button("valider");
        VBox vbox = new VBox();
        vbox.setPrefHeight(validate.getPrefHeight());
        vbox.setPrefWidth(ultraRoot.getWidth());
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(validate);

        validate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                window.close();
            }
        });
        ultraRoot.getChildren().add(vbox);

        Scene scene = new Scene(ultraRoot);
        scene.getStylesheets().add("file:src/css/manager.css");
        window.setScene(scene);
        window.showAndWait();
        return categorySelected;

    }

    private static void categoryPressed(ActionEvent e) {
        String actualCategory = ((Button)e.getSource()).getText();
        if(categorySelected.contains(actualCategory)){
            categorySelected.remove(actualCategory);
            ((Button)e.getSource()).setStyle("-fx-background-color: grey");
        }
        else{
            categorySelected.add(actualCategory);
            ((Button)e.getSource()).setStyle("-fx-background-color: #00ff00");
        }
    }
    //fin parite film creation

    //debut parite category creation

    public static ArrayList<String> displayFilm(String title, ArrayList<String> alredyAdd){
        filmSelected.clear();
        for(String film : alredyAdd){
            filmSelected.add(film);
        }
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        VBox ultraRoot = new VBox();

        FlowPane root = new FlowPane();
        root.setHgap(10);
        root.setVgap(20);
        root.setPadding(new Insets(15,15,15,15));

        ArrayList<String> allFilm = MoviesDB.getTitles();
        for(String film : allFilm){
            Button filmButton = new Button(film);
            if(filmSelected.contains(film)){
                filmButton.setStyle("-fx-background-color: #00ff00");
            }
            filmButton.setOnAction(e -> filmPressed(e));
            root.getChildren().add(filmButton);
        }

        ultraRoot.getChildren().add(root);

        Button validate = new Button("valider");
        VBox vbox = new VBox();
        vbox.setPrefHeight(validate.getPrefHeight());
        vbox.setPrefWidth(ultraRoot.getWidth());
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(validate);
        validate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                window.close();
            }
        });
        ultraRoot.getChildren().add(vbox);

        Scene scene = new Scene(ultraRoot);
        scene.getStylesheets().add("file:src/css/manager.css");
        window.setScene(scene);
        window.showAndWait();
        return filmSelected;
    }

    public static void filmPressed(ActionEvent e){
        String actualFilm = ((Button)e.getSource()).getText();
        if(filmSelected.contains(actualFilm)){
            filmSelected.remove(actualFilm);
            ((Button)e.getSource()).setStyle("-fx-background-color: grey");
        }
        else{
            filmSelected.add(actualFilm);
            ((Button)e.getSource()).setStyle("-fx-background-color: #00ff00");
        }
    }

    //Bonus part pour ne renvoyer qu'un film
    public static String displayOneFilm(String title){
        oneFilm = "";

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);


        FlowPane root = new FlowPane();
        root.setHgap(10);
        root.setVgap(20);
        root.setPadding(new Insets(15,15,15,15));

        ArrayList<String> allFilm = MoviesDB.getTitles();
        for(String film : allFilm){
            Button filmButton = new Button(film);
            filmButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    oneFilm = filmButton.getText();
                    window.close();
                }
            });
            root.getChildren().add(filmButton);
        }


        Scene scene = new Scene(root);
        scene.getStylesheets().add("file:src/css/manager.css");
        window.setScene(scene);
        window.showAndWait();

        return oneFilm;
    }


}
