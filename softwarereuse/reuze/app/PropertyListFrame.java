package reuze.app;

public class PropertyListFrame extends MinyFrame {
	/**
	 * 
	 */
	private final appGUI appGUI;
	private MinyInteger _value;
	private String[] _choices;
	private boolean _moving, _over;
	private int _selected;

	PropertyListFrame(appGUI appGUI, MinyGUI parent, String name,
			MinyInteger value, String[] choices) {
		super(appGUI, parent, name);
		this.appGUI = appGUI;
		_value = value;
		_choices = choices;
		_selected = value.getValue();
		_moving = true;
		_over = false;
		_parent.getLock(this);
	}

	public void lostFocus() {
		_parent.removeFrame(this);
	}

	public void onMousePressed() {
		_parent.removeFrame(this);
		_parent.releaseLock(this);

		Rect b = getClientArea();
		b.h /= _choices.length;
		for (int i = 0; i < _choices.length; i++) {
			if (this.appGUI.overRect(b))
				_value.setValue(i);
			b.y += b.h;
		}
	}

	public void update() {
		_value.setValue(appGUI.constrain(_value.getValue(), 0, _choices.length));

		if (_moving) {
			if (!this.appGUI.mousePressed) {
				if (_over) {
					_value.setValue(_selected);
					_parent.releaseLock(this);
					_parent.removeFrame(this);
					return;
				}
				_moving = false;
			}

			_over = false;
			Rect b = getClientArea();
			b.h /= _choices.length;
			for (int i = 0; i < _choices.length; i++) {
				if (this.appGUI.overRect(b)) {
					_selected = i;
					_over = true;
					break;
				}
				b.y += b.h;
			}

			if (!_over)
				_selected = _value.getValue();
		}
	}

	public void display() {
		super.display();

		Rect b = getClientArea();
		b.h /= _choices.length;
		this.appGUI.noStroke();
		this.appGUI.fill(_parent.selectColor);
		Rect bs = new Rect(this.appGUI, b);
		bs.y += bs.h * _selected;
		bs.w++;
		bs.h++;
		this.appGUI.rect(bs);
		this.appGUI.fill(_parent.fg);
		this.appGUI.textAlign(appGUI.LEFT, appGUI.CENTER);
		for (int i = 0; i < _choices.length; i++) {
			this.appGUI.text(_choices[i], b);
			b.y += b.h;
		}
	}
}