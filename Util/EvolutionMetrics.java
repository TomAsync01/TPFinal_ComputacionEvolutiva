package Util;

import Model.Population;
import Model.Path;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para rastrear y almacenar métricas de evolución de una población a lo largo de generaciones.
 */
public class EvolutionMetrics {
    private List<Double> bestFitnessList = new ArrayList<>();
    private List<Double> diversityList = new ArrayList<>();
    private List<Integer> generationList = new ArrayList<>();
    private List<Long> timeList = new ArrayList<>();
    private long startTime;

    /** Inicia el seguimiento del tiempo de evolución */
    public void startTracking() {
        startTime = System.currentTimeMillis();
    }

    /** Registra las métricas de una generación dada */
    public void recordGeneration(int generation, Population population) {
        generationList.add(generation);

        double best = population.getBestPath().getFitness();
        double worst = population.getWorstPath().getFitness();
        double diversity = calculateDiversity(population);

        bestFitnessList.add(best);
        diversityList.add(diversity);
        timeList.add(System.currentTimeMillis() - startTime);
    }


    /** Calcula la diversidad de la población basada en la distancia promedio entre caminos */
    private double calculateDiversity(Population population) {
        List<Path> paths = (List<Path>) population.getPaths();
        int n = paths.size();
        double sumDistances = 0.0;
        int comparisons = 0;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                sumDistances += calculatePathDistance(paths.get(i), paths.get(j));
                comparisons++;
            }
        }

        return comparisons > 0 ? sumDistances / comparisons : 0.0;
    }

    /** Calcula la diversidad promedio de todas las generaciones */
    public double getAverageDiversity() {
        if (diversityList.isEmpty()) return 0.0;
        return diversityList.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    /** Calcula la distancia entre dos caminos como la proporción de ciudades en posiciones diferentes */
    private double calculatePathDistance(Path p1, Path p2) {
        int differences = 0;
        List<Integer> path1 = p1.getCities();
        List<Integer> path2 = p2.getCities();

        for (int i = 0; i < path1.size(); i++) {
            if (!path1.get(i).equals(path2.get(i))) {
                differences++;
            }
        }
        return (double) differences / path1.size();
    }

    /** Obtiene el mejor fitness de la generación final */
    public double getFinalBestFitness() {
        return bestFitnessList.isEmpty() ? 0.0 : bestFitnessList.getLast();
    }


    public List<Double> getBestFitnessList() { return bestFitnessList; }
    public List<Double> getDiversityList() { return diversityList; }
    public List<Integer> getGenerationList() { return generationList; }
}
