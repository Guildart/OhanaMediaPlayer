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


    public void connexion(ActionEvent actionEvent) {
        String password = passField.getText();
        String username = (String) choiceBox.getSelectionModel().getSelectedItem();

        if(username != null){
            if(Modele.AccountManagement.getPassword(username).equals(password)){
                message.setText("Valid Password");
                message.setTextFill(Color.rgb(21, 117, 84));
                message.setVisible(true);
                //Todo charger la scene de la videothèque (Admin ou User selon les cas)
            }else{
                message.setText("Invalid Password");
                message.setTextFill(Color.rgb(210, 39, 30));
                message.setVisible(true);
            }
        }else{
            message.setText("You have to select a username");
            message.setTextFill(Color.rgb(210, 39, 30));
            message.setVisible(true);
        }
    }

    public void enterPassword(KeyEvent actionEvent) {
        if(actionEvent.getCode().equals(KeyCode.ENTER)){
           validButton.fire(); //Si on appuie sur entrée depuis le password field on active le button valider
        }
    }
}
