package map;

import map.interfaces.IEvents;
import map.interfaces.IMap;

import java.util.Random;

public class AccidentEvent implements IEvents {
    private static final Random random = new Random();
    @Override
    public void GenerateEvent(IMap map) {
        for (Edge edge : map.getEdges()) {
            GenerateAccident(map, edge, 0.05);
        }
    }

    private void GenerateAccident(IMap map, Edge edge, double chance) {
        if(random.nextDouble() < chance) {
            int severity = random.nextInt(3) + 1;
            double speedLimit = edge.getSpeedLimit() - edge.getSpeedLimit() / severity;
            if (speedLimit == 0) {
                edge.getSource().getEdges().remove(edge);
                map.removeEdge(edge);
            }else {
                edge.setSpeedLimit(speedLimit);
            }
        }

    }
}
