package GA;

import GA.Interfaces.IShortestPathFinder;
import cars.Car;
import map.IMap;
import map.Vertex;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.max;

public class GeneticAlgorithmPathFinder implements IShortestPathFinder {
    private List<List<Vertex>> paths = new ArrayList<>();
    private Car car;
    public GeneticAlgorithmPathFinder(Car car) {
        this.car = car;
    }

    @Override
    public List<Vertex> findShortestPath(Vertex start, Vertex end, IMap map) {
        // Implementation of the GA to find the shortest path
        for(int i = 0; i< 10; i++)  {
            Population population = new Population();
            List<Vertex> path = population.generateRandomPath(start, end, map);
            paths.add(path);
        }
        List<Double> fitness = new ArrayList<>();
        for (int i = 0; i < paths.size(); i++) {
            fitness.add(PathFitnessCalculator.calculateTotalFuelConsumption(car, paths.get(i), map));
        }
// Now minIndex points to the path with the least fuel consumption
        //List<Vertex> mostEfficientPath = paths.get(minIndex);
        // This would involve initializing a population of possible paths,

        // evaluating them based on a fitness function (e.g., distance, fuel consumption),
        // and using genetic operators to evolve the population towards an optimal solution.

        // Placeholder for the actual GA logic
        return evolveToFindShortestPath(start, end, map);
    }

    private List<Vertex> evolveToFindShortestPath(Vertex start, Vertex end, IMap map) {
        // Placeholder for GA evolution logic
        var tournamentWinners = Selection.tournamentSelection(paths, car, map, 3);
        var elites = Selection.elitism(paths, car, map, 2);
        int length = Math.min(tournamentWinners.size(), elites.size());


        // Return the most efficient path found by the GA
        return null; // This should be replaced with the actual list of vertices forming the shortest path
    }
}
