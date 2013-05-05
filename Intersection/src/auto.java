import processing.core.PApplet;
import processing.core.PFont;

/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/6989*@* */
/* !do not delete the line above, required for linking your tweak if you re-upload */

public class auto extends PApplet {

	Vehicle_System vsys;
	PFont arial;
	String stimmt = "+";
	boolean state = false;

	public void setup() {

		size(900, 600);
		// smooth();
		rectMode(CENTER);
		frameRate(30);
		ellipseMode(CENTER);
		vsys = new Vehicle_System(5);
		arial = loadFont("ArialMT-14.vlw");
		textFont(arial, 14);
		state = true;
	}

	public void draw() {
		if (state) {
			vsys.backgr();
			vsys.update();
		}
	}

}
