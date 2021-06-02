package Controller;

import Model.Account;
import Model.AccountManagement;
import View.CategoryView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.event.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CUserManager implements Initializable {

    @FXML
    private VBox toAddOn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupAllAccounts();
    }

    private void setupAllAccounts(){
        toAddOn.getChildren().clear();
        ArrayList<Account> accounts = AccountManagement.getAccounts();
        System.out.println(accounts.size());
        for(Account account : accounts){
            toAddOn.getChildren().add(this.createAccountHBox(account));
        }
    }

    private HBox createAccountHBox(Account account){
        HBox accountBox = new HBox();

        accountBox.setAlignment(Pos.CENTER);

        //code to generate img & textfield
        VBox identifiersBox = new VBox();
        ImageView imgVw = new ImageView();
        Image img = account.getImage();
        imgVw.setImage(img);
        imgVw.setFitWidth(64);
        imgVw.setFitHeight(64);
        Button bt = new Button("");
        bt.setId(account.getUserName());
        bt.setGraphic(imgVw);
        bt.setOnAction(this::changeAccountImage);


        identifiersBox.getChildren().addAll(bt,new TextField(account.getUserName()));

        FlowPane forbiddenCategories = new FlowPane();
        forbiddenCategories.getChildren().add(new Label("forbidden : "));
        for (String forbiddenCategory :
             account.getForbiddenCategories()) {
            CategoryView categoryView = new CategoryView(forbiddenCategory, true);
            forbiddenCategories.getChildren().add(categoryView);
        }
        Button forbidBt = new Button();
        forbidBt.setText("+");
        forbidBt.setId(account.getUserName());

        forbidBt.setOnAction(this::forbidCategory);

        forbiddenCategories.getChildren().add(forbidBt);




        accountBox.getChildren().addAll(identifiersBox, forbiddenCategories);

        return accountBox;
    }

    private void forbidCategory(ActionEvent actionEvent) {
    }

    private void changeAccountImage(ActionEvent actionEvent) {
    }


}
