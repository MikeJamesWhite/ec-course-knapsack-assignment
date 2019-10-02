package algorithm.ga.main;

import algorithm.ga.base.Chromosome;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GeneticAlgorithm {
    public static int crossoverType;
    public static String mutationType;
    public static String selectionType;

    private static ArrayList<Chromosome> currentGeneration = new ArrayList<>();
    private static ArrayList<Chromosome> parents = new ArrayList<>();
    private static ArrayList<Chromosome> offspring;
    private static ArrayList<Chromosome> nextGeneration = new ArrayList<>();

    private static Chromosome bestIndividual;

    public static void execute(int generationSize, int maxGenerations, double crossoverChance, double mutationChance, int numOffspring) {
        int numGenerations = 0;

        // Initialise first generation
        for (int i = 0; i < generationSize; i++) {
            currentGeneration.add(new Chromosome());
        }

        // Run for specified number of generations
        while (numGenerations < maxGenerations) {
            selectParents();

            offspring = new ArrayList<>();
            while(offspring.size() < numOffspring)

            produceOffspring();
            mutateOffspring();



            numGenerations++;
        }
    }

    public static void selectParents() {

    }

    public static void produceOffspring() {

    }

    public static void mutateOffspring() {
        switch(mutationType) {
            case ("bitflip"):
                break;
            case("displacement"):
                break;
            case("exchange"):
                break;
            case("insertion"):
                break;
            case("inversion"):
                break;
        }
    }

    public static void selectNextGeneration() {

    }
}
