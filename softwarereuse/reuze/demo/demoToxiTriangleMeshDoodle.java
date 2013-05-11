package reuze.demo;
import java.util.ArrayList;
import java.util.Iterator;

import com.software.reuze.ga_Vector2;
import com.software.reuze.gb_TriangleFace;
import com.software.reuze.gb_TriangleMesh;
import com.software.reuze.gb_Vector3;

import processing.core.PApplet;

public class demoToxiTriangleMeshDoodle  extends PApplet {
	gb_TriangleMesh mesh=new gb_TriangleMesh("doodle");
	gb_Vector3 prev=new gb_Vector3();
	gb_Vector3 p=new gb_Vector3();
	gb_Vector3 q=new gb_Vector3();
    ArrayList<Integer> colors=new ArrayList<Integer>();
	ga_Vector2 rotation=new ga_Vector2();
	float weight=0;

	public void setup() {
	  size(600,600,P3D);
	}

	public void draw() {
	  background(0);
	  lights();
	  translate(width/2,height/2,0);
	  rotateX(rotation.x);
	  rotateY(rotation.y);
	  noStroke();
	  beginShape(TRIANGLES);
	  // iterate over all faces/triangles of the mesh
	  int j=0;
	  for(Iterator i=mesh.faces.iterator(); i.hasNext();) {
	    gb_TriangleFace f=(gb_TriangleFace)i.next();
	    // create vertices for each corner point
	    fill(colors.get(j++));
	    vertex(f.a);
	    vertex(f.b);
	    vertex(f.c);
	  }
	  endShape();
	  // udpate rotation
	  rotation.add(0.014f,0.0237f);
	}

	void vertex(gb_Vector3 v) {
	  vertex(v.x,v.y,v.z);
	}
	public void mouseMoved() {
	  // get 3D rotated mouse position
	  gb_Vector3 pos=new gb_Vector3(mouseX-width/2,mouseY-height/2,0);
	  pos.rotateX(rotation.x);
	  pos.rotateY(rotation.y);
	  // use distance to previous point as target stroke weight
	  weight+=(sqrt(pos.dst(prev))*2-weight)*0.1;
	  // define offset points for the triangle strip
	  gb_Vector3 a=pos.cpy().add(0,0,weight);
	  gb_Vector3 b=pos.cpy().add(0,0,-weight);
	  // add 2 faces to the mesh
	  colors.add(0xff000000^(int)random(0xffffff));
	  colors.add(0xff000000^(int)random(0xffffff));
	  mesh.addFace(p,b,q);
	  mesh.addFace(p,a,b);
	  // store current points for next iteration
	  prev=pos;
	  p=a;
	  q=b;
	}

	public void keyPressed() {
	  if (key!='s') {
	    mesh.clear();
	  }
	}
}