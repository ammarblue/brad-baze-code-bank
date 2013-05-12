import processing.core.PApplet;


// Free software: you can redistribute this program and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// This class is for each neuron.  It works
// as a feed-forward multilayer perceptron, 
// learning by backpropagation

class Neuron extends PApplet {
	float LEARNING_RATE = 0.01f;
	Neuron[] m_inputs;
	float[] m_weights;
	float m_threshold;
	float m_output;
	float m_error;
	Sigmoid sig=new Sigmoid();
	ann Ann;

	// the input layer of neurons have no inputs:
	Neuron(ann in) {
		Ann=in;
		m_threshold = 0.0f;
		m_error = 0.0f;
		// initial random output
		m_output = sig.lookupSigmoid(random(-5.0f, 5.0f));
	}

	// all other layers (hidden and output) have
	// neural inputs
	Neuron(Neuron[] inputs,ann in) {
		Ann=in;
		m_inputs = new Neuron[inputs.length];
		m_weights = new float[inputs.length];
		for (int i = 0; i < inputs.length; i++) {
			m_inputs[i] = inputs[i];
			m_weights[i] = random(-1.0f, 1.0f);
		}
		m_threshold = random(-1.0f, 1.0f);
		m_error = 0.0f;
		// initial random output
		m_output = sig.lookupSigmoid(random(-5.0f, 5.0f));
	}

	// respond looks at the layer below, and prepares a response:
	float respond() {
		float input = 0.0f;
		for (int i = 0; i < m_inputs.length; i++) {
			input += m_inputs[i].m_output * m_weights[i];
		}
		m_output = sig.lookupSigmoid(input + m_threshold);
		// reset our error value ready for training
		m_error = 0.0f;
		return m_output;
	}

	// find error is used on the output neurons
	void finderror(float desired) {
		m_error = desired - m_output;
	}

	// train adjusts the weights by comparing actual output to correct output
	void train() {
		float delta = (1.0f - m_output) * (1.0f + m_output) * m_error
				* LEARNING_RATE;
		for (int i = 0; i < m_inputs.length; i++) {
			// tell the next layer down what it's doing wrong
			m_inputs[i].m_error += m_weights[i] * m_error;
			// correct our weights
			m_weights[i] += m_inputs[i].m_output * delta;
		}
	}

	void Draw(boolean not_hidden) {
		float level = (0.5f - m_output * 0.5f);
		if (not_hidden) {
			Ann.fill(255 * level);
		} else {
			// merge hidden layer with background color
			Ann.fill(110 + 128 * level, 102 + 128 * level, 127 + 128 * level);
		}
		Ann.ellipse(0, 0, 16, 16);
	}
}
