package Controller;

import Model.Account;
import Model.AccountManagement;
import Model.CategoriesDB;
import Model.MoviesDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class CFilmCreationView implements Initializable{
    @FXML
    public Button choiceButton;
    public TextField pathText;
    public TextField nameText;
    public SplitMenuButton splitMenuCategory;
    public HBox categoryList;
    public HBox notAllowedUsers;
    public Label pathErrorMessage;
    public Label titleErrorMessage;
    public Label errorToAddMessage;

    public ArrayList<String> categoryAdded = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String category[] = CategoriesDB.getCategories();
        for(int i = 0; i < category.length; i++){
            MenuItem cate = new MenuItem(category[i]);
            cate.setOnAction(e -> categoryClicked(e));
            this.splitMenuCategory.getItems().add(cate);
        }
        this.pathErrorMessage.setVisible(false);
        this.titleErrorMessage.setVisible(false);
        this.errorToAddMessage.setVisible(false);
    }

    public void categoryClicked(ActionEvent e){
        HBox cateNSupButton = new HBox();
        Button supButton = new Button("x");
        supButton.setOnAction(a -> supClicked(a));
        String categoryName = ((MenuItem)e.getSource()).getText();
        Button newCategory = new Button(categoryName);
        cateNSupButton.getChildren().addAll(newCategory, supButton);
        this.categoryList.getChildren().add(cateNSupButton);
        this.categoryAdded.add(categoryName);
        updateUsersAllowed();
    }

    public void supClicked(ActionEvent e){
        HBox currentHBox = (HBox)((Button)e.getSource()).getParent();
        this.categoryAdded.remove(((Button)currentHBox.getChildren().get(0)).getText());
        this.categoryList.getChildren().remove(currentHBox);
    }



    public void choiceAction(ActionEvent e){
        Stage stage = new Stage();
        FileChooser myFileChooser = new FileChooser();
        FileChooser.ExtensionFilter filters = new FileChooser.ExtensionFilter("MP4 files (*.mp4)", "*.mp4");
        myFileChooser.getExtensionFilters().add(filters);
        myFileChooser.setTitle("selectioner un film");
        File file = myFileChooser.showOpenDialog(stage);
        if (file != null) {
            String myPath = file.getPath();
            this.pathText.setText(myPath);
        }
    }

    public void validate(ActionEvent e) throws IOException {
        this.pathErrorMessage.setVisible(false);
        this.titleErrorMessage.setVisible(false);
        this.errorToAddMessage.setVisible(false);
        if(this.pathText.getText() != "" &&
           this.nameText.getText() != ""){
            /**if(!MoviesDB.addMovie(nameText.getText(), pathText.getText(), this.categoryAdded)){
                this.errorToAddMessage.setVisible(true);
            }**/
            //CVueVideotheque.imgPath = currentImgPath; //Donner chemin image pour test rapide
            System.out.println("titre : " + this.nameText.getText());
            System.out.println("chemain d'acces : " + this.pathText.getText());
            System.out.println("liste des categorie \n" + this.categoryAdded);

            Stage stage = (Stage) categoryList.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/VueVideotheque.fxml"));
            Parent root = loader.load();
            stage.setMaximized(true);
            Scene scene = new Scene(root, categoryList.getScene().getWidth(), categoryList.getScene().getHeight());
            stage.setScene(scene);
            stage.show();
        }
        else {
            if(this.pathText.getText() == ""){
                this.pathErrorMessage.setVisible(true);
            }
            if(this.nameText.getText() == ""){
                this.titleErrorMessage.setVisible(true);
            }
        }
    }

    private static boolean hasCommonElements(ArrayList<String> arr1,
                                           ArrayList<String> arr2)
    {

        // Check if length of arr1 is greater than 0
        // and length of arr2 is greater than 0
        if (arr1.size() > 0 && arr2.size() > 0) {
            Set<String> firstSet = new HashSet<String>(); //bizarre de créer un hashset mais en fait c'est + rapide
            for (int i = 0; i < arr1.size(); i++) {
                firstSet.add(arr1.get(i));
            }

            // Iterate the elements of the arr2
            for (int j = 0; j < arr2.size(); j++) {
                if (firstSet.contains(arr2.get(j))) {//ça fait surement une recherche sur un arbre binaire là
                    return true;
                }
            }
        }
        return false;
    }

    public void updateUsersAllowed(){
        notAllowedUsers.getChildren().clear();
        for (Account curAccount : AccountManagement.getAccounts()) {
            if (hasCommonElements(curAccount.getForbiddenCategories(), categoryAdded)){
                VBox accountRep = new VBox();
                ImageView accImgVw = new ImageView(curAccount.getImage());
                accImgVw.setFitWidth(64);
                accImgVw.setFitHeight(64);
                accountRep.setAlignment(Pos.CENTER);
                accountRep.getChildren().addAll(accImgVw, new Label(curAccount.getUserName() ) );
                notAllowedUsers.getChildren().add(accountRep);
            }
        }
    }


}
