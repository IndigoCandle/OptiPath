package GA;

import GA.Interfaces.IShortestPathFinder;
import cars.Car;
import com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator;
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
        int numberOfGenerations = 100;
        var children = paths;
        for (int i = 0; i < numberOfGenerations; i++) {
            var tournamentWinners = Selection.tournamentSelection(children, car, map, 3, 2);
            var elites = Selection.elitism(children, car, map, 2);
            int length = Math.min(tournamentWinners.size(), elites.size());
            children = (ArrayList<List<Vertex>>) Crossover.crossover(tournamentWinners.get(0),tournamentWinners.get(1));
            for(var vertex : children){
                Mutation.mutate(vertex, 0.1, map);
            }
            children.addAll(elites);
            // Return the most efficient path found by the GA
        }
        var winner = Selection.elitism(children, car, map, 1);
        // Placeholder for GA evolution logic

        return null; // This should be replaced with the actual list of vertices forming the shortest path
    }
}
