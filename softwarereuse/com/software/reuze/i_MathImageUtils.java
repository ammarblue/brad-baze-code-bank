package com.software.reuze;


public class i_MathImageUtils {
	/**
	 * The triangle function. Returns a repeating triangle shape in the range 0..1 with wavelength 1.0
	 * @param x the input parameter
	 * @return the output value
	 */
	public static float triangle(float x) {
		float r = m_MathUtils.mod(x, 1.0f);
		return 2.0f*(r < 0.5 ? r : 1-r);
	}
	/**
	 * Linear interpolation of ARGB values.
	 * @param t the interpolation parameter
	 * @param rgb1 the lower interpolation range
	 * @param rgb2 the upper interpolation range
	 * @return the interpolated value
	 */
	public static int mixColors(float t, int rgb1, int rgb2) {
		int a1 = (rgb1 >> 24) & 0xff;
		int r1 = (rgb1 >> 16) & 0xff;
		int g1 = (rgb1 >> 8) & 0xff;
		int b1 = rgb1 & 0xff;
		int a2 = (rgb2 >> 24) & 0xff;
		int r2 = (rgb2 >> 16) & 0xff;
		int g2 = (rgb2 >> 8) & 0xff;
		int b2 = rgb2 & 0xff;
		a1 = m_InterpolateLerp.lerp(t, a1, a2);
		r1 = m_InterpolateLerp.lerp(t, r1, r2);
		g1 = m_InterpolateLerp.lerp(t, g1, g2);
		b1 = m_InterpolateLerp.lerp(t, b1, b2);
		return (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
	}

	/**
	 * Bilinear interpolation of ARGB values.
	 * @param x the X interpolation parameter 0..1
	 * @param y the y interpolation parameter 0..1
	 * @param rgb array of four ARGB values in the order NW, NE, SW, SE
	 * @return the interpolated value
	 */
	public static int bilinearInterpolate(float x, float y, int... p) {
		float m0, m1;
		int a0 = (p[0] >> 24) & 0xff;
		int r0 = (p[0] >> 16) & 0xff;
		int g0 = (p[0] >> 8) & 0xff;
		int b0 = p[0] & 0xff;
		int a1 = (p[1] >> 24) & 0xff;
		int r1 = (p[1] >> 16) & 0xff;
		int g1 = (p[1] >> 8) & 0xff;
		int b1 = p[1] & 0xff;
		int a2 = (p[2] >> 24) & 0xff;
		int r2 = (p[2] >> 16) & 0xff;
		int g2 = (p[2] >> 8) & 0xff;
		int b2 = p[2] & 0xff;
		int a3 = (p[3] >> 24) & 0xff;
		int r3 = (p[3] >> 16) & 0xff;
		int g3 = (p[3] >> 8) & 0xff;
		int b3 = p[3] & 0xff;

		float cx = 1.0f-x;
		float cy = 1.0f-y;

		m0 = cx * a0 + x * a1;
		m1 = cx * a2 + x * a3;
		int a = (int)(cy * m0 + y * m1);

		m0 = cx * r0 + x * r1;
		m1 = cx * r2 + x * r3;
		int r = (int)(cy * m0 + y * m1);

		m0 = cx * g0 + x * g1;
		m1 = cx * g2 + x * g3;
		int g = (int)(cy * m0 + y * m1);

		m0 = cx * b0 + x * b1;
		m1 = cx * b2 + x * b3;
		int b = (int)(cy * m0 + y * m1);

		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	/**
	 * Return the NTSC gray level of an RGB value.
	 * @param rgb1 the input pixel
	 * @return the gray level (0-255)
	 */
	public static int brightnessNTSC(int rgb) {
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >> 8) & 0xff;
		int b = rgb & 0xff;
		return (int)(r*0.299f + g*0.587f + b*0.114f);
	}
	
	// Catmull-Rom splines
	private final static float m00 = -0.5f;
	private final static float m01 =  1.5f;
	private final static float m02 = -1.5f;
	private final static float m03 =  0.5f;
	private final static float m10 =  1.0f;
	private final static float m11 = -2.5f;
	private final static float m12 =  2.0f;
	private final static float m13 = -0.5f;
	private final static float m20 = -0.5f;
	private final static float m21 =  0.0f;
	private final static float m22 =  0.5f;
	private final static float m23 =  0.0f;
	private final static float m30 =  0.0f;
	private final static float m31 =  1.0f;
	private final static float m32 =  0.0f;
	private final static float m33 =  0.0f;

	/**
	 * Compute a Catmull-Rom spline.
	 * @param x the input parameter
	 * @param numKnots the number of knots in the spline
	 * @param knots the array of knots
	 * @return the spline value
	 */
	public static float spline(float x, int numKnots, float[] knots) {
		int span;
		int numSpans = numKnots - 3;
		float k0, k1, k2, k3;
		float c0, c1, c2, c3;
		
		if (numSpans < 1)
			throw new IllegalArgumentException("Too few knots in spline");
		
		x = m_MathUtils.clamp(x, 0, 1) * numSpans;
		span = (int)x;
		if (span > numKnots-4)
			span = numKnots-4;
		x -= span;

		k0 = knots[span];
		k1 = knots[span+1];
		k2 = knots[span+2];
		k3 = knots[span+3];
		
		c3 = m00*k0 + m01*k1 + m02*k2 + m03*k3;
		c2 = m10*k0 + m11*k1 + m12*k2 + m13*k3;
		c1 = m20*k0 + m21*k1 + m22*k2 + m23*k3;
		c0 = m30*k0 + m31*k1 + m32*k2 + m33*k3;
		
		return ((c3*x + c2)*x + c1)*x + c0;
	}
	
	/**
	 * Compute a Catmull-Rom spline, but with variable knot spacing.
	 * @param x the input parameter
	 * @param numKnots the number of knots in the spline
	 * @param xknots the array of knot x values
	 * @param yknots the array of knot y values
	 * @return the spline value
	 */
	public static float spline(float x, int numKnots, int[] xknots, int[] yknots) {
		int span;
		int numSpans = numKnots - 3;
		float k0, k1, k2, k3;
		float c0, c1, c2, c3;
		
		if (numSpans < 1)
			throw new IllegalArgumentException("Too few knots in spline");
		
		for (span = 0; span < numSpans; span++)
			if (xknots[span+1] > x)
				break;
		if (span > numKnots-3)
			span = numKnots-3;
		float t = (float)(x-xknots[span]) / (xknots[span+1]-xknots[span]);
		span--;
		if (span < 0) {
			span = 0;
			t = 0;
		}

		k0 = yknots[span];
		k1 = yknots[span+1];
		k2 = yknots[span+2];
		k3 = yknots[span+3];
		
		c3 = m00*k0 + m01*k1 + m02*k2 + m03*k3;
		c2 = m10*k0 + m11*k1 + m12*k2 + m13*k3;
		c1 = m20*k0 + m21*k1 + m22*k2 + m23*k3;
		c0 = m30*k0 + m31*k1 + m32*k2 + m33*k3;
		
		return ((c3*t + c2)*t + c1)*t + c0;
	}

	/**
	 * Compute a Catmull-Rom spline for RGB values.
	 * @param x the input parameter
	 * @param numKnots the number of knots in the spline
	 * @param knots the array of knots
	 * @return the spline value
	 */
	public static int colorSpline(float x, int numKnots, int[] knots) {
		int span;
		int numSpans = numKnots - 3;
		float k0, k1, k2, k3;
		float c0, c1, c2, c3;
		
		if (numSpans < 1)
			throw new IllegalArgumentException("Too few knots in spline");
		
		x = m_MathUtils.clamp(x, 0, 1) * numSpans;
		span = (int)x;
		if (span > numKnots-4)
			span = numKnots-4;
		x -= span;

		int v = 0;
		for (int i = 0; i < 4; i++) {
			int shift = i * 8;
			
			k0 = (knots[span] >> shift) & 0xff;
			k1 = (knots[span+1] >> shift) & 0xff;
			k2 = (knots[span+2] >> shift) & 0xff;
			k3 = (knots[span+3] >> shift) & 0xff;
			
			c3 = m00*k0 + m01*k1 + m02*k2 + m03*k3;
			c2 = m10*k0 + m11*k1 + m12*k2 + m13*k3;
			c1 = m20*k0 + m21*k1 + m22*k2 + m23*k3;
			c0 = m30*k0 + m31*k1 + m32*k2 + m33*k3;
			int n = (int)(((c3*x + c2)*x + c1)*x + c0);
			if (n < 0)
				n = 0;
			else if (n > 255)
				n = 255;
			v |= n << shift;
		}
		
		return v;
	}

	/**
	 * Compute a Catmull-Rom spline for RGB values, but with variable knot spacing.
	 * @param x the input parameter
	 * @param numKnots the number of knots in the spline
	 * @param xknots the array of knot x values
	 * @param yknots the array of knot y values
	 * @return the spline value
	 */
	public static int colorSpline(int x, int numKnots, int[] xknots, int[] yknots) {
		int span;
		int numSpans = numKnots - 3;
		float k0, k1, k2, k3;
		float c0, c1, c2, c3;
		
		if (numSpans < 1)
			throw new IllegalArgumentException("Too few knots in spline");
		
		for (span = 0; span < numSpans; span++)
			if (xknots[span+1] > x)
				break;
		if (span > numKnots-3)
			span = numKnots-3;
		float t = (float)(x-xknots[span]) / (xknots[span+1]-xknots[span]);
		span--;
		if (span < 0) {
			span = 0;
			t = 0;
		}

		int v = 0;
		for (int i = 0; i < 4; i++) {
			int shift = i * 8;
			
			k0 = (yknots[span] >> shift) & 0xff;
			k1 = (yknots[span+1] >> shift) & 0xff;
			k2 = (yknots[span+2] >> shift) & 0xff;
			k3 = (yknots[span+3] >> shift) & 0xff;
			
			c3 = m00*k0 + m01*k1 + m02*k2 + m03*k3;
			c2 = m10*k0 + m11*k1 + m12*k2 + m13*k3;
			c1 = m20*k0 + m21*k1 + m22*k2 + m23*k3;
			c0 = m30*k0 + m31*k1 + m32*k2 + m33*k3;
			int n = (int)(((c3*t + c2)*t + c1)*t + c0);
			if (n < 0)
				n = 0;
			else if (n > 255)
				n = 255;
			v |= n << shift;
		}
		
		return v;
	}

	/**
	 * An implementation of Fant's resampling algorithm.
	 * @param source the source pixels
	 * @param dest the destination pixels
	 * @param length the length of the scanline to resample
	 * @param offset the start offset into the arrays
	 * @param stride the offset between pixels in consecutive rows
	 * @param out an array of output positions for each pixel
	 */
	public static void resample(int[] source, int[] dest, int length, int offset, int stride, float[] out) {
		int i, j;
		float intensity;
		float sizfac;
		float inSegment;
		float outSegment;
		int a, r, g, b, nextA, nextR, nextG, nextB;
		float aSum, rSum, gSum, bSum;
		float[] in;
		int srcIndex = offset;
		int destIndex = offset;
		int lastIndex = source.length;
		int rgb;

		in = new float[length+1];
		i = 0;
		for (j = 0; j < length; j++) {
			while (out[i+1] < j)
				i++;
			in[j] = i + (float) (j - out[i]) / (out[i + 1] - out[i]);
		}
		in[length] = length;

		inSegment  = 1.0f;
		outSegment = in[1];
		sizfac = outSegment;
		aSum = rSum = gSum = bSum = 0.0f;
		rgb = source[srcIndex];
		a = (rgb >> 24) & 0xff;
		r = (rgb >> 16) & 0xff;
		g = (rgb >> 8) & 0xff;
		b = rgb & 0xff;
		srcIndex += stride;
		rgb = source[srcIndex];
		nextA = (rgb >> 24) & 0xff;
		nextR = (rgb >> 16) & 0xff;
		nextG = (rgb >> 8) & 0xff;
		nextB = rgb & 0xff;
		srcIndex += stride;
		i = 1;

		while (i < length) {
			float aIntensity = inSegment * a + (1.0f - inSegment) * nextA;
			float rIntensity = inSegment * r + (1.0f - inSegment) * nextR;
			float gIntensity = inSegment * g + (1.0f - inSegment) * nextG;
			float bIntensity = inSegment * b + (1.0f - inSegment) * nextB;
			if (inSegment < outSegment) {
				aSum += (aIntensity * inSegment);
				rSum += (rIntensity * inSegment);
				gSum += (gIntensity * inSegment);
				bSum += (bIntensity * inSegment);
				outSegment -= inSegment;
				inSegment = 1.0f;
				a = nextA;
				r = nextR;
				g = nextG;
				b = nextB;
				if (srcIndex < lastIndex)
					rgb = source[srcIndex];
				nextA = (rgb >> 24) & 0xff;
				nextR = (rgb >> 16) & 0xff;
				nextG = (rgb >> 8) & 0xff;
				nextB = rgb & 0xff;
				srcIndex += stride;
			} else {
				aSum += (aIntensity * outSegment);
				rSum += (rIntensity * outSegment);
				gSum += (gIntensity * outSegment);
				bSum += (bIntensity * outSegment);
				dest[destIndex] = 
					((int)Math.min(aSum/sizfac, 255) << 24) |
					((int)Math.min(rSum/sizfac, 255) << 16) |
					((int)Math.min(gSum/sizfac, 255) << 8) |
					(int)Math.min(bSum/sizfac, 255);
				destIndex += stride;
				rSum = gSum = bSum = 0.0f;
				inSegment -= outSegment;
				outSegment = in[i+1] - in[i];
				sizfac = outSegment;
				i++;
			}
		}
	}
	/**
     * Premultiply a block of pixels
     */
	public static void premultiply( int[] p, int offset, int length ) {
      length += offset;
          for ( int i = offset; i < length; i ++ ) {
          int rgb = p[i];
          int a = (rgb >> 24) & 0xff;
          int r = (rgb >> 16) & 0xff;
          int g = (rgb >> 8) & 0xff;
          int b = rgb & 0xff;
          float f = a * (1.0f / 255.0f);
          r *= f;
          g *= f;
          b *= f;
          p[i] = (a << 24) | (r << 16) | (g << 8) | b;
      }
  }

  /**
     * Premultiply a block of pixels
     */
	public static void unpremultiply( int[] p, int offset, int length ) {
      length += offset;
          for ( int i = offset; i < length; i ++ ) {
          int rgb = p[i];
          int a = (rgb >> 24) & 0xff;
          int r = (rgb >> 16) & 0xff;
          int g = (rgb >> 8) & 0xff;
          int b = rgb & 0xff;
          if ( a != 0 && a != 255 ) {
              float f = 255.0f / a;
              r *= f;
              g *= f;
              b *= f;
              if ( r > 255 )
                  r = 255;
              if ( g > 255 )
                  g = 255;
              if ( b > 255 )
                  b = 255;
              p[i] = (a << 24) | (r << 16) | (g << 8) | b;
          }
      }
  }
}
