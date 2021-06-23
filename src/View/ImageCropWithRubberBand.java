package View;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;


/**
 * Load image, provide rectangle for rubberband selection. Press right mouse button for "crop" context menu which then crops the image at the selection rectangle and saves it as jpg.
 */
public class ImageCropWithRubberBand {

    RubberBandSelection rubberBandSelection;
    ImageView imageView;

    public int finalWidth = 171;
    public int finalHeight = 96;
    public Rectangle rec = new Rectangle(0, 0, 171, 96);
    private Stage window;
    private boolean isRegistered;
    private String imgpath;
    private String finaleImage;
    public Image img;

    public ImageCropWithRubberBand(){
        isRegistered = false;
    }

    private String chooseFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("image", "*.png", "*.jpg"));
        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            String myPath = file.getAbsolutePath();
            //System.out.println(myPath);
            return myPath;
        }
        return null;
    }

    public boolean cropApplication() throws MalformedURLException {
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Image Crop");
        window.setMinWidth(250);

        BorderPane root = new BorderPane();

        // container for image layers
        ScrollPane scrollPane = new ScrollPane();

        // image layer: a group of images
        Group imageLayer = new Group();

        // load the image
        //Image image = new Image( getClass().getResource( "cat.jpg").toExternalForm());
        imgpath = chooseFile();
        if(imgpath == null)
            return false;
        System.out.println(imgpath);
        Image image = new Image(("file:" + imgpath));

        // the container for the image as a javafx node
        imageView = new ImageView(image);

        // add image to layer
        imageLayer.getChildren().add(imageView);

        // use scrollpane for image view in case the image is large
        scrollPane.setContent(imageLayer);

        // put scrollpane in scene
        root.setCenter(scrollPane);

        //calc rec size

        int fact = Math.min(((int) image.getWidth()/finalWidth), ((int) image.getHeight()/finalHeight));
        int width = finalWidth*fact;
        int height = finalHeight*fact;

        System.out.println(image.getWidth() + ":" + image.getHeight());

        rec = new Rectangle(0, 0, width, height);

        //Put rect in imageView
        imageLayer.getChildren().add(rec);

        // rubberband selection
        rubberBandSelection = new RubberBandSelection(imageLayer, rec);

        // create context menu and menu items
        ContextMenu contextMenu = new ContextMenu();

        MenuItem cropMenuItem = new MenuItem("Crop");
        cropMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                // get bounds for image crop
                Bounds selectionBounds = rubberBandSelection.getBounds();

                // show bounds info
                System.out.println("Selected area: " + selectionBounds);

                // crop the image
                crop(selectionBounds);

            }
        });
        contextMenu.getItems().add(cropMenuItem);

        // set context menu on image layer
        imageLayer.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isSecondaryButtonDown()) {
                    contextMenu.show(imageLayer, event.getScreenX(), event.getScreenY());
                }
            }
        });

        window.setScene(new Scene(root, 1024, 768));
        window.showAndWait();

        return isRegistered;

    }


    private void crop(Bounds bounds) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("image png(*.png)", "*.png"));

        File file = fileChooser.showSaveDialog(window);
        if (file == null)
            return;

        if(!file.getName().contains(".")) {
            file = new File(file.getAbsolutePath() + ".png");
        }

        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        parameters.setViewport(new Rectangle2D(bounds.getMinX(), bounds.getMinY(), width, height));

        WritableImage wi = new WritableImage(width, height);
        imageView.snapshot(parameters, wi);
        BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
        BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);
        Image image = SwingFXUtils.toFXImage(bufImageRGB, null);
        imageView = new ImageView(image);
        Graphics2D graphics = bufImageRGB.createGraphics();
        graphics.drawImage(bufImageARGB, 0, 0, null);
        try {

            ImageIO.write(bufImageRGB, "png", file);
            isRegistered = true;
            finaleImage = file.toURI().toString();
            System.out.println("Image saved to " + finaleImage);

        } catch (IOException e) {
            e.printStackTrace();
        }

        graphics.dispose();
        window.close();

    }

    public String getImagePath(){
        return this.finaleImage;
    }


    /**
     * Drag rectangle with mouse cursor in order to get selection bounds
     */
    public static class RubberBandSelection {

        final DragContext dragContext = new DragContext();
        Rectangle rect;
        Group group;


        public Bounds getBounds() {
            return rect.getBoundsInParent();
        }

        public RubberBandSelection(Group group, Rectangle rectangle) {

            this.group = group;

            rect = rectangle ;
            rect.setStroke(Color.BLUE);
            rect.setStrokeWidth(1);
            rect.setStrokeLineCap(StrokeLineCap.ROUND);
            rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));

            group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

        }

        EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                dragContext.mouseAnchorX = event.getX();
                dragContext.mouseAnchorY = event.getY();
            }
        };

        EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if (event.isSecondaryButtonDown())
                    return;

                double offsetX = event.getX() - dragContext.mouseAnchorX;
                double offsetY = event.getY() - dragContext.mouseAnchorY;
                dragContext.mouseAnchorX = event.getX();
                dragContext.mouseAnchorY = event.getY();

                double newPosX = rect.getX()+offsetX;
                double newPosY = rect.getY()+offsetY;

                double width = ((ImageView) group.getChildren().get(0)).getImage().getWidth();
                double height = ((ImageView) group.getChildren().get(0)).getImage().getHeight();

                System.out.println(newPosX + ":" + newPosY);
                if(newPosX > 0 && newPosX + rect.getWidth() < width){
                    rect.setX(newPosX);
                }
                if(newPosY > 0 && newPosY + rect.getHeight() < height ){
                    rect.setY(newPosY);
                }
            }
        };


        EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if (event.isSecondaryButtonDown())
                    return;

            }
        };

        private static final class DragContext {

            public double mouseAnchorX;
            public double mouseAnchorY;


        }
    }
}