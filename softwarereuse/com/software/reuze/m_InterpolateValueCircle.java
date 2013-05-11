package com.software.reuze;

public class m_InterpolateValueCircle implements m_i_InterpolateValue {
	int inout;
	public m_InterpolateValueCircle(int inout) {
		this.inout=inout;
	}
	public m_InterpolateValueCircle() {
	}
	public void set(String name, float value) {
		inout=(int)value;
	}

	public float getFloat(String name) {
		return inout;
	}

	public float interpolate(float a, float b, float f) {
		if (inout==1) return a+(b-a)*applyin(f);
		if (inout==2) return a+(b-a)*applyout(f);
		return a+(b-a)*apply(f);
	}
	public float apply (float a) {
		if (a <= 0.5f) {
			a *= 2;
			return (1 - (float)Math.sqrt(1 - a * a)) / 2;
		}
		a--;
		a *= 2;
		return ((float)Math.sqrt(1 - a * a) + 1) / 2;
	}

	public float applyin (float a) {
		return 1 - (float)Math.sqrt(1 - a * a);
	}

	public float applyout (float a) {
		a--;
		return (float)Math.sqrt(1 - a * a);
	}
}
