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


public abstract class gb_a_VoxelSpace {

    protected static final Logger logger = Logger
            .getLogger(gb_a_VoxelSpace.class.getName());

    public final int resX, resY, resZ;
    public final int resX1, resY1, resZ1;

    public final int sliceRes;

    public final gb_Vector3 scale = new gb_Vector3();
    public final gb_Vector3 halfScale = new gb_Vector3();
    public final gb_Vector3 voxelSize = new gb_Vector3();

    public final int numCells;

    public gb_a_VoxelSpace(gb_Vector3 scale, int resX, int resY, int resZ) {
        this.resX = resX;
        this.resY = resY;
        this.resZ = resZ;
        resX1 = resX - 1;
        resY1 = resY - 1;
        resZ1 = resZ - 1;
        sliceRes = resX * resY;
        numCells = sliceRes * resZ;
        setScale(scale);
        logger.info("new space of " + resX + "x" + resY + "x" + resZ
                + " cells: " + numCells);
    }

    public abstract void clear();

    public void closeSides() {
        throw new UnsupportedOperationException(
                "This VolumetricSpace implementation does not support closeSides()");
    }

    public final int getIndexFor(int x, int y, int z) {
        x = m_MathUtils.clip(x, 0, resX1);
        y = m_MathUtils.clip(y, 0, resY1);
        z = m_MathUtils.clip(z, 0, resZ1);
        return x + y * resX + z * sliceRes;
    }

    public final gb_Vector3 getResolution() {
        return new gb_Vector3(resX, resY, resZ);
    }

    /**
     * @return the scale
     */
    public final gb_Vector3 getScale() {
        return scale.cpy();
    }

    public abstract float getVoxelAt(int index);

    /**
     * @param scale
     *            the scale to set
     */
    public final void setScale(gb_Vector3 scale) {
        this.scale.set(scale);
        this.halfScale.set(scale.tmp().mul(0.5f));
        voxelSize.set(scale.x / resX, scale.y / resY, scale.z / resZ);
    }

    public void setVoxelAt(int index, float value) {
        throw new UnsupportedOperationException(
                "This VolumetricSpace implementation does not support setVoxelAt()");
    }

    public void setVoxelAt(int x, int y, int z, float value) {
        throw new UnsupportedOperationException(
                "This VolumetricSpace implementation does not support setVoxelAt()");
    }
}
