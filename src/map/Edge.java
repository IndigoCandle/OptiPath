package map;

public class Edge {
    private Vertex source;
    private Vertex destination;
    private double distance; // Represents the physical length of the road
    private double speedLimit; // Maximum allowed speed on this road
    //private int roadDegree; // Could potentially be used for elevation changes if interpreted as such
    private boolean hasTrafficLights; // Indicates if there are traffic lights on this road
    private double elevationChange; // Represents the elevation change between source and destination
    private int stopsCount; // Represents the number of stops along the edge, including traffic lights

    // Constructor
    public Edge(Vertex source, Vertex destination, double distance, double speedLimit, boolean hasTrafficLights, double elevationChange, int stopsCount) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.speedLimit = speedLimit;
        //this.roadDegree = roadDegree;
        this.hasTrafficLights = hasTrafficLights;
        this.elevationChange = elevationChange;
        this.stopsCount = stopsCount;
        if(hasTrafficLights)
            this.stopsCount += (int) (distance / 300);
    }

    // Getters and Setters
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
