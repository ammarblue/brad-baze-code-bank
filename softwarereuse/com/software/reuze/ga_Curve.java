package com.software.reuze;

/*
** Copyright 2005 Huxtable.com. All rights reserved.
*/

public class ga_Curve {
      public float[] x;
      public float[] y;
      
      public ga_Curve() {
            x = new float[] { 0, 1 };
            y = new float[] { 0, 1 };
      }
      
      public ga_Curve( ga_Curve curve ) {
            x = (float[])curve.x.clone();
            y = (float[])curve.y.clone();
      }
      
      public int addKnot( float kx, float ky ) {
            int pos = -1;
            int numKnots = x.length;
            float[] nx = new float[numKnots+1];
            float[] ny = new float[numKnots+1];
            int j = 0;
            for ( int i = 0; i < numKnots; i++ ) {
                  if ( pos == -1 && x[i] > kx ) {
                        pos = j;
                        nx[j] = kx;
                        ny[j] = ky;
                        j++;
                  }
                  nx[j] = x[i];
                  ny[j] = y[i];
                  j++;
            }
            if ( pos == -1 ) {
                  pos = j;
                  nx[j] = kx;
                  ny[j] = ky;
            }
            x = nx;
            y = ny;
            return pos;
      }
      
      public void removeKnot( int n ) {
            int numKnots = x.length;
            if ( numKnots <= 2 )
                  return;
            float[] nx = new float[numKnots-1];
            float[] ny = new float[numKnots-1];
            int j = 0;
            for ( int i = 0; i < numKnots-1; i++ ) {
                  if ( i == n )
                        j++;
                  nx[i] = x[j];
                  ny[i] = y[j];
                  j++;
            }
            x = nx;
            y = ny;
      }

      private void sortKnots() {
            int numKnots = x.length;
            for (int i = 1; i < numKnots-1; i++) {
                  for (int j = 1; j < i; j++) {
                        if (x[i] < x[j]) {
                              float t = x[i];
                              x[i] = x[j];
                              x[j] = t;
                              t = y[i];
                              y[i] = y[j];
                              y[j] = t;
                        }
                  }
            }
      }

      public int[] makeTable() {
            int numKnots = x.length;
            float[] nx = new float[numKnots+2];
            float[] ny = new float[numKnots+2];
            System.arraycopy( x, 0, nx, 1, numKnots);
            System.arraycopy( y, 0, ny, 1, numKnots);
            nx[0] = nx[1];
            ny[0] = ny[1];
            nx[numKnots+1] = nx[numKnots];
            ny[numKnots+1] = ny[numKnots];

            int[] table = new int[256];
            for (int i = 0; i < 1024; i++) {
                  float f = i/1024.0f;
                  int x = (int)(255 * i_MathImageUtils.spline( f, nx.length, nx ) + 0.5f);
                  int y = (int)(255 * i_MathImageUtils.spline( f, nx.length, ny ) + 0.5f);
                  x = m_MathUtils.clampToByte( x );
                  y = m_MathUtils.clampToByte( y );
                  table[x] = y;
            }
            return table;
      }
}