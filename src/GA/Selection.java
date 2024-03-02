package GA;

import cars.Car;
import javafx.util.Pair;
import map.IMap;
import map.Vertex;

import java.util.*;
import java.util.stream.Collectors;

public class Selection {

    // Tournament selection to select parents for crossover
    public static List<List<Vertex>> tournamentSelection(List<List<Vertex>> population, Car car, IMap map, int tournamentSize, int numParents) {
        Random random = new Random();
        List<List<Vertex>> selectedParents = new ArrayList<>();
        while (selectedParents.size() < numParents) {
            List<List<Vertex>> tournamentParticipants = new ArrayList<>();
            for (int i = 0; i < tournamentSize; i++) {
                int randomIndex = random.nextInt(population.size());
                tournamentParticipants.add(population.get(randomIndex));
            }

            // Determine the best path in the tournament
            List<Vertex> bestPath = tournamentParticipants.get(0);
            double bestFitness = PathFitnessCalculator.calculateTotalFuelConsumption(car, bestPath, map);
            for (List<Vertex> path : tournamentParticipants) {
                double currentFitness = PathFitnessCalculator.calculateTotalFuelConsumption(car, path, map);
                if (currentFitness < bestFitness) { // Assuming lower fuel consumption is better
                    bestPath = path;
                    bestFitness = currentFitness;
                }
            }
            // Add the best path from this tournament to the selected parents
            if (!selectedParents.contains(bestPath)) { // Check to avoid duplicates
                selectedParents.add(bestPath);
            }
        }
        return selectedParents;
    }


    // Method to implement elitism
    public static List<List<Vertex>> elitism(List<List<Vertex>> population, Car car, IMap map, int numberOfElites) {
        // Create a list of route-fitness pairs
        List<Pair<List<Vertex>, Double>> routeFitnessPairs = population.stream()
                .map(route -> new Pair<>(route, PathFitnessCalculator.calculateTotalFuelConsumption(car, route, map)))
                .collect(Collectors.toList());

        // Sort the pairs by fitness value
        routeFitnessPairs.sort(Comparator.comparing(Pair::getValue));

        // Extract the top N routes based on their fitness
        return routeFitnessPairs.stream()
                .limit(numberOfElites)
                .map(Pair::getKey)
                .collect(Collectors.toList());
    }
}
