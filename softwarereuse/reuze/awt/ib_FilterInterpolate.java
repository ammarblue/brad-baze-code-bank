package reuze.awt;

import com.software.reuze.ib_a_Ops;
import com.software.reuze.m_InterpolateLerp;
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

public class ib_FilterInterpolate extends ib_a_Ops {
	
	private z_BufferedImage destination;
	private float interpolation;

	public ib_FilterInterpolate() {
	}

	public void setDestination( z_BufferedImage destination ) {
		this.destination = destination;
	}
	
	public z_BufferedImage getDestination() {
		return destination;
	}
	
	public void setInterpolation( float interpolation ) {
		this.interpolation = interpolation;
	}
	
	public float getInterpolation() {
		return interpolation;
	}
	
    public z_BufferedImage filter( z_BufferedImage src, z_BufferedImage dst ) {
        int width = src.getWidth();
        int height = src.getHeight();
		int type = src.getType();
		//WritableRaster srcRaster = src.getRaster();

        if ( dst == null )
            dst = createCompatibleDestImage( src, null );
		//WritableRaster dstRaster = dst.getRaster();

        if ( destination != null ) {
			width = Math.min( width, destination.getWidth() );
			height = Math.min( height, destination.getWidth() );
			int[] pixels1 = null;
			int[] pixels2 = null;

			for (int y = 0; y < height; y++) {
				pixels1 = getRGB( src, 0, y, width, 1, pixels1 );
				pixels2 = getRGB( destination, 0, y, width, 1, pixels2 );
				for (int x = 0; x < width; x++) {
					int rgb1 = pixels1[x];
					int rgb2 = pixels2[x];
					int a1 = (rgb1 >> 24) & 0xff;
					int r1 = (rgb1 >> 16) & 0xff;
					int g1 = (rgb1 >> 8) & 0xff;
					int b1 = rgb1 & 0xff;
					int a2 = (rgb2 >> 24) & 0xff;
					int r2 = (rgb2 >> 16) & 0xff;
					int g2 = (rgb2 >> 8) & 0xff;
					int b2 = rgb2 & 0xff;
					r1 = m_MathUtils.clampToByte( m_InterpolateLerp.lerp( interpolation, r1, r2 ) );
					g1 = m_MathUtils.clampToByte( m_InterpolateLerp.lerp( interpolation, g1, g2 ) );
					b1 = m_MathUtils.clampToByte( m_InterpolateLerp.lerp( interpolation, b1, b2 ) );
					pixels1[x] = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
				}
				setRGB( dst, 0, y, width, 1, pixels1 );
			}
        }

        return dst;
    }

	public String toString() {
		return "Effects/Interpolate...";
	}
}
