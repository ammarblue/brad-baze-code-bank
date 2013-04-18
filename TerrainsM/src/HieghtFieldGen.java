import java.util.ArrayList;

public class HieghtFieldGen {
	ArrayList<HieghtField> hf = new ArrayList();
	terrainmarch t;

	HieghtFieldGen() {
	}

	HieghtFieldGen(terrainmarch t) {
		this.t = t;
	}

	void addHf(HieghtField in) {
		in.addTerrainmarch(t);
		hf.add(in);
	}

	HieghtField[] toArray() {
		return hf.toArray(new HieghtField[hf.size()]);
	}
}
