package Model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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

            for (Object user : users
                 ) {
                accounts.add(new Account(((JSONObject) user)));
            }
            return accounts;
        }catch (java.io.FileNotFoundException e){
            e.printStackTrace();
            return null;
        }

    }
}
