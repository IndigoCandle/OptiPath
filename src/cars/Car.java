package cars;

import map.Edge;

public class Car {
    private String id;
    private double currentSpeed;
    private double bestFuelConsumptionSpeed;
    private final double fuelEfficiency;
    private double fuelConsumed;
    private double topSpeed;


    public Car(String id, double bestFuelConsumptionSpeed, double fuelEfficiency, double topSpeed) {
        this.id = id;
        this.bestFuelConsumptionSpeed = bestFuelConsumptionSpeed;
        this.fuelEfficiency = fuelEfficiency;
        this.fuelConsumed = 0;
        this.currentSpeed = 0;
        this.topSpeed = topSpeed;
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

    public double getFuelEfficiency() {
        return fuelEfficiency;
    }
    public double getTopSpeed(){
        return topSpeed;
    }
    public void setFuelConsumed(int fuelConsumed) {
        this.fuelConsumed = fuelConsumed;
    }

    public double getFuelConsumed() {
        return fuelConsumed;
    }

}



