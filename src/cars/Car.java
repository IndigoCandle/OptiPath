package cars;

import map.Edge;

public class Car {
    private String id;
    private double currentSpeed;
    private double bestFuelConsumptionSpeed; // Optimal speed for best fuel efficiency
    private double fuelEfficiency; // Fuel efficiency at the best consumption speed
    private double fuelConsumed;
    private boolean isInTraffic;
    private double elevationChange;
    private int stopsCount;
    private double currentX; // Current X position on the map
    private double currentY; // Current Y position on the map

    public Car(String id, double bestFuelConsumptionSpeed, double fuelEfficiency) {
        this.id = id;
        this.bestFuelConsumptionSpeed = bestFuelConsumptionSpeed;
        this.fuelEfficiency = fuelEfficiency;
        this.fuelConsumed = 0;
        this.currentSpeed = 0;
        this.isInTraffic = false;
        this.elevationChange = 0;
        this.stopsCount = 0;
    }

    public void updateStatus(double distanceTraveled, double currentSpeed, boolean isInTraffic, double elevationChange, int stops) {
        this.currentSpeed = currentSpeed;
        this.isInTraffic = isInTraffic;
        this.elevationChange = elevationChange;
        this.stopsCount += stops;

        double fuelUsed = calculateFuelConsumption(distanceTraveled, currentSpeed, isInTraffic, elevationChange, stops);
        this.fuelConsumed += fuelUsed;
    }

    private double calculateFuelConsumption(double distance, double currentSpeed, boolean isInTraffic, double elevationChange, int stops) {
        double basicFuelConsumption = distance / fuelEfficiency;
        double speedFactor = calculateSpeedFactor(currentSpeed);

        // Adjusting basic consumption based on current speed
        basicFuelConsumption *= speedFactor;

        // Adjust for other factors like traffic, elevation, and stops
        if (elevationChange > 0) {
            // Increase fuel consumption more significantly when going uphill
            basicFuelConsumption *= (1 + 0.2 * elevationChange); // 20% more fuel used per unit of elevation gain
        } else if (elevationChange < 0) {
            // Decrease fuel consumption when going downhill, but to a lesser extent
            basicFuelConsumption *= (1 - 0.05 * Math.abs(elevationChange)); // 5% less fuel used per unit of elevation loss
        }
        basicFuelConsumption += stops * 0.005;

        return basicFuelConsumption;
    }

    private double calculateSpeedFactor(double currentSpeed) {
        // Assuming fuel efficiency decreases as the deviation from the optimal speed increases
        // This is a simple linear relationship for demonstration
        double deviation = Math.abs(currentSpeed - bestFuelConsumptionSpeed);
        return 1.0 + deviation * 0.01; // 1% decrease in efficiency for every unit of speed away from optimal
    }
    public void setPosition(double x, double y) {
        this.currentX = x;
        this.currentY = y;
    }
    public double getCurrentX() {
        return currentX;
    }

    public double getCurrentY() {
        return currentY;
    }

    // Method to update the car's position based on the simulation step
    public void updatePosition(double deltaX, double deltaY) {
        this.currentX += deltaX;
        this.currentY += deltaY;
    }

    // Getters and setters...
}



