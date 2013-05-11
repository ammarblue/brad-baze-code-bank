package reuze.demo;

import java.io.PrintWriter;

import com.software.reuze.gb_Ray;
import com.software.reuze.gb_TerrainBlockCity;
import com.software.reuze.gb_TerrainBuildings;
import com.software.reuze.gb_TerrainCamera;
import com.software.reuze.gb_TerrainCrater;
import com.software.reuze.gb_TerrainEgypt;
import com.software.reuze.gb_TerrainGammaTable;
import com.software.reuze.gb_TerrainMaterials;
import com.software.reuze.gb_TerrainPerturb;
import com.software.reuze.gb_TerrainPolarCamera;
import com.software.reuze.gb_TerrainQualitySettings;
import com.software.reuze.gb_TerrainRolling;
import com.software.reuze.gb_TerrainSimple;
import com.software.reuze.gb_TerrainSimplePlane;
import com.software.reuze.gb_TerrainSnow;
import com.software.reuze.gb_TerrainSunLighting;
import com.software.reuze.gb_TerrainWet;
import com.software.reuze.gb_Vector3;
import com.software.reuze.gb_i_TerrainHeightField;
import com.software.reuze.m_NoiseGradient;

import processing.core.PApplet;
import processing.core.PImage;

public class demoTerrains extends PApplet {
	// ray marching technique for procedural height field
	// rendering based heavily on
	// http://iquilezles.org/www/articles/terrainmarching/terrainmarching.htm
	// with each optimizations from 'procedural texturing and modeling' book.
	//
	// Controls
	// use mouse to look around
	// cursor keys to move about
	// +/- to select scene

	gb_i_TerrainHeightField[] Scenes;
	Photo[] photographs;

	boolean g_HiQuality = false;
	boolean g_usePost = false;
	boolean g_useGamma = true;
	PImage g_workScreen = null;
	PImage g_screen = null;
	PImage g_selectScreen = null;
	int g_res = 16;
	int minRes = 8;
	float g_renderTime = 0.0f;
	int g_xloc = 200000;
	int g_maxPixelStep = 2 * 5000;
	boolean MoveSun = false;
	boolean ApplyMove = false;
	gb_Vector3 g_mpos;
	int g_NoiseSeed = 1789235;
	int g_SceneIdx = 0;
	boolean g_useClouds = false;
	gb_Vector3 g_sunDir = new gb_Vector3(1, 0.5f, 1);
	int g_CurrentPhotograph = 0; // start on selectScreen
	int g_PhotoIconSize = 30;
	boolean g_EditWater = false;
	boolean g_UseExpensiveAO = false;
	boolean g_UseRefinement = true;
	boolean g_doCloudShadows = false;
	boolean g_walk = false;
	boolean g_perturb = false;

	float g_WaterHeight = -0.2f;
	gb_TerrainPolarCamera g_camControl = new gb_TerrainPolarCamera();
	m_NoiseGradient g_NoiseGen;
	gb_TerrainMaterials g_materials;

	PImage CreateScreen(int w, int h) {
		return createImage(w, h, RGB);
	}

	void CreateStrip(PImage sc, int xStart, int xEnd) {
		float w = sc.width;
		float h = sc.height;

		if (g_walk) {
			g_camControl.m_camPos.y = Scenes[g_SceneIdx].GetHeight(
					g_camControl.m_camPos.x, g_camControl.m_camPos.z, 0) + 0.1f;
		}

		gb_TerrainCamera cam = new gb_TerrainCamera(g_camControl.m_camPos,
				new gb_Vector3(0.0f, 1.0f, 0.0f), g_camControl.m_camDir, w, h,
				w);

		g_materials.SetFovRatio(45, sc.width);

		gb_TerrainSunLighting lgt = new gb_TerrainSunLighting(g_materials,
				g_sunDir, constrain(g_sunDir.y * 4, 0, 1), g_useClouds);

		xEnd = (int) min(xEnd, w);
		gb_TerrainQualitySettings qs = new gb_TerrainQualitySettings();
		qs.useSubSampling = g_HiQuality && w == width;
		if (sc.width < 320)
			qs.SetFast();

		gb_i_TerrainHeightField hgt = Scenes[g_SceneIdx % Scenes.length];
		if (g_perturb) {
			hgt = new gb_TerrainPerturb(g_materials, hgt);
		}
		renderImage(qs, cam, sc, lgt, hgt, xStart, xEnd, g_usePost, g_useGamma);
	}

	public void setup() {
		g_sunDir.nor();
		size(700, 440);// 360); // HD quarter rez works better
		randomSeed(12789);
		Reset();
		PhotoSizeX = width / 4;
		PhotoSizeY = height / 3;
		Scenes = new gb_i_TerrainHeightField[] {
				new gb_TerrainSimple(g_materials),
				new gb_TerrainBuildings(g_materials),
				new gb_TerrainBlockCity(),
				new gb_TerrainWet(g_materials, g_WaterHeight),
				new gb_TerrainCrater(g_materials),
				new gb_TerrainEgypt(g_materials), new gb_TerrainRolling(),
				new gb_TerrainSimplePlane(g_materials, 0.3f),
				new gb_TerrainSnow(g_materials), };
		photographs = new Photo[] {
				new Photo("Terrain", 0, new gb_Vector3(1, 0.5f, 1),
						new gb_Vector3(0.0f, 2.0f, 0.0f), 0, -0.25f, 1789235,
						false, false),
				new Photo("Valley", 0, new gb_Vector3(0.75711614f, 0.35974383f,
						0.54530686f), new gb_Vector3(5.7643337f, 0.24380729f,
						1.2518655f), 0.9031241f, -0.013541698f, 1789235, true,
						true),
				new Photo("Terrain with Water", 3, new gb_Vector3(1, 0.5f, 1),
						new gb_Vector3(0.0f, 2.0f, 0.0f), 0, -0.25f, 1789235,
						false, false),
				new Photo("Block City", 2, new gb_Vector3(0.76745266f,
						0.13576707f, 0.62656504f), new gb_Vector3(-1.9958267f,
						4.6237698f, -11.27423f), 0.915625f, -0.12499994f,
						1789235, false, false),

				new Photo("Crator", 4, new gb_Vector3(0.7794047f, 0.420355f,
						0.46457517f), new gb_Vector3(-1.0333986f, 0.100956775f,
						0.40554827f), 0.35312504f, 0.099999785f, 1789235, true,
						false),
				new Photo("Reflections", 3, new gb_Vector3(-0.25588194f,
						0.23144478f, 0.9385935f), new gb_Vector3(27.797665f,
						0.15013918f, -2.2791333f), 6.0093794f, -0.07499951f,
						1789235, false, true),
				new Photo("Forests", 7, new gb_Vector3(0.71523553f,
						0.36930996f, 0.59333664f), new gb_Vector3(-13.837193f,
						0.92376465f, -1.1682764f), 0.109375134f, -0.2150001f,
						1789235, false, false),
				new Photo("Snowy Rocks", 8, new gb_Vector3(0.70858866f,
						0.258834f, 0.6564352f), new gb_Vector3(-5.7562084f,
						0.7503891f, 5.645005f), 0.9f, 0.0f, 1789235, true, true),
				new Photo("rock", 8, new gb_Vector3(-0.1716191f, 0.5352259f,
						-0.82709134f), new gb_Vector3(-6.1476126f, 0.16771004f,
						7.3597717f), 2.0187488f, 0.1291665f, 1789235, false,
						true),
				new Photo("Crator II", 4, new gb_Vector3(0.6666667f,
						0.33333334f, 0.6666667f), new gb_Vector3(11.495641f,
						5.169949f, 8.202846f), -1.6156223f, -0.42916673f,
						1789235, true, false),
				new Photo("Pyramids", 5, new gb_Vector3(0.68041384f,
						0.27216554f, 0.68041384f), new gb_Vector3(6.9793053f,
						0.736299f, 11.575281f), 4.284375f, -0.025000066f,
						1789235, true, false),
				new Photo("sunset", 0, new gb_Vector3(0.90130556f, 0.05478374f,
						0.42970574f), new gb_Vector3(-2.2255332f, 0.888599f,
						-2.7276325f), 0.89375f, -0.054166615f, 167267927, true,
						true), };
	}

	boolean regenImage = false;

	void Reset() {
		g_xloc = 200000;
		g_res = minRes;
		g_renderTime = (float) millis() / 1000.0f;
		regenImage = true;
		noiseSeed(g_NoiseSeed);
		randomSeed(g_NoiseSeed);
		g_NoiseGen = new m_NoiseGradient(7);
		g_HiQuality = false;
		g_materials = new gb_TerrainMaterials(g_NoiseGen, 0);
	}

	class Photo {
		int noiseSeedValue;
		int sceneId;
		float cangX;
		float cangY;
		gb_Vector3 cpos;
		gb_Vector3 sunDir;
		String name;
		boolean applyPost;
		boolean useClouds;

		Photo(String n, int scene, gb_Vector3 sd, gb_Vector3 _cpos,
				float _cangX, float _cangY, int noiseS, boolean post, boolean uc) {
			name = n;
			sceneId = scene;
			sunDir = sd;
			sunDir.nor();
			cpos = _cpos;
			cangX = _cangX;
			cangY = _cangY;
			noiseSeedValue = noiseS;
			applyPost = post;
			useClouds = uc;
		}

		void Output() {
			PrintWriter output = createWriter("photo.txt");
			output.print("new Photo(\"name\"," + g_SceneIdx + ",");
			output.println("new Vector3(" + g_sunDir.x + "," + g_sunDir.y + ","
					+ g_sunDir.z + "),");
			output.print("new Vector3(" + g_camControl.m_camPos.x + ","
					+ g_camControl.m_camPos.y + "," + g_camControl.m_camPos.z
					+ "),");
			output.print(g_camControl.m_CamAngleX + ","
					+ g_camControl.m_CamAngleY + "," + g_NoiseSeed + ",");
			output.print(g_usePost + "," + g_useClouds + "),");
			output.flush();
			output.close();
		}

		void Set() {
			g_NoiseSeed = noiseSeedValue;
			g_SceneIdx = sceneId;
			g_sunDir.set(sunDir);
			g_camControl = new gb_TerrainPolarCamera(cpos, cangX, cangY);
			g_usePost = applyPost;
			g_useClouds = useClouds;
			Reset();
		}
	};

	int PhotoSizeX = width / 5;
	int PhotoSizeY = height / 3;

	public void mousePressed() {
		if (g_CurrentPhotograph != -1) // photo mode
		{
			float photof = floor((float) mouseX / (float) PhotoSizeX)
					+ floor((float) mouseY / (float) PhotoSizeY) * 4;
			int p = min((int) photof, photographs.length - 1);
			photographs[p].Set();
			Reset();
			g_CurrentPhotograph = -1;
			return;
		} else if (mouseX < g_PhotoIconSize && mouseY < g_PhotoIconSize) {
			g_CurrentPhotograph = 0;
			g_walk = false;
			Reset();
			return;
		}
		gb_TerrainCamera cam = new gb_TerrainCamera(g_camControl.m_camPos,
				new gb_Vector3(0.0f, 1.0f, 0.0f), g_camControl.m_camDir, width,
				height, width);
		gb_Ray ray = cam.generateRay(mouseX, height - mouseY);
		float dds = ray.direction.dot(g_sunDir);
		MoveSun = dds > 0.96;
	}

	float GetMouseAltitude() {
		gb_TerrainCamera cam = new gb_TerrainCamera(g_camControl.m_camPos,
				new gb_Vector3(0.0f, 1.0f, 0.0f), g_camControl.m_camDir, width,
				height, width);
		gb_Ray ray = cam.generateRay(mouseX, height - mouseY);
		float[] pt_t = gb_TerrainSunLighting.castRay(
				new gb_TerrainQualitySettings(), Scenes[g_SceneIdx],
				ray.origin, ray.direction, 0.001f, 50, 1, g_UseRefinement);
		if (pt_t[1] < 0) {
			return -1;
		}
		float wh = ray.origin.y + pt_t[1] * ray.direction.y;
		return wh;
	}

	public void mouseClicked() {
		if (g_EditWater) {
			g_WaterHeight = GetMouseAltitude();
			Reset();
			return;
		}
	}

	public void mouseDragged() {
		if (MoveSun) {
			gb_TerrainCamera cam = new gb_TerrainCamera(g_camControl.m_camPos,
					new gb_Vector3(0.0f, 1.0f, 0.0f), g_camControl.m_camDir,
					width, height, width);
			gb_Ray ray = cam.generateRay(mouseX, height - mouseY);
			g_sunDir.set(ray.direction);
			g_sunDir.nor();
			Reset();
			return;
		}
		gb_Vector3 mpos = new gb_Vector3((float) mouseX / (float) width,
				(float) mouseY / (float) height, 0.0f);
		if (!ApplyMove) {
			ApplyMove = true;
			g_mpos = new gb_Vector3();
			g_mpos.set(mpos);
		} else {
			float dz = (mpos.y - g_mpos.y) * 2;
			float dy = (mpos.x - g_mpos.x) * 2;
			g_camControl.update(dy, dz);
			g_mpos = mpos;
			Reset();
		}
	}

	public void keyPressed() {
		if (keyCode == UP) {
			g_camControl.forward(1);
		}
		if (keyCode == DOWN) {
			g_camControl.forward(-1);
		}
		if (keyCode == LEFT) {
			g_camControl.side(1);
		}
		if (keyCode == RIGHT) {
			g_camControl.side(-1);
		}
		if (key == '-' || key == '_') {
			g_SceneIdx--;
		}
		if (key == '=' || key == '+') {
			g_SceneIdx++;
		}
		if (key == 'e' || key == 'E') {
			g_EditWater = !g_EditWater;
		}
		if (key == 'c' || key == 'C') {
			g_useClouds = !g_useClouds;
		}
		if (key == 'v' || key == 'V') {
			g_usePost = !g_usePost;
		}
		if (key == 'g' || key == 'G') {
			g_useGamma = !g_useGamma;
		}
		if (key == 'r' || key == 'R') {
			g_NoiseSeed += 27579782;
		}
		if (key == 'p' || key == 'P') {
			g_CurrentPhotograph = g_CurrentPhotograph == -1 ? 0 : -1;
		}
		if (key == 't' || key == 'T') {
			g_perturb = !g_perturb;
		}
		if (key == 's' || key == 'S') {
			photographs[0].Output();
		}
		if (key == 'a' || key == 'A') {
			g_UseExpensiveAO = !g_UseExpensiveAO;
		}
		if (key == 'w' || key == 'W') {
			g_walk = !g_walk;
		}
		if (key == 'j' || key == 'J') {
			g_doCloudShadows = !g_doCloudShadows;
		}
		if (key == 'r' || key == 'R') {
			g_UseRefinement = !g_UseRefinement;
		}
		Reset();
	}

	public void mouseReleased() {
		MoveSun = false;
		ApplyMove = false;
	}

	int MiniPhotoSizeW = 2 * 80;
	int MiniPhotoSizeH = 2 * 60;

	public void draw() {
		boolean needRefresh = false;
		// boolean needNewSmallPhoto = g_CurrentPhotograph!=-1 &&
		// g_CurrentPhotograph < photographs.length;

		if (g_CurrentPhotograph != -1) {
			if (g_CurrentPhotograph == 0) {
				g_selectScreen = CreateScreen(width, height);
			}
			if (g_CurrentPhotograph < photographs.length) {
				g_workScreen = CreateScreen(MiniPhotoSizeW, MiniPhotoSizeH);
				photographs[g_CurrentPhotograph].Set();
				needRefresh = true;
				CreateStrip(g_workScreen, 0, g_workScreen.height);
				g_selectScreen.copy(g_workScreen, 0, 0, g_workScreen.width,
						g_workScreen.height,
						(g_CurrentPhotograph % 4) * 80 * 2 + 45,
						(g_CurrentPhotograph / 4) * 70 * 2 + 20,
						g_workScreen.width, g_workScreen.height);
				g_CurrentPhotograph++;
			}
			image(g_selectScreen, 0, 0, width, height);
			// text( "Please Click on a Scene to Explore it", 200,11);

			noFill();
			stroke(~0);
			int x = (mouseX / PhotoSizeX);
			int y = (mouseY / PhotoSizeY);
			rect(x * 160 + 45, y * 140 + 20, 120, 120);
			// text(photographs[x + y*4].name, x*160+45,y*140+155);
			return;
		} else if (regenImage) {
			if (g_workScreen == null || g_xloc >= g_workScreen.width) {
				g_xloc = 0;
				g_workScreen = CreateScreen(width / g_res, (height - 60)
						/ g_res);
				g_res >>= 1;
			}
			int numPixels = g_workScreen.width * g_workScreen.height;
			int numSteps = (int) ceil((float) numPixels
					/ (float) g_maxPixelStep);
			int step = g_workScreen.width / numSteps;

			CreateStrip(g_workScreen, g_xloc, g_xloc + step);
			g_xloc += step;
			if (g_xloc >= g_workScreen.width) {
				g_screen = g_workScreen;
				needRefresh = true;
				if (g_res == 0) {
					if (g_HiQuality == false) {
						g_HiQuality = true;
						g_res = 1;
					} else
						regenImage = false;
				}
			}
		}

		if (needRefresh) {
			assert (g_screen != null);
			background(0);
			image(g_screen, 0, 30, width, height - 60);
			if (g_screen.width > 120 && g_screen.width < width)
				filter(BLUR, 1);
			// draw photo Icon
			image(g_selectScreen, 0, 0, g_PhotoIconSize, g_PhotoIconSize);

			fill(~0);
			// text( "Time For Image :" + (int)((float)millis()/1000.0-
			// g_renderTime )+
			// " secs | fps "+(int)frameRate + "| HQ "+g_HiQuality
			// +"| Image Res " + g_screen.width +"| Clouds "+g_useClouds + (
			// g_EditWater ? " | Edit Water |" : ""), g_PhotoIconSize +10,20);
		}
		noFill();
		stroke(mouseX < g_PhotoIconSize && mouseY < g_PhotoIconSize ? ~0 : 0);
		rect(0, 0, g_PhotoIconSize, g_PhotoIconSize);

	}

	float CalcLocalAO(float cds[], float y, float t) {
		float ratio = (.2f * min(1 / t, 1)) / g_eps;
		float ao = max(cds[0] - y, 0);
		ao += max(cds[1] - y, 0);
		ao += max(cds[2] - y, 0);
		ao += max(cds[3] - y, 0);

		return constrain(1.0f - ao * ratio, 0, 1);
	}

	int g_numHeightSamples = 0;
	int g_numHeightShadowSamples = 0;

	// float bSample( HeightField hgts, float x, float z, float bs )
	// {
	// return hgts.GetHeight(x,z) + detailBumpNoise(x,z)*bs;
	// }
	// Vector3 getNormal( HeightField hgts, Vector3 p )
	// {
	// float bs = hgts.GetBumpStrength(p);
	// bs/=4;
	// float eps =0.01f;
	// Vector3 n = new Vector3 ( bSample(hgts,p.x-eps,p.z,bs) -
	// bSample(hgts,p.x+eps,p.z,bs),
	// 2.0f*eps,
	// bSample(hgts,p.x,p.z-eps,bs) - bSample(hgts,p.x,p.z+eps,bs) );
	// n.normalize();
	// return n;
	// }
	float g_eps = 0.001f;

	float bumpHeight(float x, float z, float bs, float btile, int oct) {
		return g_NoiseGen.Fbm(x * btile, z * btile, oct, 0.5f, 2.189f) * bs
				* 0.01f;
		// return (noise(x*btile,z*btile)*2.-1)*bs;
	}

	float[] getBumpCDs(float x, float z, float bs, float btile, float t) {
		int numBumpOctaves = m_NoiseGradient.CalcNoiseOctaves(
				g_materials.fovRatio, t, btile * 4, 1, 1 / 2.189f);
		float[] ps = new float[4];
		if (numBumpOctaves == 0) {
			ps[0] = ps[1] = ps[2] = ps[3] = 0.0f;
			return ps;
		}
		float eps = g_eps;
		ps[0] = bumpHeight((x - eps), z, bs, btile, numBumpOctaves) * 5;
		ps[1] = bumpHeight((x + eps), z, bs, btile, numBumpOctaves) * 5;
		ps[2] = bumpHeight(x, (z - eps), bs, btile, numBumpOctaves) * 5;
		ps[3] = bumpHeight(x, (z + eps), bs, btile, numBumpOctaves) * 5;
		return ps;
	}

	gb_Vector3 postEffect(float u, float v, gb_Vector3 c) {
		u = (u - .5f) * 0.8f;
		v = (v - .5f) * 0.8f;
		float vignetting = max(1 - sqrt(u * u + v * v), 0) * 1.2f;
		c.mul(vignetting);
		return c;
	}

	gb_TerrainGammaTable g_GTable = new gb_TerrainGammaTable();

	void renderImage(gb_TerrainQualitySettings qs, gb_TerrainCamera cam,
			PImage img, gb_TerrainSunLighting lgt,
			gb_i_TerrainHeightField hgts, int xStart, int xEnd,
			boolean usePost, boolean useGamma) {
		int xres = img.width;
		int yres = img.height;

		g_numHeightSamples = 0;
		g_numHeightShadowSamples = 0;

		gb_Vector3 c = new gb_Vector3();
		int numSamples = qs.useSubSampling ? 4 : 1;
		gb_Vector3 finalC = new gb_Vector3();
		float subSampleScale = 1f / numSamples;
		float[] sampleoffsetx4 = { 0.85f, 0.3f, 0.15f, 0.6f };
		float[] sampleoffsety4 = { 0.15f, 0.3f, 0.6f, 0.85f };
		float[] sampleoffsetx1 = { 0.5f };
		float[] sampleoffsety1 = { 0.5f };
		float[] sampleoffsetx = qs.useSubSampling ? sampleoffsetx4
				: sampleoffsetx1;
		float[] sampleoffsety = qs.useSubSampling ? sampleoffsety4
				: sampleoffsety1;

		float maxT = 70;
		for (int i = xStart; i < xEnd; i++) {
			float startT = 0.01f;
			for (int j = 0; j < yres; j++) {
				finalC.set(0, 0, 0);
				for (int k = 0; k < numSamples; k++) {
					gb_Ray ray = cam.generateRay((float) i + sampleoffsetx[k],
							(float) j + sampleoffsety[k]);
					float[] pt_t = null;
					if (startT != -1.) {
						pt_t = gb_TerrainSunLighting.castRay(qs, hgts,
								ray.origin, ray.direction, startT, maxT, 1,
								g_UseRefinement);
					}

					if (pt_t != null && pt_t[1] > 0.0f) {
						// c = new Vector3(1,1,1);
						c = lgt.terrainColor(qs, hgts, ray, pt_t[1], 0,
								g_UseRefinement);
						startT = pt_t[0];// max(t-0.1f,0.01);
					} else {
						c = lgt.skyColor(ray.direction);
						startT = -1;
						// c = new Vector3(0,0,0);
					}
					c = lgt.applyClouds(c, ray.origin, ray.direction,
							pt_t != null && pt_t[1] > 0.0f ? pt_t[1] : 50.0f,
							false, g_doCloudShadows);
					finalC.add(c);
				}
				finalC.mul(subSampleScale);
				c = finalC;
				if (usePost)
					c = postEffect((float) i / (float) xres, (float) j
							/ (float) yres, c);
				if (useGamma) {
					img.pixels[xres * (yres - j - 1) + i] = color(
							g_GTable.Get(c.x), g_GTable.Get(c.y),
							g_GTable.Get(c.z));
				} else
					img.pixels[xres * (yres - j - 1) + i] = color(c.x * 255,
							c.y * 255, c.z * 255);
			}
		}
		// println("| NumSamples " + g_numHeightSamples +
		// "| Shadow Samples "+g_numHeightShadowSamples);
	}
}