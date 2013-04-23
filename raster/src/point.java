import java.awt.Color;

public class point {
	public double x;
	public double y;
	public double day;
	public double value;
	public Color color;

	public point(double x, double y, double day, double value) {
		this.x = x;
		this.y = y;
		this.day = day;
		this.value = value;
		this.color = getColor();
	}

	public point() {

	}

	public Color getColor() {
		Color color = null;
		if (this.value < 5) {
			color = new Color(250, 253, 40);
		} else if (value >= 5 && value < 10) {
			color = new Color(253, 220, 40);
		} else if (value >= 10 && value < 15) {
			color = new Color(253, 195, 40);
		} else if (value >= 15 && value < 20) {
			color = new Color(253, 135, 40);
		} else if (value >= 20 && value < 25) {
			color = new Color(253, 75, 40);
		} else if (value >= 25) {
			color = new Color(253, 40, 40);
		}

		return color;
	}
}
