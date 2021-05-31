package View;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class CategoryView extends HBox {
    private Button categoryButton;
    private Button xButton;


    public CategoryView(String categoryName, Boolean showSup){

        this.categoryButton = new Button(categoryName);
        this.categoryButton.setDisable(true);

        this.xButton = new Button("x");
        if(!showSup){
            this.xButton.setVisible(false);
        }
        //l'action a set up pour ce bouton est a faire selon le cas d'utilisation exemple dans CFilmCreationView

        this.getChildren().addAll(categoryButton, xButton);
    }

    public Button getXButton(){
        return this.xButton;
    }
}
