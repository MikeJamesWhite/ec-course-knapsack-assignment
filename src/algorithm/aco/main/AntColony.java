package algorithm.aco.main;

import algorithm.aco.base.Ant;
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

public class AntColony {
    private double[] pheromoneArray;
    private Ant[] antArray;

    public double decayFactor;
    public double alpha;
    public double beta;
    public int numberOfAnts;
    public double initialPheromone;
    private static int maxIterations = 10000;

    public AntColony(double decayFactor, double initialPheromone, int numberOfAnts, double alpha, double beta) {
        // Initialise pheromones
        pheromoneArray = new double[Configuration.instance.numberOfItems];
        for (int i = 0; i < pheromoneArray.length; i++) {
            pheromoneArray[i] = initialPheromone;
        }

        // Initialise ants
        antArray = new Ant[numberOfAnts];
        for (int i = 0; i < antArray.length; i++) {
            antArray[i] = new Ant(this);
        }

        // Init values
        this.decayFactor = decayFactor;
        this.numberOfAnts = numberOfAnts;
        this.alpha = alpha;
        this.beta = beta;
    }

    public AntColony(String configFile) {
        loadConfig(configFile);

        // Initialise pheromones
        pheromoneArray = new double[Configuration.instance.numberOfItems];
        for (int i = 0; i < pheromoneArray.length; i++) {
            pheromoneArray[i] = initialPheromone;
        }

        // Initialise ants
        antArray = new Ant[numberOfAnts];
        for (int i = 0; i < antArray.length; i++) {
            antArray[i] = new Ant(this);
        }
    }

    public void reset() {
        // Initialise pheromones
        pheromoneArray = new double[Configuration.instance.numberOfItems];
        for (int i = 0; i < pheromoneArray.length; i++) {
            pheromoneArray[i] = initialPheromone;
        }

        // Initialise ants
        antArray = new Ant[numberOfAnts];
        for (int i = 0; i < antArray.length; i++) {
            antArray[i] = new Ant(this);
        }
    }

    public int solve() {
        Ant overallBestAnt = null;
        int overallBestValue = -1;
        int iterationsSinceImprovement = 0;

        // Repeat for set number of iterations
        for (int i = 0; i < maxIterations; i++) {

            // Search for new solutions
            for (int j = 0; j < numberOfAnts; j++) {
                antArray[j].newRound();
                antArray[j].lookForWay();
            }

            // Handle pheromones
            doDecay();
            Ant bestAnt = getBestAnt();
            bestAnt.layPheromone();

            // Update overall best solution
            if (bestAnt.getObjectiveValue() > overallBestValue) {
                overallBestAnt = new Ant(bestAnt);
                overallBestValue = bestAnt.getObjectiveValue();

                System.out.println("New Best Solution:");
                System.out.println("Iteration: " + i);
                System.out.println(overallBestAnt.toString());
                System.out.println();
                iterationsSinceImprovement = 0;
            } else {
                iterationsSinceImprovement += 1;
            }

            if (iterationsSinceImprovement >= 500) {
                System.out.println("Breaking after " + i + " iterations, as no improvement since iteration " + (i-iterationsSinceImprovement));
                break;
            }
        }

        // Print out best solution
        System.out.println("\n---");
        System.out.println("Best Solution:");
        System.out.println(overallBestAnt.toString());

        return overallBestValue;
    }

    public void addPheromone(int itemNumber, double pheromoneValue) {
        pheromoneArray[itemNumber] += pheromoneValue;
    }

    public double getPheromone(int itemNumber) {
        return pheromoneArray[itemNumber];
    }

    public void doDecay() {
        for (int i = 0; i < pheromoneArray.length; i++) {
            pheromoneArray[i] *= 1.0 - decayFactor;
        }
    }

    private Ant getBestAnt() {
        Ant bestAnt = null;
        int bestValue = -1;

        for (int i = 0; i < antArray.length; i++) {
            int currentValue = antArray[i].getObjectiveValue();
            if (currentValue > bestValue) {
                bestValue = currentValue;
                bestAnt = antArray[i];
            }
        }

        return bestAnt;
    }

    public void loadConfig(String filename) {
        System.out.println("Loading config file: " + filename);
        try {
            // Set up
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(Configuration.instance.dataDirectory + filename));

            // read values
            decayFactor = Double.parseDouble(document.getElementsByTagName("decay_factor").item(0).getTextContent());
            initialPheromone = Double.parseDouble(document.getElementsByTagName("initial_pheromone").item(0).getTextContent());
            alpha = Double.parseDouble(document.getElementsByTagName("alpha").item(0).getTextContent());
            beta = Double.parseDouble(document.getElementsByTagName("beta").item(0).getTextContent());
            numberOfAnts = Integer.parseInt(document.getElementsByTagName("number_of_ants").item(0).getTextContent());

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
            Element rootElement = doc.createElement("aco_configuration");

            // write values
            e = doc.createElement("decay_factor");
            e.appendChild(doc.createTextNode(String.valueOf(decayFactor)));
            rootElement.appendChild(e);

            e = doc.createElement("initial_pheromone");
            e.appendChild(doc.createTextNode(String.valueOf(initialPheromone)));
            rootElement.appendChild(e);

            e = doc.createElement("alpha");
            e.appendChild(doc.createTextNode(String.valueOf(alpha)));
            rootElement.appendChild(e);

            e = doc.createElement("beta");
            e.appendChild(doc.createTextNode(String.valueOf(beta)));
            rootElement.appendChild(e);

            e = doc.createElement("number_of_ants");
            e.appendChild(doc.createTextNode(String.valueOf(numberOfAnts)));
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
