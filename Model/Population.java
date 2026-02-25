package Model;

import java.util.ArrayList;
import java.util.Random;

public class Population {
    private ArrayList<Path> possiblePaths;

    public Population(ArrayList<Path> possiblePaths, double mutProb, double crossProb, int generations) {
        this.possiblePaths = possiblePaths;
    }

    // Crea una población mixta de caminos, combinando caminos generados por el método del vecino más cercano y caminos generados aleatoriamente (con semilla fija para reproducibilidad).
    public static Population createMixedPopulation(int N, int C, int[][] costs, double mutProb, double crossProb, int generations, double porcentajeNN, Random random) {
        int nNN = (int) Math.round(N * porcentajeNN);
        if (nNN > N) nNN = N;
        int nRandom = N - nNN;

        ArrayList<Path> paths = new ArrayList<>(N);

        // Generar caminos por vecino más cercano
        for (int i = 0; i < nNN; i++) {
            int startCity = random.nextInt(C);
            Path p = Path.createNearestNeighborPath(startCity, costs);
            paths.add(p);
        }

        // Generar caminos al azar (usando el mismo Random con semilla)
        for (int i = 0; i < nRandom; i++) {
            Path p = Path.createRandomPath(C, costs, random);
            paths.add(p);
        }

        return new Population(paths, mutProb, crossProb, generations);
    }

    // Imprime los caminos de la población
    public void printPaths() {
        int i = 1;
        for (Path path : possiblePaths) {
            String name = "Path " + i + ": ";
            System.out.println(name + path.toString());
            i++;
        }
    }

    // Devuelve copia de los caminos de la población
    public ArrayList<Path> getPaths(){
        return new ArrayList<>(possiblePaths);
    }

    // Devuelve el mejor camino de la población (de menor costo = de mayor fitness)
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

    // Establece una nueva generación de caminos
    public void setPaths(ArrayList<Path> nuevaGeneracion) {
        this.possiblePaths = nuevaGeneracion;
    }

}
