package reuze.awt;

import com.software.reuze.ib_a_FilterPoint;
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
 * Sets the opacity (alpha) of every pixel in an image to a constant value.
 */
public class ib_FilterOpacity extends ib_a_FilterPoint implements java.io.Serializable {

	static final long serialVersionUID = 5644263685527598345L;
	
	private int opacity;
	private int opacity24;

	/**
	 * Construct an OpacityFilter with 50% opacity.
	 */
	public ib_FilterOpacity() {
		this(0x88);
	}

	/**
	 * Construct an OpacityFilter with the given opacity (alpha).
	 */
	public ib_FilterOpacity(int opacity) {
		setOpacity(opacity);
	}

	/**
	 * Set the opacity.
	 * @param opacity the opacity (alpha) in the range 0..255
	 */
	public void setOpacity(int opacity) {
		this.opacity = opacity;
		opacity24 = opacity << 24;
	}
	
	/**
	 * Get the opacity setting.
	 * @return the opacity
	 */
	public int getOpacity() {
		return opacity;
	}
	
	public int filterRGB(int x, int y, int rgb) {
		if ((rgb & 0xff000000) != 0)
			return (rgb & 0xffffff) | opacity24;
		return rgb;
	}

	public String toString() {
		return "Colors/Transparency...";
	}

}

