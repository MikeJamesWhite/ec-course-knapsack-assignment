package algorithm.ga.evolution.mutation;

import algorithm.ga.base.Chromosome;
import data.Knapsack;
import main.Configuration;

public class Inversion {

    private static int numItems = Configuration.instance.numberOfItems;

    public static Chromosome performInversionMutation(Chromosome chromosome) {
        Knapsack newGene = new Knapsack(chromosome.getGene());

        // Pick two random indices
        int index = Configuration.instance.randomGenerator.nextInt(numItems);
        int index2 = Configuration.instance.randomGenerator.nextInt(numItems);

        // Flip all bits in the range
        for (int i = index; i < index2; i++) {
            newGene.flipItem(i);
        }

        // Ensure valid solution
        newGene.makeValid();

        return new Chromosome(newGene, chromosome.ga);
    }
}