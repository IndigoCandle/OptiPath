package GA;

import map.Vertex;
import map.interfaces.IMap;

import java.util.*;

public class Population {

    private Random random = new Random();

    public List<Vertex> generateRandomPathBacktrack(Vertex start, Vertex end, IMap map) {
        List<Vertex> path = new ArrayList<>();
        Set<Vertex> visited = new HashSet<>();
        if (findPathRecursive(start, end, map, path, visited)) {
            return path;
        }
        return new ArrayList<>(); // Return an empty path if no path is found
    }

    private boolean findPathRecursive(Vertex current, Vertex end, IMap map, List<Vertex> path, Set<Vertex> visited) {
        path.add(current);
        visited.add(current);

        if (current.equals(end)) {
            return true; // Found the path
        }

        List<Vertex> neighbors = map.getNeighbors(current);
        // Shuffle the neighbors to ensure random path generation
        Collections.shuffle(neighbors, random);

        for (Vertex next : neighbors) {
            if (!visited.contains(next)) {
                // Recursively attempt to find a path from next to end
                boolean found = findPathRecursive(next, end, map, path, visited);
                if (found) {
                    return true; // Path found
                }
            }
        }

        // Backtrack: remove the current vertex from path and mark it as unvisited
        path.remove(path.size() - 1);
        visited.remove(current);
        return false; // No path found from current
    }
}
