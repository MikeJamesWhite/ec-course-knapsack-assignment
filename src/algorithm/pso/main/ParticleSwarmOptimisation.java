package algorithm.pso.main;

import algorithm.pso.base.Particle;
import data.Knapsack;
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
import java.util.ArrayList;
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
        loadConfig(configFile);

        for (int i = 0; i < numberOfParticles; i++) {
            particleList.add(new Particle(this));
        }

        Collections.sort(particleList);
        globalBestPosition = new Knapsack(particleList.get(0).knapsack);
    }

    public ParticleSwarmOptimisation(
            int numberOfParticles, double socialCoefficient,
            double inertiaCoefficient, double cognitiveCoefficient) {
        this.socialCoefficient = socialCoefficient;
        this.inertiaCoefficient = inertiaCoefficient;
        this.cognitiveCoefficient = cognitiveCoefficient;
        this.numberOfParticles = numberOfParticles;

        for (int i = 0; i < numberOfParticles; i++) {
            particleList.add(new Particle(this));
        }

        Collections.sort(particleList);
        globalBestPosition = new Knapsack(particleList.get(0).knapsack);
    }

    public void reset() {
        for (int i = 0; i < numberOfParticles; i++) {
            particleList.add(new Particle(this));
        }

        Collections.sort(particleList);
        globalBestPosition = new Knapsack(particleList.get(0).knapsack);
    }

    public int execute() {
        int currentIteration = 0;
        int iterationsSinceImprovement = 0;

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

    public void loadConfig(String filename) {
        System.out.println("Loading config file: " + filename);
        try {
            // Set up
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(Configuration.instance.dataDirectory + filename));

            // read values
            socialCoefficient = Double.parseDouble(document.getElementsByTagName("social_coefficient").item(0).getTextContent());
            cognitiveCoefficient = Double.parseDouble(document.getElementsByTagName("cognitive_coefficient").item(0).getTextContent());
            inertiaCoefficient = Double.parseDouble(document.getElementsByTagName("inertia_coefficient").item(0).getTextContent());
            numberOfParticles = Integer.parseInt(document.getElementsByTagName("number_of_particles").item(0).getTextContent());

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
            Element rootElement = doc.createElement("pso_configuration");

            // write values
            e = doc.createElement("social_coefficient");
            e.appendChild(doc.createTextNode(String.valueOf(socialCoefficient)));
            rootElement.appendChild(e);

            e = doc.createElement("cognitive_coefficient");
            e.appendChild(doc.createTextNode(String.valueOf(cognitiveCoefficient)));
            rootElement.appendChild(e);

            e = doc.createElement("inertia_coefficient");
            e.appendChild(doc.createTextNode(String.valueOf(inertiaCoefficient)));
            rootElement.appendChild(e);

            e = doc.createElement("number_of_particles");
            e.appendChild(doc.createTextNode(String.valueOf(numberOfParticles)));
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
