package reuze.app;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

//---------------------------------------------------
public class ColorGradient {
	/**
	 * 
	 */
	private final appGUI appGUI;

	class VarKey {
		float position;
		int value;
	}

	private int _defaultValue;
	private boolean _useLookupTable;
	private int[] _lookupTable;
	private int _interpolationMethod;

	ArrayList<VarKey> keysList;

	ColorGradient(appGUI appGUI, int defaultValue) {
		this.appGUI = appGUI;
		_defaultValue = defaultValue;
		_useLookupTable = false;
		keysList = new ArrayList<VarKey>();
		_interpolationMethod = 0;
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

	int add(float pos, int val) {
		pos = appGUI.constrain(pos, 0, 1);

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

	void set(int index, float pos, int val) {
		pos = appGUI.constrain(pos, 0, 1);
		VarKey var = (VarKey) keysList.get(index);
		var.position = pos;
		var.value = val;
		update();
	}

	void setValue(int index, int val) {
		((VarKey) keysList.get(index)).value = val;
		update();
	}

	void setPosition(int index, float pos) {
		pos = appGUI.constrain(pos, 0, 1);
		((VarKey) keysList.get(index)).position = pos;
		update();
	}

	float getPosition(int index) {
		return ((VarKey) keysList.get(index)).position;
	}

	int getValue(int index) {
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

	int get(float pos) {
		int size = keysList.size();
		if (size == 0)
			return _defaultValue;
		if (size == 1)
			return ((VarKey) keysList.get(0)).value;

		if (_useLookupTable) {
			int i = appGUI.constrain(appGUI.floor(pos * _lookupTable.length),
					0, _lookupTable.length - 1);
			return _lookupTable[i];
		}

		TreeMap<Float, Integer> sorted = getSortedMap();

		if (_interpolationMethod != 4 && sorted.containsKey(pos))
			return (Integer) sorted.get(pos);

		SortedMap<Float, Integer> left, right;
		left = sorted.headMap(pos);
		if (left.size() == 0)
			return (Integer) sorted.get(sorted.firstKey());
		right = sorted.tailMap(pos);
		if (right.size() == 0)
			return (Integer) sorted.get(sorted.lastKey());

		float leftKey, rightKey;
		leftKey = (Float) left.lastKey();
		rightKey = (Float) right.firstKey();

		float amt = (pos - leftKey) / (rightKey - leftKey);

		int leftValue, rightValue;
		leftValue = (Integer) sorted.get(leftKey);
		rightValue = (Integer) sorted.get(rightKey);

		switch (_interpolationMethod) {
		case 0:
		default:
			return this.appGUI.lerpColor(leftValue, rightValue,
					(1 - appGUI.cos(amt * appGUI.PI)) / 2);
		case 1:
			return this.appGUI.lerpColor(leftValue, rightValue, amt);
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
	void useLookupTable(int size) {
		_lookupTable = new int[size];
		_useLookupTable = true;
		update();
	}

	// compute a lookup table, not kept
	int[] getTable(int size) {
		int[] v = new int[size];
		boolean b = _useLookupTable;
		_useLookupTable = false;
		for (int i = 0; i < size; i++) {
			float p = (float) i / (size - 1);
			v[i] = get(p);
		}
		_useLookupTable = b;
		return v;
	}

	private TreeMap<Float, Integer> getSortedMap() {
		int size = keysList.size();
		if (size == 0)
			return null;
		TreeMap<Float, Integer> mp = new TreeMap<Float, Integer>();

		for (int i = 0; i < size; i++) {
			VarKey v = (VarKey) keysList.get(i);
			mp.put(v.position, v.value);
		}

		return mp;
	}

	private void update() {
		if (!_useLookupTable)
			return;
		_useLookupTable = false;
		for (int i = 0; i < _lookupTable.length; i++) {
			float p = (float) i / (_lookupTable.length - 1);
			_lookupTable[i] = get(p);
		}
		_useLookupTable = true;
	}
}