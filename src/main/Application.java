package main;

import algorithm.aco.main.AntColony;
import algorithm.ga.main.GeneticAlgorithm;
import algorithm.pso.main.ParticleSwarmOptimisation;
import algorithm.sa.main.SimulatedAnnealing;
import data.Item;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

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
                GeneticAlgorithm ga;

                if(configuration.equals("best")) {
                    ga = new GeneticAlgorithm("best_ga.xml");
                } else {
                    ga = new GeneticAlgorithm("default_ga.xml");
                }
                double value = ga.execute();

                break;

            case("sa"):
                System.out.println("Running Simulated Annealing...");
                SimulatedAnnealing sa;

                if(configuration.equals("best")) {
                    sa = new SimulatedAnnealing("best_sa.xml");
                } else {
                    sa = new SimulatedAnnealing("default_sa.xml");
                }
                sa.run();
                break;

            case("aco"):
                System.out.println("Running Ant Colony Optimisation...");
                AntColony aco;

                if(configuration.equals("best")) {
                    aco = new AntColony("best_aco.xml");
                } else {
                    aco = new AntColony("default_aco.xml");
                }
                aco.solve();
                break;

            case("pso"):
                System.out.println("Running Particle Swarm Optimisation...");
                ParticleSwarmOptimisation pso;

                /*
                if(configuration.equals("best")) {
                    pso = new ParticleSwarmOptimisation("best_pso.xml");
                } else {
                    pso = new ParticleSwarmOptimisation("default_pso.xml");
                }*/

                pso = new ParticleSwarmOptimisation(500, 1, 1, 1);
                pso.execute();
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

                    if (selectedAlgorithm.equals("best-algorithm")) {
                        selectedAlgorithm = getBestAlgo();
                        configuration = "best";
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
     * Check config files to find best algo
     */
    private static String getBestAlgo() {
        System.out.println("Identifying best algorithm...");
        String algorithm = "";
        double best_value = 0;
        try {
            // Set up
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document ga = builder.parse(new File(Configuration.instance.dataDirectory + "best_ga.xml"));
            Document sa = builder.parse(new File(Configuration.instance.dataDirectory + "best_sa.xml"));
            Document aco = builder.parse(new File(Configuration.instance.dataDirectory + "best_aco.xml"));
            Document pso = builder.parse(new File(Configuration.instance.dataDirectory + "best_pso.xml"));

            // read values
            double ga_value = Double.parseDouble(ga.getElementsByTagName("average_value").item(0).getTextContent());
            double sa_value = Double.parseDouble(sa.getElementsByTagName("average_value").item(0).getTextContent());
            double aco_value = Double.parseDouble(aco.getElementsByTagName("average_value").item(0).getTextContent());
            double pso_value = Double.parseDouble(pso.getElementsByTagName("average_value").item(0).getTextContent());

            // find best
            best_value = ga_value;
            algorithm = "ga";

            if (sa_value > best_value) {
                best_value = sa_value;
                algorithm = "sa";
            }
            if (aco_value > best_value) {
                best_value = aco_value;
                algorithm = "aco";
            }
            if (pso_value > best_value) {
                best_value = pso_value;
                algorithm = "pso";
            }

        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Loaded best algo '" + algorithm + "' with an average value of " + best_value);

        return algorithm;
    }
}