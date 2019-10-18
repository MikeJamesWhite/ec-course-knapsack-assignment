package algorithm.ga.base;

import algorithm.ga.evolution.crossover.OnePoint;
import algorithm.ga.evolution.crossover.TwoPoint;
import algorithm.ga.evolution.mutation.*;
import algorithm.ga.main.GeneticAlgorithm;
import data.Knapsack;

import java.util.Arrays;

public class Chromosome implements Comparable<Chromosome> {
    private final Knapsack knapsack;
    public GeneticAlgorithm ga;

    public Chromosome(GeneticAlgorithm ga) {
        this.knapsack = new Knapsack();
        this.ga = ga;
    }

    public Chromosome(Knapsack knapsack, GeneticAlgorithm ga) {
        this.knapsack = knapsack;
        this.ga = ga;
    }

    public int getFitness() {
        return -knapsack.getValue();
    }

    public Knapsack getGene() {
        return knapsack;
    }

    public Chromosome[] doCrossover(Chromosome other) {
        Chromosome[] children;

        switch (ga.crossoverType) {
            case "onepoint":
                // perform single point crossover
                children = OnePoint.performOnePointCrossover(this, other);
                break;
            case "twopoint":
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
        switch (ga.mutationType) {
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
                System.err.println("Error: No mutation type specified.");
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

    public boolean equals(Object o) {
        if (!(o instanceof Chromosome))
            return false;

        Chromosome chromosome = (Chromosome) o;

        return (Arrays.equals(getGene().getKnapsack(), chromosome.getGene().getKnapsack())) && (getFitness() == chromosome.getFitness());
    }
}
