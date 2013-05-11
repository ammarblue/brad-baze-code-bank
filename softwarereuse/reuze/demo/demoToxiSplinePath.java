package reuze.demo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.software.reuze.ga_Circle;
import com.software.reuze.ga_Polygon;
import com.software.reuze.ga_Vector2;
import com.software.reuze.gb_Quaternion;
import com.software.reuze.gb_SplineB;
import com.software.reuze.gb_Vector3;
import com.software.reuze.m_Matrix4;
import com.software.reuze.z_ToxiclibsSupport;

import processing.core.*;
//import processing.opengl.*;
public class demoToxiSplinePath  extends PApplet {
	z_ToxiclibsSupport gfx;

	ga_Polygon profile=new ga_Circle(40).toPolygon(60);

	List<gb_Vector3> path;
	ArrayList<gb_Vector3> tubeVertices=new ArrayList<gb_Vector3>();

	int pathID=0;
	boolean doRenderPoints;

	public void setup() {
	  size(680,382,P3D);
	  gfx=new z_ToxiclibsSupport(this);
	  // compute spiral key points (every 45 degrees)
	  ArrayList points = new ArrayList();
	  for (float theta=-TWO_PI, r=100; theta<8*TWO_PI; theta+=QUARTER_PI) {
	    gb_Vector3 p=gb_Vector3.fromXYTheta(theta).mul(r).add(200,0,0).rotateY(theta/9);
	    points.add(p);
	  }
	  // use points to compute a spline and sample at regular interval
	  gb_SplineB s=new gb_SplineB(points);
	  s.computeVertices(10);
	  path=s.getDecimatedVertices(4);
	}

	public void draw() {
	  background(51);
	  lights();
	  translate(width / 2, height / 2, 0);
	  rotateX(mouseY * 0.01f);
	  rotateY(mouseX * 0.01f);
	  noStroke();
	  gfx.origin(300);
	  stroke(255,0,255);
	  gfx.lineStrip3D(path);
	  if (pathID<path.size()-1) {
	    // compute current curve direction
	    gb_Vector3 dir=path.get(pathID+1).tmp().sub(path.get(pathID)).nor();
	    // calculate alignment orientation for direction
	    // our profile shape is in XY plane (2D) and
	    // so its "forward" direction is the positive Z axis
	    gb_Quaternion alignment=gb_Quaternion.getAlignmentQuat(dir,gb_Vector3.Z);
	    // construct a matrix to move shape to current curve position
	    m_Matrix4 mat=new m_Matrix4().translate(path.get(pathID));
	    // then combine with alignment matrix
	    mat.mul(alignment.toMatrix4x4());
	    // then apply matrix to (copies of) all profile shape vertices
	    // and append them to global vertex list
	    for(ga_Vector2 p : profile.points) {
	      tubeVertices.add(mat.applyTo(gb_Vector3.to3DXY(p)));
	    }
	    pathID++;
	  }
	  stroke(255);
	  if (!doRenderPoints) {
	    gfx.points3D(tubeVertices);
	  } else {
	    gfx.lineStrip3D(tubeVertices);
	  }
	  
	  // draw coordinate system for current spline direction
	  int id=constrain(pathID-2,0,path.size()-2);
	  // current curve direction
	  gb_Vector3 dir=path.get(id+1).tmp().sub(path.get(id)).nor();
	  // compute rotation axis and angle
	  float[] axis=gb_Quaternion.getAlignmentQuat(dir,gb_Vector3.Z).toAxisAngle();
	  // move to curr/last curve point
	  gfx.translate(path.get(id+1));
	  // rotate around computed axis
	  rotate(axis[0],axis[1],axis[2],axis[3]);
	  // draw rotated coordinate system
	  gfx.origin(new gb_Vector3(),100);
	}

	public void keyPressed() {
	  if (key=='r') {
	    tubeVertices.clear();
	    pathID=0;
	  }
	  if (key=='l') {
	    doRenderPoints=!doRenderPoints;
	  }
	}
}