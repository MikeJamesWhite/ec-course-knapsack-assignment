package algorithm.pso.recommender;

import algorithm.pso.main.ParticleSwarmOptimisation;

import java.util.ArrayList;
import java.util.HashMap;

public class PSOOptimiser {
    static HashMap<ParticleSwarmOptimisation, Integer> configScores;
    static int NUMBER_OF_ITERATIONS = 5;

    static double[] socialCoefficients = new double[] {1.0, 0.9, 1.1};
    static double[] cognitiveCoefficients = new double[] {1.0, 0.9, 1.1};
    static double[] inertiaCoefficients = new double[] {1.0, 0.9, 1.1};
    static int[] numberOfParticles = new int[] {10, 100, 500};

    public static void findBestConfig() {
        configScores = new HashMap<>();
        ArrayList<ParticleSwarmOptimisation> instances = new ArrayList<>();

        // init different config instances
        for (double socialCoefficient : socialCoefficients) {
            for (double cognitiveCoefficient : cognitiveCoefficients) {
                for (double inertiaCoefficient : inertiaCoefficients) {
                    for (int nParticles : numberOfParticles) {
                        ParticleSwarmOptimisation instance = new ParticleSwarmOptimisation(
                                nParticles, socialCoefficient, inertiaCoefficient, cognitiveCoefficient
                        );
                        instances.add(instance);
                        configScores.put(instance, 0);
                    }
                }
            }
        }

        // run config instances for set number of iterations
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            for (ParticleSwarmOptimisation instance : instances) {
                int currentScore = configScores.get(instance);
                currentScore += instance.execute();
                configScores.replace(instance, currentScore);
            }
        }

        // get best config instance and score
        ParticleSwarmOptimisation bestInstance = null;
        int bestTotalScore = -1;
        for (ParticleSwarmOptimisation instance : instances) {
            if (configScores.get(instance) > bestTotalScore) {
                bestTotalScore = configScores.get(instance);
                bestInstance = instance;
            }
        }
        float averageScore = ((float) bestTotalScore) / NUMBER_OF_ITERATIONS;

        // Print results
        System.out.println("\n\n\n");
        System.out.println("BEST CONFIGURATION FOUND:");
        System.out.println("Number of particles: " + bestInstance.numberOfParticles);
        System.out.println("Social coefficient: " + bestInstance.socialCoefficient);
        System.out.println("Cognitive coefficient: " + bestInstance.cognitiveCoefficient);
        System.out.println("Inertia coefficient: " + bestInstance.inertiaCoefficient);
        System.out.println("Average score: " + averageScore);

        // write best instance and average score to file
        bestInstance.writeConfig("best_pso.xml", averageScore);
    }
}
