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
public class gb_CylinderYAxisAligned extends gb_a_CylinderAxisAligned {

    public gb_CylinderYAxisAligned(gb_Vector3 pos, float radius, float length) {
        super(pos, radius, length);
    }

    public boolean containsPoint(gb_Vector3 p) {
        if (m_MathUtils.abs(p.y - pos.y) < length * 0.5) {
            float dx = p.x - pos.x;
            float dz = p.z - pos.z;
            if (Math.abs(dz * dz + dx * dx) < radiusSquared) {
                return true;
            }
        }
        return false;
    }

    public gb_Vector3 getMajorAxis() {
        return gb_Vector3.Y;
    }

}
