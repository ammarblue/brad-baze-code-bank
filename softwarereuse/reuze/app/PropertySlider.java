package reuze.app;

import processing.core.PGraphics;

public class PropertySlider extends Property {
	/**
	 * 
	 */
	private final appGUI appGUI;
	private boolean _over;

	PropertySlider(appGUI appGUI, String name) {
		super(appGUI, name);
		this.appGUI = appGUI;
	}

	float getPos() {
		return 0.0f;
	}

	void setPos(float v) {
	}

	private Rect getBox() {
		float fpos = (_x + _w * 0.4f + 8) + (_w * 0.6f - 18) * getPos();
		return new Rect(this.appGUI, (int) fpos - 5, _y + 8, 10, 10);
	}

	public void update() {
		if (this.appGUI.mousePressed) {
			if (_over)
				_parent.getLock(this);
		} else
			_parent.releaseLock(this);

		if (_parent.hasLock(this)) {
			float t = this.appGUI.mouseX - (_x + _w * 0.4f + 8);
			t /= _w * 0.6 - 18;
			setPos(t);
		} else
			_over = this.appGUI.overRect(getBox()) && !this.appGUI.mousePressed;
	}

	public void onMousePressed() {
		if (this.appGUI.overRect(getBox())) {
			_over = true;
			_parent.getLock(this);
		} else if (this.appGUI.overRect((int) (_x + _w * 0.4 + 3), _y + 8, _x
				+ _w - 5, _y + 15)) {
			float t = this.appGUI.mouseX - (_x + _w * 0.4f + 8);
			t /= _w * 0.6 - 18;
			setPos(t);
		}
	}

	public void display(PGraphics pg, int y) {
		super.display(pg, y);
		// pg.fill(_parent.bg);
		// pg.noStroke();
		// pg.rect(_w*0.4f + 3, y, _w *0.6f - 8, 20);
		pg.stroke(_parent.fg);
		pg.line(_w * 0.4f + 3, y + 13, _w - 5, y + 13);
		if (_over || _parent.hasLock(this))
			pg.strokeWeight(2);
		Rect b = getBox();
		b.x -= _x;
		b.y += y - _y;
		this.appGUI.rect(pg, b);
		pg.strokeWeight(1);
	}
}