package com.software.reuze;

public class gb_TerrainCrater implements gb_i_TerrainHeightField
	{
		gb_TerrainMaterials materials;
		public gb_TerrainCrater(gb_TerrainMaterials materials) {
		   this.materials=materials;
	   }
		float sphereFunc( float x, float z, float r)
		{
		  x/=r;
		  z/=r;
		  return (float) (Math.max(Math.sqrt(1 - x*x - z*z),0)*r);
		}
	   float GetHeightVal(float x, float z, float t, float minDetail) {
	    float xf =x/6;
	    float zf =(z-8)/6;
	    float d= 1 - Math.min(xf*xf+zf*zf,1);
	    float h =gb_TerrainSunLighting.smoothstep(0,1,d)*2;
	     h = h - sphereFunc(  x, z-8, 3.5f);
	    
	    int oct = m_NoiseGradient.CalcNoiseOctaves(materials.fovRatio,t,0.5f,minDetail,1/2.189f);
	    
	    float rock= gb_TerrainSunLighting.smoothstep(0,1,Math.min(h*2,1));
	     
	    float nv =materials.g_NoiseGen.Fbm( x*0.5f+13.4f,z*.5f+7.5f,oct,0.4f+rock*.2f,2.189f);
	     
	    h += nv*0.5 ;
	    
	    return h;
	  }
	  public float GetHeight(float x, float z, float t)
	  {
	     return GetHeightVal(x,z,t,8);
	  }
	  public float GetFinalHeight(float x, float z, float t)
	  {
	    return GetHeightVal(x,z,t,2);
	  }

	  public float GetAO(float x, float y, float z, float t)
	  {
	    
	    int oct = m_NoiseGradient.CalcNoiseOctaves(materials.fovRatio,t,0.5f,2,1/2.189f);
	    float rock= gb_TerrainSunLighting.smoothstep(0,1,Math.min(y*2,1));
	    
	    float nv  =materials.g_NoiseGen.FbmAO( x*0.5f+13.4f,z*.5f+7.5f,oct,0.4f+rock*.2f,2.189f);
	    
	    // darken in crater
	    float incrator =  m_MathUtils.clamp(ga_Vector2.ZERO.dst(x,z-8)-2.5f,0,1);
	    return incrator*nv;//abs(y-nv) ;
	  }
	  public gb_TerrainMaterial   GetMaterial(gb_Vector3 p, gb_Vector3 n, float t)
	  {
	     float rock= gb_TerrainSunLighting.smoothstep(0,1,Math.min(p.y*2,1));
	     return new gb_TerrainMaterial(m_InterpolateLerp.lerp(rock, new gb_Vector3(0.4f,0.5f,0.2f), new gb_Vector3(0.5f,0.4f,0.2f)),Math.min(p.y*2,1)*.1f);
	  }
	  public float     GetWaterHeight()
	  {
	     return -1000.0f;
	  }
	}