package GA;

import GA.Interfaces.IFitnessCalculator;
import cars.Car;
import cars.FuelCalculator;
import map.Edge;
import map.Vertex;
import map.interfaces.IMap;

import java.util.List;

public class FitnessEfficiencyCalculator implements IFitnessCalculator {

    /**
     * Calculates the total fuel consumption for a given path and car.
     *
     * @param car The car to simulate the path with.
     * @param path A list of vertices representing the path.
     * @param map The map providing details about the edges between vertices.
     * @return The total fuel consumption for traversing the path with the given car.
     */
    public double calculateFitness(Car car, List<Vertex> path, IMap map) {
        if (path == null || path.size() < 2) {

            return Double.MAX_VALUE;
        }

        double totalFuelConsumption = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            Vertex start = path.get(i);
            Vertex end = path.get(i + 1);
            Edge edge = map.getEdgeBetween(start, end);
            if (edge == null) {
                return Double.MAX_VALUE;
            }

            totalFuelConsumption += FuelCalculator.calculateFuel(edge, car);
        }

        return totalFuelConsumption;
    }
}
