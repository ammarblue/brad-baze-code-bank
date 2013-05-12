import processing.core.PApplet;
import processing.core.PFont;

// Free software: you can redistribute this program and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
public class ann extends PApplet {
	Network neuralnet;
	Sigmoid sig=new Sigmoid();
	PFont font;
	Datum data=new Datum();
	public void setup() {
		size(400, 400);

		font = loadFont("LucidaSans-20.vlw");
		textFont(font);

		sig.setupSigmoid();
		data.loadData();

		neuralnet = new Network(196, 49, 10,this);

		background(220, 204, 255);
		noStroke();
		smooth();
		pushMatrix();
		neuralnet.Draw();
		popMatrix();
		fill(0);
	}

	boolean b_dataloaded = false;

	// left click to test, right click (or ctrl+click on a Mac) to train
	boolean b_train = false, b_test = false;

	public void draw() {
		int response = -1, actual = -1;
		if (!b_dataloaded) {
			data.loadData();
			b_dataloaded = true;
			b_test = true;
		}
		if (b_train) {
			// this allows some fast training without displaying:
			for (int i = 0; i < 500; i++) {
				// select a random training input and train
				int row = (int) floor(random(0, data.training_set.length));
				response = neuralnet.respond(data.training_set[row].inputs);
				actual = data.training_set[row].output;
				neuralnet.train(data.training_set[row].outputs);
			}
		} else if (b_test) {
			int row = (int) floor(random(0, data.testing_set.length));
			response = neuralnet.respond(data.testing_set[row].inputs);
			actual = data.testing_set[row].output;
		}
		if (b_train || b_test) {
			// draw
			background(220, 204, 255);
			noStroke();
			smooth();
			pushMatrix();
			neuralnet.draw();
			popMatrix();
			b_train = b_test = false;
			fill(0);
			text(str(response), 350, 27);
			text(str(actual), 350, 275);
		}
		fill(0);
	}

	public void mousePressed() {
		if (mouseButton == LEFT) {
			b_test = true;
		} else {
			b_train = true;
		}
	}
}
