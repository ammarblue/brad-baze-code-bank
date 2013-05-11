package com.software.reuze;

public class gb_TerrainBlockCity implements gb_i_TerrainHeightField
	{
		static float IntNoise( int x, int y )
		{
		  int n =x + y *57;
		  return IntNoise(n);
		}
		final static float ratio = 1f /32767f;
		static float IntNoise( int n )
		{
		  n = (n<<13)^n;
		  float ret = 1-(float)((n*(n *n*19417+189851)+4967243)&0xffff)*ratio;
		  return ret;
		}
		static gb_Vector3 IntNoise3( int x, int y )
		{
		  float a = IntNoise(x + y *57);
		  float b = IntNoise(x*57 + y *13);
		  float c = IntNoise(x*13 + y *47);
		  return new gb_Vector3( a,b,c);
		}	  

		static float Block( float x, float y )
		{
		  float v = IntNoise((int)x,(int)y);
		  return v;
		}
		static float BlockFBM( float x, float y, int oct,  float g, float l )
		  {
		    float s =0;
		    float amp=1;
		    for (int i=0; i<oct; i++)
		    {
		      s+= amp*Block(x,y);
		      x*=l;
		      y*=l;
		      amp *= g;
		    }
		    return s;
		 }
		public float GetHeight(float x, float z, float t)
	  {
	     return BlockFBM( x*.25f, z*.25f, 3, 0.5f, 3f );
	      /*x *=0.5;
	      z *=0.5;
	      float xf = x - floor(x);
	      float xz = z - floor(z);
	      if ( x < 0 )
	      {
	        return 0;
	      }
	      float h = noise(floor(x+.5),floor(z+.5));
	      return min(abs(xf-.5)*3,1.) *min(abs(xz-.5)*3,1)*3.*h;*/
	   }
	  public float GetFinalHeight(float x, float z, float t)
	  {
	    //return GetHeight(x,z,t);
	    return BlockFBM( x*.25f, z*.25f, 5, 0.5f, 3 );
	  }

	  public float GetAO(float x, float y, float z, float t)
	  {
	    return .5f;//min(y*.25,1);
	  }
	  public gb_TerrainMaterial   GetMaterial(gb_Vector3 p, gb_Vector3 n, float t)
	  {
	    float s = m_MathUtils.pow4(2.f);
	    p.x+= 15.7;
	    p.z-=14.5;
	    double ix = Math.floor(p.x*s+.5f);
	    double iz = Math.floor(p.z*s+.5f);
	    float n1 =IntNoise((int)ix,(int)iz);
	    float n2 = IntNoise((int)iz,(int)ix);
	    return new gb_TerrainMaterial( new gb_Vector3(n1,0.5f,n2),0.0f);
	  }
	  public float     GetWaterHeight()
	  {
	     return -1000.0f;
	  }
	  /*Vector3 BlockColor( float x, float y, int oct,  float g, float l )
	  {
	    float s =0;
	    float amp=1;
	    Vector3 c = new Vector3(0,0,0);
	    for (int i=0; i<oct; i++)
	    {
	      c+= amp*Block(x,y);
	      x*=l;
	      y*=l;
	      amp *= g;
	    }
	    return s;
	 }

	float tile( float x, float y, float rad)
	{
	   x/=rad;
	   y/=rad;
	   x = x - floor(x);
	   y = y - floor(y);
	   float h = (x<0.9)&&(y<0.9) ? 1: 0;
	   return h;
	}*/
	}