package javatpoint;

import java.io.File;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javafx.scene.Group;
import javafx.scene.Scene;

public class VideoPlayerExample1 extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Example video player");

		String videoPath = "C:\\Users\\Guild\\Downloads\\test.mp4";
		String videoFileURIStr = new File(videoPath).toURI().toString();
		System.out.println(videoFileURIStr);
		//Instantiating Media class
		final Media media = new Media(videoFileURIStr);
		//Media media = new Media("");
		
		//Instantiating MediaPlayer class
		final MediaPlayer mediaPlayer = new MediaPlayer(media);
		
		//Instantiating MediaView class
		final MediaView mediaView = new MediaView(mediaPlayer);

		//by setting this property to true, the Video will be played
		mediaPlayer.setAutoPlay(true);
		
		//Setting group and scene
		int w = media.getWidth();
		int h = media.getHeight();

		Group root = new Group();
		root.getChildren().add(mediaView);
		System.out.print(h);
		Scene scene = new Scene(root,h,w);
		
		primaryStage.setScene(scene);

		mediaView.fitHeightProperty().bind(primaryStage.heightProperty());
		mediaView.fitWidthProperty().bind(primaryStage.widthProperty());
		
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
}
