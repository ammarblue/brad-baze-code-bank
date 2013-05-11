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
 * Implementation of the circular interpolation function.
 * 
 * i = a-(b-a) * (sqrt(1 - (1 - f) * (1 - f) ))
 */
public class m_InterpolateValueCircular implements m_i_InterpolateValue {

    protected boolean isFlipped;

    public m_InterpolateValueCircular() {
        this(false);
    }

    /**
     * The interpolation slope can be flipped to have its steepest ascent
     * towards the end value, rather than at the beginning in the default
     * configuration.
     * 
     * @param isFlipped
     *            true, if slope is inverted
     */
    public m_InterpolateValueCircular(boolean isFlipped) {
        this.isFlipped = isFlipped;
    }

    public float interpolate(float a, float b, float f) {
        if (isFlipped) {
            return a - (b - a) * ((float) Math.sqrt(1 - f * f) - 1);
        } else {
            f = 1 - f;
            return a + (b - a) * ((float) Math.sqrt(1 - f * f));
        }
    }

	public void set(String name, float value) {
		isFlipped = value!=0f;
	}

	public float getFloat(String name) {
		return isFlipped?1f:0f;
	}
}
