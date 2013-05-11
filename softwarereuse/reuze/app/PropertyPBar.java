package reuze.app;

import processing.core.PGraphics;

// ///////////////////////////////////////////////////////////////////////////////
// BWB032513///////////////////////////////////////////////////////////////////////
// ///////////////////////////////////////////////////////////////////////////////
class PropertyPBar extends Property {
	/**
	 * 
	 */
	private final appGUI appGUI;
	MinyInteger _value;
	private int maxBar;
	private int steps;
	private float ratio;
	private int stepVal;
	private int s_size;
	private int count = 0;

	PropertyPBar(appGUI appGUI, String name, int maxBar, int steps) {
		super(appGUI, name);
		this.appGUI = appGUI;
		this.maxBar = maxBar;
		this.steps = steps;
	}

	float getPos() {
		return 0.0f;
	}

	void setPos(float v) {
	}

	private Rect getBox(int xoff) {
		float fpos = (_x + _w * 0.4f + 8) + (_w * 0.6f - 18) * 0;
		return new Rect(this.appGUI, (int) fpos - 5, _y + 8, xoff, 10);
	}

	public void display(PGraphics pg, int y) {
		super.display(pg, y);
		pg.fill(_parent.bg);
		pg.rect(_w * 0.4f + 3, y + 8, _w * 0.6f - 8, 10);
		pg.fill(0, 0, 255);
		pg.stroke(_parent.fg);
		s_size = (int) (_w * 0.6f - 8);
		s_size /= steps * (maxBar / steps);
		Rect b = getBox(count);
		b.x -= _x;
		b.y += y - _y;
		this.appGUI.rect(pg, b);
		pg.stroke(_parent.bg);
		pg.strokeWeight(1);
		if (count <= (_w * 0.6f - 8)) {
			count = ((_value.getValue() * s_size) + 2);
		} else {
			count = 10;
		}
	}
}