package cars;

import map.Edge;

public class TimeCalculator {

public static double calculateTime(Edge edge,Car car) {
        double topSpeed = car.getTopSpeed() - edge.getElevationChange() / edge.getDistance();
        double speed = Math.min(topSpeed, edge.getSpeedLimit());
        double time = edge.getStopsCount() * edge.getDistance() / (speed * 10);
        time += edge.getDistance() / speed;
        time += edge.hasTrafficLights() ? 2 : 0;
        return time;
    }
}
