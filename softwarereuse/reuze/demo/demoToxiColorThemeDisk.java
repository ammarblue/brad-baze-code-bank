package reuze.demo;
import com.software.reuze.vc_ColorList;
import com.software.reuze.vc_ColorRange;
import com.software.reuze.vc_ColorTheme;
import com.software.reuze.vc_DistanceRGB;
import com.software.reuze.vc_a_CriteriaAccess;
import com.software.reuze.z_Colors;

import processing.core.PApplet;

public class demoToxiColorThemeDisk  extends PApplet {
	float   SWATCH_HEIGHT = 40;
	float   SWATCH_WIDTH = 5;
	int     SWATCH_GAP = 1;

	float   MAX_SIZE = 150;
	int     NUM_DISCS = 300;

	boolean showDiscs=true;

	public void setup() {
	  size(1024, 768);
	  noiseDetail(2);
	  smooth();
	  noLoop();
	}

	public void draw() {
	  // first define our new theme
	  vc_ColorTheme t = new vc_ColorTheme("test");

	  // add different color options, each with their own weight
	  t.addRange("bright red", 0.5f);
	  t.addRange("intense goldenrod", 0.25f);
	  t.addRange("warm saddlebrown", 0.15f);
	  t.addRange("fresh teal", 0.05f);
	  t.addRange("bright yellowgreen", 0.05f);

	  // now add another random hue which is using only bright shades
	  t.addRange(vc_ColorRange.BRIGHT, z_Colors.newRandom(), random(0.02f, 0.05f));

	  // use the TColor theme to create a list of 160 Colors
	  vc_ColorList list = t.getColors(160);

	  if (showDiscs) {
	    background(list.getLightest().toARGB());
	    discs(list);
	  } 
	  else {
	    background(0);
	    int yoff=32;
	    list.sortByDistance(false);
	    swatches(list, 32, yoff);
	    yoff+=SWATCH_HEIGHT+10;

	    list.sortByCriteria(vc_a_CriteriaAccess.LUMINANCE, false);
	    swatches(list, 32, yoff);
	    yoff+=SWATCH_HEIGHT+10;

	    list.sortByCriteria(vc_a_CriteriaAccess.BRIGHTNESS, false);
	    swatches(list, 32, yoff);
	    yoff+=SWATCH_HEIGHT+10;

	    list.sortByCriteria(vc_a_CriteriaAccess.SATURATION, false);
	    swatches(list, 32, yoff);
	    yoff+=SWATCH_HEIGHT+10;

	    list.sortByCriteria(vc_a_CriteriaAccess.HUE, false);
	    swatches(list, 32, yoff);
	    yoff+=SWATCH_HEIGHT+10;

	    list.sortByProximityTo(z_Colors.getColor("WHITE"), new vc_DistanceRGB(), false);
	    swatches(list, 32, yoff);
	    yoff+=SWATCH_HEIGHT+10;
	  }
	}

	public void keyPressed() {
	  if (key==' ') showDiscs=!showDiscs;
	  redraw();
	}

	void swatches(vc_ColorList sorted, int x, int y) {
	  noStroke();
	  for (z_Colors c : sorted) {
	    fill(c.toARGB());
	    rect(x, y, SWATCH_WIDTH, SWATCH_HEIGHT);
	    x += SWATCH_WIDTH + SWATCH_GAP;
	  }
	}

	void discs(vc_ColorList list) {
	  noStroke();
	  float numCols = list.size();
	  for (int i = 0; i < NUM_DISCS; i++) {
	    z_Colors c = list.get((int) random(numCols)).copy();
	    //c.alpha = random(0.5f, 1);
	    fill(c.toARGB());
	    float r = random(MAX_SIZE);
	    ellipse(random(width), random(height), r, r);
	  }
	}
}