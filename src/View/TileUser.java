package View;

import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class TileUser extends VBox implements Initializable {

    public Label label = new Label();
    public ImageView imageView = new ImageView();

    public TileUser(String username, String imgAbsPath){
        this.setAlignment(Pos.CENTER);
        FileInputStream inputstream = null;

        try {
            inputstream = new FileInputStream(imgAbsPath);
            System.out.print(inputstream);
            Image image = new Image(inputstream);
            this.imageView.setImage(image);
            this.imageView.setFitWidth(200);
            this.imageView.setFitHeight(200);
            this.label.setText(username);
            this.getChildren().add(imageView);
            this.getChildren().add(label);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void OnMouseCliked() {
        System.out.print("false");
    }


    public void OnMouseCliked(MouseEvent mouseEvent) {
        //Todo : Quand on clique on ouvre un pop up pour rentrer le mdp
        System.out.print("true");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
