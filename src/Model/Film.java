package Model;

import observerObservable.Observable;

import java.util.ArrayList;

public class Film  extends Observable {
    private ArrayList<String> myCategory;
    private String name;
    private String path;

    public Film(String name, String path, ArrayList<String> Category){
        this.name = name;
        this.path = path;
        this.myCategory = Category;
    }

    public void addCategory(String category){
        if(this.myCategory == null){
            this.myCategory = new ArrayList<String>();
        }
        this.myCategory.add(category);
    }
}
