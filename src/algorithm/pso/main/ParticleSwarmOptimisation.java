package algorithm.pso.main;

import algorithm.pso.base.Particle;
import main.Configuration;

import java.util.ArrayList;
import java.util.Collections;

public class ParticleSwarmOptimisation {
    public static int maximumIterations = 10000;

    public int numberOfParticles;
    public double socialCoefficient;
    public double inertiaCoefficient;
    public double cognitiveCoefficient;

    private ArrayList<Particle> particleList = new ArrayList<>();

    public ParticleSwarmOptimisation(String configFile) {

    }

    public ParticleSwarmOptimisation(
            int numberOfParticles, double socialCoefficient,
            double inertiaCoefficient, double cognitiveCoefficient) {

        for (int i = 0; i < numberOfParticles; i++) {
            particleList.add(new Particle());
        }
    }

    public int execute() {
        Particle particle = null;
        int currentIteration = 0;
        boolean isFinished = false;

        while(!isFinished) {
            if (currentIteration < maximumIterations) {
                for (int i = 0; i < particleList.size(); i++) {
                    particle = particleList.get(i);
                }

                Collections.sort(particleList);
                getVelocity();
                updateParticleList();

                System.out.println("currentIteration : " + currentIteration + " | bestValue : " +
                        particleList.get(0).getBestValue());
                currentIteration++;
            } else {
                isFinished = true;
            }
        }

        Particle bestSolution = particleList.get(0);

        System.out.println("\n--BEST SOLUTION--");
        System.out.println("Weight: " + bestSolution.knapsack.getWeight());
        System.out.println("Value: " + bestSolution.knapsack.getValue());
        System.out.println(bestSolution.knapsack);

        return bestSolution.knapsack.getValue();
    }

    public void randomArrangeParticle(int index) {
        int indexItem1 = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);
        particleList.get(index).knapsack.flipItem(indexItem1);
        particleList.get(index).knapsack.makeValid();
    }

    public void copyFromParticle(int indexSource, int indexDestination) {
        Particle bestParticle = particleList.get(indexSource);

    }

    private void updateParticleList() {
        // for every particle except the best one
        for (int i = 1; i < particleList.size(); i++) {
            int numberOfChanges = (int) Math.floor(Math.abs(particleList.get(i).getVelocity()));

            for (int j = 0; j < numberOfChanges; j++) {
                if (Configuration.instance.randomGenerator.nextBoolean())
                    randomArrangeParticle(i);
                copyFromParticle(i-1, i);
            }
        }
    }

    public int getDistance() {
        return -1;
    }

    public int getTotalDistance() {
        return -1;
    }

    public void getVelocity() {

    }
}
