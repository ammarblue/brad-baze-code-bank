package reuze.pending;

import java.awt.geom.Point2D;

import processing.core.PApplet;

public class demoSimulatedAnnealing extends PApplet {
	TSP m;
	public void setup() {
	  size(400,400);
	  m=new TSP();
	  frameRate(4);
	}

	public void draw() {
	  background(100,100,100);
	  for (int i=0; i<m.p.length; i++) {
	     Point2D p2=m.p[i];
	     if (i==0) fill(0,255,0);
	     else if (i==m.p.length-1) fill(255,0,0);
	     else fill(0,0,0);
	     ellipse((float)p2.getX(),(float)p2.getY(),5,5);
	  }
	  for (int i=0; i<m.sim.minimalorder.length-1; i++) {
	    Point2D j=m.p[m.sim.minimalorder[i]],
	            k=m.p[m.sim.minimalorder[i+1]];
	    line((float)j.getX(),(float)j.getY(),(float)k.getX(),(float)k.getY());
	  }
	  m.sim.compute();
	}

	public void mousePressed() {
	  m.reset();
	}
	public class TSP implements OptimizationPairwiseI {
		  SimulatedAnnealing sim;
		  Point2D p[];

		  public TSP() {
		    p=new Point2D[150];
		    sim=new SimulatedAnnealing(this, p.length, 10, .99);
		    for (int i=0; i<p.length; i++) {
		      p[i]=new Point2D.Double(Math.random()*400, Math.random()*400);
		    }
		    //to return to origin just add first point at end of array
		    //p[p.length-1]=p[0];
		  }
		  public void reset() {
		    sim.reset(this, p.length, 10, .99);
		    for (int i=0; i<p.length; i++) {
		      p[i]=new Point2D.Double(Math.random()*400, Math.random()*400);
		    }
		    //to return to origin just add first point at end of array
		    //p[p.length-1]=p[0];
		  }
		  public double getCost(int i, int j) {
		    return p[i].distance(p[j]);
		  }

		  public double getTotalCost(int[] order) {
		    double cost=0;
		    for (int i=0; i<order.length-1; i++) cost+=getCost(order[i], order[i+1]);
		    return cost;
		  }
		}
}
