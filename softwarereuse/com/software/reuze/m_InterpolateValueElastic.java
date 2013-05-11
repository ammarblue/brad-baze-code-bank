package com.software.reuze;

public class m_InterpolateValueElastic implements m_i_InterpolateValue {
    protected float value, power;
    protected int inout;
    public m_InterpolateValueElastic(float value, float power, int inout) {
    	this.value=value; this.power=power; this.inout=inout;
    }
    public m_InterpolateValueElastic() {
	}
	private float apply (float a) {
		if (a <= 0.5f) {
			a *= 2;
			return (float) ((float)Math.pow(value, power * (a - 1)) * Math.sin(a * 20) * 1.0955f / 2);
		}
		a = 1 - a;
		a *= 2;
		return (float) (1 - (float)Math.pow(value, power * (a - 1)) * Math.sin((a) * 20) * 1.0955f / 2);
	}
    private float applyin (float a) {
		return (float) ((float)Math.pow(value, power * (a - 1)) * Math.sin(a * 20) * 1.0955f);
	}
    public float applyout (float a) {
		a = 1 - a;
		return (float) (1 - (float)Math.pow(value, power * (a - 1)) * Math.sin(a * 20) * 1.0955f);
	}
    public final float interpolate(float a, float b, float f) {
    	if (inout==1) return a + (b - a) * applyin(f);
        if (inout==2) return a + (b - a) * applyout(f);
        return a + (b - a) * apply(f);
    }

	public void set(String name, float value) {
		if (name.charAt(0)=='v') this.value=value;
		else if (name.charAt(0)=='i') inout=(int)value;
		else power=value;
	}

	public float getFloat(String name) {
		if (name.charAt(0)=='v') return value;
		if (name.charAt(0)=='i') return inout;
		return power;
	}

}
