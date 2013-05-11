package reuze.demo;
import com.software.reuze.gb_Terrain;
import com.software.reuze.gb_TriangleMesh;
import com.software.reuze.z_ToxiclibsSupport;

import processing.core.*;
public class demoToxiTerrainBasic  extends PApplet {
	int DIM=20;
	float NOISE_SCALE=0.15f;

	gb_TriangleMesh mesh;
	z_ToxiclibsSupport gfx;

	public void setup() {
	  size(640, 480, P3D);
	  gb_Terrain terrain = new gb_Terrain(DIM, DIM, 16);
	  // populate elevation data
	  float[] el = new float[DIM*DIM];
	  noiseSeed(23);
	  for (int z = 0, i = 0; z < DIM; z++) {
	    for (int x = 0; x < DIM; x++) {
	      el[i++] = noise(x * NOISE_SCALE, z * NOISE_SCALE) * 100;
	    }
	  }
	  terrain.setElevation(el);
	  // create mesh
	  mesh = new gb_TriangleMesh();
	  terrain.toMesh(mesh,-10);
	  // attach drawing utils
	  gfx = new z_ToxiclibsSupport(this);
	}

	public void draw() {
	  background(0);
	  lights();
	  translate(width/2,height/2,0);
	  rotateX(mouseY*0.01f);
	  rotateY(mouseX*0.01f);
	  noStroke();
	  gfx.mesh(mesh);
	}
}