package GA;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import map.IMap;
import map.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {
    public List<Vertex> generateRandomPath(Vertex start, Vertex end, IMap map) {
        List<Vertex> path = new ArrayList<>();
        Vertex current = start;
        path.add(current);

        while (!current.equals(end)) {
            List<Vertex> neighbors = map.getNeighbors(current);
            Vertex next = neighbors.get(new Random().nextInt(neighbors.size()));
            if(next == null)
                return null;
            // Simple check to avoid immediate loops, can be expanded to more complex loop detection
            if (!path.contains(next)) {
                path.add(next);
                current = next;
            }
        }
        return path;
    }

}
