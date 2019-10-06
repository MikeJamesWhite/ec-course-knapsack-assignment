package main;

import algorithm.ga.main.GeneticAlgorithm;
import algorithm.sa.main.SimulatedAnnealing;
import data.Item;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Application {
    public static Item[] items = new Item[Configuration.instance.numberOfItems];
    static String selectedAlgorithm = "";
    static String configuration = "";
    static boolean searchForBest = false;

    // --- command line ---
    // -algorithm [ga | sa | aco | pso | best-algorithm] -configuration [default | best] -search_best_configuration
    public static void main(String... args) {
        parseArgs(args);
        loadData();

        switch (selectedAlgorithm) {
            case("ga"):
                System.out.println("Running Genetic Algorithm...");
                GeneticAlgorithm ga = new GeneticAlgorithm(
                        1024, 10000, 0.7,
                        0.00005, "bitflip", "tournament",
                        "onepoint"
                );
                ga.execute();
                break;
            case("sa"):
                System.out.println("Running Simulated Annealing...");
                SimulatedAnnealing sa = new SimulatedAnnealing(1000.0, 1.0, 0.997);
                sa.run();
                break;
            case("aco"):
                System.out.println("Running Ant Colony Optimisation...");
                break;
            case("pso"):
                System.out.println("Running Particle Swarm Optimisation...");
                break;
        }
    }

    /**
     * Handle the parsing and validation of command line arguments
     *
     * @param args Command line arguments
     */
    private static void parseArgs(String... args) {
        System.out.println("Parsing arguments...");
        for (int i = 0; i < args.length; i++) {
            switch(args[i]) {
                case "-algorithm":
                    selectedAlgorithm = args[++i];

                    // Return with error if invalid argument
                    if (!selectedAlgorithm.equals("ga") && !selectedAlgorithm.equals("sa") &&
                            !selectedAlgorithm.equals("aco") && !selectedAlgorithm.equals("pso") &&
                            !selectedAlgorithm.equals("best-algorithm")) {
                        System.err.println("Invalid -algorithm value \"" + selectedAlgorithm
                                + "\". Value should be \"ga\", \"sa\", \"aco\", \"pso\" or \"best-algorithm\".");
                        System.exit(1);
                    }

                    System.out.println("Selected algorithm: " + selectedAlgorithm);
                    break;

                case "-configuration":
                    configuration = args[++i];

                    // Return with error if invalid argument
                    if (!configuration.equals("best") && !configuration.equals("default")) {
                        System.err.println("Error: Invalid -configuration value \"" + configuration
                                + "\". Value should be \"best\" or \"default\".");
                        System.exit(1);
                    }

                    System.out.println("Using configuration: " + args[i]);
                    break;

                case "-search_best_configuration":
                    System.out.println("Searching for best configuration enabled");
                    searchForBest = true;
                    break;

                default:
                    // Return with error if unrecognised argument
                    System.err.println("Error: Invalid command line argument \"" + args[i]
                            + "\". Available arguments are \"-algorithm\", \"-configuration\" "
                            + "or \"-search_best_configuration\".");
                    System.exit(1);
            }
        }

        // Can't search for best and use an existing config
        if (searchForBest && !configuration.isBlank()) {
            System.err.println("Error: can not search for best and specify an existing configuration.");
            System.exit(1);
        }

        // Can't use other args if selected algorithm is not specified
        if ((searchForBest || !configuration.isBlank()) && selectedAlgorithm.isBlank()) {
            System.err.println("Error: algorithm must be specified when using -configuration or "
                    + "-search_best_configuration arguments.");
            System.exit(1);
        }

        System.out.println("Done parsing!\n");
    }

    /**
     * Handle loading of knapsack items from csv file.
     */
    private static void loadData() {
        try {
            BufferedReader file = new BufferedReader(new FileReader(Configuration.instance.dataFilePath));
            System.out.println("Loading data from file at " + Configuration.instance.dataFilePath);

            file.readLine(); // skip first line
            for (int i = 0; i < items.length; i++) {
                String line = file.readLine();
                String[] values = line.split(";");
                items[i] = new Item(Integer.parseInt(values[1]), Integer.parseInt(values[2]));
            }

            System.out.println("Done loading data!\n");
        } catch (FileNotFoundException e) {
            System.err.println("Knapsack Instance CSV not found at filepath: " + Configuration.instance.dataFilePath);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error while reading data from file.");
            System.exit(1);
        }
    }

    /**
     * Handle loading of configuration from file
     */
    private static void loadConfig() {

    }
}