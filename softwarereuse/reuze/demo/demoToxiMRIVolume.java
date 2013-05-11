package reuze.demo;
import com.software.reuze.gb_IsoSurfaceHash;
import com.software.reuze.gb_Vector3;
import com.software.reuze.gb_VoxelSpace;
import com.software.reuze.gb_i_IsoSurface;
import com.software.reuze.gb_i_Mesh;
import com.software.reuze.z_ToxiclibsSupport;

import processing.core.PApplet;

public class demoToxiMRIVolume  extends PApplet {
	int DIM=64;
	float ISO_THRESHOLD = 0.1f;
	gb_Vector3 SCALE=new gb_Vector3(DIM,DIM,DIM).mul(8);

	gb_i_IsoSurface surface;
	gb_i_Mesh mesh;
	z_ToxiclibsSupport gfx;

	boolean isWireframe=false;

	public void setup() {
	  size(1024,768,P3D);
	  //hint(ENABLE_OPENGL_4X_SMOOTH);
	  gfx=new z_ToxiclibsSupport(this);
	  strokeWeight(0.5f);
	  // convert MRI scan data into floats
	  // MRI data is 256 x 256 x 256 voxels @ 8bit/voxel
	  byte[] mriData=loadBytes("../data/bonsai.raw.gz");
	  // scale factor to normalize 8bit to the 0.0 - 1.0 interval
	  float mriNormalize=1/255.0f;
	  // setup lower resolution grid for IsoSurface
	  gb_VoxelSpace volume=new gb_VoxelSpace(SCALE,DIM,DIM,DIM);
	  float[] cloud=volume.getData();
	  int stride=256/DIM;
	  for(int z=0,idx=0; z<256; z+=stride) {
	    for(int y=0; y<256; y+=stride) {
	      int sliceIdx=y*256+z*65536;
	      for(int x=0; x<256; x+=stride) {
	        byte b=mriData[x+sliceIdx];
	        cloud[idx++]=(int)(b<0 ? 256+b : b)*mriNormalize;
	      }
	    }
	  }
	  long t0=System.nanoTime();
	  // create IsoSurface and compute surface mesh for the given iso threshold value
	  surface=new gb_IsoSurfaceHash(volume,0.15f);
	  mesh=surface.computeSurfaceMesh(null,ISO_THRESHOLD);
	  float timeTaken=(System.nanoTime()-t0)*1e-6f;
	  println(timeTaken+"ms to compute "+mesh.getNumFaces()+" faces");
	}

	public void draw() {
	  background(128);
	  translate(width/2,height/2,0);
	  rotateX(mouseY*0.01f);
	  rotateY(mouseX*0.01f);
	  ambientLight(48,48,48);
	  lightSpecular(230,230,230);
	  directionalLight(255,255,255,0,-0.5f,-1f);
	  specular(255,255,255);
	  shininess(16.0f);
	  if (isWireframe) {
	    stroke(255);
	    noFill();
	  } 
	  else {
	    noStroke();
	    fill(255);
	  }
	  gfx.mesh(mesh);
	}

	void normal(gb_Vector3 v) {
	  normal(v.x,v.y,v.z);
	}

	void vertex(gb_Vector3 v) {
	  vertex(v.x,v.y,v.z);
	}

	public void mousePressed() {
	  isWireframe=!isWireframe;
	}
}