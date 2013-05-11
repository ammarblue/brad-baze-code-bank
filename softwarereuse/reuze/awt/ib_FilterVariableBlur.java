package reuze.awt;

import com.software.reuze.ib_a_Ops;
import com.software.reuze.z_BufferedImage;
import com.software.reuze.z_ColorModel;
import com.software.reuze.z_Point2D;
import com.software.reuze.z_Rectangle2D;
import com.software.reuze.z_RenderingHints;

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

public class ib_FilterVariableBlur extends ib_a_Ops {

	private int hRadius = 1;
	private int vRadius = 1;
	private int iterations = 1;
	private z_BufferedImage blurMask;
	
    public z_BufferedImage filter( z_BufferedImage src, z_BufferedImage dst ) {
		int width = src.getWidth();
        int height = src.getHeight();

        if ( dst == null )
            dst = new z_BufferedImage( width, height, z_BufferedImage.TYPE_INT_ARGB );

        int[] inPixels = new int[width*height];
        int[] outPixels = new int[width*height];
        getRGB( src, 0, 0, width, height, inPixels );

        for (int i = 0; i < iterations; i++ ) {
            blur( inPixels, outPixels, width, height, hRadius, 1 );
            blur( outPixels, inPixels, height, width, vRadius, 2 );
        }

        setRGB( dst, 0, 0, width, height, inPixels );
        return dst;
    }

    public z_BufferedImage createCompatibleDestImage(z_BufferedImage src, z_ColorModel dstCM) {
        if ( dstCM == null )
            dstCM = src.getColorModel(null);
        return new z_BufferedImage(dstCM, src.getWidth(), src.getHeight());
    }
    
    public z_Rectangle2D getBounds2D( z_BufferedImage src ) {
        return new z_Rectangle2D(0, 0, src.getWidth(), src.getHeight());
    }
    
    public z_Point2D getPoint2D( z_Point2D srcPt, z_Point2D dstPt ) {
        if ( dstPt == null )
            dstPt = new z_Point2D();
        dstPt.setLocation( srcPt.getX(), srcPt.getY() );
        return dstPt;
    }

    public z_RenderingHints getRenderingHints() {
        return null;
    }

    public void blur( int[] in, int[] out, int width, int height, int radius, int pass ) {
        int widthMinus1 = width-1;
        int[] r = new int[width];
        int[] g = new int[width];
        int[] b = new int[width];
        int[] a = new int[width];
        int[] mask = new int[width];

		int inIndex = 0;

        for ( int y = 0; y < height; y++ ) {
            int outIndex = y;

			if ( blurMask != null ) {
				if ( pass == 1 )
					getRGB( blurMask, 0, y, width, 1, mask );
				else
					getRGB( blurMask, y, 0, 1, width, mask );
			}

            for ( int x = 0; x < width; x++ ) {
				int argb = in[inIndex+x];
				a[x] = (argb >> 24) & 0xff;
                r[x] = (argb >> 16) & 0xff;
                g[x] = (argb >> 8) & 0xff;
                b[x] = argb & 0xff;
                if ( x != 0 ) {
                    a[x] += a[x-1];
                    r[x] += r[x-1];
                    g[x] += g[x-1];
                    b[x] += b[x-1];
                }
			}

            for ( int x = 0; x < width; x++ ) {
				// Get the blur radius at x, y
				int ra;
				if ( blurMask != null ) {
					if ( pass == 1 )
						ra = (int)((mask[x] & 0xff)*hRadius/255f);
					else
						ra = (int)((mask[x] & 0xff)*vRadius/255f);
				} else {
					if ( pass == 1 )
						ra = (int)(blurRadiusAt( x, y, width, height ) * hRadius);
					else
						ra = (int)(blurRadiusAt( y, x, height, width ) * vRadius);
				}

                int divisor = 2*ra+1;
                int ta = 0, tr = 0, tg = 0, tb = 0;
				int i1 = x+ra;
                if ( i1 > widthMinus1 ) {
                    int f = i1-widthMinus1;
					int l = widthMinus1;
					ta += (a[l]-a[l-1]) * f;
					tr += (r[l]-r[l-1]) * f;
					tg += (g[l]-g[l-1]) * f;
					tb += (b[l]-b[l-1]) * f;
					i1 = widthMinus1;
                }
				int i2 = x-ra-1;
                if ( i2 < 0 ) {
					ta -= a[0] * i2;
					tr -= r[0] * i2;
					tg -= g[0] * i2;
					tb -= b[0] * i2;
                    i2 = 0;
				}
                
                ta += a[i1] - a[i2];
                tr += r[i1] - r[i2];
                tg += g[i1] - g[i2];
                tb += b[i1] - b[i2];
//if ( y == 0 && pass == 1 )System.out.println(x+": "+i1+" "+i2+" "+r[i1]+" "+r[i2]+" "+tr);
                out[ outIndex ] = ((ta/divisor) << 24) | ((tr/divisor) << 16) | ((tg/divisor) << 8) | (tb/divisor);

                outIndex += height;
            }
			inIndex += width;
        }
    }
    
	// Override this to get a different blur radius
	protected float blurRadiusAt( int x, int y, int width, int height ) {
		return (float)x/width;
	}

	public void setHRadius(int hRadius) {
		this.hRadius = hRadius;
	}
	
	public int getHRadius() {
		return hRadius;
	}
	
	public void setVRadius(int vRadius) {
		this.vRadius = vRadius;
	}
	
	public int getVRadius() {
		return vRadius;
	}
	
	public void setRadius(int radius) {
		this.hRadius = this.vRadius = radius;
	}
	
	public int getRadius() {
		return hRadius;
	}
	
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}
	
	public int getIterations() {
		return iterations;
	}
	
	public void setBlurMask(z_BufferedImage blurMask) {
		this.blurMask = blurMask;
	}
	
	public z_BufferedImage getBlurMask() {
		return blurMask;
	}
	
	public String toString() {
		return "Blur/Variable Blur...";
	}
}
