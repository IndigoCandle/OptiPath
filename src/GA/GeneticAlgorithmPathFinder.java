package GA;

import GA.Interfaces.IShortestPathFinder;
import cars.Car;
import map.AccidentEvent;
import map.Edge;
import map.interfaces.IEvents;
import map.interfaces.IMap;
import map.Vertex;
import java.util.ArrayList;
import java.util.List;



public class GeneticAlgorithmPathFinder implements IShortestPathFinder {
    private List<List<Vertex>> paths = new ArrayList<>();
    private Car car;
    private List<Edge> accidents;
    public GeneticAlgorithmPathFinder(Car car) {
        this.car = car;
        accidents = new ArrayList<>();
    }

    

    @Override
    public List<Vertex> findShortestPath(Vertex start, Vertex end, IMap map) throws NoRoutesFoundException {

        int populationSize = Math.min(10, (int)Math.ceil((double)map.getEdges().size() / 2));
        Population population = new Population();
        for(int i = 0; i< populationSize; i++)  {
            List<Vertex> path = population.generateRandomPathBacktrack(start, end, map);
            paths.add(path);
        }
        List<Double> fitness = new ArrayList<>();
        for (List<Vertex> path : paths) {
            fitness.add(PathFitnessCalculator.calculateTotalFuelConsumption(car, path, map));
        }
        return evolveToFindShortestPath(start, end, map);
    }

    private List<Vertex> evolveToFindShortestPath(Vertex start, Vertex end, IMap map) throws NoRoutesFoundException {
        IEvents accident = new AccidentEvent();
        int numberOfGenerations = 100;
        List<List<Vertex>> children = paths;
        int EliteCount = 0;
        List<Vertex> prevElite = new ArrayList<>();
        for (int i = 0; i < numberOfGenerations && EliteCount < 7; i++) {
            int numOfEdges = map.getEdges().size();
            if(numOfEdges == 0){
                throw new NoRoutesFoundException("No routes found between " + start.getId() + " and " + end.getId() + " vertices");
            }
            int tournamentSize = Math.min(3, children.size());
            int numOfParents = Math.min(2, (int)Math.ceil((double) numOfEdges / 2));
            if(numOfParents < 2){
                return children.get(0);
            }
            List<List<Vertex>> tournamentWinners = Selection.tournamentSelection(children, car, map, tournamentSize, numOfParents);
            List<List<Vertex>> elites = Selection.elitism(children, car, map, numOfParents);
            if(!prevElite.isEmpty()){
                if(elites.get(0).equals(prevElite)){
                    EliteCount++;
                }
                else{
                    EliteCount = 0;
                }
            }
            prevElite = elites.get(0);
            if(tournamentWinners.size() == 1){
                return tournamentWinners.get(0);
            }
            children = Crossover.crossover(tournamentWinners.get(0),tournamentWinners.get(1));
            for(List<Vertex> vertex : children){
                Mutation.mutate(vertex, 0.1, map);
            }
            children.addAll(elites);
            if(i % 10 == 0){
                accidents.addAll(accident.GenerateEvent(map));
            }
        }
        List<List<Vertex>> winner = Selection.elitism(children, car, map, 1);

        return winner.get(0);
    }

    public List<Edge> getAccidents() {
        return accidents;
    }
}
