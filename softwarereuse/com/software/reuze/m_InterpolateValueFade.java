package com.software.reuze;

public class m_InterpolateValueFade implements m_i_InterpolateValue {

    public final float interpolate(float a, float b, float f) {
        return a + (b - a) * (f * f * f * (f * (f * 6 - 15) + 10));
    }

	public void set(String name, float value) {
	}

	public float getFloat(String name) {
		return Float.NaN;
	}

}
