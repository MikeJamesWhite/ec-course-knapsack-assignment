package algorithm.ga.base;

import algorithm.ga.evolution.crossover.OnePoint;
import algorithm.ga.evolution.crossover.TwoPoint;
import algorithm.ga.evolution.mutation.*;
import algorithm.ga.main.GeneticAlgorithm;
import data.Knapsack;

public class Chromosome implements Comparable<Chromosome> {
    private final Knapsack knapsack;

    public Chromosome() { this.knapsack = new Knapsack(); }

    public Chromosome(Knapsack knapsack) {
        this.knapsack = knapsack;
    }

    public int getFitness() {
        return knapsack.getValue();
    }

    public Knapsack getGene() {
        return knapsack;
    }

    public Chromosome[] doCrossover(Chromosome other) {
        Chromosome[] children;

        switch (GeneticAlgorithm.crossoverType) {
            case 1:
                // perform single point crossover
                children = OnePoint.performOnePointCrossover(this, other);
                break;
            case 2:
                // perform double point crossover
                children = TwoPoint.performTwoPointCrossover(this, other);
                break;
            default:
                System.err.println("Error: No crossover type specified.");
                System.exit(1);
                return null;
        }

        return children;
    }

    public Chromosome doMutation() {
        Chromosome mutated;
        switch (GeneticAlgorithm.mutationType) {
            case("exchange"):
                mutated = Exchange.performExchangeMutation(this);
                break;
            case("inversion"):
                mutated = Inversion.performInversionMutation(this);
                break;
            case("bitflip"):
                mutated = BitFlip.performBitFlipMutation(this);
                break;
            case("insertion"):
                mutated = Insertion.performInsertionMutation(this);
                break;
            case("displacement"):
                mutated = Displacement.performDisplacementMutation(this);
                break;
            default:
                System.err.println("Error: No crossover type specified.");
                System.exit(1);
                return null;
        }

        return mutated;
    }

    public int compareTo(Chromosome other) {
        int fitness = getFitness();
        int otherFitness = other.getFitness();

        if (fitness < otherFitness)
            return -1;

        if (fitness > otherFitness)
            return 1;

        return 0;
    }


}