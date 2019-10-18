package algorithm.ga.evolution.mutation;

import algorithm.ga.base.Chromosome;
import data.Knapsack;
import main.Configuration;

import java.util.Arrays;

public class Insertion {
    private static int numItems = Configuration.instance.numberOfItems;

    public static Chromosome performInsertionMutation(Chromosome chromosome) {
        boolean[] oldKnapsack = chromosome.getGene().getKnapsack();
        boolean[] newKnapsack = new boolean[numItems];

        // Pick two random indices
        int source = Configuration.instance.randomGenerator.nextInt(numItems);
        int dest = Configuration.instance.randomGenerator.nextInt(numItems);

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

        // Ensure valid solution
        Knapsack newGene = new Knapsack(newKnapsack);
        newGene.makeValid();

        return new Chromosome(newGene, chromosome.ga);
    }
}