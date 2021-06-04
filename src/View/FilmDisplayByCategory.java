package View;

import Model.CategoriesDB;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FilmDisplayByCategory extends VBox {
    private HBox filmDisplay;
    private String mycategory;
    private Label title;


    public FilmDisplayByCategory(String category){
        this.mycategory = category;
        this.title = new Label(this.mycategory);

        ScrollPane myScroolPane = new ScrollPane();
        this.filmDisplay = new HBox();
        this.filmDisplay.setSpacing(10);
        this.filmDisplay.setPadding(new Insets(10));
        myScroolPane.setContent(this.filmDisplay);
        myScroolPane.setPannable(true);

        this.getChildren().addAll(this.title, myScroolPane);
    }


}
