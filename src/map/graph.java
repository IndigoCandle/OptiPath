package map;

import map.interfaces.IMap;

import java.util.ArrayList;
import java.util.List;

public class graph implements IMap {
    private List<Vertex> vertecies;
    private List<Edge> edges;

    public graph(List<Vertex> vertices){
        this.vertecies = new ArrayList<>();
        this.vertecies.addAll(vertices);
        edges = new ArrayList<>();
        for(Vertex v : vertices){
            if(v.getEdges() != null)
                this.edges.addAll(v.getEdges());
        }
    }

    @Override
    public void addVertex(Vertex vertex) {

    }

    @Override
    public void addEdge(Edge edge) {

    }

    @Override
    public void removeVertex(Vertex vertex) {
        vertecies.remove(vertex);
        for(Vertex v : vertecies){
            v.getEdges().removeIf(e -> e.getDestination().equals(vertex));
        }
    }

    @Override
    public void removeEdge(Edge edge) {
        for(Vertex v : vertecies){
            v.getEdges().remove(edge);
        }
    }

    @Override
    public List<Edge> findShortestPath(Vertex source, Vertex destination) {
        return null;
    }

    @Override
    public List<Edge> findMostFuelEfficientPath(Vertex source, Vertex destination) {
        return null;
    }

    @Override
    public List<Vertex> getVertices() {
        return null;
    }

    @Override
    public void removeEdgeBetween(Edge edge) {
        edges.remove(edge);
    }

    @Override
    public List<Edge> getEdges() {
        return edges;
    }

    @Override
    public List<Vertex> getNeighbors(Vertex vertex) {
        return vertex.getNeighbors();
    }

    @Override
    public Edge getEdgeBetween(Vertex start, Vertex end) {
        for(Edge e : start.getEdges()){
            if(e.getDestination().equals(end)){
                return e;
            }
        }
        return null;
    }
}
