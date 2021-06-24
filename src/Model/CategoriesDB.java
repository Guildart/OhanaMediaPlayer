package Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CategoriesDB {
    static String relativePath = File.separator + "Data" + File.separator +"categoriesDB.json";
    public static String accountFile = System.getProperty("user.dir") + relativePath;

    public static boolean createFile(){
        File f = new File(accountFile);
        try {
            boolean b = f.createNewFile();
            if(b){
                JSONObject obj = new JSONObject();
                JSONArray categories = new JSONArray();
                obj.put("categories", categories);
                System.out.print(obj);
                Files.write(Paths.get(accountFile), obj.toString().getBytes());
            }
            return b;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addCategory(String title) {
        File f = new File(accountFile);
        createFile();
        try {
            String data = fileToString();
            JSONObject obj = new JSONObject(data);
            JSONArray categories = (JSONArray) obj.get("categories");
            boolean contain = false;
            /**Véirfier si contient le  film**/
            for(int i = 0; i < categories.length() ; i++) {
                if (categories.getString(i).equals(title))
                    contain = true;
            }
            if(!contain){
                categories.put(title);
                Files.write(Paths.get(accountFile), obj.toString().getBytes());
            }
            return !contain;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean deleteCategory(String title) {
        File f = new File(accountFile);
        createFile();
        try {
            String data = fileToString();
            JSONObject obj = new JSONObject(data);
            JSONArray categories = (JSONArray) obj.get("categories");
            boolean contain = false;
            /**Véirfier si contient le  film**/
            for(int i = 0; i < categories.length() ; i++) {
                if (categories.getString(i).equals(title)) {
                    //On supprime la catégorie de tous les films
                    for(String s : getMoviesOfCategory(title))
                        MoviesDB.removeCategoryToMovie(s, title);
                    categories.remove(i);
                    Files.write(Paths.get(accountFile), obj.toString().getBytes());
                    return true;
                }
            }
            return false;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean changeCategoryName(String oldName, String newName){
        File f = new File(accountFile);
        createFile();
        try {
            if(getCategories().contains(newName))
                return false;
            String data = fileToString();
            JSONObject obj = new JSONObject(data);
            JSONArray categories = (JSONArray) obj.get("categories");
            /**Véirfier si contient le  film**/
            for(int i = 0; i < categories.length() ; i++) {
                if (categories.getString(i).equals(oldName)) {
                    //On supprime la catégorie de tous les films
                    for(String s : getMoviesOfCategory(oldName)) {
                        MoviesDB.removeCategoryToMovie(s, oldName);
                        MoviesDB.addCategoryToMovie(s, newName);
                    }
                    categories.remove(i);
                    categories.put(newName);
                    Files.write(Paths.get(accountFile), obj.toString().getBytes());
                    return true;
                }
            }
            return false;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String fileToString() throws FileNotFoundException {
        String data = "";
        File f = new File(accountFile);
        Scanner reader = new Scanner(f);
        while (reader.hasNextLine()) {
            data += reader.nextLine() + "\n";
        }

        return data;
    }

    public static ArrayList<String>  getCategories() {
        File f = new File(accountFile);
        String data = null;
        try {
            data = fileToString();
            JSONObject obj = new JSONObject(data);
            JSONArray jsonArray = (JSONArray) obj.get("categories");
            ArrayList<String> array = new ArrayList<String>(jsonArray.length());
            for(int i = 0; i < jsonArray.length() ; i++){
                array.add(jsonArray.getString(i));
            }
            return array;
        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> getMoviesOfCategory(String categorie) {
        List<String> categories = getCategories();
        ArrayList<String> moviesOfCategory = new ArrayList<>();
        if (categories.contains(categorie)){
            ArrayList<String>  movieTitles  = MoviesDB.getTitles();
           for(String title : movieTitles){
               if (MoviesDB.isOfCategorie(title, categorie)){
                   moviesOfCategory.add(title);
               }
           }
        }
        return moviesOfCategory;
    }

    public static ArrayList<String> getMoviesWithoutCategory(){
        List<String> categories = getCategories();
        ArrayList<String> moviesOfCategory = new ArrayList<>();
        ArrayList<String>  movieTitles  = MoviesDB.getTitles();
        for(String title : movieTitles){
            if (MoviesDB.getMovieCategories(title).isEmpty()){
                moviesOfCategory.add(title);
            }
        }
        return moviesOfCategory;
    }

    public static void main(String[] args) {
        createFile();
        addCategory("action");
        addCategory("aventure");
        addCategory("voyage dans le temps");
        addCategory("thriller");
        addCategory("humour");
        addCategory("famille");
        addCategory("fantastique");
        addCategory("course");
        addCategory("fantastique");


        System.out.print("Afficher toutes catégories : \n");
        for(String s : getCategories())
            System.out.print(s+"\n");

        System.out.print("Action movies : \n");
        for(String s : getMoviesOfCategory("action"))
            System.out.print(s+"\n");

        System.out.print("Course movies : \n");
        for(String s : getMoviesOfCategory("course"))
            System.out.print(s+"\n");

        deleteCategory("aventure");

        System.out.print("Afficher toutes catégories : \n");
        for(String s : getCategories())
            System.out.print(s+"\n");

        addCategory("aventure");

    }

}
