package com.software.reuze;


public class dg_ParticleSine extends dg_Particle implements dg_i_Particle {

	public dg_ParticleSine(vg_i_Sprite sprite) {
		super(sprite);
	}
	int counter=0;
	public void translate(float xAmount, float yAmount) {
		/*if (Math.random() < 0.5) yAmount=(float) (yAmount+Math.sin(MathUtils.TWO_PI*(counter++%40/40f))*4);
		else*/ xAmount=(float) (/*xAmount+*/Math.sin(m_MathUtils.TWO_PI*(counter++%20/20f))*40);
		super.translate(xAmount, yAmount);
	}

	public dg_Particle create(vg_i_Sprite sprite) {
		return new dg_ParticleSine(sprite);
	}

	public String get(String property) {
		return "sine";
	}

	public String set(String property) {
		// TODO Auto-generated method stub
		return null;
	}

}
