package Controller;

import Model.Account;
import Model.AccountManagement;
import View.CategoryView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.event.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class CUserManager implements Initializable {

    @FXML
    private VBox toAddOn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupAllAccounts();
    }

    private void setupAllAccounts(){
        toAddOn.getChildren().clear();
        HashMap<String,Account> accounts = AccountManagement.getAccounts();
        System.out.println(accounts.size());
        for(String key : accounts.keySet()){
            toAddOn.getChildren().add(this.createAccountHBox(accounts.get(key)));
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

        //code to generate categories
        FlowPane forbiddenCategories = new FlowPane();
        forbiddenCategories.getChildren().add(new Label("forbidden : "));
        for (String forbiddenCategory :
             account.getForbiddenCategories()) {
            CategoryView categoryView = new CategoryView(forbiddenCategory, true);
            categoryView.getXButton().setId(account.getUserName());
            categoryView.getXButton().setOnAction(this::allowCategory);
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

    private void allowCategory(ActionEvent actionEvent) {
        String username =((Button) actionEvent.getSource()).getId();
        Account toAllowOn = AccountManagement.getAccount(username);
        String categoryToAllow =
                ((Button)((HBox)((Button) actionEvent.getSource()).getParent()).getChildren().get(0)).getText();
        System.out.println(username + "  " + categoryToAllow);
        toAllowOn.allow(categoryToAllow);
        AccountManagement.saveAccount(toAllowOn);

        setupAllAccounts();
    }

    private void changeAccountImage(ActionEvent actionEvent) {
    }

    @FXML
    public void getBackToMenu(ActionEvent actionEvent) throws IOException {
        CVueVideotheque.changeToMe(toAddOn,this);
    }


}
