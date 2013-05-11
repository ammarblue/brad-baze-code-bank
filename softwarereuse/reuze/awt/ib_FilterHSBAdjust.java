package reuze.awt;

import com.software.reuze.ib_a_FilterPoint;
import com.software.reuze.z_Colors;

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

public class ib_FilterHSBAdjust extends ib_a_FilterPoint {
	
	public float hFactor, sFactor, bFactor;
	private float[] hsb = new float[3];
	
	public ib_FilterHSBAdjust() {
		this(0, 0, 0);
	}

	public ib_FilterHSBAdjust(float r, float g, float b) {
		hFactor = r;
		sFactor = g;
		bFactor = b;
		canFilterIndexColorModel = true;
	}

	public void setHFactor( float hFactor ) {
		this.hFactor = hFactor;
	}
	
	public float getHFactor() {
		return hFactor;
	}
	
	public void setSFactor( float sFactor ) {
		this.sFactor = sFactor;
	}
	
	public float getSFactor() {
		return sFactor;
	}
	
	public void setBFactor( float bFactor ) {
		this.bFactor = bFactor;
	}
	
	public float getBFactor() {
		return bFactor;
	}
	
	public int filterRGB(int x, int y, int rgb) {
		int a = rgb & 0xff000000;
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >> 8) & 0xff;
		int b = rgb & 0xff;
		z_Colors.RGBtoHSB(r, g, b, hsb);
		hsb[0] += hFactor;
		while (hsb[0] < 0)
			hsb[0] += Math.PI*2;
		hsb[1] += sFactor;
		if (hsb[1] < 0)
			hsb[1] = 0;
		else if (hsb[1] > 1.0)
			hsb[1] = 1.0f;
		hsb[2] += bFactor;
		if (hsb[2] < 0)
			hsb[2] = 0;
		else if (hsb[2] > 1.0)
			hsb[2] = 1.0f;
		rgb = z_Colors.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
		return a | (rgb & 0xffffff);
	}

	public String toString() {
		return "Colors/Adjust HSB...";
	}
}

