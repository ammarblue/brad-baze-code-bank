package reuze.demo;
import com.software.reuze.ga_Circle;
import com.software.reuze.ga_Line2D;
import com.software.reuze.ga_Vector2;
import com.software.reuze.z_ToxiclibsSupport;

import processing.core.*;
//import processing.opengl.*;
public class demoToxiPulley  extends PApplet {
	// define pulley wheels
	ga_Circle c1=new ga_Circle(140,190,50);
	ga_Circle c2=new ga_Circle(340,190,100);

	z_ToxiclibsSupport gfx;

	public void setup() {
	  size(680,382);
	  noFill();
	  gfx=new z_ToxiclibsSupport(this);
	  //noLoop();
	}

	public void draw() {
	  background(255);
	  // lock circle to mouse
	  c1.set(mouseX,mouseY);
	  // sort circles based on X position
	  ga_Circle[] wheels;
	  if (c1.position.x<c2.position.x) {
	    wheels=new ga_Circle[] { c1,c2 };
	  } 
	  else {
	    wheels=new ga_Circle[] { c2,c1 };
	  }
	  // length between circle centers
	  float len=c1.position.dst(c2.position);
	  // compute angle between circle centers
	  float theta=wheels[1].position.tmp().sub(wheels[0].position).heading();
	  // compute angle of tangent point
	  float phi=acos((wheels[0].getRadius()+wheels[1].getRadius())/len);
	  // compute tangent point on left wheel using polar coordinates
	  // adding theta as offset to take into account direction between wheel
	  ga_Vector2 c1A=pointOnCircle(wheels[0],phi+theta);
	  // tangent point on right circle is opposite (phi+PI)
	  ga_Vector2 c2A=pointOnCircle(wheels[1],phi+theta+PI);
	  // flip angle for vertically opposite tangent points
	  phi=TWO_PI-phi;
	  // compute 2nd set of tangent points
	  ga_Vector2 c1B=pointOnCircle(wheels[0],phi+theta);
	  ga_Vector2 c2B=pointOnCircle(wheels[1],phi+theta+PI);
	  // compute intersection point of ropes
	  ga_Line2D la=new ga_Line2D(c1A,c2A);
	  ga_Line2D lb=new ga_Line2D(c1B,c2B);
	  ga_Vector2 isec=la.intersectLine(lb).getPos();
	  if (isec!= null) {
	    gfx.circle(isec,5);
	    // draw rope
	    gfx.line(la);
	    gfx.line(lb);
	  }
	  // draw wheels
	  gfx.ellipse(c1);
	  gfx.ellipse(c2);
	}

	// computes a point at the given angle & circle
	ga_Vector2 pointOnCircle(ga_Circle c, float theta) {
	  return new ga_Vector2(c.getRadius(),theta).toCartesian().add(c.position);
	}
}