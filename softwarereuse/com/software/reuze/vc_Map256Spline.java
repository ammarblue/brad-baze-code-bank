package com.software.reuze;
import java.io.Serializable;


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

/**
 * A Colormap implemented using Catmull-Rom color splines. The map has a variable number
 * of knots with a minimum of four. The first and last knots give the tangent at the end
 * of the spline, and colors are interpolated from the second to the second-last knots.
 */
public class vc_Map256Spline extends vc_Map256 implements Serializable {

	public int numKnots = 4;
    public int[] xKnots = {
    	0, 0, 255, 255
    };
    public int[] yKnots = {
    	0xff000000, 0xff000000, 0xffffffff, 0xffffffff,
    };
	
	public vc_Map256Spline() {
		rebuildGradient();
	}

	public vc_Map256Spline(int[] xKnots, int[] yKnots) {
		this.xKnots = xKnots;
		this.yKnots = yKnots;
		numKnots = xKnots.length;
		rebuildGradient();
	}

	public int getKnot(int n) {
		return yKnots[n];
	}

	public void setKnot(int n, int color) {
		yKnots[n] = color;
		rebuildGradient();
	}
	
	public void addKnot(int x, int color) {
		int[] nx = new int[numKnots+1];
		int[] ny = new int[numKnots+1];
		System.arraycopy(xKnots, 0, nx, 0, numKnots);
		System.arraycopy(yKnots, 0, ny, 0, numKnots);
		xKnots = nx;
		yKnots = ny;
		xKnots[numKnots] = x;
		yKnots[numKnots] = color;
		numKnots++;
		sortKnots();
		rebuildGradient();
	}
	
	public void removeKnot(int n) {
		if (numKnots <= 4)
			return;
		if (n < numKnots-1) {
			System.arraycopy(xKnots, n+1, xKnots, n, numKnots-n-1);
			System.arraycopy(yKnots, n+1, yKnots, n, numKnots-n-1);
		}
		numKnots--;
		rebuildGradient();
	}
	
	public void setKnotPosition(int n, int x) {
		xKnots[n] = m_MathUtils.clampToByte(x);
		sortKnots();
		rebuildGradient();
	}

	private void rebuildGradient() {
		xKnots[0] = -1;
		xKnots[numKnots-1] = 256;
		yKnots[0] = yKnots[1];
		yKnots[numKnots-1] = yKnots[numKnots-2];
		for (int i = 0; i < 256; i++)
			map[i] = i_MathImageUtils.colorSpline(i, numKnots, xKnots, yKnots);
	}

	private void sortKnots() {
		for (int i = 1; i < numKnots; i++) {
			for (int j = 1; j < i; j++) {
				if (xKnots[i] < xKnots[j]) {
					int t = xKnots[i];
					xKnots[i] = xKnots[j];
					xKnots[j] = t;
					t = yKnots[i];
					yKnots[i] = yKnots[j];
					yKnots[j] = t;
				}
			}
		}
	}

}
