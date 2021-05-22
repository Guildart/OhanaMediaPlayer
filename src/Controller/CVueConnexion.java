package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class CVueConnexion implements Initializable{
    @FXML
    public ChoiceBox choiceBox;
    public PasswordField passField;
    public Label message;
    public Button validButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String usernames[] = Modele.AccountManagement.getUsernames();
        for(String s : usernames){
            choiceBox.getItems().add(s);
        }
        message.setVisible(false);
    }


    /**Action lorsqu'on clique sur valider pour tenter une connexion**/
    public void connexion(ActionEvent actionEvent) {
        String password = passField.getText();
        String username = (String) choiceBox.getSelectionModel().getSelectedItem();

        if(username != null){
            if(Modele.AccountManagement.getPassword(username).equals(password)){
                message.setText("Valid Password");
                message.setTextFill(Color.rgb(21, 117, 84));
                //Todo charger la scene de la videothèque (Admin ou User selon les cas)
            }else{
                message.setText("Invalid Password");
                message.setTextFill(Color.rgb(210, 39, 30));
            }
        }else{
            message.setText("You have to select a username");
            message.setTextFill(Color.rgb(210, 39, 30));
        }
        message.setVisible(true);
    }

    /**Clique automatiquement sur le boutton valider si on clique sur la touche entrée depuis le password fiel**/
    public void enterPassword(KeyEvent actionEvent) {
        if(actionEvent.getCode().equals(KeyCode.ENTER)){
           validButton.fire();
        }
    }
}
