package reuze.demo;
import com.software.reuze.ga_PlaneSelector;
import com.software.reuze.gb_AABB3;
import com.software.reuze.gb_Plane;
import com.software.reuze.gb_StrategySubdivisionDisplacementMidpoint;
import com.software.reuze.gb_StrategySubdivisionMidpoint;
import com.software.reuze.gb_Vector3;
import com.software.reuze.gb_WETriangleMesh;
import com.software.reuze.z_ToxiclibsSupport;

import processing.core.PApplet;

public class demoToxiCutPlanes  extends PApplet {
	z_ToxiclibsSupport gfx;
	gb_WETriangleMesh mesh;

	public void setup() {
	  size(680,382, P3D);
	  gfx = new z_ToxiclibsSupport(this);
	  mesh = (gb_WETriangleMesh) new gb_AABB3(100).toMesh(new gb_WETriangleMesh());
	  gb_StrategySubdivisionMidpoint sd = new gb_StrategySubdivisionMidpoint();
	  mesh.subdivide(sd, 0);
	  mesh.subdivide(sd, 0);
	  mesh.subdivide(sd, 0);
	  gb_StrategySubdivisionDisplacementMidpoint sd2 = new gb_StrategySubdivisionDisplacementMidpoint(new gb_Vector3(),-0.5f);
	  mesh.subdivide(sd2, 0);
	  //noLoop();
	}

	public void draw() {
	  background(0);
	  lights();
	  translate(width / 2, height / 2, 0);
	  rotateX((height / 2 - mouseY) * 0.01f);
	  rotateY((width / 2 - mouseX) * 0.01f);
	  gfx.origin(300);
	  noStroke();
	  gb_WETriangleMesh filteredMesh = new gb_WETriangleMesh().addMesh(mesh);
	  ga_PlaneSelector sel = new ga_PlaneSelector(filteredMesh, new gb_Plane(new gb_Vector3(), new gb_Vector3(0, 1, 0).rotateX(frameCount * 0.02f)), gb_Plane.PlaneSide.Front);
	  ga_PlaneSelector sel2 = new ga_PlaneSelector(filteredMesh, new gb_Plane(new gb_Vector3(), new gb_Vector3(1, 1, 0).rotateX(frameCount * 0.04f + HALF_PI)), gb_Plane.PlaneSide.Front);
	  sel.selectVertices();
	  sel2.selectVertices();
	  sel.addSelection(sel2);
	  filteredMesh.removeVertices(sel.getSelection());
	  fill(0, 255, 255);
	  gfx.mesh(filteredMesh, false);
	  fill(255,0,255);
	  //Plane p=new Plane();
	  //System.out.println(p);
	  //IMesh3D m=p.toMesh(300);
	  //for (Vector3Id v:m.getVertices()) System.out.println(v);
	  //gfx.plane(p, 300);
	  gfx.plane(sel.plane,300);
	  fill(255,255,0);
	  gfx.plane(sel2.plane,300);
	}
}