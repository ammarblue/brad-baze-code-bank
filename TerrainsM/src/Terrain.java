import processing.core.PVector;


class Terrain implements HieghtField {
	/**
	 * 
	 */
	private final terrainmarch Terrain;
	Terrain(){
		Terrain=null;
	}

	/**
	 * @param terrainmarch
	 */
	Terrain(terrainmarch terrainmarch) {
		Terrain = terrainmarch;
	}

	public float GetHieght(float x, float z) {
		return Terrain.terrain(x, z);
	}

	public float GetAO(float x, float y, float z) {
		return Terrain.ambTerrain(x, y, z);
	}

	public PVector GetMaterial(PVector p, PVector n) {
		// return new PVector(1.,1.,1.);
		return Terrain.getTerrainMaterial(this, p, n);
	}

	public float GetWaterHieght() {
		return -10000.0f;
	}

	public float GetBumpStrength(PVector p) {
		return 0.05f * terrainmarch.min(p.y * 2, 1);
	}
}