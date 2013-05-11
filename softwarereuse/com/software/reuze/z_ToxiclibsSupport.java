package com.software.reuze;
/*
 * Copyright (c) 2006-2011 Karsten Schmidt
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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */


import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
/*import toxi.geom.AABB;
import toxi.geom.AxisAlignedCylinder;
import toxi.geom.Cone;
import toxi.geom.Ellipse;
import toxi.geom.Line2D;
import toxi.geom.Line3D;
import toxi.geom.Matrix4x4;
import toxi.geom.Plane;
import toxi.geom.Polygon2D;
import toxi.geom.Ray2D;
import toxi.geom.Ray3D;
import toxi.geom.Vector2;
import toxi.geom.Vector3;
import toxi.geom.Rect;
import toxi.geom.Sphere;
import toxi.geom.Triangle2D;
import toxi.geom.Triangle3D;
import toxi.geom.Vector2;
import toxi.geom.Vector3;
import toxi.geom.mesh.TriangleFace;
import toxi.geom.mesh.IMesh3D;
import toxi.geom.mesh.TriangleMesh;
import toxi.geom.mesh.Vertex;*/

/**
 * In addition to providing new drawing commands, this class provides wrappers
 * for using data types of the toxiclibs core package directly with Processing's
 * drawing commands. The class can be configured to work with any PGraphics
 * instance (incl. offscreen buffers).
 */
public class z_ToxiclibsSupport {

    protected static final Logger logger = Logger
            .getLogger(z_ToxiclibsSupport.class.getName());

    protected PApplet app;
    protected PGraphics gfx;

    private m_Matrix4 normalMap = new m_Matrix4().translate(128, 128, 128)
            .scale(127);

    public z_ToxiclibsSupport(PApplet app) {
        this(app, app.g);
    }

    public z_ToxiclibsSupport(PApplet app, PGraphics gfx) {
        this.app = app;
        this.gfx = gfx;
    }

    public final void box(gb_AABB3 box) {
        mesh(box.toMesh(), false, 0);
    }

    public final void box(gb_AABB3 box, boolean smooth) {
        gb_i_Mesh mesh = box.toMesh();
        if (smooth) {
            mesh.computeVertexNormals();
        }
        mesh(mesh, smooth, 0);
    }

    public final void circle(ga_Vector2 p, float radius) {
        gfx.ellipse(p.x, p.y, radius, radius);
    }

    public final void cone(gb_Cone cone) {
        mesh(cone.toMesh(null, 6, 0, true, true), false, 0);
    }

    public final void cone(gb_Cone cone, boolean topClosed, boolean bottomClosed) {
        mesh(cone.toMesh(null, 6, 0, topClosed, bottomClosed), false, 0);
    }

    public final void cone(gb_Cone cone, int res, boolean smooth) {
        cone(cone, res, true, true, smooth);
    }

    public final void cone(gb_Cone cone, int res, boolean topClosed,
            boolean bottomClosed, boolean smooth) {
        gb_i_Mesh mesh = cone.toMesh(res);
        if (smooth) {
            mesh.computeVertexNormals();
        }
        mesh(mesh, smooth, 0);
    }

    public final void cylinder(gb_a_CylinderAxisAligned cylinder) {
        mesh(cylinder.toMesh(), false, 0);
    }

    public final void cylinder(gb_a_CylinderAxisAligned cylinder, int res,
            boolean smooth) {
        gb_i_Mesh mesh = cylinder.toMesh(res, 0);
        if (smooth) {
            mesh.computeVertexNormals();
        }
        mesh(mesh, smooth, 0);
    }

    public final void ellipse(ga_Ellipse e) {
        ga_Vector2 r = e.getRadii();
        switch (gfx.ellipseMode) {
            case PConstants.CENTER:
                gfx.ellipse(e.position.x, e.position.y, r.x * 2, r.y * 2);
                break;
            case PConstants.RADIUS:
                gfx.ellipse(e.position.x, e.position.y, r.x, r.y);
                break;
            case PConstants.CORNER:
            case PConstants.CORNERS:
                gfx.ellipse(e.position.x - r.x, e.position.y - r.y, r.x * 2, r.y * 2);
                break;
            default:
                logger.warning("invalid ellipse mode: " + gfx.ellipseMode);
        }
    }
    public final void ellipse(ga_Circle e) {
    	float r=e.getRadius();
        switch (gfx.ellipseMode) {
            case PConstants.CENTER:
                gfx.ellipse(e.position.x, e.position.y, r * 2, r * 2);
                break;
            case PConstants.RADIUS:
                gfx.ellipse(e.position.x, e.position.y, r, r);
                break;
            case PConstants.CORNER:
            case PConstants.CORNERS:
                gfx.ellipse(e.position.x - r, e.position.y - r, r * 2, r * 2);
                break;
            default:
                logger.warning("invalid ellipse mode: " + gfx.ellipseMode);
        }
    }

    /**
     * @return the gfx
     */
    public final PGraphics getGraphics() {
        return gfx;
    }

    public final void line(ga_Line2D line) {
        gfx.line(line.a.x, line.a.y, line.b.x, line.b.y);
    }

    public final void line(gb_Line3D line) {
        gfx.line(line.a.x, line.a.y, line.a.z, line.b.x, line.b.y, line.b.z);
    }

    public final void line(ga_Vector2 a, ga_Vector2 b) {
        gfx.line(a.x, a.y, b.x, b.y);
    }

    public final void line(gb_Vector3 a, gb_Vector3 b) {
        gfx.line(a.x, a.y, a.z, b.x, b.y, b.z);
    }

    /**
     * Draws a 2D line strip using all points in the given list of vectors.
     * 
     * @param points
     *            point list
     */
    public final void lineStrip2D(List<? extends ga_Vector2> points) {
        boolean isFilled = gfx.fill;
        gfx.fill = false;
        processVertices2D(points.iterator(), PConstants.POLYGON, false);
        gfx.fill = isFilled;
    }

    /**
     * Draws a 3D line strip using all points in the given list of vectors.
     * 
     * @param points
     *            point list
     */
    public final void lineStrip3D(List<? extends gb_Vector3> points) {
        boolean isFilled = gfx.fill;
        gfx.fill = false;
        processVertices3D(points.iterator(), PConstants.POLYGON, false);
        gfx.fill = isFilled;
    }

    /**
     * Draws a mesh instance using flat shading.
     * 
     * @param mesh
     */
    public final void mesh(gb_i_Mesh mesh) {
        mesh(mesh, false, 0);
    }

    /**
     * Draws a mesh instance.
     * 
     * @param mesh
     * @param smooth
     *            true to enable gouroud shading (uses vertex normals, which
     *            should have been computed beforehand) or false for flat
     *            shading
     */
    public final void mesh(gb_i_Mesh mesh, boolean smooth) {
        mesh(mesh, smooth, 0);
    }

    /**
     * Draws a mesh instance.
     * 
     * @param mesh
     * @param smooth
     *            true to enable gouroud shading (uses vertex normals, which
     *            should have been computed beforehand) or false for flat
     *            shading
     * @param normalLength
     *            if >0 then face (or vertex) normals are rendered at this
     *            length
     */
    public final void mesh(gb_i_Mesh mesh, boolean smooth, float normalLength) {
        gfx.beginShape(PConstants.TRIANGLES);
        if (smooth) {
            for (gb_TriangleFace f : mesh.getFaces()) {
                gfx.normal(f.a.normal.x, f.a.normal.y, f.a.normal.z);
                gfx.vertex(f.a.x, f.a.y, f.a.z);
                gfx.normal(f.b.normal.x, f.b.normal.y, f.b.normal.z);
                gfx.vertex(f.b.x, f.b.y, f.b.z);
                gfx.normal(f.c.normal.x, f.c.normal.y, f.c.normal.z);
                gfx.vertex(f.c.x, f.c.y, f.c.z);
            }
        } else {
            for (gb_TriangleFace f : mesh.getFaces()) {
                gfx.normal(f.normal.x, f.normal.y, f.normal.z);
                gfx.vertex(f.a.x, f.a.y, f.a.z);
                gfx.vertex(f.b.x, f.b.y, f.b.z);
                gfx.vertex(f.c.x, f.c.y, f.c.z);
            }
        }
        gfx.endShape();
        if (normalLength > 0) {
            int strokeCol = 0;
            boolean isStroked = gfx.stroke;
            if (isStroked) {
                strokeCol = gfx.strokeColor;
            }
            if (smooth) {
                for (gb_Vector3Id v : mesh.getVertices()) {
                    gb_Vector3 w = v.tmp().add(v.normal.tmp2().mul(normalLength));
                    gb_Vector3 n = v.normal.tmp2().mul(127);
                    gfx.stroke(n.x + 128, n.y + 128, n.z + 128);
                    gfx.line(v.x, v.y, v.z, w.x, w.y, w.z);
                }
            } else {
                float third = 1f / 3;
                for (gb_TriangleFace f : mesh.getFaces()) {
                    gb_Vector3 c = f.a.tmp().add(f.b).add(f.c).mul(third);
                    gb_Vector3 d = c.tmp3().add(f.normal.tmp2().mul(normalLength));
                    gb_Vector3 n = f.normal.tmp2().mul(127);
                    gfx.stroke(n.x + 128, n.y + 128, n.z + 128);
                    gfx.line(c.x, c.y, c.z, d.x, d.y, d.z);
                }
            }
            if (isStroked) {
                gfx.stroke(strokeCol);
            } else {
                gfx.noStroke();
            }
        }
    }

    /**
     * Draws the given mesh with each face or vertex tinted using its related
     * normal vector as RGB color. Normals can also optionally be shown as
     * lines.
     * 
     * @param mesh
     * @param vertexNormals
     *            true, if using vertex normals (else face normals only)
     * @param normalLength
     */
    public final void meshNormalMapped(gb_i_Mesh mesh, boolean vertexNormals,
            float normalLength) {
        gfx.beginShape(PConstants.TRIANGLES);
        if (vertexNormals) {
            for (gb_TriangleFace f : mesh.getFaces()) {
                gb_Vector3 n = normalMap.applyTo(f.a.normal);
                gfx.fill(n.x, n.y, n.z);
                gfx.normal(f.a.normal.x, f.a.normal.y, f.a.normal.z);
                gfx.vertex(f.a.x, f.a.y, f.a.z);
                n = normalMap.applyTo(f.b.normal);
                gfx.fill(n.x, n.y, n.z);
                gfx.normal(f.b.normal.x, f.b.normal.y, f.b.normal.z);
                gfx.vertex(f.b.x, f.b.y, f.b.z);
                n = normalMap.applyTo(f.c.normal);
                gfx.fill(n.x, n.y, n.z);
                gfx.normal(f.c.normal.x, f.c.normal.y, f.c.normal.z);
                gfx.vertex(f.c.x, f.c.y, f.c.z);
            }
        } else {
            for (gb_TriangleFace f : mesh.getFaces()) {
                gfx.normal(f.normal.x, f.normal.y, f.normal.z);
                gfx.vertex(f.a.x, f.a.y, f.a.z);
                gfx.vertex(f.b.x, f.b.y, f.b.z);
                gfx.vertex(f.c.x, f.c.y, f.c.z);
            }
        }
        gfx.endShape();
        if (normalLength > 0) {
            if (vertexNormals) {
                for (gb_Vector3Id v : mesh.getVertices()) {
                    gb_Vector3 w = v.tmp().add(v.normal.tmp2().mul(normalLength));
                    gb_Vector3 n = v.normal.tmp2().mul(127);
                    gfx.stroke(n.x + 128, n.y + 128, n.z + 128);
                    gfx.line(v.x, v.y, v.z, w.x, w.y, w.z);
                }
            } else {
                for (gb_TriangleFace f : mesh.getFaces()) {
                    gb_Vector3 c = f.getCentroid();
                    gb_Vector3 d = c.tmp().add(f.normal.tmp2().mul(normalLength));
                    gb_Vector3 n = f.normal.tmp2().mul(127);
                    gfx.stroke(n.x + 128, n.y + 128, n.z + 128);
                    gfx.line(c.x, c.y, c.z, d.x, d.y, d.z);
                }
            }
        }
    }

    public void origin(float len) {
        origin(gb_Vector3.ZERO, len);
    }

    /**
     * Draws the major axes from the given point.
     * 
     * @param o
     *            origin point
     * @param len
     *            axis length
     */
    public final void origin(gb_Vector3 o, float len) {
        final float x = o.x;
        final float y = o.y;
        final float z = o.z;
        gfx.stroke(255, 0, 0);
        gfx.line(x, y, z, x + len, y, z);
        gfx.stroke(0, 255, 0);
        gfx.line(x, y, z, x, y + len, z);
        gfx.stroke(0, 0, 255);
        gfx.line(x, y, z, x, y, z + len);
    }

    /**
     * Draws a square section of a plane at the given size.
     * 
     * @param plane
     *            plane to draw
     * @param size
     *            edge length
     */
    public final void plane(gb_Plane plane, float size) {
        mesh(plane.toMesh(size), false, 0);
    }

    /**
     * Draws a 2D point at the given position.
     * 
     * @param v
     */
    public final void point(ga_Vector2 v) {
        gfx.point(v.x, v.y);
    }

    /**
     * Draws a 3D point at the given position.
     * 
     * @param v
     */
    public final void point(gb_Vector3 v) {
        gfx.point(v.x, v.y, v.z);
    }

    public final void points2D(Iterator<? extends ga_Vector2> iterator) {
        processVertices2D(iterator, PConstants.POINTS, false);
    }

    public final void points2D(List<? extends ga_Vector2> points) {
        processVertices2D(points.iterator(), PConstants.POINTS, false);
    }

    public final void points3D(Iterator<? extends gb_Vector3> iterator) {
        processVertices3D(iterator, PConstants.POINTS, false);
    }

    public final void points3D(List<? extends gb_Vector3> points) {
        processVertices3D(points.iterator(), PConstants.POINTS, false);
    }

    public final void polygon2D(ga_Polygon poly) {
        processVertices2D(poly.points.iterator(), PConstants.POLYGON, true);
    }

    public final void processVertices2D(Iterator<? extends ga_Vector2> iterator,
            int shapeID, boolean closed) {
        gfx.beginShape(shapeID);
        while (iterator.hasNext()) {
            ga_Vector2 v = iterator.next();
            gfx.vertex(v.x, v.y);
        }
        if (closed) {
            gfx.endShape(PConstants.CLOSE);
        } else {
            gfx.endShape();
        }
    }

    public final void processVertices3D(Iterator<? extends gb_Vector3> iterator,
            int shapeID, boolean closed) {
        gfx.beginShape(shapeID);
        while (iterator.hasNext()) {
            gb_Vector3 v = iterator.next();
            gfx.vertex(v.x, v.y, v.z);
        }
        if (closed) {
            gfx.endShape(PConstants.CLOSE);
        } else {
            gfx.endShape();
        }
    }

    public final void ray(ga_Ray2D ray, float length) {
        ga_Vector2 e = ray.getPointAtDistance(length);
        gfx.line(ray.origin.x, ray.origin.y, e.x, e.y);
    }

    public final void ray(gb_Ray ray, float length) {
        gb_Vector3 e = ray.getPointAtDistance(length);
        gfx.line(ray.origin.x, ray.origin.y, ray.origin.z, e.x, e.y, e.z);
    }

    public final void rect(ga_Rectangle r) {
        switch (gfx.rectMode) {
            case PConstants.CORNER:
                gfx.rect(r.position.x, r.position.y, r.width, r.height);
                break;
            case PConstants.CORNERS:
                gfx.rect(r.position.x, r.position.y, r.position.x + r.width, r.position.y + r.height);
                break;
            case PConstants.CENTER:
                gfx.rect(r.position.x + r.width * 0.5f, r.position.y + r.height * 0.5f, r.width,
                        r.height);
                break;
            case PConstants.RADIUS:
                float rw = r.width * 0.5f;
                float rh = r.height * 0.5f;
                gfx.rect(r.position.x + rw, r.position.y + rh, rw, rh);
                break;
            default:
                logger.warning("invalid rect mode: " + gfx.rectMode);
        }
    }

    public final void rotate(float theta, gb_Vector3 v) {
        gfx.rotate(theta, v.x, v.y, v.z);
    }

    public final void scale(ga_Vector2 v) {
        gfx.scale(v.x, v.y);
    }

    public final void scale(gb_Vector3 v) {
        gfx.scale(v.x, v.y, v.z);
    }

    /**
     * @param gfx
     *            the gfx to set
     */
    public final void setGraphics(PGraphics gfx) {
        this.gfx = gfx;
    }

    public final void sphere(gb_Sphere sphere, int res) {
        mesh(sphere.toMesh(res));
    }

    public final void sphere(gb_Sphere sphere, int res, boolean smooth) {
        mesh(sphere.toMesh(res), smooth);
    }

    public final void texturedMesh(gb_TriangleMesh mesh, PImage tex, boolean smooth) {
        gfx.beginShape(PConstants.TRIANGLES);
        gfx.texture(tex);
        if (smooth) {
            for (gb_TriangleFace f : mesh.faces) {
                if (f.uvA != null && f.uvB != null && f.uvC != null) {
                    gfx.normal(f.a.normal.x, f.a.normal.y, f.a.normal.z);
                    gfx.vertex(f.a.x, f.a.y, f.a.z, f.uvA.x, f.uvA.y);
                    gfx.normal(f.b.normal.x, f.b.normal.y, f.b.normal.z);
                    gfx.vertex(f.b.x, f.b.y, f.b.z, f.uvB.x, f.uvB.y);
                    gfx.normal(f.c.normal.x, f.c.normal.y, f.c.normal.z);
                    gfx.vertex(f.c.x, f.c.y, f.c.z, f.uvC.x, f.uvC.y);
                } else {
                    gfx.vertex(f.a.x, f.a.y, f.a.z);
                    gfx.vertex(f.b.x, f.b.y, f.b.z);
                    gfx.vertex(f.c.x, f.c.y, f.c.z);
                }
            }
        } else {
            for (gb_TriangleFace f : mesh.faces) {
                gfx.normal(f.normal.x, f.normal.y, f.normal.z);
                if (f.uvA != null && f.uvB != null && f.uvC != null) {
                    gfx.vertex(f.a.x, f.a.y, f.a.z, f.uvA.x, f.uvA.y);
                    gfx.vertex(f.b.x, f.b.y, f.b.z, f.uvB.x, f.uvB.y);
                    gfx.vertex(f.c.x, f.c.y, f.c.z, f.uvC.x, f.uvC.y);
                } else {
                    gfx.vertex(f.a.x, f.a.y, f.a.z);
                    gfx.vertex(f.b.x, f.b.y, f.b.z);
                    gfx.vertex(f.c.x, f.c.y, f.c.z);
                }
            }
        }
        gfx.endShape();
    }

    public final void drawChars(PImage texture, int nRow, int nCol, float step, char... str) {
  	  gfx.beginShape(PConstants.QUADS);
	  gfx.texture(texture);
	  float k=1;
	  int i,j;
	  for (char c : str) {
		  c &= 0xff;  //no unicode support
		  i=c/16;
		  j=c%16;
		  gfx.vertex(k, 1,  1, 1f/nRow*j, 1f/nCol*i);
		  gfx.vertex( k+1, 1,  1, 1f/nRow*(j+1), 1f/nCol*i);
		  gfx.vertex( k+1,  2,  1, 1f/nRow*(j+1), 1f/nCol*(i+1));
		  gfx.vertex(k,  2,  1, 1f/nRow*j, 1f/nCol*(i+1));
		  k+=step;
	  }
	  gfx.endShape();
	}
    public final PImage loadImage(String path) {
    	return app.loadImage(path);
    }
    public final void texturedMesh(gb_TriangleMesh mesh, PImage tex, boolean smooth, int nRow, int nCol, char... str) {
        gfx.beginShape(PConstants.TRIANGLES); //mesh must be quads w 2 faces each
        gfx.texture(tex);                     //upper right tl-tr-br then lower left br-bl-tl
        if (smooth) {
            for (gb_TriangleFace f : mesh.faces) {
                if (f.uvA != null && f.uvB != null && f.uvC != null) {
                    gfx.normal(f.a.normal.x, f.a.normal.y, f.a.normal.z);
                    gfx.vertex(f.a.x, f.a.y, f.a.z, f.uvA.x, f.uvA.y);
                    gfx.normal(f.b.normal.x, f.b.normal.y, f.b.normal.z);
                    gfx.vertex(f.b.x, f.b.y, f.b.z, f.uvB.x, f.uvB.y);
                    gfx.normal(f.c.normal.x, f.c.normal.y, f.c.normal.z);
                    gfx.vertex(f.c.x, f.c.y, f.c.z, f.uvC.x, f.uvC.y);
                } else {
                    gfx.vertex(f.a.x, f.a.y, f.a.z);
                    gfx.vertex(f.b.x, f.b.y, f.b.z);
                    gfx.vertex(f.c.x, f.c.y, f.c.z);
                }
            }
        } else {
        	int i,j,c,m=0;
            for (int n=0; n<mesh.faces.size(); ) {
            	if (m>=str.length) break;
            	c = str[m++] & 0xff;  //no unicode support
            	gb_TriangleFace f=mesh.faces.get(n++);
                gfx.normal(f.normal.x, f.normal.y, f.normal.z);
        		i=c/16;
        		j=c%16;
                if (f.uvA != null && f.uvB != null && f.uvC != null) {
                    gfx.vertex(f.a.x, f.a.y, f.a.z, f.uvA.x, f.uvA.y);
                    gfx.vertex(f.b.x, f.b.y, f.b.z, f.uvB.x, f.uvB.y);
                    gfx.vertex(f.c.x, f.c.y, f.c.z, f.uvC.x, f.uvC.y);
                } else {
                    gfx.vertex(f.a.x, f.a.y, f.a.z, 1f/nRow*j, 1f/nCol*i);
                    gfx.vertex(f.b.x, f.b.y, f.b.z, 1f/nRow*(j+1), 1f/nCol*i);
                    gfx.vertex(f.c.x, f.c.y, f.c.z, 1f/nRow*(j+1), 1f/nCol*(i+1));
                }
                f=mesh.faces.get(n++);
                gfx.normal(f.normal.x, f.normal.y, f.normal.z);
        		if (f.uvA != null && f.uvB != null && f.uvC != null) {
                    gfx.vertex(f.a.x, f.a.y, f.a.z, f.uvA.x, f.uvA.y);
                    gfx.vertex(f.b.x, f.b.y, f.b.z, f.uvB.x, f.uvB.y);
                    gfx.vertex(f.c.x, f.c.y, f.c.z, f.uvC.x, f.uvC.y);
                } else {
                    gfx.vertex(f.a.x, f.a.y, f.a.z, 1f/nRow*(j+1), 1f/nCol*(i+1));
                    gfx.vertex(f.b.x, f.b.y, f.b.z, 1f/nRow*j, 1f/nCol*(i+1));
                    gfx.vertex(f.c.x, f.c.y, f.c.z, 1f/nRow*j, 1f/nCol*i);
                }
            }
        }
        gfx.endShape();
    }

    public final void translate(ga_Vector2 v) {
        gfx.translate(v.x, v.y);
    }

    public final void translate(gb_Vector3 v) {
        gfx.translate(v.x, v.y, v.z);
    }

    public final void triangle(ga_Triangle2D tri) {
        triangle(tri, true);
    }

    public final void triangle(ga_Triangle2D tri, boolean isFullShape) {
        if (isFullShape) {
            gfx.beginShape(PConstants.TRIANGLES);
        }
        gfx.vertex(tri.a.x, tri.a.y);
        gfx.vertex(tri.b.x, tri.b.y);
        gfx.vertex(tri.c.x, tri.c.y);
        if (isFullShape) {
            gfx.endShape();
        }
    }

    public final void triangle(gb_Triangle tri) {
        triangle(tri, true);
    }

    public final void triangle(gb_Triangle tri, boolean isFullShape) {
        if (isFullShape) {
            gfx.beginShape(PConstants.TRIANGLES);
        }
        gb_Vector3 n = tri.computeNormal();
        gfx.normal(n.x, n.y, n.z);
        gfx.vertex(tri.a.x, tri.a.y, tri.a.z);
        gfx.vertex(tri.b.x, tri.b.y, tri.b.z);
        gfx.vertex(tri.c.x, tri.c.y, tri.c.z);
        if (isFullShape) {
            gfx.endShape();
        }
    }

    public final void vertex(ga_Vector2 v) {
        gfx.vertex(v.x, v.y);
    }

    public final void vertex(gb_Vector3 v) {
        gfx.vertex(v.x, v.y, v.z);
    }
    public final void strokeFill(boolean isWireframe, z_Colors stroke, z_Colors fill) {
        if (isWireframe) {
            gfx.noFill();
            gfx.stroke(stroke.toARGB());
        } else {
        	if (stroke==null) gfx.noStroke(); else gfx.stroke(stroke.toARGB());
            gfx.fill(fill.toARGB());
        }
    }
    public final void strokeFill(boolean isWireframe, int strokeARGB, int fillARGB) {
        if (isWireframe) {
            gfx.noFill();
            gfx.stroke(strokeARGB);
        } else {
            if (strokeARGB==0) gfx.noStroke(); else gfx.stroke(strokeARGB);
            gfx.fill(fillARGB);
        }
    }
}
