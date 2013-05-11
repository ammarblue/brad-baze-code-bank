package reuze.awt;

import com.software.reuze.ib_a_FilterPoint;
import com.software.reuze.m_MathUtils;

/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

public class ib_FilterRGBAdjust extends ib_a_FilterPoint implements java.io.Serializable {
	
	public float rFactor, gFactor, bFactor;

	public ib_FilterRGBAdjust() {
		this(0, 0, 0);
	}

	public ib_FilterRGBAdjust(float r, float g, float b) {
		rFactor = 1+r;
		gFactor = 1+g;
		bFactor = 1+b;
		canFilterIndexColorModel = true;
	}

	public void setRFactor( float rFactor ) {
		this.rFactor = 1+rFactor;
	}
	
	public float getRFactor() {
		return rFactor-1;
	}
	
	public void setGFactor( float gFactor ) {
		this.gFactor = 1+gFactor;
	}
	
	public float getGFactor() {
		return gFactor-1;
	}
	
	public void setBFactor( float bFactor ) {
		this.bFactor = 1+bFactor;
	}
	
	public float getBFactor() {
		return bFactor-1;
	}

	public int[] getLUT() {
		int[] lut = new int[256];
		for ( int i = 0; i < 256; i++ ) {
			lut[i] = filterRGB( 0, 0, (i << 24) | (i << 16) | (i << 8) | i );
		}
		return lut;
	}
	
	public int filterRGB(int x, int y, int rgb) {
		int a = rgb & 0xff000000;
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >> 8) & 0xff;
		int b = rgb & 0xff;
		r = m_MathUtils.clampToByte((int)(r * rFactor));
		g = m_MathUtils.clampToByte((int)(g * gFactor));
		b = m_MathUtils.clampToByte((int)(b * bFactor));
		return a | (r << 16) | (g << 8) | b;
	}

	public String toString() {
		return "Colors/Adjust RGB...";
	}
}

