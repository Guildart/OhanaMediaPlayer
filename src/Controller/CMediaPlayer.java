package Controller;

import Model.MoviesDB;
import View.Player;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
    public Button pauseButton;
    public Slider timeSlider;
    public Slider volumeSlider;
    public MediaView mediaView;
    public MediaPlayer mediaPlayer;
    public static String videoPath = "C:\\Users\\Guild\\Downloads\\test.mp4";
    public String file = new File(videoPath).toURI().toString();
    public AnchorPane root;

    public static void playVideo(Node anySceneNode, Object controller, String movieName) throws IOException {
        videoPath = MoviesDB.getMoviePath(movieName);
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
        Media media = new Media(file);
        mediaPlayer = new MediaPlayer(media);
        //mediaView = new MediaView(mediaPlayer);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaView.fitHeightProperty().bind(root.heightProperty());
        mediaView.fitWidthProperty().bind(root.widthProperty());
        timeSlider.prefWidthProperty().bind(root.widthProperty().multiply(0.75));

        mediaPlayer.currentTimeProperty().addListener(ov -> updatesValues());
        timeSlider.valueProperty().addListener(ov -> timeJump());
        volumeSlider.valueProperty().addListener(ov -> changeVolume());


        mediaPlayer.setAutoPlay(true);
    }

    public void pause(ActionEvent actionEvent) {
        MediaPlayer.Status status = mediaPlayer.getStatus(); // To get the status of Player
        if (status == status.PLAYING) {

            // If the status is Video playing
            if (mediaPlayer.getCurrentTime().greaterThanOrEqualTo(mediaPlayer.getTotalDuration())) {

                // If the player is at the end of video
                mediaPlayer.seek(mediaPlayer.getStartTime()); // Restart the video
                mediaPlayer.play();
            }
            else {
                // Pausing the player
                mediaPlayer.pause();

                pauseButton.setText(">");
            }
        } // If the video is stopped, halted or paused
        if (status == MediaPlayer.Status.HALTED || status == MediaPlayer.Status.STOPPED || status == MediaPlayer.Status.PAUSED) {
            mediaPlayer.play(); // Start the video
            pauseButton.setText("||");
        }
    }

    protected void updatesValues()
    {
        Platform.runLater(new Runnable() {
            public void run()
            {
                // Updating to the new time value
                // This will move the slider while running your video
                timeSlider.setValue(mediaPlayer.getCurrentTime().toMillis()/mediaPlayer.getTotalDuration().toMillis() * 100);
            }
        });
    }

    protected void timeJump(){
        if (timeSlider.isPressed()) { // It would set the time
            // as specified by user by pressing
            mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(timeSlider.getValue() / 100));
        }
    }

    protected void changeVolume(){
        if (volumeSlider.isPressed()) {
            mediaPlayer.setVolume(volumeSlider.getValue() / 100); // It would set the volume
            // as specified by user by pressing
        }
    }

    public void goBack(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/VueVideotheque.fxml"));
        stage.setMaximized(true);
        Parent root = null;
        try {
            root = loader.load();
            Scene scene = new Scene(root, stage.getWidth(), stage.getScene().getHeight());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
