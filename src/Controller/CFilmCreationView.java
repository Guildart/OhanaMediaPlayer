package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class CFilmCreationView {
    @FXML
    public Button choiceButton;
    public TextField pathText;
    public TextField nameText;
    public ScrollPane CategoryList;
    public ScrollPane notAllowedUsers;




    public void choiceAction(ActionEvent e){
        System.out.println("ta mere");
    }
}
