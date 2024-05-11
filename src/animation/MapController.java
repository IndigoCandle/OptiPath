package animation;

import GA.FitnessEfficiencyCalculator;
import GA.FitnessTimeCalculator;
import GA.Interfaces.IFitnessCalculator;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import map.*;
import cars.Car;
import GA.GeneticAlgorithmPathFinder;
import GA.NoRoutesFoundException;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Controls the main operations of the map view, including interaction handling and graph processing.
 */
public class MapController {
    private final MapView mapView;
    private List<Vertex> vertices;
    private List<Edge> edges;
    private Vertex selectedVertex;
    private Vertex startVertex;
    private Vertex endVertex ;
    private final Stage primaryStage;
    private boolean efficientPath;
    private List<Double> recordFuel;

    /**
     * Constructor initializing the primary stage and map view components.
     *
     * @param primaryStage The primary stage for the application.
     * @param mapView The graphical view component for the map.
     */
    public MapController(Stage primaryStage, MapView mapView) {
        this.primaryStage = primaryStage;
        this.mapView = mapView;
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.selectedVertex = null;
        this.startVertex = null;
        this.endVertex = null;
        this.efficientPath = false;
        recordFuel = new ArrayList<>();
        showGraphChoiceDialog();
    }

    /**
     * Displays a choice dialog for creating or loading a graph.
     */
    private void showGraphChoiceDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Graph Selection");
        alert.setHeaderText("Choose how to create the graph:");
        alert.setContentText("Would you like to use a pre-made graph or create a new one?");

        ButtonType buttonTypePreMade = new ButtonType("Pre-made");
        ButtonType buttonTypeCreateNew = new ButtonType("Create New");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/animation/styling/museum-dialog.css")).toExternalForm());
        dialogPane.getStyleClass().add("myDialog");

        alert.getButtonTypes().setAll(buttonTypePreMade, buttonTypeCreateNew);

        Image preMadeImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/resources/car.png")));
        ImageView preMadeView = new ImageView(preMadeImage);
        preMadeView.setFitWidth(20);
        preMadeView.setFitHeight(20);
        ((Button) dialogPane.lookupButton(buttonTypePreMade)).setGraphic(preMadeView);

        Image createNewImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/resources/car.png")));
        ImageView createNewView = new ImageView(createNewImage);
        createNewView.setFitWidth(20);
        createNewView.setFitHeight(20);
        ((Button) dialogPane.lookupButton(buttonTypeCreateNew)).setGraphic(createNewView);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonTypePreMade) {
            loadPremadeGraph();
            attachEventHandlers();
        } else if (result.isPresent() && result.get() == buttonTypeCreateNew) {
            attachEventHandlers();
        }
    }

    /**
     * Prompts the user to choose the type of path optimization.
     */
    private void promptUserForPathType() {
        mapView.showPathChoiceDialog(isEfficient -> efficientPath = isEfficient);
    }

    /**
     * Loads a premade graph configuration from a factory method.
     */
    private void loadPremadeGraph() {
        graph cityGraph = CityGraphFactory.createSmallCityGraph();
        this.vertices = cityGraph.getVertices();
        this.edges = cityGraph.getEdges();

        for (Vertex vertex : this.vertices) {
            mapView.drawVertex(vertex, Color.BLUE);
        }

        for (Edge edge : this.edges) {
            mapView.drawEdge(edge);
        }
    }

    /**
     * Attaches handlers for UI actions like button clicks and mouse events.
     */
    private void attachEventHandlers() {
        mapView.getSolveButton().setOnAction(e -> {
            runGeneticAlgorithm();
            e.consume();
        });

        mapView.getResetButton().setOnAction(e -> {
            resetGraph();
            e.consume();
        });
        mapView.getShowFitnessGraphButton().setOnAction(e ->{
            ShowFitnessGraph();
            e.consume();
        });
        mapView.getRoot().setOnMouseClicked(this::handleGraphBuilding);
    }
    private void ShowFitnessGraph(){
        if(!recordFuel.isEmpty()){
            FuelConsumptionGraph graph = new FuelConsumptionGraph(recordFuel);
            graph.displayGraph();
        } else{
            mapView.showAlert("A path must be found first",
                    "Please run the genetic algorithm first.");
        }

    }
    /**
     * Handles the building of a graph based on mouse click events.
     *
     * @param event The mouse event triggering graph building.
     */
    private void handleGraphBuilding(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        Optional<Vertex> nearVertex = findVertexNear(x, y);

        if (nearVertex.isPresent()) {
            if (selectedVertex == null) {
                selectedVertex = nearVertex.get();
                if (startVertex == null) {
                    startVertex = selectedVertex;
                    mapView.drawVertex(startVertex, Color.GREEN);
                } else if (endVertex == null && !selectedVertex.equals(startVertex)) {
                    endVertex = selectedVertex;
                    mapView.drawVertex(endVertex, Color.RED);
                    selectedVertex = null;
                }
            } else if (!selectedVertex.equals(nearVertex.get())) {
                createEdge(selectedVertex, nearVertex.get());
                selectedVertex = null;
            }
        } else {
            Vertex newVertex = new Vertex(vertices.size() + 1, x, y);
            vertices.add(newVertex);
            mapView.drawVertex(newVertex, Color.BLUE);
            if (startVertex == null) {
                startVertex = newVertex;
                mapView.drawVertex(startVertex, Color.GREEN);
            }
        }
    }

    /**
     * Finds a vertex near a given screen coordinate.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return An optional vertex if one is near the coordinates.
     */
    private Optional<Vertex> findVertexNear(double x, double y) {
        return vertices.stream()
                .filter(v -> Math.sqrt(Math.pow(v.getX() - x, 2) + Math.pow(v.getY() - y, 2)) < 10)
                .findFirst();
    }

    /**
     * Creates an edge between two vertices and updates the view.
     *
     * @param from The source vertex.
     * @param to The destination vertex.
     */
    private void createEdge(Vertex from, Vertex to) {
        try{
            mapView.showCreateEdgeDialog(from, to, edge -> {
                edges.add(edge);
                from.addEdge(edge);
                to.addEdge(edge);
                mapView.drawEdge(edge);
            });
        }catch (RuntimeException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Selection Needed");
            alert.setHeaderText("Start and End Vertex Selection");
            alert.setContentText("Please select the start and end vertices before running the algorithm.");
            alert.showAndWait();

        }

    }

    /**
     * Runs the genetic algorithm to find an optimal path based on user preferences.
     */
    private void runGeneticAlgorithm() {
        if (startVertex == null || endVertex == null) {
            mapView.showAlert("Selection Needed",
                    "Please select the start and end vertices before running the algorithm.");
            return;
        }
        promptUserForPathType();
        IFitnessCalculator fitnessCalculator = efficientPath ?
                new FitnessEfficiencyCalculator() : new FitnessTimeCalculator();
        graph map = new graph(vertices);
        Car car = new Car("Car1", 60, 20, 100);
        GeneticAlgorithmPathFinder ga = new GeneticAlgorithmPathFinder(car, fitnessCalculator);

        try {
            List<Vertex> efficientPath = ga.findShortestPath(startVertex, endVertex, map);
            List<Edge> accidents = ga.getAccidents();
            for (Edge edge : accidents) {
                mapView.drawCrossOnAccidentEdge(edge);
            }
            recordFuel = ga.recordFuel;
            mapView.highlightPath(efficientPath, Color.GREEN);
        } catch (NoRoutesFoundException e) {
            System.out.println(e.getMessage());
            List<Edge> accidents = ga.getAccidents();
            for (Edge edge : accidents) {
                mapView.drawCrossOnAccidentEdge(edge);
            }
        }
    }

    /**
     * Resets the graph to a clean state.
     */
    private void resetGraph() {
        vertices.clear();
        edges.clear();
        selectedVertex = null;
        startVertex = null;
        endVertex = null;
        mapView.clear();
        attachEventHandlers();
    }

    /**
     * Initiates the application GUI.
     */
    public void startApplication() {
        primaryStage.setTitle("Graph Visualization");
        Scene scene = new Scene(mapView.getRoot(), 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
