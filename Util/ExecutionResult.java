package Util;

import java.util.ArrayList;
import java.util.List;

//Clase que almacena los resultados de una ejecución que se utilizarán para crear el .json
public class ExecutionResult {
    private final double finalBestFitness;
    private final int bestCost;
    private final long executionTime;
    private final List<Double> fitnessEvolution;
    private final ArrayList<Integer> bestSolution;

    public ExecutionResult(double finalBestFitness, int bestCost, long executionTime,
                           List<Double> fitnessEvolution, ArrayList<Integer> bestSolution) {
        this.finalBestFitness = finalBestFitness;
        this.bestCost = bestCost;
        this.executionTime = executionTime;
        this.fitnessEvolution = fitnessEvolution;
        this.bestSolution = bestSolution;
    }

    public double getFinalBestFitness() { return finalBestFitness; }
    public int getBestCost() { return bestCost; }
    public long getExecutionTime() { return executionTime; }
    public List<Double> getFitnessEvolution() { return fitnessEvolution; }
    public ArrayList<Integer> getBestSolution() { return bestSolution; }
}
