package reuze.demo;
import processing.core.PApplet;
import processing.core.PImage;

public class demoImageExtrude  extends PApplet {
	PImage extrude;
	int[][] values;
	float angle = 0;

	public void setup() {
	  size(640, 360, P3D);
	  // Load the image into a new array
	  extrude = loadImage("../data/pencils.jpg");
	  extrude.loadPixels();
	  values = new int[extrude.width][extrude.height];
	  for (int y = 0; y < extrude.height; y++) {
	    for (int x = 0; x < extrude.width; x++) {
	      int pixel = extrude.get(x, y);
	      values[x][y] = (int)brightness(pixel);
	    }
	  }
	}

	public void draw() {
	  background(0);
	  
	  // Update the angle
	  angle += 0.005;
	  
	  // Rotate around the center axis
	  translate(width/2, 0, -128);
	  rotateY(angle);  
	  translate(-extrude.width/2, 100, -128);
	  // Display the image mass
	  for (int y = 0; y < extrude.height; y++) {
	    for (int x = 0; x < extrude.width; x++) {
	      stroke(extrude.get(x, y));
	      point(x, y, -values[x][y]);
	    }
	  }

	}
}