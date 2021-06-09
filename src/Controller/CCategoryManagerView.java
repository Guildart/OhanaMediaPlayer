package Controller;

import Model.CategoriesDB;
import Model.MoviesDB;
import View.AutoCompleteTextField;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import observerObservable.Observer;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class CCategoryManagerView implements Initializable {
    @FXML
    public ChoiceBox choiceBox;
    public HBox hboxAddMovie;
    public ListView listView;


    public AutoCompleteTextField autoCompleteTextField = new AutoCompleteTextField() ;
    public TextField nameCategory;
    public Label message;
    public HBox hboxAddCategory;
    public TextField newCategoryName;
    public Label labelCatAlreadyExist;

    private String actualCatgory = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<String>  allCategories = CategoriesDB.getCategories();
        for(String s : allCategories)
            choiceBox.getItems().add(s);

        hboxAddMovie.getChildren().add(1,autoCompleteTextField);
        autoCompleteTextField.getEntries().addAll(MoviesDB.getTitles());
        message.setVisible(false);
        labelCatAlreadyExist.setVisible(false);

        message.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            focusMessageState(newValue, message);
        });

        labelCatAlreadyExist.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            focusMessageState(newValue, labelCatAlreadyExist);
        });

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


    public void selectCategory(ActionEvent actionEvent) {
        actualCatgory = (String) choiceBox.getSelectionModel().getSelectedItem();
        for(String s : CategoriesDB.getMoviesOfCategory(actualCatgory))
            System.out.print(s + "\n");
        listView.getItems().setAll(CategoriesDB.getMoviesOfCategory(actualCatgory));
        nameCategory.setText(actualCatgory);

        //System.out.print(actualCatgory + "\n");
    }

    public void addMovie(ActionEvent actionEvent) throws IOException, JSONException {
        String movie = autoCompleteTextField.getText();
        boolean isAdd = MoviesDB.addCategoryToMovie(movie, actualCatgory);
        if(isAdd)
            listView.getItems().add(movie);
    }

    public void renameCategory(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            String newName = nameCategory.getText();
            boolean added = CategoriesDB.addCategory(newName);
            if(added){
                CategoriesDB.deleteCategory(actualCatgory);
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
