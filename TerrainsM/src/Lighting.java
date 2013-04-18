import processing.core.PVector;

class Lighting {
	/**
	 * 
	 */
	private final terrainmarch terrainmarch;
	PVector _sd;
	PVector _sc;
	PVector _am;
	PVector skytop;
	PVector skybot;
	PVector fogcol;
	float shadowDist;
	boolean useClouds;

	Lighting(terrainmarch terrainmarch, PVector sunDirection, float sunUp,
			boolean uc) {
		this.terrainmarch = terrainmarch;
		useClouds = uc;
		// simple time of day stuff sunset -> sunrise

		_sc = this.terrainmarch.smoothblend(new PVector(1.f, 0.3f, 0.05f),
				new PVector(1.f, 0.8f, 0.5f), sunUp);
		skytop = this.terrainmarch.smoothblend(new PVector(0.1f, 0.2f, 0.4f),
				new PVector(0.25f, 0.4f, 0.7f), sunUp);
		skybot = this.terrainmarch.smoothblend(new PVector(1.2f, 0.5f, 0.05f),
				new PVector(0.5f, 0.8f, 1.0f), sunUp);
		fogcol = this.terrainmarch.smoothblend(new PVector(1.f, 0.5f, 0.05f),
				new PVector(0.45f, 0.65f, 0.9f), sunUp);
		_am = this.terrainmarch.smoothblend(new PVector(0.5f, 0.4f, 0.3f),
				new PVector(0.3f, 0.4f, 0.6f), sunUp);
		_sd = sunDirection;
		_sd.normalize();

	}

	public PVector getShading(QualitySettings qs, HieghtField hgts, PVector p,
			PVector n, PVector m, float t) {
		// ? shot shadow ray?
		PVector amb = new PVector();
		amb.set(_am);
		float normFactor = n.y * .5f + .5f;

		float abFactor = hgts.GetAO(p.x, p.y, p.z);
		amb.mult(abFactor + normFactor * 0.2f);// *normFactor);

		float shadowSoftness = 0.4f;
		// float shadow =castRay( hgts, p, _sd, 0.015, 10) < 0.0f ? 1 : 0.;
		PVector sunLight = new PVector();
		sunLight.set(_sc);
		float nd = terrainmarch.max(n.dot(_sd), 0);

		float shadow = 1.0f;
		if (nd > 0)
			shadow = this.terrainmarch.castShadowRay(qs, hgts, p, _sd, t
					* qs.shadowRayStartRatio/* ,t*0.005 */, 0.2f);

		// apply bounce depending on in shadow
		float bounceStrength = 1.f;
		float bounceAmt = 1.f - normFactor;
		PVector bounceCol = new PVector();
		bounceCol.set(m);
		bounceCol.mult(_sc);
		bounceCol.mult((shadow * .5f + .5f) * bounceAmt * bounceStrength);
		amb.add(bounceCol);

		sunLight.mult(nd * shadow);
		sunLight.add(amb);
		return sunLight;
	}

}