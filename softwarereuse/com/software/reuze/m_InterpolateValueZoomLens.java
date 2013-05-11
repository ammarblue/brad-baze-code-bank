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
 * This class provides an adjustable zoom lens to either bundle or dilate values
 * around a focal point within a given interval. For a example use cases, please
 * have a look at the provided ScaleMapDataViz and ZoomLens examples.
 */
public class m_InterpolateValueZoomLens implements m_i_InterpolateValue {

    protected m_InterpolateValueCircular leftImpl = new m_InterpolateValueCircular();
    protected m_InterpolateValueCircular rightImpl = new m_InterpolateValueCircular();

    protected float lensPos, smooth;
    protected float lensStrength, absStrength;

    public m_InterpolateValueZoomLens() {
        this(0.5f, 1);
    }

    public m_InterpolateValueZoomLens(float lensPos, float lensStrength) {
    	this.smooth=1;
        this.lensPos = lensPos;
        this.lensStrength = lensStrength;
        this.absStrength = m_MathUtils.abs(lensStrength);
        leftImpl.set("flip", lensStrength > 0?1f:0f);
        rightImpl.set("flip", lensStrength < 0?1f:0f);
    }

    public float interpolate(float min, float max, float t) {
        float val = min + (max - min) * t;
        if (t < lensPos) {
            val +=
                    (leftImpl.interpolate(min, min + (max - min) * lensPos, t
                            / lensPos) - val)
                            * absStrength;
        } else {
            val +=
                    (rightImpl.interpolate(min + (max - min) * lensPos, max,
                            (t - lensPos) / (1 - lensPos)) - val) * absStrength;
        }
        return val;
    }

    public void setLensPos(float position, float smooth) {
        //lensPos += (MathUtils.clipNormalized(position) - lensPos) * smooth;
    	lensPos = m_MathUtils.clipNormalized(position) * smooth;
    }

    public void setLensStrength(float strength, float smooth) {
        //lensStrength += (MathUtils.clip(strength, -1, 1) - lensStrength) * smooth;
    	lensStrength = m_MathUtils.clip(strength, -1, 1) * smooth;
        absStrength = m_MathUtils.abs(lensStrength);
        leftImpl.set("flip", lensStrength > 0?1f:0f);
        rightImpl.set("flip", lensStrength < 0?1f:0f);
    }
    /*set smooth first before strength or position*/
	public void set(String name, float value) {
		if (name.charAt(0)=='p') setLensPos(value, smooth);
		else if (name.charAt(1)=='t') setLensStrength(value, smooth);
		else {smooth=value;}
	}

	public float getFloat(String name) {
		if (name.charAt(0)=='p') return lensPos;
		if (name.charAt(1)=='t') return lensStrength;
		return smooth;
	}
}