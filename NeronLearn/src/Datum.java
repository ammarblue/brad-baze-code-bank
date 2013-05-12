import processing.core.PApplet;

// Free software: you can redistribute this program and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// this uses the MNIST database of handwritten digits
// http://yann.lecun.com/exdb/mnist/ (accessed 04.06.09)
// Yann LeCun and Corinna Cortes

// note: I have reduced the originals to 14 x 14 from 28 x 28

public class Datum extends PApplet {
	Datum[] training_set;
	Datum[] testing_set;
	float[] inputs;
	float[] outputs;
	int output;

	Datum() {
		inputs = new float[196];
		outputs = new float[10];
	}

	void imageLoad(byte[] images, int offset) {
		for (int i = 0; i < 196; i++) {
			// note, you must use int() to convert rather than (int) to cast:
			inputs[i] = (int) (images[i + offset]) / 128.0f - 1.0f;
		}
	}

	void labelLoad(byte[] labels, int offset) {
		output = (int) (labels[offset]);
		for (int i = 0; i < 10; i++) {
			if (i == output) {
				outputs[i] = 1.0f;
			} else {
				outputs[i] = -1.0f;
			}
		}
	}

	void loadData() {
		byte[] images = loadBytes("t10k-images-14x14.idx3-ubyte");
		byte[] labels = loadBytes("t10k-labels.idx1-ubyte");

		training_set = new Datum[8000];
		int tr_pos = 0;
		testing_set = new Datum[2000];
		int te_pos = 0;

		for (int i = 0; i < 10000; i++) {
			if (i % 5 != 0) {
				training_set[tr_pos] = new Datum();
				training_set[tr_pos].imageLoad(images, 16 + i * 196);
				training_set[tr_pos].labelLoad(labels, 8 + i);
				tr_pos++;
			} else {
				testing_set[te_pos] = new Datum();
				testing_set[te_pos].imageLoad(images, 16 + i * 196);
				testing_set[te_pos].labelLoad(labels, 8 + i);
				te_pos++;
			}
		}
	}
}
