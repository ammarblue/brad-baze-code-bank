import processing.core.PVector;


class SimplePlane implements HieghtField {
	/**
	 * 
	 */
	private final terrainmarch SimplePlane;

	/**
	 * @param terrainmarch
	 */
	SimplePlane(terrainmarch terrainmarch) {
		SimplePlane = terrainmarch;
	}

	public float GetHieght(float x, float z) {
		return 1.f;
	}

	public float GetAO(float x, float y, float z) {
		return 0.5f;
	}

	public PVector GetMaterial(PVector p, PVector n) {
		float b = SimplePlane.noise(terrainmarch.floor(p.x), terrainmarch.floor(p.z));
		return SimplePlane.lerp(new PVector(1.0f, 0.0f, 0.2f), new PVector(1.f, 1.f,
				0.5f), b);
	}

	public float GetWaterHieght() {
		return -1000.0f;
	}

	public float GetBumpStrength(PVector p) {
		return 0.01f;
	}
}