package animation;

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        MapView mapView = new MapView(root);
        MapController mapController = new MapController(primaryStage, mapView);

        mapController.startApplication(); // This sets up the stage and shows it
    }

    public static void main(String[] args) {
        launch(args);
    }
}
