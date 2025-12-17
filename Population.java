import java.util.ArrayList;

public class Population {
    private ArrayList<Path> possiblePaths;

    public Population(int populationSize, int numberOfCities, int[][] costs, double mutationProbability, double crossoverProbability, int generations) {
        possiblePaths = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            possiblePaths.add(new Path(numberOfCities, costs));
        }
    }

    public void printPaths() {
        int i = 1;
        for (Path path : possiblePaths) {
            String name = "Path " + i + ": ";
            System.out.println(name + path.toString());
            i++;
        }
    }

    public void printFitnessVariationPercent() {
        if (possiblePaths == null || possiblePaths.isEmpty()) {
            System.out.println("No hay caminos en la población.");
            return;
        }

        int n = possiblePaths.size();
        double sum = 0.0;
        double sumSquares = 0.0;

        for (Path p : possiblePaths) {
            double f = p.getFitness(); // ajusta si tu método se llama distinto
            sum += f;
            sumSquares += f * f;
        }

        double mean = sum / n;
        double variance = (sumSquares / n) - (mean * mean);
        if (variance < 0) variance = 0; // por errores numéricos
        double stdDev = Math.sqrt(variance);

        double variationPercent = (mean != 0.0) ? (stdDev / mean) * 100.0 : 0.0;

        System.out.printf("Los fitness difieren aproximadamente un %.2f%%%n", variationPercent);
    }

}
