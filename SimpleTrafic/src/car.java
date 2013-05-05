import java.util.ArrayList;

import processing.core.PVector;


// car
public class car {
	/**
	 * 
	 */
	private simpleCity car;
	PVector pos = new PVector();
	int target = 0;

	car(simpleCity simpleCity, float x, float y) {
		car = simpleCity;
		pos = new PVector(x, y);
	}

	// update car
	void update(ArrayList n) {
		// get target node
		node a = (node) n.get(target);
		// move toward target
		float s = 0.4f;
		if (pos.x < a.pos.x)
			pos.x += s;
		if (pos.y < a.pos.y)
			pos.y += s;
		if (pos.x > a.pos.x)
			pos.x -= s;
		if (pos.y > a.pos.y)
			pos.y -= s;
		// new target
		if (a.pos.dist(pos) < 0.6 && a.children.size() > 0) {
			int b = (int) (car.random(a.children.size()));
			if (b >= 0) {
				target = (Integer) a.children.get(b);
			}
		}
	}

	// draw car
	void render() {
		car.rectMode(simpleCity.CENTER);
		car.noStroke();
		car.fill(0, 0, 0);
		car.rect(pos.x, pos.y, 5, 5);
	}
}