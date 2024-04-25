package GA;

import GA.Interfaces.IFitnessCalculator;
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
    private static final int MAX_GENERATIONS = 100;
    private static final int MAX_ELITE_COUNT = 7;
    private static final double MUTATION_RATE = 0.1;
    private static final int ACCIDENT_GENERATION_INTERVAL = 10;
    private IEvents accident;
    private List<List<Vertex>> paths = new ArrayList<>();
    private Car car;
    private IFitnessCalculator fitnessCalculator;
    private List<Edge> accidents;
    public GeneticAlgorithmPathFinder(Car car, IFitnessCalculator fitnessCalculator) {
        this.car = car;
        this.fitnessCalculator = fitnessCalculator;
        accidents = new ArrayList<>();
        accident = new AccidentEvent();
    }



    @Override
    public List<Vertex> findShortestPath(Vertex start, Vertex end, IMap map) throws NoRoutesFoundException {
        Populate(start, end, map);
        return evolveToFindShortestPath(start, end, map);
    }

    private void Populate(Vertex start, Vertex end, IMap map) throws NoRoutesFoundException {
        int populationSize = Math.min(10, (int)Math.ceil((double)map.getEdges().size() / 2));
        Population population = new Population();
        for(int i = 0; i< populationSize; i++)  {
            List<Vertex> path = population.generateRandomPathBacktrack(start, end, map);
            if(!path.isEmpty()){
                paths.add(path);
            }

        }
        if(paths.isEmpty()){
            throw new NoRoutesFoundException("No routes found between " + start.getId() + " and " + end.getId() + " vertices");
        }
    }



    private List<Vertex> evolveToFindShortestPath(Vertex start, Vertex end, IMap map) throws NoRoutesFoundException {
        int EliteCount = 0;
        List<List<Vertex>> children = paths;
        List<List<Vertex>> tournamentWinners;
        List<List<Vertex>> elites;
        List<Vertex> prevElite = new ArrayList<>();
        for (int i = 0; i < MAX_GENERATIONS && EliteCount < MAX_ELITE_COUNT; i++) {
            int numOfEdges = map.getEdges().size();
            validEdges(start,end,numOfEdges);

            int tournamentSize = Math.min(3, children.size());

            int numOfParents = Math.min(2, (int)Math.ceil((double) numOfEdges / 2));

            if(numOfParents < 2){
                return children.get(0);
            }
            tournamentWinners = Selection.tournamentSelection(children, car, map, tournamentSize, numOfParents, fitnessCalculator);
            elites = RunElitism(children, map, numOfParents);
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
                Mutation.mutate(vertex, MUTATION_RATE, map);
            }
            children.addAll(elites);
            Accidents(i,map);
        }

        return RunElitism(children, map, 1).get(0);
    }

    private void validEdges(Vertex start, Vertex end, int numOfEdges) throws NoRoutesFoundException {
        if(numOfEdges == 0){
            throw new NoRoutesFoundException("No routes found between " + start.getId() + " and " + end.getId() + " vertices");
        }
    }

    private void Accidents(int round, IMap map){
        if(round % ACCIDENT_GENERATION_INTERVAL == 0){
            accidents.addAll(accident.GenerateEvent(map));
        }
    }
    public List<Edge> getAccidents() {
        return accidents;
    }

    private List<List<Vertex>> RunElitism(List<List<Vertex>> children, IMap map, int numOfParents){
        return Selection.elitism(children, car, map, numOfParents, fitnessCalculator);
    }
}