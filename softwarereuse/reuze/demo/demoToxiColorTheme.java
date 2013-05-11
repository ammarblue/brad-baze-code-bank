package reuze.demo;
import java.util.List;

import com.software.reuze.ga_SplineB;
import com.software.reuze.ga_Vector2;
import com.software.reuze.vc_ColorList;
import com.software.reuze.vc_ColorRange;
import com.software.reuze.vc_ColorTheme;
import com.software.reuze.z_Colors;

import processing.core.PApplet;

public class demoToxiColorTheme  extends PApplet {
	float   XRAD = 300;
	float   YRAD = 500;
	int     RES = 6;
	int     NUM_POINTS=6;

	public void setup() {
	  size(1024, 768);
	  noiseDetail(2);
	  smooth();
	  noLoop();
	}

	public void draw() {
	  noiseSeed(System.currentTimeMillis());
	  // first define our new theme
	  vc_ColorTheme t = new vc_ColorTheme("test");
	  // add different color options, each with their own weight
	  t.addRange("bright red",0.05f); t.addRange("white",0.75f);
	  t.addRange("soft ivory", 0.05f);
	  t.addRange("intense goldenrod", 0.05f);
	  t.addRange("warm saddlebrown", 0.05f);
	  t.addRange("fresh teal", 0.05f);
	  t.addRange("bright yellow", 0.05f);

	  // now add another random hue which is using only bright shades
	  z_Colors c=z_Colors.newRandom();
	  float f=random(0.02f, 0.05f);
	  t.addRange(vc_ColorRange.BRIGHT, c, f);

	  // use the Colortheme to create a list of 160 colors
	  vc_ColorList list = t.getColors(160);

	  background(list.getLightest().toARGB());
	  drawSpline(list);
	}

	public void keyPressed() {
	  redraw();
	}

	void drawSpline(vc_ColorList list) {
	  noStroke();
	  float numCols = list.size();
	  ga_Vector2[] points=new ga_Vector2[NUM_POINTS];
	  points[0]=new ga_Vector2(-XRAD,random(0.2f,0.9f)*height);
	  for(int i=1; i<points.length-1; i++) {
	    points[i]=new ga_Vector2(random(-1,1)*50+(float)i/points.length*width,random(0.25f,0.75f)*height);
	  }
	  points[points.length-1]=new ga_Vector2(width+XRAD,random(height));
	  ga_SplineB sp=new ga_SplineB(points);
	  List<ga_Vector2> vertices=sp.computeVertices(width/RES);
	  for(ga_Vector2 v: vertices) {
	    fill(list.get((int) random(numCols)).toARGB());
	    ellipse(v.x,v.y,noise(v.y*0.01f)*XRAD,noise(v.x*0.01f)*YRAD);
	  }
	}
}