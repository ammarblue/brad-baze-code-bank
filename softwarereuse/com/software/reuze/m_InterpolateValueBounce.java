package com.software.reuze;

public class m_InterpolateValueBounce implements m_i_InterpolateValue {
	int inout;
	float[] widths, heights;
	public m_InterpolateValueBounce(float[] widths, float[] heights, int inout) {
		if (widths.length != heights.length)
			throw new IllegalArgumentException("Must be the same number of widths and heights.");
		this.inout=inout; this.widths=widths; this.heights=heights;
	}
	public m_InterpolateValueBounce(int bounces) {
		bounce(bounces);
	}
	public m_InterpolateValueBounce() {
	}
	public void set(String name, float value) {
		if (name.charAt(0)=='b') bounce((int)value);
		else inout=(int)value;
	}

	public float getFloat(String name) {
		if (name.charAt(0)=='b') return widths.length;
		return inout;
	}

	public float interpolate(float a, float b, float f) {
		if (inout==1) return a+(b-a)*applyin(f);
		if (inout==2) return a+(b-a)*applyout(f);
		return a+(b-a)*apply(f);
	}
	private float out (float a) {
		float test = a + widths[0] / 2;
		if (test < widths[0]) return test / (widths[0] / 2) - 1;
		return applyout(a);
	}
	private float apply (float a) {
		if (a <= 0.5f) return (1 - out(1 - a * 2)) / 2;
		return out(a * 2 - 1) / 2 + 0.5f;
	}
	private float applyin (float a) {
		return 1 - applyout(1 - a);
	}
	private float applyout (float a) {
		a += widths[0] / 2;
		float width = 0, height = 0;
		for (int i = 0, n = widths.length; i < n; i++) {
			width = widths[i];
			if (a <= width) {
				height = heights[i];
				break;
			}
			a -= width;
		}
		a /= width;
		float z = 4 / width * height * a;
		return 1 - (z - z * a) * width;
	}
	private void bounce(int bounces) {
		if (bounces < 2 || bounces > 5) throw new IllegalArgumentException("bounces cannot be < 2 or > 5: " + bounces);
		widths = new float[bounces];
		heights = new float[bounces];
		heights[0] = 1;
		switch (bounces) {
		case 2:
			widths[0] = 0.6f;
			widths[1] = 0.4f;
			heights[1] = 0.33f;
			break;
		case 3:
			widths[0] = 0.4f;
			widths[1] = 0.4f;
			widths[2] = 0.2f;
			heights[1] = 0.33f;
			heights[2] = 0.1f;
			break;
		case 4:
			widths[0] = 0.34f;
			widths[1] = 0.34f;
			widths[2] = 0.2f;
			widths[3] = 0.15f;
			heights[1] = 0.26f;
			heights[2] = 0.11f;
			heights[3] = 0.03f;
			break;
		case 5:
			widths[0] = 0.3f;
			widths[1] = 0.3f;
			widths[2] = 0.2f;
			widths[3] = 0.1f;
			widths[4] = 0.1f;
			heights[1] = 0.45f;
			heights[2] = 0.3f;
			heights[3] = 0.15f;
			heights[4] = 0.06f;
			break;
		}
		widths[0] *= 2;
	}
}
