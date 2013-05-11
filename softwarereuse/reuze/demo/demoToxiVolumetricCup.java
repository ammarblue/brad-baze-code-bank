package reuze.demo;
	/**
	 * <p>NoiseSurface demo showing how to utilize the IsoSurface class to efficiently
	 * visualise volumetric data, in this case using 3D SimplexNoise. The demo also
	 * shows how to save the generated mesh as binary STL file (or alternatively in
	 * OBJ format) for later use in other 3D tools/digital fabrication.</p>
	 * 
	 * <p>Further classes for the toxi.volume package are planned to easier draw
	 * and manipulate volumetric data.</p>
	 * 
	 * <p>Key controls:</p>
	 * <ul>
	 * <li>w : toggle rendering style between shaded/wireframe</li>
	 * <li>s : save model as STL & quit</li>
	 * <li>1-9 : adjust brush density</li>
	 * <li>a-z : adjust density threshold for calculating surface</li>
	 * <li>-/= : adjust zoom</li>
	 * </ul>
	 */

	/* 
	 * Copyright (c) 2009 Karsten Schmidt
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

import com.software.reuze.gb_IsoSurfaceArrayVertices;
import com.software.reuze.gb_TriangleMesh;
import com.software.reuze.gb_Vector3;
import com.software.reuze.gb_VoxelSpace;
import com.software.reuze.gb_VoxelSpaceBrushRound;
import com.software.reuze.gb_a_VoxelSpace;
import com.software.reuze.gb_a_VoxelSpaceBrush;
import com.software.reuze.gb_i_IsoSurface;
import com.software.reuze.m_WaveSine;
import com.software.reuze.m_a_Wave;
import com.software.reuze.z_ToxiclibsSupport;

import processing.core.PApplet;
//import processing.opengl.*;
public class demoToxiVolumetricCup extends PApplet { //TODO does not draw a cup??


	int DIMX=96;
	int DIMY=96;
	int DIMZ=128;

	float ISO_THRESHOLD = 0.1f;
	float NS=0.03f;
	gb_Vector3 SCALE=new gb_Vector3(256f,256f,384f);

	gb_a_VoxelSpace volume;
	gb_a_VoxelSpaceBrush brush;
	gb_i_IsoSurface surface;
	gb_TriangleMesh mesh;

	m_a_Wave brushSize;

	boolean isWireframe=false;
	boolean doSave=false;

	float currScale=1;
	float density=0.5f;
	float spin=8;
	float currZ=0;
	float Z_STEP=0.005f;

	z_ToxiclibsSupport gfx;

	public void setup() {
	  size(1024,768,P3D);
	  hint(ENABLE_OPENGL_4X_SMOOTH);
	  gfx=new z_ToxiclibsSupport(this);
	  strokeWeight(0.5f);
	  volume=new gb_VoxelSpace(SCALE,DIMX,DIMY,DIMZ);
	  brush=new gb_VoxelSpaceBrushRound(volume,SCALE.x/2);
	  brushSize=new m_WaveSine(-HALF_PI,2*TWO_PI*Z_STEP,SCALE.x*0.03f,SCALE.x*0.06f);
	  surface=new gb_IsoSurfaceArrayVertices(volume);
	  mesh=new gb_TriangleMesh();
	}

	public void draw() {
	  brush.setSize(brushSize.update());
	  float offsetZ=-SCALE.z+currZ*SCALE.z*2.6666f;
	  float currRadius=SCALE.x*0.4f*sin(currZ*PI);
	  for(float t=0; t<TWO_PI; t+=TWO_PI/6f) {
	    brush.drawAtAbsolutePos(new gb_Vector3(currRadius*cos(t+currZ*spin),currRadius*sin(t+currZ*spin),offsetZ),density);
	    brush.drawAtAbsolutePos(new gb_Vector3(currRadius*cos(t-currZ*spin),currRadius*sin(t-currZ*spin),offsetZ),density);
	  }
	  currZ+=Z_STEP;
	  volume.closeSides();
	  surface.reset();
	  surface.computeSurfaceMesh(mesh,ISO_THRESHOLD);
	  background(128);
	  translate(width/2,height/2,0);
	  lightSpecular(230,230,230);
	  directionalLight(255,255,255,1,1,-1);
	  shininess(1.0f);
	  rotateX(-0.4f);
	  rotateY(frameCount*0.05f);
	  scale(currScale);
	  noStroke();
	  gfx.mesh(mesh);
	}

	public void keyPressed() {
	  if(key=='-') currScale=max(currScale-0.1f,0.5f);
	  if(key=='=') currScale=min(currScale+0.1f,10f);
	  if(key=='w') { 
	    isWireframe=!isWireframe; 
	    return; 
	  }
	  if (key>='1' && key<='9') {
	    density=-0.5f+(key-'1')*0.1f;
	    println(density);
	  }
	  if (key=='0') density=0.5f;
	  if (key>='a' && key<='z') ISO_THRESHOLD=(key-'a')*0.019f+0.01f;
	}

}
