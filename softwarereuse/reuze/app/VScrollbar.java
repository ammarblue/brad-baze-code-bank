package reuze.app;

import processing.core.PGraphics;
import reuze.app.appGUI.MinyWidget;

public class VScrollbar implements appGUI.MinyWidget {
	/**
	 * 
	 */
	private final appGUI appGUI;
	private MinyGUI _parent;
	float pos;
	private Rect _area;
	private boolean _over;

	VScrollbar(appGUI appGUI, MinyGUI parent, Rect area) {
		this.appGUI = appGUI;
		_parent = parent;
		_area = area;
		pos = 0;
		_over = false;
	}

	public Rect getRect() {
		return _area;
	}

	void setPosition(Rect r) {
		_area = r;
	}

	Rect getBox() {
		return new Rect(this.appGUI, _area.x + 2, (int) (_area.y + 2 + pos
				* (_area.h - 24)), _area.w - 4, 20);
	}

	public void onMousePressed() {
		if (this.appGUI.overRect(getBox())) {
			_over = true;
			_parent.getLock(this);
		} else {
			float t = this.appGUI.mouseY - (_area.y + 12);
			t /= _area.h - 24;
			pos = appGUI.constrain(t, 0, 1);
		}
	}

	public void update() {
		if (this.appGUI.mousePressed) {
			if (_over)
				_parent.getLock(this);
		} else
			_parent.releaseLock(this);

		if (_parent.hasLock(this)) {
			float t = this.appGUI.mouseY - (_area.y + 12);
			t /= _area.h - 24;
			pos = appGUI.constrain(t, 0, 1);
		} else
			_over = this.appGUI.overRect(getBox()) && !this.appGUI.mousePressed;
	}

	void display(PGraphics pg, int y) {
		pg.noFill();
		pg.stroke(_parent.fg);
		Rect r = new Rect(this.appGUI, _area);
		Rect pr = _parent.getRect();
		r.x -= pr.x;
		r.y -= pr.y;
		this.appGUI.rect(pg, r);

		pg.fill(_parent.bg);
		Rect b = getBox();
		b.x -= pr.x;
		b.y -= _area.y;
		if (_over || _parent.hasLock(this)) {
			b.x++;
			b.w--;
			b.y++;
			b.h--;
			pg.strokeWeight(2);
			this.appGUI.rect(pg, b);
			pg.strokeWeight(1);
		} else
			this.appGUI.rect(pg, b);
	}

	public void getFocus() {
	}

	public void lostFocus() {
		_over = false;
	}

	public void onKeyPressed() {
	}
}