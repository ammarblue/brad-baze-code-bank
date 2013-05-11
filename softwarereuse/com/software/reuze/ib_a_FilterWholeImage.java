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
 * A filter which acts as a superclass for filters which need to have the whole image in memory
 * to do their stuff.
 */
public abstract class ib_a_FilterWholeImage extends ib_a_Ops implements java.io.Serializable {

	protected ga_Rectangle transformedSpace;
	protected ga_Rectangle originalSpace;
	
	/**
	 * Construct a WholeImageFilter
	 */
	public ib_a_FilterWholeImage() {
	}

    public z_BufferedImage filter( z_BufferedImage src, z_BufferedImage dst ) {
        int width = src.getWidth();
        int height = src.getHeight();
		int type = src.getType();
		//WritableRaster srcRaster = src.getRaster();

		originalSpace = new ga_Rectangle(0, 0, width, height);
		transformedSpace = new ga_Rectangle(0, 0, width, height);
		transformSpace(transformedSpace);

        if ( dst == null ) {
            z_ColorModel dstCM = src.getColorModel(null);
			dst = new z_BufferedImage(dstCM, (int)transformedSpace.width, (int)transformedSpace.height); //RPC
		}
		//WritableRaster dstRaster = dst.getRaster();

		int[] inPixels = getRGB( src, 0, 0, width, height, null );
		inPixels = filterPixels( width, height, inPixels, transformedSpace );
		setRGB( dst, 0, 0, (int)transformedSpace.width, (int)transformedSpace.height, inPixels ); //RPC

        return dst;
    }

	protected void transformSpace(ga_Rectangle rect) {
	}
	
	protected abstract int[] filterPixels( int width, int height, int[] inPixels, ga_Rectangle transformedSpace );
}

