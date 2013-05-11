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
 * An abstract superclass for filters which distort images in some way. The subclass only needs to override
 * two methods to provide the mapping between source and destination pixels.
 */
public abstract class ib_a_FilterTransform extends ib_a_Ops {

	public final static int ZERO = 0;
	public final static int CLAMP = 1;
	public final static int WRAP = 2;

	public final static int NEAREST_NEIGHBOUR = 0;
	public final static int BILINEAR = 1;

	protected int edgeAction = ZERO;
	protected int interpolation = BILINEAR;

	protected ga_Rectangle transformedSpace;
	protected ga_Rectangle originalSpace;

	public void setEdgeAction(int edgeAction) {
		this.edgeAction = edgeAction;
	}

	public int getEdgeAction() {
		return edgeAction;
	}
	
	public void setInterpolation(int interpolation) {
		this.interpolation = interpolation;
	}

	public int getInterpolation() {
		return interpolation;
	}
	
	protected abstract void transformInverse(int x, int y, float[] out);

	protected void transformSpace(ga_Rectangle rect) {
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

		if ( interpolation == NEAREST_NEIGHBOUR )
			return filterPixelsNN( dst, width, height, inPixels, transformedSpace );

		int srcWidth = width;
		int srcHeight = height;
		int srcWidth1 = width-1;
		int srcHeight1 = height-1;
		int outWidth = (int)transformedSpace.width; //RPC
		int outHeight = (int)transformedSpace.height;
		int outX, outY;
		int index = 0;
		int[] outPixels = new int[outWidth];

		outX = (int)transformedSpace.position.x; //RPC
		outY = (int)transformedSpace.position.y;
		float[] out = new float[2];

		for (int y = 0; y < outHeight; y++) {
			for (int x = 0; x < outWidth; x++) {
				transformInverse(outX+x, outY+y, out);
				int srcX = (int)Math.floor( out[0] );
				int srcY = (int)Math.floor( out[1] );
				float xWeight = out[0]-srcX;
				float yWeight = out[1]-srcY;
				int nw, ne, sw, se;

				if ( srcX >= 0 && srcX < srcWidth1 && srcY >= 0 && srcY < srcHeight1) {
					// Easy case, all corners are in the image
					int i = srcWidth*srcY + srcX;
					nw = inPixels[i];
					ne = inPixels[i+1];
					sw = inPixels[i+srcWidth];
					se = inPixels[i+srcWidth+1];
				} else {
					// Some of the corners are off the image
					nw = getPixel( inPixels, srcX, srcY, srcWidth, srcHeight );
					ne = getPixel( inPixels, srcX+1, srcY, srcWidth, srcHeight );
					sw = getPixel( inPixels, srcX, srcY+1, srcWidth, srcHeight );
					se = getPixel( inPixels, srcX+1, srcY+1, srcWidth, srcHeight );
				}
				outPixels[x] = i_MathImageUtils.bilinearInterpolate(xWeight, yWeight, nw, ne, sw, se); //RPC
			}
			setRGB( dst, 0, y, (int)transformedSpace.width, 1, outPixels ); //RPC
		}
		return dst;
	}

	final private int getPixel( int[] pixels, int x, int y, int width, int height ) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			switch (edgeAction) {
			case ZERO:
			default:
				return 0;
			case WRAP:
				return pixels[(m_MathUtils.mod(y, height) * width) + m_MathUtils.mod(x, width)];
			case CLAMP:
				return pixels[(m_MathUtils.clamp(y, 0, height-1) * width) + m_MathUtils.clamp(x, 0, width-1)];
			}
		}
		return pixels[ y*width+x ];
	}

	protected z_BufferedImage filterPixelsNN( z_BufferedImage dst, int width, int height, int[] inPixels, ga_Rectangle transformedSpace ) {
		int srcWidth = width;
		int srcHeight = height;
		int outWidth = (int)transformedSpace.width; //RPC
		int outHeight = (int)transformedSpace.height;
		int outX, outY, srcX, srcY;
		int[] outPixels = new int[outWidth];

		outX = (int)transformedSpace.position.x; //RPC
		outY = (int)transformedSpace.position.y;
		int[] rgb = new int[4];
		float[] out = new float[2];

		for (int y = 0; y < outHeight; y++) {
			for (int x = 0; x < outWidth; x++) {
				transformInverse(outX+x, outY+y, out);
				srcX = (int)out[0];
				srcY = (int)out[1];
				// int casting rounds towards zero, so we check out[0] < 0, not srcX < 0
				if (out[0] < 0 || srcX >= srcWidth || out[1] < 0 || srcY >= srcHeight) {
					int p;
					switch (edgeAction) {
					case ZERO:
					default:
						p = 0;
						break;
					case WRAP:
						p = inPixels[(m_MathUtils.mod(srcY, srcHeight) * srcWidth) + m_MathUtils.mod(srcX, srcWidth)];
						break;
					case CLAMP:
						p = inPixels[(m_MathUtils.clamp(srcY, 0, srcHeight-1) * srcWidth) + m_MathUtils.clamp(srcX, 0, srcWidth-1)];
						break;
					}
					outPixels[x] = p;
				} else {
					int i = srcWidth*srcY + srcX;
					rgb[0] = inPixels[i];
					outPixels[x] = inPixels[i];
				}
			}
			setRGB( dst, 0, y, (int)transformedSpace.width, 1, outPixels ); //RPC
		}
		return dst;
	}

}

