package reuze.app;

import processing.core.PConstants;
import processing.core.PGraphics;

public class PropertyEdit extends Property {
	/**
	 * 
	 */
	private final appGUI appGUI;
	private int cursorPos, cursorTime, selectionStart, selectionEnd;
	private boolean cursorOn, selectioning;
	private String editText;

	PropertyEdit(appGUI appGUI, String name) {
		super(appGUI, name);
		this.appGUI = appGUI;
		cursorPos = 0;
		cursorTime = 0;
		cursorOn = true;
		selectioning = false;
		selectionStart = -1;
		editText = new String();
	}

	Rect getBox() {
		return new Rect(this.appGUI, (int) (_x + _w * 0.4 + 3), _y + 1,
				(int) (_w * 0.6 - 8), 18);
	}

	int findCursorPos() {
		float tc = this.appGUI.mouseX - (int) (_x + _w * 0.4 + 4);
		int closestPos = editText.length();
		float closestDist = _w;
		for (int i = editText.length(); i >= 0; i--) {
			float tw = this.appGUI.textWidth(editText.substring(0, i));
			float d = appGUI.abs(tc - tw);
			if (d < closestDist) {
				closestDist = d;
				closestPos = i;
			} else
				break;
		}
		return closestPos;
	}

	public void onMousePressed() {
		if (this.appGUI.overRect(getBox())) {
			if (!_parent.hasLock(this)) {
				_parent.getLock(this);
				cursorTime = this.appGUI.millis() + 500;
				cursorOn = true;
				editText = getValue();
			}

			cursorPos = findCursorPos();
			selectioning = true;
			selectionStart = cursorPos;
		}
	}

	public void onKeyPressed() {
		switch (this.appGUI.key) {
		case PConstants.CODED:
			switch (this.appGUI.keyCode) {
			case PConstants.LEFT:
				if (_parent.getModShift()) {
					if (selectionStart != -1) {
						if (cursorPos == selectionStart)
							selectionStart = appGUI.max(selectionStart - 1, 0);
						else if (cursorPos == selectionEnd)
							selectionEnd = appGUI.max(selectionEnd - 1, 0);
						cursorPos = appGUI.max(cursorPos - 1, 0);
					} else if (cursorPos > 0) {
						selectionEnd = cursorPos;
						cursorPos--;
						selectionStart = cursorPos;
					}
				} else if (selectionStart != -1) {
					cursorPos = selectionStart;
					selectionStart = selectionEnd = -1;
				} else
					cursorPos = appGUI.max(cursorPos - 1, 0);
				break;
			case PConstants.RIGHT:
				if (_parent.getModShift()) {
					if (selectionStart != -1) {
						if (cursorPos == selectionStart)
							selectionStart = appGUI.min(selectionStart + 1,
									editText.length());
						else if (cursorPos == selectionEnd)
							selectionEnd = appGUI.min(selectionEnd + 1,
									editText.length());
						cursorPos = appGUI
								.min(cursorPos + 1, editText.length());
					} else if (cursorPos < editText.length() - 1) {
						selectionStart = cursorPos;
						cursorPos++;
						selectionEnd = cursorPos;
					}
				} else if (selectionStart != -1) {
					cursorPos = selectionEnd;
					selectionStart = selectionEnd = -1;
				} else
					cursorPos = appGUI.min(cursorPos + 1, editText.length());
				break;
			}
			break;
		case PConstants.RETURN:
		case PConstants.ENTER:
			lostFocus();
			break;
		case PConstants.DELETE:
			if (!selectioning && (selectionStart != -1)) {
				editText = editText.substring(0, selectionStart)
						+ editText.substring(selectionEnd);
				cursorPos = selectionStart;
				selectionStart = selectionEnd = -1;
			} else if (cursorPos < editText.length())
				editText = editText.substring(0, cursorPos)
						+ editText.substring(cursorPos + 1);
			break;
		case PConstants.BACKSPACE:
			if (!selectioning && (selectionStart != -1)) {
				editText = editText.substring(0, selectionStart)
						+ editText.substring(selectionEnd);
				cursorPos = selectionStart;
				selectionStart = selectionEnd = -1;
			} else if (cursorPos > 0) {
				editText = editText.substring(0, cursorPos - 1)
						+ editText.substring(cursorPos);
				cursorPos--;
			}
			break;
		default:
			if (_parent.getModCtrl() || _parent.getModAlt())
				break;
			if (!selectioning && (selectionStart != -1)) {
				editText = editText.substring(0, selectionStart)
						+ editText.substring(selectionEnd);
				cursorPos = selectionStart;
				selectionStart = selectionEnd = -1;
			}
			String tempText = editText.substring(0, cursorPos)
					+ this.appGUI.key + editText.substring(cursorPos);
			if (validate(tempText)) {
				editText = tempText;
				cursorPos++;
			}
			break;
		}
	}

	boolean validate(String test) {
		return true;
	}

	void saveValue(String val) {
	}

	String getValue() {
		return "";
	}

	public void lostFocus() {
		if (validate(editText))
			saveValue(editText);
		cursorPos = 0;
		cursorOn = true;
		selectioning = false;
		// cursor(ARROW);
		_parent.releaseLock(this);
	}

	public void update() {
		if (!_parent.hasLock(this))
			return;

		if (this.appGUI.millis() > cursorTime) {
			cursorOn = !cursorOn;
			cursorTime = this.appGUI.millis() + 500;
		}

		if (!this.appGUI.mousePressed) {
			selectioning = false;
			if (selectionStart != selectionEnd) {
				int start = appGUI.min(selectionStart, selectionEnd);
				int end = appGUI.max(selectionStart, selectionEnd);
				selectionStart = start;
				selectionEnd = end;
			} else
				selectionStart = selectionEnd = -1;
		}

		/*
		 * if(overRect(getBox())) cursor(TEXT); else cursor(ARROW);
		 */

		if (selectioning)
			cursorPos = selectionEnd = findCursorPos();
	}

	public void display(PGraphics pg, int y) {
		super.display(pg, y);

		pg.noFill();
		pg.stroke(_parent.fg);
		Rect b = getBox();
		b.y += y - _y;
		b.x -= _x;
		if (_parent.hasLock(this))
			pg.strokeWeight(2);
		this.appGUI.rect(pg, b);
		pg.strokeWeight(1);
		b.grow(-1);
		pg.textAlign(appGUI.LEFT, appGUI.CENTER);
		if (_parent.hasLock(this)) {
			if (selectionStart != selectionEnd) {
				float tw1, tw2, tw;
				tw1 = this.appGUI.textWidth(editText.substring(0,
						selectionStart));
				tw2 = this.appGUI
						.textWidth(editText.substring(0, selectionEnd));
				tw = tw2 - tw1;

				pg.fill(_parent.selectColor);
				pg.noStroke();
				pg.rect(b.x + tw1, b.y + 1, tw, b.h - 2);
				pg.noFill();
			}

			pg.fill(_parent.fg);
			this.appGUI.text(pg, editText, b);
			pg.stroke(_parent.fg);
			if (cursorOn) {
				float tw = this.appGUI.textWidth(editText.substring(0,
						cursorPos));
				pg.line(_w * 0.4f + 4 + tw, y + 4, _w * 0.4f + 4 + tw, y + 17);
			}
		} else
			this.appGUI.text(pg, getValue(), b);
	}
}