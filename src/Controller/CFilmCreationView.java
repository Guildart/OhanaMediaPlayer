package Controller;

import Model.CategoriesDB;
import Model.MoviesDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class CFilmCreationView implements Initializable{
    @FXML
    public Button choiceButton;
    public TextField pathText;
    public TextField nameText;
    public SplitMenuButton splitMenuCategory;
    public HBox categoryList;
    public ScrollPane notAllowedUsers;
    public Label pathErrorMessage;
    public Label titleErrorMessage;

    public ArrayList<String> categoryAdded = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String category[] = CategoriesDB.getCategories();
        for(int i = 0; i < category.length; i++){
            MenuItem cate = new MenuItem(category[i]);
            cate.setOnAction(e -> categoryClicked(e));
            this.splitMenuCategory.getItems().add(cate);
        }
        this.pathErrorMessage.setVisible(false);
        this.titleErrorMessage.setVisible(false);
    }

    public void categoryClicked(ActionEvent e){
        HBox cateNSupButton = new HBox();
        Button supButton = new Button("x");
        supButton.setOnAction(a -> supClicked(a));
        String categoryName = ((MenuItem)e.getSource()).getText();
        Button newCategory = new Button(categoryName);
        cateNSupButton.getChildren().addAll(newCategory, supButton);
        this.categoryList.getChildren().add(cateNSupButton);
        this.categoryAdded.add(categoryName);
    }

    public void supClicked(ActionEvent e){
        HBox currentHBox = (HBox)((Button)e.getSource()).getParent();
        this.categoryAdded.remove(((Button)currentHBox.getChildren().get(0)).getText());
        this.categoryList.getChildren().remove(currentHBox);
    }



    public void choiceAction(ActionEvent e){
        Stage stage = new Stage();
        FileChooser myFileChooser = new FileChooser();
        FileChooser.ExtensionFilter filters = new FileChooser.ExtensionFilter("MP4 files (*.mp4)", "*.mp4");
        myFileChooser.getExtensionFilters().add(filters);
        myFileChooser.setTitle("selectioner un film");
        File file = myFileChooser.showOpenDialog(stage);
        if (file != null) {
            String myPath = file.getPath();
            this.pathText.setText(myPath);
        }
    }

    public void validate(ActionEvent e) throws IOException {
        this.pathErrorMessage.setVisible(false);
        this.titleErrorMessage.setVisible(false);
        if(this.pathText.getText() != "" &&
           this.nameText.getText() != ""){
            //MoviesDB.addMovie(nameText.getText(), pathText.getText(), this.categoryAdded);
            System.out.println("titre : " + this.nameText.getText());
            System.out.println("chemain d'acces : " + this.pathText.getText());
            System.out.println("liste des categorie \n" + this.categoryAdded);
        }
        else {
            if(this.pathText.getText() == ""){
                this.pathErrorMessage.setVisible(true);
            }
            if(this.nameText.getText() == ""){
                this.titleErrorMessage.setVisible(true);
            }
        }
    }

    public void updateUserAllowed(){

    }


}
