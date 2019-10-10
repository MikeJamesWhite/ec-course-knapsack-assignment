package algorithm.ga.main;

import algorithm.ga.base.Chromosome;
import algorithm.ga.base.Population;

import java.text.DecimalFormat;

public class GeneticAlgorithm {
    public static DecimalFormat decimalFormat = new DecimalFormat("000000");
    public static String mutationType, selectionType, crossoverType;
    public static int tournamentSize;
    public int generationSize;
    public int maxGenerations;
    public double crossoverChance, mutationChance;

    public GeneticAlgorithm(int generationSize, int maxGenerations, double crossoverChance,
                            double mutationChance, String mutationType,
                            String selectionType, String crossoverType,
                            int tournamentSize) {
        this.generationSize = generationSize;
        this.maxGenerations = maxGenerations;
        this.crossoverChance = crossoverChance;
        this.mutationChance = mutationChance;
        GeneticAlgorithm.mutationType = mutationType;
        GeneticAlgorithm.selectionType = selectionType;
        GeneticAlgorithm.crossoverType = crossoverType;
        GeneticAlgorithm.tournamentSize = tournamentSize;
    }

    public void execute() {
        double currentBestFitness = Double.MAX_VALUE;

        // Initialise population
        Population population = new Population(generationSize, crossoverChance, 0.1, mutationChance);
        Chromosome bestChromosome = population.getPopulation()[0];

        // Run for specified number of generations
        int numGenerations = 0;
        while (numGenerations < maxGenerations) {
            population.evolve();
            bestChromosome = population.getPopulation()[0];
            if (bestChromosome.getFitness() < currentBestFitness) {
                currentBestFitness = bestChromosome.getFitness();
                System.out.println("generation " + decimalFormat.format(numGenerations)+ " : ");
                System.out.println(bestChromosome.getGene());
                System.out.println("Value: "+ (-bestChromosome.getFitness()));
                System.out.println("Weight: " + (bestChromosome.getGene().getWeight()));
                System.out.println();
            }

            numGenerations++;
        }

        // Print final results
        System.out.println("\nBest chromosome found:");
        System.out.println(bestChromosome.getGene());
        System.out.println("Value: "+ (-bestChromosome.getFitness()));
        System.out.println("Weight: " + (bestChromosome.getGene().getWeight()));
    }
}
