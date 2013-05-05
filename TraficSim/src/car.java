import java.util.ArrayList;

import processing.core.PVector;

// car
public class car {
	/**
		 * 
		 */
	public car_city_sim car;
	float speed = 0.0f;
	float dir = 0.0f;
	PVector pos = new PVector();
	int target = 0;
	int col;

	car(car_city_sim car_city_sim, float x, float y) {
		car = car_city_sim;
		pos = new PVector(x, y);
		col = car.color(car.random(360), 70, 100);
	}

	// update car
	public void update(ArrayList n, ArrayList c) {
		// get target node
		node a = (node) n.get(target);
		// move toward target
		// float s = 0.4;
		// if(pos.x < a.pos.x) pos.x += s;
		// if(pos.y < a.pos.y) pos.y += s;
		// if(pos.x > a.pos.x) pos.x -= s;
		// if(pos.y > a.pos.y) pos.y -= s;
		// acc/dec
		for (int i = 0; i < c.size(); i++) {
			car g = (car) c.get(i);
			if (g.pos.x != pos.x || g.pos.y != pos.y) {
				if (PVector.angleBetween(new PVector(car_city_sim.sin(dir),
						car_city_sim.cos(dir)), PVector.sub(g.pos, pos)) < car_city_sim.PI * 0.125
						&& pos.dist(g.pos) < 20) {
					speed = car_city_sim.max(0, speed - 0.1f);
					speed = 0;
					// col = color(random(360),70,100);
				} else {
					speed = car_city_sim.min(0.4f, speed + 0.001f);
				}
			}
		}
		// steer
		PVector h = new PVector(car_city_sim.cos(dir), -car_city_sim.sin(dir));
		PVector d = PVector.sub(a.pos, pos);
		d.normalize();
		float e = PVector.angleBetween(h, d);
		if (h.dot(d) > 0)
			dir += 0.03;
		if (h.dot(d) < 0)
			dir -= 0.03;
		// move
		pos.add(new PVector(speed * car_city_sim.sin(dir), speed
				* car_city_sim.cos(dir)));
		// new target
		if (a.pos.dist(pos) < 10.0 && a.children.size() > 0) {
			int b = (int) (car.random(a.children.size()));
			if (b >= 0) {
				target = (Integer) a.children.get(b);
			}
		}
	}

	// draw car
	public void render() {
		car.rectMode(car_city_sim.CENTER);
		car.noStroke();
		car.fill(col);
		car.pushMatrix();
		car.translate(pos.x, pos.y);
		car.rotate(0 - dir);
		car.rect(0, 0, 5, 10);
		car.popMatrix();
	}
}