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



import java.util.logging.Logger;

public abstract class gb_a_VoxelSpaceBrush {

    protected static final Logger logger = Logger
            .getLogger(gb_a_VoxelSpaceBrush.class.getName());

    public static final m_i_FloatOpBinary MODE_ADDITIVE = new m_FloatOpAdd();
    public static final m_i_FloatOpBinary MODE_MULTIPLY = new m_FloatOpMultiply();
    public static final m_i_FloatOpBinary MODE_REPLACE = new m_FloatOpReplace();
    public static final m_i_FloatOpBinary MODE_PEAK = new m_FloatOpMax();

    protected gb_a_VoxelSpace volume;
    protected int cellRadiusX, cellRadiusY, cellRadiusZ;
    protected float stretchY, stretchZ;

    protected m_i_FloatOpBinary brushMode = MODE_ADDITIVE;

    public gb_a_VoxelSpaceBrush(gb_a_VoxelSpace volume) {
        this.volume = volume;
    }
    public String toString() {
    	return cellRadiusX+","+cellRadiusY+","+cellRadiusZ+","+stretchY+","+stretchZ;
    }
    public void drawAtAbsolutePos(gb_Vector3 pos, float density) {
        float cx = m_MathUtils.clip((pos.x + volume.halfScale.x) / volume.scale.x
                * volume.resX1, 0, volume.resX1);
        float cy = m_MathUtils.clip((pos.y + volume.halfScale.y) / volume.scale.y
                * volume.resY1, 0, volume.resY1);
        float cz = m_MathUtils.clip((pos.z + volume.halfScale.z) / volume.scale.z
                * volume.resZ1, 0, volume.resZ1);
        drawAtGridPos(cx, cy, cz, density);
    }

    public abstract void drawAtGridPos(float cx, float cy, float cz,
            float density);

    public void setMode(m_i_FloatOpBinary mode) {
        brushMode = mode;
    }

    public abstract void setSize(float radius);

    protected final void updateVoxel(int x, int y, int z, float cellVal) {
        int idx = volume.getIndexFor(x, y, z);
        volume.setVoxelAt(idx, brushMode.apply(volume.getVoxelAt(idx), cellVal));
    }
}