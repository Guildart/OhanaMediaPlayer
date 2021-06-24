package Controller;

import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CFirstLaunch implements Initializable {
    public Button imageButton;
    public TextField textField;
    public PasswordField passField;
    private String imgPath = "file:res/user.png";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void chooseProfilePic(ActionEvent actionEvent) {
        Stage stage = new Stage();
        FileChooser myFileChooser = new FileChooser();
        FileChooser.ExtensionFilter filters = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        myFileChooser.getExtensionFilters().add(filters);
        myFileChooser.setTitle("Sélectionner une photo de profil");
        File file = myFileChooser.showOpenDialog(stage);
        if (file != null) {
            imgPath = file.toURI().toString();
        }
    }

    public void valider(ActionEvent actionEvent) {
        String username = textField.getText();
        String password = passField.getText();
        if(username != null && username != "" && password != "" && password!=null){
            AccountManagement.createFile();
            MoviesDB.createFile();
            CategoriesDB.createFile();
            Account  account = new Account(username, password, imgPath, Role.admin, new ArrayList<>());
            AccountManagement.saveAccount(account);
            account = new Account("child", "", "file:res/childrenPP.png", Role.child, new ArrayList<>());
            AccountManagement.saveAccount(account);

            Stage stage = (Stage) imageButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/VueConnexion.fxml"));
            stage.setMaximized(true);
            //stage.setMinWidth(900);
            Parent root = null;
            try {
                root = loader.load();
                Scene scene = new Scene(root, stage.getWidth(), stage.getScene().getHeight());
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
