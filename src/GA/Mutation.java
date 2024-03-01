package GA;

import map.IMap;
import map.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Mutation {
    static Random random = new Random();
    public static void mutate(List<Vertex> originalPath, double mutationRate, IMap map) {
        List<Vertex> path = new ArrayList<>(originalPath); // Make a copy for mutation
        int madeChanges = 0;
        for (int i = 1; i < path.size() - 1; i++) {
            if (random.nextDouble() < mutationRate) {
                int randomIndex = random.nextInt(path.size() - 2) + 1; // Exclude start and end vertices
                Collections.swap(path, i, randomIndex);
                madeChanges++;
                // Check validity immediately after each mutation
                if (!ValidatePath(path, map)) {
                    Collections.swap(path, i, randomIndex); // Revert the swap if it results in an invalid path
                    madeChanges--; // No valid changes were made
                }
            }
        }
        if (madeChanges > 0) {
            originalPath.clear();
            originalPath.addAll(path); // Apply the successful mutation to the original path
        }
    }

    public static boolean ValidatePath(List<Vertex> path, IMap map){
        for(int i = 0; i < path.size() - 1; i++){
            if(!map.getNeighbors(path.get(i)).contains(path.get(i+1))) {
                return false;
            }
        }
        return true;
    }
}
