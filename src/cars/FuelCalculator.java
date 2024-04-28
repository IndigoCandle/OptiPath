package cars;

import map.Edge;

public class FuelCalculator {
    /**
     * Calculates the fuel consumption for a car traveling over a given edge.
     *
     * @param edge The edge the car travels over.
     * @param car The car for which fuel consumption is calculated.
     * @return The amount of fuel consumed to travel over the edge.
     */
    public static double calculateFuel(Edge edge, Car car) {
        double speed = Math.min(car.getBestFuelConsumptionSpeed(), edge.getSpeedLimit());
        double speedFactor = calculateSpeedFactor(speed, car.getBestFuelConsumptionSpeed());
        double distance = edge.getDistance();
        boolean isInTraffic = edge.hasTrafficLights();
        double elevationChange = edge.getElevationChange();
        int stops = edge.getStopsCount();

        // Simplified example calculation, modify according to actual fuel calculation logic
        double basicFuelConsumption = distance / car.getFuelEfficiency();
        basicFuelConsumption *= speedFactor;
        if (elevationChange > 0) {
            basicFuelConsumption *= (1 + 0.2 * elevationChange);
        } else if (elevationChange < 0) {
            basicFuelConsumption *= (1 - 0.2 * Math.abs(elevationChange));
        }
        basicFuelConsumption += stops * 0.005;
        if (isInTraffic) {

            basicFuelConsumption += 0.002 * distance;
        }

        return basicFuelConsumption;
    }

    private static double calculateSpeedFactor(double currentSpeed, double bestFuelConsumptionSpeed) {

        double deviation = Math.abs(currentSpeed - bestFuelConsumptionSpeed);
        return 1.0 + deviation * 0.01;
    }
}
