package GA.Interfaces;

import cars.Car;
import map.Vertex;
import map.interfaces.IMap;

import java.util.List;

public interface IFitnessCalculator {
      double calculateFitness(Car car, List<Vertex> path, IMap map);

}
