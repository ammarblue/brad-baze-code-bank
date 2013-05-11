package reuze.aifiles;

import com.software.reuze.dg_GameXMLInput;
import com.software.reuze.dg_i_SteeringConstants;
import com.software.reuze.ga_Vector2D;
import com.software.reuze.pt_StopWatch;
import com.software.reuze.z_Processing;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PShape;

public class aitest2 extends PApplet {
	pt_StopWatch watch;
	dg_GameXMLInput gmxml;
	Object background;
	public void setup() {
		z_Processing app=new z_Processing(this);
		  gmxml=new dg_GameXMLInput();
		  gmxml.loadXML(app, "../data/game3.xml", gmxml);
		  size(gmxml.width, gmxml.height);
		  if (gmxml.cursor>=0) cursor(gmxml.cursor);
		  if (background!=null) {
			  background=app.loadImage(gmxml.background);
			  if (background instanceof PImage) ((PImage)background).resize(width,height);
			  else ((PShape)background).scale(width, height);
		  }
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
		  gmxml.world.update(deltaTime);
		  gmxml.world.draw();
		  text(""+(int)gmxml.world.time(),30,60);
	}
}
