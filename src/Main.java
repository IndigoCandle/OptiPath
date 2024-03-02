/*
import GA.GeneticAlgorithmPathFinder;
import cars.Car;
import map.Edge;
import map.Vertex;
import map.graph;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Initialize the map (graph)
         // Assuming the graph class has appropriate constructors and methods.

        // Add vertices
        Vertex v1 = new Vertex(1, 100, 100);
        Vertex v2 = new Vertex(2, 300, 200);
        Vertex v3 = new Vertex(3, 500, 100);
        Vertex v4 = new Vertex(4, 600, 200);
        Vertex v5 = new Vertex(5, 700, 100);
        Vertex v6 = new Vertex(6, 200, 300);
        Vertex v7 = new Vertex(7, 400, 400);
        Vertex v8 = new Vertex(8, 500, 500);
        Vertex v9 = new Vertex(9, 300, 600);
        Vertex v10 = new Vertex(10, 100, 500);

        // Continue adding more vertices as needed
        List<Vertex> vertices = new ArrayList<>();
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
        v1.addEdge(new Edge(v1, v2, calculateDistance(v1, v2), 50, 1, false, 0, 0));
        // Add more vertices to the map
        v2.addEdge(new Edge(v2, v3, calculateDistance(v2, v3), 50, 1, false, 0, 0));
        // Add edges
        v3.addEdge(new Edge(v3, v1, calculateDistance(v3, v1), 50, 1, false, 0, 0));
        v1.addEdge(new Edge(v1, v3, calculateDistance(v1, v3), 5, 30, false, 0, 0));
        v3.addEdge(new Edge(v3, v4, calculateDistance(v3, v4), 50, 1, false, 0, 0));
        v1.addEdge(new Edge(v1, v6, 3, 60, 1, false, 0, 0));
        v5.addEdge(new Edge(v5, v1, calculateDistance(v5, v1), 50, 1, false, 0, 0)); // Connecting back to v1
        v2.addEdge(new Edge(v2, v6, calculateDistance(v2, v6), 50, 1, false, 0, 0));
        v6.addEdge(new Edge(v6, v3, 3, 60, 1, false, 0, 0));
        v7.addEdge(new Edge(v7, v8, calculateDistance(v7, v8), 50, 1, false, 0, 0));
        v8.addEdge(new Edge(v8, v9, calculateDistance(v8, v9), 50, 1, false, 0, 0));
        v9.addEdge(new Edge(v9, v10, calculateDistance(v9, v10), 50, 1, false, 0, 0));
        v10.addEdge(new Edge(v10, v1, calculateDistance(v10, v1), 50, 1, false, 0, 0)); // Connecting back to v1
        // Continue adding more edges as needed, including elevation change and stops if applicable
        graph map = new graph(vertices);
        // Initialize the car with optimal parameters
        Car car = new Car("Car1", 60, 20); // ID, bestFuelConsumptionSpeed, fuelEfficiency

        // Run the GA solver to find the most efficient path
        GeneticAlgorithmPathFinder gaSolver = new GeneticAlgorithmPathFinder(car);
        List<Vertex> efficientPath = gaSolver.findShortestPath(v1, v3, map);

        // Output the efficient path found
        System.out.println("Most efficient path found:");
        efficientPath.forEach(vertex -> System.out.println(vertex.getId()));
    }

    // Helper method to calculate the distance between two vertices
    private static double calculateDistance(Vertex v1, Vertex v2) {
        return Math.sqrt(Math.pow(v1.getX() - v2.getX(), 2) + Math.pow(v1.getY() - v2.getY(), 2));
    }
}
*/
