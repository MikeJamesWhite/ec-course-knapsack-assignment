package algorithm.ga.evolution.crossover;

import algorithm.ga.base.Chromosome;
import data.Knapsack;
import main.Configuration;

public class OnePoint {
    static final int nItems = Configuration.instance.numberOfItems;

    public static Chromosome[] performOnePointCrossover(Chromosome first, Chromosome second) {
        int crossoverPoint = Configuration.instance.randomGenerator.nextInt(nItems);
        boolean[] child1 = new boolean[nItems];
        boolean[] child2 = new boolean[nItems];

        for (int i = 0; i < crossoverPoint; i++) {
            child1[i] = first.getGene().getKnapsack()[i];
            child2[i] = second.getGene().getKnapsack()[i];
        }
        for (int i = crossoverPoint; i < nItems; i++) {
            child1[i] = second.getGene().getKnapsack()[i];
            child2[i] = first.getGene().getKnapsack()[i];
        }

        return new Chromosome[] {
                new Chromosome(new Knapsack(child1)),
                new Chromosome(new Knapsack(child2))
        };
    }
}
