package GA;

import map.interfaces.IMap;
import map.Vertex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Population {
    private final Random random = new Random();

    public List<Vertex> generateRandomPath(Vertex start, Vertex end, IMap map) {
        List<Vertex> path = new ArrayList<>();
        Set<Vertex> visited = new HashSet<>();
        Vertex current = start;
        path.add(current);
        visited.add(current);

        while (!current.equals(end)) {
            List<Vertex> neighbors = map.getNeighbors(current);
            if(neighbors.isEmpty()) {
                // Handle case where no neighbors are available
                return null; // Or consider an alternative approach
            }
            neighbors.removeIf(visited::contains); // Efficient loop prevention
            if (neighbors.isEmpty()) {
                // No unvisited neighbors left, handle accordingly
                return null; // Or backtrack, depending on your design
            }
            Vertex next = neighbors.get(random.nextInt(neighbors.size()));
            path.add(next);
            visited.add(next);
            current = next;
        }
        return path;
    }
}
