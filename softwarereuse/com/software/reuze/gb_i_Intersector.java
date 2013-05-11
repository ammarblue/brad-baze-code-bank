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



/**
 * Generic interface for ray intersection with 3D geometry
 */
public interface gb_i_Intersector {

    /**
     * @return intersection data parcel
     */
    public gb_IntersectionData getIntersectionData();

    /**
     * Checks if entity intersects with the given ray. Further intersection
     * details can then be queried via the {@link gb_IntersectionData} instance returned
     * by {@link #getIntersectionData()}.
     * 
     * @param ray
     *            ray to check
     * @return true, if ray hits the entity
     */
    public boolean intersectsRay(gb_Ray ray);

}