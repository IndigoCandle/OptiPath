package GA;

import cars.Car;
import javafx.util.Pair;
import map.interfaces.IMap;
import map.Vertex;

import java.util.*;
import java.util.stream.Collectors;

public class Selection {

// Roulette wheel selection to select parents for crossover
    public static List<List<Vertex>> rouletteWheelSelection(List<List<Vertex>> population, Car car, IMap map, int numParents) {
        List<Pair<List<Vertex>, Double>> routeFitnessPairs = population.stream()
                .map(route -> new Pair<>(route, PathFitnessCalculator.calculateTotalFuelConsumption(car, route, map)))
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

            List<Vertex> bestPath = tournamentParticipants.get(0);
            double bestFitness = PathFitnessCalculator.calculateTotalFuelConsumption(car, bestPath, map);
            for (List<Vertex> path : tournamentParticipants) {
                double currentFitness = PathFitnessCalculator.calculateTotalFuelConsumption(car, path, map);
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


    public static List<List<Vertex>> elitism(List<List<Vertex>> population, Car car, IMap map, int numberOfElites) {
        List<Pair<List<Vertex>, Double>> routeFitnessPairs = population.stream()
                .map(route -> new Pair<>(route, PathFitnessCalculator.calculateTotalFuelConsumption(car, route, map)))
                .collect(Collectors.toList());

        routeFitnessPairs.sort(Comparator.comparing(Pair::getValue));


        return routeFitnessPairs.stream()
                .limit(numberOfElites)
                .map(Pair::getKey)
                .collect(Collectors.toList());
    }
}
