package com.software.reuze;

public class gb_TerrainMaterial
	{
	  float   bs;
	  float   btile;
	  float   rimAmount;
	  float   specAmount;
	  gb_Vector3 surfcol;
	  
	  public gb_TerrainMaterial( gb_Vector3 color, float _bs)
	  {  
	    bs=_bs;
	    btile=32;
	    rimAmount=0;
	    specAmount=0;
	    surfcol=color;
	  }
	  public gb_TerrainMaterial( gb_Vector3 color, float _bs, float _bt, float _ra, float _sa)
	  {   
	    bs=_bs;
	    btile=_bt;
	    rimAmount=_ra;
	    specAmount=_sa;
	    surfcol=color;
	  }
	  public static gb_TerrainMaterial lerp(float t, gb_TerrainMaterial a, gb_TerrainMaterial b)
		{
		  return new gb_TerrainMaterial( m_InterpolateLerp.lerp(t, a.surfcol,b.surfcol),
				  m_InterpolateLerp.lerp(t, a.bs, b.bs),
				  m_InterpolateLerp.lerp(t, a.btile, b.btile),
				  m_InterpolateLerp.lerp(t, a.rimAmount, b.rimAmount),
				  m_InterpolateLerp.lerp(t, a.specAmount, b.specAmount));
		}
	}