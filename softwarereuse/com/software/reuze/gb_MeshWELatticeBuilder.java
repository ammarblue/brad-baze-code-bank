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
import java.util.List;
import java.util.logging.Logger;


public class gb_MeshWELatticeBuilder {

    protected static final Logger logger = Logger
            .getLogger(gb_MeshWELatticeBuilder.class.getName());

    public static gb_WETriangleMesh build(gb_WETriangleMesh mesh, int res,
            float stroke) {
        return build(mesh, res, new m_RangeFloat(stroke, stroke));
    }

    public static gb_WETriangleMesh build(gb_WETriangleMesh mesh, int res,
            m_RangeFloat stroke) {
        gb_a_VoxelSpace volume = buildVolume(mesh, res, stroke);
        gb_i_IsoSurface surface = new gb_IsoSurfaceHash(volume);
        mesh = (gb_WETriangleMesh) surface.computeSurfaceMesh(new gb_WETriangleMesh(
                "iso", 300000, 900000), 0.2f);
        logger.info("created lattice mesh: " + mesh);
        return mesh;
    }

    public static gb_a_VoxelSpace buildVolume(gb_WETriangleMesh mesh, int res,
            float stroke) {
        return buildVolume(mesh, res, new m_RangeFloat(stroke, stroke));
    }

    public static gb_a_VoxelSpace buildVolume(gb_WETriangleMesh mesh, int res,
            m_RangeFloat stroke) {
        gb_MeshWELatticeBuilder builder = new gb_MeshWELatticeBuilder(mesh
                .getBoundingBox().getExtent().mul(2), res, res, res, stroke);
        return builder.buildVolume(mesh);
    }

    protected m_RangeInteger voxRangeX, voxRangeY, voxRangeZ;
    protected m_RangeFloat stroke;

    private gb_a_VoxelSpace volume;

    private float drawStep = 0.5f;

    private m_ScaleMap bboxToVoxelX;

    private m_ScaleMap bboxToVoxelY;
    private m_ScaleMap bboxToVoxelZ;

    public gb_MeshWELatticeBuilder(gb_Vector3 scale, int res, float stroke) {
        this(scale, res, res, res, new m_RangeFloat(stroke, stroke));
    }

    public gb_MeshWELatticeBuilder(gb_Vector3 scale, int resX, int resY, int resZ,
            m_RangeFloat stroke) {
        this.stroke = stroke;
        this.voxRangeX = new m_RangeInteger(1, resX - 2);
        this.voxRangeY = new m_RangeInteger(1, resY - 2);
        this.voxRangeZ = new m_RangeInteger(1, resZ - 2);
        volume = new gb_VoxelSpaceHashMap(scale, resX, resY, resZ, 0.1f);
    }

    public gb_WETriangleMesh buildLattice(gb_WETriangleMesh mesh, gb_i_Mesh targetMesh,
            float isoValue) {
        if (volume == null) {
            volume = buildVolume(mesh);
        }
        if (targetMesh == null) {
            targetMesh = new gb_WETriangleMesh();
        }
        gb_i_IsoSurface surface = new gb_IsoSurfaceHash(volume);
        mesh = (gb_WETriangleMesh) surface
                .computeSurfaceMesh(targetMesh, isoValue);
        return mesh;
    }

    public gb_a_VoxelSpace buildVolume(gb_WETriangleMesh mesh) {
        gb_a_VoxelSpaceBrush brush = new gb_VoxelSpaceBrushRound(volume, 1);
        brush.setMode(gb_a_VoxelSpaceBrush.MODE_PEAK);
        return buildVolume(mesh, brush);
    }

    public gb_a_VoxelSpace buildVolume(final gb_WETriangleMesh mesh,
            final gb_a_VoxelSpaceBrush brush) {
        logger.info("creating lattice...");
        setMesh(mesh);
        List<Float> edgeLengths = new ArrayList<Float>(mesh.edges.size());
        for (gb_WEWingedEdge e : mesh.edges.values()) {
            edgeLengths.add(e.getLength());
        }
        m_RangeFloat range = m_RangeFloat.fromSamples(edgeLengths);
        m_ScaleMap brushSize = new m_ScaleMap(range.min, range.max, stroke.min,
                stroke.max);
        for (gb_WEWingedEdge e : mesh.edges.values()) {
            brush.setSize((float) brushSize.getClippedValueFor(e.getLength()));
            createLattice(brush, e, drawStep);
        }
        volume.closeSides();
        return volume;
    }

    public void createLattice(gb_a_VoxelSpaceBrush brush, gb_Line3D l, float drawStep) {
        List<gb_Vector3> points = l.splitIntoSegments(null, drawStep, true);
        for (gb_Vector3 p : points) {
            float x = (float) bboxToVoxelX.getClippedValueFor(p.x);
            float y = (float) bboxToVoxelY.getClippedValueFor(p.y);
            float z = (float) bboxToVoxelZ.getClippedValueFor(p.z);
            brush.drawAtGridPos(x, y, z, 1);
        }
    }

    /**
     * @return the drawStep
     */
    public float getDrawStep() {
        return drawStep;
    }

    /**
     * @return the volume
     */
    public gb_a_VoxelSpace getVolume() {
        return volume;
    }

    /**
     * Sets the distance between {@link gb_a_VoxelSpaceBrush} positions when tracing
     * mesh edges.
     * 
     * @param drawStep
     *            the drawStep to set
     */
    public void setDrawStepLength(float drawStep) {
        this.drawStep = drawStep;
    }

    public void setInputBounds(gb_AABB3 box) {
        gb_Vector3 bmin = box.getMin();
        gb_Vector3 bmax = box.getMax();
        bboxToVoxelX = new m_ScaleMap(bmin.x, bmax.x, voxRangeX.min,
                voxRangeX.max);
        bboxToVoxelY = new m_ScaleMap(bmin.y, bmax.y, voxRangeY.min,
                voxRangeY.max);
        bboxToVoxelZ = new m_ScaleMap(bmin.z, bmax.z, voxRangeZ.min,
                voxRangeZ.max);
    }

    public void setMesh(gb_WETriangleMesh mesh) {
        setInputBounds(mesh.getBoundingBox());
    }

    protected void setRangeMinMax(m_RangeInteger range, int min, int max,
            int maxRes) {
        // swap if necessary...
        if (min > max) {
            max ^= min;
            min ^= max;
            max ^= min;
        }
        if (min < 0 || min >= maxRes || max < 0 || max >= maxRes) {
            throw new IllegalArgumentException(
                    "voxel range min/max is out of bounds: " + min + "->" + max);
        }
        range.min = min;
        range.max = max;
    }

    /**
     * @param volume
     *            the volume to set
     */
    public void setVolume(gb_a_VoxelSpace volume) {
        this.volume = volume;
    }

    public gb_MeshWELatticeBuilder setVoxelRangeX(int min, int max) {
        setRangeMinMax(voxRangeX, min, max, volume.resX);
        return this;
    }

    public gb_MeshWELatticeBuilder setVoxelRangeY(int min, int max) {
        setRangeMinMax(voxRangeY, min, max, volume.resY);
        return this;
    }

    public gb_MeshWELatticeBuilder setVoxelRangeZ(int min, int max) {
        setRangeMinMax(voxRangeZ, min, max, volume.resZ);
        return this;
    }
}
