package Controller;

import Model.MoviesDB;
import View.WarningTrigger;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

//Todo : Full Screen Button + Sound Button + Hide Button + Afficher temps de video

public class CMediaPlayer implements Initializable {
    public Button pauseButton;
    public Slider timeSlider;
    public Slider volumeSlider;
    public MediaView mediaView;
    public MediaPlayer mediaPlayer;
    public static String videoPath = "C:\\Users\\Guild\\Downloads\\test.mp4";
    public String file = new File(videoPath).toURI().toString();
    public AnchorPane root;
    public HBox controlBar;
    public ToggleButton fullScreenButton;
    public Button goBackButton;
    public Label timeLabel;
    public Label volumeLabel;
    public ToggleButton soundToggle;
    public Button stopButton;
    private double lastAction = 0;

    public void playVideo(Node anySceneNode, Object controller, String movieName) throws IOException {
        videoPath = MoviesDB.getMoviePath(movieName);

        Stage stage = (Stage) anySceneNode.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(controller.getClass().getResource("/View/MediaPlayer.fxml"));
        if(!isPayable(videoPath)){
            boolean test = WarningTrigger.warningWindow("Le chemin d'accès du film " + movieName + " est incorrect ! \n " +
                    "Voulez vous que le film soit supprimé de la vidéothèque ?");
            if(test)
                MoviesDB.deleteMovie(movieName);
            loader = new FXMLLoader(controller.getClass().getResource("/View/VueVideotheque.fxml"));
        }
        Parent root = loader.load();
        //stage.setMaximized(true);
        Scene scene = new Scene(root, anySceneNode.getScene().getWidth(), anySceneNode.getScene().getHeight());
        stage.setScene(scene);
        //stage.setFullScreen(true);
        stage.show();
    }

    private boolean isPayable(String path) throws MalformedURLException {
        //URL url = new URL(path);
        File file = new File(path);
        if(path != null && path.length() >= 4) {
            String extension = path.substring(path.length() - 4);
            return file.exists() && extension.equals(".mp4");
        }
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Media media = new Media(file);
        mediaPlayer = new MediaPlayer(media);
        //mediaView = new MediaView(mediaPlayer);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaView.fitHeightProperty().bind(root.heightProperty());
        mediaView.fitWidthProperty().bind(root.widthProperty());
        timeSlider.prefWidthProperty().bind(root.widthProperty().multiply(0.70));
        volumeSlider.setValue(mediaPlayer.getVolume()*100);

        volumeLabel.setText((int) volumeSlider.getValue() + "%");

        Image img = new Image("file:res/not_full_screen.png");
        ImageView view = new ImageView(img);
        view.setFitHeight(20);
        view.setPreserveRatio(true);
        fullScreenButton.setGraphic(view);
        fullScreenButton.selectedProperty().addListener(ov -> setFullScreenIcon());

        /*Stage stage = (Stage) fullScreenButton.getScene().getWindow();
        stage.fullScreenProperty().addListener(ov -> fullScreenButton.fire());*/

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
                //mediaPlayer.play();
            }
            else {
                // Pausing the player
                mediaPlayer.pause();

                //pauseButton.setText(">");
            }
            pauseButton.setStyle("-fx-background-image:url(file:res/play.png);");
        } // If the video is stopped, halted or paused
        if (status == MediaPlayer.Status.HALTED || status == MediaPlayer.Status.STOPPED || status == MediaPlayer.Status.PAUSED) {
            mediaPlayer.play(); // Start the video
            pauseButton.setStyle("-fx-background-image:url(file:res/pause.png);");
            //pauseButton.setText("||");
        }
    }

    public void stop(ActionEvent actionEvent) {
        MediaPlayer.Status status = mediaPlayer.getStatus(); // To get the status of Player
        if(status == status.PLAYING)
            pauseButton.fire();
        mediaPlayer.stop();
    }

    private String getTime(Duration duration){
        int heure = (int) duration.toHours();
        int minute = (int) duration.toMinutes() % 60;
        int second = (int) duration.toSeconds()%60;

        if(heure > 0)
            return heure + ":" + minute + ":" + second;
        else if(minute > 0)
            if(minute > 9)
                return minute + ":" + second;
            else
                return "0" + minute + ":" + second;
        else
            if(second > 9)
                return "00:" + second;
            else
                return "00:0" + second;

    }

    protected void updatesValues()
    {
        Platform.runLater(new Runnable() {
            public void run()
            {
                // Updating to the new time value
                // This will move the slider while running your video
                timeSlider.setValue(mediaPlayer.getCurrentTime().toMillis()/mediaPlayer.getTotalDuration().toMillis() * 100);
                timeLabel.setText(getTime(mediaPlayer.getCurrentTime()) + "/" + getTime(mediaPlayer.getTotalDuration()));
                if(controlBar.isVisible() && mediaPlayer.getCurrentTime().toSeconds() - lastAction > 10){
                    controlBar.setVisible(false);
                    goBackButton.setVisible(false);
                }
            }
        });


    }

    protected void timeJump(){
        if (timeSlider.isPressed()) { // It would set the time
            // as specified by user by pressing
            mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(timeSlider.getValue() / 100));
            lastAction = mediaPlayer.getCurrentTime().toSeconds();
        }
    }

    protected void changeVolume(){
        if (volumeSlider.isPressed()) {
            mediaPlayer.setVolume(volumeSlider.getValue() / 100); // It would set the volume
            volumeLabel.setText((int) volumeSlider.getValue() + "%");
            if(soundToggle.isSelected())
                soundToggle.fire();
            // as specified by user by pressing
        }
    }

    public void goBack(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        mediaPlayer.stop();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/VueVideotheque.fxml"));
        //stage.setMaximized(true);
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

    public void onMouseMoved(MouseEvent mouseEvent) {
        controlBar.setVisible(true);
        goBackButton.setVisible(true);
        lastAction = mediaPlayer.getCurrentTime().toSeconds();
    }

    private void setFullScreenIcon() {
        if(fullScreenButton.isSelected()) {
            Image img = new Image("file:res/full_screen.png");
            ImageView view = new ImageView(img);
            view.setFitHeight(20);
            view.setPreserveRatio(true);
            fullScreenButton.setGraphic(view);
            Stage stage = (Stage) fullScreenButton.getScene().getWindow();
            stage.setFullScreen(true);
        }else{
            Image img = new Image("file:res/not_full_screen.png");
            ImageView view = new ImageView(img);
            view.setFitHeight(20);
            view.setPreserveRatio(true);
            fullScreenButton.setGraphic(view);
            Stage stage = (Stage) fullScreenButton.getScene().getWindow();
            stage.setFullScreen(false);
        }
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode()==KeyCode.ESCAPE && fullScreenButton.isSelected())
            fullScreenButton.setSelected(false);
        if(keyEvent.getCode()==KeyCode.F && keyEvent.isControlDown())
            fullScreenButton.fire();
        if(keyEvent.getCode()==KeyCode.A && keyEvent.isControlDown())
            stopButton.fire();
        if(keyEvent.getCode()==KeyCode.Z && keyEvent.isControlDown())
            goBackButton.fire();
        if(keyEvent.getCode() == KeyCode.SPACE)
            pauseButton.fire();
        if(keyEvent.getCode() == KeyCode.RIGHT)
            System.out.println("test");
            //mediaPlayer.seek(mediaPlayer.getTotalDuration().add(Duration.seconds(5)));
        if(keyEvent.getCode() == KeyCode.LEFT)
            mediaPlayer.seek(mediaPlayer.getTotalDuration().subtract(Duration.seconds(5)));;
    }

    public void mute(ActionEvent actionEvent) {
        if(soundToggle.isSelected()){
            mediaPlayer.setVolume(0);
            soundToggle.setStyle("-fx-background-image:url(file:res/mute.png);");
        }else{
            mediaPlayer.setVolume(volumeSlider.getValue() / 100);
            soundToggle.setStyle("-fx-background-image:url(file:res/sound.png);");
        }
    }
}
