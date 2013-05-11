package com.software.reuze;

public class m_InterpolateValueExp implements m_i_InterpolateValue {
	float value, power, min, scale;
    int inout;
	public m_InterpolateValueExp(float value, float power) {
		this.value = value;
		this.power = power;
		min = (float)Math.pow(value, -power);
		scale = 1 / (1 - min);
	}

	public m_InterpolateValueExp() {
	}

	private float apply (float a) {
		if (a <= 0.5f) return ((float)Math.pow(value, power * (a * 2 - 1)) - min) * scale / 2;
		return (2 - ((float)Math.pow(value, -power * (a * 2 - 1)) - min) * scale) / 2;
	}

	private float applyin (float a) {
		return ((float)Math.pow(value, power * (a - 1)) - min) * scale;
	}

	private float applyout (float a) {
		return 1 - ((float)Math.pow(value, -power * a) - min) * scale;
	}
	
    public final float interpolate(float a, float b, float f) {
    	if (inout==1) return a + (b - a) * applyin(f);
    	if (inout==2) return a + (b - a) * applyout(f);
        return a + (b - a) * apply(f);
    }

	public void set(String name, float value) {
		if (name.charAt(0)=='v') this.value=value;
		else if (name.charAt(0)=='i') inout=(int)value;
		else {
			power=value;
			min = (float)Math.pow(value, -power);
			scale = 1 / (1 - min);
		}
	}

	public float getFloat(String name) {
		if (name.charAt(0)=='v') return value;
		if (name.charAt(0)=='i') return inout;
		return power;
	}
}
