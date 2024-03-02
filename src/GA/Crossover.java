package GA;

import map.Vertex;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Crossover {
final static double CROSSOVER_RATE = 0.7;
    public static List<List<Vertex>> crossover(List<Vertex> parent1, List<Vertex> parent2) {
        // Find common vertices excluding the start and end points
        Set<Vertex> commonVertices = new HashSet<>(parent1);
        commonVertices.retainAll(new HashSet<>(parent2));
        commonVertices.remove(parent1.get(0)); // Exclude start vertex
        commonVertices.remove(parent1.get(parent1.size() - 1)); // Exclude end vertex

        List<List<Vertex>> children = new ArrayList<>();
        if (commonVertices.isEmpty()) {
            // If no common vertices, return the parents as is to avoid empty children
            children.add(new ArrayList<>(parent1)); // Use new ArrayList to avoid direct modification issues
            children.add(new ArrayList<>(parent2));
        } else {
            for (Vertex commonVertex : commonVertices) {
                children.add(createChild(parent1, parent2, commonVertex));
                children.add(createChild(parent2, parent1, commonVertex));
                break;
            }
        }
        return children;
    }

    private static List<Vertex> createChild(List<Vertex> parent1, List<Vertex> parent2, Vertex commonVertex) {
        // Create a new child starting with a portion from parent1
        List<Vertex> child = new ArrayList<>(parent1.subList(0, parent1.indexOf(commonVertex) + 1));

        // Then add the remaining portion from parent2
        child.addAll(new ArrayList<>(parent2.subList(parent2.indexOf(commonVertex) + 1, parent2.size())));

        return child;
    }
}
