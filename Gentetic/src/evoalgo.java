import processing.core.PApplet;

public class evoalgo extends PApplet {
	Population pop;

	public void setup() {
		size(400, 400, P3D);
		pop = new Population(this);
	}

	public void draw() {
		// evolution is slowed to
		// make it easier to see
		if (frameCount % 10 == 0) {
			pop.evolve();
		}
		background(336699);
		lights();
		noStroke();
		fill(255);
		for (int i = 0; i < pop.m_pop.length; i++) {
			// this draws all the members of the population at
			// any one time step
			// fitter individuals appear to the top right
			pushMatrix();
			scale(0.1f, 0.1f, 0.1f);
			translate(width * (i % 10), height * (9 - (i / 10)));
			translate(width / 2, height / 2, 0);
			rotateY(0.01f * frameCount);
			pop.m_pop[i].Draw();
			popMatrix();
		}
	}

	public void mousePressed() {
		pop = new Population(this);
	}
}
