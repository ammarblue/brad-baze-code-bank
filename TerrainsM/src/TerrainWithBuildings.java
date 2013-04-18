import processing.core.PVector;

class TerrainWithBuildings extends Terrain {
	/**
	 * 
	 */
	private terrainmarch TerrainWithBuildings;

	/**
	 * @param terrainmarch
	 */
	TerrainWithBuildings() {
	}

	TerrainWithBuildings(terrainmarch terrainmarch) {
		TerrainWithBuildings = terrainmarch;
	}

	public float Buildings(float x, float z) {
		return TerrainWithBuildings.building(x - 1, z - 6)
				* 1.4f
				+ TerrainWithBuildings.flatPyramid((x + 3) * .25f,
						(z - 15.f) * .25f) * 8.f;
	}

	public float GetHieght(float x, float z) {
		return terrainmarch.max(Buildings(x, z), super.GetHieght(x, z));
	}

	public float GetAO(float x, float y, float z) {
		if (terrainmarch.abs(y - Buildings(x, z)) < 0.01f) {
			return 0.5f;
		}
		return super.GetAO(x, y, z);
	}

	public PVector GetMaterial(PVector p, PVector n) {
		if (terrainmarch.abs(p.y - Buildings(p.x, p.z)) < 0.01f) {
			return new PVector(1.0f, 0.8f, 1.0f);
		}
		return super.GetMaterial(p, n);
	}

	public void addTerrainmarch(terrainmarch in) {
		super.addTerrainmarch(in);
		TerrainWithBuildings = in;
	}
}