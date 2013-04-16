import processing.core.PVector;


class Crator implements HieghtField {
	/**
	 * 
	 */
	private final terrainmarch Crator;

	/**
	 * @param terrainmarch
	 */
	Crator(terrainmarch terrainmarch) {
		Crator = terrainmarch;
	}

	public float GetHieght(float x, float z) {
		float xf = x / 6;
		float zf = (z - 8) / 6;
		float d = 1.f - terrainmarch.min(xf * xf + zf * zf, 1.f);
		float h = Crator.smoothstep(0, 1.f, d) * 2.f;
		h = h - Crator.sphereFunc(x + 0, z - 8, 3.5f);

		Crator.noiseDetail(4, 0.67f);
		float nv = Crator.noise(x * 0.5f, z * 0.5f) * 2.f - 1;
		h += nv * 0.5f;// + detailNoise(x,z)*min(h*4,1)*.05;
		return h;
	}

	public float GetAO(float x, float y, float z) {
		Crator.noiseDetail(3, 0.67f);
		float nv = Crator.noise(x * 0.5f, z * 0.5f) * 2.f - 1;
		nv *= 0.5f;
		float incrator = terrainmarch.constrain(terrainmarch.mag(x, z - 8) - 2.5f, 0, 1);
		return incrator * terrainmarch.abs(y - nv);
	}

	public PVector GetMaterial(PVector p, PVector n) {
		float rock = Crator.smoothstep(0, 1, terrainmarch.min(p.y * 2.f, 1));
		return Crator.lerp(new PVector(0.4f, 0.5f, 0.2f), new PVector(0.5f, 0.4f,
				0.2f), rock);
	}

	public float GetWaterHieght() {
		return -1000.0f;
	}

	public float GetBumpStrength(PVector p) {
		return terrainmarch.min(p.y * 2, 1) * .2f;
	}
}