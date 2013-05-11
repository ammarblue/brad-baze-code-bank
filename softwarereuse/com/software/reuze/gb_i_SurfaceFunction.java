package com.software.reuze;
import com.software.reuze.gb_SurfaceMeshBuilder;

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
 * This interface defines a functor for evaluating the coordinates of a surface
 * mesh used by {@link gb_SurfaceMeshBuilder}.
 * 
 * It is assumed the implementation creates vertices within the unit sphere
 * (normalized).
 */
public interface gb_i_SurfaceFunction {

    public gb_Vector3 computeVertexFor(gb_Vector3 p, float phi, float theta);

    public float getPhiRange();

    public int getPhiResolutionLimit(int res);

    public float getThetaRange();

    public int getThetaResolutionLimit(int res);
}
