package reuze.awt;

import com.software.reuze.ib_a_FilterTransform;
import com.software.reuze.m_MathUtils;
import com.software.reuze.z_BufferedImage;


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
 * This filter diffuses an image by moving its pixels in random directions.
 */
public class ib_FilterDiffuse extends ib_a_FilterTransform {

	public float[] sinTable, cosTable;
	public float scale = 4;
	
	public ib_FilterDiffuse() {
		setEdgeAction(CLAMP);
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getScale() {
		return scale;
	}

	protected void transformInverse(int x, int y, float[] out) {
		int angle = (int)(Math.random() * 255);
		float distance = (float)Math.random();
		out[0] = x + distance * sinTable[angle];
		out[1] = y + distance * cosTable[angle];
	}

    public z_BufferedImage filter( z_BufferedImage src, z_BufferedImage dst ) {
		sinTable = new float[256];
		cosTable = new float[256];
		for (int i = 0; i < 256; i++) {
			float angle = m_MathUtils.TWO_PI*i/256f;
			sinTable[i] = (float)(scale*Math.sin(angle));
			cosTable[i] = (float)(scale*Math.cos(angle));
		}
		return super.filter( src, dst );
	}

	public String toString() {
		return "Distort/Diffuse...";
	}
}
