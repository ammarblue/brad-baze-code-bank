package reuze.awt;

import com.software.reuze.ga_Rectangle;
import com.software.reuze.ib_a_FilterWholeImage;
import com.software.reuze.m_MathUtils;
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

public class ib_FilterContour extends ib_a_FilterWholeImage {

	private float levels = 5;
	private float scale = 1;
	private float offset = 0;
	private int contourColor = 0xff000000;
	
	public ib_FilterContour() {
	}

	public void setLevels( float levels ) {
		this.levels = levels;
	}
	
	public float getLevels() {
		return levels;
	}
	
	public void setScale( float scale ) {
		this.scale = scale;
	}
	
	public float getScale() {
		return scale;
	}
	
	public void setOffset( float offset ) {
		this.offset = offset;
	}
	
	public float getOffset() {
		return offset;
	}
	
	protected int[] filterPixels( int width, int height, int[] inPixels, ga_Rectangle transformedSpace ) {
		int index = 0;
		short[][] r = new short[3][width];
		int[] outPixels = new int[width * height];

		short[] table = new short[256];
		int offsetl = (int)(offset * 256 / levels);
		for ( int i = 0; i < 256; i++ )
			table[i] = (short)m_MathUtils.clampToByte( (int)(255 * Math.floor(levels*(i+offsetl) / 256) / (levels-1) - offsetl) );

		for (int x = 0; x < width; x++) {
			int rgb = inPixels[x];
			r[1][x] = (short)vc_ColorOps.brightness( rgb );
		}
		for (int y = 0; y < height; y++) {
			boolean yIn = y > 0 && y < height-1;
			int nextRowIndex = index+width;
			if ( y < height-1) {
				for (int x = 0; x < width; x++) {
					int rgb = inPixels[nextRowIndex++];
					r[2][x] = (short)vc_ColorOps.brightness( rgb );
				}
			}
			for (int x = 0; x < width; x++) {
				boolean xIn = x > 0 && x < width-1;
				int w = x-1;
				int e = x+1;
				int v = 0;
				
				if ( yIn && xIn ) {
					short nwb = r[0][w];
					short neb = r[0][x];
					short swb = r[1][w];
					short seb = r[1][x];
					short nw = table[nwb];
					short ne = table[neb];
					short sw = table[swb];
					short se = table[seb];

					if (nw != ne || nw != sw || ne != se || sw != se) {
						v = (int)(scale * (Math.abs(nwb - neb) + Math.abs(nwb - swb) + Math.abs(neb - seb) + Math.abs(swb - seb)));
//						v /= 255;
						if (v > 255)
							v = 255;
					}
				}

				if ( v != 0 )
					outPixels[index] = vc_ColorOps.combinePixels( inPixels[index], contourColor, vc_ColorOps.NORMAL, v );
				else
					outPixels[index] = inPixels[index];
				index++;
			}
			short[] t;
			t = r[0];
			r[0] = r[1];
			r[1] = r[2];
			r[2] = t;
		}
	
		return outPixels;
	}

	public String toString() {
		return "Stylize/Contour...";
	}

}

