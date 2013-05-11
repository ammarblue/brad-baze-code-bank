package reuze.demo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.software.reuze.ga_SplineB;
import com.software.reuze.ga_Vector2;
import com.software.reuze.m_InterpolateValueZoomLens;
import com.software.reuze.vc_ColorNames;
import com.software.reuze.z_Colors;

import processing.core.PApplet;

public class demoToxiColorPalette  extends PApplet {
//  by Amnon Owed (05/05/2011)
//  minor refactorings by Karsten Schmidt (06/05/2011)
ArrayList <String> names;
ArrayList <ColorWorm> worms = new ArrayList <ColorWorm> ();

m_InterpolateValueZoomLens zoomLens = new m_InterpolateValueZoomLens();

boolean showColorPalette = true;
int selectedColorID;

// screen center
ga_Vector2 center;

// background color (readonly colors can't be modified)
z_Colors bg;

public void setup() {
  size(1280, 720, P3D);
  center = new ga_Vector2(width/2, height/2);
  // create a list of all the Toxiclibs NamedColors
  names = new ArrayList(vc_ColorNames.colors());
  // sort it alphabetically
  Collections.sort(names);
  textFont(createFont("SansSerif", 28));
  // set zoom lens to a dilating characteristic
  // setting the first parameter to a negative value will create a bundling effect
  zoomLens.setLensStrength(0.45f, 1);
  // set the background color to deepskyblue
  bg = z_Colors.getColor("deepskyblue");
}

public void draw() {
  // convert the bg color into ARGB color format (32bit packed integer)
  background(bg.toARGB());

  // run through all the worms (backwards cause we are also removing some from the list)
  for (Iterator<ColorWorm> i=worms.iterator(); i.hasNext();) {
    ColorWorm w = i.next();
    // if the worm's last point is 'off the screen' remove the worm
    // distanceToSquared() is faster than distanceTo() since it avoids
    // the square root calculation and we don't need precise values here...
    if (w.points.get(0).dst2(center) > 640000) {
      i.remove();
    } 
    else {
      // otherwise update and display the worm
      w.update();
      w.display();
    }
  }

  // set the zoom location based on the normalized mouseX (0.0 .. 1.0 interval)
  float normX=(float)mouseX / width;
  // interpolate focal point to new mouse position (15% step per frame)
  zoomLens.setLensPos(normX, 0.15f);
  // determine the selected color based on mouseX
  // by finding which color area contains mouseX
  float focalX=zoomLens.interpolate(0, width, normX);
  for (int i=0, num=names.size()-1; i<=num; i++) {
    float x=zoomLens.interpolate(0, width, (float)i/num);
    float x2=zoomLens.interpolate(0, width, (float)(i+1)/num);
    // select color if focalX is between x and x2
    if (focalX >= x && focalX < x2) {
      selectedColorID=i;
      break;
    }
  }

  // toggle the color palette
  if (showColorPalette) {
    drawColorPalette();
  }

  if (mousePressed) {
    // Create worms with the LEFT mouse button
    if (mouseButton == LEFT) {
      ga_Vector2 mouse = new ga_Vector2(mouseX, mouseY);
      String s=names.get(selectedColorID);
      z_Colors c = z_Colors.getColor(s);
      System.out.println(Integer.toHexString(selectedColorID)+" "+s+" "+Integer.toHexString(c.toARGB()));
      worms.add(new ColorWorm(mouse, c));
      // Change the background color with the RIGHT or MIDDLE mouse button
    } 
    else {
      bg = z_Colors.getColor(names.get(selectedColorID));
    }
  }
}

// Press ANY key to toggle the color palette
public void keyPressed() {
  showColorPalette = !showColorPalette;
}

class ColorWorm {
  List <ga_Vector2> points = new ArrayList <ga_Vector2> ();
  ga_Vector2 direction;
  z_Colors c;

  ColorWorm(ga_Vector2 origin, z_Colors c) {
    // at the origin point (mouseX,mouseY)
    points.add(origin);
    // create a copy of the readonly color for later manipulation
    this.c = c.copy();
    // create a random direction
    direction = ga_Vector2.random();
  }

  void update() {
    // every second frame (not too fast, not too slow)
    if (frameCount % 2 == 0) {
      // create a new point given the last point's coordinates
      ga_Vector2 p = points.get(points.size()-1).copy();
      // rotate the direction randomly somewhere between -30 and 30 degrees
      direction.rotate(radians(random(-30, 30)), false);
      // create a movement vector in that direction with a random magnitude between 15 and 30
      ga_Vector2 move = direction.tmp().norTo(random(15, 30));
      // move the point in that direction and with that distance
      p.add(move);
      // add the new point to the list
      points.add(p);
    }

    // truncate at 25 points (remove the oldest point)
    while (points.size () > 25) {
      points.remove(0);
    }
  }

  void display() {
    // need at least 3 points to construct a spline
    if (points.size()>2) {
      // create Spline2D from the points
      ga_SplineB s = new ga_SplineB(points);
      // subdivide it by 8 into a list of vertices
      List <ga_Vector2> vertices = s.computeVertices(8);
      noFill();
      strokeWeight(2);
      // draw a line through all the vertices
      beginShape();
      for (int i=0,num=vertices.size()-1; i<=num; i++) {
        ga_Vector2 v = vertices.get(i);
        // the position in the list determines the transparency of the segment
        //TODO c.setAlpha(map(i, 0, num, 0, 1));
        // convert the color into ARGB color format (32bit packed integer)
        stroke(c.toARGB());
        vertex(v.x, v.y);
      }
      endShape();
    }
  }
}

void drawColorPalette() {
  noStroke();

  // display all the colors over the width of the screen
  for (int i=0,num=names.size()-1; i<=num; i++) {
    // determine the color swatch's position and width based on
    // it's relative position and the zoom location (mouseX)
    float x = zoomLens.interpolate(0, width, (float)i / num);
    float x2 = zoomLens.interpolate(0, width, (float)(i+1) / num);
    // convert the color into ARGB color format (32bit packed integer)
    fill(z_Colors.getColor(names.get(i)).toARGB());
    // move the colors vertically with mouseY
    rect(x, mouseY-15, x2-x, 30);
  }

  // get the name of the selectedColor
  String name = names.get(selectedColorID);
  float ascent = textAscent();
  float textwidth = textWidth(name);
  // keep the text and it's background fill within screen boundaries
  float x = min(mouseX, width-textwidth-8);
  float y = min(mouseY + 52, height-4);
  // draw a white text background
  fill(255);
  rect(x, y-ascent-4, textwidth+8, ascent+8);
  // draw a black text
  fill(0);
  text(name, x+4, y);
}
}