package GA;

import map.Vertex;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The Crossover class provides static methods to perform crossover operations
 * on two parent paths in a genetic algorithm. The crossover operation combines
 * segments from both parents to produce new child paths.
 */
public class Crossover {




    /**
     * Creates two new child paths from the given parent paths. Crossover is
     * performed at a common vertex between the two parent paths, excluding
     * the start and end vertices.
     *
     * @param parent1 The first parent path.
     * @param parent2 The second parent path.
     * @return A list containing two new child paths resulting from the crossover.
     */
    public static List<List<Vertex>> crossover(List<Vertex> parent1, List<Vertex> parent2) {
        Set<Vertex> commonVertices = new HashSet<>(parent1);
        commonVertices.retainAll(new HashSet<>(parent2));
        // Exclude the start and end vertices from the set of common vertices.
        commonVertices.remove(parent1.get(0));
        commonVertices.remove(parent1.get(parent1.size() - 1));

        List<List<Vertex>> children = new ArrayList<>();
        if (commonVertices.isEmpty()) {
            // If there are no common vertices, return the parents as children.
            children.add(new ArrayList<>(parent1));
            children.add(new ArrayList<>(parent2));
        } else {
            // Otherwise, perform the crossover at the first common vertex found.
            for (Vertex commonVertex : commonVertices) {
                children.add(createChild(parent1, parent2, commonVertex));
                children.add(createChild(parent2, parent1, commonVertex));
                break; // Perform the crossover only once.
            }
        }
        return children;
    }

    /**
     * Helper method to create a child path by combining segments from both
     * parent paths at the specified common vertex.
     *
     * @param parent1       The first parent path.
     * @param parent2       The second parent path.
     * @param commonVertex  A common vertex shared between both parent paths.
     * @return A new child path created by combining segments of parent1 and parent2.
     */
    private static List<Vertex> createChild(List<Vertex> parent1, List<Vertex> parent2, Vertex commonVertex) {
        // Start with a segment from parent1 up to the common vertex.
        List<Vertex> child = new ArrayList<>(parent1.subList(0, parent1.indexOf(commonVertex) + 1));
        // Add the segment from parent2 starting just after the common vertex to the end.
        child.addAll(new ArrayList<>(parent2.subList(parent2.indexOf(commonVertex) + 1, parent2.size())));
        return child;
    }
}
