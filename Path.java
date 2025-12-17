// java
import java.util.ArrayList;
import java.util.Random;

public class Path {
    private final ArrayList<Integer> cities;
    private int pathCost;
    private double fitness;
    private final Random random = new Random();

    public Path(int numberOfCities, int[][] costs) {
        if (numberOfCities <= 0) {
            throw new IllegalArgumentException("numberOfCities must be > 0");
        }
        if (costs == null || costs.length != numberOfCities) {
            throw new IllegalArgumentException("costs must be a square matrix with size numberOfCities");
        }
        for (int i = 0; i < numberOfCities; i++) {
            if (costs[i] == null || costs[i].length != numberOfCities) {
                throw new IllegalArgumentException("costs must be a square matrix with size numberOfCities");
            }
        }

        cities = new ArrayList<>(numberOfCities);
        for (int i = 1; i <= numberOfCities; i++) {
            cities.add(i);
        }
        shuffleCities();
        calculateCost(costs);
    }

    private void shuffleCities() {
        for (int i = cities.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int tmp = cities.get(i);
            cities.set(i, cities.get(j));
            cities.set(j, tmp);
        }
    }

    private void calculateCost(int[][] costs) {
        int total = 0;
        for (int i = 0; i < cities.size() - 1; i++) {
            int from = cities.get(i) - 1;
            int to = cities.get(i + 1) - 1;
            total += costs[from][to];
        }
        // Cerrar el ciclo: volver a la primera ciudad
        if (!cities.isEmpty()) {
            int last = cities.get(cities.size() - 1) - 1;
            int first = cities.get(0) - 1;
            total += costs[last][first];
        }
        pathCost = total;
        fitness = 1.0 / pathCost;
    }

    public int getPathCost() {
        return pathCost;
    }

    public double getFitness() {
        return fitness;
    }

    @Override
    public String toString() {
        String fitnessStr = String.format("%.8f", fitness);

        return cities.toString() + " | Cost: " + pathCost + "  | Fitness: " + fitnessStr;
    }
}
