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
    Vertex start;
    Vertex end;

    // Adjust these colors and image paths as needed
    private static final Color VERTEX_COLOR = Color.web("#FFC857");
    private static final Color EDGE_COLOR = Color.web("#000000");
    private static final Color PATH_COLOR = Color.web("#F76C6C");

    @Override
    public void start(Stage primaryStage) {
        // Initialization and drawing methods...
        root.getStyleClass().add("background");
        drawBackground();
        initializeGraph();
        drawGraph(); // Only draws the graph without the efficient path

        Button solveButton = new Button("Solve");
        solveButton.setOnAction(e -> solveAndDisplayPath());
        solveButton.setLayoutX(10); // Position the button; adjust as needed
        solveButton.setLayoutY(10);
        root.getChildren().add(solveButton);

        Scene scene = new Scene(root, 800, 600);
        String css = this.getClass().getResource("map.css").toExternalForm();
        scene.getStylesheets().add(css);

        primaryStage.setTitle("Graph Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void solveAndDisplayPath() {
        efficientPath = findEfficientPath(); // Finds the most efficient path using the GA
        addCarImageView(); // Adds a car image to the starting vertex
        startCarSimulation();
        drawEfficientPath(); // Draws the efficient path on the graph

        // Optionally, add animations or other visual enhancements here
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
        Vertex v2 = new Vertex(2, 300, 200);
        Vertex v3 = new Vertex(3, 500, 100);
        Vertex v4 = new Vertex(4, 600, 200);
        Vertex v5 = new Vertex(5, 700, 100);
        Vertex v6 = new Vertex(6, 200, 300);
        Vertex v7 = new Vertex(7, 400, 400);
        Vertex v8 = new Vertex(8, 500, 500);
        Vertex v9 = new Vertex(9, 300, 600);
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
        Edge e1 = (new Edge(v1, v2,500 , 50,  false, 0, 0));
        v1.addEdge(e1);
        edges.add(e1);
        // Add more vertices to the map
        Edge e2 = (new Edge(v2, v3, 500, 50,  false, 0, 0));
        v2.addEdge(e2);
        edges.add(e2);
        // Add edges
        Edge e3 = (new Edge(v3, v1, calculateDistance(v3, v1), 50,  false, 0, 0));
        v3.addEdge(e3);
        edges.add(e3);
        Edge e4 = (new Edge(v1, v3, 1000, 50,  true, 20, 0));
        v1.addEdge(e4);
        Edge e14 = (new Edge(v1, v3, 1000, 50,  false, 50, 0));
        v1.addEdge(e14);
        edges.add(e14);
        Edge e5 = (new Edge(v3, v4, calculateDistance(v3, v4), 50,  false, 0, 0));
        v3.addEdge(e5);
        edges.add(e5);
        Edge e6 = (new Edge(v1, v6, 3000, 60,  false, 0, 0));
        v1.addEdge(e6);
        edges.add(e6);
        Edge e7 =(new Edge(v5, v1, calculateDistance(v5, v1), 50,  false, 0, 0)); // Connecting back to v1
        v5.addEdge(e7);
        Edge e8 = (new Edge(v2, v6, calculateDistance(v2, v6), 50,  false, 0, 0));
        v2.addEdge(e8);
        edges.add(e8);
        Edge e9 = (new Edge(v6, v3, 30000, 60,  true, 0, 0));
        v6.addEdge(e9);
        edges.add(e9);
        Edge e10 = (new Edge(v7, v8, calculateDistance(v7, v8), 50,  false, 0, 0));
        v7.addEdge(e10);
        edges.add(e10);
        Edge e11 = (new Edge(v8, v9, calculateDistance(v8, v9), 50,  false, 0, 0));
        v8.addEdge(e11);
        edges.add(e11);
        Edge e12 = (new Edge(v9, v10, calculateDistance(v9, v10), 50,  false, 0, 0));
        v9.addEdge(e12);
        edges.add(e12);
        Edge e13 = (new Edge(v10, v1, calculateDistance(v10, v1), 50,  false, 0, 0)); // Connecting back to v1
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

    private void drawGraph() {
        for (Vertex vertex : vertices) {
            Circle circle = new Circle(vertex.getX(), vertex.getY(), 10);
            circle.getStyleClass().add("circle");
            circle.setEffect(new DropShadow(15, VERTEX_COLOR.darker()));
            root.getChildren().add(circle);

            Text idText = new Text(vertex.getX() - 5, vertex.getY() - 15, String.valueOf(vertex.getId()));
            //idText.setFill(TEXT_COLOR);
            //idText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            root.getChildren().add(idText);
        }

        edges.forEach(edge -> {
            Line line = new Line(edge.getSource().getX(), edge.getSource().getY(),
                    edge.getDestination().getX(), edge.getDestination().getY());
            line.setStroke(EDGE_COLOR);
            line.getStrokeDashArray().setAll(10.0, 5.0);
            line.setStrokeWidth(2);

            root.getChildren().add(line);

            drawArrow(line, EDGE_COLOR);
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
            System.out.print(start.getId() + " -> " + end.getId() + " -> ");
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