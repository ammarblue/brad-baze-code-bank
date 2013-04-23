import processing.core.PVector;

public class Test {
	PhotoGen p;
	terrainmarch t;
	HieghtFieldGen hf;
	demoXML tx;

	Test() {
	}

	Test(PhotoGen p, terrainmarch t, HieghtFieldGen hf) {
		this.p = p;
		this.t = t;
		this.hf = hf;
		tx = new demoXML(this);
	}

	void drive() {
			hf.addHf(new CityScape());
			p.addPhoto(t, "City", 2, new PVector(1.f,
				  0.5f, 1), new PVector( 0.0f, 2.0f, 0.0f), 0.5f,
				 -0.3f, 1789235, false, false);
			t.state=true;
	}
}
