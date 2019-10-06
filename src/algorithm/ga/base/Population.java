package algorithm.ga.base;

import main.Configuration;

import java.util.ArrayList;
import java.util.Arrays;

public class Population {
    private Chromosome[] population;
    private double elitismRatio;
    private double mutationRatio;
    private double crossoverRatio;
    private int numberOfCrossoverOperations = 0;
    private int numberOfMutationOperations = 0;

    public Population(int size, double crossoverRatio, double elitismRatio, double mutationRatio) {
        population = new Chromosome[size];
        this.elitismRatio = elitismRatio;
        this.mutationRatio = mutationRatio;
        this.crossoverRatio = crossoverRatio;

        for (int i = 0; i < size; i++) {
            population[i] = new Chromosome();
        }

        Arrays.sort(population);
    }

    public void evolve() {
        Chromosome[] chromosomeArray = new Chromosome[population.length];
        int index = (int) Math.round(population.length * elitismRatio);
        System.arraycopy(population, 0, chromosomeArray, 0, index);

        while (index < chromosomeArray.length) {
            if (Configuration.instance.randomGenerator.nextFloat() <= crossoverRatio) {
                Chromosome[] parents = selectParents();
                Chromosome[] children = parents[0].doCrossover(parents[1]);
                numberOfCrossoverOperations++;

                if (Configuration.instance.randomGenerator.nextFloat() <= mutationRatio) {
                    chromosomeArray[(index++)] = children[0].doMutation();
                    numberOfMutationOperations++;
                } else
                    chromosomeArray[(index++)] = children[0];

                if (index < chromosomeArray.length)
                    if (Configuration.instance.randomGenerator.nextFloat() <= mutationRatio) {
                        chromosomeArray[index] = children[1].doMutation();
                        numberOfMutationOperations++;
                    } else
                        chromosomeArray[index] = children[1];
            } else if (Configuration.instance.randomGenerator.nextFloat() <= mutationRatio) {
                chromosomeArray[index] = population[index].doMutation();
                numberOfMutationOperations++;
            } else {
                chromosomeArray[index] = population[index];
            }

            index++;
        }

        Arrays.sort(chromosomeArray);
        population = chromosomeArray;
    }

    public Chromosome[] getPopulation() {
        Chromosome[] chromosomeArray = new Chromosome[population.length];
        System.arraycopy(population, 0, chromosomeArray, 0, population.length);
        return chromosomeArray;
    }

    private Chromosome[] selectParents() {
        Chromosome[] parentArray = new Chromosome[2];

        for (int i = 0; i < 2; i++) {
            parentArray[i] = population[Configuration.instance.randomGenerator.nextInt(population.length)];
            for (int j = 0; j < 3; j++) {
                int index = Configuration.instance.randomGenerator.nextInt(population.length);
                if (population[index].compareTo(parentArray[i]) < 0)
                    parentArray[i] = population[index];
            }
        }

        return parentArray;
    }

    public int getNumberOfCrossoverOperations() {
        return numberOfCrossoverOperations;
    }

    public int getNumberOfMutationOperations() {
        return numberOfMutationOperations;
    }
}
