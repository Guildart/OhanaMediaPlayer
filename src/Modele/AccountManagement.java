package Modele;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.*;

public class AccountManagement {
    static String relativePath = File.separator + "src" + File.separator + "Data" + File.separator +"accountDB.json";
    private static String accountFile = System.getProperty("user.dir") + relativePath;

    public static boolean createFile(){
        File f = new File(accountFile);
        try {
            boolean b = f.createNewFile();
            if(b){
                JSONObject obj = new JSONObject();
                JSONArray pseudo = new JSONArray();
                JSONArray pass = new JSONArray();
                obj.put("password", pass);
                obj.put("username", pseudo);
                Files.write(Paths.get(accountFile), obj.toString().getBytes());
            }
            return b;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void addAccount(String username, String password) {
        File f = new File(accountFile);
        createFile();
        try {
            String data = fileToString();
            JSONObject obj = new JSONObject(data);
            JSONArray pseudo = (JSONArray) obj.get("username");
            JSONArray pass = (JSONArray) obj.get("password");
            boolean contain = false;
            for(int i = 0; i < pseudo.length() ; i++)
                if(pseudo.getString(i).equals(username))
                    contain = true;
            if(!contain){
                pass.put(password);
                pseudo.put(username);
                Files.write(Paths.get(accountFile), obj.toString().getBytes());
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
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

    public static String[] JsonArrayToArray(String arg) throws FileNotFoundException, JSONException {
        File f = new File(accountFile);
        String data = fileToString();
        JSONObject obj = new JSONObject(data);
        JSONArray jsonArray = (JSONArray) obj.get(arg);
        String array [] = new String[jsonArray.length()];
        for(int i = 0; i < jsonArray.length() ; i++){
            array[i] = jsonArray.getString(i);
        }
        return array;
    }

    public static String[] getUsernames() {
        try {
            return JsonArrayToArray("username");
        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPassword(String username){
        try {
            File f = new File(accountFile);
            String data = fileToString();
            JSONObject obj = new JSONObject(data);
            JSONArray pseudo = (JSONArray) obj.get("username");
            JSONArray pass = (JSONArray) obj.get("password");
            for (int i = 0; i < pseudo.length(); i++)
                if (pseudo.getString(i).equals(username))
                    return pass.getString(i);
        }catch (FileNotFoundException | JSONException e){
            e.printStackTrace();
        }
        throw new RuntimeException("L'utilisateur : " + username + " n'existe pas");
    }
}
