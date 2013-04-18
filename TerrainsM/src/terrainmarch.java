import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class terrainmarch extends PApplet {

	// ray marching technique for procedural hieght field
	// rendering based heavily on
	// http://iquilezles.org/www/articles/terrainmarching/terrainmarching.htm
	// with each optimizations from 'procedural texturing
	// and modeling' book.
	//
	// Controls
	// use mouse to look around
	// cursor keys to move abount
	// +/- to select scene

	HieghtField[] Scenes; /*
						 * { new Terrain(this), new TerrainWithBuildings(this),
						 * new BlockCity(this), /* new WetTerrain (this),
						 * 
						 * new Crator(this), new Egypt(this), new Wavey(this),
						 * new SimplePlane(this), };
						 */
	Photo[] photographs; /*
						 * { new Photo(this, "Terrain", 0, new PVector(1.f,
						 * 0.5f, 1), new PVector( 0.0f, 2.0f, 0.0f), 0.f, -0.3f,
						 * 7789235, false, false), new Photo(this, "valley", 0,
						 * new PVector(0.78003854f, 0.24797048f, 0.57450026f),
						 * new PVector(12.530619f, 0.7902899f, -2.8712194f),
						 * 0.89374983f, 0.0072916746f, 1789235, true, true), new
						 * Photo(this, "Terrain with Water", 3, new PVector(1.f,
						 * 0.5f, 1), new PVector(0.0f, 2.0f, 0.0f), 0.f, -0.3f,
						 * 1789235, false, false),
						 * 
						 * new Photo(this, "Block City", 2, new PVector(1.f,
						 * 0.5f, 1), new PVector( 0.0f, 2.0f, 0.0f), 0.5f,
						 * -0.3f, 1789235, false, false),
						 * 
						 * new Photo(this, "Crator", 4, new PVector(1.f, 0.5f,
						 * 1), new PVector(0.0f, 2.0f, 0.0f), 0.f, -0.3f,
						 * 1789235, true, false), new Photo(this, "Reflections",
						 * 3, new PVector(0.48092172f, 0.26969612f,
						 * 0.83425313f), new PVector(-0.012731299f, 1.2863867f,
						 * 2.8419714f), 0.63750005f, -0.016666591f, 1789235,
						 * false, true), new Photo(this, "Sine World", 6, new
						 * PVector(1.f, 0.5f, 1), new PVector( 0.0f, 2.0f,
						 * 0.0f), 0.f, -0.3f, 1789235, true, false), new
						 * Photo(this, "Sunset", 7, new PVector(1.f, 0.1f, 1),
						 * new PVector(0.0f, 2.0f, 0.0f), 0.9f, 0.0f, 1789235,
						 * false, true), new Photo(this, "rock", 0, new
						 * PVector(0.6666667f, 0.33333334f, 0.6666667f), new
						 * PVector(10.25f, 1.7044797f, 0.9553365f), -0.046875f,
						 * -0.046875f, 1789235, false, true), new Photo(this,
						 * "Crator II", 4, new PVector(0.6666667f, 0.33333334f,
						 * 0.6666667f), new PVector(11.495641f, 5.169949f,
						 * 8.202846f), -1.6156223f, -0.42916673f, 1789235, true,
						 * false), new Photo(this, "Pyramids", 5, new
						 * PVector(0.68041384f, 0.27216554f, 0.68041384f), new
						 * PVector(6.9793053f, 0.736299f, 11.575281f),
						 * 4.284375f, -0.025000066f, 1789235, true, false), new
						 * Photo(this, "sunset ", 0, new PVector(0.70649046f,
						 * 0.07023498f, 0.70422894f), new PVector(3.6276436f,
						 * 1.5967011f, -1.5997708f), 0.8718749f, -0.08749986f,
						 * 29369017, false, true), };
						 */

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
	PVector g_mpos;
	int g_NoiseSeed = 1789235;
	int g_SceneIdx = 0;
	boolean g_useClouds = false;
	PVector g_sunDir = new PVector(1.f, 0.5f, 1);
	int g_CurrentPhotograph = 0; // start on selectScreen
	int g_PhotoIconSize = 30;
	boolean g_EditWater = false;
	float g_WaterHieght = 0.8f;
	PolarCamera g_camControl = new PolarCamera(this, new PVector(0.0f, 2.0f,
			0.0f), 0.f, -0.3f);
	PhotoGen pics = new PhotoGen();
	HieghtFieldGen hf = new HieghtFieldGen(this);
	boolean state = false;

	public PImage CreateScreen(int w, int h) {
		return createImage(w, h, RGB);
	}

	public void CreateStrip(PImage sc, int xStart, int xEnd) {
		float w = sc.width;
		float h = sc.height;
		Camera cam = new Camera(this, g_camControl.m_camPos, new PVector(0.0f,
				1.0f, 0.0f), g_camControl.m_camDir, w, h, w);

		Lighting lgt = new Lighting(this, g_sunDir, constrain(g_sunDir.y * 4,
				0, 1), g_useClouds);

		xEnd = (int) min(xEnd, w);
		QualitySettings qs = new QualitySettings(this);
		qs.useSubSampling = g_HiQuality && w == width;
		if (sc.width < 320)
			qs.SetFast();

		renderImage(qs, cam, sc, lgt, Scenes[0],
				xStart, xEnd, g_usePost, g_useGamma);
	}

	public void setup() {
		Test test = new Test(pics, this, hf);
		test.drive();
		this.photographs = pics.toArray();
		this.Scenes = hf.toArray();
		g_sunDir.normalize();
		size(640, 480);
		randomSeed(12789);
		Reset();
		PhotoSize = width / 4;
		state = true;
	}

	boolean regenImage = false;

	public void Reset() {
		g_xloc = 200000;
		g_res = minRes;
		g_renderTime = (float) millis() / 1000.0f;
		regenImage = true;
		noiseSeed(g_NoiseSeed);
	}

	int PhotoSize = width / 5;

	public void mousePressed() {
		if (g_CurrentPhotograph != -1) // photo mode
		{
			float photof = floor((float) mouseX / (float) PhotoSize)
					+ floor((float) mouseY / (float) PhotoSize) * 4.f;
			int p = min((int) photof, photographs.length - 1);
			photographs[p].Set();
			Reset();
			g_CurrentPhotograph = -1;
			return;
		} else if (mouseX < g_PhotoIconSize && mouseY < g_PhotoIconSize) {
			g_CurrentPhotograph = 0;
			Reset();
			return;
		}
		Camera cam = new Camera(this, g_camControl.m_camPos, new PVector(0.0f,
				1.0f, 0.0f), g_camControl.m_camDir, width, height, width);
		Ray ray = cam.generateRay(mouseX, height - mouseY);
		float dds = ray.direction.dot(g_sunDir);
		MoveSun = dds > 0.96f;
	}

	public void mouseDragged() {
		if (g_EditWater) {
			g_WaterHieght = (1 - ((float) mouseY / (float) height)) * 2;
			Reset();
			return;
		} else if (MoveSun) {
			Camera cam = new Camera(this, g_camControl.m_camPos, new PVector(
					0.0f, 1.0f, 0.0f), g_camControl.m_camDir, width, height,
					width);
			Ray ray = cam.generateRay(mouseX, height - mouseY);
			g_sunDir.set(ray.direction);
			g_sunDir.normalize();
			Reset();
			return;
		}
		PVector mpos = new PVector((float) mouseX / (float) width,
				(float) mouseY / (float) height, 0.0f);
		if (!ApplyMove) {
			ApplyMove = true;
			g_mpos = new PVector();
			g_mpos.set(mpos);
		} else {
			float dz = (mpos.y - g_mpos.y) * 2.f;
			float dy = (mpos.x - g_mpos.x) * 2.f;
			g_camControl.update(dy, dz);
			g_mpos = mpos;
			Reset();
		}
	}

	public void keyPressed() {
		if (keyCode == UP) {
			g_camControl.forward(1.f);
		}
		if (keyCode == DOWN) {
			g_camControl.forward(-1);
		}
		if (keyCode == LEFT) {
			g_camControl.side(1.f);
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
		if (key == 'h' || key == 'H') {
			g_HiQuality = !g_HiQuality;
		}
		if (key == 'w' || key == 'W') {
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
		if (key == 's' || key == 'S') {
			photographs[0].Output();
		}

		Reset();
	}

	public void mouseReleased() {
		MoveSun = false;
		ApplyMove = false;
	}

	int MiniPhotoSizeW = 80;
	int MiniPhotoSizeH = 60;

	public void draw() {
		if (state) {
			boolean needRefresh = false;
			boolean needNewSmallPhoto = g_CurrentPhotograph != -1
					&& g_CurrentPhotograph < photographs.length;
			g_CurrentPhotograph=-1;
			if (g_CurrentPhotograph != -1) {
				if (g_CurrentPhotograph == 0) {
					g_selectScreen = CreateScreen(width / 2, height / 2);
				}
				if (g_CurrentPhotograph < photographs.length) {
					g_workScreen = CreateScreen(MiniPhotoSizeW, MiniPhotoSizeH);
					photographs[g_CurrentPhotograph].Set();
					needRefresh = true;
					CreateStrip(g_workScreen, 0, g_workScreen.height);//puts photo in box
					g_selectScreen.copy(g_workScreen, 0, 0, g_workScreen.width,
							g_workScreen.height,
							(g_CurrentPhotograph % 4) * 80 + 10,
							(g_CurrentPhotograph / 4) * 80 + 10,
							g_workScreen.width, g_workScreen.height);
					g_CurrentPhotograph=-1;
					g_CurrentPhotograph++;
				}
				image(g_selectScreen, 0, 0, width, height);

				noFill();
				stroke(~0);
				int x = (mouseX / 160);
				int y = (mouseY / 160);
				rect(x * 160 + 20, y * 160 + 20, 120, 120);
				//text(photographs[x + y].name, x * 160 + 20, y * 160 + 155);
				return;
			} else if (regenImage) {
				if (g_workScreen == null || g_xloc >= g_workScreen.width) {
					g_xloc = 0;
					g_workScreen = CreateScreen(width / g_res, height / g_res);
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
					if (g_res == 0)
						regenImage = false;
				}
			}

			if (needRefresh) {
				assert (g_screen != null);
				image(g_screen, 0, 0, width, height);

				// draw photo Icon
				//image(g_selectScreen, 0, 0, g_PhotoIconSize, g_PhotoIconSize);

				fill(~0);
				text("Time For Image :"
						+ (int) ((float) millis() / 1000.0f - g_renderTime)
						+ " secs | fps " + (int) frameRate + "| HQ "
						+ g_HiQuality + "| Image Res " + g_screen.width
						+ "| Clouds " + g_useClouds, g_PhotoIconSize + 10, 20);
			}
			noFill();
			stroke(mouseX < g_PhotoIconSize && mouseY < g_PhotoIconSize ? ~0
					: 0);
			//rect(0, 0, g_PhotoIconSize, g_PhotoIconSize);
		}
	}

	// lighting

	public PVector reflection(QualitySettings qs, HieghtField hgts,
			Lighting lgt, PVector d, PVector p, PVector n, float oldt) {
		float r = 2.f * d.dot(n);
		PVector rv = new PVector();
		rv.set(n);
		rv.mult(-r);
		rv.add(d);
		rv.normalize();

		// always go up
		rv.y = max(rv.y, 0.001f);
		rv.normalize();
		PVector np = new PVector();
		np.set(p);
		Ray ray = new Ray(this, np, rv);
		PVector c;
		float t = castRay(qs, hgts, np, rv, 0.1f, 30 - oldt, max(oldt * 2.f, 1));
		if (t > 0.0f)
			c = terrainColor(qs, hgts, ray, lgt, t, 1);
		else
			c = skyColor(qs, lgt, ray.origin, ray.direction);

		return applyClouds(c, lgt, ray.origin, ray.direction, t > 0.0f ? t
				: 1000.0f);

	}

	public PVector applyFog(Lighting lgt, PVector d, PVector c, float t) {
		float b = 1.f / 30;
		t = max(t - 4, 0);
		float fogAmount = exp(-t * b);
		float sunAmount = pow(max(d.dot(lgt._sd), 0.0f), 6.f);
		PVector fogColor = lerp(lgt.fogcol, lgt._sc, sunAmount * .75f);
		return lerp(fogColor, c, fogAmount);
	}

	public float fresnel(PVector i, PVector n) {
		float f = abs(i.dot(n));
		f = pow(1.f - f, 5);
		float r = 0.05f;
		return r + (1 - r) * (f);
	}

	public PVector terrainColor(QualitySettings qs, HieghtField hgts, Ray ray,
			Lighting lgt, float t, int depth) {
		PVector p = new PVector();
		p.set(ray.direction);
		p.mult(t);
		p.add(ray.origin);

		PVector n = getNormal(hgts, p);

		PVector m;
		if (p.y < hgts.GetWaterHieght() && depth == 0 && qs.useReflection) {

			// apply water fog
			float fogLength = -(hgts.GetWaterHieght() - p.y) / ray.direction.y;
			float wfd = 1 / 0.25f;
			float fm = exp(-fogLength * wfd);
			if (fm > 0.001f) {
				m = hgts.GetMaterial(p, n);
				PVector s = lgt.getShading(qs, hgts, p, n, m, t);
				m.mult(s);
				m = lerp(new PVector(0, 0.1f, 0.05f), m, fm);
			} else
				m = new PVector(0, 0.1f, 0.05f);

			// find water intsection point
			PVector np = new PVector();
			np.set(ray.direction);
			np.mult(-fogLength);
			np.add(p);

			assert (abs(np.y - hgts.GetWaterHieght()) < 0.0001f);
			PVector waterN = getNoiseNormal(np, 0.002f);
			// add in reflection

			PVector ref = reflection(qs, hgts, lgt, ray.direction, np, waterN,
					t);
			m = lerp(m, ref, fresnel(ray.direction, waterN));
		} else {
			m = hgts.GetMaterial(p, n);
			PVector s = lgt.getShading(qs, hgts, p, n, m, t);
			m.mult(s);
		}
		return applyFog(lgt, ray.direction, m, t);
	}

	public PVector applyClouds(PVector c1, Lighting lgt, PVector p, PVector d,
			float maxT) {
		if (!lgt.useClouds) {
			return c1;
		}
		int numSteps = 24;// 8;//16;//8;
		float cloudsSize = 1.3f; // 0.7
		float cloudHieght = 4.5f;// 5;//2.5;//- random(0,1/(float)numSteps);
		float cloudEnd = cloudHieght - cloudsSize;
		float cloudCover = .7f;

		float t0 = max((cloudHieght - p.y) / d.y, 0);
		float t1 = (cloudEnd - p.y) / d.y;
		float tmin = max(min(t0, t1), 0);
		float tmax = max(t0, t1);

		if (tmin > maxT || tmax < 0.0f) {
			return c1;
		}
		PVector np = new PVector();
		float ccv = 0.0f;

		noiseDetail(5, 0.7f);
		PVector cCol = new PVector(1.f, 1.f, 1.f);

		float dt = (tmin - tmax) / (float) numSteps;
		tmin = max(min(maxT, tmin), 0);
		tmax = max(min(maxT, tmax), 0);

		// go back to front
		// TODO : allow for step size in calc
		for (float t = tmax + dt * .5f; t > tmin; t += dt) {
			np.set(d);
			np.mult(t);
			np.add(p);
			float cv = max(
					noise(np.x * 0.35f + 13.5f, np.y * .5f, np.z * 0.35f + 5.7f)
							- cloudCover, 0)
					* 1.f / cloudCover;
			cv = min(cv, 1);
			// blend over the old one
			cCol.mult(1.f - cv);
			PVector bcol = new PVector();
			float nc = (np.y - cloudEnd) / (cloudHieght - cloudEnd);
			nc = nc * 0.9f + .1f;
			nc *= cv;
			bcol.set(nc, nc, nc);
			cCol.add(bcol);
			ccv = (1.f - cv) * ccv + cv;
		}
		cCol.mult(1.3f);
		// TODO : add detail noise
		// from http://freespace.virgin.net/hugo.elias/models/m_clouds.htm
		float cloudSharpness = 0.75f;
		// ccv =max(ccv +(noise(np.x*5.,np.z*5.)*2.-1)*0.2,0);
		float cd = min(pow(ccv, cloudSharpness), 1);
		PVector colCol = cCol;// lerp(new PVector(1.,1.,1.),new
								// PVector(0.0,0.0,0.0),shadow);//(cd-.75)*4);
		PVector sunTint = new PVector();
		sunTint.set(1, 1, 1);
		sunTint.add(lgt._sc);
		sunTint.mult(0.5f);
		colCol.mult(sunTint);
		cd *= exp(-tmin * 1 / 50);

		return lerp(c1, colCol, cd);
	}

	public PVector skyColor(QualitySettings qs, Lighting lgt, PVector p,
			PVector d) {
		float blendv = sqrt(max(d.y + 0.01f, 0.000001f));
		PVector sky = lerp(lgt.skybot, lgt.skytop, blendv);
		PVector sun = new PVector();
		sun.set(lgt._sc);
		float sf = pow(max(d.dot(lgt._sd), 0), 64.f);

		sun.mult(sf);

		sky.add(sun);
		return sky;
	}

	int g_numHieghtSamples = 0;
	int g_numHieghtShadowSamples = 0;

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////TODO most important
	// method////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////
	public float castRay(QualitySettings qs, HieghtField hgts, PVector ro,
			PVector rd, float mint, float maxt, float erratio) {
		float dt = max(qs.rayDeltaRatio * mint, 0.001f);
		float lh = 0.0f;
		float ly = 0.0f;
		PVector p = new PVector();
		erratio = erratio * qs.rayDeltaRatio;
		for (float t = mint; t < maxt; t += dt) {
			p.set(rd);
			p.mult(t);
			p.add(ro);
			float h = hgts.GetHieght(p.x, p.z);
			g_numHieghtSamples++;
			if (p.y < h) {
				// interpolate the intersection distance
				return t - dt + dt * (lh - ly) / (p.y - ly - h + lh);
			}
			// allow the error to be proportional to the distance
			dt = erratio * t;
			lh = h;
			ly = p.y;
		}
		return -1.f;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////
	public float bSample(HieghtField hgts, float x, float z, float bs) {
		return hgts.GetHieght(x, z) + detailNoise(x, z) * bs;
	}

	public PVector getNormal(HieghtField hgts, PVector p) {
		float bs = hgts.GetBumpStrength(p);
		float eps = 0.01f;
		bs /= 4;
		PVector n = new PVector(bSample(hgts, p.x - eps, p.z, bs)
				- bSample(hgts, p.x + eps, p.z, bs), 2.0f * eps, bSample(hgts,
				p.x, p.z - eps, bs) - bSample(hgts, p.x, p.z + eps, bs));
		n.normalize();
		return n;
	}

	public PVector getNoiseNormal(PVector p, float bs) {
		float eps = 0.01f;
		bs /= 4;
		PVector n = new PVector(detailNoise(p.x - eps, p.z) * bs
				- detailNoise(p.x + eps, p.z) * bs, 2.0f * eps, detailNoise(
				p.x, p.z - eps) * bs - detailNoise(p.x, p.z + eps) * bs);
		n.normalize();
		return n;
	}

	public float castShadowRay(QualitySettings qs, HieghtField hgts,
			PVector ro, PVector rd, float mint, float shadowSoftness) {
		float dt = max(qs.shadowRayStartRatio * mint, 0.001f);
		float lh = 0.0f;
		float ly = 0.0f;
		float maxt = (qs.maxHgt - ro.y) / max(rd.y, 0.1f); // find maximum
															// distance
		float sratio = qs.shadowRayDeltaRatio;
		float sratioM = random(1.2f, 1.3f);
		PVector p = new PVector();
		float sdist = 0.f;
		int bsampleCnt = 0;
		float bs = hgts.GetBumpStrength(ro);

		for (float t = mint; t < maxt; t += dt) {
			p.set(rd);
			p.mult(t);
			p.add(ro);
			float h;
			if (bsampleCnt < 4) // get the detail shadows
			{
				h = bSample(hgts, p.x, p.z, bs);
				bsampleCnt++;
			} else {
				h = hgts.GetHieght(p.x, p.z);
			}
			sdist += max(h - p.y, 0.f);

			g_numHieghtShadowSamples++;

			// allow the error to be proportional to the distance
			dt = sratio * t;
			sratio *= sratioM;
			lh = h;
			ly = p.y;
		}

		return 1 - smoothstep(0, shadowSoftness, sdist);
	}

	public PVector postEffect(float u, float v, PVector c) {
		u = (u - .5f) * 0.8f;
		v = (v - .5f) * 0.8f;
		float vignetting = max(1.f - sqrt(u * u + v * v), 0) * 1.2f;
		c.mult(vignetting);
		return c;
	}

	GammaTable g_GTable = new GammaTable(this);

	public void renderImage(QualitySettings qs, Camera cam, PImage img,
			Lighting lgt, HieghtField hgts, int xStart, int xEnd,
			boolean usePost, boolean useGamma) {
		int xres = img.width;
		int yres = img.height;

		g_numHieghtSamples = 0;
		g_numHieghtShadowSamples = 0;

		PVector c = new PVector();
		int numSamples = qs.useSubSampling ? 4 : 1;
		PVector finalC = new PVector();
		float subSampleScale = 1.f / numSamples;
		float[] sampleoffsetx4 = { 0.85f, 0.15f, 0.3f, 0.6f };
		float[] sampleoffsety4 = { 0.15f, 0.6f, 0.3f, 0.85f };
		float[] sampleoffsetx1 = { 0.5f };
		float[] sampleoffsety1 = { 0.5f };
		float[] sampleoffsetx = qs.useSubSampling ? sampleoffsetx4
				: sampleoffsetx1;
		float[] sampleoffsety = qs.useSubSampling ? sampleoffsety4
				: sampleoffsety1;

		for (int i = xStart; i < xEnd; i++) {
			float startT = 1.f;
			for (int j = 0; j < yres; j++) {
				finalC.set(0.f, 0.f, 0.f);
				for (int k = 0; k < numSamples; k++) {
					Ray ray = cam.generateRay((float) i + sampleoffsetx[k],
							(float) j + sampleoffsety[k]);
					float t = -1;
					if (startT != -1) {
						t = castRay(qs, hgts, ray.origin, ray.direction,
								startT, 50, 1.f);
					}
					if (t > 0.0f) {
						c = terrainColor(qs, hgts, ray, lgt, t, 0);
						startT = max(t - 0.1f, 0.0001f);
					} else {
						c = skyColor(qs, lgt, ray.origin, ray.direction);
						startT = -1;
					}
					c = applyClouds(c, lgt, ray.origin, ray.direction,
							t > 0.0f ? t : 50.0f);

					finalC.add(c);
				}
				finalC.mult(subSampleScale);
				c = finalC;
				if (usePost)
					c = postEffect((float) i / (float) xres, (float) j
							/ (float) yres, c);
				if (useGamma)
					img.pixels[xres * (yres - j - 1) + i] = color(
							g_GTable.Get(c.x), g_GTable.Get(c.y),
							g_GTable.Get(c.z));
				else
					img.pixels[xres * (yres - j - 1) + i] = color(c.x * 255.f,
							c.y * 255.f, c.z * 255.f);
			}
		}
		println("| NumSamples " + g_numHieghtSamples + "| Shadow Samples "
				+ g_numHieghtShadowSamples);
	}

	public PVector lerp(PVector a, PVector b, float t) {
		return new PVector(lerp(a.x, b.x, t), lerp(a.y, b.y, t), lerp(a.z, b.z,
				t));
	}

	public float smoothstep(float edge0, float edge1, float x) {
		x = min(max((x - edge0) / (edge1 - edge0), 0.0f), 1.0f);
		return x * x * (3 - 2 * x);
	}

	public PVector smoothblend(PVector a, PVector b, float x) {
		x = x * x * (3 - 2 * x);
		return lerp(a, b, x);
	}

	// scenes
	// Scenes

	public float detailNoise(float x, float z) {
		noiseDetail(2, 0.8f);
		return noise(x * 24.f, z * 24.f) * 2.f - 1;
	}

	public float terrain(float x, float z) {
		noiseDetail(4, 0.5f);
		float tn = noise(x * 0.3f + 2423.1103f, z * 0.3f + 1217.937f);
		tn *= tn;
		return tn * 4.f;// + detailNoise(x,z)*0.03;
	}

	public float ambTerrain(float x, float y, float z) {
		noiseDetail(2, 0.5f);
		float f = noise(x * 0.3f + 2423.1103f, z * 0.3f + 1217.937f);
		f = 4 * f * f;
		return constrain((y - f + 0.03f * detailNoise(x, z)) * 2.f, 0.f, 1.f);
	}

	// look at fracplanet
	public PVector getTerrainMaterial(HieghtField hgts, PVector p, PVector n) {
		PVector base = new PVector(0.75f, 0.75f, 0.75f);
		PVector layer1 = new PVector(0.75f, 1.f, 0.3f);

		PVector layer2 = new PVector(0.65f, 0.5f, 0.2f);
		float dn = detailNoise(p.x, p.z);
		float blendLayer2 = smoothstep(0.4f, 1.0f, n.y + dn * .05f);
		float blendLayer1 = smoothstep(0.9f, 1.0f, n.y + dn * .025f);

		return lerp(lerp(base, layer2, blendLayer2), layer1, blendLayer1);
	}

	public float sphereFunc(float x, float z, float r) {
		x /= r;
		z /= r;
		return max(sqrt(1 - x * x - z * z), 0) * r;
	}

	public float building(float x, float z) {
		return sphereFunc(x, z, 2) - sphereFunc(x - .5f, z - 0.8f, 1.5f);
	}

	public float quantize(float a, float q) {
		return floor(a * q) / q;
	}

	public float pyramid(float x, float z) {
		return quantize(max(1.f - (max(abs(x), abs(z))), 0), 8.f);
	}

	public float flatPyramid(float x, float z) {
		return max(1.f - (max(abs(x), abs(z))), 0);
	}

	public float oldPyramid(float x, float z) {
		noiseDetail(3, 0.7f);
		float h = flatPyramid(x, z);
		float n = noise(x * 20, z * 20) * .2f - 1;
		return h + n * .3f * min(h * 40, 1);
	}

	static public void main(String args[]) {
		PApplet.main(new String[] { "--bgcolor=#F0F0F0", "terrainmarch" });
	}
}
