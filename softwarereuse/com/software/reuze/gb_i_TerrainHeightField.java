package com.software.reuze;

public interface gb_i_TerrainHeightField
	{
	  float GetHeight(float x, float z, float t);
	  float GetFinalHeight(float x, float z, float t);
	  
	  float GetAO(float x, float y, float z, float t);
	  gb_TerrainMaterial   GetMaterial(gb_Vector3 p, gb_Vector3 sn, float t);
	  float             GetWaterHeight();
	}