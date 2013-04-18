import processing.core.PVector;

class CityScape implements HieghtField {
	/**
	 * 
	 */
	private terrainmarch CityScape;

	/**
	 * @param terrainmarch
	 */
	CityScape(terrainmarch terrainmarch) {
		CityScape = terrainmarch;
	}
	CityScape(){}
	public float GetHieght(float x, float z) {
		x *= 1.5f;
		z *= 1.5f;
		float xf = x - terrainmarch.floor(x);
		float xz = z - terrainmarch.floor(z);
		if (x < 0) {
			return 0;
		}
		float h = CityScape.noise(terrainmarch.floor(x + .5f),
				terrainmarch.floor(z + .5f));
		return terrainmarch.min(terrainmarch.abs(xf - .5f) * 4, 1)
				* terrainmarch.min(terrainmarch.abs(xz - .5f) * 4, 1) * 3.5f * h;
	}

	public float GetAO(float x, float y, float z) {
		return terrainmarch.min(y, 1);
	}

	public PVector GetMaterial(PVector p, PVector n) {
		float ix = terrainmarch.floor(p.x * .5f + .5f);
		float iz = terrainmarch.floor(p.z * .5f + .5f);
		float n1 = CityScape.noise(ix, iz);
		float n2 = CityScape.noise(iz, ix);
		return new PVector(0, 0, 0);
	}

	public float GetBumpStrength(PVector p) {
		return 0.0f;
	}

	public float GetWaterHieght() {
		return -1000.0f;
	}

	@Override
	public void addTerrainmarch(terrainmarch in) {
		CityScape = in;
	}
}