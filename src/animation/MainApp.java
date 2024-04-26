package animation;

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * This class is the main entry point for a JavaFX application that visualizes a map.
 * It sets up the primary stage and initializes the map view and controller components.
 */
public class MainApp extends Application {

    /**
     * Starts and sets up the main JavaFX application window.
     * This method initializes the primary UI components and displays the stage.
     *
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be primary stages.
     */
    @Override
    public void start(Stage primaryStage) {
        // Create the main pane that will hold the UI components.
        Pane root = new Pane();

        // Initialize the map view with the root pane.
        MapView mapView = new MapView(root);

        // Create the map controller, which will handle the logic of the map operations.
        MapController mapController = new MapController(primaryStage, mapView);

        // Start the map application, setting up the stage and showing it to the user.
        mapController.startApplication();
    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * @param args the command line arguments passed to the application.
     *             An application may get these parameters using the getParameters() method.
     */
    public static void main(String[] args) {
        // Launch the JavaFX application.
        launch(args);
    }
}
