package reuze.app;

import reuze.app.appGUI.MinyWidget;

public class MinyFrame implements appGUI.MinyWidget {
	/**
	 * 
	 */
	private final appGUI appGUI;
	protected Rect _box;
	protected MinyGUI _parent;
	protected int _minW, _minH;
	private boolean _moveable, _resizeable, _hasFocus, _movingFrame;
	private Rect _saved;
	private int _resizing, _clickX, _clickY;
	String _name;

	MinyFrame(appGUI appGUI, MinyGUI parent, String name) {
		this.appGUI = appGUI;
		_parent = parent;
		_name = name;
		_moveable = _resizeable = _hasFocus = _movingFrame = false;
		_resizing = -1;
		_minW = 100;
		_minH = 8;
	}

	void onClose() {
	}

	void onResize() {
	}

	void setRect(Rect r) {
		_box = r;
	}

	public Rect getRect() {
		return _box;
	}

	void setMoveable(boolean b) {
		_moveable = b;
	}

	void setResizeable(boolean b) {
		_resizeable = b;
	}

	void setClientAreaSize(int w, int h) {
		_box.w = w + 2;
		_box.h = h + 2;
		if (_moveable)
			_box.h += 18;
		if (_resizeable) {
			_box.w += 6;
			_box.h += 6;
		}

		validatePosition();
	}

	Rect getClientArea() {
		Rect r = new Rect(this.appGUI, _box);
		r.grow(-1);
		if (_resizeable)
			r.grow(-3);
		if (_moveable) {
			r.y += 18;
			r.h -= 18;
		}
		return r;
	}

	void placeAt(int x, int y, int w, int h) {
		w = w + 2;
		h = h + 2;
		if (_moveable)
			h += 18;
		if (_resizeable) {
			w += 6;
			h += 6;
		}

		if (x + w > this.appGUI.width - 5)
			x = this.appGUI.width - w - 5;
		if (y + h > this.appGUI.height - 5)
			y = this.appGUI.height - h - 5;

		_box = new Rect(this.appGUI, x, y, w, h);
		validatePosition();
	}

	void validatePosition() {
		_box.w = appGUI.constrain(_box.w, _minW, this.appGUI.width - 1);
		_box.h = appGUI.constrain(_box.h, _minH, this.appGUI.height - 1);
		_box.x = appGUI.max(0, _box.x);
		_box.y = appGUI.max(0, _box.y);
		if (_box.x + _box.w + 1 > this.appGUI.width)
			_box.x = this.appGUI.width - _box.w - 1;
		if (_box.y + _box.h + 1 > this.appGUI.height)
			_box.y = this.appGUI.height - _box.h - 1;
	}

	public void update() {
		if (!_parent.hasLock(this)) {
			if (_resizeable) {
				Rect r = new Rect(this.appGUI, _box);
				r.grow(-4);
				/*
				 * if(overRect(_box) && !overRect(r)) cursor(CROSS); else
				 * cursor(ARROW);
				 */
			}
			return;
		}

		if (this.appGUI.mousePressed) {
			int oldW = _box.w, oldH = _box.h;
			if (_resizing != -1
					&& (this.appGUI.pmouseX != this.appGUI.mouseX || this.appGUI.pmouseY != this.appGUI.mouseY)) {
				if ((_resizing & 1) != 0) {
					_box.x = appGUI.min(_saved.x + _saved.w - _minW, _saved.x
							+ this.appGUI.mouseX - _clickX);
					_box.w = appGUI.max(_minW, _saved.w - this.appGUI.mouseX
							+ _clickX);
				} else if ((_resizing & 2) != 0) {
					_box.x = _saved.x;
					_box.w = appGUI.max(_minW, _saved.w + this.appGUI.mouseX
							- _clickX);
				}
				if ((_resizing & 4) != 0) {
					_box.y = appGUI.min(_saved.y + _saved.h - _minH, _saved.y
							+ this.appGUI.mouseY - _clickY);
					_box.h = appGUI.max(_minH, _saved.h - this.appGUI.mouseY
							+ _clickY);
				} else if ((_resizing & 8) != 0) {
					_box.y = _saved.y;
					_box.h = appGUI.max(_minH, _saved.h + this.appGUI.mouseY
							- _clickY);
				}
			}

			if (_movingFrame) {
				_box.x = _saved.x + this.appGUI.mouseX - _clickX;
				_box.y = _saved.y + this.appGUI.mouseY - _clickY;
			}

			validatePosition();
			if (_box.w != oldW || _box.h != oldH)
				onResize();
		} else if (_resizing != -1 || _movingFrame) {
			_parent.releaseLock(this);
			_resizing = -1;
			_movingFrame = false;
			// cursor(ARROW);
		}
	}

	public void display() {
		this.appGUI.fill(_parent.bg);
		this.appGUI.stroke(_parent.fg);
		Rect r = new Rect(this.appGUI, _box);
		if (_resizeable && _moveable) {
			this.appGUI.noFill();
			this.appGUI.rect(r);
			r.grow(-1);
			this.appGUI.stroke(_parent.bg);
			this.appGUI.rect(r);
			r.grow(-1);
			this.appGUI.rect(r);
			r.grow(-1);
			r.y += 18;
			r.h -= 18;
			this.appGUI.stroke(_parent.fg);
			this.appGUI.fill(_parent.bg);
			this.appGUI.rect(r);
		} else if (_resizeable) {
			this.appGUI.rect(r);
			r = new Rect(this.appGUI, _box);
			r.grow(-3);
			this.appGUI.noFill();
			this.appGUI.rect(r);
		} else if (_moveable) {
			r.y += 18;
			r.h -= 18;
			this.appGUI.rect(r);
		} else
			this.appGUI.rect(r);

		if (_moveable) {
			r = new Rect(this.appGUI, _box);
			if (_resizeable)
				r.grow(-3);
			r.h = 18;

			r.y++;
			r.h--;
			r.w -= r.h;
			this.appGUI.noStroke();
			if (_hasFocus)
				this.appGUI.fill(_parent.selectColor);
			else
				this.appGUI.fill(_parent.bg);
			this.appGUI.rect(r);

			r.y--;
			r.w += r.h;
			r.h++;
			this.appGUI.noFill();
			this.appGUI.stroke(_parent.fg);
			this.appGUI.rect(r);

			this.appGUI.fill(_parent.fg);
			this.appGUI.noStroke();
			r.x += 5;
			r.w -= 10;
			this.appGUI.textAlign(appGUI.LEFT);
			this.appGUI.text(_name, r);

			r.x = _box.x + _box.w - r.h;
			if (_resizeable)
				r.x -= 3;
			r.w = r.h;
			this.appGUI.noFill();
			if (_hasFocus)
				this.appGUI.stroke(_parent.selectColor);
			else
				this.appGUI.stroke(_parent.bg);
			r.grow(-1);
			this.appGUI.rect(r);
			r.grow(-1);
			this.appGUI.rect(r);
			r.grow(-1);
			this.appGUI.rect(r);

			this.appGUI.fill(_parent.bg);
			this.appGUI.stroke(_parent.fg);
			this.appGUI.rect(r);
			this.appGUI.noFill();
			r.grow(-2);
			this.appGUI.line(r.x, r.y, r.x + r.w, r.y + r.h);
			this.appGUI.line(r.x + r.h, r.y, r.x, r.y + r.h);
		}
	}

	public void getFocus() {
		_hasFocus = true;
	}

	public void lostFocus() {
		_hasFocus = false;
	}

	public void onMousePressed() {
		_clickX = this.appGUI.mouseX;
		_clickY = this.appGUI.mouseY;
		_saved = new Rect(this.appGUI, _box);
		if (_resizeable) {
			Rect r = new Rect(this.appGUI, _box);
			r.grow(-4);
			if (this.appGUI.overRect(_box) && !this.appGUI.overRect(r)) {
				_parent.getLock(this);
				// cursor(CROSS);
				_resizing = 0;
				if (this.appGUI.mouseX - _box.x < 4)
					_resizing += 1;
				if (_box.x + _box.w - this.appGUI.mouseX < 4)
					_resizing += 2;
				if (this.appGUI.mouseY - _box.y < 4)
					_resizing += 4;
				if (_box.y + _box.h - this.appGUI.mouseY < 4)
					_resizing += 8;
				return;
			}
		}

		if (_moveable) {
			Rect r = new Rect(this.appGUI, _box);
			if (_resizeable)
				r.grow(-3);
			r.h = 18;

			Rect r2 = new Rect(this.appGUI, r);
			r2.x = _box.x + _box.w - r2.h;
			if (_resizeable)
				r2.x -= 3;
			r2.w = r2.h;
			r2.grow(-3);
			if (this.appGUI.overRect(r2)) {
				_parent.removeFrame(this);
				return;
			}

			if (this.appGUI.overRect(r)) {
				// cursor(MOVE);
				_movingFrame = true;
				_parent.getLock(this);
				return;
			}
		}
	}

	public void onKeyPressed() {
	}
}