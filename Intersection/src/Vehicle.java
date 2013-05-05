import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

public class Vehicle extends PApplet{
	// --------------------------------fields------------------------
	PVector ort, geschw;
	float maxspeed, travelspeed, angle_to_go;
	Vehicle_System vsys;
	PFont arial;
	String stimmt = "+";

	Vehicle(PVector ort, PVector geschw, float maxspeed, float travelspeed,
			float angle_to_go) {
		// ------------------------------------constructor---------------
		this.ort = ort;
		this.geschw = geschw;
		this.maxspeed = maxspeed;
		this.travelspeed = travelspeed;
		this.angle_to_go = angle_to_go;
		boolean is_turning = false;
	}

	// --------------------------------------adjust ort (get the right lane)
	void adjust_ort(float winkel, PVector posit) {
		if (abs(winkel - 0.75f * TWO_PI) < 0.01f) {
			posit.y = 280;
			// horiz. nachlinks
		} else if (abs(winkel - 0.25f * TWO_PI) < 0.01f
				|| abs(winkel - 1.25f * TWO_PI) < 0.01f) {
			posit.y = 320;
			// horiz. nach rechts
		} else if (abs(winkel - 0.5f * TWO_PI) < 0.01f) {
			posit.x = 320;
			// vertikal nach oben
		} else if (abs(winkel) < 0.01f || abs(winkel - TWO_PI) < 0.01f) {
			posit.x = 280;
			// vertik. nach unten
		}
		// take care of rounding errors
		if (abs(geschw.x) < 0.01)
			geschw.x = 0;
		if (abs(geschw.y) < 0.01)
			geschw.y = 0;
	}

	// ------------------------------check_if_crossing ((ev. nicth void, sondern
	// boolean "wait" bis kreuzung frei, solange anhalten.
	void check_if_crossing() {
		Boolean from_left = abs(geschw.x) > 0.001f && geschw.x > 0
				&& (300 - ort.x) < 90 && (300 - ort.x) > 40;
		Boolean from_right = abs(geschw.x) > 0.001f && geschw.x < 0
				&& (ort.x - 300) < 90 && (ort.x - 300) > 40;
		Boolean from_top = abs(geschw.y) > 0.001f && geschw.y > 0
				&& (300 - ort.y) < 90 && (300 - ort.y) > 40;
		Boolean from_down = abs(geschw.y) > 0.001f && geschw.y < 0
				&& (ort.y - 300) < 90 && (ort.y - 300) > 40;
		Boolean isin_ = false;
		PVector cross_right = new PVector(0, 0, 0);
		PVector center = new PVector(300, 300, 0);
		if (from_left || from_right || from_top || from_down) {
			this.acc(0.5f);// crossing ahead, now check for cars and drive
							// slowly!
			// ---------------------------------------------TEXTAUSGABE
			fill(255);
			text(stimmt, 650 + vsys.vehicles.indexOf(this) * 30, 440);
			for (int i = 0; i < vsys.vehicles.size(); i++) {
				Vehicle v = (Vehicle) vsys.vehicles.get(i);
				if (abs(this.geschw.x) > 0.0001f) {
					if (this.geschw.x > 0)
					// little difference from zero is always there,
					// because a float is never exactly zero
					{
						cross_right.set(340, 380, 0);
						// setting the place, where to look for other cars.

						// println("von links: "+cross_right.x+" "+cross_right.y);
					} else if (this.geschw.x < 0) {
						cross_right.set(260, 220, 0);
						// println("von rechts: "+cross_right.x+" "+cross_right.y);
					}
				}
				if (abs(this.geschw.y) > 0.001) {
					if (this.geschw.y < 0) {
						cross_right.set(380, 260, 0);
						// println("von unten: "+cross_right.x+" "+cross_right.y);
					} else if (this.geschw.y > 0) {
						cross_right.set(220, 340, 0);
						// println("von oben: "+cross_right.x+" "+cross_right.y);
					}
				}
				isin_ = isin(cross_right, v.ort, 80);
				// is there a car with priority on the right side?

				if (isin_ || isin(center, v.ort, 80)) {
					// -----------------------------------------------TEXTAUSGABE
					text(stimmt, 650 + vsys.vehicles.indexOf(this) * 30, 460);
					this.acc(0.01f);// wait, but not zero because
					// so it is easier to accelerate again
				} else {
					acc(geschw.mag() * 1.05f);
					geschw.limit(travelspeed);
				}// end else
			}// end for
		}// end if
	}// end void

	// ----------------------------------------------------------check_car_before
	void check_car_before() {
		int ahead;
		Boolean isin_ahead;
		PVector vnorm = new PVector(0, 0, 0);
		PVector ort_ahead = new PVector(0, 0, 0);
		PVector v40 = new PVector(0, 0, 0);

		vnorm.set(geschw);
		vnorm.normalize();
		v40.set(vnorm);
		v40.mult(40);// from vehicles 40 pixels ahead
		// a normal-vector in direction of travel
		for (int i = 0; i < vsys.vehicles.size(); i++) {
			Vehicle v = (Vehicle) vsys.vehicles.get(i);
			ort_ahead = PVector.add(v40, this.ort);// position 40 pixels ahead
			isin_ahead = isin(ort_ahead, v.ort, 60);

			if (isin_ahead) {
				acc(0.01f);//
				// ----------------------TEXTAUSGABE
				text(stimmt, 650 + vsys.vehicles.indexOf(this) * 30, 420);
			} else {
				// acc(v.travelspeed);
				acc(geschw.mag() * 1.01f);
				geschw.limit(travelspeed);
			}// end else
		}// end for
	}// end else

	// -------------------------------------------boolean isin------

	Boolean isin(PVector vec_center, PVector vec_test, float diam) {
		boolean test = false;
		diam = diam / 2;
		if (abs(vec_center.x - vec_test.x) < diam
				&& abs(vec_center.y - vec_test.y) < diam) {
			test = true;
		}
		return test;

	}

	// ------------------------------acc--------------------------------
	void acc(float wanted_speed) {
		// very simple for a start,not smooth!
		geschw.set(wanted_speed * sin(angle_to_go), wanted_speed
				* cos(angle_to_go), 0);
	}

	// to override by subclasses----------------display
	void display() {

		stroke(0);
		fill(200);
		ellipse(ort.x, ort.y, 5, 5);

	}

	// -------------------------------------abbiegen
	void abbiegen(float turn_angle) {
		// println(turn_angle);
		if (abs(turn_angle - PI / 2) < 0.001) {
			// turning to the left
			// car coming on the other side?
			PVector vnorm = new PVector(0, 0, 0);
			PVector ort_ahead = new PVector(0, 0, 0);
			PVector v40 = new PVector(0, 0, 0);
			PVector senkr = new PVector(0, 0, 1);
			vnorm.set(sin(angle_to_go), cos(angle_to_go), 0);
			// vnorm.normalize();
			v40.set(vnorm);
			v40.mult(100);// from vehicles 100 pixels ahead
			vnorm.set(vnorm.y, -vnorm.x, 0);
			vnorm.mult(40);
			v40.add(vnorm);
			ort_ahead.set(v40);
			ort_ahead.add(ort);
			for (int i = 0; i < vsys.vehicles.size(); i++) {
				Vehicle v = (Vehicle) vsys.vehicles.get(i);
				if (isin(ort_ahead, v.ort, 90)) {
					turn_angle = 0;
					// -----------------------------------------------TEXTAUSGABE
					text(stimmt, 650 + vsys.vehicles.indexOf(this) * 30, 480);
					// better go straight ahead
				}
			}
		}

		float vv = geschw.mag();
		angle_to_go = angle_to_go + turn_angle;
		if (angle_to_go > TWO_PI)
			angle_to_go = angle_to_go - TWO_PI;
		geschw.set(vv * sin(angle_to_go), vv * cos(angle_to_go), 0);
	}

	// ------------------------update (vehicle)
	void update() {

		checkbounds(ort);
		adjust_ort(angle_to_go, ort);
		check_if_crossing();
		check_car_before();
		ort.set(ort.x + geschw.x, ort.y + geschw.y, 0);

		if (millis() % 20 == 0)
			adjust_angle(angle_to_go);
	}

	// }
	// ------------------------------checkbounds
	void checkbounds(PVector ort) {

		if (ort.x > width + 20)
			ort.set(-20, ort.y, 0);
		if (ort.y < -20)
			ort.set(ort.x, height + 20, 0);
		if (ort.x < -20)
			ort.set(width + 20, ort.y, 0);
		if (ort.y > height + 20)
			ort.set(ort.x, -20, 0);
		travelspeed = random(1.5f, 2.0f);

	}

	// --------------------------------adjust_angle
	void adjust_angle(float winkel) {
		winkel = (float) (TWO_PI * (round(100 * winkel / TWO_PI))) / 100;

	}
}
