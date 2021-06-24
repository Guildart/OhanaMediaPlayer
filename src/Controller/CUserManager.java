package Controller;

import Model.Account;
import Model.AccountManagement;
import Model.CategoriesDB;
import Model.Role;
import View.CategoryView;
import View.MultipleChoiceBox;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javafx.event.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CUserManager implements Initializable {

    @FXML
    private VBox toAddOn;


    private HashMap<String, TextField> oldUserNameToNewUserNameTextfield = new HashMap<>(0);
    private HashMap<String, TextField> oldUserNameToNewPasswordTextfield = new HashMap<>(0);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupAllAccounts();
    }

    public void saveNewUsernamesAndPasswords(){
        HashMap<String,Account> accountHash = AccountManagement.getAccounts();
        for (String username: oldUserNameToNewUserNameTextfield.keySet()) {
            String newUserName = oldUserNameToNewUserNameTextfield.get(username).getText();
            String newPassword = oldUserNameToNewPasswordTextfield.get(username).getText();
            Account updatedAccount = accountHash.get(username);
            if (updatedAccount != null) {
                accountHash.remove(username);
                updatedAccount.setUserName(newUserName);
                updatedAccount.setPassword(newPassword);
                accountHash.put(newUserName, updatedAccount);
            }
        }
        try {
            AccountManagement.saveAccounts(accountHash);
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("couldn't save modified username & passwords");
        }
    }


    private void setupAllAccounts(){
        if (toAddOn.getChildren().size() > 0){
            saveNewUsernamesAndPasswords();
        }
        toAddOn.getChildren().clear();
        HashMap<String,Account> accounts = AccountManagement.getAccounts();
        for(String key : accounts.keySet()){
            toAddOn.getChildren().add(this.createAccountHBox(accounts.get(key)));
        }
        Button accountCreatorBt = new Button("create a new Account");
        accountCreatorBt.setOnAction(this::createAccount);
        toAddOn.getChildren().add(accountCreatorBt);
    }

    private void createAccount(ActionEvent actionEvent) {
        AccountManagement.createAccount(actionEvent);
        setupAllAccounts();
    }

    private HBox createAccountHBox(Account account){
        HBox accountBox = new HBox();
        accountBox.setAlignment(Pos.CENTER);
        accountBox.setMaxWidth(Double.MAX_VALUE);
        accountBox.setPrefWidth(1500);
        //code to generate img & textfield
        VBox identifiersBox = new VBox();
        ImageView imgVw = new ImageView();
        Image img = account.getImage();
        imgVw.setImage(img);
        imgVw.setFitWidth(64);
        imgVw.setFitHeight(64);
        Button profilePictureButton = new Button("");
        profilePictureButton.getStyleClass().add("profilePicture");
        profilePictureButton.setId(account.getUserName());
        profilePictureButton.setGraphic(imgVw);
        profilePictureButton.setOnAction(this::changeAccountImage);

        TextField textFieldUserName = new TextField(account.getUserName());
        TextField textFieldPassword = new TextField(account.getPassword());

        oldUserNameToNewUserNameTextfield.put(account.getUserName(),textFieldUserName);
        oldUserNameToNewPasswordTextfield.put(account.getUserName(),textFieldPassword);
        if (account.getRole().equals(Role.child)){
            textFieldPassword.setDisable(true);
        }

        identifiersBox.getChildren().addAll(profilePictureButton,new Label("Username"),textFieldUserName,
                new Label("Password"),textFieldPassword);

        //code to generate categories
        FlowPane forbiddenCategories = new FlowPane();
        forbiddenCategories.setVgap(10);
        forbiddenCategories.setHgap(10);
        forbiddenCategories.getChildren().add(new Label("forbidden : "));

        for (String forbiddenCategory :
             account.getForbiddenCategories()) {
            forbiddenCategories.getChildren().add(createCategoryView(forbiddenCategory,account));
        }

        Label message = new Label();
        message.setTextFill(Color.RED);
        message.setText("");
        //TextField forbidTextField = createForbidTextField(account.getUserName());
        //Button forbidButton = createForbidButton(account.getUserName());


        //forbiddenCategories.getChildren().addAll(forbidTextField,forbidButton,message);

        HBox.setHgrow(forbiddenCategories, Priority.SOMETIMES);
        forbiddenCategories.setMaxWidth(Double.MAX_VALUE);
        forbiddenCategories.setPrefWidth(1000);

        Button multipleCatSelect = new Button("forbid/allow");
        multipleCatSelect.setId(account.getUserName());
        multipleCatSelect.setOnAction(this::multipleCatSelect);
        forbiddenCategories.getChildren().add(multipleCatSelect);


        if (account.getRole() == Role.other) {
            Button deleteButton = new Button("delete this account");

            deleteButton.setOnAction(this::tryToDeleteAccount);
            deleteButton.setId(account.getUserName());

            accountBox.getChildren().addAll(identifiersBox, forbiddenCategories,deleteButton);

        }else{
            accountBox.getChildren().addAll(identifiersBox, forbiddenCategories);
        }


        return accountBox;
    }

    private void multipleCatSelect(ActionEvent actionEvent) {
        Account account = AccountManagement.getAccount(((Button) actionEvent.getSource()).getId());
        ArrayList<String> newForbidden = MultipleChoiceBox.displayCategory("Highlight in green the category to forbid.",account.getForbiddenCategories());
        account.setForbiddenCategories(newForbidden);
        AccountManagement.saveAccount(account);
        setupAllAccounts();
    }

    private void tryToDeleteAccount(ActionEvent actionEvent){
        String accountName = ((Button) actionEvent.getSource()).getId();

        Stage confirmationWindow=new Stage();
        confirmationWindow.initModality(Modality.APPLICATION_MODAL);
        confirmationWindow.setTitle("Warning!");

        Label label= new Label("Are you sure you want to delete "+ accountName+ "'s account?");
        label.setTextFill(Color.RED);


        Button confirmBt= new Button("Yes");
        Button cancelBt= new Button("No");



        confirmBt.setOnAction(e -> {
                                    AccountManagement.deleteAccount(accountName);
                                    setupAllAccounts();
                                    confirmationWindow.close();
        });
        cancelBt.setOnAction(e -> confirmationWindow.close());


        VBox layout= new VBox(10);
        HBox buttonLayout = new HBox(100);
        buttonLayout.getChildren().addAll(confirmBt, cancelBt);
        buttonLayout.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(label, buttonLayout);

        layout.setAlignment(Pos.CENTER);

        Scene scene1= new Scene(layout, 300, 250);

        confirmationWindow.setScene(scene1);

        confirmationWindow.showAndWait();
    }

    private CategoryView createCategoryView(String categoryName, Account account){
        CategoryView categoryView = new CategoryView(categoryName, true);
        categoryView.getXButton().setId(account.getUserName());
        categoryView.getXButton().setOnAction(this::allowCategory);
        return categoryView;
    }

    private Button createForbidButton(String accountUserName, TextField forbidTextField){
        Button forbidBt = new Button();
        forbidBt.setText("+");
        forbidBt.setId(accountUserName);
        forbidBt.setOnAction(e -> forbidTextField.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED,"","",
                KeyCode.ENTER,false,false,false,false)));
        return forbidBt;
    }

    private TextField createForbidTextField(String accountUserName){
        TextField forbidTextField = new TextField();
        forbidTextField.setId(accountUserName);
        forbidTextField.setOnKeyPressed(this::categoryEnteredOrEscaped);
        return forbidTextField;
    }


    private void categoryEnteredOrEscaped(KeyEvent keyEvent){
        TextField source = ((TextField) keyEvent.getSource());
        ObservableList<Node> userCategories = ((FlowPane) source.getParent()).getChildren();
        int toAddAt = userCategories.indexOf(source);
        if (keyEvent.getCode().equals(KeyCode.ESCAPE)){
            displayMessage(userCategories,"");
            source.clear();
        }
        if (keyEvent.getCode().equals(KeyCode.ENTER)){
            String enteredText = source.getText();
            if (enteredText.length() != 0){
                String username =(source).getId();
                Account toForbidOn = AccountManagement.getAccount(username);
                if (toForbidOn.forbid(enteredText)){
                    AccountManagement.saveAccount(toForbidOn);
                    userCategories.add(toAddAt,createCategoryView(enteredText,toForbidOn));
                    displayMessage(userCategories,"");
                    CategoriesDB.addCategory(enteredText);
                    source.clear();
                }else{
                    displayMessage(userCategories,"category already added");
                }
            }else{
                displayMessage(userCategories,"\"\" is not a category");
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
        String username = ((Button) actionEvent.getSource()).getId();
        Stage stage = new Stage();
        FileChooser myFileChooser = new FileChooser();
        FileChooser.ExtensionFilter filters = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        myFileChooser.getExtensionFilters().add(filters);
        myFileChooser.setTitle("Sélectionner une photo de profil");
        File file = myFileChooser.showOpenDialog(stage);
        if (file != null) {
            String myPath = file.getPath();
            Account accountToModify = AccountManagement.getAccount(username);
            accountToModify.setImagePath(myPath);
            AccountManagement.saveAccount(accountToModify);
            setupAllAccounts();
        }
    }

    @FXML
    public void getBackToMenu(ActionEvent actionEvent) throws IOException {
        CVueVideotheque.changeToMe(toAddOn,this);
        saveNewUsernamesAndPasswords();
    }

}
