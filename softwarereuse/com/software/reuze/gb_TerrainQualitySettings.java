package com.software.reuze;
public class gb_TerrainQualitySettings
	{
	  public boolean useSubSampling=false;
	  public float   shadowRayDeltaRatio=0.025f;//0.025f;
	  public float   shadowRayStartRatio=0.008f;//0.008f;
	  public float   rayDeltaRatio = 0.05f;//5;//35;//1;//1f;// 0.005f;
	  public boolean useReflection = true;
	  public float   maxHgt=1.5f;
	  public void SetFast()
	  {
	    shadowRayDeltaRatio*=4;
	    rayDeltaRatio*=4;
	    useReflection=true;
	    maxHgt=1.0f;
	    shadowRayStartRatio*=4;
	  }
	};