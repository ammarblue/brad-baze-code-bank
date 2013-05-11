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
 * Implements the sigmoid interpolation function with adjustable curve sharpness
 */
public class m_InterpolateValueSigmoid implements m_i_InterpolateValue {

    private float sharpness;

    /**
     * Initializes the s-curve with default sharpness = 2
     */
    public m_InterpolateValueSigmoid() {
        sharpness=2f*5;
    }

    public m_InterpolateValueSigmoid(float s) {
        sharpness=s;
    }

    public float interpolate(float a, float b, float f) {
        f = (f * 2 - 1) * sharpness;
        f = (float) (1.0f / (1.0f + Math.exp(-f)));
        return a + (b - a) * f;
    }

	public void set(String name, float value) {
		sharpness = value;
	}

	public float getFloat(String name) {
		return sharpness;
	}
}
