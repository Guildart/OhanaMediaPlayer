package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.*;

public class AccountManagement {
    static String relativePath = File.separator + "src" + File.separator + "Data" + File.separator +"accountDB.json";
    private static String accountFile = System.getProperty("user.dir") + relativePath;

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


    public static ArrayList<Account> getAccounts(){
        try {
            ArrayList<Account> accounts = new ArrayList<Account>(0);
            String data = fileToString();
            JSONObject obj = new JSONObject(data);
            JSONArray users = obj.getJSONArray("users");

            for (int i = 0; i < users.length(); i++) {
                accounts.add(new Account(((JSONObject) users.getJSONObject(i))));
            }
            return accounts;
        }catch (FileNotFoundException | JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getUserNamePassword(String userName){
        for (Account account :getAccounts()
             ) {
            if (account.getUserName().equals(userName)){
                return account.getPassword();
            }
        }
        return "";
    }


    public static String getImgPath(String userName){
        for (Account account :getAccounts()) {
            if (account.getUserName().equals(userName)){
                return account.getImagePath();
            }
        }
        return "";
    }

    public static Account getAccount(String userName){
        for (Account account :getAccounts()
        ) {
            if (account.getUserName().equals(userName)){
                return account;
            }
        }
        System.out.println("account not found");
        return null;
    }



}
