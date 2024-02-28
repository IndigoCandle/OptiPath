package map;

import java.util.List;

public interface IMap {
    /**
     * Adds a vertex to the map.
     * @param vertex The vertex to be added.
     */
    void addVertex(Vertex vertex);

    /**
     * Adds an edge between two vertices in the map.
     * @param edge The edge to be added.
     */
    void addEdge(Edge edge);

    /**
     * Removes a vertex from the map.
     * @param vertex The vertex to be removed.
     */
    void removeVertex(Vertex vertex);

    /**
     * Removes an edge from the map.
     * @param edge The edge to be removed.
     */
    void removeEdge(Edge edge);

    /**
     * Finds the shortest path between two vertices using a specified algorithm.
     * @param source The source vertex.
     * @param destination The destination vertex.
     * @return A list of edges representing the shortest path.
     */
    List<Edge> findShortestPath(Vertex source, Vertex destination);

    /**
     * Finds the most fuel-efficient path between two vertices.
     * This method considers factors like road grade, traffic, and speed limits.
     * @param source The source vertex.
     * @param destination The destination vertex.
     * @return A list of edges representing the most fuel-efficient path.
     */
    List<Edge> findMostFuelEfficientPath(Vertex source, Vertex destination);

    /**
     * Gets all vertices in the map.
     * @return A list of all vertices.
     */
    List<Vertex> getVertices();

    /**
     * Gets all edges in the map.
     * @return A list of all edges.
     */
    List<Edge> getEdges();

    /**
     * Gets the neighbors of a specified vertex.
     * @param vertex The vertex whose neighbors are to be retrieved.
     * @return A list of vertices that are neighbors of the specified vertex.
     */
    List<Vertex> getNeighbors(Vertex vertex);

    Edge getEdgeBetween(Vertex start, Vertex end);
}
