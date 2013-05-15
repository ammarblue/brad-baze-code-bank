import processing.core.PApplet;
import processing.core.PGraphics;

public class Main extends PApplet {
	Simulation sim;
	int translateX;
	int translateY;
	PGraphics simGraphics;

	public void setup() {
		size(WINDOW_WIDTH, WINDOW_HEIGHT, P2D);
		background(0);
		ellipseMode(CENTER);
		frameRate(FRAMERATE);
		textFont(loadFont("bitstream.vlw"), 15);
		sim = new Simulation();
		smooth();
		translateX = width - (SIMULATION_WIDTH + 5);
		translateY = height - (SIMULATION_HEIGHT + 7);
		simGraphics = createGraphics(SIMULATION_WIDTH, SIMULATION_HEIGHT, P2D);
		simGraphics.smooth();
		rectMode(CORNER);
		stroke(255);
		fill(0);
		rect(translateX - 1, translateY - 1, SIMULATION_WIDTH + 2,
				SIMULATION_HEIGHT + 2);
	}

	public void draw() {
		sim.step();
	}

	public void drawNeuralNet(NeuralNet net) {
		int numOfLayers = net.layers.size();
		int sizex = width - (SIMULATION_WIDTH + 30);
		int sizey = height - 30;

		stroke(0);
		fill(0);
		rect(0, 0, sizex + 30, sizey + 30);

		int spaceBetweenLayers = sizey / (net.layers.size() - 1);
		int spaceBetweenNodes = sizex / (net.largestLayer - 1);

		fill(255);
		Node[] previousNeurons = null;
		int previousXStart = 0;

		for (int i = 0; i < net.layers.size(); i++) {
			stroke(255);
			Node[] neurons = (Node[]) net.layers.get(i);
			int xstart = ((sizex / 2) - ((spaceBetweenNodes * (neurons.length)) / 2))
					+ (spaceBetweenNodes / 2);

			for (int j = 0; j < neurons.length; j++) {
				ellipse(15 + xstart + (spaceBetweenNodes * j), 15
						+ spaceBetweenLayers * i, 10, 10);
			}

			if (i == 0) {
				previousNeurons = neurons;
				previousXStart = xstart;
				continue;
			}

			double midWeight = (net.smallestWeight + net.largestWeight) / 2;
			double midToEdge = net.largestWeight - midWeight;

			for (int x = 0; x < neurons.length; x++) {
				for (int y = 0; y < previousNeurons.length; y++) {
					if (neurons[x].weights[y] == midWeight)
						stroke(0, 0, 255);
					if (neurons[x].weights[y] > midWeight) {
						stroke(0,
								Math.round(255 * ((neurons[x].weights[y] - midWeight) / midToEdge)),
								255);
					} else {
						stroke(Math
								.round(255 * ((midWeight - neurons[x].weights[y]) / midToEdge)),
								0, 255);
					}

					line(15 + xstart + (spaceBetweenNodes * x), 15
							+ spaceBetweenLayers * i, 15 + previousXStart
							+ (spaceBetweenNodes * y), 15 + spaceBetweenLayers
							* (i - 1));
				}
			}

			previousNeurons = neurons;
			previousXStart = xstart;
		}
	}

	void drawSimulation(Ant[] ants, Coordinate[] mines) {
		simGraphics.beginDraw();
		simGraphics.stroke(0);
		simGraphics.fill(0);
		simGraphics.rect(0, 0, SIMULATION_WIDTH - 1, SIMULATION_HEIGHT - 1);
		simGraphics.stroke(255, 0, 0);
		simGraphics.fill(255, 0, 0);
		for (int i = 0; i < mines.length; i++) {
			simGraphics.ellipse((float) mines[i].x, (float) mines[i].y, 4, 4);
		}

		simGraphics.stroke(128, 255, 128);
		simGraphics.fill(0, 0, 255);
		for (int i = 0; i < ants.length; i++) {
			ants[i].draw(simGraphics);
		}
		simGraphics.endDraw();
		image(simGraphics, translateX, translateY);
	}

	void displayInformation(int generation, int max, int min, double average,
			NeuralNet net) {
		stroke(0);
		fill(0);
		rect(width - SIMULATION_WIDTH, 15, SIMULATION_WIDTH, height
				- (SIMULATION_HEIGHT + 8));
		drawText("Evolution", 1);
		drawText("Current generation: " + generation, 3);
		drawText("Previous generation statistics:", 5);
		drawText("Maximum fitness: " + max, 6);
		drawText("Minimum fitness: " + min, 7);
		drawText("Average fitness: " + average, 8);
		drawText("Best network from previous generation:", 10);
		drawText("Largest weight:  " + net.largestWeight, 11);
		drawText("Smallest weight: " + net.smallestWeight, 12);
	}

	void drawText(String text, int line) {
		int translateX = width - SIMULATION_WIDTH;
		int translateY = 15;

		fill(255);
		text(text, translateX, translateY + (15 * line) + (5 * (line - 1)));
	}
}
