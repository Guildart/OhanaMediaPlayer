package Controller;

import Model.Account;
import Model.AccountManagement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
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
    public HBox accountsBox;


    private String currentUserName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupAllButtons();
    }

    public void setupAllButtons(){
        accountsBox.getChildren().clear();
        ArrayList<Account> accounts = AccountManagement.getAccounts();
        for(Account account : accounts){
            accountsBox.getChildren().add(this.createAccountVBox(account));
        }
        message.setVisible(false);
    }

    public void removeAllPasswordFields(){
        for (Node accountBox : accountsBox.getChildren()
             ) {
            if (((VBox) accountBox).getChildren().get(1) instanceof PasswordField){
                ((VBox) accountBox).getChildren().remove(1);
            }

        }
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
        bt.setOnAction(this::tryToConnect);
        bt.setId(account.getUserName());
        bt.setGraphic(imgVw);



        accountBox.getChildren().addAll(bt,new Label(account.getUserName()));
        return accountBox;
    }

    public void tryToConnect(ActionEvent e){

        currentUserName = ((Button) e.getSource()).getId();
        removeAllPasswordFields();
        if (currentUserName.equals("Enfant")){
            connection("");
            return;
        }
        VBox accountVbox = ((VBox) ((Button) e.getSource()).getParent());
        PasswordField pwField = new PasswordField();
        pwField.setPromptText("Enter " + currentUserName + " password :");
        pwField.setOnKeyPressed(this::enterPassword);
        accountVbox.getChildren().add(1, pwField);
    }


    public void connection(String enteredPassword) {
        if(currentUserName != null){
            if(Model.AccountManagement.getUserNamePassword(currentUserName).equals(enteredPassword)){
                message.setText("Welcome");
                message.setTextFill(Color.rgb(21, 117, 84));
                //Todo charger la scene de la videothèque (Admin ou User selon les cas)
            }else{
                message.setText("Invalid Password");
                message.setTextFill(Color.rgb(210, 39, 30));
            }
        }
        message.setVisible(true);
    }

    public void enterPassword(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            connection(((PasswordField) keyEvent.getSource()).getText());
        }
    }
}
