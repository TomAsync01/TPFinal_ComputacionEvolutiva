import java.io.File;
import java.io.IOException;
import InstancesTSP.ATSPReader;

public class Main {
    public static void main(String[] args) throws IOException {
        File atspFile = new File("InstancesTSP/br17.atsp");
        int[][] costs = ATSPReader.read(atspFile);
        int C = costs.length; //Number of cities to visit

        int N = 10; //Number of individuals
        int G = 1000; //Number of generations
        double mutProb = 0.05; //Mutation probability
        double crossProb = 0.6; //Crossover probability

        Population p = new Population(N, C, costs, mutProb, crossProb, G);
        p.printPaths();
        System.out.println("-----------------------------------");
        p.printFitnessVariationPercent();

    }
}
