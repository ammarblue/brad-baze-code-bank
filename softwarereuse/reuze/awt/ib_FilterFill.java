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
 * A filter which fills an image with a given color. Normally you would just call Graphics.fillRect but it can sometimes be useful
 * to go via a filter to fit in with an existing API.
 */
public class ib_FilterFill extends ib_a_FilterPoint {

	private int fillColor;

	public ib_FilterFill() {
		this(0xff000000);
	}

	public ib_FilterFill(int color) {
		this.fillColor = color;
	}

	public void setFillColor(int fillColor) {
		this.fillColor = fillColor;
	}

	public int getFillColor() {
		return fillColor;
	}

	public int filterRGB(int x, int y, int rgb) {
		return fillColor;
	}
}

