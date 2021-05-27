package Model;

import javafx.scene.image.Image;
import org.json.JSONException;
import org.json.JSONObject;

public class Account {
    private String userName = "";
    private String passWord = "";
    private String imagePath = "";
    private Role role = Role.other;

    Account (JSONObject jsonObject) throws JSONException {
        userName = jsonObject.getString("user name");
        passWord = jsonObject.getString("password");
        imagePath = jsonObject.getString("image path");
        role = Role.valueOf(jsonObject.getString("role"));
    }

    Account (String userName, String passWord, String imagePath){ // l'admin ne peut ajouter que des comptes other
        this.userName = userName;
        this.passWord = passWord;
        this.imagePath = imagePath;
        this.role = Role.other;
    }

    public String getPassword() {
        return passWord;
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
}
