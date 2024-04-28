package GA;

import map.Vertex;
import map.interfaces.IMap;

import java.util.*;

/**
 * This class represents a population of paths in a genetic algorithm.
 * It is responsible for generating random paths between two vertices in a graph,
 * utilizing backtracking to ensure valid paths are created.
 */
public class Population {

    private final Random random = new Random();

    /**
     * Generates a random path from the start vertex to the end vertex using a backtracking algorithm.
     * This method attempts to find a path by exploring neighbors randomly, backtracking when a dead-end is reached.
     *
     * @param start The starting vertex of the path.
     * @param end   The ending vertex of the path.
     * @param map   The map providing the graph structure and connections between vertices.
     * @return A list of vertices representing the path from start to end if found; otherwise, an empty list.
     */
    public List<Vertex> generateRandomPathBacktrack(Vertex start, Vertex end, IMap map) {
        List<Vertex> path = new ArrayList<>();
        Set<Vertex> visited = new HashSet<>();
        if (findPathRecursive(start, end, map, path, visited)) {
            return path;
        }
        return new ArrayList<>();
    }

    /**
     * Recursively finds a path from the current vertex to the end vertex.
     * This method adds the current vertex to the path and marks it as visited.
     * It then recursively explores each neighbor, backtracking when no further progress can be made.
     *
     * @param current  The current vertex being explored.
     * @param end      The destination vertex.
     * @param map      The map providing neighbor data for each vertex.
     * @param path     The current path being built.
     * @param visited  A set of vertices that have been visited in the current path to prevent cycles.
     * @return true if a path to the end vertex is successfully found, false otherwise.
     */
    private boolean findPathRecursive(Vertex current, Vertex end, IMap map, List<Vertex> path, Set<Vertex> visited) {
        path.add(current);
        visited.add(current);

        if (current.equals(end)) {
            return true;
        }

        List<Vertex> neighbors = map.getNeighbors(current);

        Collections.shuffle(neighbors, random); // Randomize the order of neighbors to ensure varied paths

        for (Vertex next : neighbors) {
            if (!visited.contains(next)) {
                boolean found = findPathRecursive(next, end, map, path, visited);
                if (found) {
                    return true;
                }
            }
        }

        // Backtrack by removing the current vertex from path and visited set if no path is found
        path.remove(path.size() - 1);
        visited.remove(current);
        return false;
    }
}
