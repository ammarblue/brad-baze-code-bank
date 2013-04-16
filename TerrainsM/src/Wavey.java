import processing.core.PVector;


class Wavey implements HieghtField {
	/**
	 * 
	 */
	private final terrainmarch Wavey;

	/**
	 * @param terrainmarch
	 */
	Wavey(terrainmarch terrainmarch) {
		Wavey = terrainmarch;
	}

	public float GetHieght(float x, float z) {
		return terrainmarch.sin(x) * terrainmarch.sin(z) * 1.5f;
	}

	public float GetAO(float x, float y, float z) {
		y /= 1.5f;
		y = y + 1;
		return terrainmarch.min(y * y, 1) * 0.5f;
	}

	public PVector GetMaterial(PVector p, PVector n) {
		return new PVector(0.75f, 0.55f, 0.4f);
	}

	public float GetWaterHieght() {
		return -10000.0f;
	}

	public float GetBumpStrength(PVector p) {
		return 0.05f;
	}

}