package reuze.demo;

import com.software.reuze.gb_IsoSurfaceHash;
import com.software.reuze.gb_MeshWEFilterStrategyLaplacianSmooth;
import com.software.reuze.gb_Vector3;
import com.software.reuze.gb_WETriangleMesh;
import com.software.reuze.gb_a_VoxelSpace;
import com.software.reuze.gb_i_IsoSurface;
import com.software.reuze.m_SinCosLUT;
import com.software.reuze.z_ToxiclibsSupport;

import processing.core.*;
//import processing.opengl.*;
public class demoToxiVolumeFunction  extends PApplet {
	int RES = 48;
	float ISO = 0.2f;
	float MAX_ISO=0.66f;

	gb_WETriangleMesh mesh;

	z_ToxiclibsSupport gfx;
	float currZoom = 1;
	boolean isWireframe;

	public void setup() {
	  size(1280,720, P3D);
	  gfx = new z_ToxiclibsSupport(this);
	  gb_a_VoxelSpace vol = new EvaluatingVolume(new gb_Vector3(400,400,400), RES, MAX_ISO);
	  gb_i_IsoSurface surface = new gb_IsoSurfaceHash(vol);
	  mesh = new gb_WETriangleMesh();
	  surface.computeSurfaceMesh(mesh, ISO);
	}

	public void draw() {
	  background(0);
	  translate(width / 2, height / 2, 0);
	  rotateX(mouseY * 0.01f);
	  rotateY(mouseX * 0.01f);
	  scale(currZoom);
	  if (isWireframe) {
	    noFill();
	    stroke(255);
	  } 
	  else {
	    fill(255);
	    noStroke();
	    lights();
	  }
	  gfx.mesh(mesh, true);
	}

	public void keyPressed() {
	  if (key == 'w') {
	    isWireframe = !isWireframe;
	  }
	  if (key == '-') {
	    currZoom -= 0.1;
	  }
	  if (key == '+') {
	    currZoom += 0.1;
	  }
	  if (key == 'l') {
	    new gb_MeshWEFilterStrategyLaplacianSmooth().filter(mesh, 1);
	  }
	}
	class EvaluatingVolume extends gb_a_VoxelSpace {

		  private final float FREQ = PI * 3.8f;
		  private float upperBound;
		  private m_SinCosLUT lut;
		  
		  public EvaluatingVolume(gb_Vector3 scale, int res, float upperBound) {
		    this(scale,res,res,res,upperBound);
		  }
		  
		  public EvaluatingVolume(gb_Vector3 scale, int resX, int resY, int resZ, float upperBound) {
		    super(scale, resX, resY, resZ);
		    this.upperBound = upperBound;
		    this.lut=new m_SinCosLUT();
		  }

		  public void clear() {
		    // nothing to do here
		  }
		  
		  public final float getVoxelAt(int i) {
		    return getVoxelAt(i % resX, (i % sliceRes) / resX, i / sliceRes);
		  }

		  public final float getVoxelAt(int x, int y, int z) {
		    float val = 0;
		    if (x > 0 && x < resX1 && y > 0 && y < resY1 && z > 0 && z < resZ1) {
		      float xx = (float) x / resX - 0.5f;
		      float yy = (float) y / resY - 0.5f;
		      float zz = (float) z / resZ - 0.5f;
		      val = lut.sin(xx * FREQ) + lut.cos(yy * FREQ) + lut.sin(zz * FREQ);
		      if (val > upperBound) {
		        val = 0;
		      }
		    }
		    return val;
		  }
		}
}