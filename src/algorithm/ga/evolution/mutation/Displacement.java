package algorithm.ga.evolution.mutation;

import algorithm.ga.base.Chromosome;
import data.Knapsack;
import main.Configuration;

import java.util.ArrayList;
import java.util.Arrays;

public class Displacement {
    private static int numItems = Configuration.instance.numberOfItems;

    public static Chromosome performDisplacementMutation(Chromosome chromosome) {
        boolean[] oldKnapsack = chromosome.getGene().getKnapsack();

        // Pick range of indexes which will be displaced
        int index1 = Configuration.instance.randomGenerator.nextInt(numItems);
        int index2 = Configuration.instance.randomGenerator.nextInt(numItems);
        if (!(index1 <= index2)) {
            int tmp = index1;
            index1 = index2;
            index2 = tmp;
        }

        // Split into two lists depending on whether displaced or not
        ArrayList<Boolean> stationary = new ArrayList<>();
        ArrayList<Boolean> displaced = new ArrayList<>();

        for (int i = 0; i < oldKnapsack.length; i++) {
            if (i < index1 || i > index2) {
                stationary.add(oldKnapsack[i]);
            } else {
                displaced.add(oldKnapsack[i]);
            }
        }

        // insert displaced at destination in stationary list
        int dest = Configuration.instance.randomGenerator.nextInt(stationary.size());
        stationary.addAll(dest, displaced);

        // pack into new knapsack array
        boolean[] newKnapsack = new boolean[numItems];
        for(int i = 0; i < stationary.size(); i++) {
            newKnapsack[i] = stationary.get(i);
        }

        // Ensure valid solution
        Knapsack newGene = new Knapsack(newKnapsack);
        newGene.makeValid();

        return new Chromosome(newGene, chromosome.ga);
    }
}