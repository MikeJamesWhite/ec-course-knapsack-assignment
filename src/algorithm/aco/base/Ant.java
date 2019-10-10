package algorithm.aco.base;

import algorithm.aco.main.AntColony;
import data.Knapsack;
import main.Application;
import main.Configuration;

import java.util.Arrays;
import java.util.Vector;

public class Ant {
    private Knapsack tour;
    private AntColony antColony;
    private Vector<Integer> notYetChosen;
    private boolean[] init;

    // Standard constructor
    public Ant(AntColony antColony) {
        this.antColony = antColony;
        init = new boolean[Configuration.instance.numberOfItems];
        Arrays.fill(init, false);
    }

    // Copy constructor
    public Ant(Ant other) {
        this.tour = new Knapsack(other.tour);
        this.antColony = other.antColony;
    }

    public int getObjectiveValue() {
        return tour.getValue();
    }

    public void newRound() {
        notYetChosen = new Vector<>();

        for(int i = 0; i < Configuration.instance.numberOfItems; i++) {
            notYetChosen.addElement(i);
        }
    }

    public void layPheromone() {
        double pheromone = AntColony.decayFactor / getObjectiveValue();

        boolean[] knapsack = tour.getKnapsack();
        for (int i = 0; i < knapsack.length; i++) {
            if (knapsack[i]) {
                antColony.addPheromone(i, pheromone);
            }
        }
    }

    public void lookForWay() {
        int numberOfItems = Configuration.instance.numberOfItems;
        tour = new Knapsack(new boolean[numberOfItems]);
        int currentWeight = 0;
        int maxWeight = Configuration.instance.maximumCapacity;

        int selectedItem;

        while(notYetChosen.size() > 0) {
            double sum = 0.0;
            selectedItem = -1;

            Vector<Integer> toRemove = new Vector<>();
            for (int j = 0; j < notYetChosen.size(); j++) {
                int itemIndex = notYetChosen.elementAt(j);
                int itemWeight = Application.items[itemIndex].weight;

                if (itemWeight + currentWeight > maxWeight) {
                    toRemove.addElement(itemIndex);
                }
                else {
                    sum += antColony.getPheromone(itemIndex) / itemWeight;
                }
            }

            for (int index : toRemove) {
                notYetChosen.removeElement(index);
            }

            double selectionProbability = 0.0;
            double randomNumber = Configuration.instance.randomGenerator.nextDouble();

            for (int j = 0; j < notYetChosen.size(); j++) {
                int itemIndex = notYetChosen.elementAt(j);
                int itemWeight = Application.items[itemIndex].weight;

                selectionProbability += antColony.getPheromone(itemIndex) / itemWeight / sum;

                if (randomNumber < selectionProbability) {
                    selectedItem = itemIndex;
                    break;
                }
            }

            if (selectedItem == -1) break;

            tour.addItem(selectedItem);
            notYetChosen.removeElement(selectedItem);
            currentWeight += Application.items[selectedItem].weight;
        }
    }

    public String toString() {
        return tour.toString() + "\nValue: " + tour.getValue() + "\nWeight: " + tour.getWeight();
    }

}
