import processing.core.PFont;
import processing.core.PVector;

public class PKW extends Vehicle {
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
	PKW(PVector ort, PVector geschw, float maxspeed, float travelspeed,
			float angle_to_go, int c) {
		super(ort, geschw, maxspeed, travelspeed, angle_to_go);
		this.c = c;
		this.ort = ort;
		this.geschw = geschw;
		this.travelspeed = travelspeed;
		this.angle_to_go = angle_to_go;
	}

	void display() {
		// ------------------display PKW---overrides Vehicles.display
		float x, y;
		x = ort.x;
		y = ort.y;
		stroke(0);
		strokeWeight(1);
		fill(c);
		pushMatrix();
		translate(x, y);
		rotate(-angle_to_go);

		beginShape();
		vertex(20 * s30, 20 * c30);
		vertex(-20 * s30, 20 * c30);
		vertex(20 * s220, 20 * c220);
		vertex(20 * s150, 20 * c150);
		endShape();
		stroke(60);
		strokeWeight(6);
		line(7 * s60, 7 * c60, -7 * s60, 7 * c60);
		strokeWeight(4);
		line(8 * s220, 8 * c220, 8 * s140, 8 * c140);
		stroke(255);
		strokeWeight(2);
		ellipse(18 * s30, 18 * c30, 2, 2);
		ellipse(-18 * s30, 18 * c30, 2, 2);
		fill(255);
		stroke(c);
		strokeWeight(3);

		popMatrix();
		ellipse(648 + vsys.vehicles.indexOf(this) * 30, 400, 4, 4);

	}
}
