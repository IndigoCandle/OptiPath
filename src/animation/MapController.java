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
import map.CityGraphFactory;
import map.Vertex;
import map.Edge;
import cars.Car;
import GA.GeneticAlgorithmPathFinder;
import GA.NoRoutesFoundException;
import javafx.stage.Stage;
import map.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MapController {
    private final MapView mapView;
    private List<Vertex> vertices = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();
    private Vertex selectedVertex = null;
    private Vertex startVertex = null;
    private Vertex endVertex = null;
    private final Stage primaryStage;
    private boolean efficientPath = false;

    public MapController(Stage primaryStage, MapView mapView) {
        this.primaryStage = primaryStage;
        this.mapView = mapView;
        showGraphChoiceDialog();
    }

    private void showGraphChoiceDialog() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Graph Selection");
        alert.setHeaderText("Choose how to create the graph:");
        alert.setContentText("Would you like to use a pre-made graph or create a new one?");


        ButtonType buttonTypePreMade = new ButtonType("Pre-made");
        ButtonType buttonTypeCreateNew = new ButtonType("Create New");


        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/animation/museum-dialog.css")).toExternalForm());
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




    private void promptUserForPathType() {
        mapView.showPathChoiceDialog(isEfficient -> efficientPath = isEfficient);
    }

    private void loadPremadeGraph() {
        // Get the premade graph
        graph cityGraph = CityGraphFactory.createSmallCityGraph();

        // Save the vertices and edges in the controller
        this.vertices = cityGraph.getVertices();
        this.edges = cityGraph.getEdges();

        // Draw the vertices
        for (Vertex vertex : this.vertices) {
            mapView.drawVertex(vertex, Color.BLUE); // Or choose another color
        }

        // Draw the edges
        for (Edge edge : this.edges) {
            mapView.drawEdge(edge);
        }

    }




    private void attachEventHandlers() {


        // Attach event handlers for solve and reset buttons
        mapView.getSolveButton().setOnAction(e -> {
            runGeneticAlgorithm();
            e.consume();
        });

        mapView.getResetButton().setOnAction(e -> {
            resetGraph();
            e.consume();
        });

        // Set the mouse click handler for the mapView
        mapView.getRoot().setOnMouseClicked(this::handleGraphBuilding);
    }





    private void handleGraphBuilding(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        Optional<Vertex> nearVertex = findVertexNear(x, y);

        // If a vertex is clicked
        if (nearVertex.isPresent()) {
            if (selectedVertex == null) {
                // First click: select the vertex
                selectedVertex = nearVertex.get();
                if (startVertex == null) {
                    startVertex = selectedVertex;
                    mapView.drawVertex(startVertex, Color.GREEN); // Start vertex is green
                } else if (endVertex == null && !selectedVertex.equals(startVertex)) {
                    endVertex = selectedVertex;
                    mapView.drawVertex(endVertex, Color.RED); // End vertex is red
                    selectedVertex = null; // Allow new selections for creating edges
                }
            } else if (!selectedVertex.equals(nearVertex.get())) {
                // Second click: create an edge between the selected vertex and this vertex
                createEdge(selectedVertex, nearVertex.get());
                selectedVertex = null; // Reset selection to allow for more edges
            }
        } else {
            // No vertex was clicked, create a new one
            Vertex newVertex = new Vertex(vertices.size() + 1, x, y);
            vertices.add(newVertex);
            mapView.drawVertex(newVertex, Color.BLUE); // Draw new vertex
            // If no start vertex has been set, the first created vertex becomes the start
            if (startVertex == null) {
                startVertex = newVertex;
                mapView.drawVertex(startVertex, Color.GREEN);
            }
        }
    }



    private Optional<Vertex> findVertexNear(double x, double y) {

        return vertices.stream()
                .filter(v -> Math.sqrt(Math.pow(v.getX() - x, 2) + Math.pow(v.getY() - y, 2)) < 10)
                .findFirst();
    }

    private void createEdge(Vertex from, Vertex to) {
        mapView.showCreateEdgeDialog(from, to, edge -> {
            // Logic to be executed after edge creation
            edges.add(edge);
            from.addEdge(edge);
            to.addEdge(edge);
            mapView.drawEdge(edge);
        });
    }



    private void runGeneticAlgorithm(){
        if (startVertex == null || endVertex == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Selection Needed");
            alert.setHeaderText("Start and End Vertex Selection");
            alert.setContentText("Please select the start and end vertices before running the algorithm.");
            alert.showAndWait();
            return;
        }
        promptUserForPathType();
        IFitnessCalculator fitnessCalculator;
        if(efficientPath){
             fitnessCalculator = new FitnessEfficiencyCalculator();

        } else{
             fitnessCalculator = new FitnessTimeCalculator();
        }
        graph map = new graph(vertices);
        Car car = new Car("Car1", 60, 20);
        GeneticAlgorithmPathFinder ga = new GeneticAlgorithmPathFinder(car, fitnessCalculator);

        try {
            List<Vertex> efficientPath = ga.findShortestPath(startVertex, endVertex, map);
            List<Edge> accidents = ga.getAccidents();
            for (Edge edge : accidents) {
                mapView.drawCrossOnAccidentEdge(edge);
            }
            mapView.highlightPath(efficientPath, Color.GREEN); // Visualize the path
        } catch (NoRoutesFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    private void resetGraph() {
        vertices.clear();
        edges.clear();
        selectedVertex = null;
        startVertex = null;
        endVertex = null;
        mapView.clear(); // Clear the view
        attachEventHandlers(); // Reattach event handlers as they are removed by mapView.clear()
    }

    public void startApplication() {
        primaryStage.setTitle("Graph Visualization");
        Scene scene = new Scene(mapView.getRoot(), 800, 600); // Assuming a default size for the scene
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
