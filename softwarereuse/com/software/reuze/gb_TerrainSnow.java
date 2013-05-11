package com.software.reuze;

public class gb_TerrainSnow extends gb_TerrainSimple
	{
	  public gb_TerrainSnow(gb_TerrainMaterials mat) {
		  super(mat);
	  }
	  public float GetHeight(float x, float z, float t)
	  {
	    return mat.snowTerrain( x,z,t,8);
	  }
	  public float GetFinalHeight(float x, float z, float t)
	  {
	    return mat.snowTerrain( x,z,t,2);
	  }

	  public gb_TerrainMaterial   GetMaterial(gb_Vector3 p, gb_Vector3 n, float t)
	  {
	    return mat.getSnowMaterial(this,p,n,t);
	  }
	   public float GetAO(float x, float y, float z, float t)
	  {
	    return mat.ambSnow( x,y,z,t,2);
	  }
	}