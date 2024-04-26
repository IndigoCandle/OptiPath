package map;

import map.interfaces.IEvents;
import map.interfaces.IMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AccidentEvent implements IEvents {
    private static final Random random = new Random();
    public static List<List<Vertex>> newPaths = new ArrayList<>();

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
            Edge accident = GenerateAccident(map, edge, 0.05, paths);
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
     * @param chance The probability of an accident occurring on this edge.
     * @return The edge if an accident occurred on it; null otherwise.
     */
    private Edge GenerateAccident(IMap map, Edge edge, double chance, List<List<Vertex>> paths) {
        int counter = 0;

        if(random.nextDouble() < chance) {
            int severity = random.nextInt(3) + 1;
            double speedLimit = edge.getSpeedLimit() - edge.getSpeedLimit() / severity;
            if (speedLimit == 0) {
                edge.getSource().getEdges().remove(edge);
                map.removeEdge(edge);
                System.out.println("Accident on edge " + edge.getSource().getId() + " -> " + edge.getDestination().getId() + " has occurred. The road is now closed.");
                Vertex source = edge.getSource();
                Vertex dest = edge.getDestination();
                for(List<Vertex> path : paths){
                    for(Vertex v : path){
                        if(counter == 0){
                            if(v == source)
                                counter++;
                        } else {
                            if(v == dest){
                                counter++;
                                break;
                            }
                            else
                                counter = 0;
                        }

                    }
                    if(counter != 2){
                        newPaths.add(path);
                    }
                }
                return edge;
            } else {
                edge.setSpeedLimit(speedLimit);
            }
        }
        return null;
    }
}