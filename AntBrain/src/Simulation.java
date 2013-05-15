import processing.core.PApplet;

public class Simulation extends PApplet {
	Ant[] ants;
	Coordinate[] mines;
	GA evolution;
	int ticks;
	int generation;

	Simulation() {
		evolution = new GA();
		ticks = NUMBER_OF_STEPS_PER_GENERATION;
		generation = 1;

		ants = new Ant[NUMBER_OF_ANTS];

		for (int i = 0; i < NUMBER_OF_ANTS; i++) {
			NeuralNet net = new NeuralNet();
			for (int j = 0; j < NEURAL_NETWORK_LAYERS.length; j++) {
				net.addLayer(NEURAL_NETWORK_LAYERS[j]);
			}

			ants[i] = new Ant(net);
		}

		mines = new Coordinate[NUMBER_OF_MINES];
		resetMines();
		displayInformation(generation, 0, 0, 0, ants[0].brain);
		drawNeuralNet(ants[0].brain);
	}

	void resetMines() {
		for (int i = 0; i < NUMBER_OF_MINES; i++) {
			mines[i] = new Coordinate(random(SIMULATION_WIDTH),
					random(SIMULATION_HEIGHT));
		}
	}

	void step() {
		drawSimulation(ants, mines);

		for (int i = 0; i < ants.length; i++) {
			ants[i].update(mines);
			int mineEaten = ants[i].checkForMine(mines);

			if (mineEaten > -1) {
				ants[i].fitness++;
				mines[mineEaten] = new Coordinate(random(SIMULATION_WIDTH),
						random(SIMULATION_HEIGHT));
			}
		}

		drawSimulation(ants, mines);

		ticks--;

		if (ticks > 0)
			return;

		generation++;

		ticks = NUMBER_OF_STEPS_PER_GENERATION;

		Genome[] genomes = new Genome[ants.length];

		for (int i = 0; i < ants.length; i++) {
			genomes[i] = new Genome(ants[i].getGenome(), ants[i].fitness);
		}

		double[][] newChromos = evolution.evolve(genomes);

		for (int i = 0; i < ants.length; i++) {
			ants[i].setGenome(newChromos[i]);
		}

		resetMines();
		displayInformation(generation,
				(int) Math.floor(evolution.maximumFitness),
				(int) Math.floor(evolution.minimumFitness),
				evolution.averageFitness, ants[0].brain);
		drawNeuralNet(ants[0].brain);
	}
}
