package data;

import main.Application;
import main.Configuration;

import java.util.ArrayList;

public class Knapsack {
    private static final Item[] items = Application.items;
    private static final int capacity = Configuration.instance.maximumCapacity;
    private ArrayList<Integer> chosenItems;
    private boolean[] knapsack;

    public boolean[] getKnapsack() { return this.knapsack; }

    /**
     * Initialise a new Knapsack with random items.
     */
    public Knapsack() {
        chosenItems = new ArrayList<>();
        knapsack = new boolean[Configuration.instance.numberOfItems];
        int weight = 0;

        while(true) {
            // Select a random index to flip
            int index = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);

            if(!knapsack[index]) {
                // if the new item would overload the knapsack, break, otherwise flip the bit and update weight
                if (weight + items[index].weight > capacity) {
                    break;
                } else {
                    addItem(index);
                    weight += items[index].weight;
                }
            } else {
                removeItem(index);
                weight -= items[index].weight;
            }
        }
    }

    /**
     * Clone an existing knapsack.
     *
     * @param previousKnapsack
     */
    public Knapsack(Knapsack previousKnapsack) {
        knapsack = previousKnapsack.knapsack.clone();
        chosenItems = (ArrayList<Integer>) previousKnapsack.chosenItems.clone();
    }

    /**
     * Create a new knapsack from a boolean array.
     *
     * @param knapsack
     */
    public Knapsack(boolean[] knapsack) {
        this.knapsack = knapsack;
        chosenItems = new ArrayList<>();
        for (int i = 0; i < knapsack.length; i++) {
            if (knapsack[i]) chosenItems.add(i);
        }
    }

    /**
     * Return the value of items contained in the knapsack.
     *
     * @return
     */
    public int getValue() {
        int value = 0;
        for (int i = 0; i < knapsack.length; i++) {
            if (knapsack[i]) value += items[i].value;
        }

        return value;
    }

    /**
     * Return the weight of items contained in the knapsack.
     *
     * @return
     */
    public int getWeight() {
        int weight = 0;
        for (int i = 0; i < knapsack.length; i++) {
            if (knapsack[i]) weight += items[i].weight;
        }

        return weight;
    }

    /**
     * Ensure a knapsack is valid, with weight less than or equal to the maximum capacity.
     */
    public void makeValid() {
        int weight = getWeight();

        // Remove random items until valid knapsack instance
        while (!(weight <= capacity)) {
            int index = Configuration.instance.randomGenerator.nextInt(chosenItems.size());
            int item_number = chosenItems.remove(index);
            knapsack[item_number] = false;

            // update weight accordingly
            weight -= items[item_number].weight;
        }
    }

    public void removeItem(int item_number) {
        chosenItems.remove((Integer) item_number);
        knapsack[item_number] = false;
    }

    public void addItem(int item_number) {
        chosenItems.add(item_number);
        knapsack[item_number] = true;
    }

    public void flipItem(int itemNumber) {
        if (knapsack[itemNumber]) removeItem(itemNumber);
        else addItem(itemNumber);
    }

    public String toString() {
        return chosenItems.size() + " items: " + chosenItems.toString();
    }
}
