package map;

import map.interfaces.IEvents;
import map.interfaces.IMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AccidentEvent implements IEvents {
    private static final Random random = new Random();
    @Override
    public List<Edge> GenerateEvent(IMap map) {
        List<Edge> accidentsOccured = new ArrayList<>();
        for (Edge edge : map.getEdges()) {
            Edge accident = GenerateAccident(map, edge, 0.05);
            if(accident != null) {
                accidentsOccured.add(accident);
            }
        }
        return accidentsOccured;
    }

    private Edge GenerateAccident(IMap map, Edge edge, double chance) {
        if(random.nextDouble() < chance) {
            int severity = random.nextInt(3) + 1;
            double speedLimit = edge.getSpeedLimit() - edge.getSpeedLimit() / severity;
            if (speedLimit == 0) {
                edge.getSource().getEdges().remove(edge);
                map.removeEdge(edge);
                System.out.println("Accident on edge " + edge.getSource().getId() + " -> " + edge.getDestination().getId() + " has occurred. The road is now closed.");
                return edge;
            }else {
                edge.setSpeedLimit(speedLimit);
            }

        }
        return null;
    }
}
