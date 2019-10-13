package algorithm.ga.evolution.selection;

import algorithm.ga.base.Chromosome;
import main.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RouletteWheel {
    static double populationFitness;
    static ArrayList<Chromosome> populationList;
    static ArrayList<Double> probabilityFitnessValueList;
    static ArrayList<Double> rouletteField;

    public static void setupRouletteWheel(Chromosome[] population) {
        populationList = new ArrayList(Arrays.asList(population));
        populationFitness = getPopulationFitness(populationList);
        probabilityFitnessValueList = probabilityFitness(populationList, populationFitness);
        rouletteField = createRouletteField(probabilityFitnessValueList);
    }

    public static Chromosome selectParent() {
        double pointer = Configuration.instance.randomGenerator.nextInt(10000000) / 10000000.0;
        if (pointer < rouletteField.get(0)) {
            return(populationList.get(0));
        } else if (pointer > rouletteField.get(rouletteField.size() - 1)) {
            return(populationList.get(rouletteField.size() - 1));
        } else {
            for (int i = 1; i <= rouletteField.size() - 1; i++)
                if (pointer < rouletteField.get(i) && pointer >= rouletteField.get(i - 1))
                    return(populationList.get(i));
        }
        return null;
    }

    public static Double getPopulationFitness(ArrayList<Chromosome> populationList) {
        double populationFitness = 0.0;

        for (Chromosome chromosome : populationList)
            populationFitness = populationFitness + chromosome.getFitness();

        return populationFitness;
    }

    public static ArrayList<Double> probabilityFitness(ArrayList<Chromosome> populationList, double populationFitness) {
        ArrayList<Double> probabilityFitnessValues = new ArrayList<>();

        for (Chromosome chromosome : populationList)
            probabilityFitnessValues.add(chromosome.getFitness() / populationFitness);

        return probabilityFitnessValues;
    }

    public static ArrayList<Double> createRouletteField(ArrayList<Double> probabilityFitnessValueList) {
        ArrayList<Double> rouletteField = new ArrayList<>();

        for (int i = 0; i <= probabilityFitnessValueList.size() - 1; i++)
            if (i == 0)
                rouletteField.add(probabilityFitnessValueList.get(i));
            else
                rouletteField.add(probabilityFitnessValueList.get(i) + rouletteField.get(i - 1));

        return rouletteField;
    }
}
