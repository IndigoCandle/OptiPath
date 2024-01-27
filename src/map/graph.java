package map;

import java.util.ArrayList;
import java.util.List;

public class graph {
    private List<Vertex> vertecies;

    public graph(List<Vertex> vertecies){
        this.vertecies = new ArrayList<>();
        for(Vertex vertex : vertecies){
            this.vertecies.add(vertex);
        }
    }

}
