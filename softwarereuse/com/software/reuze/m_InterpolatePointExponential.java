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
 * Exponential curve interpolation with adjustable exponent. Use exp in the
 * following ranges to achieve these effects:
 * <ul>
 * <li>0.0 &lt; x &lt; 1.0 : ease in (steep changes towards b)</li>
 * <li>1.0 : same as {@link LinearInterpolation}</li>
 * <li>&gt; 1.0 : ease-out (steep changes from a)</li>
 * </ul>
 */
public class m_InterpolatePointExponential implements m_i_InterpolateValue {

    private float exponent;

    /**
     * Default constructor uses square parabola (exp=2)
     */
    public m_InterpolatePointExponential() {
        this(2);
    }

    /**
     * @param exp
     *            curve exponent
     */
    public m_InterpolatePointExponential(float exp) {
        this.exponent = exp;
    }

    public float interpolate(float a, float b, float f) {
        return a + (b - a) * (float) Math.pow(f, exponent);
    }

	public void set(String name, float value) {
		exponent=value;
	}

	public float getFloat(String name) {
		return exponent;
	}

}
