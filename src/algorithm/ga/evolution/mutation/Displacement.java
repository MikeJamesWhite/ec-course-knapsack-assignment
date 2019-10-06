package algorithm.ga.evolution.mutation;

import algorithm.ga.base.Chromosome;
import data.Knapsack;
import main.Configuration;

public class Displacement {
    private static int numItems = Configuration.instance.numberOfItems;

    public static Chromosome performDisplacementMutation(Chromosome chromosome) {
        boolean[] oldKnapsack = chromosome.getGene().getKnapsack();
        boolean[] newKnapsack = new boolean[numItems];

        // Pick range of indexes which will be displaced
        int index1 = Configuration.instance.randomGenerator.nextInt(numItems);
        int index2 = Configuration.instance.randomGenerator.nextInt(numItems);
        if (!(index1 <= index2)) {
            int tmp = index1;
            index1 = index2;
            index2 = tmp;
        }

        // Pick destination index
        /*
        // Copy array up until source
        for (int i = 0; i < source; i++) {
            newKnapsack[i] = oldKnapsack[i];
        }

        // Start copying one in front until reach the dest
        for (int i = source; i < dest; i++) {
            newKnapsack[i] = oldKnapsack[i+1];
        }

        // Copy source to dest
        newKnapsack[dest] = oldKnapsack[source];

        // Copy array until end
        for (int i = dest + 1; i < numItems; i++) {
            newKnapsack[i] = oldKnapsack[i];
        }
        */

        // Ensure valid solution
        Knapsack newGene = new Knapsack(newKnapsack);
        newGene.makeValid();

        return new Chromosome(newGene);
    }
}