package GA;

import map.interfaces.IMap;
import map.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Provides methods for mutating a path in a genetic algorithm.
 * Mutation can introduce variability into the population by randomly swapping vertices in the path.
 */
public class Mutation {
    static Random random = new Random();

    /**
     * Mutates a given path by randomly swapping its vertices according to a specified mutation rate.
     * After each swap, the path is checked for validity. If the mutated path is not valid,
     * the swap is reverted. If any changes are made, the original path is updated.
     *
     * @param originalPath The path to mutate.
     * @param mutationRate The probability of each vertex in the path being swapped.
     * @param map          The map context used to validate the path after mutation.
     */
    public static void mutate(List<Vertex> originalPath, double mutationRate, IMap map) {
        List<Vertex> path = new ArrayList<>(originalPath);
        int madeChanges = 0;
        for (int i = 1; i < path.size() - 1; i++) {
            if (random.nextDouble() < mutationRate) {
                int randomIndex = random.nextInt(path.size() - 2) + 1;
                Collections.swap(path, i, randomIndex);
                madeChanges++;
                if (!validatePath(path, map)) {
                    Collections.swap(path, i, randomIndex);
                    madeChanges--;
                }
            }
        }
        if (madeChanges > 0) {
            originalPath.clear();
            originalPath.addAll(path);
        }
    }

    /**
     * Validates a path to ensure that each consecutive pair of vertices in the path
     * is connected by an edge in the map.
     *
     * @param path The path to validate.
     * @param map  The map used to check vertex connectivity.
     * @return true if the path is valid, false otherwise.
     */
    public static boolean validatePath(List<Vertex> path, IMap map) {
        for (int i = 0; i < path.size() - 1; i++) {
            if (!map.getNeighbors(path.get(i)).contains(path.get(i + 1))) {
                return false;
            }
        }
        return true;
    }
}
