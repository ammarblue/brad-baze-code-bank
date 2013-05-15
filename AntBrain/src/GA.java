import processing.core.PApplet;

public class GA extends PApplet {
	double totalFitness;
	Genome[] population;
	int fittestGenome;
	double maximumFitness;
	double minimumFitness;
	double averageFitness;

	GA() {
	}

	private void Mutate(double[] chromo) {
		for (int i = 0; i < chromo.length; i++) {
			if (random(1) < MUTATION_RATE) {
				chromo[i] = chromo[i] + (random(-1, 1) * MAXIMUM_PERTURBATION);
			}
		}
	}

	private Genome GetChromoRoulette() {
		double Slice = random(1) * totalFitness;

		Genome TheChosenOne = null;

		double FitnessSoFar = 0;

		for (int i = 0; i < population.length; i++) {
			FitnessSoFar += population[i].fitness;
			if (FitnessSoFar >= Slice) {
				TheChosenOne = population[i];
				break;
			}

		}
		return TheChosenOne;
	}

	private void Crossover(double[] mum, double[] dad, double[] baby1, double[] baby2) 
    { 
        if ((random(1) > CROSSOVER_RATE) || (mum == dad)) 
        { 
            for (int i = 0; i < mum.length; i++) 
            { 
                baby1[i] = mum[i]; 
                baby2[i] = dad[i]; 
            } 
            return; 
        } 
 
        int cp = int(random(mum.length + 1)); 
 
        for (int i = 0; i < cp; ++i) 
        { 
          baby1[i] = mum[i]; 
          baby2[i] = dad[i]; 
        } 
 
        for (int i = cp; i < mum.length; i++) 
        { 
          baby1[i] = dad[i]; 
          baby2[i] = mum[i]; 
        } 
    }	private void CalculateBestWorstAvTot() {
		totalFitness = 0;

		maximumFitness = 0;
		minimumFitness = Double.MAX_VALUE;

		for (int i = 0; i < population.length; i++) {
			if (population[i].fitness > maximumFitness) {
				maximumFitness = population[i].fitness;
				fittestGenome = i;
			}

			if (population[i].fitness < minimumFitness) {
				minimumFitness = population[i].fitness;
			}

			totalFitness += population[i].fitness;
		}

		averageFitness = totalFitness / population.length;
	}

	private void Elitism(double[][] Pop) {
		for (int i = 0; i < 4; i++)
			Pop[i] = population[i].genome;
	}

	public double[][] evolve(Genome[] oldPopulation) {
		population = oldPopulation;

		java.util.Arrays.sort(population);

		CalculateBestWorstAvTot();

		double[][] newPopulation = new double[oldPopulation.length][];

		Elitism(newPopulation);

		int pos = ELITISM;

		while (pos < newPopulation.length) {
			Genome mum = GetChromoRoulette();
			Genome dad = GetChromoRoulette();

			double[] baby1 = new double[mum.genome.length];
			double[] baby2 = new double[mum.genome.length];

			Crossover(mum.genome, dad.genome, baby1, baby2);

			Mutate(baby1);
			Mutate(baby2);

			if (pos < newPopulation.length) {
				newPopulation[pos] = baby1;
				pos++;
			}
			if (pos < newPopulation.length) {
				newPopulation[pos] = baby2;
				pos++;
			}
		}

		return newPopulation;
	}
}
