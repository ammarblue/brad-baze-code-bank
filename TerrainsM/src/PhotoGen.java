import java.util.ArrayList;

import processing.core.PVector;

public class PhotoGen {
	ArrayList<Photo> pics = new ArrayList<Photo>();
	int numPhotos;

	PhotoGen() {
	}

	PhotoGen(int numPhotos) {
		this.numPhotos = numPhotos;
	}

	void addPhoto(terrainmarch terrainmarch, String name, int scene,
			PVector sd, PVector _cpos, float _cangX, float _cangY, int noiseS,
			boolean post, boolean uc) {
		pics.add(new Photo(terrainmarch, name, scene, sd, _cpos, _cangX,
				_cangY, noiseS, post, uc));
	}

	void readFromFile(String fname) {
		FileRead fin = new FileRead(this);
		fin.Read();
	}

	void removePhoto(int index) {
		pics.remove(index);
	}

	Photo[] toArray() {
		return pics.toArray(new Photo[pics.size()]);
	}
}
