package reuze.demo;
	/**
	 * <p>This example demonstrates the MeshVoxelizer utility to turn a given
	 * triangle mesh into a volumetric representation for further manipulation.
	 * E.g. This is useful for some digital fabrication tasks when only a shell
	 * with a physical wall thickness is desired rather than a completely solid/filled
	 * polygon model. Other use cases incl. experimentation with VolumetricBrushes
	 * to drill holes into models etc.</p>
	 *
	 * <p>The MeshVoxelizer class is currently still in ongoing development, so any
	 * feature requests/ideas/help is appreciated.</p>
	 *
	 * <p><strong>Usage:</strong><ul>
	 * <li>v: voxelize current mesh (see details in function comment)</li>
	 * <li>l: apply laplacian mesh smooth</li>
	 * <li>w: wireframe on/off</li>
	 * <li>n: show normals on/off</li>
	 * <li>r: reset mesh</li>
	 * <li>-/=: adjust zoom</li>
	 * </ul></p>
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
	 
import com.software.reuze.ga_AABB;
import com.software.reuze.gb_AABB3;
import com.software.reuze.gb_IsoSurfaceHash;
import com.software.reuze.gb_MeshVoxelizer;
import com.software.reuze.gb_MeshWEFilterStrategyLaplacianSmooth;
import com.software.reuze.gb_StrategySubdivisionDisplacementMidpoint;
import com.software.reuze.gb_Vector3;
import com.software.reuze.gb_WETriangleMesh;
import com.software.reuze.gb_a_StrategySubdivision;
import com.software.reuze.gb_a_VoxelSpace;
import com.software.reuze.gb_i_IsoSurface;
import com.software.reuze.z_ToxiclibsSupport;

import processing.core.PApplet;
//import processing.opengl.*;

public class demoToxiGLVoxelMesh extends PApplet { //TODO doesn't work

	z_ToxiclibsSupport gfx;
	gb_WETriangleMesh mesh;

	boolean isWireframe;
	float currZoom = 1.25f;

	boolean showNormals;

	public void setup() {
	  size(1280, 720, P3D);
	  gfx = new z_ToxiclibsSupport(this);
	  initMesh();
	}

	public void draw() {
	  background(255);
	  translate(width / 2, height / 2, 0);
	  //rotateX(mouseY * 0.01f);
	  //rotateY(mouseX * 0.01f);
	  scale(currZoom);
	  if (!isWireframe) {
	    fill(255);
	    noStroke();
	    lights();
	  } 
	  else {
	    gfx.origin(new gb_Vector3(), 100);
	    noFill();
	    stroke(0);
	  }
	  gfx.meshNormalMapped(mesh, !isWireframe, showNormals ? 10 : 0);
	}

	// creates a simple cube mesh and applies displacement subdivision
	// on all edges for several iterations
	void initMesh() {
	  mesh = new gb_WETriangleMesh();
	  new gb_AABB3(new gb_Vector3(0, 0, 0), 100).toMesh(mesh);
	  for(int i=0; i<5; i++) {
	    gb_a_StrategySubdivision subdiv = new gb_StrategySubdivisionDisplacementMidpoint(
	      mesh.computeCentroid(),
	      i % 2 == 0 ? 0.35f : -0.2f
	    );
	    mesh.subdivide(subdiv,0);
	  }
	  mesh.computeFaceNormals();
	  mesh.faceOutwards();
	  mesh.computeVertexNormals();
	}

	// this function will first translate the mesh into a volumetric version
	// this volumetric representation will constitute of a solid shell
	// coinciding (albeit with loss of precision) with the original mesh
	// the function then calculates a new mesh of an iso surface in this voxel space
	// the original mesh will be discarded (overwritten)
	//
	// if you have enough RAM and would like less holes in the resulting surface
	// try a higher voxel resolution (e.g. 128, 192) and/or increase wall thickness
	void voxelizeMesh() {
	  gb_MeshVoxelizer voxelizer=new gb_MeshVoxelizer(64);
	  // try setting to 1 or 2 (voxels)
	  voxelizer.setWallThickness(0);
	  gb_a_VoxelSpace vol = voxelizer.voxelizeMesh(mesh);
	  vol.closeSides();
	  gb_i_IsoSurface surface = new gb_IsoSurfaceHash(vol);
	  mesh = new gb_WETriangleMesh();
	  surface.computeSurfaceMesh(mesh, 0.2f);
	  mesh.computeVertexNormals();
	}

	public void keyPressed() {
	  if (key == 'w') {
	    isWireframe = !isWireframe;
	  }
	  if (key == 'l') {
	    new gb_MeshWEFilterStrategyLaplacianSmooth().filter(mesh, 1);
	  }
	  if (key == '-') {
	    currZoom -= 0.1;
	  }
	  if (key == '=') {
	    currZoom += 0.1;
	  }
	  if (key == 'v') {
	    voxelizeMesh();
	  }
	  if (key == 'n') {
	    showNormals = !showNormals;
	  }
	  if (key=='r') {
	    initMesh();
	  }
	}
}
