package algorithm.aco.main;

import algorithm.aco.base.Ant;
import main.Configuration;

public class AntColony {
    private double[] pheromoneArray;
    private Ant[] antArray;

    public static double decayFactor;
    private static int numberOfAnts;
    private static int maxIterations;

    public AntColony(double decayFactor, double initialPheromone, int numberOfAnts, int maxIterations) {
        // Initialise pheromones
        pheromoneArray = new double[Configuration.instance.numberOfItems];
        for (int i = 0; i < pheromoneArray.length; i++) {
            pheromoneArray[i] = initialPheromone;
        }

        // Initialise ants
        antArray = new Ant[numberOfAnts];
        for (int i = 0; i < antArray.length; i++) {
            antArray[i] = new Ant(this);
        }

        // Init values
        AntColony.decayFactor = decayFactor;
        AntColony.numberOfAnts = numberOfAnts;
        AntColony.maxIterations = maxIterations;
    }

    public void solve() {
        Ant overallBestAnt = null;
        int overallBestValue = -1;

        // Repeat for set number of iterations
        for (int i = 0; i < maxIterations; i++) {

            // Search for new solutions
            for (int j = 0; j < numberOfAnts; j++) {
                antArray[j].newRound();
                antArray[j].lookForWay();
            }

            // Handle pheromones
            doDecay();
            Ant bestAnt = getBestAnt();
            bestAnt.layPheromone();

            // Update overall best solution
            if (bestAnt.getObjectiveValue() > overallBestValue) {
                overallBestAnt = new Ant(bestAnt);
                overallBestValue = bestAnt.getObjectiveValue();

                System.out.println("New Best Solution:");
                System.out.println("Iteration: " + i);
                System.out.println(overallBestAnt.toString());
                System.out.println();
            }
        }

        // Print out best solution
        System.out.println("\n---");
        System.out.println("Best Solution:");
        System.out.println(overallBestAnt.toString());
    }

    public void addPheromone(int itemNumber, double pheromoneValue) {
        pheromoneArray[itemNumber] += pheromoneValue;
    }

    public double getPheromone(int itemNumber) {
        return pheromoneArray[itemNumber];
    }

    public void doDecay() {
        for (int i = 0; i < pheromoneArray.length; i++) {
            pheromoneArray[i] *= 1.0 - decayFactor;
        }
    }

    private Ant getBestAnt() {
        Ant bestAnt = null;
        int bestValue = -1;

        for (int i = 0; i < antArray.length; i++) {
            int currentValue = antArray[i].getObjectiveValue();
            if (currentValue > bestValue) {
                bestValue = currentValue;
                bestAnt = antArray[i];
            }
        }

        return bestAnt;
    }
}
