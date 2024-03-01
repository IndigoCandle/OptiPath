package GA;

import cars.Car;
import map.IMap;
import map.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
        // Create a map of routes to their fitness values
        Map<List<Vertex>, Double> fitnessMap = population.stream()
                .collect(Collectors.toMap(route -> route, route -> PathFitnessCalculator.calculateTotalFuelConsumption(car, route, map)));

        // Sort the population based on the fitness values (lower fuel consumption is better)
        List<List<Vertex>> sortedPopulation = population.stream()
                .sorted((route1, route2) -> fitnessMap.get(route1).compareTo(fitnessMap.get(route2)))
                .collect(Collectors.toList());

        // Return the top N fittest routes
        return new ArrayList<>(sortedPopulation.subList(0, Math.min(numberOfElites, sortedPopulation.size())));
    }
}
