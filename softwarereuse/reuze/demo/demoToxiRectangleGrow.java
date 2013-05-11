package reuze.demo;
import java.util.ArrayList;
import java.util.List;

import com.software.reuze.ga_Rectangle;
import com.software.reuze.ga_Vector2;
import com.software.reuze.z_ToxiclibsSupport;

import processing.core.*;
public class demoToxiRectangleGrow  extends PApplet {
	List<ga_Vector2> points = new ArrayList<ga_Vector2>();
	ga_Rectangle bounds=new ga_Rectangle(200,200,0,0);

	z_ToxiclibsSupport gfx;

	public void setup() {
	  size(400,400);
	  smooth();
	  gfx=new z_ToxiclibsSupport(this);
	}

	public void draw() {
	  background(255);
	  noFill();
	  stroke(0);
	  gfx.rect(bounds);
	  fill(255,0,0);
	  noStroke();
	  for(ga_Vector2 p : points) {
	    gfx.circle(p,5);
	  }
	}

	public void mousePressed() {
	  ga_Vector2 p=new ga_Vector2(mouseX,mouseY);
	  points.add(p);
	  bounds.union(p);
	}
}