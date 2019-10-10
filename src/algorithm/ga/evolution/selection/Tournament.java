package algorithm.ga.evolution.selection;

import algorithm.ga.base.Chromosome;
import main.Configuration;

import java.util.*;

public class Tournament {
    public static Chromosome runTournament(int tournamentSize, Chromosome[] population) {
        List<Chromosome> populationList = new ArrayList(Arrays.asList(population));
        List<Chromosome> tournament = new ArrayList<>();

        // Randomly add individuals to the tournament
        for (int i = 0; i < tournamentSize; i++) {
            int chosenIndividual = Configuration.instance.randomGenerator.nextInt(populationList.size());
            tournament.add(populationList.remove(chosenIndividual));
        }

        // Decide winner and return
        Collections.sort(tournament);
        return tournament.get(0);
    }
}
