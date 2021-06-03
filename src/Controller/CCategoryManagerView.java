package Controller;

import Model.CategoriesDB;
import Model.MoviesDB;
import View.AutoCompleteTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.HBox;
import observerObservable.Observer;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class CCategoryManagerView implements Initializable {
    @FXML
    public ChoiceBox choiceBox;
    public HBox hboxAddMovie;
    public ListView listView;


    public AutoCompleteTextField autoCompleteTextField = new AutoCompleteTextField() ;

    private String actualCatgory = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<String>  allCategories = CategoriesDB.getCategories();
        for(String s : allCategories)
            choiceBox.getItems().add(s);

        hboxAddMovie.getChildren().add(1,autoCompleteTextField);
        autoCompleteTextField.getEntries().addAll(MoviesDB.getTitles());
    }


    public void selectCategory(ActionEvent actionEvent) {
        actualCatgory = (String) choiceBox.getSelectionModel().getSelectedItem();
        for(String s : CategoriesDB.getMoviesOfCategory(actualCatgory))
            System.out.print(s + "\n");
        listView.getItems().setAll(CategoriesDB.getMoviesOfCategory(actualCatgory));

        //System.out.print(actualCatgory + "\n");
    }

    public void addMovie(ActionEvent actionEvent) throws IOException, JSONException {
        String movie = autoCompleteTextField.getText();
        boolean isAdd = MoviesDB.addCategoryToMovie(movie, actualCatgory);
        if(isAdd)
            listView.getItems().add(movie);
    }
}
