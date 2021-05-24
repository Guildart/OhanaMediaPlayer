package Model;

import javafx.scene.image.Image;
import org.json.JSONObject;

public class Account {
    private String userName = "";
    private String passWord = "";
    private String imagePath = "";


    Account (JSONObject jsonObject){
        userName = jsonObject.getString("user name");
        passWord = jsonObject.getString("password");
        imagePath = jsonObject.getString("image path");
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
}
