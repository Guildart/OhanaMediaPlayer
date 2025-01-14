package Controller;

import Model.Account;
import Model.AccountManagement;
import Model.CategoriesDB;
import Model.MoviesDB;
import View.CategoryView;
import View.ImageCropWithRubberBand;
import View.MultipleChoiceBox;
import View.WarningTrigger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class CFilmCreationView implements Initializable{
    @FXML
    public Button choiceButton;
    public TextField pathText;
    public TextField nameText;
    public HBox categoryList;
    public HBox notAllowedUsers;
    public Label pathErrorMessage;
    public Label titleErrorMessage;
    public Label errorToAddMessage;
    public Label modificationLabel;

    public ArrayList<String> categoryAdded = new ArrayList<>();
    public Boolean modifying = false;
    public String actualMovie;
    public String oldTitle = "";
    public Button deleteButton;
    public HBox buttonBox;
    public Button modifButton;
    public Button imageButton;
    public Button goBackButton;

    private String movieImagePath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.pathErrorMessage.setVisible(false);
        this.titleErrorMessage.setVisible(false);
        this.errorToAddMessage.setVisible(false);
        this.modificationLabel.setVisible(false);
        buttonBox.getChildren().remove(deleteButton);
        movieImagePath = "file:res/default.png";
    }


    public void supClicked(ActionEvent e){
        HBox currentHBox = (HBox)((Button)e.getSource()).getParent();
        this.categoryAdded.remove(((Button)currentHBox.getChildren().get(0)).getText());
        this.categoryList.getChildren().remove(currentHBox);
        updateUsersAllowed();
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
        String path = this.pathText.getText();
        String name = this.nameText.getText();
        if( name != null && path!= null && !name.isEmpty() && !path.isEmpty()){
            if(!this.modifying){
                if(!MoviesDB.addMovie(nameText.getText(), pathText.getText(), movieImagePath, this.categoryAdded))
                    this.errorToAddMessage.setVisible(true);

            }else{
                if(!MoviesDB.set(this.oldTitle, this.nameText.getText(), this.pathText.getText(),movieImagePath, this.categoryAdded))
                    this.errorToAddMessage.setVisible(true);
            }
            System.out.println("old titre : " + this.oldTitle);
            System.out.println(this.nameText.getText().isEmpty());
            System.out.println("titre : " + this.nameText.getText());
            System.out.println("chemain d'acces : " + this.pathText.getText());
            System.out.println("chemain image: " + this.movieImagePath);
            System.out.println("liste des categorie \n" + this.categoryAdded);
            System.out.println("je modifie : " + this.modifying);

            Stage stage = (Stage) categoryList.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/VueVideotheque.fxml"));
            Parent root = loader.load();
            //stage.setMaximized(true);
            Scene scene = new Scene(root, categoryList.getScene().getWidth(), categoryList.getScene().getHeight());
            stage.setScene(scene);
            stage.show();
        }
        else {
            if(path.isEmpty() || path == null){
                this.pathErrorMessage.setVisible(true);
            }
            if(name.isEmpty() || name == null){
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
        HashMap<String,Account> accounts = AccountManagement.getAccounts();
        for (String key : accounts.keySet()) {
            Account curAccount = accounts.get(key);
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

    public void cancelClicked(ActionEvent e) throws IOException {
        Stage stage = (Stage) categoryList.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/VueVideotheque.fxml"));
        Parent root = loader.load();
        //stage.setMaximized(true);
        Scene scene = new Scene(root, categoryList.getScene().getWidth(), categoryList.getScene().getHeight());
        stage.setScene(scene);
        stage.show();
    }

    public void addMultipleCategory(ActionEvent e){
        ArrayList<String> categorySelected = MultipleChoiceBox.displayCategory("Choisissez vos catégories", this.categoryAdded);
        this.categoryAdded.clear();
        this.categoryList.getChildren().clear();
        for(String category : categorySelected){
            if(!this.categoryAdded.contains(category)){
                CategoryView categoryToAdd = new CategoryView(category, true);
                categoryToAdd.getXButton().setOnAction(a -> supClicked(a));
                this.categoryList.getChildren().add(categoryToAdd);
                this.categoryAdded.add(category);
                updateUsersAllowed();
            }
        }
    }

    public void modifClicked(ActionEvent e){
        this.modifying = !modifying;
        if(this.modifying){
            this.oldTitle = MultipleChoiceBox.displayOneFilm("choisissez le film a modifier");
            if(this.oldTitle != "" && this.oldTitle != null) {

                this.modificationLabel.setText("Vous ètes en train de modifiez " + this.oldTitle);
                this.movieImagePath = MoviesDB.getImagePath(this.oldTitle);
                imageButton.setStyle("-fx-background-image:url(" + movieImagePath +");");
                this.modificationLabel.setVisible(true);

                this.nameText.setText(this.oldTitle);
                this.pathText.setText(MoviesDB.getMoviePath(this.oldTitle));


                this.categoryAdded = MoviesDB.getMovieCategories(this.oldTitle);
                this.categoryList.getChildren().clear();
                for (String category : categoryAdded) {
                    CategoryView categoryToAdd = new CategoryView(category, true);
                    categoryToAdd.getXButton().setOnAction(a -> supClicked(a));
                    this.categoryList.getChildren().add(categoryToAdd);
                    updateUsersAllowed();
                }
                ((Button) e.getSource()).setText("Ne plus modifier");
                buttonBox.getChildren().add(deleteButton);
            }
            else{
                modifying = false;
                buttonBox.getChildren().remove(deleteButton);
            }
        }
        else{
            ((Button)e.getSource()).setText("Modifier");
            this.modificationLabel.setVisible(false);
            this.nameText.setText("");
            this.pathText.setText("");
            this.categoryList.getChildren().clear();
            this.categoryAdded = new ArrayList<>();
            updateUsersAllowed();
        }
    }


    public void deleteMovie(ActionEvent actionEvent) {
        boolean test = false;
        if(modifying)
            test = WarningTrigger.warningWindow("Êtes vous sûr de vouloir supprimer le film : " + oldTitle + " ?");
        if(test){
            MoviesDB.deleteMovie(oldTitle);
            modifButton.fire();
        }
    }

    public void addImage(ActionEvent actionEvent) throws MalformedURLException {
        ImageCropWithRubberBand imgCrop = new ImageCropWithRubberBand();
        imgCrop.cropApplication();
        if(imgCrop.getImagePath() != null){
            movieImagePath = imgCrop.getImagePath();
            System.out.println("movie : " + movieImagePath);
            imageButton.setStyle("-fx-background-image:url(" + movieImagePath +");");
        }
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode()==KeyCode.Z && keyEvent.isControlDown())
            goBackButton.fire();
    }
}
