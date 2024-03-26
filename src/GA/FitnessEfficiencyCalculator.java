package GA;

import GA.Interfaces.IFitnessCalculator;
import cars.Car;
import map.Edge;
import map.interfaces.IMap;
import map.Vertex;

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
        double totalFuelConsumption = 0.0;

        if (path == null || path.size() < 2) {
            return Double.MAX_VALUE;
        }

        for (int i = 0; i < path.size() - 1; i++) {
            Vertex start = path.get(i);
            Vertex end = path.get(i + 1);

            Edge edge = map.getEdgeBetween(start, end);
            if (edge == null) {
                return Double.MAX_VALUE;
            }

            double distance = edge.getDistance();
            boolean isInTraffic = edge.hasTrafficLights();
            double elevationChange = edge.getElevationChange();
            int stops = edge.getStopsCount();


            double speed = Math.min(car.getBestFuelConsumptionSpeed(), edge.getSpeedLimit());


            car.updateStatus(distance, speed, isInTraffic, elevationChange, stops);
            totalFuelConsumption += car.getFuelConsumed();


            car.setFuelConsumed(0);
        }

        return totalFuelConsumption;
    }
}
