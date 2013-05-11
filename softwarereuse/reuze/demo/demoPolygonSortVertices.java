package reuze.demo;
import java.util.ArrayList;

import com.software.reuze.ga_Vector2;

import processing.core.PApplet;

public class demoPolygonSortVertices  extends PApplet {
	// Daniel Shiffman
	// Hanukkah 2011
	// 8 nights of Processing examples
	// http://www.shiffman.net


	// A Polygon object
	Poly p;

	public void setup() {
	  size(640, 360);
	  smooth();
	  // An empty one
	  p = new Poly();
	}

	public void draw() {
	  background(50);
	  // Draw the polygon
	  p.display();
	  fill(255);
	}

	public void keyPressed() {
	  // Clear it when you press the mouse
	  if (key == ' ') {
	    p.clear();
	  } 
	  // If you want to see the polygon unsorted, comment
	  // out the automatic sortVertices() in the class
	  else if (key == 's') {
	    p.sortVertices();
	  }
	}

	// Add a vertex!
	public void mousePressed() {
	  ga_Vector2 mouse = new ga_Vector2(mouseX, mouseY);
	  p.addVertex(mouse);
	}
	class Poly {
		  // A list of vertices
		  ArrayList<ga_Vector2> vertices;
		  // The center
		  ga_Vector2 centroid;

		  Poly() {
		    // Empty at first
		    vertices = new ArrayList<ga_Vector2>();
		    centroid = new ga_Vector2();
		  }

		  // We can clear the whole thing if necessary
		  void clear() {
		    vertices.clear();
		  }


		  // Add a new vertex
		  void addVertex(ga_Vector2 newVertex) {
		    vertices.add(newVertex);
		    // Whenever we have a new vertex
		    // We have to recalculate the center
		    // and re-sort the vertices
		    updateCentroid();
		    // Comment out the sorting if you want to see it drawn incorrectly
		    //sortVertices();
		  }

		  // The center is the average location of all vertices
		  void updateCentroid() {
		    centroid = new ga_Vector2();
		    for (ga_Vector2 v : vertices) {
		      centroid.add(v);
		    } 
		    centroid.mul(1f/vertices.size());
		  }


		  // Sorting the ArrayList
		  void sortVertices() {

		    // This is something like a selection sort
		    // Here, instead of sorting within the ArrayList
		    // We'll just build a new one sorted
		    ArrayList<ga_Vector2> newVertices = new ArrayList<ga_Vector2>();

		    // As long as it's not empty
		    while (!vertices.isEmpty ()) {
		      // Let's find the one with the highest angle
		      float biggestAngle = 0;
		      ga_Vector2 biggestVertex = null;
		      // Look through all of them
		      for (ga_Vector2 v : vertices) {
		        // Make a vector that points from center
		        ga_Vector2 dir = v.tmp().sub(centroid);
		        // What is it's heading
		        // The heading function will give us values between -PI and PI
		        // easier to sort if we have from 0 to TWO_PI
		        float a = dir.heading() + PI;
		        // Did we find it
		        if (a > biggestAngle) {
		          biggestAngle = a;
		          biggestVertex = v;
		        }
		      }

		      // Put the one we found in the new arraylist
		      newVertices.add(biggestVertex);
		      // Delete it so that the next biggest one 
		      // will be found the next time
		      vertices.remove(biggestVertex);
		    }
		    // We've got a new ArrayList
		    vertices = newVertices;
		  }

		  // Draw everything!
		  void display() {
		    
		    // First draw the polygon
		    stroke(255);
		    fill(255, 127);
		    beginShape();
		    for (ga_Vector2 v : vertices) {
		      vertex(v.x, v.y);
		    } 
		    endShape(CLOSE);
		    
		    
		    // Then we'll draw some addition information
		    // at each vertex to show the sorting
		    for (int i = 0; i < vertices.size(); i++) {
		      
		      // This is overkill, but we want the numbers to
		      // appear outside the polygon so we extend a vector
		      // from the center
		      ga_Vector2 v = vertices.get(i);      
		      ga_Vector2 dir = v.tmp().sub(centroid);
		      dir.nor();
		      dir.mul(12);
		      // Number the vertices
		      fill(255);
		      stroke(255);
		      ellipse(v.x, v.y, 4, 4);
		      //textAlign(CENTER);
		      //text(i, v.x+dir.x, v.y+dir.y+6);
		    } 

		   
		    // Once we have two vertices draw the center
		    if (vertices.size() > 1  ) {
		      fill(255);
		      ellipse(centroid.x, centroid.y, 8, 8);
		    }
		  }
		}
}