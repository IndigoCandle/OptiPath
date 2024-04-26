package GA;

import GA.Interfaces.IFitnessCalculator;
import cars.Car;
import map.Edge;
import map.Vertex;
import map.interfaces.IMap;
import cars.TimeCalculator;
import java.util.List;

public class FitnessTimeCalculator implements IFitnessCalculator {

    /**
     * Calculates the total time required for a car to traverse a given path.
     * The fitness value is determined based on the time taken to travel each edge in the path.
     * If the path is null, has fewer than two vertices, or contains edges not present in the map,
     * the fitness value will default to {@code Double.MAX_VALUE}.
     *
     * @param car  The car object that contains properties affecting travel time.
     * @param path A list of vertices representing the path to traverse.
     * @param map  The map providing the edges and their corresponding details.
     * @return The total time taken to traverse the path or {@code Double.MAX_VALUE} if path is invalid.
     */
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
