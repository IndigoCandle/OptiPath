package cars;

import map.Edge;

public class Car {
    private String id;
    private double currentSpeed;
    private double bestFuelConsumptionSpeed;
    private double fuelEfficiency;
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


        basicFuelConsumption *= speedFactor;


        if (elevationChange > 0) {

            basicFuelConsumption *= (1 + 0.2 * elevationChange);
        } else if (elevationChange < 0) {

            basicFuelConsumption *= (1 - 0.05 * Math.abs(elevationChange));
        }
        basicFuelConsumption += stops * 0.005;

        return basicFuelConsumption;
    }

    private double calculateSpeedFactor(double currentSpeed) {

        double deviation = Math.abs(currentSpeed - bestFuelConsumptionSpeed);
        return 1.0 + deviation * 0.01;
    }


    public double getCurrentSpeed() {
        return currentSpeed;
    }

    public double getBestFuelConsumptionSpeed() {
        return bestFuelConsumptionSpeed;
    }

    public void setBestFuelConsumptionSpeed(double bestFuelConsumptionSpeed) {
        this.bestFuelConsumptionSpeed = bestFuelConsumptionSpeed;
    }

    public void setFuelConsumed(int fuelConsumed) {
        this.fuelConsumed = fuelConsumed;
    }

    public double getFuelConsumed() {
        return fuelConsumed;
    }

}



