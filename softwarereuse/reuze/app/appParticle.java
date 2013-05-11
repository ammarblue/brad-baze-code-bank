package reuze.app;

import java.util.ArrayList;

import com.software.reuze.gb_Vector3;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class appParticle extends PApplet {
	ParticleSystem ps;

	public void setup() {
		size(640, 360);
		ps = new ParticleSystem(1, new gb_Vector3(width / 2, height / 2, 0));
		smooth();
	}

	public void draw() {
		background(0);
		ps.run();
		ps.addParticle(mouseX, mouseY);
	}

	// A class to describe a group of Particles
	// An ArrayList is used to manage the list of Particles

	class ParticleSystem {

		ArrayList<Particle> particles; // An arraylist for all the particles
		gb_Vector3 origin; // An origin point for where particles are born

		ParticleSystem(int num, gb_Vector3 v) {
			particles = new ArrayList<Particle>(); // Initialize the arraylist
			origin = new gb_Vector3(v); // Store the origin point
			for (int i = 0; i < num; i++) {
				particles.add(new Particle(origin)); // Add "num" amount of
														// particles to the
														// arraylist
			}
		}

		void run() {
			// Cycle through the ArrayList backwards b/c we are deleting
			for (int i = particles.size() - 1; i >= 0; i--) {
				Particle p = (Particle) particles.get(i);
				p.run();
				if (p.dead()) {
					particles.remove(i);
				}
			}
		}

		void addParticle() {
			particles.add(new Particle(origin));
		}

		void addParticle(float x, float y) {
			particles.add(new Particle(new gb_Vector3(x, y, 0)));
		}

		void addParticle(Particle p) {
			particles.add(p);
		}

		// A method to test if the particle system still has particles
		boolean dead() {
			if (particles.isEmpty()) {
				return true;
			} else {
				return false;
			}
		}

	}

	// A simple Particle class

	class Particle {
		gb_Vector3 loc;
		gb_Vector3 vel;
		gb_Vector3 acc;
		float r;
		float timer;

		// Another constructor (the one we are using here)
		Particle(gb_Vector3 l) {
			acc = new gb_Vector3(0, 0.05f, 0);
			vel = new gb_Vector3(random(-1, 1), random(-2, 0), 0);
			loc = new gb_Vector3(l);
			r = 10.0f;
			timer = 100.0f;
		}

		void run() {
			update();
			render();
		}

		// Method to update location
		void update() {
			vel.add(acc);
			loc.add(vel);
			timer -= 1.0;
		}

		// Method to display
		void render() {
			ellipseMode(CENTER);
			stroke(0, 0, 255, 255);
			fill(255, 255, 0, 255);
			ellipse(loc.x, loc.y, r, r);
			displayVector(vel, loc.x, loc.y, 10);
		}

		// Is the particle still useful?
		boolean dead() {
			if (timer <= 0.0) {
				return true;
			} else {
				return false;
			}
		}

		void displayVector(gb_Vector3 v, float x, float y, float scayl) {
			pushMatrix();
			float arrowsize = 4;
			// Translate to location to render vector
			translate(x, y);
			stroke(255);
			// Call vector heading function to get direction (note that pointing
			// up is a heading of 0) and rotate
			rotate(v.heading2D());
			// Calculate length of vector & scale it to be bigger or smaller if
			// necessary
			float len = v.len() * scayl;
			// Draw three lines to make an arrow (draw pointing up since we've
			// rotate to the proper direction)
			line(0, 0, len, 0);
			line(len, 0, len - arrowsize, +arrowsize / 2);
			line(len, 0, len - arrowsize, -arrowsize / 2);
			popMatrix();
		}

	}
}
