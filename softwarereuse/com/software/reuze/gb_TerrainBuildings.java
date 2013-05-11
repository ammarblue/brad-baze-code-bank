package com.software.reuze;

public class gb_TerrainBuildings extends gb_TerrainSimple
{
	public gb_TerrainBuildings(gb_TerrainMaterials mat) {
		  super(mat);
	  }
	static float sphereFunc( float x, float z, float r)
	{
	  x/=r;
	  z/=r;
	  return (float) (Math.max(Math.sqrt(1 - x*x - z*z),0)*r);
	}
	static float building( float x, float z)
	{
	  double ang = Math.atan(x/z);
	  double hDiff = Math.max(Math.sin(ang*6),0);
	  float s = sphereFunc( x, z, 2);
	  double rd = Math.min(Math.cos(s*40),0)*.1f;
	  return (float) (Math.max(s*hDiff, s*0.4f)+rd);
	}
	//float building( float x, float z)
	//{
	//  return sphereFunc( x, z, 2) - sphereFunc( x-.5, z-0.8, 1.5);
	//}
	
  float Buildings( float x, float z)
  {
    return building(x-1, z-6)*0.5f + building((x+3)*.25f, (z-15f)*.25f)*1f;
  }
  public float GetFinalHeight(float x, float z, float t)
  {
    return GetHeight(x,z,t);
  }

  public float GetHeight(float x, float z, float t)
  {
    //float d = tile(x,z,.05);
   // float f = max(g_NoiseGen.Get(x*.5,z*.5),0);//*.5+.5;
   // f =  sqrt(f);
  //  f += f * g_NoiseGen.Turb( x*8.,z*8.,2,0.5,2.189)*.1;
//return f;
    return Math.max(Buildings(x,z),super.GetHeight( x, z,t));
  }
  public float GetAO(float x, float y, float z, float t)
  {
    if (Math.abs( y - Buildings(x,z))<0.01f)
   {
     return 0.5f;
   } 
    return super.GetAO(x,y,z,t);
  }
  public gb_TerrainMaterial   GetMaterial(gb_Vector3 p, gb_Vector3 n, float t)
  {
    if ( Buildings(p.x,p.z)>0.0001f)
   {
     float bl = Math.min(mat.g_NoiseGen.Turb( p.x,p.z,8,0.5f,2.189f)*6,1);
     return gb_TerrainMaterial.lerp(bl,gb_TerrainMaterials.Rock,gb_TerrainMaterials.DarkDirt);
     //return DarkDirt;//new Material(new Vector3(1.0f,0.8f,1.0f),0.0);
   } 
    return super.GetMaterial(p,n,t);
  }
}	