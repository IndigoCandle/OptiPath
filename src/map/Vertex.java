package map;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a vertex in a graph. Each vertex has a unique identifier, a position defined by x and y coordinates,
 * and a list of edges which represent connections to other vertices.
 */
public class Vertex {
    private final int id;
    private final double x; // The x-coordinate of the vertex on the screen
    private final double y; // The y-coordinate of the vertex on the screen
    private final List<Edge> edges; // The list of edges connected to this vertex

    /**
     * Constructs a new Vertex with specified identifier and coordinates.
     *
     * @param id The unique identifier for the vertex.
     * @param x  The x-coordinate of the vertex.
     * @param y  The y-coordinate of the vertex.
     */
    public Vertex(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.edges = new ArrayList<>();
    }

    /**
     * Returns the identifier of the vertex.
     *
     * @return The identifier of this vertex.
     */
    public int getId() {
        return id;
    }



    /**
     * Returns the x-coordinate of the vertex.
     *
     * @return The x-coordinate of this vertex.
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the vertex.
     *
     * @return The y-coordinate of this vertex.
     */
    public double getY() {
        return y;
    }

    /**
     * Returns a list of all edges connected to this vertex.
     *
     * @return A list of edges.
     */
    public List<Edge> getEdges() {
        return edges;
    }

    /**
     * Adds an edge to the vertex if it is not already connected.
     *
     * @param edge The edge to be added to this vertex.
     */
    public void addEdge(Edge edge) {
        if (!this.edges.contains(edge)) {
            this.edges.add(edge);
        }
    }

    /**
     * Retrieves all vertices that are directly connected to this vertex by its edges.
     *
     * @return A list of neighboring vertices.
     */
    public List<Vertex> getNeighbors() {
        List<Vertex> neighbors = new ArrayList<>();
        for (Edge e : edges) {
            neighbors.add(e.getDestination());
        }
        return neighbors;
    }
}
