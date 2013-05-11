package reuze.awt;

import com.software.reuze.ib_a_FilterTransform;
import com.software.reuze.z_Point2D;

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
 * A Filter to pixelate images.
 */
public class ib_FilterBlock extends ib_a_FilterTransform {

	static final long serialVersionUID = 8077109551486196569L;
	
	private int blockSize = 2;

	/**
	 * Set the pixel block size
	 * @param blockSize the number of pixels along each block edge
	 */
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	/**
	 * Get the pixel block size
	 * @return the number of pixels along each block edge
	 */
	public int getBlockSize() {
		return blockSize;
	}


	public ib_FilterBlock() {
	}

	protected void transform(int x, int y, z_Point2D out) {
		out.x = (x / blockSize) * blockSize;
		out.y = (y / blockSize) * blockSize;
	}

	protected void transformInverse(int x, int y, float[] out) {
		out[0] = (x / blockSize) * blockSize;
		out[1] = (y / blockSize) * blockSize;
	}

	public String toString() {
		return "Stylize/Mosaic...";
	}
}

