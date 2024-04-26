package map;

import map.interfaces.IEvents;
import map.interfaces.IMap;

import java.util.List;

public class ConstructionEvent implements IEvents {

    public List<Edge> GenerateEvent(IMap map) {
        return null;
    }

    @Override
    public List<Edge> GenerateEvent(IMap map, List<List<Vertex>> paths) {
        return null;
    }
}
