package reuze.app;

public class dg_Particle {
	public final float p[];
	public int color1, color2;

	public int getInt(String property) {
		return property.charAt(5) == '2' ? color2 : color1;
	}

	public int setInt(String property, int value) {
		int i;
		if (property.charAt(5) == '2') {
			i = color2;
			color2 = value;
		} else {
			i = color1;
			color1 = value;
		}
		return i;
	}

	public float getFloat(String property) {
		int i = dg_NameSpaceParticles.nsp.id(property);
		return (i < 0) ? Float.NaN : p[i];
	}

	public float setFloat(String property, float value) {
		int i = dg_NameSpaceParticles.nsp.id(property);
		if (i < 0)
			return Float.NaN;
		float x = p[i];
		p[i] = value;
		return x;
	}

	public dg_Particle(int n) {
		p = new float[n];
	}
}
