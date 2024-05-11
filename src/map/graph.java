package map;

import map.interfaces.IMap;

import java.util.ArrayList;
import java.util.List;
/**
 * This class implements the IMap interface and represents a graph composed of vertices and edges.
 */
public class graph implements IMap {
    private final List<Vertex> vertecies;
    private final List<Edge> edges;
    /**
     * Constructs a graph with the given list of vertices. Initializes the edges based on the vertices' connections.
     *
     * @param vertices The list of vertices to include in the graph.
     */
    public graph(List<Vertex> vertices){
        this.vertecies = new ArrayList<>();
        this.vertecies.addAll(vertices);
        edges = new ArrayList<>();
        for(Vertex v : vertices){
            if(v.getEdges() != null)
                this.edges.addAll(v.getEdges());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addVertex(Vertex vertex) {

    }

    /**
     * {@inheritDoc}
     * Adds the edge to the graph and links it to its source vertex.
     */
    @Override
    public void addEdge(Edge edge) {
        edges.add(edge);
        edge.getSource().addEdge(edge);
    }

    /**
     * {@inheritDoc}
     * Also removes any edges that are connected to the vertex.
     */
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


    /**
     * {@inheritDoc}
     * Returns the list of all vertices in the graph.
     */

    @Override
    public List<Vertex> getVertices() {
        return vertecies;
    }

    /**
     * {@inheritDoc}
     * Removes a specified edge from the graph.
     */
    @Override
    public void removeEdgeBetween(Edge edge) {
        edges.remove(edge);
    }

    /**
     * {@inheritDoc}
     * Returns the list of all edges in the graph.
     */
    @Override
    public List<Edge> getEdges() {
        return edges;
    }

    /**
     * {@inheritDoc}
     * Returns a list of vertices that are adjacent to the specified vertex.
     */
    @Override
    public List<Vertex> getNeighbors(Vertex vertex) {
        return vertex.getNeighbors();
    }

    /**
     * {@inheritDoc}
     * Retrieves the edge between two specified vertices if it exists.
     */
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
