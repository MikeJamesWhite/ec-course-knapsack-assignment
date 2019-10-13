package algorithm.ga.main;

import algorithm.ga.base.Chromosome;
import algorithm.ga.base.Population;
import main.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

public class GeneticAlgorithm {
    public static DecimalFormat decimalFormat = new DecimalFormat("000000");
    public static String mutationType, selectionType, crossoverType;
    public static int tournamentSize;
    public int generationSize;
    public int maxGenerations;
    public double crossoverChance, mutationChance;

    public GeneticAlgorithm(String configFile) {
        loadConfig(configFile);
    }

    public void execute() {
        double currentBestFitness = Double.MAX_VALUE;

        // Initialise population
        Population population = new Population(generationSize, crossoverChance, 0.1, mutationChance);
        Chromosome bestChromosome = population.getPopulation()[0];

        // Run for specified number of generations, or until stagnation
        int numGenerations = 0;
        int generationsSinceImprovement = 0;
        while (numGenerations < maxGenerations) {
            population.evolve();
            bestChromosome = population.getPopulation()[0];
            if (bestChromosome.getFitness() < currentBestFitness) {
                currentBestFitness = bestChromosome.getFitness();
                System.out.println("generation " + decimalFormat.format(numGenerations)+ " : ");
                System.out.println(bestChromosome.getGene());
                System.out.println("Value: "+ (-bestChromosome.getFitness()));
                System.out.println("Weight: " + (bestChromosome.getGene().getWeight()));
                System.out.println();
                generationsSinceImprovement = 0;
            }

            numGenerations++;
            generationsSinceImprovement++;

            if (generationsSinceImprovement > 150) {
                System.out.println("Stopping after " + numGenerations + " generations due to stagnation for " + generationsSinceImprovement + " generations.");
                break;
            }
        }

        // Print final results
        System.out.println("\nBest chromosome found:");
        System.out.println(bestChromosome.getGene());
        System.out.println("Value: "+ (-bestChromosome.getFitness()));
        System.out.println("Weight: " + (bestChromosome.getGene().getWeight()));
    }

    public void loadConfig(String filename) {
        System.out.println("Loading config file: " + filename);
        try {
            // Set up
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(Configuration.instance.dataDirectory + filename));

            // read values
            mutationChance = Double.parseDouble(document.getElementsByTagName("mutation_chance").item(0).getTextContent());
            crossoverChance = Double.parseDouble(document.getElementsByTagName("crossover_chance").item(0).getTextContent());
            generationSize = Integer.parseInt(document.getElementsByTagName("generation_size").item(0).getTextContent());
            maxGenerations = Integer.parseInt(document.getElementsByTagName("max_generations").item(0).getTextContent());
            tournamentSize = Integer.parseInt(document.getElementsByTagName("tournament_size").item(0).getTextContent());
            mutationType = document.getElementsByTagName("mutation_type").item(0).getTextContent();
            selectionType = document.getElementsByTagName("selection_type").item(0).getTextContent();
            crossoverType = document.getElementsByTagName("crossover_type").item(0).getTextContent();

        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Successfully loaded config!");
    }

    public void writeConfig(String filename, float averageValue) {
        System.out.println("Writing config file: " + filename);
        Element e = null;
        Document doc;

        try {
            // Set up
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();

            // create root element
            Element rootElement = doc.createElement("ga_configuration");

            // write values
            e = doc.createElement("generation_size");
            e.appendChild(doc.createTextNode(String.valueOf(generationSize)));
            rootElement.appendChild(e);

            e = doc.createElement("max_generations");
            e.appendChild(doc.createTextNode(String.valueOf(maxGenerations)));
            rootElement.appendChild(e);

            e = doc.createElement("crossover_chance");
            e.appendChild(doc.createTextNode(String.valueOf(crossoverChance)));
            rootElement.appendChild(e);

            e = doc.createElement("mutation_chance");
            e.appendChild(doc.createTextNode(String.valueOf(mutationChance)));
            rootElement.appendChild(e);

            e = doc.createElement("mutation_type");
            e.appendChild(doc.createTextNode(mutationType));
            rootElement.appendChild(e);

            e = doc.createElement("selection_type");
            e.appendChild(doc.createTextNode(selectionType));
            rootElement.appendChild(e);

            e = doc.createElement("crossover_type");
            e.appendChild(doc.createTextNode(crossoverType));
            rootElement.appendChild(e);

            e = doc.createElement("tournament_size");
            e.appendChild(doc.createTextNode(String.valueOf(tournamentSize)));
            rootElement.appendChild(e);

            e = doc.createElement("average_value");
            e.appendChild(doc.createTextNode(String.valueOf(averageValue)));
            rootElement.appendChild(e);

            doc.appendChild(rootElement);

            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            // send DOM to file
            tr.transform(new DOMSource(doc),
                    new StreamResult(new FileOutputStream(Configuration.instance.dataDirectory + filename)));

        } catch(Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        System.out.println("Successfully wrote config!");
    }
}
