import processing.core.PApplet;

public class Cell extends PApplet{

	int x;
	int y;
	boolean[] walls = { true, true, true, true };
	int w;
	int visited = 0;
	boolean marked = false;

	Cell(int _x, int _y, int _w) {
		x = _x;
		y = _y;
		w = _w;
	}

	void display(int _weight) {
		noStroke();
		fill(255 - visited);
		rect(x + _weight / 2, y + _weight / 2, w - _weight, w - _weight);
		stroke(0);
		strokeWeight(_weight);

		if (walls[0])
			line(x, y, x + w, y);
		if (walls[1])
			line(x, y + w, x + w, y + w);
		if (walls[2])
			line(x, y, x, y + w);
		if (walls[3])
			line(x + w, y, x + w, y + w);
	}

	void reset() {
		for (int i = 0; i < 4; i++) {
			walls[i] = true;
		}
		visited = 0;
	}
}
