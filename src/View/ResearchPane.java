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

        //supprimer les film n'ayant pas au moins les 3 premiere lettre en commun avec la recherche
        //ou moins si la recherche est plus petite
        ArrayList<String> allFilm = MoviesDB.getTitles();
        ArrayList<String> searchFilm = new ArrayList<>();
        for(int i = 0; i < (this.research.length()) && (i <= 3) ; i++){
            for(String film : allFilm){
                if(this.research.toLowerCase().charAt(i) != film.toLowerCase().charAt(i)){
                    allFilm.remove(film);
                }
            }
        }

        //ranger les film restant dans l'ordre de proximité avec la recherche
        while(allFilm.size() > 0){

            int maxCommonValue = 0;
            String toADD = allFilm.get(0);

            for(String film : allFilm){
                int check  = this.charInCommon(this.research, film);
                if(check > maxCommonValue){
                    maxCommonValue = check;
                    toADD = film;
                }
            }
            searchFilm.add(toADD);
            allFilm.remove(toADD);
        }

        //ajouter les films dans l'ordre de la liste au flowPane


    }

    public void setDisplayByCategory(String category){

    }

    public int charInCommon(String A, String B){
        int res  = 0;
        for(int i = 0; i < Math.min(A.length(), B.length()); i++){
            if(A.toLowerCase().charAt(i) == B.toLowerCase().charAt(i)){
                res += 1;
            }
        }
        return res;
    }

}
