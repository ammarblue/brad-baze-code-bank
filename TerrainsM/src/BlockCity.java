import processing.core.PVector;


class BlockCity implements HieghtField {
	/**
	 * 
	 */
	private final terrainmarch BlockCity;

	/**
	 * @param terrainmarch
	 */
	BlockCity(terrainmarch terrainmarch) {
		BlockCity = terrainmarch;
	}

	public float GetHieght(float x, float z) {
		x *= 0.5f;
		z *= 0.5f;
		float xf = x - terrainmarch.floor(x);
		float xz = z - terrainmarch.floor(z);
		if (x < 0) {
			return 0;
		}
		float h = BlockCity.noise(terrainmarch.floor(x + .5f), terrainmarch.floor(z + .5f));
		return terrainmarch.min(terrainmarch.abs(xf - .5f) * 3, 1.f) * terrainmarch.min(terrainmarch.abs(xz - .5f) * 3, 1)
				* 3.f * h;
	}

	public float GetAO(float x, float y, float z) {
		return terrainmarch.min(y, 1);
	}

	public PVector GetMaterial(PVector p, PVector n) {
		float ix = terrainmarch.floor(p.x * .5f + .5f);
		float iz = terrainmarch.floor(p.z * .5f + .5f);
		float n1 = BlockCity.noise(ix, iz);
		float n2 = BlockCity.noise(iz, ix);
		return new PVector(n1, 0.5f, n2);
	}

	public float GetBumpStrength(PVector p) {
		return 0.0f;
	}

	public float GetWaterHieght() {
		return -1000.0f;
	}
}