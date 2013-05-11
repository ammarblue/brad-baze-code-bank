package com.software.reuze;

public class gb_TerrainMaterials {
	final m_NoiseGradient g_NoiseGen;
	public float fovRatio;
	public gb_TerrainMaterials(m_NoiseGradient g, float fovRatio) {
		g_NoiseGen=g; this.fovRatio=fovRatio;
	}
	public void SetFovRatio( float fov, int w )
	{ 
	  this.fovRatio=(float) (Math.tan(m_MathUtils.degreesToRadians*(fov)/2)  /((float)w/2));
	}
	public float detailNoiseLod(float x, float z, float t)
	{
	  return   g_NoiseGen.Fbm( x*4,z*4,m_NoiseGradient.CalcNoiseOctaves(fovRatio,t,4f,2f,1f/2.189f),0.9f,2.189f);
	}
	public gb_Vector3 getTerrainMaterial( gb_i_TerrainHeightField hgts, gb_Vector3 p, gb_Vector3 n, float t )
	{
	  gb_Vector3 base= new gb_Vector3(0.75f,0.75f,0.75f);
	  gb_Vector3 layer1 = new gb_Vector3(0.75f,1,0.3f);
	  
	  gb_Vector3 layer2 = new gb_Vector3(0.65f,0.5f,0.2f);
	  float dn = detailNoiseLod(p.x,p.z, t);
	  float blendLayer2 = gb_TerrainSunLighting.smoothstep(0.4f,1.0f,n.y+ dn*.1f);
	  float blendLayer1 = gb_TerrainSunLighting.smoothstep(0.9f,1.0f,n.y+ dn*.025f);

	  return m_InterpolateLerp.lerp(blendLayer1, m_InterpolateLerp.lerp(blendLayer2, base, layer2), layer1);
	}
	// Material library
	public static final gb_TerrainMaterial LightGrass = new gb_TerrainMaterial( new gb_Vector3(0.75f,0.9f,0.3f),  0.01f, 32f*4f,0.5f,0.1f);
	public static final gb_TerrainMaterial YellowGrass = new gb_TerrainMaterial( new gb_Vector3(0.75f,0.8f,0.3f),  0.01f, 32f*4f,0.2f,0.01f);

	public static final gb_TerrainMaterial DarkGrass = new gb_TerrainMaterial( new gb_Vector3(0.45f,0.6f,0.2f),  0.01f, 32f*4f,0.3f,0.1f);
	public static final gb_TerrainMaterial DarkTrees = new gb_TerrainMaterial( new gb_Vector3(0.35f,0.5f,0.3f),  0.2f, 32f*4f,0.0f,0.01f);

	public static final gb_TerrainMaterial Sand = new gb_TerrainMaterial(  new gb_Vector3(0.7f,0.7f,0.3f),  0.0f, 32f*5f,0f,0.1f);//new Vector3(0.65,0.5,0.35)
	public static final gb_TerrainMaterial DarkSand = new gb_TerrainMaterial(  new gb_Vector3(0.6f,0.6f,0.4f),  0.0f, 32f*5f,0f,0.1f);//new Vector3(0.65,0.5,0.35)

	public static final gb_TerrainMaterial Rock = new gb_TerrainMaterial( new gb_Vector3(0.35f,0.35f,0.35f),  0.0f, 32f*4f,0f,0.3f);
	public static final gb_TerrainMaterial Dirt = new gb_TerrainMaterial(  new gb_Vector3(0.55f,0.55f,0.55f),  0.01f, 32f*4f,0f,0.1f);//new Vector3(0.65,0.5,0.35)
	public static final gb_TerrainMaterial DarkDirt = new gb_TerrainMaterial(  new gb_Vector3(0.45f,0.4f,0.2f),  0.01f, 32f*3f,0f,0.01f);//new Vector3(0.65,0.5,0.35)

	public static final gb_TerrainMaterial Snow = new gb_TerrainMaterial( new gb_Vector3(1f,1f,1f),  0.005f, 32f*4f,0f,0.6f);
	public static final gb_TerrainMaterial DarkRock = new gb_TerrainMaterial( new gb_Vector3(0.15f,0.15f,0.15f),  0.1f, 32f,0f,0.3f);
	public static final gb_TerrainMaterial Blank = new gb_TerrainMaterial( new gb_Vector3(0.5f,0.5f,0.5f),  0.1f, 16f,0.0f,0.0f);

	public static gb_TerrainMaterial  getTerrainMaterial4( gb_i_TerrainHeightField hgts, gb_Vector3 p, gb_Vector3 n, float t )
	{
	  return Blank;//new Material( lerp(new Vector3(1,0,0),new Vector3(0,0,1),(float)g_lastOctaves/24),0.5);
	}
	  
	public gb_TerrainMaterial  getSimpleTerrainMaterial( gb_i_TerrainHeightField hgts, gb_Vector3 p, gb_Vector3 n, float t )
	{  
	  float dn = detailNoiseLod(p.x,p.z,t);
	  float dn2 = detailNoiseLod(p.x+13.89f,p.z-1.95f,t);
	  float blendLayer2 = gb_TerrainSunLighting.smoothstep(0.7f,0.9f,n.y+ dn*.4f);
	  float blendLayer1 = gb_TerrainSunLighting.smoothstep(0.85f,1,n.y+ dn*.1f);

	  float blendLayer0 = m_MathUtils.clamp(Math.min(dn2*2,1) * blendLayer1,0,1);

	   gb_TerrainMaterial mat =gb_TerrainMaterial.lerp(blendLayer2, Rock, Dirt);
	   mat= gb_TerrainMaterial.lerp(blendLayer1, mat, DarkGrass);
	   mat= gb_TerrainMaterial.lerp(blendLayer0, mat, LightGrass);
	   return mat;
	}

	public gb_TerrainMaterial  getSnowMaterial( gb_i_TerrainHeightField hgts, gb_Vector3 p, gb_Vector3 n, float t )
	{  
	  float dn2 = detailNoiseLod(p.x,p.z,t);
	  float blendLayer1 = gb_TerrainSunLighting.smoothstep(0.85f,0.9f,n.y+ dn2*.05f + m_MathUtils.clamp(0.2f-p.y,0,1));

	 return gb_TerrainMaterial.lerp(blendLayer1, DarkRock, Snow);
	}
	
	float Rocks2( float x, float z, int numSamples, float rad )
	{
	   float s = 4 *1/rad;
	  float msk = m_MathUtils.clamp(g_NoiseGen.Fbm( x*.25f,z*.25f,4,0.5f,2.189f)*4,0,1);
	  float nv = Math.max(Math.abs(g_NoiseGen.Get( x*s,z*s)),0);
	  nv = (float) Math.sqrt(Math.sqrt(nv));
	  return nv*msk;
	}  
	float RocksAO( float x, float z, int numSamples, float rad )
	{
	  float s = 4 *1/rad;
	  float msk = m_MathUtils.clamp(g_NoiseGen.Fbm( x*.25f,z*.25f,4,0.5f,2.189f)*4,0,1);
	  float nv = Math.max(Math.abs(g_NoiseGen.Get( x*s,z*s)),0.4f);
	  
	  return Math.max(nv, 1-msk);
	}
	float terrain( float x, float z, float t , float minDetail)
	{
	 x+=17.0;
	 z-=15.0;
	  int oct = m_NoiseGradient.CalcNoiseOctaves(fovRatio,t,0.3f,minDetail,1/2.189f);
	  float tn = 0;
	  tn =g_NoiseGen.Turb( x*0.3f,z*.3f,oct,0.5f,2.189f);//*.5+.5;
	  float tn2 =tn*tn;
	  return tn2*3;
	}

	float ambTerrain( float x, float y, float z, float t, float minDetail )
	{ 
	  x+=17.0;
	 z-=15.0;
	   int oct = m_NoiseGradient.CalcNoiseOctaves(fovRatio,t,0.3f,minDetail,1/2.189f);
	  float tn =g_NoiseGen.TurbAO( x*0.3f,z*.3f,oct,0.5f,2.189f);
	  return tn;
	}
	
	float snowTerrain( float x, float z, float t, float minDetail)
	{
	  x+=17.0;
	 z-=15.0;
	  int oct =  m_NoiseGradient.CalcNoiseOctaves(fovRatio,t,0.3f,minDetail,1/2.189f);
	  float tn =g_NoiseGen.Fbm( x*0.3f,z*.3f,oct,0.5f,2.189f)*.5f+.5f;
	 
	  float tn2 =tn*tn;
	  tn2*=tn2;
	  return tn2*2;
	}
	float ambSnow( float x, float y, float z, float t,float minDetail )
	{
	 x+=17.0;
	 z-=15.0;
	   int oct = m_NoiseGradient.CalcNoiseOctaves(fovRatio,t,0.3f,minDetail,1/2.189f);
	  float tn =g_NoiseGen.FbmAO( x*0.3f,z*.3f,oct,0.5f,2.189f);
	  return m_InterpolateLerp.lerp(m_MathUtils.clamp(y*4,0,1),1,tn);
	}
	float rollingHills( float x, float z, float t, float minDetail)
	{
	   x+=17.0;
	   z-=15.0;
	   int oct =  m_NoiseGradient.CalcNoiseOctaves(fovRatio,t,0.15f,minDetail,1/2.189f);
	   float h = g_NoiseGen.Fbm( x*0.15f,z*.15f,oct,0.4f,2.189f)+.2f;
	   return h;
	}
}
