package algorithm.ga.evolution.mutation;

import algorithm.ga.base.Chromosome;
import data.Knapsack;
import main.Configuration;

public class Exchange {
    static final int nItems = Configuration.instance.numberOfItems;

    public static Chromosome performExchangeMutation(Chromosome chromosome) {
        Knapsack newGene = new Knapsack(chromosome.getGene());

        // Pick two random indices to swap
        int index = Configuration.instance.randomGenerator.nextInt(nItems);
        int index2 = Configuration.instance.randomGenerator.nextInt(nItems);

        // Swap is only necessary if their status is different
        if (newGene.getKnapsack()[index] && !newGene.getKnapsack()[index2]) {
            newGene.removeItem(index);
            newGene.addItem(index2);
        }
        else if (newGene.getKnapsack()[index2] && !newGene.getKnapsack()[index]) {
            newGene.removeItem(index2);
            newGene.addItem(index);
        }

        // Ensure valid solution
        newGene.makeValid();

        return new Chromosome(newGene);
    }
}