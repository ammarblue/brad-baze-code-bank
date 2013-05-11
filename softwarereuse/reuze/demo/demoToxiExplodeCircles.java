package reuze.demo;
import java.util.ArrayList;

import com.software.reuze.ga_Circle;
import com.software.reuze.ga_Polygon;
import com.software.reuze.ga_PolygonClipperSutherlandHodgeman;
import com.software.reuze.ga_Rectangle;
import com.software.reuze.ga_Vector2;
import com.software.reuze.ga_Voronoi;
import com.software.reuze.ga_i_PolygonClipper;
import com.software.reuze.m_RangeFloat;
import com.software.reuze.m_RangeFloatBiased;
import com.software.reuze.pma_BehaviorAttraction;
import com.software.reuze.pma_ParticleVerlet;
import com.software.reuze.pma_PhysicsVerlet;
import com.software.reuze.z_ToxiclibsSupport;

import processing.core.PApplet;

public class demoToxiExplodeCircles  extends PApplet {

	ArrayList <BreakCircle> circles = new ArrayList <BreakCircle> ();
	pma_PhysicsVerlet physics;
	z_ToxiclibsSupport gfx;
	m_RangeFloat radius;
	ga_Vector2 origin, mouse;

	int maxCircles = 90; // maximum amount of circles on the screen
	int numPoints = 50;  // number of voronoi points / segments
	int minSpeed = 2;    // minimum speed of a voronoi segment
	int maxSpeed = 14;   // maximum speed of a voronoi segment

	public void setup() {
	  size(1280,720);
	  smooth();
	  noStroke();
	  gfx = new z_ToxiclibsSupport(this);
	  physics = new pma_PhysicsVerlet();
	  physics.setDrag(0.05f);
	  physics.setWorldBounds(new ga_Rectangle(0,0,width,height));
	  radius = new m_RangeFloatBiased(30, 100, 30, 0.6f);
	  origin = new ga_Vector2(width/2,height/2);
	  reset();
	}

	public void draw() {
	  removeAddCircles();
	  background(255,0,0);
	  physics.update();

	  mouse = new ga_Vector2(mouseX,mouseY);
	  for (BreakCircle bc : circles) {
	    bc.run();
	  }
	}

	void removeAddCircles() {
	  for (int i=circles.size()-1; i>=0; i--) {
	    // if a circle is invisible, remove it...
	    if (circles.get(i).transparency < 0) {
	      circles.remove(i);
	      // and add two new circles (if there are less than maxCircles)
	      if (circles.size() < maxCircles) {
	        circles.add(new BreakCircle(origin,radius.pickRandom()));
	        circles.add(new BreakCircle(origin,radius.pickRandom()));
	      }
	    }
	  }
	}

	public void keyPressed() {
	  if (key == ' ') { reset(); }
	}

	public void reset() {
	  // remove all physics elements
	  for (BreakCircle bc : circles) {
	    physics.removeParticle(bc.vp);
	    physics.removeBehavior(bc.abh);
	  }
	  // remove all circles
	  circles.clear();
	  // add one circle of radius 200 at the origin
	  circles.add(new BreakCircle(origin,200));
	}
	class BreakCircle {
		  ArrayList <ga_Polygon> polygons = new ArrayList <ga_Polygon> ();
		  ga_Voronoi voronoi;
		  m_RangeFloat xpos, ypos;
		  ga_i_PolygonClipper clip;
		  float[] moveSpeeds;
		  ga_Vector2 pos, impact;
		  float radius;
		  int transparency;
		  int start;
		  pma_ParticleVerlet vp;
		  pma_BehaviorAttraction abh;
		  boolean broken;

		  BreakCircle(ga_Vector2 pos, float radius) {
		    this.pos = pos;
		    this.radius = radius;
		    vp = new pma_ParticleVerlet(pos);
		    abh = new pma_BehaviorAttraction(vp, radius*2.5f + max(0,50-radius), -1.2f, 0.01f);
		    physics.addParticle(vp);
		    physics.addBehavior(abh);
		  }

		  void run() {
		    // for regular (not broken) circles
		    if (!broken) {
		      moveVerlet();
		      displayVerlet();
		      checkBreak();
		    // if the circle is broken
		    } else {
		      moveBreak();
		      displayBreak();
		    }
		  }

		  // set position based on the particle in the physics system
		  void moveVerlet() {
		    pos = vp;
		  }

		  // display circle
		  void displayVerlet() {
		    fill(255);
		    gfx.circle(pos,radius*2);
		  }

		  // if the mouse is pressed on a circle, it will be broken
		  void checkBreak() {
		    if (ga_Circle.contains(pos,radius,mouse) && mousePressed) {
		      // remove particle + behavior in the physics system
		      physics.removeParticle(vp);
		      physics.removeBehavior(abh);
		      // point of impact is set to mouseX,mouseY
		      impact = mouse;
		      initiateBreak();
		    }
		  }

		  void initiateBreak() {
		    broken = true;
		    transparency = 255;
		    start = frameCount;
		    // create a voronoi shape
		    voronoi = new ga_Voronoi();
		    // set biased float ranges based on circle position, radius and point of impact
		    xpos = new m_RangeFloatBiased(pos.x-radius, pos.x+radius, impact.x, 0.333f);
		    ypos = new m_RangeFloatBiased(pos.y-radius, pos.y+radius, impact.y, 0.5f);
		    // set clipping based on circle position and radius
		    clip = new ga_PolygonClipperSutherlandHodgeman(new ga_Rectangle(pos.x-radius, pos.y-radius, radius*2, radius*2));
		    addPolygons();
		    addSpeeds();
		  }

		  void addPolygons() {
		    // add random points (biased towards point of impact) to the voronoi
		    for (int i=0; i<numPoints; i++) {
		      voronoi.addPoint(new ga_Vector2(xpos.pickRandom(), ypos.pickRandom()));
		    }
		    // generate polygons from voronoi segments
		    for (ga_Polygon poly : voronoi.getRegions()) {
		      // clip them based on the rectangular clipping
		      poly = clip.clipPolygon(poly);
		      for (ga_Vector2 v : poly.getPoints()) {
		        // if a point is outside the circle
		        if (!ga_Circle.contains(pos,radius,v)) {
		          // scale it's distance from the center to the radius
		          clipPoint(v);
		        }
		      }
		      polygons.add(new ga_Polygon(poly.getPoints()));
		    }
		  }

		  void addSpeeds() {
		    // generate random speeds for all polygons
		    moveSpeeds = new float[polygons.size()];
		    for (int i=0; i<moveSpeeds.length; i++) {
		      moveSpeeds[i] = random(minSpeed,maxSpeed);
		    }
		  }

		  // move polygons away from the point of impact at their respective speeds
		  void moveBreak() {
		    for (int i=0; i<polygons.size(); i++) {
		      ga_Polygon poly = polygons.get(i);
		      ga_Vector2 centroid = poly.getCentroid();
		      ga_Vector2 targetDir = centroid.tmp().sub(impact).nor();
		      targetDir.mul(moveSpeeds[i]);
		      for (ga_Vector2 v : poly.getPoints()) {
		        v.add(targetDir);
		      }
		    }
		  }

		  // draw the polygons
		  void displayBreak() {
		    // after 12 frames, start decreasing the transparency
		    if (frameCount-start > 12) { transparency -= 7; }
		    fill(255,transparency);
		    for (ga_Polygon poly : polygons) {
		      gfx.polygon2D(poly);
		    }
		  }

		  void clipPoint(ga_Vector2 v) {
		    v.sub(pos);
		    v.nor();
		    v.mul(radius);
		    v.add(pos);
		  }
		}
}