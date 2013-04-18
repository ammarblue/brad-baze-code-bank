import processing.core.PVector;

class Egypt implements HieghtField {
	/**
	 * 
	 */
	private terrainmarch Egypt;

	/**
	 * @param terrainmarch
	 */
	Egypt(terrainmarch terrainmarch) {
		Egypt = terrainmarch;
	}
	Egypt(){}

	public float Buildings(float x, float z) {
		float p1 = Egypt.oldPyramid((x + 2), (z - 12.f)) * 2.f;
		float p2 = Egypt.oldPyramid((x), (z - 10)) * 1.5f;
		float p3 = Egypt.oldPyramid((x - 2) * .75f, (z - 8) * .5f) * 2.5f;
		return terrainmarch.max(terrainmarch.max(p1, p2), p3);

	}

	public float GetHieght(float x, float z) {
		float xf = x / 8;
		float zf = (z - 10) / 8;
		float d = terrainmarch.min(xf * xf + zf * zf, 1.f);
		float b = Buildings(x, z);
		Egypt.noiseDetail(3, 0.45f);
		float dune = Egypt.noise(x * 0.5f, z * 0.5f) * d * 1.5f;
		// dune +=noise(x*128.,z*128.)*0.01;
		return terrainmarch.max(b, dune);
	}

	public float GetBumpStrength(PVector p) {
		return 0.01f;
	}

	public float GetAO(float x, float y, float z) {
		return 0.5f;
	}

	public PVector GetMaterial(PVector p, PVector n) {
		return new PVector(0.9f, 0.7f, 0.5f);
	}

	public float GetWaterHieght() {
		return -1000.0f;
	}

	@Override
	public void addTerrainmarch(terrainmarch in) {
		Egypt = in;
	}
}