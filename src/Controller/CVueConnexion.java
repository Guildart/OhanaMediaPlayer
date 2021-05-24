package Controller;

import Model.Account;
import Model.AccountManagement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CVueConnexion implements Initializable{
    @FXML
    public Label message;
    public Button validButton;
    public HBox accountBox;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("initialize");
        ArrayList<Account> accounts = AccountManagement.getAccounts();
        for(Account account : accounts){
            accountBox.getChildren().add(this.createAccountVBox(account));
            System.out.println(account.getUserName());
        }
        message.setVisible(false);
    }

    public VBox createAccountVBox(Account account){
        VBox accountBox = new VBox();
        accountBox.setAlignment(Pos.CENTER);
        ImageView imgVw = new ImageView();
        Image img = account.getImage();
        imgVw.setImage(img);
        imgVw.setFitWidth(128);
        imgVw.setFitHeight(128);
        Button bt = new Button("");
        bt.setId(account.getUserName());
        bt.setGraphic(imgVw);
        accountBox.getChildren().addAll(bt,new Label(account.getUserName()));
        return accountBox;
    }

/*
    public void connexion(ActionEvent actionEvent) {
        String password = passField.getText();
        String username = (String) choiceBox.getSelectionModel().getSelectedItem();

        if(username != null){
            if(Model.AccountManagement.getPassword(username).equals(password)){
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

    public void enterPassword(KeyEvent actionEvent) {
        if(actionEvent.getCode().equals(KeyCode.ENTER)){
           validButton.fire();
        }
    }
    */
}
