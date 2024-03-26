package map;

import java.util.ArrayList;
import java.util.List;

public class CityGraphFactory {

    public static graph createSmallCityGraph() {
        List<Vertex> vertices = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        int idCounter = 0;


        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                vertices.add(new Vertex(idCounter++, i * 200 + 50, j * 150 + 100));
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                vertices.add(new Vertex(idCounter++, j * 200 + 100, i * 150 + 50));
            }
        }


        graph cityGraph = new graph(vertices);

        for (int i = 0; i < 20; i += 4) {
            for (int j = 0; j < 3; j++) {
                Vertex source = vertices.get(i + j);
                Vertex destination = vertices.get(i + j + 1);
                Edge edge = new Edge(source, destination, 200, 50, false, 0, 0);
                edges.add(edge);
                cityGraph.addEdge(edge);

                Edge reverseEdge = new Edge(destination, source, 200, 50, false, 0, 0);
                edges.add(reverseEdge);
                cityGraph.addEdge(reverseEdge);
            }
        }


        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 16; j += 4) {
                Vertex source = vertices.get(i + j);
                Vertex destination = vertices.get(i + j + 4);
                Edge edge = new Edge(source, destination, 150, 50, true, 0, 1);
                edges.add(edge);
                cityGraph.addEdge(edge);

                Edge reverseEdge = new Edge(destination, source, 150, 50, true, 0, 1);
                edges.add(reverseEdge);
                cityGraph.addEdge(reverseEdge);
            }
        }

        return cityGraph;
    }
}
