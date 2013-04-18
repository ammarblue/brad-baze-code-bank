class QualitySettings {
	/**
	 * 
	 */
	private final terrainmarch QualitySettings;

	/**
	 * @param terrainmarch
	 */
	QualitySettings(terrainmarch terrainmarch) {
		QualitySettings = terrainmarch;
	}

	public boolean useSubSampling = false;
	public float shadowRayDeltaRatio = 0.025f;
	public float shadowRayStartRatio = 0.04f;
	public float rayDeltaRatio = 0.02f;
	public boolean useReflection = true;
	public float maxHgt = 2.5f;

	public void SetFast() {
		shadowRayDeltaRatio *= 4.f;
		rayDeltaRatio *= 4.f;
		useReflection = true;
		maxHgt = 2.0f;
		shadowRayStartRatio *= 4.f;
	}
}