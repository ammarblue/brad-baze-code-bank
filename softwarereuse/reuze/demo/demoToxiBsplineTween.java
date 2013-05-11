package reuze.demo;
import com.software.reuze.ga_Circle;
import com.software.reuze.ga_Polygon;
import com.software.reuze.ga_Vector2;
import com.software.reuze.m_InterpolateValueBezier;
import com.software.reuze.m_i_InterpolateValue;
import com.software.reuze.z_ToxiclibsSupport;

import processing.core.PApplet;
	/**
	 * <p>This example uses the BezierInterpolation strategy to control the resolution of a polygon.</p>
	 * <p><strong>Usage:</strong> Move mouse horizontally to move through bezier curve.</p>
	 */

	/* 
	 * Copyright (c) 2010 Karsten Schmidt
	 * 
	 * This library is free software; you can redistribute it and/or
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
public class demoToxiBsplineTween extends PApplet {

	int MAX_RES = 24;

	// create bezier curve for interpolation
	// the 2 parameters control curvatures in both halves
	m_i_InterpolateValue tween=new m_InterpolateValueBezier(1f,-2.2f);
	z_ToxiclibsSupport gfx;

	public void setup() {
	  size(680,382);
	  smooth();
	  gfx=new z_ToxiclibsSupport(this);
	}

	public void draw() {
	  background(255);
	  noFill();
	  stroke(0,255,255);
	  float normX=mouseX/(float)width;
	  float res=tween.interpolate(3,MAX_RES,normX);
	  ga_Polygon poly=new ga_Circle(new ga_Vector2(width/2,height/2),150).toPolygon(round(res));
	  gfx.polygon2D(poly);

	  float scale=(float)height/MAX_RES;
	  stroke(160);
	  beginShape();
	  for(int x=0; x<=width; x+=5) {
	    vertex(x,tween.interpolate(3,MAX_RES,(float)x/width)*scale);
	  }
	  endShape();
	  stroke(255,0,128);
	  ellipse(mouseX,res*scale,10,10);
	}

}
