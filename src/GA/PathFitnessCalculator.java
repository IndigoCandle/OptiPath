package GA;

import cars.Car;
import map.Edge;
import map.IMap;
import map.Vertex;

import java.util.List;

public class PathFitnessCalculator {

    /**
     * Calculates the total fuel consumption for a given path and car.
     *
     * @param car The car to simulate the path with.
     * @param path A list of vertices representing the path.
     * @param map The map providing details about the edges between vertices.
     * @return The total fuel consumption for traversing the path with the given car.
     */
    public static double calculateTotalFuelConsumption(Car car, List<Vertex> path, IMap map) {
        double totalFuelConsumption = 0.0;

        if (path == null || path.size() < 2) {
            return Double.MAX_VALUE; // Return a large value to indicate an invalid or infeasible path.
        }

        for (int i = 0; i < path.size() - 1; i++) {
            Vertex start = path.get(i);
            Vertex end = path.get(i + 1);

            Edge edge = map.getEdgeBetween(start, end);
            if (edge == null) {
                return Double.MAX_VALUE; // Skip if no edge exists between the vertices, or consider adding a penalty.
            }

            // Simulate driving for the segment defined by the edge.
            double distance = edge.getDistance();
            boolean isInTraffic = edge.hasTrafficLights(); // Assuming traffic lights indicate traffic.
            double elevationChange = edge.getElevationChange();
            int stops = edge.getStopsCount();

            // Assuming the car's speed adapts to the speed limit of the edge or its best fuel consumption speed, whichever is lower.
            double speed = Math.min(car.getBestFuelConsumptionSpeed(), edge.getSpeedLimit());

            // Use the car's method to calculate fuel consumption for the segment.
            car.updateStatus(distance, speed, isInTraffic, elevationChange, stops);
            totalFuelConsumption += car.getFuelConsumed();

            // Reset the fuel consumed after each segment to simulate the next segment independently.
            car.setFuelConsumed(0);
        }

        return totalFuelConsumption;
    }
}
