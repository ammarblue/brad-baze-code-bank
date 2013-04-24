import java.io.PrintWriter;

import processing.core.PVector;

class Photo {
	/**
	 * 
	 */
	private final terrainmarch terrainmarch;
	int noiseSeedValue;
	int sceneId;
	float cangX;
	float cangY;
	PVector cpos;
	PVector sunDir;
	String name;
	boolean applyPost;
	boolean useClouds;

	Photo(terrainmarch terrainmarch, String n, int scene, PVector sd,
			PVector _cpos, float _cangX, float _cangY, int noiseS,
			boolean post, boolean uc) {
		this.terrainmarch = terrainmarch;
		name = n;
		sceneId = scene;
		sunDir = sd;
		sunDir.normalize();
		cpos = _cpos;
		cangX = _cangX;
		cangY = _cangY;
		noiseSeedValue = noiseS;
		applyPost = post;
		useClouds = uc;
		Set();
	}

	public void Output() {
		PrintWriter output = this.terrainmarch.createWriter("photo.txt");
		output.print("new Photo(\"name\"," + this.terrainmarch.g_SceneIdx + ",");
		output.println("new PVector(" + this.terrainmarch.g_sunDir.x + ","
				+ this.terrainmarch.g_sunDir.y + ","
				+ this.terrainmarch.g_sunDir.z + "),");
		output.print("new PVector(" + this.terrainmarch.g_camControl.m_camPos.x
				+ "," + this.terrainmarch.g_camControl.m_camPos.y + ","
				+ this.terrainmarch.g_camControl.m_camPos.z + "),");
		output.print(this.terrainmarch.g_camControl.m_CamAngleX + ","
				+ this.terrainmarch.g_camControl.m_CamAngleY + ","
				+ this.terrainmarch.g_NoiseSeed + ",");
		output.print(this.terrainmarch.g_usePost + ","
				+ this.terrainmarch.g_useClouds + "),");
		output.flush();
		output.close();
	}

	public void Set() {
		this.terrainmarch.g_NoiseSeed = noiseSeedValue;
		this.terrainmarch.g_SceneIdx = sceneId;
		this.terrainmarch.g_sunDir.set(sunDir);
		this.terrainmarch.g_camControl = new PolarCamera(this.terrainmarch,
				cpos, cangX, cangY);
		this.terrainmarch.g_usePost = applyPost;
		this.terrainmarch.g_useClouds = useClouds;
		this.terrainmarch.Reset();
	}
}