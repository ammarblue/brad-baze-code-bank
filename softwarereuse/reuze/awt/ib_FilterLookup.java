package reuze.awt;

import com.software.reuze.ib_a_FilterPoint;
import com.software.reuze.vc_Map256Gradient;
import com.software.reuze.vc_i_ColorMap;
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
 * A filter which uses the brightness of each pixel to lookup a color from a colormap. 
 */
public class ib_FilterLookup extends ib_a_FilterPoint {
	
	private vc_i_ColorMap colormap = new vc_Map256Gradient();
	
	public ib_FilterLookup() {
		canFilterIndexColorModel = true;
	}

	public ib_FilterLookup(vc_i_ColorMap colormap) {
		canFilterIndexColorModel = true;
		this.colormap = colormap;
	}

	public void setColormap(vc_i_ColorMap colormap) {
		this.colormap = colormap;
	}

	public vc_i_ColorMap getColormap() {
		return colormap;
	}

	public int filterRGB(int x, int y, int rgb) {
//		int a = rgb & 0xff000000;
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >> 8) & 0xff;
		int b = rgb & 0xff;
		rgb = (r + g + b) / 3;
		return colormap.getColor(rgb/255.0f);
	}

	public String toString() {
		return "Colors/Lookup...";
	}

}


