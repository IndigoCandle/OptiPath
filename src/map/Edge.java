package map;

public class Edge {
    private final Vertex source;
    private final Vertex destination;
    private final double distance;
    private double speedLimit;
    private final boolean hasTrafficLights;
    private final double elevationChange;
    private int stopsCount;


    public Edge(Vertex source, Vertex destination, double distance, double speedLimit, boolean hasTrafficLights, double elevationChange, int stopsCount) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.speedLimit = speedLimit;
        this.hasTrafficLights = hasTrafficLights;
        this.elevationChange = elevationChange;
        this.stopsCount = stopsCount;
        if (hasTrafficLights)
            this.stopsCount += (int) (distance / 300);
    }


    public Vertex getSource() {
        return source;
    }



    public Vertex getDestination() {
        return destination;
    }



    public double getDistance() {
        return distance;
    }



    public double getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = speedLimit;
    }


    public boolean hasTrafficLights() {
        return hasTrafficLights;
    }



    public double getElevationChange() {
        return elevationChange;
    }



    public int getStopsCount() {
        return stopsCount;
    }


}
