package algorithm.ga.evolution.crossover;

import algorithm.ga.base.Chromosome;
import algorithm.ga.main.GeneticAlgorithm;
import data.Knapsack;
import main.Configuration;

public class TwoPoint {
     static final int nItems = Configuration.instance.numberOfItems;

     public static Chromosome[] performTwoPointCrossover(Chromosome first, Chromosome second) {
         int crossoverPoint1 = Configuration.instance.randomGenerator.nextInt(nItems);
         int crossoverPoint2 = Configuration.instance.randomGenerator.nextInt(nItems);
         if (crossoverPoint1 > crossoverPoint2) {
             int temp = crossoverPoint1;
             crossoverPoint1 = crossoverPoint2;
             crossoverPoint2 = temp;
         }

         boolean[] child1 = new boolean[nItems];
         boolean[] child2 = new boolean[nItems];

         for (int i = 0; i < crossoverPoint1; i++) {
             child1[i] = first.getGene().getKnapsack()[i];
             child2[i] = second.getGene().getKnapsack()[i];
         }
         for (int i = crossoverPoint1; i < crossoverPoint2; i++) {
             child1[i] = second.getGene().getKnapsack()[i];
             child2[i] = first.getGene().getKnapsack()[i];
         }
         for (int i = crossoverPoint2; i < nItems; i++) {
             child1[i] = first.getGene().getKnapsack()[i];
             child2[i] = second.getGene().getKnapsack()[i];
         }

         Knapsack knapsack1 = new Knapsack(child1);
         knapsack1.makeValid();

         Knapsack knapsack2 = new Knapsack(child2);
         knapsack2.makeValid();

         return new Chromosome[] {
                 new Chromosome(knapsack1, first.ga),
                 new Chromosome(knapsack2, first.ga)
         };
     }
}
