package reuze.demo;
import java.util.ArrayList;
import java.util.Iterator;

import com.software.reuze.ga_SplineB;
import com.software.reuze.ga_Vector2;

import processing.core.*;
//import processing.opengl.*;
public class demoToxiBspline extends PApplet {
	int RES=32;

	public void setup() {
	  size(300,500);
	  smooth();
	  textFont(createFont("SansSerif",10));
	}

	public void draw() {
	  background(255);

	  float x=map(mouseX,0,width,80,150);
	  ga_SplineB s=new ga_SplineB();
	  s.add(new ga_Vector2(60,100));
	  s.add(new ga_Vector2(60,0));
	  s.add(new ga_Vector2(x,0));
	  s.add(new ga_Vector2(x,100));
	  s.add(new ga_Vector2(200,0));
	  s.add(new ga_Vector2(200,100));

	  translate(50,20);
	  stroke(255,0,0);
	  fill(255,0,0);
	  text("control points",-40,0);
	  noFill();
	  beginShape();
	  for(Iterator i=s.pointList.iterator(); i.hasNext();) {
		  ga_Vector2 v=(ga_Vector2)i.next();
	    vertex(v.x, v.y);
	  }
	  endShape();

	  translate(0,160);
	  fill(0);
	  text("tweened vertices",-40,0);
	  noFill();
	  int c=0;
	  for(Iterator i=s.computeVertices(RES).iterator(); i.hasNext();) {
		  ga_Vector2 p=(ga_Vector2)i.next();
	    if (0 == c % RES) stroke(255,0,0);
	    else stroke((c % RES)*(255f/RES));
	    ellipse(p.x,p.y,5,5);
	    c++;
	  }

	  translate(0,160);
	  stroke(0,0,255);
	  fill(0,0,255);
	  text("fixed interval",-40,0);
	  noFill();
	  for(Iterator i=s.getDecimatedVertices(20,false).iterator(); i.hasNext();) {
	    ga_Vector2 p=(ga_Vector2)i.next();
	    line(p.x-2,p.y,p.x+2,p.y);
	    line(p.x,p.y-2,p.x,p.y+2);
	  }
	}
}