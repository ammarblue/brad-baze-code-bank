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


import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.software.reuze.ff_OBJWriter;
import com.software.reuze.ff_STLWriter;
import com.software.reuze.ff_i_STLColorModel;


/*import toxi.geom.AABB;
import toxi.geom.Intersector3D;
import toxi.geom.IsectData3D;
import toxi.geom.Matrix4x4;
import toxi.geom.Quaternion;
import toxi.geom.Ray3D;
import toxi.geom.ReadonlyVector3;
import toxi.geom.Sphere;
import toxi.geom.Triangle3D;
import toxi.geom.TriangleIntersector;
import toxi.geom.Vector2;
import toxi.geom.Vector3;
import toxi.math.MathUtils;*/

/**
 * An extensible class to dynamically build, manipulate & export triangle
 * meshes. Meshes are built face by face. This implementation automatically
 * re-uses existing vertices and can generate smooth vertex normals. Vertex and
 * face lists are directly accessible for speed & convenience.
 */
public class gb_TriangleMesh implements gb_i_Mesh, gb_i_Intersector {

    /**
     * Default size for vertex list
     */
    public static final int DEFAULT_NUM_VERTICES = 1000;

    /**
     * Default size for face list
     */
    public static final int DEFAULT_NUM_FACES = 3000;

    /**
     * Default stride setting used for serializing mesh properties into arrays.
     */
    public static final int DEFAULT_STRIDE = 4;

    protected static final Logger logger = Logger.getLogger(gb_TriangleMesh.class
            .getName());

    /**
     * Mesh name
     */
    public String name;

    /**
     * Vector3Id buffer & lookup index when adding new faces
     */
    public LinkedHashMap<gb_Vector3, gb_Vector3Id> vertices;

    /**
     * TriangleFace list
     */
    public ArrayList<gb_TriangleFace> faces;

    protected gb_AABB3 bounds;
    protected gb_Vector3 centroid = new gb_Vector3();
    protected int numVertices;
    protected int numFaces;

    protected m_Matrix4 matrix = new m_Matrix4();
    protected gb_TriangleIntersector intersector = new gb_TriangleIntersector();

    protected int uniqueVertexID;

    public gb_TriangleMesh() {
        this("untitled");
    }

    /**
     * Creates a new mesh instance with initial default buffer sizes.
     * 
     * @param name
     *            mesh name
     */
    public gb_TriangleMesh(String name) {
        this(name, DEFAULT_NUM_VERTICES, DEFAULT_NUM_FACES);
    }

    /**
     * Creates a new mesh instance with the given initial buffer sizes. These
     * numbers are no limits and the mesh can be smaller or grow later on.
     * They're only used to initialize the underlying collections.
     * 
     * @param name
     *            mesh name
     * @param numV
     *            initial vertex buffer size
     * @param numF
     *            initial face list size
     */
    public gb_TriangleMesh(String name, int numV, int numF) {
        init(name, numV, numF);
    }

    public gb_TriangleMesh addFace(gb_Vector3 a, gb_Vector3 b, gb_Vector3 c) {
        return addFace(a, b, c, null, null, null, null);
    }

    public gb_TriangleMesh addFace(gb_Vector3 a, gb_Vector3 b, gb_Vector3 c, ga_Vector2 uvA,
            ga_Vector2 uvB, ga_Vector2 uvC) {
        return addFace(a, b, c, null, uvA, uvB, uvC);
    }

    public gb_TriangleMesh addFace(gb_Vector3 a, gb_Vector3 b, gb_Vector3 c, gb_Vector3 n) {
        return addFace(a, b, c, n, null, null, null);
    }
    /*TODO RPC hiding the triangleface constructor prevents subclassing it*/
    public gb_TriangleMesh addFace(gb_Vector3 a, gb_Vector3 b, gb_Vector3 c, gb_Vector3 n, ga_Vector2 uvA,
            ga_Vector2 uvB, ga_Vector2 uvC) {
        gb_Vector3Id va = checkVertex(a);
        gb_Vector3Id vb = checkVertex(b);
        gb_Vector3Id vc = checkVertex(c);
        if (va.id == vb.id || va.id == vc.id || vb.id == vc.id) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("ignoring invalid face: " + a + "," + b + "," + c);
            }
        } else {
            if (n != null) {
                gb_Vector3 nc = va.tmp2().sub(vc).crs(va.tmp().sub(vb));
                if (n.dot(nc) < 0) {
                    gb_Vector3Id t = va;
                    va = vb;
                    vb = t;
                }
            }
            gb_TriangleFace f = new gb_TriangleFace(va, vb, vc, uvA, uvB, uvC);
            faces.add(f);
            numFaces++;
        }
        return this;
    }

    /**
     * Adds all faces from the given mesh to this one.
     * 
     * @param m
     *            source mesh instance
     */
    public gb_TriangleMesh addMesh(gb_i_Mesh m) {
        for (gb_TriangleFace f : m.getFaces()) {
            addFace(f.a, f.b, f.c, f.uvA, f.uvB, f.uvC);
        }
        return this;
    }

    public gb_AABB3 center(final gb_Vector3 origin) {
        computeCentroid();
        gb_Vector3 delta =
                origin != null ? origin.tmp().sub(centroid) : centroid.tmp().inv();
        for (gb_Vector3Id v : vertices.values()) {
            v.add(delta);
        }
        getBoundingBox();
        return bounds;
    }

    private final gb_Vector3Id checkVertex(gb_Vector3 v) {
        gb_Vector3Id vertex = vertices.get(v);
        if (vertex == null) {
            vertex = createVertex(v, uniqueVertexID++);
            vertices.put(vertex, vertex);
            numVertices++;
        }
        return vertex;
    }

    /**
     * Clears all counters, and vertex & face buffers.
     */
    public gb_TriangleMesh clear() {
        vertices.clear();
        faces.clear();
        bounds = null;
        numVertices = 0;
        numFaces = 0;
        return this;
    }

    public gb_Vector3 computeCentroid() {
        centroid.clr();
        for (gb_Vector3 v : vertices.values()) {
            centroid.add(v);
        }
        return centroid.mul(1f / numVertices).cpy();
    }

    /**
     * Re-calculates all face normals.
     */
    public gb_TriangleMesh computeFaceNormals() {
        for (gb_TriangleFace f : faces) {
            f.computeNormal();
        }
        return this;
    }

    /**
     * Computes the smooth vertex normals for the entire mesh.
     */
    public gb_TriangleMesh computeVertexNormals() {
        for (gb_Vector3Id v : vertices.values()) {
            v.clearNormal();
        }
        for (gb_TriangleFace f : faces) {
            f.a.addFaceNormal(f.normal);
            f.b.addFaceNormal(f.normal);
            f.c.addFaceNormal(f.normal);
        }
        for (gb_Vector3Id v : vertices.values()) {
            v.computeNormal();
        }
        return this;
    }

    /**
     * Creates a deep clone of the mesh. The new mesh name will have "-copy" as
     * suffix.
     * 
     * @return new mesh instance
     */
    public gb_TriangleMesh copy() {
        gb_TriangleMesh m =
                new gb_TriangleMesh(name + "-copy", numVertices, numFaces);
        for (gb_TriangleFace f : faces) {
            m.addFace(f.a, f.b, f.c, f.normal, f.uvA, f.uvB, f.uvC);
        }
        return m;
    }

    protected gb_Vector3Id createVertex(gb_Vector3 v, int id) {
        return new gb_Vector3Id(v, id);
    }

    public gb_TriangleMesh faceOutwards() {
        computeCentroid();
        for (gb_TriangleFace f : faces) {
            gb_Vector3 n = f.getCentroid().sub(centroid);
            float dot = n.dot(f.normal);
            if (dot < 0) {
                f.flipVertexOrder();
            }
        }
        return this;
    }

    public gb_TriangleMesh flipVertexOrder() {
        for (gb_TriangleFace f : faces) {
            gb_Vector3Id t = f.a;
            f.a = f.b;
            f.b = t;
            f.normal.inv();
        }
        return this;
    }

    public gb_TriangleMesh flipYAxis() {
        transform(new m_Matrix4().scale(1, -1, 1));
        flipVertexOrder();
        return this;
    }

    public gb_AABB3 getBoundingBox() {
        final gb_Vector3 minBounds = gb_Vector3.MAX_VALUE.cpy();
        final gb_Vector3 maxBounds = gb_Vector3.MIN_VALUE.cpy();
        for (gb_Vector3Id v : vertices.values()) {
            minBounds.min(v);
            maxBounds.max(v);
        }
        bounds = gb_AABB3.fromMinMax(minBounds, maxBounds);
        return bounds;
    }

    public gb_Sphere getBoundingSphere() {
        float radius = 0;
        computeCentroid();
        for (gb_Vector3Id v : vertices.values()) {
            radius = m_MathUtils.max(radius, v.dst2(centroid));
        }
        return new gb_Sphere(centroid, (float) Math.sqrt(radius));
    }

    public gb_Vector3Id getClosestVertexToPoint(final gb_Vector3 p) {
        gb_Vector3Id closest = null;
        float minDist = Float.MAX_VALUE;
        for (gb_Vector3Id v : vertices.values()) {
            float d = v.dst2(p);
            if (d < minDist) {
                closest = v;
                minDist = d;
            }
        }
        return closest;
    }

    /**
     * Creates an array of unraveled normal coordinates. For each vertex the
     * normal vector of its parent face is used. This is a convenience
     * invocation of {@link #getFaceNormalsAsArray(float[], int, int)} with a
     * default stride = 4.
     * 
     * @return array of xyz normal coords
     */
    public float[] getFaceNormalsAsArray() {
        return getFaceNormalsAsArray(null, 0, DEFAULT_STRIDE);
    }

    /**
     * Creates an array of unraveled normal coordinates. For each vertex the
     * normal vector of its parent face is used. This method can be used to
     * translate the internal mesh data structure into a format suitable for
     * OpenGL Vector3Id Buffer Objects (by choosing stride=4). For more detail,
     * please see {@link #getMeshAsVertexArray(float[], int, int)}
     * 
     * @see #getMeshAsVertexArray(float[], int, int)
     * 
     * @param normals
     *            existing float array or null to automatically create one
     * @param offset
     *            start index in array to place normals
     * @param stride
     *            stride/alignment setting for individual coordinates (min value
     *            = 3)
     * @return array of xyz normal coords
     */
    public float[] getFaceNormalsAsArray(float[] normals, int offset, int stride) {
        stride = m_MathUtils.max(stride, 3);
        if (normals == null) {
            normals = new float[faces.size() * 3 * stride];
        }
        int i = offset;
        for (gb_TriangleFace f : faces) {
            normals[i] = f.normal.x;
            normals[i + 1] = f.normal.y;
            normals[i + 2] = f.normal.z;
            i += stride;
            normals[i] = f.normal.x;
            normals[i + 1] = f.normal.y;
            normals[i + 2] = f.normal.z;
            i += stride;
            normals[i] = f.normal.x;
            normals[i + 1] = f.normal.y;
            normals[i + 2] = f.normal.z;
            i += stride;
        }
        return normals;
    }

    public List<gb_TriangleFace> getFaces() {
        return faces;
    }

    /**
     * Builds an array of vertex indices of all faces. Each vertex ID
     * corresponds to its position in the {@link #vertices} HashMap. The
     * resulting array will be 3 times the face count.
     * 
     * @return array of vertex indices
     */
    public int[] getFacesAsArray() {
        int[] faceList = new int[faces.size() * 3];
        int i = 0;
        for (gb_TriangleFace f : faces) {
            faceList[i++] = f.a.id;
            faceList[i++] = f.b.id;
            faceList[i++] = f.c.id;
        }
        return faceList;
    }

    public gb_IntersectionData getIntersectionData() {
        return intersector.getIntersectionData();
    }

    /**
     * Creates an array of unraveled vertex coordinates for all faces using a
     * stride setting of 4, resulting in a serialized version of all mesh vertex
     * coordinates suitable for VBOs.
     * 
     * @see #getMeshAsVertexArray(float[], int, int)
     * @return float array of vertex coordinates
     */
    public float[] getMeshAsVertexArray() {
        return getMeshAsVertexArray(null, 0, DEFAULT_STRIDE);
    }

    /**
     * Creates an array of unraveled vertex coordinates for all faces. This
     * method can be used to translate the internal mesh data structure into a
     * format suitable for OpenGL Vector3Id Buffer Objects (by choosing stride=4).
     * The order of the array will be as follows:
     * 
     * <ul>
     * <li>Face 1:
     * <ul>
     * <li>Vector3Id #1
     * <ul>
     * <li>x</li>
     * <li>y</li>
     * <li>z</li>
     * <li>[optional empty indices to match stride setting]</li>
     * </ul>
     * </li>
     * <li>Vector3Id #2
     * <ul>
     * <li>x</li>
     * <li>y</li>
     * <li>z</li>
     * <li>[optional empty indices to match stride setting]</li>
     * </ul>
     * </li>
     * <li>Vector3Id #3
     * <ul>
     * <li>x</li>
     * <li>y</li>
     * <li>z</li>
     * <li>[optional empty indices to match stride setting]</li>
     * </ul>
     * </li>
     * </ul>
     * <li>Face 2:
     * <ul>
     * <li>Vector3Id #1</li>
     * <li>...etc.</li>
     * </ul>
     * </ul>
     * 
     * @param verts
     *            an existing target array or null to automatically create one
     * @param offset
     *            start index in array to place vertices
     * @param stride
     *            stride/alignment setting for individual coordinates
     * @return array of xyz vertex coords
     */
    public float[] getMeshAsVertexArray(float[] verts, int offset, int stride) {
        stride = m_MathUtils.max(stride, 3);
        if (verts == null) {
            verts = new float[faces.size() * 3 * stride];
        }
        int i = offset;
        for (gb_TriangleFace f : faces) {
            verts[i] = f.a.x;
            verts[i + 1] = f.a.y;
            verts[i + 2] = f.a.z;
            i += stride;
            verts[i] = f.b.x;
            verts[i + 1] = f.b.y;
            verts[i + 2] = f.b.z;
            i += stride;
            verts[i] = f.c.x;
            verts[i + 1] = f.c.y;
            verts[i + 2] = f.c.z;
            i += stride;
        }
        return verts;
    }

    public int getNumFaces() {
        return numFaces;
    }

    public int getNumVertices() {
        return numVertices;
    }

    public gb_TriangleMesh getRotatedAroundAxis(gb_Vector3 axis, float theta) {
        return copy().rotateAroundAxis(axis, theta);
    }

    public gb_TriangleMesh getRotatedX(float theta) {
        return copy().rotateX(theta);
    }

    public gb_TriangleMesh getRotatedY(float theta) {
        return copy().rotateY(theta);
    }

    public gb_TriangleMesh getRotatedZ(float theta) {
        return copy().rotateZ(theta);
    }

    public gb_TriangleMesh getScaled(float scale) {
        return copy().scale(scale);
    }

    public gb_TriangleMesh getScaled(gb_Vector3 scale) {
        return copy().scale(scale);
    }

    public gb_TriangleMesh getTranslated(gb_Vector3 trans) {
        return copy().translate(trans);
    }

    public float[] getUniqueVerticesAsArray() {
        float[] verts = new float[numVertices * 3];
        int i = 0;
        for (gb_Vector3Id v : vertices.values()) {
            verts[i++] = v.x;
            verts[i++] = v.y;
            verts[i++] = v.z;
        }
        return verts;
    }

    public gb_Vector3Id getVertexAtPoint(gb_Vector3 v) {
        return vertices.get(v);
    }

    public gb_Vector3Id getVertexForID(int id) {
        gb_Vector3Id vertex = null;
        for (gb_Vector3Id v : vertices.values()) {
            if (v.id == id) {
                vertex = v;
                break;
            }
        }
        return vertex;
    }

    /**
     * Creates an array of unraveled vertex normal coordinates for all faces.
     * Uses default stride = 4.
     * 
     * @see #getVertexNormalsAsArray(float[], int, int)
     * @return array of xyz normal coords
     */
    public float[] getVertexNormalsAsArray() {
        return getVertexNormalsAsArray(null, 0, DEFAULT_STRIDE);
    }

    /**
     * Creates an array of unraveled vertex normal coordinates for all faces.
     * This method can be used to translate the internal mesh data structure
     * into a format suitable for OpenGL Vector3Id Buffer Objects (by choosing
     * stride=4). For more detail, please see
     * {@link #getMeshAsVertexArray(float[], int, int)}
     * 
     * @see #getMeshAsVertexArray(float[], int, int)
     * 
     * @param normals
     *            existing float array or null to automatically create one
     * @param offset
     *            start index in array to place normals
     * @param stride
     *            stride/alignment setting for individual coordinates (min value
     *            = 3)
     * @return array of xyz normal coords
     */
    public float[] getVertexNormalsAsArray(float[] normals, int offset,
            int stride) {
        stride = m_MathUtils.max(stride, 3);
        if (normals == null) {
            normals = new float[faces.size() * 3 * stride];
        }
        int i = offset;
        for (gb_TriangleFace f : faces) {
            normals[i] = f.a.normal.x;
            normals[i + 1] = f.a.normal.y;
            normals[i + 2] = f.a.normal.z;
            i += stride;
            normals[i] = f.b.normal.x;
            normals[i + 1] = f.b.normal.y;
            normals[i + 2] = f.b.normal.z;
            i += stride;
            normals[i] = f.c.normal.x;
            normals[i + 1] = f.c.normal.y;
            normals[i + 2] = f.c.normal.z;
            i += stride;
        }
        return normals;
    }

    public Collection<gb_Vector3Id> getVertices() {
        return vertices.values();
    }

    protected void handleSaveAsSTL(ff_STLWriter stl, boolean useFlippedY) {
        if (useFlippedY) {
            stl.setScale(new gb_Vector3(1, -1, 1));
            for (gb_TriangleFace f : faces) {
                stl.face(f.a, f.b, f.c, f.normal, ff_STLWriter.DEFAULT_RGB);
            }
        } else {
            for (gb_TriangleFace f : faces) {
                stl.face(f.b, f.a, f.c, f.normal, ff_STLWriter.DEFAULT_RGB);
            }
        }
        stl.endSave();
        logger.info(numFaces + " faces written");
    }

    public gb_TriangleMesh init(String name, int numV, int numF) {
        setName(name);
        vertices = new LinkedHashMap<gb_Vector3, gb_Vector3Id>(numV, 1.5f, false);
        faces = new ArrayList<gb_TriangleFace>(numF);
        return this;
    }

    public boolean intersectsRay(gb_Ray ray) {
        gb_Triangle tri = intersector.getTriangle();
        for (gb_TriangleFace f : faces) {
            tri.a = f.a;
            tri.b = f.b;
            tri.c = f.c;
            if (intersector.intersectsRay(ray)) {
                return true;
            }
        }
        return false;
    }

    public gb_Triangle perforateFace(final gb_TriangleFace f, float size) {
        gb_Vector3 centroid = f.getCentroid();
        float d = 1 - size;
        gb_Vector3 a2 = f.a.cpy().interpolate(centroid, d);
        gb_Vector3 b2 = f.b.cpy().interpolate(centroid, d);
        gb_Vector3 c2 = f.c.cpy().interpolate(centroid, d);
        removeFace(f);
        addFace(f.a, b2, a2);
        addFace(f.a, f.b, b2);
        addFace(f.b, c2, b2);
        addFace(f.b, f.c, c2);
        addFace(f.c, a2, c2);
        addFace(f.c, f.a, a2);
        return new gb_Triangle(a2, b2, c2);
    }

    /**
     * Rotates the mesh in such a way so that its "forward" axis is aligned with
     * the given direction. This version uses the positive Z-axis as default
     * forward direction.
     * 
     * @param dir
     *            new target direction to point in
     * @return itself
     */
    public gb_TriangleMesh pointTowards(final gb_Vector3 dir) {
        return transform(gb_Quaternion.getAlignmentQuat(dir, gb_Vector3.Z).toMatrix4(matrix), true);
    }

    /**
     * Rotates the mesh in such a way so that its "forward" axis is aligned with
     * the given direction. This version allows to specify the forward
     * direction.
     * 
     * @param dir
     *            new target direction to point in
     * @param forward
     *            current forward axis
     * @return itself
     */
    public gb_TriangleMesh pointTowards(final gb_Vector3 dir, final gb_Vector3 forward) {
        return transform(
                gb_Quaternion.getAlignmentQuat(dir, forward).toMatrix4(matrix), true);
    }

    public void removeFace(gb_TriangleFace f) {
        faces.remove(f);
    }

    public gb_TriangleMesh rotateAroundAxis(gb_Vector3 axis, float theta) {
        return transform(matrix.idt().rotateAroundAxis(axis, theta));
    }

    public gb_TriangleMesh rotateX(float theta) {
        return transform(matrix.idt().rotateX(theta));
    }

    public gb_TriangleMesh rotateY(float theta) {
        return transform(matrix.idt().rotateY(theta));
    }

    public gb_TriangleMesh rotateZ(float theta) {
        return transform(matrix.idt().rotateZ(theta));
    }

    /**
     * Saves the mesh as OBJ format by appending it to the given mesh
     * {@link OBJWriter} instance.
     * 
     * @param obj
     */
    public void saveAsOBJ(ff_OBJWriter obj) {
        int vOffset = obj.getCurrVertexOffset() + 1;
        int nOffset = obj.getCurrNormalOffset() + 1;
        logger.info("writing OBJMesh: " + this.toString());
        obj.newObject(name);
        // vertices
        for (gb_Vector3Id v : vertices.values()) {
            obj.vertex(v);
        }
        // normals
        for (gb_Vector3Id v : vertices.values()) {
            obj.normal(v.normal);
        }
        // faces
        for (gb_TriangleFace f : faces) {
            obj.faceWithNormals(f.b.id + vOffset, f.a.id + vOffset, f.c.id
                    + vOffset, f.b.id + nOffset, f.a.id + nOffset, f.c.id
                    + nOffset);
        }
    }

    /**
     * Saves the mesh as OBJ format to the given {@link OutputStream}. Currently
     * no texture coordinates are supported or written.
     * 
     * @param stream
     */
    public void saveAsOBJ(OutputStream stream) {
        ff_OBJWriter obj = new ff_OBJWriter();
        obj.beginSave(stream);
        saveAsOBJ(obj);
        obj.endSave();
    }

    /**
     * Saves the mesh as OBJ format to the given file path. Existing files will
     * be overwritten.
     * 
     * @param path
     */
    public void saveAsOBJ(String path) {
        ff_OBJWriter obj = new ff_OBJWriter();
        obj.beginSave(path);
        saveAsOBJ(obj);
        obj.endSave();
    }

    /**
     * Saves the mesh as binary STL format to the given {@link OutputStream}.
     * 
     * @param stream
     * @see #saveAsSTL(OutputStream, boolean)
     */
    public final void saveAsSTL(OutputStream stream) {
        saveAsSTL(stream, false);
    }

    /**
     * Saves the mesh as binary STL format to the given {@link OutputStream}.
     * The exported mesh can optionally have it's Y axis flipped by setting the
     * useFlippedY flag to true.
     * 
     * @param stream
     * @param useFlippedY
     */
    public final void saveAsSTL(OutputStream stream, boolean useFlippedY) {
        ff_STLWriter stl = new ff_STLWriter();
        stl.beginSave(stream, numFaces);
        handleSaveAsSTL(stl, useFlippedY);
    }

    /**
     * Saves the mesh as binary STL format to the given {@link OutputStream} and
     * using the supplied {@link ff_STLWriter} instance. Use this method to export
     * data in a custom {@link ff_i_STLColorModel}. The exported mesh can optionally
     * have it's Y axis flipped by setting the useFlippedY flag to true.
     * 
     * @param stream
     * @param stl
     * @param useFlippedY
     */
    public final void saveAsSTL(OutputStream stream, ff_STLWriter stl,
            boolean useFlippedY) {
        stl.beginSave(stream, numFaces);
        handleSaveAsSTL(stl, useFlippedY);
    }

    /**
     * Saves the mesh as binary STL format to the given file path. Existing
     * files will be overwritten.
     * 
     * @param fileName
     */
    public final void saveAsSTL(String fileName) {
        saveAsSTL(fileName, false);
    }

    /**
     * Saves the mesh as binary STL format to the given file path. The exported
     * mesh can optionally have it's Y axis flipped by setting the useFlippedY
     * flag to true. Existing files will be overwritten.
     * 
     * @param fileName
     * @param useFlippedY
     */
    public final void saveAsSTL(String fileName, boolean useFlippedY) {
        saveAsSTL(fileName, new ff_STLWriter(), useFlippedY);
    }

    public final void saveAsSTL(String fileName, ff_STLWriter stl,
            boolean useFlippedY) {
        stl.beginSave(fileName, numFaces);
        handleSaveAsSTL(stl, useFlippedY);
    }

    public gb_TriangleMesh scale(float scale) {
        return transform(matrix.tmp().idt().scale(scale));
    }

    public gb_TriangleMesh scale(final gb_Vector3 scale) {
        return transform(matrix.tmp().idt().scale(scale));
    }

    public gb_TriangleMesh setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "TriangleMesh: " + name + " vertices: " + getNumVertices()
                + " faces: " + getNumFaces();
    }

    /**
     * Applies the given matrix transform to all mesh vertices and updates all
     * face normals.
     * 
     * @param mat
     * @return itself
     */
    public gb_TriangleMesh transform(m_Matrix4 mat) {
        return transform(mat, true);
    }

    /**
     * Applies the given matrix transform to all mesh vertices. If the
     * updateNormals flag is true, all face normals are updated automatically,
     * however vertex normals need a manual update.
     * 
     * @param mat
     * @param updateNormals
     * @return itself
     */
    public gb_TriangleMesh transform(m_Matrix4 mat, boolean updateNormals) {
        for (gb_Vector3Id v : vertices.values()) {
            mat.applyTo(v);
        }
        if (updateNormals) {
            computeFaceNormals();
        }
        return this;
    }

    public gb_TriangleMesh translate(final gb_Vector3 trans) {
        return transform(matrix.idt().translate(trans));
    }

    public gb_TriangleMesh updateVertex(final gb_Vector3 orig, final gb_Vector3 newPos) {
        gb_Vector3Id v = vertices.get(orig);
        if (v != null) {
            vertices.remove(v);
            v.set(newPos);
            vertices.put(v, v);
        }
        return this;
    }
    
    public static gb_TriangleMesh getCharQuad(String name, ga_Rectangle r, int nChars) {
      gb_TriangleMesh mesh = new gb_TriangleMesh(name,(nChars+1)*2,nChars*2);
      float step=r.width/nChars;
	  float k=r.position.x;
	  for (int m=0; m<nChars; m++) {
		  mesh.addFace(new gb_Vector3(k,0,0), new gb_Vector3(k+step,0,0), new gb_Vector3(k+step,r.height,0));
		  mesh.addFace(new gb_Vector3(k+step,r.height,0), new gb_Vector3(k,r.height,0), new gb_Vector3(k,0,0));
		  k+=step;
	  }
	  return mesh;
    }
}
