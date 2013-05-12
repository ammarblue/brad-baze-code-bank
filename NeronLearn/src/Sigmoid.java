import processing.core.PApplet;

// Simple neural nets: sigmoid functions
// (c) Alasdair Turner 2009

// Free software: you can redistribute this program and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// a sigmoid function is the neuron's response to inputs
// the sigmoidal response ranges from -1.0 to 1.0

// for example, the weighted sum of inputs might be "2.1"
// our response would be lookupSigmoid(2.1) = 0.970

// this is a look up table for sigmoid (neural response) values
// which is valid from -5.0 to 5.0
public class Sigmoid extends PApplet {
	float[] g_sigmoid = new float[200];
	Sigmoid(){}
	// this function precalculate a sigmoid (response) function
	void setupSigmoid() {
		for (int i = 0; i < 200; i++) {
			float x = (i / 20.0f) - 5.0f;
			g_sigmoid[i] = 2.0f / (1.0f + exp(-2.0f * x)) - 1.0f;
		}
	}

	// once the sigmoid has been set up, this function accesses it:
	float lookupSigmoid(float x) {
		return g_sigmoid[constrain((int) floor((x + 5.0f) * 20.0f), 0, 199)];
	}

}