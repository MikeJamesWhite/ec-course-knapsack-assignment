package algorithm.ga.evolution.mutation;

import algorithm.ga.base.Chromosome;
import data.Knapsack;
import main.Configuration;

public class BitFlip {
    private static final int numItems = Configuration.instance.numberOfItems;

    public static Chromosome performBitFlipMutation(Chromosome chromosome) {
        Knapsack newGene = new Knapsack(chromosome.getGene());

        // Pick a random index and flip the bit
        int index = Configuration.instance.randomGenerator.nextInt(numItems);
        newGene.flipItem(index);

        // Ensure valid solution
        newGene.makeValid();

        return new Chromosome(newGene, chromosome.ga);
    }
}