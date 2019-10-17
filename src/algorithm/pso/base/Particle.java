package algorithm.pso.base;

import algorithm.pso.main.ParticleSwarmOptimisation;
import data.Knapsack;
import main.Configuration;

import java.util.Arrays;

public class Particle implements Comparable<Particle> {
    public ParticleSwarmOptimisation swarm;
    public Knapsack knapsack;
    private int bestValue;
    private double[] velocity;
    private Knapsack localBestPosition;

    public Particle(ParticleSwarmOptimisation swarm) {
        this.swarm = swarm;
        this.velocity = new double[Configuration.instance.numberOfItems];
        for(int i = 0; i < velocity.length; i++) {
            velocity[i] = Configuration.instance.randomGenerator.nextDouble();
        }

        this.knapsack = new Knapsack();
        localBestPosition = new Knapsack(knapsack);
    }

    public int getBestValue() {
        return bestValue;
    }

    public double[] getVelocity() {
        return velocity;
    }

    public void updateVelocity() {
        for (int i = 0; i < velocity.length; i++) {
            // get cognitive component
            int localBest = localBestPosition.getKnapsack()[i]? 1 : 0;
            int currentPos = knapsack.getKnapsack()[i]? 1 : 0;
            int deltaPos = localBest - currentPos;

            double r1 = Configuration.instance.randomGenerator.nextDouble();
            double cognitiveComponent = swarm.cognitiveCoefficient * r1 * deltaPos;

            // get social component
            int globalBest = swarm.globalBestPosition.getKnapsack()[i] ? 1 : 0;
            deltaPos = globalBest - currentPos;

            double r2 = Configuration.instance.randomGenerator.nextDouble();
            double socialComponent = swarm.socialCoefficient * r2 * deltaPos;

            // get inertia component
            double inertiaComponent = swarm.inertiaCoefficient * velocity[i];

            // update velocity
            velocity[i] = inertiaComponent + cognitiveComponent + socialComponent;
        }
    }

    public void updatePosition() {
        boolean[] newPosition = new boolean[Configuration.instance.numberOfItems];

        for (int i = 0; i < knapsack.getKnapsack().length; i++) {
            double selectionChance = 1/(1 + Math.pow(Math.E, -velocity[i]));
            double randomValue = Configuration.instance.randomGenerator.nextDouble();
            if (randomValue < selectionChance) {
                newPosition[i] = true;
            }
        }

        knapsack = new Knapsack(newPosition);
        knapsack.makeValid();

        // check if better than current best
        if (knapsack.getValue() > localBestPosition.getValue()) {
            localBestPosition = new Knapsack(knapsack);
            bestValue = localBestPosition.getValue();
        }
    }

    public int compareTo(Particle other) {
        int value = knapsack.getValue();
        int otherValue = other.knapsack.getValue();

        if (value > otherValue)
            return -1;

        if (value < otherValue)
            return 1;

        return 0;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Particle))
            return false;

        Particle p = (Particle) o;

        return (Arrays.equals(knapsack.getKnapsack(), p.knapsack.getKnapsack())) && (getBestValue() == p.getBestValue());
    }
}
