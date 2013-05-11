package reuze.demo;
import java.util.ArrayList;

import com.software.reuze.ga_Polygon;
import com.software.reuze.ga_Vector2;
import com.software.reuze.z_ToxiclibsSupport;

import processing.core.*;
//import processing.opengl.*;
public class demoToxiPolygonPicking  extends PApplet {

	ArrayList <ga_Polygon> polygons = new ArrayList <ga_Polygon> ();
	ArrayList <ga_Vector2> points = new ArrayList <ga_Vector2> ();
	int draggedPolygon = -1;
	z_ToxiclibsSupport gfx;
	boolean onPolygon;
	ga_Vector2 mouse;

	public void setup() {
	  size(1280,720);
	  gfx = new z_ToxiclibsSupport(this);
	  noStroke();
	  smooth();
	}

	public void draw() {
	  background(255);
	  mouse = new ga_Vector2(mouseX,mouseY);

	  // (re)set onPolygon to false
	  onPolygon = false;

	  // draw all the polygons
	  for (ga_Polygon p : polygons) {
	    if (p.contains(mouse)) {
	      // if the mouse is over a polygon...
	      // set onPolygon to true and color it red
	      onPolygon = true;
	      fill(255,0,0);
	    } else {
	      fill(0);
	    }
	    gfx.polygon2D(p);
	  }

	  // draw all the points
	  fill(0);
	  for (ga_Vector2 p : points) {
	    ellipse(p.x,p.y,5,5);
	  }
	}

	public void mousePressed() {
	  // if the mouse is NOT on a polygon
	  if (!onPolygon) {
	    // add a point at mouseX,mouseY
	    points.add(mouse);
	    // if the right mouse button is pressed
	    // and there are more than 2 points
	    if (mouseButton == RIGHT && points.size() > 2) {
	      // create a polygon from the points
	      polygons.add(new ga_Polygon(points));
	      // clear the points
	      points.clear();
	    }
	  }
	}

	public void mouseDragged() {
	  // if no polygon is selected
	  if (draggedPolygon == -1) {
	    // check if the mouse is on a polygon
	    for (int i=0; i<polygons.size(); i++) {
	      if (polygons.get(i).contains(mouse)) {
	        // if so, set this to be the selected polygon
	        draggedPolygon = i;
	      }
	    }
	  // if a polygon is selected
	  } else {
	    // set change amount to the movement of the mouse
		  ga_Vector2 change = new ga_Vector2(mouseX-pmouseX,mouseY-pmouseY);
	    // get the selected polygon
	    ga_Polygon p = polygons.get(draggedPolygon);
	    // add the change to all of it's individual points
	    for (ga_Vector2 v : p.getPoints()) {
	      v.add(change);
	    }
	  }
	}

	public void mouseReleased() {
	  // on mouse release reset to 'no polygon selected'
	  draggedPolygon = -1;
	}

	public void keyPressed() {
	  // clear all points and polygons
	  if (key == ' ' && !mousePressed) {
	    points.clear();
	    polygons.clear();
	  }
	  // delete the polygon under the mouse
	  if (key == 'd' && !mousePressed) {
	    for (int i=polygons.size()-1; i>=0; i--) {
	      if (polygons.get(i).contains(mouse)) {
	        polygons.remove(i);
	      }
	    }
	  }
	  // remove the last point (if points > 0)
	  if (key == 'x') {
	    if (points.size() > 0) {
	      points.remove(points.size()-1);
	    }
	  }
	}
}