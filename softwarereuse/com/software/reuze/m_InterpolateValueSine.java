package com.software.reuze;

public class m_InterpolateValueSine implements m_i_InterpolateValue {
	int inout;
	public m_InterpolateValueSine(int inout) {
		this.inout=inout;
	}
	public m_InterpolateValueSine() {
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
		return (float) ((1 - Math.cos(a * Math.PI)) / 2);
	}

	public float applyin (float a) {
		return (float) (1 - Math.cos(a * Math.PI / 2));
	}

	public float applyout (float a) {
		return (float) Math.sin(a * Math.PI / 2);
	}
}
