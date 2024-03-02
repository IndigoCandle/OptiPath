package animation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.animation.PathTransition;
import javafx.util.Duration;
import javafx.animation.Interpolator;
import map.Vertex;
import map.Edge;
import map.graph;
import cars.Car;
import GA.GeneticAlgorithmPathFinder;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapVisualizer extends Application {
    private Pane root = new Pane();
    private List<Vertex> vertices = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();
    private ImageView carView;
    private graph map;
    private List<Vertex> efficientPath;
    private Vertex selectedStart = null;
    private Vertex selectedEnd = null;
    Vertex start;
    Vertex end;

    // Adjust these colors and image paths as needed
    private static final Color VERTEX_COLOR = Color.web("#FFC857");
    private static final Color EDGE_COLOR = Color.web("#000000");
    private static final Color PATH_COLOR = Color.web("#F76C6C");

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(root, 800, 600);
        root.getStyleClass().add("background"); // Apply the root style class

        String css = this.getClass().getResource("map.css").toExternalForm();
        scene.getStylesheets().add(css);

        initializeGraph();
        drawGraph(); // Only draws the graph without the efficient path

        Button solveButton = new Button("Solve");
        solveButton.getStyleClass().add("solve-button"); // Apply CSS class
        solveButton.setLayoutX(10); // Adjust layout as needed
        solveButton.setLayoutY(10);
        solveButton.setOnAction(e -> {
            if(selectedStart != null && selectedEnd != null) {
                solveAndDisplayPath();
            } else {
                System.out.println("Please select start and end vertices.");
            }
        });
        root.getChildren().add(solveButton);

        primaryStage.setTitle("Graph Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    private void setGrassBackground() {
        // Correctly reference the resource path
        URL imageUrl = getClass().getResource("resources/background1.png");
        if (imageUrl != null) {
            Image backgroundImage = new Image(imageUrl.toExternalForm());
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(800);
            backgroundView.setFitHeight(600);
            root.getChildren().add(backgroundView);
        } else {
            System.err.println("Background image file not found");
        }
    }


    private void initializeGraph() {
        Vertex v1 = new Vertex(1, 100, 100);
        Vertex v2 = new Vertex(2, 270, 170);
        Vertex v3 = new Vertex(3, 500, 100);
        Vertex v4 = new Vertex(4, 600, 200);
        Vertex v5 = new Vertex(5, 700, 100);
        Vertex v6 = new Vertex(6, 200, 300);
        Vertex v7 = new Vertex(7, 400, 400);
        Vertex v8 = new Vertex(8, 500, 400);
        Vertex v9 = new Vertex(9, 300, 400);
        Vertex v10 = new Vertex(10, 100, 500);

        // Continue adding more vertices as needed
        List<Vertex> vertices = new ArrayList<>();
        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);
        vertices.add(v4);
        vertices.add(v5);
        vertices.add(v6);
        vertices.add(v7);
        vertices.add(v8);
        vertices.add(v9);
        vertices.add(v10);
        Edge e1 = (new Edge(v1, v2, 1, 50, 1, false, 0, 0));
        v1.addEdge(e1);
        edges.add(e1);
        // Add more vertices to the map
        Edge e2 = (new Edge(v2, v3, 100000000, 50, 1, false, 0, 0));
        v2.addEdge(e2);
        edges.add(e2);
        // Add edges
        Edge e3 = (new Edge(v3, v1, calculateDistance(v3, v1), 50, 1, false, 0, 0));
        v3.addEdge(e3);
        edges.add(e3);
        Edge e4 = (new Edge(v1, v3, 10000000, 5, 30, false, 0, 0));
        v1.addEdge(e4);
        edges.add(e4);
        Edge e5 = (new Edge(v3, v4, calculateDistance(v3, v4), 50, 1, false, 0, 0));
        v3.addEdge(e5);
        edges.add(e5);
        Edge e6 = (new Edge(v1, v6, 30, 60, 1, false, 0, 0));
        v1.addEdge(e6);
        edges.add(e6);
        Edge e7 =(new Edge(v5, v1, calculateDistance(v5, v1), 50, 1, false, 0, 0)); // Connecting back to v1
        v5.addEdge(e7);
        Edge e8 = (new Edge(v2, v6, calculateDistance(v2, v6), 50, 1, false, 0, 0));
        v2.addEdge(e8);
        edges.add(e8);
        Edge e9 = (new Edge(v6, v3, 3, 60, 1, false, 0, 0));
        v6.addEdge(e9);
        edges.add(e9);
        Edge e10 = (new Edge(v7, v8, calculateDistance(v7, v8), 50, 1, false, 0, 0));
        v7.addEdge(e10);
        edges.add(e10);
        Edge e11 = (new Edge(v8, v9, calculateDistance(v8, v9), 50, 1, false, 0, 0));
        v8.addEdge(e11);
        edges.add(e11);
        Edge e12 = (new Edge(v9, v10, calculateDistance(v9, v10), 50, 1, false, 0, 0));
        v9.addEdge(e12);
        edges.add(e12);
        Edge e13 = (new Edge(v10, v1, calculateDistance(v10, v1), 50, 1, false, 0, 0)); // Connecting back to v1
        v10.addEdge(e13);
        edges.add(e13);
        // Continue adding more edges as needed, including elevation change and stops if applicable
        map = new graph(vertices);
        // Initialize the car with optimal parameters
        Car car = new Car("Car1", 60, 20); // ID, bestFuelConsumptionSpeed, fuelEfficiency // Assuming 'graph' constructor can accept a list of vertices
        // Make sure to populate 'edges' in your 'graph' as well
        start = v1;
        end = v3;
    }

    private void drawBackground() {
        int tileSize = 50; // Size of each "tile" or "patch" of grass
        Color[] grassColors = { Color.web("#6B8E23"), Color.web("#556B2F"), Color.web("#7CFC00"), Color.web("#ADFF2F") };

        // Cover the entire pane with "tiles" to simulate grass
        for (int x = 0; x < root.getWidth(); x += tileSize) {
            for (int y = 0; y < root.getHeight(); y += tileSize) {
                Color tileColor = grassColors[(int) (Math.random() * grassColors.length)];
                Rectangle grassTile = new Rectangle(x, y, tileSize, tileSize);
                grassTile.setFill(tileColor);
                root.getChildren().add(grassTile);
            }
        }

        // Optionally, add some "flowers" or other decorations randomly
        for (int i = 0; i < 100; i++) { // 100 flowers
            Circle flower = new Circle(Math.random() * root.getWidth(), Math.random() * root.getHeight(), 5, Color.web("#FFD700")); // Golden flowers
            root.getChildren().add(flower);
        }
    }

    private List<Vertex> findEfficientPath() {
        // Instantiate your GA solver and find the most efficient path
        Car car = new Car("Car1", 60, 20);
        GeneticAlgorithmPathFinder gaSolver = new GeneticAlgorithmPathFinder(car);
        return gaSolver.findShortestPath(start, end, map);
    }
    private void handleVertexClick(Vertex vertex) {
        if (selectedStart == null || selectedStart == vertex) {
            selectedStart = vertex; // Select as start vertex
            selectedEnd = null; // Clear end vertex selection
        } else if (selectedEnd == null) {
            selectedEnd = vertex; // Select as end vertex
        }
        highlightVertices(); // Update the UI based on the new selection
    }


    private void highlightVertices() {
        for (javafx.scene.Node node : root.getChildren()) {
            if (node instanceof Circle) {
                Circle circle = (Circle) node;
                Vertex vertex = (Vertex) circle.getUserData();
                if (vertex == selectedStart) {
                    circle.setFill(Color.RED);
                } else if (vertex == selectedEnd) {
                    circle.setFill(Color.GREEN);
                } else {
                    circle.setFill(VERTEX_COLOR);
                }
            }
        }
    }
    private void resetSelection() {
        selectedStart = null;
        selectedEnd = null;
        drawGraph(); // Redraw the graph to reset the colors
    }

    private void solveAndDisplayPath() {
        efficientPath = findEfficientPath(); // Adjust this method as needed
        drawEfficientPath();
        addCarImageView();
        startCarSimulation();
    }

    private void drawGraph() {
        root.getChildren().clear(); // Clear the pane before redrawing
        for (Vertex vertex : vertices) {
            Circle circle = new Circle(vertex.getX(), vertex.getY(), 10, VERTEX_COLOR);
            circle.setUserData(vertex); // Associate the Vertex with the Circle
            circle.setOnMouseClicked(e -> handleVertexClick(vertex));
            root.getChildren().add(circle);
        }

        edges.forEach(edge -> {
            Line line = new Line(edge.getSource().getX(), edge.getSource().getY(),
                    edge.getDestination().getX(), edge.getDestination().getY());
            line.getStyleClass().add("edge"); // Apply edge style class
            root.getChildren().add(line);
        });
    }

    private void drawEfficientPath() {
        for (int i = 0; i < efficientPath.size() - 1; i++) {
            Vertex start = efficientPath.get(i);
            Vertex end = efficientPath.get(i + 1);
            Line line = new Line(start.getX(), start.getY(), end.getX(), end.getY());
            line.setStroke(PATH_COLOR);
            line.setStrokeWidth(3);
            root.getChildren().add(line);
        }
    }




    private void addCarImageView() {
        carView = new ImageView(new Image("resources/car.png"));
        carView.setFitWidth(40);
        carView.setPreserveRatio(true);
        carView.setX(efficientPath.get(0).getX() - 20);
        carView.setY(efficientPath.get(0).getY() - 20);
        root.getChildren().add(carView);
    }

    private void startCarSimulation() {
        Path path = new Path();
        path.getElements().add(new MoveTo(efficientPath.get(0).getX(), efficientPath.get(0).getY()));
        efficientPath.forEach(vertex -> path.getElements().add(new LineTo(vertex.getX(), vertex.getY())));

        PathTransition transition = new PathTransition(Duration.seconds(efficientPath.size() * 0.5), path, carView);
        transition.setInterpolator(Interpolator.EASE_BOTH);
        transition.play();
    }

    private void drawArrow(Line line, Color color) {
        double size = 5;
        double angle = Math.atan2(line.getEndY() - line.getStartY(), line.getEndX() - line.getStartX());
        Polygon arrowHead = new Polygon();
        arrowHead.getPoints().addAll(new Double[]{
                0.0, 0.0,
                size, -size,
                size * 2, 0.0,
                size, size
        });
        arrowHead.setFill(color);
        arrowHead.setRotate(Math.toDegrees(angle) + 90);
        arrowHead.setLayoutX(line.getEndX());
        arrowHead.setLayoutY(line.getEndY());

        root.getChildren().add(arrowHead);
    }

    public static void main(String[] args) {
        launch(args);
    }
    private static double calculateDistance(Vertex v1, Vertex v2) {
        return Math.sqrt(Math.pow(v1.getX() - v2.getX(), 2) + Math.pow(v1.getY() - v2.getY(), 2));
    }
}
