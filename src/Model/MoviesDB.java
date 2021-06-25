package Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MoviesDB {
    static String relativePath = File.separator + "Data" + File.separator +"moviesDB.json";
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

    public static boolean addMovie(String title, String path, String imagePath, ArrayList<String> categories) {
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
                movie.put("imagePath", imagePath);
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

    public static boolean deleteMovie(String title) {
        File f = new File(accountFile);
        createFile();
        try {
            String data = fileToString();
            JSONObject obj = new JSONObject(data);
            JSONArray movies = (JSONArray) obj.get("movies");
            JSONObject movie;
            boolean contain = false;
            /**Véirfier si contient le  film**/
            for(int i = 0; i < movies.length() ; i++) {
                movie = movies.getJSONObject(i);
                if (movie.get("title").equals(title)){
                    movies.remove(i);
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

    public static boolean addCategoryToMovie(String movieTitle, String category) throws IOException, JSONException {
        String data = fileToString();
        JSONObject obj = new JSONObject(data);
        JSONArray movies = (JSONArray) obj.get("movies");
        JSONObject movie;
        boolean contain = false;

        for(int i = 0; i < movies.length() ; i++) {
            movie = movies.getJSONObject(i);
            if (movie.get("title").equals(movieTitle)){
                JSONArray categories = (JSONArray) movie.getJSONArray("categories");
                System.out.print(categories.length());
                for(int j = 0; j < categories.length(); j++){
                    String nameCat = categories.getString(j);
                    if(nameCat.contentEquals(category))
                        return false;
                }
                categories.put(category);
                System.out.print(categories.length());
                Files.write(Paths.get(accountFile), obj.toString().getBytes());
                return true;
            }
        }
        return false;
    }

    public static boolean removeCategoryToMovie(String movieTitle, String category) throws IOException, JSONException {
        String data = fileToString();
        JSONObject obj = new JSONObject(data);
        JSONArray movies = (JSONArray) obj.get("movies");
        JSONObject movie;
        boolean contain = false;

        for(int i = 0; i < movies.length() ; i++) {
            movie = movies.getJSONObject(i);
            if (movie.get("title").equals(movieTitle)){
                JSONArray categories = (JSONArray) movie.getJSONArray("categories");
                System.out.print(categories.length());
                for(int j = 0; j < categories.length(); j++){
                    String nameCat = categories.getString(j);
                    if(nameCat.contentEquals(category)){
                        categories.remove(j);
                        Files.write(Paths.get(accountFile), obj.toString().getBytes());
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
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

    public static ArrayList<String> getTitles() {
        File f = new File(accountFile);
        String data = null;
        try {
            data = fileToString();
            JSONObject obj = new JSONObject(data);
            JSONArray jsonArray = (JSONArray) obj.get("movies");
            ArrayList<String> array = new ArrayList<String>(jsonArray.length());
            for(int i = 0; i < jsonArray.length() ; i++){
                array.add(jsonArray.getJSONObject(i).getString("title"));
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

    public static String getImagePath(String title){
        try {
            File f = new File(accountFile);
            String data = fileToString();
            JSONObject obj = new JSONObject(data);
            JSONArray movies = (JSONArray) obj.get("movies");
            for (int i = 0; i < movies.length(); i++) {
                JSONObject movie = movies.getJSONObject(i);
                if (movie.getString("title").equals(title)) {
                    String path = movie.getString("imagePath");
                    URL url = new URL(path);
                    File file = new File(url.getPath());
                    if(file.exists()) {
                        return movie.getString("imagePath");
                    }else{
                        setImagePath(title, "file:res/default.png");
                        return "file:res/default.png";
                    }

                }
            }
        }catch (JSONException | IOException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Le film : " + title + " n'existe pas");
    }

    public static ArrayList<String> getMovieCategories(String title){
        try {
            File f = new File(accountFile);
            String data = fileToString();
            JSONObject obj = new JSONObject(data);
            JSONArray movies = (JSONArray) obj.get("movies");
            for (int i = 0; i < movies.length(); i++) {
                JSONObject movie = movies.getJSONObject(i);
                if (movie.getString("title").equals(title)){
                    JSONArray Jcategories = movie.getJSONArray("categories");
                    ArrayList<String> categories = new ArrayList<String>(Jcategories.length());
                    for(int n = 0; n < Jcategories.length(); n++)
                        categories.add(Jcategories.getString(n));
                    return categories;
                }
            }
        }catch (FileNotFoundException | JSONException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Le film : " + title + " n'existe pas");
    }

    /*public static boolean setTitle(String acutalTitle, String newTitle) throws IOException {
        String data = fileToString();
        JSONObject obj = new JSONObject(data);
        JSONArray movies = (JSONArray) obj.get("movies");
        JSONObject movie;

        if(getTitles().contains(newTitle))
            return false;

        for(int i = 0; i < movies.length() ; i++) {
            movie = movies.getJSONObject(i);
            if (movie.get("title").equals(acutalTitle)){
                movie.put("title", newTitle);
                Files.write(Paths.get(accountFile), obj.toString().getBytes());
                return true;
            }
        }
        return false;
    }

    public static boolean setCategory(String movieTitle, ArrayList<String> categories) throws IOException {
        String data = fileToString();
        JSONObject obj = new JSONObject(data);
        JSONArray movies = (JSONArray) obj.get("movies");
        JSONObject movie;

        for(int i = 0; i < movies.length() ; i++) {
            movie = movies.getJSONObject(i);
            if (movie.get("title").equals(movieTitle)){
                JSONArray Jcategories = (JSONArray) movie.getJSONArray("categories");
                Jcategories.clear();
                for(String s : categories){
                    Jcategories.put(s);
                }
                Files.write(Paths.get(accountFile), obj.toString().getBytes());
                return true;
            }
        }
        return false;
    }

    public static boolean setPath(String movieTitle, String path) throws IOException {
        String data = fileToString();
        JSONObject obj = new JSONObject(data);
        JSONArray movies = (JSONArray) obj.get("movies");
        JSONObject movie;

        for(int i = 0; i < movies.length() ; i++) {
            movie = movies.getJSONObject(i);
            if (movie.get("title").equals(movieTitle)){
                movie.put("path", path);
                Files.write(Paths.get(accountFile), obj.toString().getBytes());
                return true;
            }
        }
        return false;
    }*/

    public static boolean setImagePath(String movieTitle, String path) throws IOException {
        String data = fileToString();
        JSONObject obj = new JSONObject(data);
        JSONArray movies = (JSONArray) obj.get("movies");
        JSONObject movie;

        for(int i = 0; i < movies.length() ; i++) {
            movie = movies.getJSONObject(i);
            if (movie.get("title").equals(movieTitle)){
                movie.put("imagePath", path);
                Files.write(Paths.get(accountFile), obj.toString().getBytes());
                return true;
            }
        }
        return false;
    }

    /*public static boolean set(String actualTitle, String newTitle, String path, String imagePath, ArrayList<String> categories) throws IOException {
        String data = fileToString();
        JSONObject obj = new JSONObject(data);
        JSONArray movies = (JSONArray) obj.get("movies");
        JSONObject movie;

        for(int i = 0; i < movies.length() ; i++) {
            movie = movies.getJSONObject(i);
            if (movie.get("title").equals(actualTitle)){
                JSONArray Jcategories = (JSONArray) movie.getJSONArray("categories");
                Jcategories.clear();
                for(String s : categories){
                    Jcategories.put(s);
                }
                movie.put("path", path);
                movie.put("imagePath", imagePath);
                movie.put("title", newTitle);
                Files.write(Paths.get(accountFile), obj.toString().getBytes());
                return true;
            }
        }
        return false;
    }*/
    public static boolean set(String actualTitle, String newTitle, String path, String imagePath, ArrayList<String> categories) throws IOException {
        if(actualTitle.equals(newTitle)){
            MoviesDB.deleteMovie(actualTitle);
            return MoviesDB.addMovie(newTitle, path, imagePath, categories);
        }else{
            boolean test = MoviesDB.addMovie(newTitle, path, imagePath, categories);
            if(test)
                MoviesDB.deleteMovie(actualTitle);
            return test;
        }
    }

    public static boolean isOfCategorie(String movie, String categorie){
        List<String> categories = getMovieCategories(movie);
        if(categories.contains(categorie))
            return true;
        return false;
    }

    public static ArrayList<String> getAuthorizedMovies(String username){
        Account user = AccountManagement.getAccount(username);
        ArrayList<String> forbiddenCategories = user.getForbiddenCategories();
        ArrayList<String> movies = MoviesDB.getTitles();
        Set<String> forbiddenMovies = new LinkedHashSet<>();
        for(String category : forbiddenCategories)
            forbiddenMovies.addAll(CategoriesDB.getMoviesOfCategory(category));
        movies.removeAll(forbiddenMovies);
        return movies;
    }

    public static void main(String[] args) throws IOException {
        createFile();
        ArrayList<String> cat = new ArrayList<>();

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

        System.out.print("Ducobu is of categorie famille : " + isOfCategorie("Ducobu", "famille") + "\n");

        System.out.print("Thriller movies : \n");
        for(String s : CategoriesDB.getMoviesOfCategory("thriller"))
            System.out.print(s+"\n");

        System.out.print("Child authorized movies : \n");
        for(String s : getAuthorizedMovies("child"))
            System.out.print(s+"\n");

        /*setCategory("Tenet2", (new ArrayList<String>(List.of("Humour", "fantastique"))));
        System.out.print("Tenet2 categories : " + "\n");
        for(String s :getMovieCategories("Tenet2"))
            System.out.print(s + "\n");

        setTitle("Tenet2", "Tenet3");
        System.out.println("Contains Tenet2 : " + getTitles().contains("Tenet2"));
        System.out.println("Contains Tenet3 : " + getTitles().contains("Tenet3"));
        setTitle("Tenet3", "Tenet2");

        setPath("Tenet2", "C://videos/Tenet2.mp4");
        System.out.println("path Tenet2 : " + getMoviePath("Tenet2"));
        System.out.println("nombre de film : " + getTitles().size());

        addMovie("newMovie", "null", "file:res/default", new ArrayList<>());
        for(String s :getTitles())
            setImagePath(s,"file:res/default.png");*/

    }
}
