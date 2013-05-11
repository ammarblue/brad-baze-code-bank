package com.software.reuze;

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
 * A colormap implemented with an array of colors. This corresponds to the IndexColorModel class.
 */
public class vc_Map256 implements vc_i_ColorMap, Cloneable, java.io.Serializable {

	static final long serialVersionUID = -7990431442314209043L;
	
	/**
	 * The array of colors.
	 */
	protected int[] map;

	/**
	 * Construct an all-black colormap
	 */
	public vc_Map256() {
		this.map = new int[256];
	}

	/**
	 * Construct a colormap with the given map
	 * @param map the array of ARGB colors
	 */
	public vc_Map256(int[] map) {
		this.map = map;
	}

	public Object clone() {
		try {
			vc_Map256 g = (vc_Map256)super.clone();
			g.map = (int[])map.clone();
			return g;
		}
		catch (CloneNotSupportedException e) {
		}
		return null;
	}
	
	public void setMap(int[] map) {
		this.map = map;
	}

	public int[] getMap() {
		return map;
	}

	/**
	 * Convert a value in the range 0..1 to an RGB color.
	 * @param v a value in the range 0..1
	 * @return an RGB color
	 */
	public int getColor(float v) {
/*
		v *= 255;
		int n = (int)v;
		float f = v-n;
		if (n < 0)
			return map[0];
		else if (n >= 255)
			return map[255];
		return ImageMath.mixColors(f, map[n], map[n+1]);
*/
		int n = (int)(v*255);
		if (n < 0)
			n = 0;
		else if (n > 255)
			n = 255;
		return map[n];
	}
	
	/**
	 * Set the color at "index" to "color". Entries are interpolated linearly from
	 * the existing entries at "firstIndex" and "lastIndex" to the new entry.
	 * firstIndex < index < lastIndex must hold.
	 */
	public void setColorInterpolated(int index, int firstIndex, int lastIndex, int color) {
		int firstColor = map[firstIndex];
		int lastColor = map[lastIndex];
		for (int i = firstIndex; i <= index; i++)
			map[i] = i_MathImageUtils.mixColors((float)(i-firstIndex)/(index-firstIndex), firstColor, color);
		for (int i = index; i < lastIndex; i++)
			map[i] = i_MathImageUtils.mixColors((float)(i-index)/(lastIndex-index), color, lastColor);
	}

	public void setColorRange(int firstIndex, int lastIndex, int color1, int color2) {
		for (int i = firstIndex; i <= lastIndex; i++)
			map[i] = i_MathImageUtils.mixColors((float)(i-firstIndex)/(lastIndex-firstIndex), color1, color2);
	}

	public void setColorRange(int firstIndex, int lastIndex, int color) {
		for (int i = firstIndex; i <= lastIndex; i++)
			map[i] = color;
	}

	public void setColor(int index, int color) {
		map[index] = color;
	}

}
