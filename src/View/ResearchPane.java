package View;

import Model.MoviesDB;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;

public class ResearchPane extends FlowPane {
    public String research;

    public ResearchPane(String research){
        this.research = research;
    }

    public void setDisplay(){
        ArrayList<String> allFilm = MoviesDB.getTitles();
        ArrayList<String> searchFilm = new ArrayList<>();
    }

    public void setDisplayByCategory(String category){

    }

}
