import processing.core.PApplet;
import processing.core.PFont;

/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/93855*@* */
/* !do not delete the line above, required for linking your tweak if you re-upload */
//camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ)
public class US_Population extends PApplet {
	PFont a_aaron;
	DataDisplay d=new DataDisplay(this);
	public void setup() {
		size(600, 600, P2D);
		d.loadData("us_pop.txt");
		a_aaron = createFont("Arial", 32, true);
	}

	public void draw() {
		background(0);
		fill(255);
		textAlign(CENTER);
		textFont(a_aaron);
		text("U.S. Population 1776 - 2010", width / 2, 35);

		int year1;
		if (mouseX > 50 && mouseX < width - 50) {
			year1 = (int) map(mouseX, 50, width - 50, d.data.size(), 0);
		} else if (mouseX <= 50) {
			year1 = d.data.size();
		} else {
			year1 = 0;
		}
		d.drawData(year1);
	}
}
