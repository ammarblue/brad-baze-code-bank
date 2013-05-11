package com.software.reuze;

public class m_InterpolateValueSwing implements m_i_InterpolateValue {
	private float scale;
	private int inout;
	public m_InterpolateValueSwing(float scale, int inout) {
		this.scale=scale;
		this.inout=inout;
	}
    public m_InterpolateValueSwing() {
	}
	public final float interpolate(float a, float b, float f) {
        if (inout==1) return a + (b - a) * (f * f * ((scale + 1) * f - scale)); //in
        if (inout==2) {f--; return a + (b - a)*(f * f * ((scale + 1) * f + scale) + 1); } //out
        return a + (b - a) * apply(f); //regular
    }
    public float apply (float a) {
		if (a <= 0.5f) {
			a *= 2;
			return a * a * ((scale + 1) * a - scale) / 2;
		}
		a--;
		a *= 2;
		return a * a * ((scale + 1) * a + scale) / 2 + 1;
	}
	public void set(String name, float value) {
		if (name.charAt(0)=='s') scale=value;
		else inout=(int)value;
	}

	public float getFloat(String name) {
		if (name.charAt(0)=='s') return scale;
		return inout;
	}

}
