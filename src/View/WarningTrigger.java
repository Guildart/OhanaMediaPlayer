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

import java.util.ArrayList;

public class WarningTrigger {
    private static boolean bool;
    private static Stage window;




    //parite film creation
    public static boolean warningWindow(String message){
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Warning");
        window.setMinWidth(250);

        VBox ultraRoot = new VBox();
        ultraRoot.setAlignment(Pos.CENTER);

        Label label = new Label(message);

        Button validate = new Button("Valider");
        Button cancel = new Button("Annuler");

        HBox hbox = new HBox();
        hbox.setSpacing(10);
        ultraRoot.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(validate, cancel);
        ultraRoot.getChildren().addAll(label, hbox);

        validate.setOnAction(ov -> validateAction(ov));
        cancel.setOnAction(ov -> cancelAction(ov));

        Scene scene = new Scene(ultraRoot);
        window.setScene(scene);
        window.showAndWait();
        return bool;

    }

    public static void warningNoFiles(String message){
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("File Not Found");
        window.setMinWidth(250);

        VBox ultraRoot = new VBox();
        ultraRoot.setAlignment(Pos.CENTER);

        Label label = new Label(message);

        Button validate = new Button("Valider");

        HBox hbox = new HBox();
        hbox.setSpacing(10);
        ultraRoot.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(validate);
        ultraRoot.getChildren().addAll(label, hbox);

        validate.setOnAction(ov -> validateAction(ov));

        Scene scene = new Scene(ultraRoot);
        window.setScene(scene);
        window.showAndWait();
    }

    private static void cancelAction(ActionEvent e) {
        bool = false;
        window.close();
    }

    private static void validateAction(ActionEvent e){
        bool = true;
        window.close();
    }


}
