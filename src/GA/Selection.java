package GA;

import GA.Interfaces.IFitnessCalculator;
import cars.Car;
import javafx.util.Pair;
import map.interfaces.IMap;
import map.Vertex;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class provides methods to perform selection in a genetic algorithm context.
 * It supports various selection techniques to choose parents for reproduction.
 */
public class Selection {

    /**
     * Performs roulette wheel selection to choose parents based on their fitness proportion.
     *
     * @param population         The current population of paths.
     * @param car                The car used for fitness calculations.
     * @param map                The map over which the paths are defined.
     * @param numParents         The number of parents to select.
     * @param fitnessCalculator  The fitness calculator to determine the fitness of each path.
     * @return A list of selected parent paths for the next generation.
     */
    public static List<List<Vertex>> rouletteWheelSelection(List<List<Vertex>> population, Car car, IMap map, int numParents, IFitnessCalculator fitnessCalculator) {
        List<Pair<List<Vertex>, Double>> routeFitnessPairs = population.stream()
                .map(route -> new Pair<>(route, fitnessCalculator.calculateFitness(car, route, map)))
                .collect(Collectors.toList());

        double totalFitness = routeFitnessPairs.stream()
                .mapToDouble(Pair::getValue)
                .sum();

        List<List<Vertex>> selectedParents = new ArrayList<>();
        Random random = new Random();
        while (selectedParents.size() < numParents) {
            double randomFitness = random.nextDouble() * totalFitness;
            double currentSum = 0;
            for (Pair<List<Vertex>, Double> pair : routeFitnessPairs) {
                currentSum += pair.getValue();
                if (currentSum > randomFitness) {
                    selectedParents.add(pair.getKey());
                    break;
                }
            }
        }
        return selectedParents;
    }

    /**
     * Performs tournament selection to choose parents from the population.
     * A subset of the population competes, and the path with the best fitness is selected as a parent.
     *
     * @param population         The current population of paths.
     * @param car                The car used for fitness calculations.
     * @param map                The map over which the paths are defined.
     * @param tournamentSize     The number of participants in each tournament.
     * @param numParents         The number of parents to select.
     * @param fitnessCalculator  The fitness calculator to determine the fitness of each path.
     * @return A list of selected parent paths for the next generation.
     */
    public static List<List<Vertex>> tournamentSelection(List<List<Vertex>> population, Car car, IMap map,
                                                         int tournamentSize, int numParents, IFitnessCalculator fitnessCalculator) {
        Random random = new Random();
        List<List<Vertex>> selectedParents = new ArrayList<>();
        while (selectedParents.size() < numParents) {
            List<List<Vertex>> tournamentParticipants = new ArrayList<>();
            for (int i = 0; i < tournamentSize; i++) {
                tournamentParticipants.add(population.get(random.nextInt(population.size())));
            }

            List<Vertex> bestPath = tournamentParticipants.get(0);
            double bestFitness = fitnessCalculator.calculateFitness(car, bestPath, map);
            for (List<Vertex> path : tournamentParticipants) {
                double currentFitness = fitnessCalculator.calculateFitness(car, path, map);
                if (currentFitness < bestFitness) {
                    bestPath = path;
                    bestFitness = currentFitness;
                }
            }
            if (!selectedParents.contains(bestPath)) {
                selectedParents.add(bestPath);
            }
        }
        return selectedParents;
    }

    /**
     * Performs elitism selection, where the best paths from the population are carried over to the next generation.
     *
     * @param population         The current population of paths.
     * @param car                The car used for fitness calculations.
     * @param map                The map over which the paths are defined.
     * @param numberOfElites     The number of top paths to carry over.
     * @param fitnessCalculator  The fitness calculator to determine the fitness of each path.
     * @return A list of the elite paths.
     */
    public static List<List<Vertex>> elitism(List<List<Vertex>> population, Car car, IMap map, int numberOfElites, IFitnessCalculator fitnessCalculator) {
        List<Pair<List<Vertex>, Double>> routeFitnessPairs = population.stream()
                .map(route -> new Pair<>(route, fitnessCalculator.calculateFitness(car, route, map)))
                .sorted(Comparator.comparing(Pair::getValue))
                .collect(Collectors.toList());

        return routeFitnessPairs.stream()
                .limit(numberOfElites)
                .map(Pair::getKey)
                .collect(Collectors.toList());
    }
}
