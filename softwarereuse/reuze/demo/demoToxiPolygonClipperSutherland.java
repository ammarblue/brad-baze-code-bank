package reuze.demo;
import com.software.reuze.ga_Circle;
import com.software.reuze.ga_Polygon;
import com.software.reuze.ga_PolygonClipperSutherlandHodgeman;
import com.software.reuze.ga_Rectangle;
import com.software.reuze.ga_Triangle2D;
import com.software.reuze.ga_Vector2;
import com.software.reuze.ga_Voronoi;
import com.software.reuze.ga_i_PolygonClipper;
import com.software.reuze.m_RangeFloat;
import com.software.reuze.m_RangeFloatBiased;
import com.software.reuze.z_ToxiclibsSupport;

import processing.core.*;
//import processing.opengl.*;
public class demoToxiPolygonClipperSutherland  extends PApplet {
	// ranges for x/y positions of points
	m_RangeFloat xpos, ypos;

	// helper class for rendering
	z_ToxiclibsSupport gfx;

	// empty voronoi mesh container
	ga_Voronoi voronoi = new ga_Voronoi();

	// optional polygon clipper
	ga_i_PolygonClipper clip;

	// switches
	boolean doShowPoints = true;
	boolean doShowDelaunay;
	boolean doShowHelp=true;
	boolean doClip;

	public void setup() {
	  size(600, 600);
	  smooth();
	  // focus x positions around horizontal center (w/ 33% standard deviation)
	  xpos=new m_RangeFloatBiased(0, width, width/2, 0.333f);
	  // focus y positions around bottom (w/ 50% standard deviation)
	  ypos=new m_RangeFloatBiased(0, height, height/2, 0.5f);
	  // setup clipper with centered octagon
	  ga_Polygon p=new ga_Circle(width*0.45f).toPolygon(8).translate(new ga_Vector2(width/2,height/2));
	  ga_Rectangle r=ga_Polygon.getBounds(p);
	  clip=new ga_PolygonClipperSutherlandHodgeman(r);
	  gfx = new z_ToxiclibsSupport(this);
	  textFont(createFont("SansSerif", 10));
	}

	public void draw() {
	  background(255);
	  stroke(0);
	  noFill();
	  // draw all voronoi polygons, clip them if needed...
	  for (ga_Polygon poly : voronoi.getRegions()) {
	    if (doClip) {
	      gfx.polygon2D(clip.clipPolygon(poly));
	    } 
	    else {
	      gfx.polygon2D(poly);
	    }
	  }
	  // draw delaunay triangulation
	  if (doShowDelaunay) {
	    stroke(0, 0, 255, 50);
	    beginShape(TRIANGLES);
	    for (ga_Triangle2D t : voronoi.getTriangles()) {
	      gfx.triangle(t, false);
	    }
	    endShape();
	  }
	  // draw original points added to voronoi
	  if (doShowPoints) {
	    fill(255, 0, 255);
	    noStroke();
	    for (ga_Vector2 c : voronoi.getSites()) {
	      ellipse(c.x, c.y, 5, 5);
	    }
	  }
	  if (doShowHelp) {
	    fill(255, 0, 0);
	    text("p: toggle points", 20, 20);
	    text("t: toggle triangles", 20, 40);
	    text("x: clear all", 20, 60);
	    text("r: add random", 20, 80);
	    text("c: toggle clipping", 20, 100);
	    text("h: toggle help display", 20, 120);
	    text("space: save frame", 20, 140);
	  }
	}

	public void keyPressed() {
	  switch(key) {
	  case 't':
	    doShowDelaunay = !doShowDelaunay;
	    break;
	  case 'x':
	    voronoi = new ga_Voronoi();
	    break;
	  case 'p':
	    doShowPoints = !doShowPoints;
	    break;
	  case 'c':
	    doClip=!doClip;
	    break;
	  case 'h':
	    doShowHelp=!doShowHelp;
	    break;
	  case 'r':
	    for (int i = 0; i < 10; i++) {
	      voronoi.addPoint(new ga_Vector2(xpos.pickRandom(), ypos.pickRandom()));
	    }
	    break;
	  }
	}

	public void mousePressed() {
	  voronoi.addPoint(new ga_Vector2(mouseX, mouseY));
	}
}