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




import java.util.Collection;

import com.software.reuze.gb_TriangleFace;


/*import toxi.geom.AABB;
import toxi.geom.ReadonlyVector3;
import toxi.geom.Sphere;
import toxi.geom.Vec2D;
import toxi.geom.Vector3;*/

/**
 * Common interface for 3D (triangle) mesh containers.
 */
public interface gb_i_Mesh {

    /**
     * Adds the given 3 points as triangle face to the mesh. The assumed vertex
     * order is anti-clockwise.
     * 
     * @param a
     * @param b
     * @param c
     */
    public gb_i_Mesh addFace(gb_Vector3 a, gb_Vector3 b, gb_Vector3 c);

    /**
     * Adds the given 3 points as triangle face to the mesh and assigns the
     * given texture coordinates to each vertex. The assumed vertex order is
     * anti-clockwise.
     * 
     * @param a
     * @param b
     * @param c
     * @param uvA
     * @param uvB
     * @param uvC
     * @return itself
     */
    public gb_i_Mesh addFace(gb_Vector3 a, gb_Vector3 b, gb_Vector3 c, ga_Vector2 uvA, ga_Vector2 uvB,
    		ga_Vector2 uvC);

    public gb_i_Mesh addFace(gb_Vector3 a, gb_Vector3 b, gb_Vector3 c, gb_Vector3 n);

    public gb_i_Mesh addFace(gb_Vector3 a, gb_Vector3 b, gb_Vector3 c, gb_Vector3 n, ga_Vector2 uvA,
    		ga_Vector2 uvB, ga_Vector2 uvC);

    public gb_i_Mesh addMesh(gb_i_Mesh mesh);

    /**
     * Centers the mesh around the given pivot point (the centroid of its AABB).
     * Method also updates & returns the new bounding box.
     * 
     * @param origin
     *            new centroid or null (defaults to {0,0,0})
     */
    public gb_AABB3 center(gb_Vector3 origin);

    /**
     * Clears all counters, and vertex & face buffers.
     */
    public gb_i_Mesh clear();

    /**
     * Computes the mesh centroid, the average position of all vertices.
     * 
     * @return center point
     */
    public gb_Vector3 computeCentroid();

    /**
     * Re-calculates all face normals.
     */
    public gb_i_Mesh computeFaceNormals();

    /**
     * Computes the smooth vertex normals for the entire mesh.
     * 
     * @return itself
     */
    public gb_i_Mesh computeVertexNormals();

    /**
     * Changes the vertex order of faces such that their normal is facing away
     * from the mesh centroid.
     * 
     * @return itself
     */
    public gb_i_Mesh faceOutwards();

    /**
     * Flips the vertex ordering between clockwise and anti-clockwise. Face
     * normals are updated automatically too.
     * 
     * @return itself
     */
    public gb_i_Mesh flipVertexOrder();

    /**
     * Flips all vertices along the Y axis and reverses the vertex ordering of
     * all faces to compensate and keep the direction of normals intact.
     * 
     * @return itself
     */
    public gb_i_Mesh flipYAxis();

    /**
     * Computes & returns the axis-aligned bounding box of the mesh.
     * 
     * @return bounding box
     */
    public gb_AABB3 getBoundingBox();

    /**
     * Computes & returns the bounding sphere of the mesh. The origin of the
     * sphere is the mesh's centroid.
     * 
     * @return bounding sphere
     */
    public gb_Sphere getBoundingSphere();

    public gb_Vector3Id getClosestVertexToPoint(gb_Vector3 p);

    public Collection<gb_TriangleFace> getFaces();

    /**
     * Returns the number of triangles used.
     * 
     * @return face count
     */
    public int getNumFaces();

    /**
     * Returns the number of actual vertices used (unique vertices).
     * 
     * @return vertex count
     */
    public int getNumVertices();

    public Collection<gb_Vector3Id> getVertices();

    public gb_i_Mesh init(String name, int numV, int numF);

    public gb_i_Mesh setName(String name);
}
