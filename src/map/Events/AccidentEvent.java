package map.Events;

import map.Edge;
import map.Vertex;
import map.interfaces.IEvents;
import map.interfaces.IMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AccidentEvent implements IEvents {
    private static final Random random = new Random();
    private final double ACCIDENT_CHANCE = 0.025;
    public static List<List<Vertex>> newPaths;

    public AccidentEvent(){
        newPaths = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     * This implementation simulates random road accidents on the edges of the map. Each edge has a
     * specified chance to have an accident occur, potentially modifying its speed limit or removing
     * the edge entirely if the road becomes impassable. The method iterates over all edges and applies
     * a random chance of accident occurrence based on the provided chance parameter.
     *
     * @return A list of edges where accidents have occurred during this event. If an edge becomes impassable,
     *         it is still included in the returned list but is removed from the map.
     */
    @Override
    public List<Edge> GenerateEvent(IMap map, List<List<Vertex>> paths) {
        List<Edge> accidentsOccurred = new ArrayList<>();
        for (Edge edge : map.getEdges()) {
            Edge accident = GenerateAccident(map, edge, paths);
            if(accident != null) {
                accidentsOccurred.add(accident);

            }
        }
        return accidentsOccurred;
    }

    /**
     * Attempts to generate an accident on a given edge based on a probability chance.
     * If an accident occurs, the speed limit of the edge may be reduced or the edge may be
     * removed entirely from the map if it becomes impassable (speed limit goes to zero).
     *
     * @param map The map on which the edge exists.
     * @param edge The edge on which to potentially generate an accident.
     * @return The edge if an accident occurred on it; null otherwise.
     */
    private Edge GenerateAccident(IMap map, Edge edge, List<List<Vertex>> paths) {
        if (random.nextDouble() < ACCIDENT_CHANCE) {
            //שימוש בקבוע
            int severity = random.nextInt(3) + 1;
            double speedLimit = edge.getSpeedLimit() - edge.getSpeedLimit() / severity;
            if (speedLimit == 0) {
                // Remove the edge completely if it becomes impassable
                edge.getSource().getEdges().remove(edge);
                map.removeEdge(edge);
                System.out.println("Accident on edge " + edge.getSource().getId() + " -> " + edge.getDestination().getId() + " has occurred. The road is now closed.");

                Vertex source = edge.getSource();
                Vertex dest = edge.getDestination();

                paths.forEach(path -> {
                    boolean hasAccidentEdge = false;
                    for (int i = 0; i < path.size() - 1 && !hasAccidentEdge; i++) {
                        if (path.get(i).equals(source) && path.get(i + 1).equals(dest)) {
                            hasAccidentEdge = true;
                        }
                    }
                    if (!hasAccidentEdge) {
                        newPaths.add(new ArrayList<>(path));
                    }
                });

                return edge;
            } else {
                System.out.println("Speed limit: "+ edge.getSpeedLimit() + " changed to: " + speedLimit);
                edge.setSpeedLimit(speedLimit);
            }
        }
        return null;
    }

}