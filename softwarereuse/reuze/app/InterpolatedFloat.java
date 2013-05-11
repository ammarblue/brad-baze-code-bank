package reuze.app;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import com.software.reuze.ff_XMLWriter;

//---------------------------------------------------
public class InterpolatedFloat {
	class VarKey {
		float position, value;
	}

	private float _defaultValue;
	private boolean _useLookupTable;
	private float _lookupMin, _lookupMax;
	private float[] _lookupTable;
	private float _XMin, _XMax, _YMin, _YMax;
	private boolean _limitXMin, _limitXMax, _limitYMin, _limitYMax;
	private int _interpolationMethod;

	ArrayList<VarKey> keysList;

	InterpolatedFloat(float defaultValue) {
		_defaultValue = defaultValue;
		_useLookupTable = false;
		keysList = new ArrayList<VarKey>();
		_interpolationMethod = 0;

		_XMin = 0;
		_YMin = 0;
		_XMax = 1;
		_YMax = 1;
		_limitXMin = _limitXMax = _limitYMin = _limitYMax = false;
	}

	void setXMin(float v) {
		_limitXMin = true;
		_XMin = v;
	}

	boolean limitXMin() {
		return _limitXMin;
	}

	float getXMin() {
		return _XMin;
	}

	void setXMax(float v) {
		_limitXMax = true;
		_XMax = v;
	}

	boolean limitXMax() {
		return _limitXMax;
	}

	float getXMax() {
		return _XMax;
	}

	void setYMin(float v) {
		_limitYMin = true;
		_YMin = v;
	}

	boolean limitYMin() {
		return _limitYMin;
	}

	float getYMin() {
		return _YMin;
	}

	void setYMax(float v) {
		_limitYMax = true;
		_YMax = v;
	}

	boolean limitYMax() {
		return _limitYMax;
	}

	float getYMax() {
		return _YMax;
	}

	int getInterpolation() {
		return _interpolationMethod;
	}

	void setInterpolation(int i) {
		_interpolationMethod = i % 5;
	}

	int size() {
		return keysList.size();
	}

	void clear() {
		keysList.clear();
		update();
	}

	void remove(int index) {
		keysList.remove(index);
		update();
	}

	int add(float pos, float val) {
		if (_limitXMin)
			pos = appGUI.max(_XMin, pos);
		if (_limitXMax)
			pos = appGUI.min(_XMax, pos);
		if (_limitYMin)
			val = appGUI.max(_YMin, val);
		if (_limitYMax)
			val = appGUI.min(_YMax, val);

		VarKey newVar = new VarKey();
		newVar.position = pos;
		newVar.value = val;

		int size = keysList.size();
		for (int i = 0; i < size; i++) {
			VarKey v = (VarKey) keysList.get(i);
			if (v.position == pos) // don't add, just update
			{
				v.value = val;
				update();
				return i;
			}
		}

		keysList.add(newVar);
		update();
		return size;
	}

	void set(int index, float pos, float val) {
		VarKey var = (VarKey) keysList.get(index);
		if (_limitXMin)
			pos = appGUI.max(_XMin, pos);
		if (_limitXMax)
			pos = appGUI.min(_XMax, pos);
		if (_limitYMin)
			val = appGUI.max(_YMin, val);
		if (_limitYMax)
			val = appGUI.min(_YMin, val);
		var.position = pos;
		var.value = val;
		update();
	}

	void setValue(int index, float val) {
		if (_limitYMin)
			val = appGUI.max(_YMin, val);
		if (_limitYMax)
			val = appGUI.min(_YMin, val);
		((VarKey) keysList.get(index)).value = val;
		update();
	}

	void setPosition(int index, float pos) {
		if (_limitXMin)
			pos = appGUI.max(_XMin, pos);
		if (_limitXMax)
			pos = appGUI.min(_XMax, pos);
		((VarKey) keysList.get(index)).position = pos;
		update();
	}

	float getPosition(int index) {
		return ((VarKey) keysList.get(index)).position;
	}

	float getValue(int index) {
		return ((VarKey) keysList.get(index)).value;
	}

	int getPrev(float pos) {
		int size = keysList.size();
		int best = -1;
		float bestPos = pos;
		for (int i = 0; i < size; i++) {
			float p = getPosition(i);
			if (p < pos && (bestPos <= p || bestPos == pos)) {
				bestPos = p;
				best = i;
			}
		}

		return best;
	}

	int getPrev(int index) {
		return getPrev(getPosition(index));
	}

	int getNext(float pos) {
		int size = keysList.size();
		int best = -1;
		float bestPos = pos;
		for (int i = 0; i < size; i++) {
			float p = getPosition(i);
			if (p > pos && (bestPos >= p || bestPos == pos)) {
				bestPos = p;
				best = i;
			}
		}

		return best;
	}

	int getNext(int index) {
		return getNext(getPosition(index));
	}

	float get(float pos) {
		int size = keysList.size();
		if (size == 0)
			return _defaultValue;
		if (size == 1)
			return ((VarKey) keysList.get(0)).value;

		if (_useLookupTable) {
			float p = appGUI.map(pos, _lookupMin, _lookupMax, 0, 1)
					* _lookupTable.length;
			int i = appGUI.constrain(appGUI.floor(p), 0,
					_lookupTable.length - 1);
			return _lookupTable[i];
		}

		TreeMap<Float, Float> sorted = getSortedMap();

		if (_interpolationMethod != 4 && sorted.containsKey(pos))
			return (Float) sorted.get(pos);

		SortedMap<Float, Float> left, right;
		left = sorted.headMap(pos);
		if (left.size() == 0)
			return (Float) sorted.get(sorted.firstKey());
		right = sorted.tailMap(pos);
		if (right.size() == 0)
			return (Float) sorted.get(sorted.lastKey());

		float leftKey, rightKey;
		leftKey = (Float) left.lastKey();
		rightKey = (Float) right.firstKey();

		float amt = (pos - leftKey) / (rightKey - leftKey);

		float leftValue, rightValue;
		leftValue = (Float) sorted.get(leftKey);
		rightValue = (Float) sorted.get(rightKey);

		switch (_interpolationMethod) {
		case 0:
		default:
			return appGUI.lerp(leftValue, rightValue,
					(1 - appGUI.cos(amt * appGUI.PI)) / 2);
		case 1:
			return appGUI.lerp(leftValue, rightValue, amt);
		case 2:
			return rightValue;
		case 3:
			return leftValue;
		case 4:
			return amt < 0.5 ? leftValue : rightValue;
			// return (leftValue+rightValue)/2;
		}
	}

	// prepare a lookup table used in following calls of "get"
	void useLookupTable(int size, float from, float to) {
		_lookupMin = from;
		_lookupMax = to;
		_lookupTable = new float[size];
		_useLookupTable = true;
		update();
	}

	void useLookupTable(int size) {
		useLookupTable(size, _XMin, _XMax);
	}

	// compute a lookup table, not kept
	float[] getTable(int size, float from, float to) {
		float[] v = new float[size];
		boolean b = _useLookupTable;
		_useLookupTable = false;
		for (int i = 0; i < size; i++) {
			float p = appGUI.map(i, 0, size - 1, from, to);
			v[i] = get(p);
		}
		_useLookupTable = b;
		return v;
	}

	float[] getTable(int size) {
		return getTable(size, _XMin, _XMax);
	}

	private TreeMap<Float, Float> getSortedMap() {
		int size = keysList.size();
		if (size == 0)
			return null;
		TreeMap<Float, Float> mp = new TreeMap<Float, Float>();

		for (int i = 0; i < size; i++) {
			VarKey v = (VarKey) keysList.get(i);
			mp.put(v.position, v.value);
		}

		return mp;
	}

	private void update() {
		updateLimits();
		if (!_useLookupTable)
			return;
		_useLookupTable = false;
		for (int i = 0; i < _lookupTable.length; i++) {
			float p = appGUI.map(i, 0, _lookupTable.length - 1, _lookupMin,
					_lookupMax);
			_lookupTable[i] = get(p);
		}
		_useLookupTable = true;
	}

	private void updateLimits() {
		float tXMin = _XMin, tXMax = _XMax, tYMin = _YMin, tYMax = _YMax;
		int size = keysList.size();
		if (size <= 1) {
			if (size == 0) {
				tXMin = 0;
				tXMax = 1;
			} else {
				float p = getPosition(0);
				tXMin = p - 0.5f;
				tXMax = p + 0.5f;
			}

			float v = getValue(0);
			tYMin = v - 0.5f;
			tYMax = v + 0.5f;
		} else {
			tXMin = tXMax = getPosition(0);
			tYMin = tYMax = getValue(0);
			for (int i = 1; i < size; i++) {
				float p = getPosition(i);
				if (p < tXMin)
					tXMin = p;
				else if (p > tXMax)
					tXMax = p;

				float v = getValue(i);
				if (v < tYMin)
					tYMin = v;
				else if (v > tYMax)
					tYMax = v;
			}
		}

		if (!_limitXMin)
			_XMin = tXMin;
		if (!_limitXMax)
			_XMax = tXMax;
		if (!_limitYMin)
			_YMin = tYMin;
		if (!_limitYMax)
			_YMax = tYMax;
	}

}
