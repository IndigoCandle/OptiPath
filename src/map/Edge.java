package map;

public class Edge {
    private Vertex source;
    private Vertex destination;
    private double distance; // Represents the physical length of the road
    private double speedLimit; // Maximum allowed speed on this road
    private int roadDegree;
    private boolean hasTrafficLights; // Indicates if there are traffic lights on this road
    //private double toll; // Represents the toll charge for using this road, if any

    public Edge(Vertex source, Vertex destination, double distance, double speedLimit, int roadDegree, boolean hasTrafficLights) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.speedLimit = speedLimit;
        this.roadDegree = roadDegree;
        this.hasTrafficLights = hasTrafficLights;
        //this.toll = toll;
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

    public int getRoadDegree() {
        return roadDegree;
    }

    public void setRoadDegree(int roadDegree) {
        this.roadDegree = roadDegree;
    }

    public boolean isHasTrafficLights() {
        return hasTrafficLights;
    }

    public void setHasTrafficLights(boolean hasTrafficLights) {
        this.hasTrafficLights = hasTrafficLights;
    }
/*
    public double getToll() {
        return toll;
    }

     public void setToll(double toll) {
        this.toll = toll;
    }*/
}


