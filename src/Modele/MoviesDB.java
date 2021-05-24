package Modele;

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
import java.util.Scanner;
import java.util.Vector;

public class MoviesDB {
    static String relativePath = File.separator + "src" + File.separator + "Data" + File.separator +"moviesDB.json";
    private static String accountFile = System.getProperty("user.dir") + relativePath;

    public static boolean createFile(){
        File f = new File(accountFile);
        try {
            boolean b = f.createNewFile();
            if(b){
                JSONObject obj = new JSONObject();
                JSONArray movies = new JSONArray();
                obj.put("movies", movies);
                System.out.print(obj);
                Files.write(Paths.get(accountFile), obj.toString().getBytes());
            }
            return b;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addMovie(String title, String path, ArrayList<String> categories) {
        File f = new File(accountFile);
        createFile();
        try {
            String data = fileToString();
            JSONObject obj = new JSONObject(data);
            JSONArray movies = (JSONArray) obj.get("movies");
            boolean contain = false;
            JSONObject movie;
            /**Véirfier si contient le  film**/
            for(int i = 0; i < movies.length() ; i++) {
                movie = movies.getJSONObject(i);
                if (movie.get("title").equals(title))
                    contain = true;
            }
            if(!contain){
                movie = new JSONObject();
                movie.put("title", title);
                movie.put("path", path);
                JSONArray Jcategories = new JSONArray();
                for (String cat : categories)
                    Jcategories.put(cat);
                movie.put("categories", Jcategories);
                movies.put(movie);
                Files.write(Paths.get(accountFile), obj.toString().getBytes());
            }
            return !contain;
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
            //System.out.print(data);
        }

        return data;
    }

    public static String[] getTitles() {
        File f = new File(accountFile);
        String data = null;
        try {
            data = fileToString();
            JSONObject obj = new JSONObject(data);
            JSONArray jsonArray = (JSONArray) obj.get("movies");
            String array [] = new String[jsonArray.length()];
            for(int i = 0; i < jsonArray.length() ; i++){
                array[i] = jsonArray.getJSONObject(i).getString("title");
            }
            return array;
        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMoviePath(String title){
        try {
            File f = new File(accountFile);
            String data = fileToString();
            JSONObject obj = new JSONObject(data);
            JSONArray movies = (JSONArray) obj.get("movies");
            for (int i = 0; i < movies.length(); i++) {
                JSONObject movie = movies.getJSONObject(i);
                if (movie.getString("title").equals(title))
                    return movie.getString("path");
            }
        }catch (FileNotFoundException | JSONException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Le film : " + title + " n'existe pas");
    }


    public static String[] getMovieCategories(String title){
        try {
            File f = new File(accountFile);
            String data = fileToString();
            JSONObject obj = new JSONObject(data);
            JSONArray movies = (JSONArray) obj.get("movies");
            for (int i = 0; i < movies.length(); i++) {
                JSONObject movie = movies.getJSONObject(i);
                if (movie.getString("title").equals(title)){
                    JSONArray Jcategories = movie.getJSONArray("categories");
                    String categories[] = new String[Jcategories.length()];
                    for(int n = 0; n < Jcategories.length(); n++)
                        categories[n] = Jcategories.getString(n);
                    return categories;
                }
            }
        }catch (FileNotFoundException | JSONException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Le film : " + title + " n'existe pas");
    }

    public static void main(String[] args) {
        createFile();
        ArrayList<String> cat = new ArrayList<>();
        addMovie("Tenet", "C://videos/Tenet.mp4",
                new ArrayList<String>(Arrays.asList(new String[]{"action", "thriller", "voyage dans le temps"})));
        addMovie("Tenet", "C://videos/Tenet.mp4",
                new ArrayList<String>(Arrays.asList(new String[]{"action", "thriller", "voyage dans le temps"})));
        addMovie("Ducobu", "C://videos/Tenet.mp4",
                new ArrayList<String>(Arrays.asList(new String[]{"Humour", "Famille"})));

        for(String s :getTitles())
            System.out.print(s + "\n");

        System.out.print("Tenet categories : " + "\n");
        for(String s :getMovieCategories("Tenet"))
            System.out.print(s + "\n");

        System.out.print("Ducobu categories : " + "\n");
        for(String s :getMovieCategories("Ducobu"))
            System.out.print(s + "\n");

        System.out.print("Tenet path : " + getMoviePath("Tenet") + "\n");
        System.out.print("Ducobu path : " + getMoviePath("Ducobu") + "\n");
    }

}
