/*
package animation;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CarSimulation extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 800, 600);

        // Load the car image
        Image carImage = new Image("file:resources/car.png"); // Adjust path as necessary
        ImageView carView = new ImageView(carImage);

        // Set initial position of the car
        carView.setX(-carImage.getWidth());
        carView.setY(300); // Middle of the scene's height

        // Add the car to the scene
        root.getChildren().add(carView);

        // Create a timeline animation to move the car across the screen
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(carView.xProperty(), -carImage.getWidth())),
                new KeyFrame(Duration.seconds(10), new KeyValue(carView.xProperty(), scene.getWidth()))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        primaryStage.setTitle("Car Driving Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

*/
