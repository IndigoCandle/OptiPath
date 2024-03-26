package GA;

import GA.Interfaces.IFitnessCalculator;
import cars.Calculator;
import cars.Car;
import map.Edge;
import map.Vertex;
import map.interfaces.IMap;
import cars.TimeCalculator;
import java.util.List;

public class FitnessTimeCalculator implements IFitnessCalculator {

    public double calculateFitness(Car car, List<Vertex> path, IMap map) {
        if (path == null || path.size() < 2) {
            return Double.MAX_VALUE;
        }
        double time = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Vertex start = path.get(i);
            Vertex end = path.get(i + 1);

            Edge edge = map.getEdgeBetween(start, end);
            if (edge == null) {
                return Double.MAX_VALUE;
            }
            time += TimeCalculator.calculateTime(edge, car);

        }

        return time;
    }
}
