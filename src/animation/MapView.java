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
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * This class represents the view component of the map in the application. It handles all visual aspects,
 * from rendering vertices and edges to animating paths.
 */
public class MapView {
    private final Pane root;
    private Button solveButton;
    private Button resetButton;
    private Button ShowFitnessGraphButton;


    /**
     * Constructor initializes the MapView with a specific root pane.
     *
     * @param root The root pane on which all UI elements are placed.
     */
    public MapView(Pane root) {
        this.root = root;
        setupUIComponents();
        loadStylesheet();
    }

    /**
     * Sets up the initial UI components of the map view, including background, buttons, and layout.
     */
    private void setupUIComponents() {
        setBackground("resources/background1.png");
        solveButton = new Button("Solve");
        solveButton.setLayoutX(700);
        solveButton.setLayoutY(550);

        resetButton = new Button("Reset");
        resetButton.setLayoutX(622);
        resetButton.setLayoutY(550);

        ShowFitnessGraphButton = new Button("Fitness Graph");
        ShowFitnessGraphButton.setLayoutX(500);
        ShowFitnessGraphButton.setLayoutY(550);
        this.root.getChildren().addAll(solveButton, resetButton, ShowFitnessGraphButton);
    }

    /**
     * Sets the background image of the map view.
     *
     * @param imagePath The relative path to the background image file.
     */
    public void setBackground(String imagePath) {
        this.root.setStyle("-fx-background-image: url('" + imagePath + "'); " +
                "-fx-background-position: center center; " +
                "-fx-background-repeat: stretch;");
    }

    /**
     * Returns the solve button to attach actions externally.
     *
     * @return The solve button.
     */
    public Button getSolveButton() {
        return solveButton;
    }

    /**
     * Returns the ShowFitnessGraph button to attach actions externally.
     *
     * @return The ShowFitnessGraph button.
     */
    public Button getShowFitnessGraphButton() {
        return ShowFitnessGraphButton;
    }

    /**
     * Returns the reset button to attach actions externally.
     *
     * @return The reset button.
     */
    public Button getResetButton() {
        return resetButton;
    }

    /**
     * Loads the CSS stylesheet and applies it to the root pane.
     */
    private void loadStylesheet() {
        String stylesheet = Objects.requireNonNull(getClass().getResource("/animation/styling/main.css")).toExternalForm();
        root.getStylesheets().add(stylesheet);
    }
    /**
     * Draws a vertex on the map.
     *
     * @param vertex The vertex to draw.
     * @param color The color to use for the vertex representation.
     */
    public void drawVertex(Vertex vertex, Color color) {
        Circle circle = new Circle(vertex.getX(), vertex.getY(), 5, color);
        this.root.getChildren().add(circle);
    }

    /**
     * Draws an edge between two vertices on the map.
     *
     * @param edge The edge to be drawn.
     */
    public void drawEdge(Edge edge) {
        Line line = new Line(edge.getSource().getX(), edge.getSource().getY(),
                edge.getDestination().getX(), edge.getDestination().getY());
        this.root.getChildren().add(line);
        drawArrowHead(edge);
    }


    /**
     * Highlights a path between vertices using a specific color.
     *
     * @param path The list of vertices that form the path to highlight.
     * @param color The color to use for highlighting the path.
     */
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

    /**
     * Displays a dialog for the user to choose the type of path they want to find.
     *
     * @param onChoiceMade A consumer to handle the choice made by the user.
     */
    public void showPathChoiceDialog(Consumer<Boolean> onChoiceMade) {
        // Create a custom dialog pane
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Path Choice");
        dialog.setHeaderText("Choose the type of path:");
        ButtonType efficientButtonType = new ButtonType("Most Efficient");
        ButtonType fastestButtonType = new ButtonType("Fastest");
        dialog.getDialogPane().getButtonTypes().addAll(efficientButtonType, fastestButtonType);

        dialog.setResultConverter(buttonType -> buttonType == efficientButtonType);

        Optional<Boolean> result = dialog.showAndWait();
        result.ifPresent(onChoiceMade);
    }

    /**
     * Draws a red cross on an edge to indicate an accident or disruption.
     *
     * @param edge The edge on which to draw the accident symbol.
     */
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

    /**
     * Animates a car moving along a path defined by a list of vertices.
     *
     * @param path The list of vertices that define the path along which the car moves.
     */
    private void CarAnimation(List<Vertex> path) {
        if (path == null || path.isEmpty()) return;

        Path animationPath = new Path();
        MoveTo moveTo = new MoveTo(path.get(0).getX(), path.get(0).getY());
        animationPath.getElements().add(moveTo);

        for (int i = 1; i < path.size(); i++) {
            LineTo lineTo = new LineTo(path.get(i).getX(), path.get(i).getY());
            animationPath.getElements().add(lineTo);
        }


        ImageView carView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/resources/car.png"))));
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

    /**
     * Draws an arrowhead at the end of an edge to visually indicate the direction of the edge.
     *
     * @param edge The edge for which to draw the arrowhead.
     */
    private void drawArrowHead(Edge edge) {
        drawArrowHead(edge.getSource(), edge.getDestination(), Color.BLACK);
    }

    /**
     * Draws an arrowhead pointing from the source vertex to the destination vertex.
     * The method calculates the direction of the edge and places the arrowhead at the destination vertex.
     *
     * @param source      The starting vertex of the edge.
     * @param destination The ending vertex of the edge where the arrowhead points to.
     * @param color       The color of the arrowhead.
     */
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

    /**
     * Clears all graphical elements from the map view and re-initializes the UI components.
     */
    public void clear() {
        this.root.getChildren().clear();
        setupUIComponents();
    }


    /**
     * Displays a dialog for creating a new edge with specific details, allowing user input for parameters such as distance and speed limit.
     *
     * @param from The starting vertex of the new edge.
     * @param to The ending vertex of the new edge.
     * @param edgeConsumer A consumer that handles the edge created based on user input.
     */
    public void showCreateEdgeDialog(Vertex from, Vertex to, Consumer<Edge> edgeConsumer) {
        Dialog<Edge> dialog = createEdgeDialog();
        GridPane grid = createEdgeDetailsGrid();

        // Adding fields to grid
        addTextField(grid, "Distance:", 0);
        addTextField(grid, "Speed Limit:", 1);
        addCheckbox(grid);
        addTextField(grid, "Elevation Change:", 3);
        addTextField(grid, "Stops Count:", 4);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> convertDialogResult(dialogButton, from, to, dialog));
        Optional<Edge> result = dialog.showAndWait();
        // Else, the user has been shown an alert about invalid input, and no further action is taken
        result.ifPresent(edgeConsumer);
    }

    /**
     * Creates a dialog for edge creation with 'Create' and 'Cancel' buttons.
     * This dialog will be used to capture user input for creating a new edge in the map view.
     *
     * @return A {@code Dialog<Edge>} object for edge creation.
     */
    private Dialog<Edge> createEdgeDialog() {
        Dialog<Edge> dialog = new Dialog<>();
        dialog.setTitle("Edge Details");
        dialog.setHeaderText("Enter the edge details");
        dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Create", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);
        return dialog;
    }

    /**
     * Creates and configures a grid pane for edge detail input fields.
     * The grid pane is used in the edge creation dialog and lays out the input fields in a structured manner.
     *
     * @return A configured {@code GridPane} object for edge detail input fields.
     */
    private GridPane createEdgeDetailsGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10); // Horizontal gap between columns
        grid.setVgap(10); // Vertical gap between rows
        return grid;
    }

    /**
     * Adds a text field for user input to the grid pane with a corresponding label.
     * This is a utility method used in setting up the edge creation dialog.
     *
     * @param grid The grid pane to which the text field is added.
     * @param label The label text for the text field.
     * @param row The row index in the grid pane where the label and text field will be placed.
     */
    private void addTextField(GridPane grid, String label, int row) {
        Label fieldLabel = new Label(label);
        TextField textField = new TextField();
        grid.add(fieldLabel, 0, row); // Add label to column 0 of the specified row
        grid.add(textField, 1, row); // Add text field to column 1 of the specified row
    }

    /**
     * Adds a checkbox for user input to the grid pane with a corresponding label.
     * This method facilitates the input of boolean values such as the presence of traffic lights on an edge.
     *
     * @param grid The grid pane to which the checkbox is added.
     */
    private void addCheckbox(GridPane grid) {
        CheckBox checkBox = new CheckBox();
        grid.add(new Label("Has Traffic Lights:"), 0, 2);
        grid.add(checkBox, 1, 2);
    }


    /**
     * Converts the dialog button result into an edge object if the OK button is clicked.
     * This method extracts edge details from the dialog's input fields and constructs a new edge object.
     *
     * @param dialogButton The button pressed by the user.
     * @param from         The source vertex for the new edge.
     * @param to           The destination vertex for the new edge.
     * @param dialog       The dialog from which to extract input values.
     * @return A new Edge object if valid inputs are provided; null otherwise.
     */
    private Edge convertDialogResult(ButtonType dialogButton, Vertex from, Vertex to, Dialog dialog) {
        if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            try {
                GridPane grid = (GridPane) dialog.getDialogPane().getContent();
                double distance = Double.parseDouble(((TextField) grid.getChildren().get(1)).getText());
                double speedLimit = Double.parseDouble(((TextField) grid.getChildren().get(3)).getText());
                boolean hasTrafficLights = ((CheckBox) grid.getChildren().get(5)).isSelected();
                double elevationChange = Double.parseDouble(((TextField) grid.getChildren().get(7)).getText());
                int stopsCount = Integer.parseInt(((TextField) grid.getChildren().get(9)).getText());
                if(stopsCount < 0 || speedLimit < 0 || distance <= 0){
                    ValueAlert();
                    return null;
                }
                return new Edge(from, to, distance, speedLimit, hasTrafficLights, elevationChange, stopsCount);
            } catch (NumberFormatException e) {
                ValueAlert();
                return null;
            }
        }
        return null;
    }

    private void ValueAlert(){
        showAlert("Invalid input", "Please enter valid numbers for distance, speed limit, and other values.");
    }

    /**
     * Pops up an alert on the screen
     * @param title Title for the warning
     * @param content the message content of the warning
     */
    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }




    public Pane getRoot() {
        return root ;
    }
}
