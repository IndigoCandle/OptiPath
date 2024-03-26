package cars;

import map.Edge;

public class TimeCalculator {

public static double calculateTime(Edge edge,Car car) {
        double speed = Math.min(car.getBestFuelConsumptionSpeed(), edge.getSpeedLimit());
        double time = edge.getStopsCount() * edge.getDistance() / (speed * 10);
        time += edge.getDistance() / speed;
        time += edge.hasTrafficLights() ? 2 : 0;
        return time;
    }
}
