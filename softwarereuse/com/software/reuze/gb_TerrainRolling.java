package com.software.reuze;

public class gb_TerrainRolling implements gb_i_TerrainHeightField
	{
	  public float GetHeight(float x, float z, float t)
	  {
	    return (float) (Math.sin(x)*Math.sin(z)*1.5 + Math.cos(x*8)*Math.cos(z*8)*.25);
	  }
	  public float GetFinalHeight(float x, float z, float t)
	  {
	    return GetHeight(x,z,t);
	  }
	  public float GetAO(float x, float y, float z, float t)
	  {
	    y*=0.6666667f;
	    y++;
	    return Math.min(y*y,1)*0.5f;
	  }
	  final static gb_TerrainMaterial mat=new gb_TerrainMaterial(new gb_Vector3(0.75f,0.55f,0.4f), 0.01f);
	  public gb_TerrainMaterial   GetMaterial(gb_Vector3 p, gb_Vector3 n, float t)
	  {
	    return mat;
	  }
	  public float     GetWaterHeight()
	  {
	     return -10000.0f;
	  }
	}