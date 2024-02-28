package GA.Interfaces;

import map.IMap;
import map.Vertex;
import java.util.List;

public interface IShortestPathFinder {
    /**
     * Finds the shortest path between two vertices in a graph.
     * @param start The starting vertex.
     * @param end The ending vertex.
     * @return A list of vertices representing the shortest path from start to end.
     */
    List<Vertex> findShortestPath(Vertex start, Vertex end, IMap map);
}
