package com.software.reuze;

// http://developer.amd.com/assets/Tatarchuk-Noise(GDC07-D3D_Day).pdf

//Creates a pre-computed table of random gradients
//for fast generation of gradient noise
//
public class m_NoiseGradient
{

float[] ranGradTable;
int wMask;
int wShift;

public static int CalcNoiseOctaves(float FovRatio, float t, float s, float octSize, float lac )
{
   float pixSize = t*FovRatio*octSize ; // 8 pixels is done by bump mapping
   float si = 1/s;
    int numOctaves = 0;
  while( si > pixSize)
  {
    si*=lac;
    numOctaves++;
    if (numOctaves >12)
      return numOctaves;
  }
  return numOctaves;
}

public static int CalcNoiseOctaves2( float pixSize, float s, float octSize, float lac )
{
   pixSize = pixSize*octSize ; 
   float si = 1/s;
  int numOctaves = 0;
  while( si > pixSize)
  {
    si*=lac;
    numOctaves++;
    assert(numOctaves < 30);
  }
  return numOctaves;
}

float[] genTab(int w)
{
	float[] gradTable = {
	    1,1, -1,1,  1,-1, -1,-1,
//	    1,0, -1,0,  0,1,  0,-1,
       m_MathUtils.SQRT2,0 , -m_MathUtils.SQRT2,0,  0,m_MathUtils.SQRT2,  0,-m_MathUtils.SQRT2
	};
 assert( gradTable.length == 16);
 float[] tab  = new float[w*w*2];
 for (int i =0; i < w*w; i++)
 {
   int idx = (int)Math.floor(m_MathUtils.random(8f));
   tab[i*2+0] = gradTable[idx*2];
   tab[i*2+1] = gradTable[idx*2+1];
 }
 // build 2x2 nearborhood

 float[] ptab  = new float[w*w*2*4];
 for (int y =0; y < w; y++)
 {
   for (int x =0; x < w; x++)
   {
     int i = x + y*w;
     int j=i;
     assert( j < w*w);
     ptab[i*8+0] = tab[j*2+0];
     ptab[i*8+1] = tab[j*2+1];

     j= ((x+1)&wMask) + y*w;
     assert( j < w*w);
     ptab[i*8+2] = tab[j*2+0];
     ptab[i*8+3] = tab[j*2+1];

     j= ((x)&wMask) + ((y+1)&wMask)*w;
     assert( j < w*w);
     ptab[i*8+4] = tab[j*2+0];
     ptab[i*8+5] = tab[j*2+1];

     j= ((x+1)&wMask) + ((y+1)&wMask)*w;
     assert( j < w*w);
     ptab[i*8+6] = tab[j*2+0];
     ptab[i*8+7] = tab[j*2+1];
   }
 }
 return ptab;
}
public m_NoiseGradient( int wS )
{
 int w = 1<<wS;
 wShift = wS;
 wMask = w-1;
 ranGradTable = genTab(w);
}
float Get( float x, float y )
{
  x = Math.abs(x);
 y = Math.abs(y);
 int i = (int)(x);
 int j = (int)(y);
 float u = x -(float)i;
 float v = y -(float)j;
 float iu= u-1;
 float iv = v-1;

 i &=wMask;
 j &=wMask;
 int idx = (((j <<wShift) + i)<<3);

 // Sample 2x2 neighborhood of gradient values for each dimension
 // and Extrapolate gradients. Distance in X,Y from each vertex.
 float xg0=ranGradTable[idx+0] * u;
 float yg0=ranGradTable[idx+1] * v;

 float xg1=ranGradTable[idx+2] * iu;
 float yg1=ranGradTable[idx+3] * v;

 float xg2=ranGradTable[idx+4] * u;
 float yg2=ranGradTable[idx+5] * iv;

 float xg3=ranGradTable[idx+6] * iu;
 float yg3=ranGradTable[idx+7] * iv;

 // Distance in Y from each vertex.


 //This now contains the 2D dot product between the gradient vector
 // and the x, y offsets from lattice point in the current 2x2
 // neighborhood.
 float a = xg0+yg0;
 float b = xg1+yg1;
 float c = xg2+yg2;
 float d = xg3+yg3;

 u = u*u*u*(u*(u*6.0f-15.0f)+10.0f);
 v = v*v*v*(v*(v*6.0f-15.0f)+10.0f);

 float k0 =   a;
 float k1 =   b - a;
 float k2 =   c - a;
 float k4 =   a - b - c + d;

 return k0 + k1*u + k2*v + k4*u*v ;
}


void GetWithNormal( float x, float y, float[] r )
{
 x = Math.abs(x);
 y = Math.abs(y);
 int i = (int)(x);
 int j = (int)(y);
 float u = x -(float)i;
 float v = y -(float)j;
 float iu= u-1;
 float iv = v-1;

 i &=wMask;
 j &=wMask;
 int idx = (((j <<wShift) + i)<<3);

 // Sample 2x2 neighborhood of gradient values for each dimension
 // and Extrapolate gradients. Distance in X,Y from each vertex.
 float xga=ranGradTable[idx+0] ;
 float yga=ranGradTable[idx+1] ;

 float xgb=ranGradTable[idx+2];
 float ygb=ranGradTable[idx+3];

 float xgc=ranGradTable[idx+4];
 float ygc=ranGradTable[idx+5];

 float xgd=ranGradTable[idx+6] ;
 float ygd=ranGradTable[idx+7] ;

 // Distance in Y from each vertex.


 //This now contains the 2D dot product between the gradient vector
 // and the x, y offsets from lattice point in the current 2x2
 // neighborhood.
 float pa = xga* u+yga* v;
 float pb = xgb * iu+ygb * v;
 float pc = xgc * u+ygc * iv;
 float pd = xgd* iu+ygd* iv;

 // get derivatives
 float du = 30.0f*u*u*(u*(u-2.0f)+1.0f);
 float dv = 30.0f*v*v*(v*(v-2.0f)+1.0f);
 
 u = u*u*u*(u*(u*6.0f-15.0f)+10.0f);
 v = v*v*v*(v*(v*6.0f-15.0f)+10.0f);
 
 float k0 =   pa;
 float k1 =   pb - pa;
 float k2 =   pc - pa;
 float k4 =   pa - pb - pc + pd;

 float dk1x = xgb-xga;
 float dk1y = ygb-yga;
 
 float dk2x = xgc-xga;
 float dk2y = ygc-yga;
     
 float dk4x = xga - xgb - xgc + xgd;
 float dk4y = yga - ygb - ygc + ygd;

 float dx = xga + u*dk1x + k1*du + dk2x*v  + dk4x*u*v + k4*v*du;
 float dy = yga  + u*dk1y + k2*dv  + dk2y*v+ k4*u*dv + dk4y*u*v ;

 r[0] = k0 + k1*u + k2*v + k4*u*v ;
 r[1] = dx;
 r[2] = dy;
}
public float Fbm( float x, float y, int oct,  float p, float l )
{
 float s =0;
 float amp=1;
 for (int i=0; i<oct; i++)
 {
   s+= amp*Get(x,y);
   x*=l;
   y*=l;
   amp *= p;
 }
 return s;
}
float dFbm( float x, float y, int oct,  float g, float l, float r[] )
{
 float s =0;
 float amp=1;
 float[] t= new float[3];
 r[0]=r[1]=r[2]=0;
 for (int i=0; i<oct; i++)
 {
   GetWithNormal( x,y,t);
   r[0]+= amp*t[0];
   r[1]+= t[1];
   r[2]+= t[2];
   x*=l;
   y*=l;
   amp *= g;
 }
 return s;
}
float FbmAO(float x, float z, int oct,  float g, float l )
{
 float r = 1;
 float am=1;
 x*=l;
 z*=l;
 for (int i =1; i < oct;i++)
 {
   float localAo = am*(Get(x,z)*.5f+.5f)+.45f;
   //localAo = localAo < 1.2 ? 0.2 : 1.;
   
   float n = m_MathUtils.clamp( localAo,0,1);
   n *= n*n*n;
   r *=n;
   x*=l;
   z*=l;
   am*=0.96;
}
 r = (float) Math.sqrt(r);
 return m_MathUtils.clamp(r,0,1);
}
float TurbAO(float x, float z, int oct,  float g, float l )
{
 float r = 1;
 float am=1;
 x*=l;
 z*=l;
 for (int i =1; i < oct;i++)
 {
   float n = Math.min(Math.abs(Get(x,z))+.6f, 1);
   r *=n;
   x*=l;
   z*=l;
}
 r = (float) Math.sqrt(r);
 return m_MathUtils.clamp(r,0,1);
}
float Turb( float x, float y, int oct,  float g, float l )
{
 float s =0;
 float amp=1;
 float sc=0;
 for (int i=0; i<oct; i++)
 {
   s+= amp*Math.abs(Get(x,y));
   sc+=amp;
   x*=l;
   y*=l;
   amp *= g;
 }
 return s/sc;
}
void dTurb( float x, float y, int oct,  float g, float l, float r[] )
{
 float amp=1;
 float sc=0;
 float[] t= new float[3];
 r[0]=r[1]=r[2]=0;
 for (int i=0; i<oct; i++)
 {
   GetWithNormal( x,y,t);
   if ( t[0] < 0 )
   {
     t[0]=-t[0];
     t[1]=-t[1];
     t[2]=-t[2];
   }
   r[0]+= amp*t[0];
   sc+=amp;
   r[1]+= t[1];
   r[2]+= t[2];
   x*=l;
   y*=l;
   amp *= g;
 }
 r[0]/=sc;

}
float detailNoiseLod(float fovRatio, float x, float z,float t)
{
  return   Fbm( x*4,z*4,CalcNoiseOctaves(fovRatio,t,4,2,1f/2.189f),0.9f,2.189f);
}
}