package reuze.app;

import com.software.reuze.m_InterpolateLerp;

import processing.core.PGraphics;

/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/52842*@* */
// Particle Ellipse 
// By Apewire
public class dg_ParticleLerp extends dg_Particle {
	public static final int x = dg_NameSpaceParticles.nsp.add("x");
	public static final int y = dg_NameSpaceParticles.nsp.add("y");
	public static final int w = dg_NameSpaceParticles.nsp.add("w");
	public static final int prevx = dg_NameSpaceParticles.nsp.add("prevx");
	public static final int prevy = dg_NameSpaceParticles.nsp.add("prevy");
	public static final int nextx = dg_NameSpaceParticles.nsp.add("nextx");
	public static final int nexty = dg_NameSpaceParticles.nsp.add("nexty");
	public static final int lerpValue = dg_NameSpaceParticles.nsp
			.add("lerpValue");
	public static final int lerpSpeed = dg_NameSpaceParticles.nsp
			.add("lerpSpeed");
	public static final int maxAlpha = dg_NameSpaceParticles.nsp
			.add("maxAlpha");
	public static final int alpha = dg_NameSpaceParticles.nsp.add("alpha");
	public static final int newAlpha = dg_NameSpaceParticles.nsp
			.add("newAlpha");
	public static final int border = dg_NameSpaceParticles.nsp.add("border");

	public dg_ParticleLerp(int width, int n) {
		super(n + 13);
		p[w] = (float) (Math.random() * 16 + 1);
		p[lerpValue] = 1;
		p[lerpSpeed] = 0.5f;
		p[maxAlpha] = 150;
		p[alpha] = 150;
		p[border] = width;
		color1 = 0xff000000 | (int) (Math.random() * 0xffffff);
	}

	public void draw(PGraphics g) {
		if (p[x] < 0 && p[y] < 0)
			return;
		g.noStroke();
		g.fill(color1, p[alpha]);
		g.ellipse(p[x], p[y], p[w], p[w]);
	}

	void move(float _centerX, float _centerY) {
		if (p[lerpValue] >= 1) {
			// radius = square root (random(maxsize)); use sqrt for an even
			// distribution
			float radius = (float) Math.sqrt(Math.random() * p[border]);
			// set random angles/positions, make a circle (TWO_PI)
			double angle = Math.random() * Math.PI * 2;
			// set previous position
			p[prevx] = p[nextx];
			p[prevy] = p[nexty];
			// set new position
			p[nextx] = (float) (_centerX + Math.cos(angle) * radius);
			p[nexty] = (float) (_centerY + Math.sin(angle) * radius);
			// set lerpSpeed -> distance between prevpos (x,y) & nextpos (x,y) *
			// 0.0002
			// calculate distance between prev position and next position
			float dx = p[prevx] - p[nextx];
			float dy = p[prevy] - p[nexty];
			p[lerpSpeed] = (float) (Math.sqrt(dx * dx + dy * dy) * 0.0008);
			// reset lerp values
			p[lerpValue] = 0;
			p[newAlpha] = 0;
		} else {
			p[x] = m_InterpolateLerp.lerp(p[lerpValue], p[prevx], p[nextx]);
			p[y] = m_InterpolateLerp.lerp(p[lerpValue], p[prevy], p[nexty]);
			p[lerpValue] += p[lerpSpeed];
			if (p[newAlpha] == 0) {
				p[alpha] += 1;
				if (p[alpha] >= p[maxAlpha]) {
					p[newAlpha] = 1;
				}
			} else if (p[alpha] >= 10) {
				p[alpha] -= 8;
			}
		}
	}
}
