package com.software.reuze;

public class dg_f_ParticleFactory implements dg_i_Particle {

	public static dg_i_Particle create(String name) {
		return new dg_f_ParticleFactory();
	}
	public dg_Particle create(vg_i_Sprite sprite) {
		return new dg_ParticleSine(sprite);
	}

	public String get(String property) {
		return "default";
	}

	public String set(String property) {
		// TODO Auto-generated method stub
		return null;
	}

}
