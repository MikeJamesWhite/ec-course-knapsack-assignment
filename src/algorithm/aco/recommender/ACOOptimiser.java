package algorithm.aco.recommender;

import algorithm.aco.main.AntColony;

import java.util.ArrayList;
import java.util.HashMap;

public class ACOOptimiser {
    static HashMap<AntColony, Integer> configScores;
    static int NUMBER_OF_ITERATIONS = 5;

    static double[] decayFactors = new double[] {0.3, 0.1};
    static double[] alphas = new double[] {0.5, 0.4};
    static double[] betas = new double[] {0.1, 0.2};
    static double[] initialPheromones = new double[] {0.0005, 0.0010};
    static int[] numberOfAnts = new int[] {5, 10};

    public static void findBestConfig() {
        configScores = new HashMap<>();
        ArrayList<AntColony> instances = new ArrayList<>();

        // init different config instances
        for (double decayFactor : decayFactors) {
            for (double alpha : alphas) {
                for (double beta : betas) {
                    for (double initialPheromone : initialPheromones) {
                        for (int nAnts : numberOfAnts) {
                            AntColony instance  = new AntColony(decayFactor,initialPheromone,nAnts,alpha,beta);
                            instances.add(instance);
                            configScores.put(instance, 0);
                        }
                    }
                }
            }
        }

        // run config instances for set number of iterations
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            for (AntColony instance : instances) {
                int currentScore = configScores.get(instance);
                currentScore += instance.solve();
                configScores.replace(instance, currentScore);
            }
        }

        // get best config instance and score
        AntColony bestInstance = null;
        int bestTotalScore = -1;
        for (AntColony instance : instances) {
            if (configScores.get(instance) > bestTotalScore) {
                bestTotalScore = configScores.get(instance);
                bestInstance = instance;
            }
        }
        float averageScore = ((float) bestTotalScore) / NUMBER_OF_ITERATIONS;

        // Print results
        System.out.println("\n\n\n");
        System.out.println("BEST CONFIGURATION FOUND:");
        System.out.println("Beta: " + bestInstance.beta);
        System.out.println("Alpha: " + bestInstance.alpha);
        System.out.println("Number Of Ants: " + bestInstance.numberOfAnts);
        System.out.println("Decay Factor: " + bestInstance.decayFactor);
        System.out.println("Initial Pheromone: " + bestInstance.initialPheromone);
        System.out.println("Average score: " + averageScore);

        // write best instance and average score to file
        bestInstance.writeConfig("best_aco.xml", averageScore);
    }
}
