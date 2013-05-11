package com.software.reuze;

public class gb_TerrainSimple implements gb_i_TerrainHeightField
	{
	final gb_TerrainMaterials mat;
	  public gb_TerrainSimple(gb_TerrainMaterials mat) {
		  this.mat=mat;
	  }
	  public float GetHeight(float x, float z, float t)
	  {
	    return mat.terrain(x,z,t,12);
	  }
	  public float GetFinalHeight(float x, float z, float t)
	  {
	    return mat.terrain(x,z,t,2);
	  }
	  public float GetAO(float x, float y, float z, float t)
	  {
	    return mat.ambTerrain( x,y,z,t,2);
	  }
	  public gb_TerrainMaterial   GetMaterial(gb_Vector3 p, gb_Vector3 n, float t)
	  {
	    return mat.getSimpleTerrainMaterial(this,p,n,t);
	  }
	  public float     GetWaterHeight()
	  {
	     return -10000.0f;
	  }
	}