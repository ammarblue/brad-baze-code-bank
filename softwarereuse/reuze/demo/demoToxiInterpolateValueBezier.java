package reuze.demo;
import com.software.reuze.m_InterpolateValueBezier;

import processing.core.PApplet;

public class demoToxiInterpolateValueBezier  extends PApplet {
	//InterpolateStrategy tween=new SigmoidInterpolation(1.2);
	m_InterpolateValueBezier tween=new m_InterpolateValueBezier(3,-3);

	public void setup() {
	  size(400,400);
	}

	public void draw() {
	  tween.set("c1",sin(frameCount*0.05f)*0.5f+2f);
	  tween.set("c2",-(sin(frameCount*0.03f)*0.5f+2f));
	  background(0,128,255);
	  noStroke();
	  fill(255,100);
	  // pythagorean: c^2=a^2+b^2
	  float maxDist=sqrt(sq(width/2-0)+sq(height/2));
	  for(int y=0; y<=height; y+=20) {
	    for(int x=0; x<=width; x+=20) {
	      float d=min(dist(width/2,height/2,x,y),maxDist)/maxDist;
	      float r=tween.interpolate(1,16,d);
	      ellipse(x,y,r,r);
	    }
	  }
	  stroke(255,255,0);
	  for(int x=0; x<width; x++) {
	    point(x,tween.interpolate(0,height,(float)x/width));
	  }
	}
}