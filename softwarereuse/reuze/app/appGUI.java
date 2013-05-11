package reuze.app;

import processing.core.*;
import java.io.*;
import java.util.*;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class appGUI extends PApplet {
	/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/11032*@* */
	/*
	 * !do not delete the line above, required for linking your tweak if you
	 * re-upload
	 */
	/*
	 * TinyGUI by Christophe Guebert A small example to illustrate the MinyGUI
	 * Library. Now with color chooser, gradient and graph editors ! (it's click
	 * in color area to add a new point to gradient, drag point to far left to
	 * delete, point 0 cannot be moved/deleted) Sorry for the lack of comments
	 * in the code...
	 */

	MinyGUI gui;

	float time, lastTime;
	MinyBoolean running;
	MinyInteger speed;
	MinyFloat stWidth, rSize;
	MinyString timeCaption;
	MinyColor borderColor;

	InterpolatedFloat rotation;
	ColorGradient gradient;

	public void mousePressed() {
		gui.onMousePressed();
	}

	public void keyPressed() {
		gui.onKeyPressed();
	}

	public void keyReleased() {
		gui.onKeyReleased();
	}

	PImage img;

	/*
	 * public void setup() { size(600, 400); smooth();
	 * img=loadImage("../data/mearth.jpg"); time = 0; running = new
	 * MinyBoolean(true);this. speed = new MinyInteger(2); timeCaption = new
	 * MinyString("0.0"); stWidth = new MinyFloat(2.0f); rSize = new
	 * MinyFloat(1.0f); borderColor = new MinyColor(color(192)); rotation = new
	 * InterpolatedFloat(1); rotation.add(0, 0); rotation.add(1.5f, -PI);
	 * rotation.add(3.0f, PI); rotation.add(4.0f, 2*PI); rotation.add(6.0f, 0);
	 * 
	 * gradient = new ColorGradient(color(0)); gradient.add(0, color(0, 1));
	 * gradient.add(0.25f, color(255, 0, 0)); gradient.add(0.5f, color(0, 255,
	 * 0)); gradient.add(0.75f, color(0, 0, 255)); gradient.add(1, color(255));
	 * 
	 * gui = new MinyGUI(0, 0, 200, height); new PropertyButton("Start", new
	 * TestButton()).add(gui); new PropertyButton("Tester", new
	 * TestButton()).add(gui); new PropertyButtonImage("../data/particle.png",
	 * new TestButton()).add(gui); new PropertyCheckBox("Running",
	 * running).add(gui); new PropertyList("Speed", speed,
	 * "slowest;slow;normal;fast;fastest").add(gui); new PropertyDisplay("Time",
	 * timeCaption).add(gui); new PropertyEditFloat("Border width",
	 * stWidth).add(gui); new PropertySliderFloat("Rect size", rSize, 0.5f,
	 * 2.0f).add(gui); new PropertyColorChooser("Fill color",
	 * borderColor).add(gui); new PropertyGraph("Rotation", rotation).add(gui);
	 * new PropertyGradient("Gradient", gradient).add(gui);
	 * 
	 * gui.fg = color(0); gui.bg = color(255); gui.selectColor = color(196); for
	 * (Property p:gui) System.out.println(p.get().getString());
	 * //gui.drawBackground=false; //uncomment for image background }
	 */

	public void draw() {
		background(0);

		float t = millis() / 1000.0f;
		float dt = t - lastTime;
		dt *= pow(2, speed.getValue() - 2);
		if (running.getValue())
			time += dt;

		float m = rotation.getXMax();
		if (m == 0)
			m = 1;
		if (time > m)
			running.setValue(false);
		lastTime = t;
		timeCaption.setValue(nf(time, 0, 2));

		pushMatrix();
		translate(400, 200);
		rotate(rotation.get(time));
		scale(rSize.getValue());
		float f = stWidth.getValue();
		if (f < 0)
			f = 0;
		if (f > 20)
			f = 20;
		strokeWeight(f);
		stroke(borderColor.getValue());
		fill(gradient.get(time / m));
		rect(-50, -30, 100, 60);
		popMatrix();
		image(img, 0, 0, 200, height);
		gui.display();
	}

	static PImage _imgChecker, _imgSmallChecker;

	interface ButtonCallback {
		void onButtonPressed();
	}

	interface MinyWidget {
		Rect getRect();

		void update();

		void getFocus();

		void lostFocus();

		void onMousePressed();

		void onKeyPressed();
	}

	interface FrameCreator {
		void onCloseFrame(MinyFrame frame);
	}

	// ---------------------------------------------------
	interface MinyValue {
		String getString();
	}

	boolean overRect(int x, int y, int width, int height) {
		if (mouseX >= x && mouseX <= x + width && mouseY >= y
				&& mouseY <= y + height)
			return true;
		else
			return false;
	}

	boolean overRect(Rect r) {
		return overRect(r.x, r.y, r.w, r.h);
	}

	void rect(Rect r) {
		rect(r.x, r.y, r.w, r.h);
	}

	void rect(PGraphics pg, Rect r) {
		pg.rect(r.x, r.y, r.w, r.h);
	}

	void text(String t, Rect r) {
		text(t, r.x, r.y, r.w, r.h);
	}

	void text(PGraphics pg, String t, Rect r) {
		pg.text(t, r.x, r.y, r.w, r.h);
	}

	public int sketchWidth() {
		return 480;
	}

	public int sketchHeight() {
		return 800;
	}
	// public String sketchRenderer() { return OPENGL; }
}
