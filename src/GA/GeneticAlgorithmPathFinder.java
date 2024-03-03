package GA;

import GA.Interfaces.IShortestPathFinder;
import cars.Car;
import map.AccidentEvent;
import map.interfaces.IEvents;
import map.interfaces.IMap;
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
    public List<Vertex> findShortestPath(Vertex start, Vertex end, IMap map) throws NoRoutesFoundException {
        int populationSize = Math.min(10, map.getVertices().size() / 2);
        for(int i = 0; i< populationSize; i++)  {
            Population population = new Population();
            List<Vertex> path = population.generateRandomPath(start, end, map);
            paths.add(path);
        }
        List<Double> fitness = new ArrayList<>();
        for (int i = 0; i < paths.size(); i++) {
            fitness.add(PathFitnessCalculator.calculateTotalFuelConsumption(car, paths.get(i), map));
        }
        return evolveToFindShortestPath(start, end, map);
    }

    private List<Vertex> evolveToFindShortestPath(Vertex start, Vertex end, IMap map) throws NoRoutesFoundException {
        IEvents accident = new AccidentEvent();
        int numberOfGenerations = 100;
        List<List<Vertex>> children = paths;

        for (int i = 0; i < numberOfGenerations; i++) {
            int numOfEdges = map.getEdges().size();
            if(numOfEdges == 0){
                throw new NoRoutesFoundException("No routes found between " + start.getId() + " and " + end.getId() + " vertices");
            }
            int tournamentSize = Math.min(3, numOfEdges);
            int numOfParents = Math.min(2, map.getVertices().size() / 2);
            if(numOfParents < 2){
                return children.get(0);
            }
            List<List<Vertex>> tournamentWinners = Selection.tournamentSelection(children, car, map, tournamentSize, numOfParents);
            List<List<Vertex>> elites = Selection.elitism(children, car, map, 1);
            //System.out.println("No routes found between " + start.getId() + " and " + end.getId() + " vertices");

            children = Crossover.crossover(tournamentWinners.get(0),tournamentWinners.get(1));
            for(List<Vertex> vertex : children){
                Mutation.mutate(vertex, 0.1, map);
            }
            children.addAll(elites);
            if(i % 10 == 0){
                accident.GenerateEvent(map);
            }
        }
        List<List<Vertex>> winner = Selection.elitism(children, car, map, 1);
        // Placeholder for GA evolution logic

        return winner.get(0); // This should be replaced with the actual list of vertices forming the shortest path
    }

}
