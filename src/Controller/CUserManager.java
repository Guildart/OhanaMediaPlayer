package Controller;

import Model.Account;
import Model.AccountManagement;
import Model.CategoriesDB;
import View.CategoryView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.event.*;
import javafx.scene.paint.Color;
import java.net.URL;
import java.util.HashMap;
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
        HashMap<String,Account> accounts = AccountManagement.getAccounts();
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

        FlowPane forbiddenCategories = new FlowPane();
        forbiddenCategories.getChildren().add(new Label("forbidden : "));
        for (String forbiddenCategory :
             account.getForbiddenCategories()) {
            forbiddenCategories.getChildren().add(createCategoryView(forbiddenCategory,account));
        }

        Label message = new Label();
        message.setTextFill(Color.RED);
        message.setText("");
        forbiddenCategories.getChildren().addAll(createForbidButton(account.getUserName()),message);
        accountBox.getChildren().addAll(identifiersBox, forbiddenCategories);

        return accountBox;
    }

    private CategoryView createCategoryView(String categoryName, Account account){
        CategoryView categoryView = new CategoryView(categoryName, true);
        categoryView.getXButton().setId(account.getUserName());
        categoryView.getXButton().setOnAction(this::allowCategory);
        return categoryView;
    }

    private Button createForbidButton(String accountUserName){
        Button forbidBt = new Button();
        forbidBt.setText("+");
        forbidBt.setId(accountUserName);
        forbidBt.setOnAction(this::onAddCategoryPressed);
        return forbidBt;
    }

    private TextField createForbidTextField(String accountUserName){
        TextField forbidTextField = new TextField();
        forbidTextField.setId(accountUserName);
        forbidTextField.setOnKeyPressed(this::categoryEnteredOrEscaped);
        return forbidTextField;
    }

    private void onAddCategoryPressed(ActionEvent actionEvent) {
        Button source = ((Button) actionEvent.getSource());
        ObservableList<Node> userCategories =  ((FlowPane) source.getParent()).getChildren();
        int toAddAt = userCategories.indexOf(source);
        userCategories.remove(source);
        userCategories.add(toAddAt, createForbidTextField(source.getId()));
    }

    private void categoryEnteredOrEscaped(KeyEvent keyEvent){
        TextField source = ((TextField) keyEvent.getSource());
        ObservableList<Node> userCategories = ((FlowPane) source.getParent()).getChildren();
        int toAddAt = userCategories.indexOf(source);
        if (keyEvent.getCode().equals(KeyCode.ESCAPE)){
            userCategories.remove(source);

            userCategories.add(toAddAt, createForbidButton(source.getId()));
            displayMessage(userCategories,"");
        }
        if (keyEvent.getCode().equals(KeyCode.ENTER)){
            String enteredText = source.getText();
            if (enteredText.length() != 0){
                String username =(source).getId();
                Account toForbidOn = AccountManagement.getAccount(username);
                String categoryToForbid =
                        enteredText;
                if (toForbidOn.forbid(categoryToForbid)){
                    AccountManagement.saveAccount(toForbidOn);
                    userCategories.remove(source);
                    userCategories.add(toAddAt, createForbidButton(source.getId()));
                    userCategories.add(toAddAt,createCategoryView(enteredText,toForbidOn));
                    displayMessage(userCategories,"");
                    CategoriesDB.addCategory(enteredText);
                }else{
                    displayMessage(userCategories,"the added category has to be unique for this user");
                }
            }else{
                displayMessage(userCategories,"the added category has to not be empty");
            }
        }
    }

    public static void displayMessage(ObservableList<Node> toDisplayMessageOn, String message){
        ((Label) toDisplayMessageOn.get(toDisplayMessageOn.size()-1)).setText(message);
    }

    private void allowCategory(ActionEvent actionEvent) {
        Button source =  (Button) actionEvent.getSource();

        String username =(source).getId();
        Account toAllowOn = AccountManagement.getAccount(username);
        String categoryToAllow =
                ((Button)((HBox)(source).getParent()).getChildren().get(0)).getText();
        toAllowOn.allow(categoryToAllow);
        AccountManagement.saveAccount(toAllowOn);

        ObservableList<Node> userCategories =  ((FlowPane) source.getParent().getParent()).getChildren();
        userCategories.remove(source.getParent());
    }

    private void changeAccountImage(ActionEvent actionEvent) {
    }


}
