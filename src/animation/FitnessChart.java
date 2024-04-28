/*
package animation;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.List;

public class FitnessChart extends JFrame {

    public FitnessChart(String title, List<Double> fitnessData) {
        super(title);
        XYSeries series = new XYSeries("Fitness Over Generations");

        for (int i = 0; i < fitnessData.size(); i++) {
            series.add(i, fitnessData.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Genetic Algorithm Fitness",
                "Generation",
                "Fitness",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 400));
        setContentPane(chartPanel);
    }

    public static void displayChart(List<Double> fitnessData) {
        FitnessChart chart = new FitnessChart("GA Fitness Improvement", fitnessData);
        chart.pack();
        chart.setLocationRelativeTo(null);
        chart.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chart.setVisible(true);
    }
}
*/
