package algorithm.pso.main;

import algorithm.pso.base.Particle;
import data.Knapsack;
import main.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ParticleSwarmOptimisation {
    public static int maximumIterations = 10000;

    public Knapsack globalBestPosition;
    public int numberOfParticles;
    public double socialCoefficient;
    public double inertiaCoefficient;
    public double cognitiveCoefficient;

    private ArrayList<Particle> particleList = new ArrayList<>();

    public ParticleSwarmOptimisation(String configFile) {

        for (int i = 0; i < numberOfParticles; i++) {
            particleList.add(new Particle(this));
        }
    }

    public ParticleSwarmOptimisation(
            int numberOfParticles, double socialCoefficient,
            double inertiaCoefficient, double cognitiveCoefficient) {
        this.socialCoefficient = socialCoefficient;
        this.inertiaCoefficient = inertiaCoefficient;
        this.cognitiveCoefficient = cognitiveCoefficient;

        for (int i = 0; i < numberOfParticles; i++) {
            particleList.add(new Particle(this));
        }

        Collections.sort(particleList);
        globalBestPosition = new Knapsack(particleList.get(0).knapsack);
    }

    public int execute() {
        int currentIteration = 0;
        int iterationsSinceImprovement = 0;
        boolean isFinished = false;

        while(currentIteration < maximumIterations) {
            currentIteration++;
            updateParticleList();
            Collections.sort(particleList);

            if (particleList.get(0).knapsack.getValue() > globalBestPosition.getValue()) {
                globalBestPosition = new Knapsack(particleList.get(0).knapsack);
                iterationsSinceImprovement = 0;

                System.out.println("New Best Solution:");
                System.out.println("currentIteration : " + currentIteration + " | bestValue : " +
                        particleList.get(0).getBestValue() + " | weight: " + particleList.get(0).knapsack.getWeight());
                System.out.println(particleList.get(0).knapsack);
                System.out.println();
            } else {
                iterationsSinceImprovement++;
            }


            if (iterationsSinceImprovement >= 500) {
                System.out.println("Breaking after " + currentIteration + " iterations, as no improvement since iteration " + (currentIteration-iterationsSinceImprovement));
                break;
            }
        }

        Particle bestSolution = particleList.get(0);

        System.out.println("\n--BEST SOLUTION--");
        System.out.println("Weight: " + bestSolution.knapsack.getWeight());
        System.out.println("Value: " + bestSolution.knapsack.getValue());
        System.out.println(bestSolution.knapsack);

        return bestSolution.knapsack.getValue();
    }

    private void updateParticleList() {
        for (int i = 1; i < particleList.size(); i++) {
            particleList.get(i).updateVelocity();
            particleList.get(i).updatePosition();
        }
    }
}
