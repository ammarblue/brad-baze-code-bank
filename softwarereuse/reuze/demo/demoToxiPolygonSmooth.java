package reuze.demo;
import java.util.ArrayList;

import com.software.reuze.ga_Polygon;
import com.software.reuze.ga_Vector2;
import com.software.reuze.m_MathUtils;
import com.software.reuze.z_ToxiclibsSupport;

import processing.core.*;
//import processing.opengl.*;
public class demoToxiPolygonSmooth  extends PApplet {
	// number of vertices in each polygon
	int num=30;

	ArrayList<ColoredPolygon> polygons = new ArrayList<ColoredPolygon>();

	z_ToxiclibsSupport gfx;

	public void setup() {
	  size(680,382);
	  noStroke();
	  smooth();
	  gfx=new z_ToxiclibsSupport(this);
	}

	public void draw() {
	  background(255);
	  stroke(255,0,192);
	  // iterate over all polygon created so far
	  for(ColoredPolygon p : polygons) {
	    // apply vertex smoothing
	    p.smooth(0.01f,0.05f);
	    fill((p.col>>12)&0xff,(p.col>>10)&0xff,(p.col>>8)&0xff,(p.col>>14)&0xff);
	    gfx.polygon2D(p);
	    // and draw
	    /*for (int i=0; i<p.points.size(); i++) {
	    	Vector2 v=p.points.get(i);
	    	Vector2 v1;
	    	if (i==p.points.size()-1 ) v1=p.points.get(0);
	    	else v1=p.points.get(i+1);
	    	line(v.x,v.y, v1.x,v1.y);
	    }*/
	  }
	}

	// create a new polygon around the mouse position using a random radius for each vertex
	public void mousePressed() {
	  // pick a random bright color and set its alpha
	  int col=(int) (Math.random()*4000000);
	  // add randomized vertices
	  ColoredPolygon poly=new ColoredPolygon(col);
	  float radius=random(50,200);
	  for(int i=0; i<num; i++) {
	    poly.add(ga_Vector2.fromTheta((float)i/num*TWO_PI).mul(m_MathUtils.random(0.2f,1f)*radius).add(mouseX,mouseY));
	  }
	  poly.calcAll();
	  // add poly to list of polygons
	  polygons.add(poly);
	}

	// extend the standard Polygon2D class to include color information
	class ColoredPolygon extends ga_Polygon {
	  int col;
	  
	  public ColoredPolygon(int col) {
	    this.col=col;
	  }
	}
}