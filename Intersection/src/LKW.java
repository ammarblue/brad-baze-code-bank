import processing.core.PFont;
import processing.core.PVector;

public class LKW extends Vehicle {
	int c;
	Vehicle_System vsys;
	PFont arial;
	String stimmt = "+";
	float s30 = sin(radians(30));
	float s60 = sin(radians(60));
	float s220 = sin(radians(30 + 180));
	float s40 = sin(radians(40));
	float s150 = sin(radians(150));
	float s140 = sin(radians(140));
	float c30 = cos(radians(30));
	float c60 = cos(radians(60));
	float c220 = cos(radians(30 + 180));
	float c40 = cos(radians(40));
	float c150 = cos(radians(150));
	float c140 = cos(radians(140));
	float s28 = sin(radians(28));
	float c28 = cos(radians(28));
	float s280 = sin(radians(280));
	float c280 = cos(radians(280));
	float s80 = sin(radians(80));
	float c80 = cos(radians(80));
	LKW(PVector ort, PVector geschw, float maxspeed, float travelspeed,
			float angle_to_go, int c) {
		super(ort, geschw, maxspeed, travelspeed, angle_to_go);
		this.c = c;
		this.c = c;
		this.ort = ort;
		this.geschw = geschw;
		this.travelspeed = travelspeed;
		this.angle_to_go = angle_to_go;
	}

	public void display() {
		// -----------------------display-LKW--overrides Vehicles.display
		float x, y;
		x = ort.x;
		y = ort.y;
		stroke(0);
		strokeWeight(1);
		fill(c);
		pushMatrix();
		translate(x, y);
		rotate(-angle_to_go);
		beginShape();// umriss
		vertex(25 * s30, 25 * c30);
		vertex(-25 * s30, 25 * c30);
		vertex(25 * s220, 25 * c220);
		vertex(25 * s150, 25 * c150);
		endShape();
		stroke(60);
		strokeWeight(6);// fahrerfenster
		line(20 * s28, 20 * c28, -20 * s28, 20 * c28);
		strokeWeight(2);
		line(12 * s280, 12 * c280, 12 * s80, 12 * c80);
		stroke(255);
		strokeWeight(2);
		ellipse(23 * s30, 23 * c30, 2, 2);
		ellipse(-23 * s30, 23 * c30, 2, 2);
		fill(255);
		stroke(c);
		strokeWeight(3);
		popMatrix();
		ellipse(648 + vsys.vehicles.indexOf(this) * 30, 400, 4, 4);
	}
}
