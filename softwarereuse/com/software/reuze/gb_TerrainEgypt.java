package com.software.reuze;

public class gb_TerrainEgypt implements gb_i_TerrainHeightField
	{
	  final gb_TerrainMaterials mat;
	  public gb_TerrainEgypt(gb_TerrainMaterials mat) {
		  this.mat=mat;
	  }
	  float Buildings( float x, float z)
	  {
	    float p1 = oldPyramid((x+2), (z-12))*2;
	    float p2 =pyramid((x), (z-10))*1.5f;
	     float p3 =flatPyramid((x-2)*.75f, (z-8)*.5f)*2.5f;
	    float v = Math.max(Math.max(p1,p2),p3);
	     float nv  =mat.g_NoiseGen.Fbm( x*20+13.4f,z*20+7.5f,2,0.5f,2.189f);
	   
	    v += nv*.025* Math.min(v*40,1);
	    return v;                
	  }
	  public float GetFinalHeight(float x, float z, float t)
	  {
	    return GetHeight(x,z,t);
	  }

	  final float DuneLac = 4f;
	  public float GetHeight(float x, float z, float t)
	  {
	    float xf =x/8;
	    float zf =(z-10)/8;
	    float d= Math.min(xf*xf+zf*zf,1);
	    float b = Buildings(x,z);
	    int oct = m_NoiseGradient.CalcNoiseOctaves(mat.fovRatio,t,0.5f,2,1f/DuneLac);
	    
	    float nv  =mat.g_NoiseGen.Fbm( x*0.5f+13.4f,z*.5f+7.5f,oct,0.2f,DuneLac)*.5f+.5f;
	    
	    float dune =nv*d*1.5f;
	    return Math.max(b,dune);
	  }
	  public float GetAO(float x, float y, float z, float t)
	  {
	    int oct = m_NoiseGradient.CalcNoiseOctaves(mat.fovRatio,t,0.5f,2,1/DuneLac);
	    float nv  =mat.g_NoiseGen.FbmAO( x*0.5f+13.4f,z*.5f+7.5f,oct,0.2f,DuneLac)*.5f+.5f;
	    
	    return Math.max(nv,.5f);
	  }
	  public gb_TerrainMaterial   GetMaterial(gb_Vector3 p, gb_Vector3 n, float t)
	  {
	     return new gb_TerrainMaterial(new gb_Vector3(0.9f,0.7f,0.5f),0.01f);
	  }
	  public float     GetWaterHeight()
	  {
	     return -1000.0f;
	  }
	  static float quantize(double a, double q)
		{
		  return (float) (Math.floor(a*q)/q);
		}
	  static float pyramid(float x, float z)
		{
		  //return flatPyramid(x,z);
		  return quantize(Math.max( 1.-(Math.max(Math.abs(x),Math.abs(z))),0),16.);
		}
	  static float flatPyramid(float x, float z)
		{
		  return Math.max( 1f-(Math.max(Math.abs(x),Math.abs(z))),0);
		}
	  static float oldPyramid(float x, float z)
		{
		  float h = flatPyramid(x, z);
		  return h;// + n*.3* Math.min(h*40,1);
		}
	}