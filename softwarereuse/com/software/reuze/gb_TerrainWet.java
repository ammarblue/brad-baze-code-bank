package com.software.reuze;

public class gb_TerrainWet extends gb_TerrainSimple
	{
	  final float g_WaterHeight;
	  public gb_TerrainWet(gb_TerrainMaterials mat, float g_WaterHeight) {
			  super(mat);
			  this.g_WaterHeight=g_WaterHeight;
		  }
	  public float GetHeight(float x, float z, float t)
	  {
	    return mat.rollingHills( x,z,t,8);
	  }
	  public float GetFinalHeight(float x, float z, float t)
	  {
	    return mat.rollingHills(x,z,t,1);
	  }
	  public float GetWaterHeight()
	  {
	    return g_WaterHeight;
	  }
	  public gb_TerrainMaterial   GetMaterial(gb_Vector3 p, gb_Vector3 n, float t)
	  {
	    float wh = GetWaterHeight();
	   float dn = mat.detailNoiseLod(p.x-1.2f,p.z-10,t);
	   float dn2 = mat.detailNoiseLod(p.x+13.89f,p.z-1.95f,t);
	   
	   gb_TerrainMaterial grass =gb_TerrainMaterial.lerp(gb_TerrainSunLighting.smoothstep(0.85f,0.9f,n.y+dn*.1f), gb_TerrainMaterials.DarkDirt, gb_TerrainMaterials.DarkGrass);
	   dn = gb_TerrainSunLighting.smoothstep(-0.2f,.8f,dn);
	   gb_TerrainMaterial sand = gb_TerrainMaterial.lerp(dn, gb_TerrainMaterials.Sand, gb_TerrainMaterials.DarkSand);
	   return gb_TerrainMaterial.lerp(gb_TerrainSunLighting.smoothstep(wh+0.05f,wh+.2f,p.y+dn2*0.1f), sand, grass);
	}
	// do water depending on water height
	  public float GetAO(float x, float y, float z, float t)
	  {
	    x+=17.0;
	    z-=15.0;
	    int oct =  m_NoiseGradient.CalcNoiseOctaves(mat.fovRatio,t,0.15f,1f,1f/2.189f);
	    return mat.g_NoiseGen.FbmAO( x*0.15f,z*.15f,oct,0.4f,2.189f)*.8f+.2f;
	  }
	}