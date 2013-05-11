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

/*import toxi.geom.IsectData3D;
import toxi.geom.Ray3D;
import toxi.geom.Triangle3D;
import toxi.geom.TriangleIntersector;
import toxi.geom.Vec3D;
import toxi.math.Interpolation2D;
import toxi.math.MathUtils;*/

/**
 * Implementation of a 2D grid based height field with basic intersection
 * features and conversion to {@link gb_TriangleMesh}. The terrain is always
 * located in the XZ plane with the positive Y axis as up vector.
 */
public class gb_Terrain {

    protected float[] elevation;
    protected gb_Vector3[] vertices;

    protected int width;
    protected int depth;
    protected ga_Vector2 scale;

    /**
     * Constructs a new and initially flat terrain of the given size in the XZ
     * plane, centered around the world origin.
     */
    public gb_Terrain(int width, int depth, float scale) {
    	this(width, depth, new ga_Vector2(scale, scale));
    }
    public gb_Terrain(int width, int depth, ga_Vector2 scale) {
        this.width = width;
        this.depth = depth;
        this.scale = scale;
        this.elevation = new float[width * depth];
        this.vertices = new gb_Vector3[elevation.length];
        gb_Vector3 offset = new gb_Vector3(width / 2, 0, depth / 2);
        gb_Vector3 scaleXZ = gb_Vector3.to3DXZ(scale);
        for (int z = 0, i = 0; z < depth; z++) {
            for (int x = 0; x < width; x++) {
                vertices[i++] =
                        new gb_Vector3(x, 0, z).sub(offset).mul(scaleXZ);
            }
        }
    }

    /**
     * @return number of grid cells along the Z axis.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * @param x
     * @param z
     * @return the elevation at grid point
     */
    public float getHeightAtCell(int x, int z) {
        return elevation[getIndex(x, z)];
    }
    static final m_InterpolatePointBilinear ipb=new m_InterpolatePointBilinear();
    /**
     * Computes the elevation of the terrain at the given 2D world coordinate
     * (based on current terrain scale).
     * 
     * @param x
     *            scaled world coord x
     * @param z
     *            scaled world coord z
     * @return interpolated elevation
     */
    public float getHeightAtPoint(float x, float z) {
        float xx = x / scale.x + width * 0.5f;
        float zz = z / scale.y + depth * 0.5f;
        float y = 0;
        if (xx >= 0 && xx < width && zz >= 0 && zz < depth) {
            int x2 = (int) m_MathUtils.min(xx + 1, width - 1);
            int z2 = (int) m_MathUtils.min(zz + 1, depth - 1);
            float a = getHeightAtCell((int) xx, (int) zz);
            float b = getHeightAtCell(x2, (int) zz);
            float c = getHeightAtCell((int) xx, z2);
            float d = getHeightAtCell(x2, z2);
            y =
                    ipb.bilinear(xx, zz, (int) xx, (int) zz, x2,
                            z2, a, b, c, d);
        }
        return y;
    }

    /**
     * Computes the array index for the given cell coords & checks if they're in
     * bounds. If not an {@link IndexOutOfBoundsException} is thrown.
     * 
     * @param x
     * @param z
     * @return array index
     */
    protected int getIndex(int x, int z) {
        int idx = z * width + x;
        if (idx < 0 || idx > elevation.length) {
            throw new IndexOutOfBoundsException(
                    "the given terrain cell is invalid: " + x + ";" + z);
        }
        return idx;
    }

    protected gb_Vector3 getVertexAtCell(int x, int z) {
        return vertices[getIndex(x, z)];
    }

    /**
     * @return number of grid cells along the X axis.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Computes the 3D position (with elevation) and normal vector at the given
     * 2D location in the terrain. The position is in scaled world coordinates
     * based on the given terrain scale. The returned data is encapsulated in a
     * {@link gb_IntersectionData.geom.IsectData3D} instance.
     * 
     * @param x
     * @param z
     * @return intersection data parcel
     */
    public gb_IntersectionData intersectAtPoint(float x, float z) {
        float xx = x / scale.x + width * 0.5f;
        float zz = z / scale.y + depth * 0.5f;
        gb_IntersectionData isec = new gb_IntersectionData();
        if (xx >= 0 && xx < width && zz >= 0 && zz < depth) {
            int x2 = (int) m_MathUtils.min(xx + 1, width - 1);
            int z2 = (int) m_MathUtils.min(zz + 1, depth - 1);
            gb_Vector3 a = getVertexAtCell((int) xx, (int) zz);
            gb_Vector3 b = getVertexAtCell(x2, (int) zz);
            gb_Vector3 c = getVertexAtCell(x2, z2);
            gb_Vector3 d = getVertexAtCell((int) xx, z2);
            gb_Ray r = new gb_Ray(new gb_Vector3(x, 10000, z), new gb_Vector3(0, -1, 0));
            gb_TriangleIntersector i =
                    new gb_TriangleIntersector(new gb_Triangle(a, b, d));
            if (i.intersectsRay(r)) {
                isec = i.getIntersectionData();
            } else {
                i.setTriangle(new gb_Triangle(b, c, d));
                i.intersectsRay(r);
                isec = i.getIntersectionData();
            }
        }
        return isec;
    }

    /**
     * Sets the elevation of all cells to those of the given array values.
     * 
     * @param elevation
     *            array of height values
     * @return itself
     */
    public gb_Terrain setElevation(float[] elevation) {
        if (this.elevation.length == elevation.length) {
            for (int i = 0; i < elevation.length; i++) {
                this.vertices[i].y = this.elevation[i] = elevation[i];
            }
        } else {
            throw new IllegalArgumentException(
                    "the given elevation array size does not match terrain");
        }
        return this;
    }

    /**
     * Sets the elevation for a single given grid cell.
     * 
     * @param x
     * @param z
     * @param h
     *            new elevation value
     * @return itself
     */
    public gb_Terrain setHeightAtCell(int x, int z, float h) {
        int index = getIndex(x, z);
        elevation[index] = h;
        vertices[index].y = h;
        return this;
    }

    public gb_i_Mesh toMesh() {
        return toMesh(null);
    }

    public gb_i_Mesh toMesh(float groundLevel) {
        return toMesh(null, groundLevel);
    }

    /**
     * Creates a {@link gb_TriangleMesh} instance of the terrain surface or adds
     * its geometry to an existing mesh.
     * 
     * @param mesh
     * @return mesh instance
     */
    public gb_i_Mesh toMesh(gb_i_Mesh mesh) {
        return toMesh(mesh, 0, 0, width, depth);
    }

    /**
     * Creates a {@link gb_TriangleMesh} instance of the terrain and constructs
     * side panels and a bottom plane to form a fully enclosed mesh volume, e.g.
     * suitable for CNC fabrication or 3D printing. The bottom plane will be
     * created at the given ground level (can also be negative) and the sides
     * are extended downward to that level too.
     * 
     * @param mesh
     *            existing mesh or null
     * @param groundLevel
     * @return mesh
     */
    public gb_i_Mesh toMesh(gb_i_Mesh mesh, float groundLevel) {
        return toMesh(mesh, 0, 0, width, depth, groundLevel);
    }

    public gb_i_Mesh toMesh(gb_i_Mesh mesh, int minX, int minZ, int maxX, int maxZ) {
        if (mesh == null) {
            mesh = new gb_TriangleMesh("terrain", vertices.length,
                    vertices.length * 2);
        }
        minX = m_MathUtils.clip(minX, 0, width - 1);
        maxX = m_MathUtils.clip(maxX, 0, width);
        minZ = m_MathUtils.clip(minZ, 0, depth - 1);
        maxZ = m_MathUtils.clip(maxZ, 0, depth);
        minX++;
        minZ++;
        for (int z = minZ, idx = minX * width; z < maxZ; z++, idx += width) {
            for (int x = minX; x < maxX; x++) {
                mesh.addFace(vertices[idx - width + x - 1], vertices[idx
                        - width + x], vertices[idx + x - 1]);
                mesh.addFace(vertices[idx - width + x], vertices[idx + x],
                        vertices[idx + x - 1]);
            }
        }
        return mesh;
    }

    public gb_i_Mesh toMesh(gb_i_Mesh mesh, int mix, int miz, int mxx, int mxz,
            float groundLevel) {
        mesh = toMesh(mesh, mix, miz, mxx, mxz);
        mix = m_MathUtils.clip(mix, 0, width - 1);
        mxx = m_MathUtils.clip(mxx, 0, width);
        miz = m_MathUtils.clip(miz, 0, depth - 1);
        mxz = m_MathUtils.clip(mxz, 0, depth);
        gb_Vector3 offset = new gb_Vector3(width, 0, depth).mul(0.5f);
        float minX = (mix - offset.x) * scale.x;
        float minZ = (miz - offset.z) * scale.y;
        float maxX = (mxx - offset.x) * scale.x;
        float maxZ = (mxz - offset.z) * scale.y;
        for (int z = miz + 1; z < mxz; z++) {
        	gb_Vector3 a = new gb_Vector3(minX, groundLevel, (z - 1 - offset.z) * scale.y);
        	gb_Vector3 b = new gb_Vector3(minX, groundLevel, (z - offset.z) * scale.y);
            // left
            mesh.addFace(getVertexAtCell(mix, z - 1), getVertexAtCell(mix, z),
                    a);
            mesh.addFace(getVertexAtCell(mix, z), b, a);
            // right
            a.x = b.x = maxX - scale.x;
            mesh.addFace(getVertexAtCell(mxx - 1, z),
                    getVertexAtCell(mxx - 1, z - 1), b);
            mesh.addFace(getVertexAtCell(mxx - 1, z - 1), a, b);
        }
        for (int x = mix + 1; x < mxx; x++) {
        	gb_Vector3 a = new gb_Vector3((x - 1 - offset.x) * scale.x, groundLevel, minZ);
        	gb_Vector3 b = new gb_Vector3((x - offset.x) * scale.x, groundLevel, minZ);
            // back
            mesh.addFace(getVertexAtCell(x, miz), getVertexAtCell(x - 1, miz),
                    b);
            mesh.addFace(getVertexAtCell(x - 1, miz), a, b);
            // front
            a.z = b.z = maxZ - scale.y;
            mesh.addFace(getVertexAtCell(x - 1, mxz - 1),
                    getVertexAtCell(x, mxz - 1), a);
            mesh.addFace(getVertexAtCell(x, mxz - 1), b, a);
        }
        // bottom plane
        for (int z = miz + 1; z < mxz; z++) {
            for (int x = mix + 1; x < mxx; x++) {
            	gb_Vector3 a = getVertexAtCell(x - 1, z - 1).cpy();
            	gb_Vector3 b = getVertexAtCell(x, z - 1).cpy();
            	gb_Vector3 c = getVertexAtCell(x - 1, z).cpy();
            	gb_Vector3 d = getVertexAtCell(x, z).cpy();
                a.y = groundLevel;
                b.y = groundLevel;
                c.y = groundLevel;
                d.y = groundLevel;
                mesh.addFace(a, c, d);
                mesh.addFace(a, d, b);
            }
        }
        return mesh;
    }

    public gb_Terrain updateElevation() {
        for (int i = 0; i < elevation.length; i++) {
            vertices[i].y = elevation[i];
        }
        return this;
    }
}
