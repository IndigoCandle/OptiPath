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
        return new ArrayList<>();
    }

    private boolean findPathRecursive(Vertex current, Vertex end, IMap map, List<Vertex> path, Set<Vertex> visited) {
        path.add(current);
        visited.add(current);

        if (current.equals(end)) {
            return true;
        }

        List<Vertex> neighbors = map.getNeighbors(current);

        Collections.shuffle(neighbors, random);

        for (Vertex next : neighbors) {
            if (!visited.contains(next)) {

                boolean found = findPathRecursive(next, end, map, path, visited);
                if (found) {
                    return true;
                }
            }
        }


        path.remove(path.size() - 1);
        visited.remove(current);
        return false;
    }
}
