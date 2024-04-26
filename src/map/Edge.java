package map;

public class Edge {
    private Vertex source;
    private Vertex destination;
    private double distance;
    private double speedLimit;
    private boolean hasTrafficLights;
    private double elevationChange;
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

    public void setSource(Vertex source) {
        this.source = source;
    }

    public Vertex getDestination() {
        return destination;
    }

    public void setDestination(Vertex destination) {
        this.destination = destination;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
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

    public void setHasTrafficLights(boolean hasTrafficLights) {
        this.hasTrafficLights = hasTrafficLights;
    }

    public double getElevationChange() {
        return elevationChange;
    }

    public void setElevationChange(double elevationChange) {
        this.elevationChange = elevationChange;
    }

    public int getStopsCount() {
        return stopsCount;
    }

    public void setStopsCount(int stopsCount) {
        this.stopsCount = stopsCount;
    }
}
