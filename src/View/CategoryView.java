package View;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CategoryView extends HBox {
    private Label label;
    private Button xButton;


    public CategoryView(String categoryName, Boolean showSup){

        this.label = new Label(categoryName);
        this.label.setDisable(true);
        this.label.getStyleClass().add("pastille");

        this.xButton = new Button("x");
        this.xButton.getStyleClass().add("close-button");
        this.label.setGraphic(xButton);
        label.setContentDisplay(ContentDisplay.RIGHT);
        if(!showSup){
            this.xButton.setVisible(false);
        }
        //l'action a set up pour ce bouton est a faire selon le cas d'utilisation exemple dans CFilmCreationView

        this.getChildren().addAll(label, xButton);
    }

    public Button getXButton(){
        return this.xButton;
    }


}
