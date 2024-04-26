package map.interfaces;

import map.Edge;
import map.Vertex;

import java.util.ArrayList;
import java.util.List;

public interface IEvents {
    /**
     * triggers an event
     * @param map the map for the event to be triggered on
     * @param paths the current list of winning paths
     * @return returns a list of edges after the event had occurred
     */
    public List<Edge> GenerateEvent(IMap map, List<List<Vertex>> paths);
}
