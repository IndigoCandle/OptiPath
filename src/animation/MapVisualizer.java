/*

package animation;
import GA.NoRoutesFoundException;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import map.Vertex;
import map.Edge;
import map.graph;
import cars.Car;
import GA.GeneticAlgorithmPathFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MapVisualizer extends Application {
    private final Pane root = new Pane();
    private List<Vertex> vertices = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();
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
        solveButton.setOnAction(e -> {
            runGeneticAlgorithm();
            e.consume(); // Consume the event to prevent it from propagating
        });

        resetButton = new Button("Reset");
        resetButton.setLayoutX(600);
        resetButton.setLayoutY(550);
        resetButton.setOnAction(e -> {
            resetGraph();
            e.consume(); // Consume the event to prevent it from propagating
        });

        root.getChildren().addAll(solveButton, resetButton);

        root.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (isEventInsideButton(event, solveButton) || isEventInsideButton(event, resetButton)) {
                event.consume();
            }
        });

        root.setOnMouseClicked(event -> handleGraphBuilding(event.getX(), event.getY()));
        primaryStage.setTitle("Graph Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean isEventInsideButton(MouseEvent event, Button button) {
        // Calculate the button's bounds
        Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());
        // Check if the mouse event is inside the button's bounds
        return buttonBounds.contains(event.getSceneX(), event.getSceneY());
    }

    private void handleGraphBuilding(double x, double y) {
        Optional<Vertex> nearVertex = findVertexNear(x, y);
        if (nearVertex.isPresent()) {
            if (selectedVertex == null) {
                selectedVertex = nearVertex.get();
               // highlightVertex(selectedVertex, true);
            } else if (!nearVertex.get().equals(selectedVertex)) {
                createEdge(selectedVertex, nearVertex.get());
                //highlightVertex(selectedVertex, false);
                selectedVertex = null;
            }
        } else if (selectedVertex == null) {
            // No vertex near click and no vertex selected, create a new vertex
            Vertex newVertex = new Vertex(vertices.size() + 1, x, y);
            vertices.add(newVertex);
            drawVertex(newVertex);
            // Don't set selectedVertex here if you want to allow immediate creation of another vertex
        }
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


        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);


        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);


        ObservableList<Boolean> options =
                FXCollections.observableArrayList(
                        true,
                        false
                );
        final ComboBox comboBox = new ComboBox(options);
        TextField distanceField = new TextField();
        TextField speedLimitField = new TextField();
        TextField elevationChangeField = new TextField();
        TextField stopsCountField = new TextField();
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


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                return new Edge(from, to, Double.parseDouble(distanceField.getText()),
                        Double.parseDouble(speedLimitField.getText()),
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

    private void CarAnimation(List<Vertex> path) {
        if (path == null || path.isEmpty()) return;

        Path animationPath = new Path();
        MoveTo moveTo = new MoveTo(path.get(0).getX(), path.get(0).getY());
        animationPath.getElements().add(moveTo);

        for (int i = 1; i < path.size(); i++) {
            LineTo lineTo = new LineTo(path.get(i).getX(), path.get(i).getY());
            animationPath.getElements().add(lineTo);
        }


        ImageView carView = new ImageView(new Image(getClass().getResourceAsStream("/resources/car.png")));
        carView.setFitWidth(40);
        carView.setPreserveRatio(true);

        carView.setX(path.get(0).getX() - carView.getFitWidth() / 2);
        carView.setY(path.get(0).getY() - carView.getFitHeight() / 2);


        root.getChildren().add(carView);


        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.seconds(path.size()));
        pathTransition.setPath(animationPath);
        pathTransition.setNode(carView);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setInterpolator(Interpolator.LINEAR);
        pathTransition.setCycleCount(1);

        pathTransition.play();
    }


    private void drawEdge1(Edge edge) {
        Line line = new Line(edge.getSource().getX(), edge.getSource().getY(),
                edge.getDestination().getX(), edge.getDestination().getY());
        root.getChildren().add(line);
    }
    private void drawEdge(Edge edge) {
        Vertex source = edge.getSource();
        Vertex destination = edge.getDestination();


        Line line = new Line(source.getX(), source.getY(), destination.getX(), destination.getY());
        root.getChildren().add(line);


        double dx = destination.getX() - source.getX();
        double dy = destination.getY() - source.getY();
        double length = Math.sqrt(dx * dx + dy * dy);
        double unitDx = dx / length;
        double unitDy = dy / length;


        double arrowHeadSize = 10;


        Polygon arrowHead = new Polygon();
        arrowHead.getPoints().addAll(
                destination.getX(), destination.getY(), // Arrow tip
                destination.getX() - arrowHeadSize * (unitDx + unitDy), destination.getY() - arrowHeadSize * (unitDy - unitDx),
                destination.getX() - arrowHeadSize * (unitDx - unitDy), destination.getY() - arrowHeadSize * (unitDy + unitDx)
        );


        root.getChildren().add(arrowHead);
    }






    private void runGeneticAlgorithm() {

        if (startVertex == null || endVertex == null) {
            System.out.println("Select start and end vertices.");
            return;
        }

        graph map = new graph(vertices);

        Car car = new Car("Car1", 60, 20);
        GeneticAlgorithmPathFinder ga = new GeneticAlgorithmPathFinder(car);

        List<Vertex> efficientPath = null;
        try {
            efficientPath = ga.findShortestPath(startVertex, endVertex, map);
        } catch (NoRoutesFoundException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        List<Edge> accidents = ga.getAccidents();
        for (Edge edge : accidents) {
            drawCrossOnAccidentEdge(edge);
        }
        highlightPath(efficientPath);
    }

    private void highlightPath(List<Vertex> path) {
        if(!path.isEmpty()){
            for (int i = 0; i < path.size() - 1; i++) {
                Vertex from = path.get(i);
                Vertex to = path.get(i + 1);


                Line line = new Line(from.getX(), from.getY(), to.getX(), to.getY());
                line.setStroke(Color.GREEN);
                line.setStrokeWidth(2);
                root.getChildren().add(line);


                double dx = to.getX() - from.getX();
                double dy = to.getY() - from.getY();
                double length = Math.sqrt(dx * dx + dy * dy);
                double unitDx = dx / length;
                double unitDy = dy / length;


                double arrowHeadSize = 10;


                Polygon arrowHead = new Polygon();
                arrowHead.getPoints().addAll(
                        to.getX(), to.getY(),
                        to.getX() - arrowHeadSize * (unitDx + unitDy), to.getY() - arrowHeadSize * (unitDy - unitDx),
                        to.getX() - arrowHeadSize * (unitDx - unitDy), to.getY() - arrowHeadSize * (unitDy + unitDx)
                );
                arrowHead.setFill(Color.GREEN);
                CarAnimation(path);

                root.getChildren().add(arrowHead);
            }

        }else{
            System.out.println("No path found");

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
    private void drawCrossOnAccidentEdge(Edge edge) {
        //System.out.println("Accident on edge: " + edge);
        double x = (edge.getSource().getX() + edge.getDestination().getX()) / 2;
        double y = (edge.getSource().getY() + edge.getDestination().getY()) / 2;
        Line line1 = new Line(x - 5, y - 5, x + 5, y + 5);
        Line line2 = new Line(x - 5, y + 5, x + 5, y - 5);
        line1.resize(10, 10);
        line2.resize(10, 10);
        line1.setStroke(Color.RED);
        line2.setStroke(Color.RED);
        root.getChildren().addAll(line1, line2);
    }

    private void drawXAccidentOnEdge(Edge edge) {
        double x = (edge.getSource().getX() + edge.getDestination().getX()) / 2;
        double y = (edge.getSource().getY() + edge.getDestination().getY()) / 2;
        Circle circle = new Circle(x, y, 5, Color.RED);
        root.getChildren().add(circle);
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

*/
