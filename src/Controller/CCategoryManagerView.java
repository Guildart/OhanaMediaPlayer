package Controller;

import Model.Account;
import Model.AccountManagement;
import Model.CategoriesDB;
import Model.MoviesDB;
import View.AutoCompleteTextField;
import View.CategoryView;
import View.MultipleChoiceBox;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import observerObservable.Observer;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.util.*;

public class CCategoryManagerView implements Initializable {
    @FXML
    public ComboBox choiceBox;
    public HBox hboxAddMovie;


    public TextField nameCategory;
    public Label message;
    public HBox hboxAddCategory;
    public TextField newCategoryName;
    public Label labelCatAlreadyExist;
    public FlowPane flowPaneMovies;
    public FlowPane flowPaneUsers;
    public HBox notAllowedUsers;
    public HBox hboxUsers;
    public HBox hboxCatName;

    private String actualCatgory = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<String>  allCategories = CategoriesDB.getCategories();
        for(String s : allCategories)
            choiceBox.getItems().add(s);
        message.setVisible(false);
        labelCatAlreadyExist.setVisible(false);

        message.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            focusMessageState(newValue, message);
        });

        labelCatAlreadyExist.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            focusMessageState(newValue, labelCatAlreadyExist);
        });

        hboxCatName.setVisible(false);
        hboxAddMovie.setVisible(false);
        hboxUsers.setVisible(false);


    }

    private void focusMessageState(Boolean value, Node node) {
        if(value){
            node.setVisible(true);
        }else{
            node.setVisible(false);
        }
    }

    private void focusTextFieldState(Boolean value, Node node) {
        if(value){
            node.setVisible(true);
        }else{
            hboxAddCategory.getChildren().remove(node);
        }
    }

    public void supClicked(ActionEvent e) {
        try {
            HBox currentHBox = (HBox)((Button)e.getSource()).getParent();
            String movie = ((Button)currentHBox.getChildren().get(0)).getText();
            System.out.print(movie);
            MoviesDB.removeCategoryToMovie(movie, actualCatgory);
            this.flowPaneMovies.getChildren().remove(currentHBox);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        //this.categoryAdded.remove(((Button)currentHBox.getChildren().get(0)).getText());
    }


    public void selectCategory(ActionEvent actionEvent) {
        actualCatgory = (String) choiceBox.getSelectionModel().getSelectedItem();
        flowPaneMovies.getChildren().clear();
        for(String s : CategoriesDB.getMoviesOfCategory(actualCatgory)){
                CategoryView MovieToAdd = new CategoryView(s, true);
                MovieToAdd.getXButton().setOnAction(a -> supClicked(a));
                flowPaneMovies.getChildren().add(MovieToAdd);
        }
        //listView.getItems().setAll(CategoriesDB.getMoviesOfCategory(actualCatgory));
        nameCategory.setText(actualCatgory);
        updateUsersAllowed();
        hboxCatName.setVisible(true);
        hboxAddMovie.setVisible(true);
        hboxUsers.setVisible(true);

        //System.out.print(actualCatgory + "\n");
    }



    public void addMovie(ActionEvent actionEvent) throws IOException, JSONException {
        ArrayList<String> moviesInCategory = CategoriesDB.getMoviesOfCategory(actualCatgory);
        ArrayList<String> moviesSelected = MultipleChoiceBox.displayFilm("Choisissez vos films", moviesInCategory);
        ArrayList<String> moviesAdded = new ArrayList<>();

        for(String s : moviesSelected){
            if(!moviesInCategory.contains(s)){
                MoviesDB.addCategoryToMovie(s, actualCatgory);
                moviesAdded.add(s);
            }
            moviesInCategory.remove(s);
        }

        for(String s : moviesInCategory)
            MoviesDB.removeCategoryToMovie(s, actualCatgory);

        flowPaneMovies.getChildren().clear();
        for(String movie : moviesSelected){
            CategoryView MovieToAdd = new CategoryView(movie, true);
            MovieToAdd.getXButton().setOnAction(a -> supClicked(a));
            flowPaneMovies.getChildren().add(MovieToAdd);
        }
    }

    public void renameCategory(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            String newName = nameCategory.getText();
            boolean added = CategoriesDB.changeCategoryName(actualCatgory, newName);
            if(added){
                actualCatgory = newName;
                choiceBox.getItems().setAll(CategoriesDB.getCategories());
                choiceBox.getSelectionModel().select(actualCatgory);
            }else{
                nameCategory.setText(actualCatgory);
                message.setVisible(true);
                message.requestFocus();
            }
        }
    }

    public void deleteCategory(ActionEvent actionEvent) {
        //Todo : Trogger "êtes vous certain" à implémenter
        System.out.print(CategoriesDB.deleteCategory(actualCatgory));
        choiceBox.getSelectionModel().clearSelection();
        choiceBox.getItems().clear();
        choiceBox.getItems().setAll(CategoriesDB.getCategories());
    }

    public void addCategory(ActionEvent actionEvent) {
        newCategoryName = new TextField();
        newCategoryName.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            focusTextFieldState(newValue, newCategoryName);
        });
        hboxAddCategory.getChildren().add(3,newCategoryName);
        newCategoryName.requestFocus();
        newCategoryName.setOnKeyPressed(this::enterNewCategoryName);

    }

    public void enterNewCategoryName(KeyEvent keyEvent){
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            String newCat = newCategoryName.getText();
            boolean added = CategoriesDB.addCategory(newCat);
            hboxAddCategory.getChildren().remove(newCategoryName);
            if(!added){
                labelCatAlreadyExist.setVisible(true);
                labelCatAlreadyExist.requestFocus();
            }else{
                choiceBox.getItems().add(newCat);
                choiceBox.getSelectionModel().select(newCat);
            }
        }
    }
    private static boolean hasCommonElements(ArrayList<String> arr1, ArrayList<String> arr2) {
        if (arr1.size() > 0 && arr2.size() > 0) {
            Set<String> firstSet = new HashSet<String>();
            for (int i = 0; i < arr1.size(); i++) {
                firstSet.add(arr1.get(i));
            }

            for (int j = 0; j < arr2.size(); j++) {
                if (firstSet.contains(arr2.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void updateUsersAllowed(){
        notAllowedUsers.getChildren().clear();
        HashMap<String, Account> accounts = AccountManagement.getAccounts();
        for (String key : accounts.keySet()) {
            Account curAccount = accounts.get(key);
            if (curAccount.getForbiddenCategories().contains(actualCatgory)){
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

    public void goBack(ActionEvent actionEvent) {
        Stage stage = (Stage) choiceBox.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/VueVideotheque.fxml"));
        stage.setMaximized(true);
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
