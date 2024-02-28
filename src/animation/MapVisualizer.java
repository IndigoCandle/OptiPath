package animation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.scene.image.ImageView;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;

import map.graph;
import map.Vertex;
import map.Edge;

public class MapVisualizer extends Application {

    private List<Vertex> vertices;
    private List<Edge> edges;
    private ImageView carView;

    private void startCarSimulation() {
        if (edges.isEmpty()) {
            return; // No edges to travel on
        }

        // Create a path transition for the car
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.seconds(5)); // Duration of 5 seconds for each segment
        pathTransition.setNode(carView);
        pathTransition.setInterpolator(Interpolator.LINEAR);

        // Create a path for the car to follow
        Path path = new Path();

        // Start from the first vertex
        Edge firstEdge = edges.get(0);
        Vertex startVertex = firstEdge.getSource();
        path.getElements().add(new MoveTo(startVertex.getX(), startVertex.getY()));

        // Add lines for each edge to the path
        for (Edge edge : edges) {
            path.getElements().add(new VLineTo(edge.getDestination().getY()));
        }

        // Set the path for the transition
        pathTransition.setPath(path);

        // When the animation is finished, stop the car
        pathTransition.setOnFinished(e -> {
            System.out.println("Car has reached the final destination.");
        });

        // Start the animation
        pathTransition.play();
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 800, 600);


        createVerticesAndEdges();

        Image carImage = new Image("file:src/resources/car.png");
        carView = new ImageView(carImage);
        carView.setFitWidth(50);
        carView.setPreserveRatio(true);


        carView.setX(vertices.get(0).getX() - carImage.getWidth() / 2);
        carView.setY(vertices.get(0).getY() - carImage.getHeight() / 2);

        root.getChildren().add(carView);

        drawMap(root);

        primaryStage.setTitle("Map Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();

        startCarSimulation();
    }


    private void createVerticesAndEdges() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();

        // Create vertices with example positions
        Vertex v1 = new Vertex(1, new ArrayList<>(), 100, 100);
        Vertex v2 = new Vertex(2, new ArrayList<>(), 300, 200);
        Vertex v3 = new Vertex(3, new ArrayList<>(), 500, 100);

        // Add vertices to the list
        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);

        // Connect vertices with directed edges
        Edge e1 = new Edge(v1, v2, calculateDistance(v1, v2), 50, 1, false);
        Edge e2 = new Edge(v2, v3, calculateDistance(v2, v3), 50, 1, false);
        Edge e3 = new Edge(v3, v1, calculateDistance(v3, v1), 50, 1, false);

        // Add edges to the list
        edges.add(e1);
        edges.add(e2);
        edges.add(e3);

        // Add neighbors based on directed edges
        v1.addNeighbor(v2); // v1 -> v2
        v2.addNeighbor(v3); // v2 -> v3
        v3.addNeighbor(v1); // v3 -> v1
    }



    // Helper method to calculate the distance between two vertices
    private double calculateDistance(Vertex v1, Vertex v2) {
        return Math.sqrt(Math.pow(v1.getX() - v2.getX(), 2) + Math.pow(v1.getY() - v2.getY(), 2));
    }

    private void drawMap(Pane root) {
        double radius = 10; // Radius of the vertex circles

        for (Vertex vertex : vertices) {
            Circle circle = new Circle(vertex.getX(), vertex.getY(), radius);
            circle.setFill(Color.BLUE);
            root.getChildren().add(circle);
        }

        // Draw edges
        for (Edge edge : edges) {
            Line line = new Line(
                    edge.getSource().getX(), edge.getSource().getY(),
                    edge.getDestination().getX(), edge.getDestination().getY()
            );
            line.setStroke(Color.BLACK);
            root.getChildren().add(line);

        // Calculate the midpoint for the text
        double midX = (edge.getSource().getX() + edge.getDestination().getX()) / 2;
        double midY = (edge.getSource().getY() + edge.getDestination().getY()) / 2;

        // Create a text element to display the length of the edge
        Text text = new Text(midX, midY, String.format("%.2f", edge.getDistance()));
        root.getChildren().add(text);
    }
}
    public static void main(String[] args) {
        launch(args);
    }
}
