package Model;

import javafx.scene.image.Image;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Account {
    private String userName;
    private String password;
    private String imagePath;
    private Role role = Role.other;
    private ArrayList<String> forbiddenCategories = new ArrayList<String>(0);

    Account (String userName, JSONObject jsonObject) throws JSONException {
        this.userName = userName;
        password = jsonObject.getString("password");
        imagePath = jsonObject.getString("image path");
        role = Role.valueOf(jsonObject.getString("role"));
        JSONArray forbiddenJSONCat = jsonObject.getJSONArray("forbidden");
        for (int catNum= 0; catNum < forbiddenJSONCat.length(); catNum++) {
            forbiddenCategories.add(forbiddenJSONCat.getString(catNum));
        }
    }

    Account (String userName, String password, String imagePath, ArrayList<String> forbiddenCategories){ // l'admin ne peut ajouter que des comptes other
        this.userName = userName;
        this.password = password;
        this.imagePath = imagePath;
        this.role = Role.other;
        this.forbiddenCategories = forbiddenCategories;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Image getImage(){
        return new Image("file:"+imagePath);
    }

    public Role getRole(){
        return role;
    }

    public ArrayList<String> getForbiddenCategories(){
        return forbiddenCategories;
    }

    public void allow(String category){
        forbiddenCategories.remove(category);
    }

    public JSONObject toJson(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", userName);
        jsonObject.put("password", password);
        jsonObject.put("image path", imagePath);
        jsonObject.put("role", role.toString());
        JSONArray forbiddenJSONCat = new JSONArray();
        for (String category: forbiddenCategories) {
            forbiddenJSONCat.put(category);
        }
        jsonObject.put("forbidden", forbiddenJSONCat);
        return jsonObject;
    }
}
