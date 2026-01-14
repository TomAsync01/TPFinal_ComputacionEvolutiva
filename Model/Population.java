package Model;

import java.util.ArrayList;
import java.util.Random;

/**
 * Clase que representa una población de caminos en un algoritmo genético para el problema del viajante de comercio (TSP).
 */
public class Population {
    private ArrayList<Path> possiblePaths;
    private final double mutProb;
    private final double crossProb;
    private final int generations;

   /* public enum FitnessRegime {
        MUY_SIMILARES,
        VARIACION_MEDIA_ALTA
    }
    */

    public Population(ArrayList<Path> possiblePaths, double mutProb, double crossProb, int generations) {
        this.possiblePaths = possiblePaths;
        this.mutProb = mutProb;
        this.crossProb = crossProb;
        this.generations = generations;
    }

    /**
     * Crea una población mixta de caminos, combinando caminos generados por el método del vecino más cercano y caminos generados aleatoriamente.
     *
     * @param N            Número total de individuos en la población.
     * @param C            Número de ciudades en el problema TSP.
     * @param costs        Matriz de costos entre ciudades.
     * @param mutProb      Probabilidad de mutación.
     * @param crossProb    Probabilidad de cruce.
     * @param generations  Número de generaciones para la evolución.
     * @param porcentajeNN Porcentaje de individuos generados por el método del vecino más cercano.
     * @return Una nueva instancia de Population con la mezcla de individuos.
     */
    public static Population createMixedPopulation(int N, int C, int[][] costs, double mutProb, double crossProb, int generations, double porcentajeNN) {
        int nNN = (int) Math.round(N * porcentajeNN);
        if (nNN > N) nNN = N;
        int nRandom = N - nNN;

        ArrayList<Path> paths = new ArrayList<>(N);
        Random rnd = new Random();

        // 1) individuos generados por vecino más cercano
        for (int i = 0; i < nNN; i++) {
            int startCity = rnd.nextInt(C);
            Path p = Path.createNearestNeighborPath(startCity, costs);
            paths.add(p);
        }

        // 2) individuos generados de forma aleatoria
        for (int i = 0; i < nRandom; i++) {
            Path p = new Path(C, costs);
            paths.add(p);
        }

        return new Population(paths, mutProb, crossProb, generations);
    }

    /** Imprime los caminos de la población */
    public void printPaths() {
        int i = 1;
        for (Path path : possiblePaths) {
            String name = "Path " + i + ": ";
            System.out.println(name + path.toString());
            i++;
        }
    }


    /**
     * Determina el régimen de fitness basado en la variabilidad de los costos de los caminos en la población.
     *
     * @return El régimen de fitness determinado.
     */
    /*
    public FitnessRegime determinarRegimenFitnessPorCosto() {
        if (possiblePaths == null || possiblePaths.isEmpty()) {
            return FitnessRegime.MUY_SIMILARES;
        }

        int n = possiblePaths.size();
        double sum = 0.0;
        double sumSquares = 0.0;

        for (Path p : possiblePaths) {
            double c = p.getPathCost();
            sum += c;
            sumSquares += c * c;
        }

        double mean = sum / n;
        double variance = (sumSquares / n) - (mean * mean);
        if (variance < 0) variance = 0;
        double stdDev = Math.sqrt(variance);

        double cv = (mean != 0.0) ? (stdDev / mean) * 100.0 : 0.0;
        System.out.printf("Los fitness difieren aproximadamente un %.2f%%%n", cv);
        if (cv < 10.0) {
            return FitnessRegime.MUY_SIMILARES;
        } else {
            return FitnessRegime.VARIACION_MEDIA_ALTA;
        }
    }


    /**
     * Aplica la función de fitness a los caminos de la población según el régimen determinado.
     */
    /*
    public void aplicarFitnessSegunRegimen() {
        FitnessRegime regime = determinarRegimenFitnessPorCosto();


        if (regime == FitnessRegime.MUY_SIMILARES) {
            System.out.println("Los costos de los caminos son muy similares. Aplicando transformación lineal al fitness.");
            // 1) calcular fitness base y hallar el peor (mínimo)
            double peorFitness = Double.MAX_VALUE;
            for (Path p : possiblePaths) {
                double fitness = 1.0 / p.getPathCost();
                p.setFitness(fitness);
                if (fitness < peorFitness) {
                    peorFitness = fitness;
                }
            }

            // 2) aplicar transformación lineal f'(i) = f(i) - beta
            for (Path p : possiblePaths) {
                double f = p.getFitness();
                double fTransformado = f - peorFitness;
                p.setFitness(fTransformado);
            }
        } else if (regime == FitnessRegime.VARIACION_MEDIA_ALTA) {
            System.out.println("Los costos de los caminos tienen variación media/alta. Aplicando fitness inverso al costo.");
            for (Path p : possiblePaths) {
                double fitness = 1.0 / p.getPathCost();
                p.setFitness(fitness);
            }
        }
    }
    */

    /** Devuelve una copia de la lista de caminos en la población */
    public ArrayList<Path> getPaths(){
        return new ArrayList<>(possiblePaths);
    }

    /** Devuelve el mejor camino en la población según el fitness */
    public Path getBestPath(){
        if (possiblePaths == null || possiblePaths.isEmpty()) {
            return null;
        }

        Path best = possiblePaths.getFirst();
        for (Path p : possiblePaths) {
            if (p.getFitness() > best.getFitness()) {
                best = p;
            }
        }
        return best;
    }

    /** Devuelve el mejor camino en la población según el fitness */
    public Path getWorstPath(){
        if (possiblePaths == null || possiblePaths.isEmpty()) {
            return null;
        }

        Path worst = possiblePaths.getFirst();
        for (Path p : possiblePaths) {
            if (p.getFitness() < worst.getFitness()) {
                worst = p;
            }
        }
        return worst;
    }

    /** Establece una nueva generación de caminos en la población */
    public void setPaths(ArrayList<Path> nuevaGeneracion) {
        this.possiblePaths = nuevaGeneracion;
    }



}
