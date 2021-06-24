package Model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javafx.event.ActionEvent;
import org.json.*;

public class AccountManagement {
    static String relativePath = File.separator + "Data" + File.separator +"accountDB.json";
    private static String accountFile = System.getProperty("user.dir") + relativePath;

    public static boolean createDataFile(){
        File f = new File(System.getProperty("user.dir") + File.separator + "Data");
        System.out.println(f.toURI().toString());
        if(!f.exists()){
            f.mkdir();
            return true;
        }
        return false;
    }

    public static boolean createFile(){
        createDataFile();
        File f = new File(accountFile);
        try {
            boolean b = f.createNewFile();
            if(b){
                JSONObject obj = new JSONObject();
                Files.write(Paths.get(accountFile), obj.toString().getBytes());
            }
            return b;

        } catch (IOException | JSONException e) {
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


    public static HashMap<String, Account> getAccounts(){
        try {
            HashMap<String,Account> accounts = new HashMap<>(0);
            String data = fileToString();
            JSONObject obj = new JSONObject(data);

            for (String key:
                 obj.keySet()) {
                accounts.put(key, new Account(key,((JSONObject) obj.getJSONObject(key))));
            }
            return accounts;
        }catch (FileNotFoundException | JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getUserNamePassword(String userName){
        return getAccounts().get(userName).getPassword();
    }


    public static String getImgPath(String userName){
        return getAccounts().get(userName).getImagePath();
    }

    public static Account getAccount(String userName){
        return getAccounts().get(userName);
    }

    public static void deleteAccount(String accountName){
        HashMap<String,Account> accounts = getAccounts();
        if (accounts.get(accountName).getRole() != Role.other){
            System.out.println("can't delete a "+accounts.get(accountName).getRole().toString() +
                    " role (must be an other role)");
            return;
        }
        accounts.remove(accountName);
        try {
            saveAccounts(accounts);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("couldn't delete account" + accountName);
        }
    }

    public static void saveAccount(Account toSave){
        HashMap<String,Account> accounts = getAccounts();
        accounts.put(toSave.getUserName(), toSave);
        try {
            saveAccounts(accounts);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("couldn't save account :" + toSave.getUserName());
        }
    }

    public static void saveAccounts(HashMap<String,Account> toSave) throws IOException {
        JSONObject accountJSON = new JSONObject();
        for (String key: toSave.keySet()) {
            accountJSON.put(key, toSave.get(key).toJson());
        }
        Files.write(Paths.get(accountFile), accountJSON.toString().getBytes());
    }



    public static void createAccount(ActionEvent actionEvent) {
        saveAccount(new Account("untitled","0000","file:res/user.png",Role.other,new ArrayList<>(0)));
    }
}
