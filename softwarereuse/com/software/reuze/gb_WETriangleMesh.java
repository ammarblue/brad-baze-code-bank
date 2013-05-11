package com.software.reuze;
/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
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


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;

import com.software.reuze.gb_StrategySubdivisionMidpoint;
import com.software.reuze.gb_a_StrategySubdivision;


/**
 * A class to dynamically build, manipulate & export triangle meshes. Meshes are
 * built face by face. The class automatically re-uses existing vertices and can
 * create smooth vertex normals. Vertices and faces are directly accessible for
 * speed & convenience.
 */
public class gb_WETriangleMesh extends gb_TriangleMesh {

    /**
     * WEVertex buffer & lookup index when adding new faces
     */
    public LinkedHashMap<gb_Line3D, gb_WEWingedEdge> edges;

    private final gb_Line3D edgeCheck = new gb_Line3D(new gb_Vector3(), new gb_Vector3());

    private int uniqueEdgeID;

    public gb_WETriangleMesh() {
        this("untitled");
    }

    /**
     * Creates a new mesh instance with initial default buffer sizes.
     * 
     * @param name
     *            mesh name
     */
    public gb_WETriangleMesh(String name) {
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
    public gb_WETriangleMesh(String name, int numV, int numF) {
        super(name, numV, numF);
    }

    public gb_WETriangleMesh addFace(gb_Vector3 a, gb_Vector3 b, gb_Vector3 c) {
        return addFace(a, b, c, null, null, null, null);
    }

    public gb_WETriangleMesh addFace(gb_Vector3 a, gb_Vector3 b, gb_Vector3 c, ga_Vector2 uvA,
            ga_Vector2 uvB, ga_Vector2 uvC) {
        return addFace(a, b, c, null, uvA, uvB, uvC);
    }

    public gb_WETriangleMesh addFace(gb_Vector3 a, gb_Vector3 b, gb_Vector3 c, gb_Vector3 n) {
        return addFace(a, b, c, n, null, null, null);
    }

    public gb_WETriangleMesh addFace(gb_Vector3 a, gb_Vector3 b, gb_Vector3 c, gb_Vector3 n,
            ga_Vector2 uvA, ga_Vector2 uvB, ga_Vector2 uvC) {
        gb_WEVertex va = checkVertex(a);
        gb_WEVertex vb = checkVertex(b);
        gb_WEVertex vc = checkVertex(c);
        if (va.id == vb.id || va.id == vc.id || vb.id == vc.id) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("ignoring invalid face: " + a + "," + b + "," + c);
            }
        } else {
            if (n != null) {
                gb_Vector3 nc = va.tmp().sub(vc).crs(va.tmp2().sub(vb));
                if (n.dot(nc) < 0) {
                    gb_WEVertex t = va;
                    va = vb;
                    vb = t;
                }
            }
            gb_WEFace f = new gb_WEFace(va, vb, vc, uvA, uvB, uvC);
            faces.add(f);
            numFaces++;
            updateEdge(va, vb, f);
            updateEdge(vb, vc, f);
            updateEdge(vc, va, f);
        }
        return this;
    }

    /**
     * Adds all faces from the given mesh to this one.
     * 
     * @param m
     *            source mesh instance
     */
    public gb_WETriangleMesh addMesh(gb_i_Mesh m) {
        super.addMesh(m);
        return this;
    }

    @Override
    public gb_AABB3 center(gb_Vector3 origin) {
        super.center(origin);
        rebuildIndex();
        return bounds;
    }

    private final gb_WEVertex checkVertex(gb_Vector3 v) {
        gb_WEVertex vertex = (gb_WEVertex) vertices.get(v);
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
    public gb_WETriangleMesh clear() {
        super.clear();
        edges.clear();
        return this;
    }

    /**
     * Creates a deep clone of the mesh. The new mesh name will have "-copy" as
     * suffix.
     * 
     * @return new mesh instance
     */
    public gb_WETriangleMesh copy() {
        gb_WETriangleMesh m = new gb_WETriangleMesh(name + "-copy", numVertices,
                numFaces);
        for (gb_TriangleFace f : faces) {
            m.addFace(f.a, f.b, f.c, f.normal, f.uvA, f.uvB, f.uvC);
        }
        return m;
    }

    protected gb_WEVertex createVertex(gb_Vector3 v, int id) {
        return new gb_WEVertex(v, id);
    }

    /**
     * Flips the vertex ordering between clockwise and anti-clockwise. WEFace
     * normals are updated automatically too.
     * 
     * @return itself
     */
    public gb_WETriangleMesh flipVertexOrder() {
        super.flipVertexOrder();
        return this;
    }

    public gb_WETriangleMesh flipYAxis() {
        super.flipYAxis();
        return this;
    }

    public gb_WEVertex getClosestVertexToPoint(gb_Vector3 p) {
        return (gb_WEVertex) super.getClosestVertexToPoint(p);
    }

    public Collection<gb_WEWingedEdge> getEdges() {
        return edges.values();
    }

    public int getNumEdges() {
        return edges.size();
    }

    public gb_WETriangleMesh getRotatedAroundAxis(gb_Vector3 axis, float theta) {
        return copy().rotateAroundAxis(axis, theta);
    }

    public gb_WETriangleMesh getRotatedX(float theta) {
        return copy().rotateX(theta);
    }

    public gb_WETriangleMesh getRotatedY(float theta) {
        return copy().rotateY(theta);
    }

    public gb_WETriangleMesh getRotatedZ(float theta) {
        return copy().rotateZ(theta);
    }

    public gb_WETriangleMesh getScaled(float scale) {
        return copy().scale(scale);
    }

    public gb_WETriangleMesh getScaled(gb_Vector3 scale) {
        return copy().scale(scale);
    }

    public gb_WETriangleMesh getTranslated(gb_Vector3 trans) {
        return copy().translate(trans);
    }

    public gb_WEVertex getVertexAtPoint(gb_Vector3 v) {
        return (gb_WEVertex) vertices.get(v);
    }

    public gb_WEVertex getVertexForID(int id) {
        return (gb_WEVertex) super.getVertexForID(id);
    }

    public gb_WETriangleMesh init(String name, int numV, int numF) {
        super.init(name, numV, numF);
        edges = new LinkedHashMap<gb_Line3D, gb_WEWingedEdge>(numV, 1.5f, false);
        return this;
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
    public gb_WETriangleMesh pointTowards(gb_Vector3 dir) {
        return transform(gb_Quaternion.getAlignmentQuat(dir, gb_Vector3.Z)
                .toMatrix4(matrix), true);
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
    public gb_WETriangleMesh pointTowards(gb_Vector3 dir, gb_Vector3 forward) {
        return transform(
                gb_Quaternion.getAlignmentQuat(dir, forward).toMatrix4(matrix),
                true);
    }

    public void rebuildIndex() {
        LinkedHashMap<gb_Vector3, gb_Vector3Id> newV = new LinkedHashMap<gb_Vector3, gb_Vector3Id>(
                vertices.size());
        for (gb_Vector3Id v : vertices.values()) {
            newV.put(v, v);
        }
        vertices = newV;
        LinkedHashMap<gb_Line3D, gb_WEWingedEdge> newE = new LinkedHashMap<gb_Line3D, gb_WEWingedEdge>(
                edges.size());
        for (gb_WEWingedEdge e : edges.values()) {
            newE.put(e, e);
        }
        edges = newE;
    }

    protected void removeEdge(gb_WEWingedEdge e) {
        e.remove();
        gb_WEVertex v = (gb_WEVertex) e.a;
        if (v.edges.size() == 0) {
            vertices.remove(v);
        }
        v = (gb_WEVertex) e.b;
        if (v.edges.size() == 0) {
            vertices.remove(v);
        }
        for (gb_WEFace f : e.faces) {
            removeFace(f);
        }
        gb_WEWingedEdge removed = edges.remove(edgeCheck.set(e.a, e.b));
        if (removed != e) {
            throw new IllegalStateException("can't remove edge");
        }
    }

    @Override
    public void removeFace(gb_TriangleFace f) {
        faces.remove(f);
        for (gb_WEWingedEdge e : ((gb_WEFace) f).edges) {
            e.faces.remove(f);
            if (e.faces.size() == 0) {
                removeEdge(e);
            }
        }
    }

    // FIXME
    public void removeUnusedVertices() {
        for (Iterator<gb_Vector3Id> i = vertices.values().iterator(); i.hasNext();) {
            gb_Vector3Id v = i.next();
            boolean isUsed = false;
            for (gb_TriangleFace f : faces) {
                if (f.a == v || f.b == v || f.c == v) {
                    isUsed = true;
                    break;
                }
            }
            if (!isUsed) {
                logger.info("removing vertex: " + v);
                i.remove();
            }
        }
    }

    public void removeVertices(Collection<gb_Vector3Id> selection) {
        for (gb_Vector3Id v : selection) {
            gb_WEVertex wv = (gb_WEVertex) v;
            for (gb_WEWingedEdge e : new ArrayList<gb_WEWingedEdge>(wv.edges)) {
                for (gb_TriangleFace f : new ArrayList<gb_TriangleFace>(e.faces)) {
                    removeFace(f);
                }
            }
        }
        // rebuildIndex();
    }

    public gb_WETriangleMesh rotateAroundAxis(gb_Vector3 axis, float theta) {
        return transform(matrix.idt().rotateAroundAxis(axis, theta));
    }

    public gb_WETriangleMesh rotateX(float theta) {
        return transform(matrix.idt().rotateX(theta));
    }

    public gb_WETriangleMesh rotateY(float theta) {
        return transform(matrix.idt().rotateY(theta));
    }

    public gb_WETriangleMesh rotateZ(float theta) {
        return transform(matrix.idt().rotateZ(theta));
    }

    public gb_WETriangleMesh scale(float scale) {
        return transform(matrix.idt().scale(scale));
    }

    public gb_WETriangleMesh scale(gb_Vector3 scale) {
        return transform(matrix.idt().scale(scale));
    }

    public void splitEdge(gb_Vector3 a, gb_Vector3 b,
            gb_a_StrategySubdivision subDiv) {
        gb_WEWingedEdge e = edges.get(edgeCheck.set(a, b));
        if (e != null) {
            splitEdge(e, subDiv);
        }
    }

    public void splitEdge(gb_WEWingedEdge e, gb_a_StrategySubdivision subDiv) {
        List<gb_Vector3> mid = subDiv.computeSplitPoints(e);
        splitFace(e.faces.get(0), e, mid);
        if (e.faces.size() > 1) {
            splitFace(e.faces.get(1), e, mid);
        }
        removeEdge(e);
    }

    protected void splitFace(gb_WEFace f, gb_WEWingedEdge e, List<gb_Vector3> midPoints) {
        gb_Vector3 p = null;
        for (int i = 0; i < 3; i++) {
            gb_WEWingedEdge ec = f.edges.get(i);
            if (!ec.equals(e)) {
                if (ec.a.equals(e.a) || ec.a.equals(e.b)) {
                    p = ec.b;
                } else {
                    p = ec.a;
                }
                break;
            }
        }
        gb_Vector3 prev = null;
        for (int i = 0, num = midPoints.size(); i < num; i++) {
            gb_Vector3 mid = midPoints.get(i);
            if (i == 0) {
                addFace(p, e.a, mid, f.normal);
            } else {
                addFace(p, prev, mid, f.normal);
            }
            if (i == num - 1) {
                addFace(p, mid, e.b, f.normal);
            }
            prev = mid;
        }
    }

    public void subdivide() {
        subdivide(0);
    }

    public void subdivide(float minLength) {
        subdivide(new gb_StrategySubdivisionMidpoint(), minLength);
    }

    public void subdivide(gb_a_StrategySubdivision subDiv) {
        subdivide(subDiv, 0);
    }

    public void subdivide(gb_a_StrategySubdivision subDiv, float minLength) {
        subdivideEdges(new ArrayList<gb_WEWingedEdge>(edges.values()), subDiv,
                minLength);
    }

    protected void subdivideEdges(List<gb_WEWingedEdge> origEdges,
            gb_a_StrategySubdivision subDiv, float minLength) {
        Collections.sort(origEdges, subDiv.getEdgeOrdering());
        minLength *= minLength;
        for (gb_WEWingedEdge e : origEdges) {
            if (edges.containsKey(e)) {
                if (e.getLengthSquared() >= minLength) {
                    splitEdge(e, subDiv);
                }
            }
        }
    }

    public void subdivideFaceEdges(List<gb_WEFace> faces,
            gb_a_StrategySubdivision subDiv, float minLength) {
        List<gb_WEWingedEdge> fedges = new ArrayList<gb_WEWingedEdge>();
        for (gb_WEFace f : faces) {
            for (gb_WEWingedEdge e : f.edges) {
                if (!fedges.contains(e)) {
                    fedges.add(e);
                }
            }
        }
        subdivideEdges(fedges, subDiv, minLength);
    }

    @Override
    public String toString() {
        return "WETriangleMesh: " + name + " vertices: " + getNumVertices()
                + " faces: " + getNumFaces() + " edges:" + getNumEdges();
    }

    /**
     * Applies the given matrix transform to all mesh vertices and updates all
     * face normals.
     * 
     * @param mat
     * @return itself
     */
    public gb_WETriangleMesh transform(m_Matrix4 mat) {
        return transform(mat, true);
    }

    /**
     * Applies the given matrix transform to all mesh vertices. If the
     * updateNormals flag is true, all face normals are updated automatically,
     * however vertex normals still need a manual update.
     * 
     * @param mat
     * @param updateNormals
     * @return itself
     */
    public gb_WETriangleMesh transform(m_Matrix4 mat, boolean updateNormals) {
        for (gb_Vector3Id v : vertices.values()) {
            mat.applyTo(v);
        }
        rebuildIndex();
        if (updateNormals) {
            computeFaceNormals();
        }
        return this;
    }

    public gb_WETriangleMesh translate(gb_Vector3 trans) {
        return transform(matrix.idt().translate(trans));
    }

    protected void updateEdge(gb_WEVertex va, gb_WEVertex vb, gb_WEFace f) {
        edgeCheck.set(va, vb);
        gb_WEWingedEdge e = edges.get(edgeCheck);
        if (e != null) {
            e.addFace(f);
        } else {
            e = new gb_WEWingedEdge(va, vb, f, uniqueEdgeID++);
            edges.put(e, e);
            va.addEdge(e);
            vb.addEdge(e);
        }
        f.addEdge(e);
    }
}
