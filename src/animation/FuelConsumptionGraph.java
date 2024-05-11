package animation;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

/**
 * The {@code FuelConsumptionGraph} class handles the creation and animation of a line chart
 * that displays the evolution of fuel consumption over generations.
 */
public class FuelConsumptionGraph {
    private final LineChart<Number, Number> lineChart;
    private final XYChart.Series<Number, Number> series;
    private final List<Double> fuelConsumptionData;

    /**
     * Constructs a {@code FuelConsumptionGraph} with the provided fuel consumption data.
     *
     * @param fuelConsumptionData The list of fuel consumption values for each generation.
     */
    public FuelConsumptionGraph(List<Double> fuelConsumptionData) {
        this.fuelConsumptionData = fuelConsumptionData;

        // Create the X and Y axes for the chart
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Generation");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Fuel Consumption");

        // Creates the line chart and adds the series
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Fuel Consumption Over Generations");

        series = new XYChart.Series<>();
        series.setName("Fuel Consumption");
        lineChart.getData().add(series);
    }

    /**
     * Displays the graph in a new window and animates the addition of data points.
     */
    public void displayGraph() {
        Stage stage = new Stage();
        Scene scene = new Scene(lineChart, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Fuel Consumption Evolution");
        stage.show();

        animateGraph();
    }

    /**
     * Animates the addition of fuel consumption data points to the graph.
     * The graph starts with a high initial value and transitions to the actual data points.
     */
    private void animateGraph() {
        Timeline timeline = new Timeline();


        double initialValue = fuelConsumptionData.stream()
                .max(Double::compare)
                .orElse(100.0) * 1.5;


        KeyFrame initialKeyFrame = new KeyFrame(Duration.seconds(0), e -> {
            series.getData().add(new XYChart.Data<>(0, initialValue));
        });
        timeline.getKeyFrames().add(initialKeyFrame);


        for (int i = 1; i <= fuelConsumptionData.size(); i++) {
            final int index = i;

            KeyFrame keyFrame = new KeyFrame(Duration.seconds(i), e -> {
                series.getData().add(new XYChart.Data<>(index, fuelConsumptionData.get(index - 1)));
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.setCycleCount(1);
        timeline.play();
    }
}
