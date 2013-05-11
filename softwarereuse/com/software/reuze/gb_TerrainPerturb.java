package com.software.reuze;

public class gb_TerrainPerturb implements gb_i_TerrainHeightField
	{
	  final float power=0.04f;
	  final float freq= 8;
	  gb_i_TerrainHeightField m_child;
	  final gb_TerrainMaterials mat;
	  public gb_TerrainPerturb( gb_TerrainMaterials mat, gb_i_TerrainHeightField c )
	  {
	    m_child = c; this.mat=mat;
	  }
	  
	  public float GetHeight(float x, float y, float t) {
	    float tx = mat.g_NoiseGen.Fbm(x*freq,y*freq,3,0.5f,2.1f);
	    float ty = mat.g_NoiseGen.Fbm(y*freq,x*freq,3,0.5f,2.1f);
	    return m_child.GetHeight( x +tx*power, y + ty*power,t);
	  }
	  public float GetAO(float x, float u, float y, float t) {
	    float tx = mat.g_NoiseGen.Fbm(x*freq,y*freq,3,0.5f,2.1f);
	    float ty = mat.g_NoiseGen.Fbm(y*freq,x*freq,3,0.5f,2.1f);
	    return m_child.GetAO( x +tx*power,u, y + ty*power,t);
	  }

	  public gb_TerrainMaterial   GetMaterial(gb_Vector3 p, gb_Vector3 n, float t)
	  {
	    return m_child.GetMaterial(p,n,t);
	  }
	  public float GetFinalHeight(float x, float z, float t)
	  {
	    return GetHeight(x,z,t);
	  }
	  public float     GetWaterHeight()
	  {
	     return m_child.GetWaterHeight();
	  }
	}