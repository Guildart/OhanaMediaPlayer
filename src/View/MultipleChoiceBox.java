package View;

import Model.CategoriesDB;
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
    private static boolean answer;
    private static ArrayList<String> categorySelected = new ArrayList<>();
    private static ArrayList<String> filmSelected = new ArrayList<>();


    public static ArrayList<String> displayCategory(String title, ArrayList<String> alreadyAdd){
        categorySelected.clear();
        for(String alredy : alreadyAdd){
            categorySelected.add(alredy);
        }
        System.out.println(categorySelected);

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
            Button categoryButoon = new Button(category);
            if(categorySelected.contains(category)){
                categoryButoon.setStyle("-fx-background-color: #00ff00");
            }
            categoryButoon.setOnAction(e -> categoryPressed(e) );
            root.getChildren().add(categoryButoon);
        }

        ultraRoot.getChildren().add(root);

        Button validate = new Button("valider");
        validate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                window.close();
            }
        });
        ultraRoot.getChildren().add(validate);

        Scene scene = new Scene(ultraRoot);
        window.setScene(scene);
        window.showAndWait();
        return categorySelected;

    }

    private static void categoryPressed(ActionEvent e) {
        String actualCategory = ((Button)e.getSource()).getText();
        if(categorySelected.contains(actualCategory)){
            categorySelected.remove(actualCategory);
            ((Button)e.getSource()).setStyle("-fx-background-color: #ffffff");
        }
        else{
            categorySelected.add(actualCategory);
            ((Button)e.getSource()).setStyle("-fx-background-color: #00ff00");
        }
    }
}
