package GA;

import map.Vertex;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Crossover {
final static double CROSSOVER_RATE = 0.7;
    public static List<List<Vertex>> crossover(List<Vertex> parent1, List<Vertex> parent2) {

        Set<Vertex> commonVertices = new HashSet<>(parent1);
        commonVertices.retainAll(new HashSet<>(parent2));
        commonVertices.remove(parent1.get(0));
        commonVertices.remove(parent1.get(parent1.size() - 1));

        List<List<Vertex>> children = new ArrayList<>();
        if (commonVertices.isEmpty()) {
            children.add(new ArrayList<>(parent1));
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
        List<Vertex> child = new ArrayList<>(parent1.subList(0, parent1.indexOf(commonVertex) + 1));
        child.addAll(new ArrayList<>(parent2.subList(parent2.indexOf(commonVertex) + 1, parent2.size())));

        return child;
    }
}
