package map;

import jdk.nashorn.internal.objects.NativeArray;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private int id;
    private double x; // The x-coordinate of the vertex on the screen
    private double y; // The y-coordinate of the vertex on the screen
    private List<Vertex> Neighbors;

    public Vertex(int id, List<Vertex> list, double x, double y){
        this.id = id;
        this.x = x;
        this.y = y;
        this.Neighbors = new ArrayList<>();
        for(Vertex neighbor : list){
            this.Neighbors.add(neighbor);
        }
    }

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
    public List<Vertex> getNeighbors() {
        return Neighbors;
    }
    public void setNeighbors(List<Vertex> neighbors) {
        for(Vertex neighbor : neighbors){
            this.Neighbors.add(neighbor);
        }
    }

    public void addNeighbor(Vertex neighbor) {
        if (!this.Neighbors.contains(neighbor)) {
            this.Neighbors.add(neighbor);
        }
    }
}
