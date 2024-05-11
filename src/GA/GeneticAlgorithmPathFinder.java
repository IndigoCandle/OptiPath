package GA;

import GA.Interfaces.IFitnessCalculator;
import GA.Interfaces.IShortestPathFinder;
import cars.Car;
import map.Events.AccidentEvent;
import map.Edge;
import map.interfaces.IEvents;
import map.interfaces.IMap;
import map.Vertex;
import java.util.ArrayList;
import java.util.List;


/**
 * This class implements a genetic algorithm to find the shortest path between two vertices in a graph.
 * It is capable of handling dynamic changes such as accidents on the edges of the graph.
 */
public class GeneticAlgorithmPathFinder implements IShortestPathFinder {
    private static final int MAX_GENERATIONS = 200;
    private static final int MAX_ELITE_COUNT = 20;
    private static final double MUTATION_RATE = 0.1;
    private static final int ACCIDENT_GENERATION_INTERVAL = 10;

    private final IEvents accident;
    private List<List<Vertex>> paths;
    private List<List<Vertex>> children;
    private final Car car;
    private final IFitnessCalculator fitnessCalculator;
    private final List<Edge> accidents;
    private final Population population;
    private Vertex end;
    public List<Double> recordFuel;

    /**
     * Constructs a pathfinder with a specified car and fitness calculator.
     *
     * @param car              The car to use in the pathfinding algorithm.
     * @param fitnessCalculator The fitness calculator to determine the path's fitness.
     */
    public GeneticAlgorithmPathFinder(Car car, IFitnessCalculator fitnessCalculator) {
        this.car = car;
        this.fitnessCalculator = fitnessCalculator;
        accidents = new ArrayList<>();
        accident = new AccidentEvent();
        population = new Population();
        children = new ArrayList<>();
        paths = new ArrayList<>();
        recordFuel = new ArrayList<>();
    }

    /**
     * Finds the shortest path between the start and end vertices using a genetic algorithm.
     *
     * @param start The starting vertex.
     * @param end   The ending vertex.
     * @return The list of vertices forming the shortest path.
     * @throws NoRoutesFoundException If no path is found between the start and end vertices.
     */
    @Override
    public List<Vertex> findShortestPath(Vertex start, Vertex end, IMap map) throws NoRoutesFoundException {
        populatePaths(start, end, map);
        return evolveToFindShortestPath(start, end, map);
    }

    /**
     * Populates initial paths between the start and end vertices.
     *
     * @param start The starting vertex.
     * @param end   The ending vertex.
     * @param map   The graph in which the vertices exist.
     * @throws NoRoutesFoundException If no initial paths can be found.
     */
    private void populatePaths(Vertex start, Vertex end, IMap map) throws NoRoutesFoundException {
        this.end = end;
        int populationSize = Math.min(10, (int) Math.ceil((double) map.getEdges().size() / 2));
        for (int i = 0; i < populationSize; i++) {
            List<Vertex> path = population.generateRandomPathBacktrack(start, end, map);
            if (!path.isEmpty()) {
                paths.add(path);
            }
        }
        if (paths.isEmpty()) {
            throw new NoRoutesFoundException("No routes found between " + start.getId() + " and " + end.getId() + " vertices");
        }
    }

    /**
     * Evolves the population to find the shortest path.
     *
     * @param start The starting vertex.
     * @param end   The ending vertex.
     * @param map   The map where the vertices and edges are located.
     * @return The shortest path found after evolving the population.
     * @throws NoRoutesFoundException If no routes are found after evolving.
     */
    private List<Vertex> evolveToFindShortestPath(Vertex start, Vertex end, IMap map) throws NoRoutesFoundException {
        int eliteCount = 0;
        children = paths;
        List<Vertex> prevElite = new ArrayList<>();

        for (int i = 0; i < MAX_GENERATIONS && eliteCount < MAX_ELITE_COUNT; i++) {
            validateEdgesOrThrow(start, end, map.getEdges().size());

            int tournamentSize = Math.min(3, children.size());
            int numOfParents = calculateNumOfParents(map);
            if (numOfParents < 2) {
                recordFuel.add(fitnessCalculator.calculateFitness(car,children.get(0), map));
                return children.get(0);
            }

            List<List<Vertex>> tournamentWinners = Selection.tournamentSelection(children, car, map, tournamentSize, numOfParents, fitnessCalculator);
            List<List<Vertex>> elites = runElitism(children, map, numOfParents);
            eliteCount = updateEliteCount(elites, prevElite, eliteCount);
            recordFuel.add(fitnessCalculator.calculateFitness(car,elites.get(elites.size()-1), map));
            prevElite = elites.get(0);
            children = performCrossover(tournamentWinners);
            mutateChildren(children, map);
            triggerAccidents(i, map);
            if(children.isEmpty()){
                populatePaths(start,end,map);
                if(!paths.isEmpty())
                    children = paths;
                else
                    throw new NoRoutesFoundException("No routes found between " + start.getId() + " and " + end.getId() + " vertices");
            }

            if(fitnessCalculator.calculateFitness(car, selectFinalWinner(children, map), map) == Double.MAX_VALUE){
                List<Vertex> newPath = population.generateRandomPathBacktrack(start,end,map);
                if(newPath.isEmpty()){
                    throw new NoRoutesFoundException("No routes found between " + start.getId() + " and " + end.getId() + " vertices");
                }
                children.add(newPath);
            }
        }
        recordFuel.add(fitnessCalculator.calculateFitness(car,selectFinalWinner(children,map), map));
        return selectFinalWinner(children, map);
    }

    /**
     * Validates that there are edges in the map. Throws an exception if there are no edges.
     *
     * @param start        The start vertex.
     * @param end          The end vertex.
     * @param numOfEdges   The number of edges in the map.
     * @throws NoRoutesFoundException If no routes exist in the map.
     */
    private void validateEdgesOrThrow(Vertex start, Vertex end, int numOfEdges) throws NoRoutesFoundException {
        if (numOfEdges == 0) {
            throw new NoRoutesFoundException("No routes found between " + start.getId() + " and " + end.getId() + " vertices");
        }
    }

    /**
     * Performs crossover operation on a pair of parents to produce new children for the next generation.
     *
     * @param tournamentWinners The parents selected for crossover.
     * @return The children resulting from the crossover.
     */
    private List<List<Vertex>> performCrossover(List<List<Vertex>> tournamentWinners) {
        if (tournamentWinners.size() < 2) {
            return new ArrayList<>(tournamentWinners);
        }
        return Crossover.crossover(tournamentWinners.get(0), tournamentWinners.get(1));
    }


    /**
     * Mutates the given paths based on a predefined mutation rate.
     *
     * @param children The list of paths (children) to mutate.
     * @param map      The map providing the context for the mutations.
     */
    private void mutateChildren(List<List<Vertex>> children, IMap map) {
        for (List<Vertex> path : children) {
            Mutation.mutate(path, MUTATION_RATE, map);
        }
    }

    /**
     * Triggers accidents at specified intervals and updates paths affected by those accidents.
     *
     * @param generation The current generation number in the genetic algorithm cycle.
     * @param map        The map where the vertices and edges are located.
     */
    private void triggerAccidents(int generation, IMap map) {
        if (generation % ACCIDENT_GENERATION_INTERVAL == 0) {
            List<Edge> affectedEdges = accident.GenerateEvent(map, paths);
            accidents.addAll(affectedEdges);
            updatePathsAfterAccidents(affectedEdges, map);
        }
    }

    /**
     * Updates the list of paths based on the edges affected by accidents. It attempts to reroute affected paths
     * to avoid the impacted areas.
     *
     * @param affectedEdges The list of edges that have been affected by recent accidents.
     * @param map           The map providing context for pathfinding and edge data.
     */
    private void updatePathsAfterAccidents(List<Edge> affectedEdges, IMap map) {
        List<List<Vertex>> validPaths = new ArrayList<>();
        for (List<Vertex> path : paths) {
            if (!isPathAffected(path, map, affectedEdges)) {
                validPaths.add(path);
            } else {
                List<Vertex> newPath = findNewPath(path, map, affectedEdges);
                validPaths.add(newPath);
            }
        }
        paths = validPaths;
    }

    /**
     * Checks if a given path is affected by any recently occurred accidents.
     *
     * @param path          The path to check for impacts.
     * @param map           The map containing all vertex and edge data.
     * @param affectedEdges A list of edges that were affected by accidents.
     * @return true if the path is affected by any accidents, false otherwise.
     */
    private boolean isPathAffected(List<Vertex> path, IMap map, List<Edge> affectedEdges) {
        for (int i = 1; i < path.size(); i++) {
            if (affectedEdges.contains(map.getEdgeBetween(path.get(i), path.get(i - 1)))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Attempts to reconstruct a path starting from the last unaffected vertex up to the designated endpoint.
     *
     * @param oldPath       The original path that was partially affected by an accident.
     * @param map           The map used to find new paths and check connections.
     * @param affectedEdges The list of edges that have been affected by accidents.
     * @return A new path that avoids the affected edges.
     */
    private List<Vertex> findNewPath(List<Vertex> oldPath, IMap map, List<Edge> affectedEdges) {
        boolean breakFlag = false;
        List<Vertex> newPath = new ArrayList<>();
        for (int i = 1; i < oldPath.size() && !breakFlag; i++) {
            if (!affectedEdges.contains(map.getEdgeBetween(oldPath.get(i), oldPath.get(i - 1)))) {
                newPath.add(oldPath.get(i - 1));
            } else {
                breakFlag = true;
            }
        }
        if (!newPath.isEmpty()) {
            newPath.addAll(population.generateRandomPathBacktrack(newPath.get(newPath.size() - 1), end, map));
        }
        return newPath;
    }



    /**
     * Retrieves the list of accidents that have occurred on the map.
     *
     * @return A list of edges where accidents have occurred.
     */
    public List<Edge> getAccidents() {
        return accidents;
    }

    /**
     * Runs the elitism selection algorithm to select the best paths in the population.
     *
     * @param children     The current population of paths.
     * @param map          The map used for calculating fitness.
     * @param numOfParents The number of top paths to select.
     * @return A list containing the elite paths.
     */
    private List<List<Vertex>> runElitism(List<List<Vertex>> children, IMap map, int numOfParents) {
        return Selection.elitism(children, car, map, numOfParents, fitnessCalculator);
    }

    /**
     * Updates the count of consecutive generations where the best path has not changed.
     *
     * @param elites       The current elite paths.
     * @param prevElite    The elite path from the previous generation.
     * @param eliteCount   The current count of generations without change.
     * @return The updated elite count.
     */
    private int updateEliteCount(List<List<Vertex>> elites, List<Vertex> prevElite, int eliteCount) {
        if (!prevElite.isEmpty() && elites.get(0).equals(prevElite)) {
            eliteCount++;
        } else {
            eliteCount = 0;
        }
        return eliteCount;
    }

    /**
     * Selects the final winner after all generations have been processed.
     *
     * @param children The last generation of paths to select from.
     * @param map      The map used for calculating fitness.
     * @return The winning path.
     */
    private List<Vertex> selectFinalWinner(List<List<Vertex>> children, IMap map) {
        return runElitism(children, map, 1).get(0);
    }

    /**
     * Calculates the number of parents to be selected for creating the next generation.
     *
     * @param map The map providing the context for the calculation.
     * @return The number of parents.
     */
    private int calculateNumOfParents(IMap map) {
        return Math.min(2, (int) Math.ceil((double) map.getEdges().size() / 2));
    }
}
