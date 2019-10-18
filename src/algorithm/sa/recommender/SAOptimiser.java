package algorithm.sa.recommender;

import algorithm.sa.main.SimulatedAnnealing;

import java.util.ArrayList;
import java.util.HashMap;

public class SAOptimiser {
    static HashMap<SimulatedAnnealing, Integer> configScores;
    static int NUMBER_OF_ITERATIONS = 10;

    // config options
    static double[] start_temperatures = new double[] {1000.0, 950.0, 900.0};
    static double[] end_temperatures = new double[]{1.0, 100.0, 10.0};
    static double[] cooling_factors = new double[]{0.997, 0.9995, 0.99};

    public static void findBestConfig() {
        configScores = new HashMap<>();
        ArrayList<SimulatedAnnealing> instances = new ArrayList<>();

        // init different config instances
        for (double startTemp : start_temperatures) {
            for (double endTemp : end_temperatures) {
                for (double coolingFactor : cooling_factors) {
                    SimulatedAnnealing instance  = new SimulatedAnnealing(startTemp, endTemp, coolingFactor);
                    instances.add(instance);
                    configScores.put(instance, 0);
                }
            }
        }

        // run config instances for set number of iterations
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            for (SimulatedAnnealing instance : instances) {
                int currentScore = configScores.get(instance);
                currentScore += instance.run();
                configScores.replace(instance, currentScore);
            }
        }

        // get best config instance and score
        SimulatedAnnealing bestInstance = null;
        int bestTotalScore = -1;
        for (SimulatedAnnealing instance : instances) {
            if (configScores.get(instance) > bestTotalScore) {
                bestTotalScore = configScores.get(instance);
                bestInstance = instance;
            }
        }
        float averageScore = ((float) bestTotalScore) / NUMBER_OF_ITERATIONS;

        // Print results
        System.out.println("\n\n\n");
        System.out.println("BEST CONFIGURATION FOUND:");
        System.out.println("Start temperature: " + bestInstance.start_temperature);
        System.out.println("End temperature: " + bestInstance.end_temperature);
        System.out.println("Cooling factor: " + bestInstance.coolingFactor);
        System.out.println("Average score: " + averageScore);

        // write best instance and average score to file
        bestInstance.writeConfig("best_sa.xml", averageScore);
    }
}
