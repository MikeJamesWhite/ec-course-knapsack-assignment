package algorithm.ga.base;

import algorithm.ga.evolution.selection.RouletteWheel;
import algorithm.ga.evolution.selection.Tournament;
import algorithm.ga.main.GeneticAlgorithm;
import main.Configuration;

import java.util.Arrays;

public class Population {
    private GeneticAlgorithm ga;
    private Chromosome[] population;
    private double elitismRatio;
    private double mutationRatio;
    private double crossoverRatio;

    public Population(int size, double crossoverRatio, double elitismRatio, double mutationRatio, GeneticAlgorithm ga) {
        population = new Chromosome[size];
        this.ga = ga;
        this.elitismRatio = elitismRatio;
        this.mutationRatio = mutationRatio;
        this.crossoverRatio = crossoverRatio;

        for (int i = 0; i < size; i++) {
            population[i] = new Chromosome(ga);
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

                if (Configuration.instance.randomGenerator.nextFloat() <= mutationRatio) {
                    chromosomeArray[(index++)] = children[0].doMutation();
                } else
                    chromosomeArray[(index++)] = children[0];

                if (index < chromosomeArray.length)
                    if (Configuration.instance.randomGenerator.nextFloat() <= mutationRatio) {
                        chromosomeArray[index] = children[1].doMutation();
                    } else
                        chromosomeArray[index] = children[1];
            } else if (Configuration.instance.randomGenerator.nextFloat() <= mutationRatio) {
                chromosomeArray[index] = population[index].doMutation();
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

        switch (ga.selectionType) {
            case "tournament":
                parentArray[0] = Tournament.runTournament(ga.tournamentSize, population);
                parentArray[1] = Tournament.runTournament(ga.tournamentSize, population);
                break;
            case "roulette":
                RouletteWheel.setupRouletteWheel(population);
                parentArray[0] = RouletteWheel.selectParent();
                parentArray[1] = RouletteWheel.selectParent();
                break;
            default:
                System.err.println("NO VALID SELECTION ALGORITHM SPECIFIED");
                System.exit(1);
        }

        return parentArray;
    }
}
