package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.awt.*;
import java.io.File;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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

    public void addCategory(ActionEvent e) {
        Button testButton = new Button("test");
        this.categoryList.getChildren().add(testButton);
    }


}
