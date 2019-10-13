package algorithm.pso.base;

import data.Knapsack;

import java.util.Arrays;

public class Particle implements Comparable<Particle> {
    public Knapsack knapsack;
    private int bestValue;
    private double velocity;

    public Particle() {
        this.bestValue = 0;
        this.velocity = 0.0;
        this.knapsack = new Knapsack();
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

    public int getBestValue() {
        return bestValue;
    }

    public void setBestValue(final int value) {
        bestValue = value;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public int compareTo(Particle other) {
        int value = getBestValue();
        int otherValue = other.getBestValue();

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
