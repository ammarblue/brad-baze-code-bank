import processing.core.PVector;

interface HieghtField {
	public float GetHieght(float x, float z);

	public float GetAO(float x, float y, float z);

	// int GetMaterialID(float x, float z);
	public PVector GetMaterial(PVector p, PVector n);

	public float GetWaterHieght();

	public float GetBumpStrength(PVector p);

	// float GetMaxHgt();

	public void addTerrainmarch(terrainmarch in);
}