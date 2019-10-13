package algorithm.sa.main;

import data.Knapsack;
import main.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;

public class SimulatedAnnealing {
    private double start_temperature, temperature, end_temperature, coolingFactor;

    public SimulatedAnnealing(String configFile) {
        loadConfig(configFile);
    }

    public double run() {
        Solution currentSolution, bestSolution, workingSolution;
        boolean isSolution = false;
        boolean isNewSolutionUsed;

        currentSolution = new Solution();
        bestSolution = new Solution(currentSolution);
        workingSolution = new Solution(currentSolution);

        temperature = start_temperature;

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
            return bestSolution.knapsack.getValue();
        }

        return -1.0; // if no solution, return -1
    }

    public void loadConfig(String filename) {
        System.out.println("Loading config file: " + filename);
        try {
            // Set up
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(Configuration.instance.dataDirectory + filename));

            // read values
            start_temperature = Double.parseDouble(document.getElementsByTagName("start_temperature").item(0).getTextContent());
            end_temperature = Double.parseDouble(document.getElementsByTagName("end_temperature").item(0).getTextContent());
            coolingFactor = Double.parseDouble(document.getElementsByTagName("cooling_factor").item(0).getTextContent());

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
            Element rootElement = doc.createElement("sa_configuration");

            // write values
            e = doc.createElement("start_temperature");
            e.appendChild(doc.createTextNode(String.valueOf(start_temperature)));
            rootElement.appendChild(e);

            e = doc.createElement("end_temperature");
            e.appendChild(doc.createTextNode(String.valueOf(end_temperature)));
            rootElement.appendChild(e);

            e = doc.createElement("cooling_factor");
            e.appendChild(doc.createTextNode(String.valueOf(coolingFactor)));
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