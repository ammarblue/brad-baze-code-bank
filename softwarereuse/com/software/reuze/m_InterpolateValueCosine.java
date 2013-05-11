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
 * Implementation of the cosine interpolation function:
 * 
 * i = b+(a-b)*(0.5+0.5*cos(f*PI))
 */
public class m_InterpolateValueCosine implements m_i_InterpolateValue {

    /*
     * (non-Javadoc)
     * 
     * @see toxi.math.InterpolateStrategy#interpolate(float, float, float)
     */
    public final float interpolate(float a, float b, float f) {
        return b + (a - b) * (float) (0.5 + 0.5 * Math.cos(f * m_MathUtils.PI));
    }

	public void set(String name, float value) {
	}

	public float getFloat(String name) {
		return Float.NaN;
	}

}