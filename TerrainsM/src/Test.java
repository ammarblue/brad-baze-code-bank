import processing.core.PVector;

public class Test {
	PhotoGen p;
	terrainmarch t;
	HieghtFieldGen hf;
	demoXML tx;
	int noise=0;
	String Name;
	boolean clouds;
	int time=0;
	Test() {
	}

	Test(PhotoGen p, terrainmarch t, HieghtFieldGen hf) {
		this.p = p;
		this.t = t;
		this.hf = hf;
		tx = new demoXML(this);
	}

	void drive(int state) {
		switch (state) {
		case 0:
			hf.addHf(new CityScape());
			p.addPhoto(t, Name, 2, new PVector((float)time, 0.5f, 1), new PVector(
					0.0f, 8.0f, 0.0f), 0.5f, -0.3f, noise*1000000, false, clouds);
			break;
		case 1:
			hf.addHf(new Terrain());
			p.addPhoto(t, Name, 0, new PVector((float)time, 0.5f, 1), new PVector(
					0.0f, 8.0f, 0.0f), 0.f, -0.3f, noise*1000000, false, clouds);
			break;
		case 2:
			hf.addHf(new TerrainWithBuildings());
			p.addPhoto(t, Name, 0, new PVector((float)time, 0.24797048f,
					0.57450026f), new PVector(12.530619f, 8.7902899f,
					-2.8712194f), 0.89374983f, 0.0072916746f, noise*1000000, true,
					clouds);
			break;
		case 3:
			hf.addHf(new BlockCity());
			p.addPhoto(t, Name, 2, new PVector((float)time, 0.5f, 1),
					new PVector(0.0f, 8.0f, 0.0f), 0.5f, -0.3f, noise*1000000, false,
					clouds);
			break;
		case 4:
			hf.addHf(new WetTerrain());
			p.addPhoto(t, Name, 3, new PVector((float)time, 0.5f, 1),
					new PVector(0.0f, 8.0f, 0.0f), 0.f, -0.3f, noise*1000000, false,
					clouds);
			break;
		case 5:
			hf.addHf(new Crator());
			p.addPhoto(t, Name, 4, new PVector((float)time, 0.5f, 1), new PVector(
					0.0f, 8.0f, 0.0f), 0.f, -0.3f, noise*1000000, true, clouds);
			break;
		case 6:
			hf.addHf(new Egypt());
			p.addPhoto(t, Name, 5, new PVector((float)time, 0.27216554f,
					0.68041384f),
					new PVector(6.9793053f, 8.736299f, 11.575281f), 4.284375f,
					-0.025000066f, noise*1000000, true, clouds);
			break;
		case 7:
			hf.addHf(new Wavey());
			p.addPhoto(t, Name, 6, new PVector((float)time, 0.5f, 1),
					new PVector(0.0f, 8.0f, 0.0f), 0.f, -0.3f, noise*1000000, true,
					clouds);
			break;
		case 8:
			hf.addHf(new SimplePlane());
			p.addPhoto(t, Name, 7, new PVector((float)time, 0.1f, 1), new PVector(
					0.0f, 8.0f, 0.0f), 0.9f, 0.0f, noise*1000000, false, clouds);
			break;
		default:
			break;
		}
	}
}
