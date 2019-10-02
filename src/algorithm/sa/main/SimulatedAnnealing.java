package algorithm.sa.main;

import data.Knapsack;
import main.Configuration;

import java.text.DecimalFormat;

public class SimulatedAnnealing {
    private double temperature, end_temperature, coolingFactor;
    private Solution currentSolution, bestSolution, workingSolution;

    public SimulatedAnnealing(double temperature, double end_temperature, double coolingFactor) {
        this.temperature = temperature;
        this.end_temperature = end_temperature;
        this.coolingFactor = coolingFactor;
    }

    public void run() {
        boolean isSolution = false;
        boolean isNewSolutionUsed;

        currentSolution = new Solution();
        bestSolution = new Solution(currentSolution);
        workingSolution = new Solution(currentSolution);

        while (temperature > end_temperature) {
            isNewSolutionUsed = false;

            // randomly flip an item
            workingSolution.randomChange();

            // If new energy is lower, accept, otherwise decide based on Boltzmann distribution
            if (workingSolution.getEnergy() <= currentSolution.getEnergy()) {
                isNewSolutionUsed = true;
            } else {
                double randomVal = Configuration.instance.randomGenerator.nextDouble();
                double deltaSolutionEnergy = workingSolution.getEnergy() - currentSolution.getEnergy();
                double value = Math.exp(-deltaSolutionEnergy / temperature);
                if (value > randomVal) {
                    isNewSolutionUsed = true;
                }
            }

            // If the new solution is accepted, update accordingly, otherwise revert working solution
            if (isNewSolutionUsed) {
                currentSolution = new Solution(workingSolution);
                if (currentSolution.getEnergy() < bestSolution.getEnergy()) {
                    bestSolution = new Solution(currentSolution);
                    isSolution = true;
                }
            } else {
                workingSolution = new Solution(currentSolution);
            }

            System.out.println("[temperature] " + DecimalFormat.getInstance().format(temperature)
                    + " | [cse] " + DecimalFormat.getInstance().format(currentSolution.getEnergy())
                    + " | [wse] " + DecimalFormat.getInstance().format(workingSolution.getEnergy())
                    + " | [bse] " + DecimalFormat.getInstance().format(bestSolution.getEnergy()));
            temperature *= coolingFactor;
        }
        if (isSolution) {
            System.out.println("\n--BEST SOLUTION--");
            System.out.println("Weight: " + bestSolution.knapsack.getWeight());
            System.out.println("Value: " + bestSolution.knapsack.getValue());
            System.out.println(bestSolution.knapsack);
        }
    }
}

class Solution {
    public Knapsack knapsack;

    public Solution() {
        knapsack = new Knapsack();
    }

    public Solution(Solution other) {
        knapsack = new Knapsack(other.knapsack);
    }

    // Randomly select an item to add/remove from the knapsack
    public void randomChange() {
        boolean[] chosenItems = knapsack.getKnapsack();
        int index = Configuration.instance.randomGenerator.nextInt(chosenItems.length);
        if (chosenItems[index]) {
            knapsack.removeItem(index);
        } else {
            knapsack.addItem(index);
        }

        // ensure knapsack is still valid after the change
        knapsack.makeValid();
    }

    public double getEnergy() { return -knapsack.getValue(); }
}