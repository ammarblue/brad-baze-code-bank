package com.software.reuze;

//Noise java
//JAVA REFERENCE IMPLEMENTATION OF IMPROVED NOISE - COPYRIGHT 2002 KEN PERLIN.

public final class m_NoisePerlins {

public static  float noise(float x, float y, float z) {
 int ix,iy,iz,X,Y,Z;
 float u,v,w;
 ix=(int)x;
 iy=(int)y;
 iz=(int)z;
 X = ix & 255;                  // FIND UNIT CUBE THAT
 Y = iy & 255;                  // CONTAINS POINT.
 Z = iz & 255;
 x -= ix;                                // FIND RELATIVE X,Y,Z
 y -= iy;                                // OF POINT IN CUBE.
 z -= iz;

 u = x * x * x * (x * (x*6 - 15) + 10); // COMPUTE FADE CURVES
 v = y * y * y * (y * (y*6 - 15) + 10); // FOR EACH OF X,Y,Z.
 w = z * z * z * (z * (z*6 - 15) + 10);

 int A = p[X  ]+Y, AA = p[A]+Z, AB = p[A+1]+Z,      // HASH COORDINATES OF
 B = p[X+1]+Y, BA = p[B]+Z, BB = p[B+1]+Z;      // THE 8 CUBE CORNERS,

 return lerp(w, lerp(v, lerp(u, grad(p[AA  ], x, y, z   ),  // AND ADD
 grad(p[BA  ], x-1, y, z   )), // BLENDED
 lerp(u, grad(p[AB  ], x, y-1, z   ),  // RESULTS
 grad(p[BB  ], x-1, y-1, z   ))),// FROM  8
 lerp(v, lerp(u, grad(p[AA+1], x, y, z-1 ),  // CORNERS
 grad(p[BA+1], x-1, y, z-1 )), // OF CUBE
 lerp(u, grad(p[AB+1], x, y-1, z-1 ),
 grad(p[BB+1], x-1, y-1, z-1 ))));
}
static public float noise(float x) {
 int ix,iy,iz,X,Y,Z;
 float u,v,w;
 ix=(int)x;
 X = ix & 255;                  // FIND UNIT CUBE THAT    
 x -= ix;                                // FIND RELATIVE X,Y,Z


 u = x * x * x * (x * (x*6 - 15) + 10); // COMPUTE FADE CURVES

 int A = p[X  ];
 int B = p[X+1];

 return lerp(u, grad(A, x, 0, 0  ), grad(B, x-1, 0, 0  ));
}
static final float lerp(float t, float a, float b) {
 return a + t * (b - a);
}
static final float grad(int hash, float x, float y, float z) {
 /*  int h = hash & 15;                      // CONVERT LO 4 BITS OF HASH CODE
  float u = h<8 ? x : y,                 // INTO 12 GRADIENT DIRECTIONS.
  v = h<4 ? y : h==12||h==14 ? x : z;
  return ((h&1) == 0 ? u : -u) + ((h&2) == 0 ? v : -v);
  */
 switch(hash & 0xF)
 {
 case 0x0:
   return  x + y;
 case 0x1:
   return -x + y;
 case 0x2:
   return  x - y;
 case 0x3:
   return -x - y;
 case 0x4:
   return  x + z;
 case 0x5:
   return -x + z;
 case 0x6:
   return  x - z;
 case 0x7:
   return -x - z;
 case 0x8:
   return  y + z;
 case 0x9:
   return -y + z;
 case 0xA:
   return  y - z;
 case 0xB:
   return -y - z;
 case 0xC:
   return  y + x;
 case 0xD:
   return -y + z;
 case 0xE:
   return  y - x;
 case 0xF:
   return -y - z;
 default:
   return 0; // never happens
 }
}
static final int p[] = new int[512], permutation[] = {
 151,160,137,91,90,15,
 131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
 190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
 88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,
 77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
 102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,
 135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,
 5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
 223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,
 129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,
 251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,
 49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,
 138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180
};
static {
 for (int i=0; i < 256 ; i++) p[256+i] = p[i] = permutation[i];
}

static public float fbm( float x, int amt) {
 float s=0.f;
 float f=1.f;
 for(int i=0;i<amt;i++) {
   s+=noise(x)*f;
   x*=1.97f;
   f*=.5f;
 }
 return s;
}
  static public float fbm( float x, float y,float z,int amt){
 float s=0.f;
 float f=1.f;
 for(int i=0;i<amt;i++){
   s+=noise(x,y,z)*f;
   x*=1.97f;y*=1.97f;z*=1.97f;
   f*=.5f;
 }
 return s;
}

}
