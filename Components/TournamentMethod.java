package Components;


import Model.Path;
import java.util.ArrayList;
import java.util.Random;

public class TournamentMethod implements FatherSelectionMethod{
    private final int tournamentSize;
    private final Random random;

    public TournamentMethod(int tournamentSize, Random random) {
        this.tournamentSize = tournamentSize;
        this.random = random;
    }

    @Override
    public Path selectFather(ArrayList<Path> population) {
        ArrayList<Path> solucionesElegidas = new ArrayList<>();

        // Seleccionamos soluciones de la población, tantas como el tamaño del torneo  y de forma al azar
        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = random.nextInt(population.size());
            solucionesElegidas.add(population.get(randomIndex));
        }

        // Elegimos la mejor solución de las anteriores (la de mejor fitness)
        Path bestSolution = solucionesElegidas.getFirst();
        for (Path solution : solucionesElegidas) {
            if (solution.getFitness()  < bestSolution.getFitness()) {
                bestSolution = solution;
            }
        }

        return bestSolution;
    }

    @Override
    public String getName(){
        return "Torneo";
    }

}
