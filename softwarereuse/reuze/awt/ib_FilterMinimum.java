package reuze.awt;

import com.software.reuze.ga_Rectangle;
import com.software.reuze.ib_a_FilterWholeImage;
import com.software.reuze.vc_ColorOps;

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

public class ib_FilterMinimum extends ib_a_FilterWholeImage {

	static final long serialVersionUID = 1925266438370819998L;
	
	public ib_FilterMinimum() {
	}
	
	protected int[] filterPixels( int width, int height, int[] inPixels, ga_Rectangle transformedSpace ) {
		int index = 0;
		int[] outPixels = new int[width * height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = 0xffffffff;
				for (int dy = -1; dy <= 1; dy++) {
					int iy = y+dy;
					int ioffset;
					if (0 <= iy && iy < height) {
						ioffset = iy*width;
						for (int dx = -1; dx <= 1; dx++) {
							int ix = x+dx;
							if (0 <= ix && ix < width) {
								pixel = vc_ColorOps.combinePixels(pixel, inPixels[ioffset+ix], vc_ColorOps.MIN);
							}
						}
					}
				}
				outPixels[index++] = pixel;
			}
		}
		return outPixels;
	}

	public String toString() {
		return "Blur/Minimum";
	}

}

