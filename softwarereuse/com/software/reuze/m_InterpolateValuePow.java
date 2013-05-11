package com.software.reuze;

public class m_InterpolateValuePow implements m_i_InterpolateValue {
	int inout;
	float power;
	public m_InterpolateValuePow(float power, int inout) {
		this.inout=inout;
	}
	public m_InterpolateValuePow() {
	}
	public void set(String name, float value) {
		if (name.charAt(0)=='p') power=value;
		else inout=(int)value;
	}

	public float getFloat(String name) {
		if (name.charAt(0)=='p') return power;
		return inout;
	}

	public float interpolate(float a, float b, float f) {
		if (inout==1) return a+(b-a)*applyin(f);
		if (inout==2) return a+(b-a)*applyout(f);
		return a+(b-a)*apply(f);
	}
	public float apply (float a) {
		if (a <= 0.5f) return (float)Math.pow(a * 2, power) / 2;
		return (float)Math.pow((a - 1) * 2, power) / (power % 2 == 0 ? -2 : 2) + 1;
	}

	public float applyin (float a) {
		return (float)Math.pow(a, power);
	}

	public float applyout (float a) {
		return (float)Math.pow(a - 1, power) * (power % 2 == 0 ? -1 : 1) + 1;
	}
}
