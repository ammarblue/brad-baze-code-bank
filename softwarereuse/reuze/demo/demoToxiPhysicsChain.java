package reuze.demo;
import com.software.reuze.ga_Rectangle;
import com.software.reuze.ga_Vector2;
import com.software.reuze.pma_BehaviorForceGravity;
import com.software.reuze.pma_ParticleVerlet;
import com.software.reuze.pma_PhysicsVerlet;
import com.software.reuze.pma_Spring;

import processing.core.PApplet;
import processing.core.PVector;

	/**
	 * <p>A soft pendulum (series of connected springs)<br/>
	 * <a href="http://www.shiffman.net/teaching/nature/toxiclibs/">The Nature of Code</a><br/>
	 * Spring 2010</p>
	 */

	/* 
	 * Copyright (c) 2010 Daniel Schiffmann
	 * 
	 * This demo & library is free software; you can redistribute it and/or
	 * modify it under the terms of the GNU Lesser General Public
	 * License as published by the Free Software Foundation; either
	 * version 2.1 of the License, or (at your option) any later version.
	 * 
	 * http://creativecommons.org/licenses/LGPL/2.1/
	 * 
	 * This library is distributed in the hope that it will be useful,
	 * but WITHOUT ANY WARRANTY; without even the implied warranty of
	 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
	 * Lesser General Public License for more details.
	 * 
	 * You should have received a copy of the GNU Lesser General Public
	 * License along with this library; if not, write to the Free Software
	 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
	 */
public class demoToxiPhysicsChain extends PApplet {


	// Reference to physics "world" (2D)
	pma_PhysicsVerlet physics;

	// Our "Chain" object
	Chain chain;

	public void setup() {
	  size(400,300);
	  smooth();

	  // Initialize the physics world
	  physics=new pma_PhysicsVerlet();
	  physics.addBehavior(new pma_BehaviorForceGravity(new ga_Vector2(0,0.1)));
	  physics.setWorldBounds(new ga_Rectangle(0,0,width,height));

	  // Initialize the chain
	  chain = new Chain(200,20,12,0.2f);
	}

	public void draw() {
	  background(255);

	  // Update physics
	  physics.update();
	  // Update chain's tail according to mouse location 
	  chain.updateTail(mouseX,mouseY);
	  // Display chain
	  chain.display();
	}

	public void mousePressed() {
	  // Check to see if we're grabbing the chain
	  chain.contains(mouseX,mouseY);
	}

	public void mouseReleased() {
	  // Release the chain
	  chain.release();
	}
	
	// The Nature of Code
	// <http://www.shiffman.net/teaching/nature>
	// Spring 2010
	// Toxiclibs example

	// A soft pendulum (series of connected springs)

	class Chain {

	  // Chain properties
	  float totalLength;  // How long
	  int numPoints;      // How many points
	  float strength;     // Strength of springs
	  float radius;       // Radius of ball at tail

	  // Let's keep an extra reference to the tail particle
	  // This is just the last particle in the ArrayList
	  pma_ParticleVerlet tail;

	  // Some variables for mouse dragging
	  PVector offset = new PVector();
	  boolean dragged = false;

	  // Chain constructor
	  Chain(float l, int n, float r, float s) {

	    totalLength = l;
	    numPoints = n;
	    radius = r;
	    strength = s;

	    float len = totalLength / numPoints;

	    // Here is the real work, go through and add particles to the chain itself
	    for(int i=0; i < numPoints; i++) {
	      // Make a new particle with an initial starting location
	      pma_ParticleVerlet particle=new pma_ParticleVerlet(width/2,i*len);

	      // Redundancy, we put the particles both in physics and in our own ArrayList
	      physics.addParticle(particle);

	      // Connect the particles with a Spring (except for the head)
	      if (i>0) {
	        pma_ParticleVerlet previous = physics.particles.get(i-1);
	        pma_Spring spring=new pma_Spring(particle,previous,len,strength);
	        // Add the spring to the physics world
	        physics.addSpring(spring);
	      }
	    }

	    // Keep the top fixed
	    pma_ParticleVerlet head=physics.particles.get(0);
	    head.lock();

	    // Store reference to the tail
	    tail = physics.particles.get(numPoints-1);
	  }

	  // Check if a point is within the ball at the end of the chain
	  // If so, set dragged = true;
	  void contains(int x, int y) {
	    float d = dist(x,y,tail.x,tail.y);
	    if (d < radius) {
	      offset.x = tail.x - x;
	      offset.y = tail.y - y;
	      tail.lock();
	      dragged = true;
	    }
	  }

	  // Release the ball
	  void release() {
	    tail.unlock();
	    dragged = false;
	  }

	  // Update tail location if being dragged
	  void updateTail(int x, int y) {
	    if (dragged) {
	      tail.set(x+offset.x,y+offset.y);
	    }
	  }

	  // Draw the chain
	  void display() {
	    // Draw line connecting all points
	    for(int i=0; i < physics.particles.size()-1; i++) {
	      pma_ParticleVerlet p1 = physics.particles.get(i);
	      pma_ParticleVerlet p2 = physics.particles.get(i+1);
	      stroke(0);
	      line(p1.x,p1.y,p2.x,p2.y);
	    }

	    // Draw a ball at the tail
	    stroke(0);
	    fill(175);
	    ellipse(tail.x,tail.y,radius*2,radius*2);
	  }
	}

}
