class GammaTable {
	/**
	 * 
	 */
	private final terrainmarch GammaTable;
	int tSize = 2048;
	float tSizef = (float) tSize;
	int[] g_tab = new int[tSize];

	GammaTable(terrainmarch terrainmarch) {
		GammaTable = terrainmarch;
		for (int i = 0; i < tSize; i++) {
			g_tab[i] = (int) (terrainmarch.pow((float) i / tSize, 1 / 2.2f) * 255.f);
		}
	}

	public int Get(float v) {
		int iv = terrainmarch.max(
				terrainmarch.min((int) (v * tSizef), tSize - 1), 0);
		return g_tab[iv];
	}
}