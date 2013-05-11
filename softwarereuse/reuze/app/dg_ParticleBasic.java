package reuze.app;

import com.software.reuze.ga_Vector2;

import processing.core.PGraphics;

/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/52413*@* */
/**
 * Carnival by Daniel Buschek February, 2012
 */
public class dg_ParticleBasic extends dg_Particle {
	public static final int x = dg_NameSpaceParticles.nsp.add("x");
	public static final int y = dg_NameSpaceParticles.nsp.add("y");
	public static final int w = dg_NameSpaceParticles.nsp.add("w");
	public static final int prevx = dg_NameSpaceParticles.nsp.add("prevx");
	public static final int prevy = dg_NameSpaceParticles.nsp.add("prevy");
	public static final int vx = dg_NameSpaceParticles.nsp.add("vx");
	public static final int vy = dg_NameSpaceParticles.nsp.add("vy");
	public static final int ax = dg_NameSpaceParticles.nsp.add("ax");
	public static final int ay = dg_NameSpaceParticles.nsp.add("ay");
	public static final int lifeTime = dg_NameSpaceParticles.nsp
			.add("lifeTime");
	public static final int lifeTimeMax = dg_NameSpaceParticles.nsp
			.add("lifeTimeMax");
	public static final int frictionA = dg_NameSpaceParticles.nsp
			.add("frictionA");
	public static final int frictionV = dg_NameSpaceParticles.nsp
			.add("frictionV");
	public static final int dead = dg_NameSpaceParticles.nsp.add("dead");
	public static final int border = dg_NameSpaceParticles.nsp.add("border");

	public dg_ParticleBasic(float x, float y, float w, int color, int n) {
		super(n + 15);
		p[dg_ParticleBasic.x] = x;
		p[dg_ParticleBasic.y] = y;
		p[dg_ParticleBasic.w] = w;
		p[dg_ParticleBasic.lifeTimeMax] = (float) ((Math.random() + 0.5) * 60);
		p[dg_ParticleBasic.frictionA] = 0.95f;
		p[dg_ParticleBasic.frictionV] = 0.3f;
		color1 = color;
	}

	public void draw(PGraphics g) {
		if (p[dead] != 0)
			return;
		g.stroke(color1);
		// TODO weight>diameter increases diameter but leaves weight at maximum
		// of old diameter
		g.strokeWeight((float) (2 * p[w] * (1 + 2 * p[lifeTime]
				* Math.log(p[lifeTime]) / p[lifeTimeMax])));
		g.ellipse(p[x], p[y], Math.abs(p[prevx] - p[x]) + 1,
				Math.abs(p[prevy] - p[y]) + 1);
	}

	public void move() {
		// Aging:
		if (++p[lifeTime] >= p[lifeTimeMax]) {
			p[dead] = 1;
			return;
		}
		// Save last location:
		p[prevx] = p[x];
		p[prevy] = p[y];
		// Movement:
		p[ax] *= p[frictionA];
		p[ay] *= p[frictionA];
		p[vx] *= p[frictionV];
		p[vy] *= p[frictionV];
		p[vx] += p[ax];
		p[vy] += p[ay];
		p[x] += p[vx];
		p[y] += p[vy];
	}
}
