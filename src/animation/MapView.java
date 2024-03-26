package animation;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.PathTransition;
import javafx.animation.Interpolator;
import javafx.util.Duration;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.LineTo;
import map.Vertex;
import map.Edge;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class MapView {
    private final Pane root;
    private Button solveButton;
    private Button resetButton;
    private ImageView carView;

    public MapView(Pane root) {
        this.root = root;
        setupUIComponents();
    }

    private void setupUIComponents() {
        setBackground("resources/background1.png");
        solveButton = new Button("Solve");
        solveButton.setLayoutX(700);
        solveButton.setLayoutY(550);

        resetButton = new Button("Reset");
        resetButton.setLayoutX(600);
        resetButton.setLayoutY(550);

        this.root.getChildren().addAll(solveButton, resetButton);
    }

    public void setBackground(String imagePath) {
        this.root.setStyle("-fx-background-image: url('" + imagePath + "'); " +
                "-fx-background-position: center center; " +
                "-fx-background-repeat: stretch;");
    }

    public Button getSolveButton() {
        return solveButton;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public void drawVertex(Vertex vertex, Color color) {
        Circle circle = new Circle(vertex.getX(), vertex.getY(), 5, color);
        this.root.getChildren().add(circle);
    }

    public void drawEdge(Edge edge) {
        Line line = new Line(edge.getSource().getX(), edge.getSource().getY(),
                edge.getDestination().getX(), edge.getDestination().getY());
        this.root.getChildren().add(line);
        drawArrowHead(edge);
    }

    public void highlightPath(List<Vertex> path, Color color) {
        for (int i = 0; i < path.size() - 1; i++) {
            Vertex from = path.get(i);
            Vertex to = path.get(i + 1);
            Line line = new Line(from.getX(), from.getY(), to.getX(), to.getY());
            line.setStroke(color);
            line.setStrokeWidth(2);
            this.root.getChildren().add(line);
            drawArrowHead(from, to, color);
        }
        CarAnimation(path);
    }

    public void showPathChoiceDialog(Consumer<Boolean> onChoiceMade) {
        // Create a custom dialog pane
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Path Choice");
        dialog.setHeaderText("Choose the type of path:");
        ButtonType efficientButtonType = new ButtonType("Most Efficient");
        ButtonType fastestButtonType = new ButtonType("Fastest");
        dialog.getDialogPane().getButtonTypes().addAll(efficientButtonType, fastestButtonType);

        // Set content with custom graphics or information if needed
        // e.g., dialog.getDialogPane().setContent(...);

        // Convert the result to a boolean when one of the buttons is clicked
        dialog.setResultConverter(buttonType -> buttonType == efficientButtonType);

        // Show the dialog and wait for the result
        Optional<Boolean> result = dialog.showAndWait();
        result.ifPresent(onChoiceMade);
    }

    void drawCrossOnAccidentEdge(Edge edge) {
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

    private void drawArrowHead(Edge edge) {
        drawArrowHead(edge.getSource(), edge.getDestination(), Color.BLACK);
    }

    private void drawArrowHead(Vertex source, Vertex destination, Color color) {
        double dx = destination.getX() - source.getX();
        double dy = destination.getY() - source.getY();
        double length = Math.sqrt(dx * dx + dy * dy);
        double unitDx = dx / length;
        double unitDy = dy / length;

        double arrowHeadSize = 10;

        Polygon arrowHead = new Polygon();
        arrowHead.getPoints().addAll(
                destination.getX(), destination.getY(),
                destination.getX() - arrowHeadSize * (unitDx + unitDy), destination.getY() - arrowHeadSize * (unitDy - unitDx),
                destination.getX() - arrowHeadSize * (unitDx - unitDy), destination.getY() - arrowHeadSize * (unitDy + unitDx)
        );
        arrowHead.setFill(color);
        this.root.getChildren().add(arrowHead);
    }

    public void clear() {
        this.root.getChildren().clear();
        setupUIComponents(); // Re-setup the UI after clearing
    }



    public void showCreateEdgeDialog(Vertex from, Vertex to, Consumer<Edge> edgeConsumer) {
        Dialog<Edge> dialog = new Dialog<>();
        dialog.setTitle("Edge Details");
        dialog.setHeaderText("Enter the edge details");

        // Set the button types.
        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        // Create the distance, speedLimit, etc. inputs here.
        GridPane grid = new GridPane();
        TextField distanceField = new TextField();
        TextField speedLimitField = new TextField();
        CheckBox hasTrafficLightsCheckbox = new CheckBox();
        TextField elevationChangeField = new TextField();
        TextField stopsCountField = new TextField();

        // Populate the grid with labels and fields.
        grid.add(new Label("Distance:"), 0, 0);
        grid.add(distanceField, 1, 0);
        grid.add(new Label("Speed Limit:"), 0, 1);
        grid.add(speedLimitField, 1, 1);
        grid.add(new Label("Has Traffic Lights:"), 0, 2);
        grid.add(hasTrafficLightsCheckbox, 1, 2);
        grid.add(new Label("Elevation Change:"), 0, 3);
        grid.add(elevationChangeField, 1, 3);
        grid.add(new Label("Stops Count:"), 0, 4);
        grid.add(stopsCountField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to an edge when the create button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                try {
                    double distance = Double.parseDouble(distanceField.getText());
                    double speedLimit = Double.parseDouble(speedLimitField.getText());
                    boolean hasTrafficLights = hasTrafficLightsCheckbox.isSelected();
                    double elevationChange = Double.parseDouble(elevationChangeField.getText());
                    int stopsCount = Integer.parseInt(stopsCountField.getText());

                    return new Edge(from, to, distance, speedLimit, hasTrafficLights, elevationChange, stopsCount);
                } catch (NumberFormatException e) {
                    // Handle invalid input here.
                    return null;
                }
            }
            return null;
        });

        Optional<Edge> result = dialog.showAndWait();
        result.ifPresent(edgeConsumer);
    }



    public Pane getRoot() {
        return root ;
    }
}
