package algorithm.pso.base;

import data.Knapsack;

public class Particle {
    private Knapsack knapsack;
    private int bestValue;
    private double velocity;

    public Particle() {
        this.bestValue = 0;
        this.velocity = 0.0;
    }

    public boolean getKnapsackItem(int index) {
        return knapsack.getKnapsack()[index];
    }

    public void setKnapsackItem(int index, boolean value) {
        if (value && !knapsack.getKnapsack()[index])
            knapsack.addItem(index);
        else if (!value && knapsack.getKnapsack()[index])
            knapsack.removeItem(index);

        knapsack.makeValid();
    }

    public double getBestDistance() {
        return bestValue;
    }

    public void setBestDistance(final int value) {
        bestValue = value;
    }

    public double velocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }
}
