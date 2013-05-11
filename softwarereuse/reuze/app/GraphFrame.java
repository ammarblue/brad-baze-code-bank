package reuze.app;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import processing.core.PConstants;

import reuze.app.appGUI.FrameCreator;

public class GraphFrame extends MinyFrame {
	/**
	 * 
	 */
	private final appGUI appGUI;
	private int _selected, _over;
	private InterpolatedFloat _value;
	private boolean _drawGrid;
	private float _minX, _maxX, _minY, _maxY;
	private final float bigIncrement = 0.1f, smallIncrement = 0.01f;
	private float[] _graphValues;
	private FrameCreator _property;

	GraphFrame(appGUI appGUI, MinyGUI parent, String name,
			InterpolatedFloat value, FrameCreator property) {
		super(appGUI, parent, name);
		this.appGUI = appGUI;
		_value = value;
		_property = property;
		_selected = _over = -1;
		_drawGrid = false;
		setMoveable(true);
		setResizeable(true);
		_minW = 300;
		_minH = 200;
		_minX = _value.getXMin();
		_maxX = _value.getXMax();
		_minY = _value.getYMin();
		_maxY = _value.getYMax();
	}

	void placeAt(int x, int y) {
		placeAt(x, y, 350, 250);
		updateGraph();
	}

	void onClose() {
		_property.onCloseFrame(this);
	}

	void onResize() {
		updateGraph();
	}

	Rect getGraphArea() {
		Rect r = getClientArea();
		r.x += 40;
		r.w -= 45;
		r.y += 10;
		r.h -= 40;
		return r;
	}

	void setGrid(boolean b) {
		_drawGrid = b;
	}

	void updateGraph() {
		Rect r = getGraphArea();
		_graphValues = _value.getTable(r.w, _minX, _maxX);
		for (int x = 0; x < _graphValues.length; x++)
			_graphValues[x] = appGUI.map(_graphValues[x], _minY, _maxY, r.h, 0);
	}

	void drawAxis() {
		Rect r = getGraphArea();
		this.appGUI.stroke(_parent.fg);
		this.appGUI.fill(_parent.fg);
		NumberFormat formatter = new DecimalFormat("0.##E0");
		int alpha = (_parent.fg >> 24) & 0xFF;
		alpha = alpha / 3;
		int gridColor = _parent.fg & 0x00FFFFFF + (alpha << 24);

		// Positions
		this.appGUI.textAlign(appGUI.CENTER);
		float l = appGUI.log(_maxX - _minX) / appGUI.log(10);
		l = l < 0 ? -appGUI.ceil(appGUI.abs(l)) : appGUI.floor(l);
		float xn = appGUI.pow(10, l);
		int nb = appGUI.ceil((_maxX - _minX) * 1.001f / xn);

		if (nb <= 2)
			xn /= 5;
		else if (nb < 5)
			xn /= 2;
		else if (nb > 10)
			xn *= 2;

		float x1 = appGUI.ceil(_minX / xn) * xn;
		float x2 = appGUI.floor(_maxX / xn) * xn;
		nb = appGUI.ceil((x2 - x1) / xn) + 1;

		for (int i = 0; i < nb; i++) {
			float x = x1 + xn * i;
			if (x > _maxX)
				break;
			float sx = r.x + appGUI.map(x, _minX, _maxX, 0, r.w);
			if (_drawGrid) {
				this.appGUI.stroke(gridColor);
				this.appGUI.line(sx, r.y, sx, r.y + r.h);
			}
			this.appGUI.stroke(_parent.fg);
			this.appGUI.line(sx, r.y + r.h, sx, r.y + r.h + 5);
			String caption = appGUI.nf(x, 0, 0);
			// pushMatrix();
			// rotate(90);
			if (caption.length() > 4)
				caption = formatter.format(x);
			this.appGUI.text(caption, sx - 25, r.y + r.h + 10, 50, 20);
			// popMatrix();
		}

		// Values
		l = appGUI.log(_maxY - _minY) / appGUI.log(10);
		l = l < 0 ? -appGUI.ceil(appGUI.abs(l)) : appGUI.floor(l);
		float yn = appGUI.pow(10, l);
		nb = appGUI.ceil((_maxY - _minY) * 1.001f / yn);

		if (nb <= 2)
			yn /= 5;
		else if (nb < 5)
			yn /= 2;
		else if (nb > 10)
			yn *= 2;

		float y1 = appGUI.ceil(_minY / yn) * yn;
		float y2 = appGUI.floor(_maxY / yn) * yn;
		nb = appGUI.ceil((y2 - y1) / yn) + 1;

		Rect area = getClientArea();
		for (int i = 0; i < nb; i++) {
			float y = y1 + yn * i;
			if (y > _maxY)
				break;
			float sy = r.y + appGUI.map(y, _minY, _maxY, r.h, 0);
			if (_drawGrid) {
				this.appGUI.stroke(gridColor);
				this.appGUI.line(r.x, sy, r.x + r.w, sy);
			}
			this.appGUI.stroke(_parent.fg);
			this.appGUI.line(r.x - 5, sy, r.x, sy);
			String caption = appGUI.nf(y, 0, 0);
			if (caption.length() > 4) {
				caption = formatter.format(y);
				this.appGUI.textAlign(appGUI.LEFT);
				this.appGUI.text(caption, area.x, sy - 10, 100, 20);
			} else {
				this.appGUI.textAlign(appGUI.RIGHT);
				this.appGUI.text(caption, area.x, sy - 10, r.x - area.x - 10,
						20);
			}
		}

		this.appGUI.textAlign(appGUI.LEFT);
	}

	public void display() {
		super.display();

		this.appGUI.noFill();
		this.appGUI.stroke(_parent.fg);
		Rect r = getGraphArea();
		this.appGUI.rect(r);

		float lastY = r.y + _graphValues[0];
		for (int x = 1; x < r.w; x++) {
			float y = r.y + _graphValues[x];
			this.appGUI.line(r.x + x - 1, lastY, r.x + x, y);
			lastY = y;
		}

		drawAxis();

		// Draw selection handles
		for (int i = 0; i < _value.size(); i++) {
			float x = r.x
					+ appGUI.map(_value.getPosition(i), _minX, _maxX, 0, r.w);
			float y = r.y
					+ appGUI.map(_value.getValue(i), _minY, _maxY, r.h, 0);
			if (_selected == i)
				this.appGUI.fill(_parent.selectColor);
			else
				this.appGUI.fill(_parent.bg);
			this.appGUI.ellipse(x, y, 6, 6);
		}
	}

	public void update() {
		super.update();

		Rect r = getGraphArea();
		if (this.appGUI.mousePressed) {
			if (_selected != -1) {
				if (this.appGUI.pmouseX != this.appGUI.mouseX) {
					if (this.appGUI.mouseX < r.x && !_value.limitXMin()) {
						float t = appGUI.max(
								0,
								appGUI.map(this.appGUI.pmouseX
										- this.appGUI.mouseX, 0, r.w, 0, _maxX
										- _minX));
						if (t > 0) {
							float v = _value.getPosition(_selected) - t;
							_value.setPosition(_selected, v);
							_minX = v;
						}
					} else if (this.appGUI.mouseX > r.x + r.w
							&& !_value.limitXMax()) {
						float t = appGUI.max(
								0,
								appGUI.map(this.appGUI.mouseX
										- this.appGUI.pmouseX, 0, r.w, 0, _maxX
										- _minX));
						if (t > 0) {
							float v = _value.getPosition(_selected) + t;
							_value.setPosition(_selected, v);
							_maxX = v;
						}
					} else {
						float p = appGUI.constrain(appGUI.map(
								this.appGUI.mouseX, r.x, r.x + r.w, _minX,
								_maxX), _minX, _maxX);
						_value.setPosition(_selected, p);
					}
					updateGraph();
				}
				if (this.appGUI.pmouseY != this.appGUI.mouseY) {
					if (this.appGUI.mouseY < r.y && !_value.limitYMax()) {
						float t = appGUI.max(
								0,
								appGUI.map(this.appGUI.pmouseY
										- this.appGUI.mouseY, 0, r.h, 0, _maxY
										- _minY));
						if (t > 0) {
							float v = _value.getValue(_selected) + t;
							_value.setValue(_selected, v);
							_maxY = v;
						}
					} else if (this.appGUI.mouseY > r.y + r.h
							&& !_value.limitYMin()) {
						float t = appGUI.max(
								0,
								appGUI.map(this.appGUI.mouseY
										- this.appGUI.pmouseY, 0, r.h, 0, _maxY
										- _minY));
						if (t > 0) {
							float v = _value.getValue(_selected) - t;
							_value.setValue(_selected, v);
							_minY = v;
						}
					} else {
						float v = appGUI.constrain(appGUI.map(
								this.appGUI.mouseY, r.y + r.h, r.y, _minY,
								_maxY), _minY, _maxY);
						_value.setValue(_selected, v);
					}
					updateGraph();
				}
			}
		} else {
			_over = -1;
			for (int i = 0; i < _value.size(); i++) {
				float x = r.x
						+ appGUI.map(_value.getPosition(i), _minX, _maxX, 0,
								r.w);
				float y = r.y
						+ appGUI.map(_value.getValue(i), _minY, _maxY, r.h, 0);
				if (appGUI.dist(this.appGUI.mouseX, this.appGUI.mouseY, x, y) < 5) {
					_over = i;
					break;
				}
			}
		}
	}

	public void onMousePressed() {
		super.onMousePressed();
		_selected = -1;
		if (_over != -1) {
			_selected = _over;
			return;
		}

		if (_parent.getModCtrl()) {
			Rect r = getGraphArea();
			float p = appGUI.constrain(appGUI.map(this.appGUI.mouseX, r.x, r.x
					+ r.w, _minX, _maxX), _minX, _maxX);
			float v = appGUI.constrain(appGUI.map(this.appGUI.mouseY,
					r.y + r.h, r.y, _minY, _maxY), _minY, _maxY);
			_selected = _value.add(p, v);
			updateGraph();
		}
	}

	public void onKeyPressed() {
		super.onKeyPressed();
		switch (this.appGUI.key) {
		case PConstants.CODED:
			switch (this.appGUI.keyCode) {
			case PConstants.UP:
				if (_selected != -1) {
					float v = _value.getValue(_selected);
					if (_parent.getModShift())
						v += (_maxY - _minY) * bigIncrement;
					else
						v += (_maxY - _minY) * smallIncrement;
					if (v > _maxY && !_value.limitYMax()) {
						_maxY = v;
						_value.setValue(_selected, v);
					} else
						_value.setValue(_selected, appGUI.min(v, _maxY));
					updateGraph();
				}
				break;
			case PConstants.DOWN:
				if (_selected != -1) {
					float v = _value.getValue(_selected);
					if (_parent.getModShift())
						v -= (_maxY - _minY) * bigIncrement;
					else
						v -= (_maxY - _minY) * smallIncrement;
					if (v < _minY && !_value.limitYMin()) {
						_minY = v;
						_value.setValue(_selected, v);
					} else
						_value.setValue(_selected, appGUI.max(v, _minY));
					updateGraph();
				}
				break;
			case PConstants.LEFT:
				if (_selected != -1) {
					float p = _value.getPosition(_selected);
					if (_parent.getModShift())
						p -= (_maxX - _minX) * bigIncrement;
					else
						p -= (_maxX - _minX) * smallIncrement;
					if (p < _minX && !_value.limitXMin()) {
						_minX = p;
						_value.setPosition(_selected, p);
					} else
						_value.setPosition(_selected, appGUI.max(p, _minX));
					updateGraph();
				}
				break;
			case PConstants.RIGHT:
				if (_selected != -1) {
					float p = _value.getPosition(_selected);
					if (_parent.getModShift())
						p += (_maxX - _minX) * bigIncrement;
					else
						p += (_maxX - _minX) * smallIncrement;
					if (p > _maxX && !_value.limitXMax()) {
						_maxX = p;
						_value.setPosition(_selected, p);
					} else
						_value.setPosition(_selected, appGUI.min(p, _maxX));
					updateGraph();
				}
				break;
			}
			break;
		case PConstants.TAB:
			if (_parent.getModShift()) {
				if (_selected != -1) {
					int temp = _value.getPrev(_selected);
					if (temp != -1)
						_selected = temp;
				} else
					_selected = _value.getPrev(_maxX + 1.0f);
			} else {
				if (_selected != -1) {
					int temp = _value.getNext(_selected);
					if (temp != -1)
						_selected = temp;
				} else
					_selected = _value.getNext(_minX - 1.0f);
			}
			break;
		case PConstants.DELETE:
			if (_selected != -1) {
				float p = _value.getPosition(_selected);
				_value.remove(_selected);
				_selected = _value.getNext(p);
				updateGraph();
			}
			break;
		case PConstants.BACKSPACE:
			if (_selected != -1) {
				float p = _value.getPosition(_selected);
				_value.remove(_selected);
				_selected = _value.getPrev(p);
				updateGraph();
			}
			break;
		case ' ':
			_minX = _value.getXMin();
			_maxX = _value.getXMax();
			_minY = _value.getYMin();
			_maxY = _value.getYMax();
			updateGraph();
			break;
		case 'g':
		case 'G':
			_drawGrid = !_drawGrid;
			break;
		case 'n':
		case 'N':
			_selected = _value.add((_maxX + _minX) / 2, (_maxY + _minY) / 2);
			updateGraph();
			break;
		case 'i':
		case 'I':
			_value.setInterpolation(_value.getInterpolation() + 1);
			updateGraph();
			break;
		case 'q':
		case 'Q':
			_parent.removeFrame(this);
			break;
		}
	}
}