package algorithm.ga.recommender;

import algorithm.ga.main.GeneticAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;

public class GAOptimiser {
    static HashMap<GeneticAlgorithm, Integer> configScores;
    static int NUMBER_OF_ITERATIONS = 5;

    static String[] mutationTypes = new String[] {"displacement", "bitflip", "insertion", "inversion", "exchange"};
    static String[] selectionTypes = new String[] {"roulette", "tournament"};
    static String[] crossoverTypes = new String[] {"onepoint", "twopoint"};
    static int[] tournamentSizes = new int[] {20, 40};
    static double[] mutationChances = new double[] {0.0005, 0.0010};
    static double[] crossoverChances = new double[] {0.7, 0.6};
    static int[] generationSizes = new int[] {128, 512};
    static int maxGenerations = 10000;

    public static void findBestConfig() {
        configScores = new HashMap<>();
        ArrayList<GeneticAlgorithm> instances = new ArrayList<>();

        // init different config instances
        for (double mutationChance : mutationChances) {
            for (double crossoverChance : crossoverChances) {
                for (int generationSize : generationSizes) {
                    for (String mutationType : mutationTypes) {
                        for (String crossoverType : crossoverTypes) {
                            for (String selectionType: selectionTypes) {
                                if (selectionType.equals("tournament")) {
                                    for (int tournamentSize : tournamentSizes) {
                                        GeneticAlgorithm instance  = new GeneticAlgorithm(
                                                mutationType,
                                                selectionType,
                                                crossoverType,
                                                tournamentSize,
                                                generationSize,
                                                maxGenerations,
                                                crossoverChance,
                                                mutationChance
                                        );
                                        instances.add(instance);
                                        configScores.put(instance, 0);
                                    }
                                } else {
                                    GeneticAlgorithm instance  = new GeneticAlgorithm(
                                            mutationType,
                                            selectionType,
                                            crossoverType,
                                            1,
                                            generationSize,
                                            maxGenerations,
                                            crossoverChance,
                                            mutationChance
                                    );
                                    instances.add(instance);
                                    configScores.put(instance, 0);
                                }
                            }

                        }
                    }
                }
            }
        }

        // run config instances for set number of iterations
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            for (GeneticAlgorithm instance : instances) {
                int currentScore = configScores.get(instance);
                currentScore += instance.execute();
                configScores.replace(instance, currentScore);
            }
        }

        // get best config instance and score
        GeneticAlgorithm bestInstance = null;
        int bestTotalScore = -1;
        for (GeneticAlgorithm instance : instances) {
            if (configScores.get(instance) > bestTotalScore) {
                bestTotalScore = configScores.get(instance);
                bestInstance = instance;
            }
        }
        float averageScore = ((float) bestTotalScore) / NUMBER_OF_ITERATIONS;

        // Print results
        System.out.println("\n\n\n");
        System.out.println("BEST CONFIGURATION FOUND:");
        System.out.println("Max generations: " + bestInstance.maxGenerations);
        System.out.println("Generation size: " + bestInstance.generationSize);
        System.out.println("Mutation type: " + bestInstance.mutationType);
        System.out.println("Mutation chance: " + bestInstance.mutationChance);
        System.out.println("Crossover type: " + bestInstance.crossoverType);
        System.out.println("Crossover chance: " + bestInstance.crossoverChance);
        System.out.println("Selection type: " + bestInstance.selectionType);
        if (bestInstance.selectionType.equals("tournament")) {
            System.out.println("Tournament size: " + bestInstance.tournamentSize);
        }
        System.out.println("Average score: " + averageScore);

        // write best instance and average score to file
        bestInstance.writeConfig("best_ga.xml", averageScore);
    }
}
