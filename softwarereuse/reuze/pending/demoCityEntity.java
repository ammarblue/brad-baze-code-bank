package reuze.pending;

import com.software.reuze.dag_GraphFloatingEdges;
import com.software.reuze.dag_GraphNode;
import com.software.reuze.dg_GameXMLInput;
import com.software.reuze.dg_i_SteeringConstants;
import com.software.reuze.ga_Vector2D;
import com.software.reuze.pt_StopWatch;
import com.software.reuze.z_Processing;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PShape;
public class demoCityEntity extends PApplet {
	dag_GraphFloatingEdges routes;
	dag_GraphNode dest = null;
	pt_StopWatch watch;
	dg_GameXMLInput gmxml;
	Object background;

	public void setup() {
	  z_Processing app=new z_Processing(this);
	  gmxml=new dg_GameXMLInput();
	  gmxml.loadXML(app, "../data/game.xml", gmxml);
	  size(gmxml.width, gmxml.height);
	  if (gmxml.cursor>=0) cursor(gmxml.cursor);
	  if (background!=null) {
		  background=app.loadImage(gmxml.background);
		  if (background instanceof PImage) ((PImage)background).resize(width,height);
		  else ((PShape)background).scale(width, height);
	  }
	  // Get the navigation map
	  routes = gmxml.routes;
	  watch = new pt_StopWatch(); // last thing to be done in setup
	  PFont font;
	  font = createFont("SansSerif.plain", 18); 
	  textFont(font);
	}

	public void draw() {
	  float deltaTime = (float) watch.getElapsedTime();
	  if (background==null) background(gmxml.winColor);
	  else {
		  if (background instanceof PImage) image((PImage)background,0,0);
		  else shape((PShape)background,0,0);
	  }
	  showNodes();
	  gmxml.world.update(deltaTime);
	  if (!gmxml.patrol.getSB().isBehaviourOn(dg_i_SteeringConstants.PATH)) //sets velocity to zero on arrival
	    gmxml.patrol.setVelocity(ga_Vector2D.ZERO);
	  gmxml.world.draw();
	  text(""+(int)gmxml.world.time(),30,60);
	}

	public void mouseMoved() {
	  //dest = routes.getNodeNear(mouseX, mouseY, 0, 10);
	}

	public void mouseClicked() {
	    gmxml.patrol.getSB().addToPath(routes, routes.getNodeNear(mouseX, mouseY, 0, 10));
	}

	public void showNodes() {
	  pushStyle();
	  fill(200, 128);
	  noStroke();
	  int r;
	  ellipseMode(CENTER);
	  for (dag_GraphNode node : routes) {
	    if (dest == null || dest != node) {
	      r = 8;
	      fill(0, 200, 0);
	    }
	    else {
	      r = 24;
	      fill(0, 128, 0);
	    }
	    ellipse(node.xf(), node.yf(), r, r);
	  }
	  popStyle();
	}


}
