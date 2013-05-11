package reuze.demo;
import com.software.reuze.ga_Rectangle;
import com.software.reuze.gb_TriangleMesh;
import com.software.reuze.gb_Vector3;
import com.software.reuze.z_ToxiclibsSupport;

import processing.core.*;
public class demoToxiCharMesh  extends PApplet {
	PImage tex;
	float rotx = PI/4;
	float roty = PI/4;
    z_ToxiclibsSupport gfx;
    gb_TriangleMesh mesh;
	public void setup() 
	{
	  size(640, 360, P3D);
	  gfx = new z_ToxiclibsSupport(this);
	  textureMode(PConstants.NORMAL);
	  tex = loadImage("../data/font.png");
	  int n=25;
	  mesh = new gb_TriangleMesh("test",(n+1)*2,n*2);
	  float k=0;
	  for (int m=0; m<n; m++) {
		  mesh.addFace(new gb_Vector3(k,1,1), new gb_Vector3(k+1,1,1), new gb_Vector3(k+1,2,1));
		  mesh.addFace(new gb_Vector3(k+1,2,1), new gb_Vector3(k,2,1), new gb_Vector3(k,1,1));
		  k+=1.5;
	  }
	  mesh=gb_TriangleMesh.getCharQuad("test", new ga_Rectangle(1,1,n,2), n);
	}

	public void draw() 
	{
	  background(0xffffffff);
	  noStroke();
	  translate(width/2.0f, height/2.0f, -100);
	  rotateX(rotx);
	  rotateY(roty);
	  scale(20);
	  tint(0xff0000ff);
	  char chr[]="hello eveRYONE 45 @#$".toCharArray();
	  //normal(0,0,1);
	  //gfx.drawChars2(tex, 16, 16, 0.75f, chr);
	  gfx.texturedMesh(mesh, tex, false, 16, 16, chr);
	}

	public void mouseDragged() {
	  float rate = 0.01f;
	  rotx += (pmouseY-mouseY) * rate;
	  roty += (mouseX-pmouseX) * rate;
	}
}


