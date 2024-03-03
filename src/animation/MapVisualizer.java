package animation;
import GA.NoRoutesFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import map.Vertex;
import map.Edge;
import map.graph;
import cars.Car;
import GA.GeneticAlgorithmPathFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MapVisualizer extends Application {
    private Pane root = new Pane();
    private List<Vertex> vertices = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();
    private graph map;
    private Vertex selectedVertex = null;
    private Vertex startVertex = null;
    private Vertex endVertex = null;
    Button resetButton;
    Button solveButton;

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("map.css").toExternalForm());
        SetBackground("resources/background1.png");

        solveButton = new Button("Solve");
        solveButton.setLayoutX(700);
        solveButton.setLayoutY(550);
        solveButton.setOnAction(e -> runGeneticAlgorithm());

        resetButton = new Button("Reset");
        resetButton.setLayoutX(600);
        resetButton.setLayoutY(550);
        resetButton.setOnAction(e -> resetGraph());

        root.getChildren().addAll(solveButton, resetButton);

        root.setOnMouseClicked(event -> handleGraphBuilding(event.getX(), event.getY()));
        primaryStage.setTitle("Graph Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleGraphBuilding(double x, double y) {
        Optional<Vertex> nearVertex = findVertexNear(x, y);
        if (nearVertex.isPresent()) {
            if (selectedVertex == null) {
                selectedVertex = nearVertex.get();
                highlightVertex(selectedVertex, true);
            } else if (!nearVertex.get().equals(selectedVertex)) {
                createEdge(selectedVertex, nearVertex.get());
                highlightVertex(selectedVertex, false); // Unhighlight the previously selected vertex
                selectedVertex = null; // Reset for new selection
            }
        } else if (selectedVertex == null) {
            // No vertex near click and no vertex selected, create a new vertex
            Vertex newVertex = new Vertex(vertices.size() + 1, x, y);
            vertices.add(newVertex);
            drawVertex(newVertex);
            // Don't set selectedVertex here if you want to allow immediate creation of another vertex
        }
    }

    private void highlightVertex(Vertex vertex, boolean highlight) {
        // Implement this method to visually indicate a vertex is selected or not
        // This could involve changing the color of the vertex's circle, for example
    }

    private Optional<Vertex> findVertexNear(double x, double y) {
        return vertices.stream()
                .filter(v -> Math.sqrt(Math.pow(v.getX() - x, 2) + Math.pow(v.getY() - y, 2)) < 10)
                .findFirst();
    }

    private void drawVertex(Vertex vertex) {
        Circle circle = new Circle(vertex.getX(), vertex.getY(), 5, Color.BLUE);
        circle.setOnMouseClicked(e -> selectVertex(vertex, circle));
        root.getChildren().add(circle);
    }

    private void createEdge(Vertex from, Vertex to) {
        Dialog<Edge> dialog = new Dialog<>();
        dialog.setTitle("Edge Details");
        dialog.setHeaderText("Enter the edge details");

        // Set the button types.
        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        // Create the distance, speedLimit, hasTrafficLights, elevationChange, and stopsCount labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        // Create a combo box for true or false choice

        ObservableList<Boolean> options =
                FXCollections.observableArrayList(
                        true,
                        false
                );
        final ComboBox comboBox = new ComboBox(options);
        TextField distanceField = new TextField();
        TextField speedLimitField = new TextField();
        TextField hasTrafficLightsField = new TextField();
        TextField elevationChangeField = new TextField();
        TextField stopsCountField = new TextField();
        //grid.add(comboBox, 0, 4);
        grid.add(new Label("Distance:"), 0, 0);
        grid.add(distanceField, 1, 0);
        grid.add(new Label("Speed Limit:"), 0, 1);
        grid.add(speedLimitField, 1, 1);
        grid.add(new Label("Has Traffic Lights (true/false):"), 0, 2);

        grid.add(comboBox, 1, 2);
        grid.add(new Label("Elevation Change:"), 0, 3);
        grid.add(elevationChangeField, 1, 3);
        grid.add(new Label("Stops Count:"), 0, 4);
        grid.add(stopsCountField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Request focus on the distance field by default.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                return new Edge(from, to, Double.parseDouble(distanceField.getText()),
                        Double.parseDouble(speedLimitField.getText()),
                        //get result from the combo box
                        (Boolean) comboBox.getValue(),
                        Double.parseDouble(elevationChangeField.getText()),
                        Integer.parseInt(stopsCountField.getText()));
            }
            return null;
        });

        Optional<Edge> result = dialog.showAndWait();

        result.ifPresent(edge -> {
            edges.add(edge);
            from.addEdge(edge);
            drawEdge(edge);
        });
    }


    private void drawEdge(Edge edge) {
        Line line = new Line(edge.getSource().getX(), edge.getSource().getY(),
                edge.getDestination().getX(), edge.getDestination().getY());
        root.getChildren().add(line);
    }

    private void runGeneticAlgorithm() {
        // Ensure start and end vertices are selected
        if (startVertex == null || endVertex == null) {
            System.out.println("Select start and end vertices.");
            return;
        }

        // Initialize the graph with the created vertices and edges
        map = new graph(vertices);

        // Assuming Car and GeneticAlgorithmPathFinder are already implemented
        Car car = new Car("Car1", 60, 20);
        GeneticAlgorithmPathFinder ga = new GeneticAlgorithmPathFinder(car);

        List<Vertex> efficientPath = null;
        try {
            efficientPath = ga.findShortestPath(startVertex, endVertex, map);
        } catch (NoRoutesFoundException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        highlightPath(efficientPath);
    }

    private void highlightPath(List<Vertex> path) {
        for (int i = 0; i < path.size() - 1; i++) {
            Vertex from = path.get(i);
            Vertex to = path.get(i + 1);
            Line line = new Line(from.getX(), from.getY(), to.getX(), to.getY());
            line.setStroke(Color.RED);
            line.setStrokeWidth(2);
            root.getChildren().add(line);
        }
    }

    private void selectVertex(Vertex vertex, Circle circle) {
        if (startVertex == null) {
            startVertex = vertex;
            circle.setFill(Color.GREEN);
        } else if (endVertex == null && !vertex.equals(startVertex)) {
            endVertex = vertex;
            circle.setFill(Color.RED);
        }
    }

    private void resetGraph() {
        root.getChildren().clear();
        vertices.clear();
        edges.clear();
        selectedVertex = null;
        startVertex = null;
        endVertex = null;
        root.getChildren().addAll(solveButton, resetButton);
    }
    public void SetBackground(String path){
        root.setStyle("-fx-background-image: url('"+path+"'); " +
                "-fx-background-position: center center; " +
                "-fx-background-repeat: stretch;");

    }

    public static void main(String[] args) {
        launch(args);
    }




}
