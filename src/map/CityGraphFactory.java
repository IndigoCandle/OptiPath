package map;

import java.util.ArrayList;
import java.util.List;

public class CityGraphFactory {

    public static graph createSmallCityGraph() {
        List<Vertex> vertices = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();

        // Manually adding vertices with unique positions
        Vertex v1 = new Vertex(0, 50, 100);
        Vertex v2 = new Vertex(1, 150, 200);
        Vertex v3 = new Vertex(2, 250, 300);
        Vertex v4 = new Vertex(3, 350, 400);
        Vertex v5 = new Vertex(4, 450, 100);
        Vertex v6 = new Vertex(5, 550, 200);
        Vertex v7 = new Vertex(6, 650, 300);
        Vertex v8 = new Vertex(7, 750, 400);
        Vertex v9 = new Vertex(8, 850, 100);
        Vertex v10 = new Vertex(9, 950, 200);

        // Adding vertices to the graph

        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);
        vertices.add(v4);
        vertices.add(v5);
        vertices.add(v6);
        vertices.add(v7);
        vertices.add(v8);
        vertices.add(v9);
        vertices.add(v10);

        // Creating edges with elaborate details

        // Existing direct edges
        edges.add(new Edge(v1, v2, 120, 40, true, 5, 2));
        edges.add(new Edge(v2, v3, 100, 60, true, 10, 4));
        edges.add(new Edge(v3, v4, 150, 50, false, 3, 1));
        edges.add(new Edge(v4, v5, 200, 70, true, 0, 0));
        edges.add(new Edge(v5, v6, 180, 55, false, 6, 2));
        edges.add(new Edge(v6, v7, 130, 65, true, 11, 3));
        edges.add(new Edge(v7, v8, 210, 80, false, 7, 1));
        edges.add(new Edge(v8, v9, 160, 85, true, 2, 2));
        edges.add(new Edge(v9, v10, 100, 45, false, 5, 3));
        edges.add(new Edge(v10, v1, 190, 75, true, 0, 0));

        // Adding cross edges to create multiple paths between various vertices
        edges.add(new Edge(v1, v5, 300, 50, false, 5, 1));
        edges.add(new Edge(v2, v6, 350, 60, true, 4, 2));
        edges.add(new Edge(v3, v7, 400, 40, true, 2, 3));
        edges.add(new Edge(v4, v8, 450, 55, false, 3, 1));
        edges.add(new Edge(v5, v9, 500, 65, true, 10, 0));
        edges.add(new Edge(v6, v10, 550, 45, false, 8, 1));
        edges.add(new Edge(v1, v4, 600, 75, true, 0, 2));
        edges.add(new Edge(v2, v5, 650, 85, false, 4, 0));
        edges.add(new Edge(v3, v6, 700, 95, true, 5, 4));
        edges.add(new Edge(v7, v10, 750, 50, false, 1, 2));
        edges.add(new Edge(v8, v1, 800, 60, true, 6, 1));
        edges.add(new Edge(v9, v2, 850, 40, false, 3, 2));
        edges.add(new Edge(v10, v3, 900, 55, true, 2, 3));


        // Creating the graph
        graph cityGraph = new graph(vertices);
        for (Edge edge : edges) {
            cityGraph.addEdge(edge);
        }

        return cityGraph;
    }
}
