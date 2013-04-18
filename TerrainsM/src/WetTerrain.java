import processing.core.PVector;

class WetTerrain extends Terrain {
	/**
	 * 
	 */
	private terrainmarch WetTerrain;

	/**
	 * @param terrainmarch
	 */
	WetTerrain(terrainmarch terrainmarch) {
		WetTerrain = terrainmarch;
	}
	WetTerrain(){
		super();
	}
	public float GetHieght(float x, float z) {
		float h = super.GetHieght(x, z);
		float wh = GetWaterHieght();
		float shoresize = 0.2f;
		float b = WetTerrain.smoothstep(0, shoresize,
				terrainmarch.abs(h - GetWaterHieght() - 0.05f));
		// create shorelines
		h = wh + (h - wh) * b;
		return h;
	}

	public float GetWaterHieght() {
		return WetTerrain.g_WaterHieght;
	}

	public float GetBumpStrength(PVector p) {
		float shoresize = 0.15f;
		float b = WetTerrain.smoothstep(0, shoresize,
				terrainmarch.abs(p.y - GetWaterHieght() - 0.05f));
		return super.GetBumpStrength(p) * b;
	}

	public PVector GetMaterial(PVector p, PVector n) {
		float shoresize = 0.02f;
		float b = WetTerrain.smoothstep(0, shoresize,
				terrainmarch.abs(p.y - GetWaterHieght()));
		return WetTerrain.lerp(new PVector(1.f, 1.f, 0.5f),
				super.GetMaterial(p, n), b);
	}

	public float GetAO(float x, float y, float z) {
		return super.GetAO(x, y, z);
	}
	public void addTerrainmarch(terrainmarch in){
		super.addTerrainmarch(in);
		WetTerrain=in;
	}
}