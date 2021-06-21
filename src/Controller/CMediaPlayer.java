package Controller;

import Model.MoviesDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CMediaPlayer implements Initializable {
    MediaView moviePlayer;
    @FXML
    public AnchorPane toAddMovieOn;
    static Media media;
    public static void changeToMe(Node anySceneNode, Object controller, String movieName) throws IOException {
        String videoPath = MoviesDB.getMoviePath(movieName).substring(3);
        String videoFileURIStr = new File(videoPath).toURI().toString();
        media = new Media(videoFileURIStr);

        Stage stage = (Stage) anySceneNode.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(controller.getClass().getResource("/View/MediaPlayer.fxml"));
        Parent root = loader.load();
        stage.setMaximized(true);
        Scene scene = new Scene(root, anySceneNode.getScene().getWidth(), anySceneNode.getScene().getHeight());
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        moviePlayer = new MediaView(mediaPlayer);
        mediaPlayer.setAutoPlay(true);
        toAddMovieOn.getChildren().add(moviePlayer);

    }

}
