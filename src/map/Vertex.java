package map;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private int id;
    private double x; // The x-coordinate of the vertex on the screen
    private double y; // The y-coordinate of the vertex on the screen
    private List<Edge> edges; // List of edges connecting this vertex to its neighbors

    public Vertex(int id, double x, double y){
        this.id = id;
        this.x = x;
        this.y = y;
        this.edges = new ArrayList<>();
    }

    // Getters and setters
    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addEdge(Edge edge) {
        if (!this.edges.contains(edge)) {
            this.edges.add(edge);
        }
    }
    public List<Vertex> getNeighbors(){
        List<Vertex> neighbors = new ArrayList<>();
        for(Edge e : edges){
            neighbors.add(e.getDestination());
        }
        return neighbors;
    }
}
