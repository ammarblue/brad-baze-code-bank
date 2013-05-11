package com.software.reuze;
    /*
     * Color and light values that you might set in your program should be in linear space.
     * That is how energy/color calculations make sense.
     * However, Monitors have gamma curves (due to the fact they have less contrast in low luminosity colors).
     */
public class gb_TerrainGammaTable
	{
	  final static int tSize = 2048;
	  final static float tSizef = (float)tSize;
	  final int[] g_tab= new int[tSize];
	  public gb_TerrainGammaTable()
	  {
	    for (int i = 0; i < tSize; i++)
	    {
	      g_tab[i] = (int)(Math.pow( (float)i/tSize, 1/2.2f )*255);
	    }
	  }
	  public int Get( float v )
	  {
	    int iv = Math.max(Math.min((int)(v*tSizef),tSize-1),0);
	    return g_tab[ iv];
	  }
	}