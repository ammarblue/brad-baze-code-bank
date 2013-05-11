package reuze.awt;

import com.software.reuze.ib_a_FilterPoint;
import com.software.reuze.m_MathUtils;

/*
** Copyright 2005 Huxtable.com. All rights reserved.
*/

/**
 * A filter which allows channels to be swapped. You provide a matrix with specifying the input channel for 
 * each output channel.
 */
public class ib_FilterSwizzle extends ib_a_FilterPoint {

    private int[] matrix = {
        1, 0, 0, 0, 0,
        0, 1, 0, 0, 0,
        0, 0, 1, 0, 0,
        0, 0, 0, 1, 0
    };
    
      public ib_FilterSwizzle() {
      }

    /**
     * Set the swizzle matrix.
     * @param matrix the matrix
     * @see #getMatrix
     */
       public void setMatrix( int[] matrix ) {
            this.matrix = matrix;
      }

    /**
     * Get the swizzle matrix.
     * @return the matrix
     * @see #setMatrix
     */
       public int[] getMatrix() {
            return matrix;
      }

      public int filterRGB(int x, int y, int rgb) {
            int a = (rgb >> 24) & 0xff;
            int r = (rgb >> 16) & 0xff;
            int g = (rgb >> 8) & 0xff;
            int b = rgb & 0xff;

        a = matrix[0]*a + matrix[1]*r + matrix[2]*g + matrix[3]*b + matrix[4]*255;
        r = matrix[5]*a + matrix[6]*r + matrix[7]*g + matrix[8]*b + matrix[9]*255;
        g = matrix[10]*a + matrix[11]*r + matrix[12]*g + matrix[13]*b + matrix[14]*255;
        b = matrix[15]*a + matrix[16]*r + matrix[17]*g + matrix[18]*b + matrix[19]*255;

        a = m_MathUtils.clampToByte( a );
        r = m_MathUtils.clampToByte( r );
        g = m_MathUtils.clampToByte( g );
        b = m_MathUtils.clampToByte( b );

        return (a << 24) | (r << 16) | (g << 8) | b;
      }

      public String toString() {
            return "Channels/Swizzle...";
      }
      
}