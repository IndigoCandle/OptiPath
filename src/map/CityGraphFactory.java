package map;

import java.util.ArrayList;
import java.util.List;

public class CityGraphFactory {

    public static graph createSmallCityGraph() {
        List<Vertex> vertices = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        int idCounter = 0; // To ensure unique IDs for vertices

        // Add vertices for horizontal and vertical streets
        for (int i = 0; i < 5; i++) { // 5 rows
            for (int j = 0; j < 4; j++) { // 4 columns, creating vertices for horizontal streets
                vertices.add(new Vertex(idCounter++, i * 200 + 50, j * 150 + 100));
            }
        }
        for (int i = 0; i < 4; i++) { // 4 rows
            for (int j = 0; j < 5; j++) { // 5 columns, creating vertices for vertical streets
                vertices.add(new Vertex(idCounter++, j * 200 + 100, i * 150 + 50));
            }
        }

        // Initialize the graph with vertices
        graph cityGraph = new graph(vertices);

        // Create and add edges to the graph for horizontal streets
        for (int i = 0; i < 20; i += 4) { // Adjusted for 4 vertices per row in horizontal streets
            for (int j = 0; j < 3; j++) { // Connect only up to the second-last vertex in each row
                Vertex source = vertices.get(i + j);
                Vertex destination = vertices.get(i + j + 1);
                Edge edge = new Edge(source, destination, 200, 50, false, 0, 0);
                edges.add(edge);
                cityGraph.addEdge(edge); // Add edge to the graph
                // Adding reverse edge for bidirectional streets
                Edge reverseEdge = new Edge(destination, source, 200, 50, false, 0, 0);
                edges.add(reverseEdge);
                cityGraph.addEdge(reverseEdge);
            }
        }

        // Create and add edges to the graph for vertical streets
        for (int i = 0; i < 5; i++) { // 5 columns in vertical streets
            for (int j = 0; j < 16; j += 4) { // Increment by 4 to jump to the next row
                Vertex source = vertices.get(i + j);
                Vertex destination = vertices.get(i + j + 4); // Connect down vertically
                Edge edge = new Edge(source, destination, 150, 50, true, 0, 1);
                edges.add(edge);
                cityGraph.addEdge(edge); // Add edge to the graph
                // Adding reverse edge for bidirectional streets
                Edge reverseEdge = new Edge(destination, source, 150, 50, true, 0, 1);
                edges.add(reverseEdge);
                cityGraph.addEdge(reverseEdge);
            }
        }

        return cityGraph;
    }
}
