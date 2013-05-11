package com.software.reuze;

public class gb_TerrainSimplePlane implements gb_i_TerrainHeightField
	{
	  final float treeHeight; //=0.3f;
	  final gb_TerrainMaterials mat;
	  public gb_TerrainSimplePlane(gb_TerrainMaterials mat, float treeHeight) {
		  super();
		  this.mat=mat; this.treeHeight=treeHeight;
	  }
	  public float GetFinalHeight(float x, float z, float t)
	  {
	    return GetHeightVal(x,z,t,true);
	  }
	  
	  public float GetHeightVal(float x, float z, float t, boolean addGrass)
	  {
	  x+=17.0;
	 z-=15.0;
	     float bh = mat.g_NoiseGen.Fbm( x*.1f,z*.1f, 4, 0.5f,2.189f);
	     int goct = m_NoiseGradient.CalcNoiseOctaves(mat.fovRatio,t,32,1,1/3.189f);
	     float grass = 0.0f;
	     if ( addGrass)
	       grass = mat.g_NoiseGen.Fbm( x*32,z*32, goct, 0.5f,3.189f)*0.005f;
	  
	 x+=17.0;
	 z-=15.0;
	    float r = mat.Rocks2(x,z,4,treeHeight);
	    return bh+m_InterpolateLerp.lerp(Math.min(r*4,1), grass, r*treeHeight);//multiFractal( x, z, t,0.4 )*1/0.4;
	  }
	  public float GetHeight(float x, float z, float t)
	  {
	    return GetHeightVal( x,z,t,false);
	  }
	  public float GetAO(float x, float y, float z, float t)
	  {
	    x += 17;
	    z -=15;   
	     x += 17;
	    z -=15;
	   int goct = m_NoiseGradient.CalcNoiseOctaves(mat.fovRatio,t,32,1,1/3.189f); 
	   float grassAO = mat.g_NoiseGen.FbmAO( x*32,z*32, goct, 0.5f,3.189f)*.8f+.2f;
	   
	    return mat.RocksAO(x,z,4,treeHeight)*grassAO;
	  }
	  public gb_TerrainMaterial   GetMaterial(gb_Vector3 p, gb_Vector3 n, float t)
	  {
	    float x = p.x + 17*2;
	    float z = p.z - 15*2;
	    float dn = mat.detailNoiseLod(p.x,p.z, t);
	 
	    float r = mat.Rocks2(x,z,4,treeHeight);
	    gb_TerrainMaterial mat = gb_TerrainMaterial.lerp(dn, gb_TerrainMaterials.YellowGrass,gb_TerrainMaterials.DarkGrass);
	    return gb_TerrainMaterial.lerp(Math.min(r*8,1), mat, gb_TerrainMaterials.DarkTrees);
	  }
	  public float     GetWaterHeight()
	  {
	     return -1000.0f;
	  }
	}