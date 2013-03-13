 
// Fractals
 
 
isofunction[] Scenes= {
  new mandelbox(3),
  new mandelbox(2),
  new mandelbulb(),
  new menger(0),
  new mandelbox(1),
  new menger(2),
  new menger(1),
  new pyramid(),
  new Mine2(),
  new surf2(),
 
  new mandelbox(0),
  new HartverdratFract(0),  // add multiple versions as this is awesome
  new HartverdratFract(1),  // add multiple versions as this is awesome
  new HartverdratFract(2),  // add multiple versions as this is awesome
  new HartverdratFract(3),  // add multiple versions as this is awesome
  new HartverdratFract(4),  // add multiple versions as this is awesome
    new mandelbox(5),
  new boxWorld(),
 
  new ballWorld(),
  new Mine(),
 
};
 
float time;
PVector sqrt( PVector p) {
  return new PVector(sqrt(p.x),sqrt(p.y),sqrt(p.z));
}
PVector abs( PVector p) {
  return new PVector(abs(p.x),abs(p.y),abs(p.z));
}
PVector mod( PVector p, float v) {
  return new PVector(abs(p.x)%v,abs(p.y)%v,abs(p.z)%v);
}
 
PVector maxv( PVector p, float v) {
  return new PVector(max(p.x,v),max(p.y,v),max(p.z,v));
}
float sdCylinderCross(PVector p, float r) {
  return sqrt(min(min( p.x*p.x+p.y*p.y,
  p.x*p.x+p.z*p.z),
  p.y*p.y+p.z*p.z))-r;
}
float sdTorus( PVector p, float or, float ir )
{
  PVector q = new PVector(sqrt(p.x*p.x+p.z*p.z)-ir,p.y);
  return length(q)-or;
}
PVector add( PVector p, float v) {
  return new PVector(p.x+v,p.y+v,p.z+v);
}
PVector normalize( PVector p) {
  PVector a=p.get();
  a.normalize();
  return a;
}
float length(PVector p) {
  return p.mag();
}
 
float maxcomp(PVector p ) {
  return max(p.x,max(p.y,p.z));
}
float sdBox2( PVector p, PVector b )
{
  float dix=abs(p.x)-b.x;
  float diy=abs(p.y)-b.y;
  float diz=abs(p.z)-b.z;
  float mc=max(dix,max(diy,diz));
  return mc;
}
 
float sdBox( PVector p, PVector b )
{
  PVector  di = PVector.sub(abs(p),b);
  float mc = maxcomp(di);
  return min(mc,maxv(di,0.).mag());
}
float udRoundBox( PVector p, PVector b, float r ) {
  return maxv(PVector.sub(abs(p),b),0.0).mag()-r;
}
float sdSphere( PVector p, float s )
{
  return length(p)-s;
}
float map3(PVector p, float tmat[]) {
  return udRoundBox(p, new PVector(1.,1.,1.),0.4);
  //return sdSphere(p,1.);
}
 
float smoothblend(float x) {
  return x*x*(3-2*x);
}
float smoothstep (float edge0, float edge1, float x)
{
  x =  min(max((x - edge0) / (edge1 - edge0),0.0f),1.0f);
  return x*x*(3-2*x);
}
 
float[] sinVals=null;
float fastcos( float y) {
  return fastsin(y+PI/2);
}
float fastsin( float y) {
  int v=(int)(y*1024./(2*PI));
  if ( sinVals==null) {
    sinVals=new float[1024];
    for(int i=0;i<1024;i++)
      sinVals[i]=sin((float)(i/1024.)*2.*PI);
  }
  return sinVals[v&1023];
}
 
class Mine  implements isofunction
{
  float map(PVector p) {
    float d1=(p.mag()-1.);
    float at=1.;
    return d1-(Noise.noise(abs(p.x+11.5)*20.,abs(p.y+11.5)*20.,abs(p.z+11.5)*15.)*.5+.5)*0.2*at;
  }
  boolean isinterior() {
    return false;
  }
  float fudgefactor() {
    return 1./3.;
  }
  void update() {
  }
  void colormap( PVector p, PVector col, PVector surf,float ddx) {
    float nv=Noise.noise(abs(p.x+11.5)*20.,abs(p.y+11.5)*20.,abs(p.z+11.5)*15.);
    nv =smoothstep(-.2,.2,nv);
    col.set(nv,nv*.5+0.1,nv*.25+0.2);
    surf.x*=nv;
 }
  PVector glowCol() {
    return new PVector(0.,0.,0.);
  }
}
class Mine2  implements isofunction
{
  float nv(PVector p) {
    // todo use derivatives
    return abs(Noise.noise(abs(p.x+11.5)*2.15,abs(p.y+11.5)*3.15,abs(p.z+11.5)*2.15));
  }
 
  float map(PVector p) {
    float d1=(p.mag()-1.);
    return d1-nv(p)*0.4;
  }
  boolean isinterior() {
    return false;
  }
  float fudgefactor() {
    return 1./3.; // Lipschitz bound of noise
  }
  void update() {
  }
  void colormap( PVector p, PVector col, PVector surf,float ddx) {
    float v=nv(p);
    v =smoothstep(0.1,.2,v);
    lerp(col, new PVector(1.,0.,0.), new PVector(1.,0.,1.),v);
    surf.x*=v;
  }
  PVector glowCol() {
    return new PVector(0.,0.,0.);
  }
}
float planeDist(float d, float thickness) {
  return max(abs(d)-thickness,0.);
}
float sdCylinder(PVector p, float r) {
  return sqrt(p.x*p.x+p.z*p.z)-r*r;
}
class surf2 implements isofunction {
  float map(PVector p) {
    return sdSphere(p,1.);
  }
  void update() {
  }
  void colormap( PVector p, PVector col, PVector surf,float ddx) {
  }
  boolean isinterior() {
    return false;
  }
 
  float fudgefactor() {
    return 1.;
  }
  PVector glowCol() {
    return new PVector(0.,0.,0.);
  }
}
 
class ballWorld implements isofunction {
  float map(PVector p) {
 
    PVector cp=p.get();
    cp.y-=1.;
 
    float phase=((int)(p.x))*0.4f;
    float tc=fastcos(time+phase);
    cp.y+=tc*0.15;
    cp.x=(abs(cp.x)%1 )-.5;
    cp.z=(abs(cp.z)%1 )-.5;
 
    return min(sdSphere(cp,.3),.3-(p.y-2.));
  }
  void update() {
  }
  void colormap( PVector p, PVector col, PVector surf,float ddx) {
    if(p.y<1.35)
      col.set(1.,0.,0.);
    else
      col.set(0.,0.,1.);
  }
  boolean isinterior() {
    return false;
  }
  float fudgefactor() {
    return 1.;
  }
  PVector glowCol() {
    return new PVector(0.,0.,0.);
  }
}
class boxWorld implements isofunction {
  boolean switchModel(float pz) {
    return ( (((int)(pz*.5))&0x1) !=0);
  }
  float map(PVector p) {
    PVector cp=p.get();
    cp.y-=2.;
    cp.x=(abs(cp.x)%2 )-1.;
    cp.z=(abs(cp.z)%2 )-1.;
    float phase=((int)(p.x*.5))*0.1f;
    float tc=fastcos(time+phase);
    float ts=fastsin(time+phase);
    float px=tc*cp.x-ts*cp.z;
    float pz=ts*cp.x+tc*cp.z;
    cp.x=px;
    cp.z=pz;
    float de=0.;
    if (switchModel(p.z)) {
      de= sdTorus( cp,0.2,0.3 );
    }
    else
      de=udRoundBox(cp,new PVector(0.3,0.3,0.3),.1);
 
    // could rotate on xz plane?
    return min(de,.4-cp.y);
  }
  void update() {
  }
  void colormap( PVector p, PVector col, PVector surf,float ddx) {
    if(p.y<2.35) {
      if (switchModel(p.z))
        col.set(1.,0.,0.);
      else
        col.set(1.,1.,0.);
    }
    else
      col.set(0.,0.,1.);
  }
  boolean isinterior() {
    return false;
  }
  float fudgefactor() {
    return 1.;
  }
  PVector glowCol() {
    return new PVector(0.,0.,0.);
  }
}
// do base KIFS
// http://blog.hvidtfeldts.net/index.php/2011/08/distance-estimated-3d-fractals-iii-folding-space/
class pyramid implements isofunction {
  final int iterations=8;
  float Scale=2.;
  PVector cx=new PVector(1.,1.,0.5);
 
  float cosangle0;
  float sinangle0;
  float cosangle1;
  float sinangle1;
 
  void setAngles(float angle, float angle1) {
    cosangle0=cos(angle);
    sinangle0=sin(angle);
    cosangle1=cos(angle1);
    sinangle1=sin(angle1);
  }
  pyramid() {
    setAngles( -0.1,0.1);
  }
 
  float map(PVector p) {
    PVector cp=p; 
 
    // cp.x-=3.;
    float ss=1.;
    for(int i=0;i<iterations ;i++) {
 
      // Rotation around z-axis
      float x2 = cosangle0*cp.x + sinangle0*cp.y;
      float y2 = -sinangle0*cp.x + cosangle0*cp.y;
      cp.x = x2;
      cp.y = y2;
 
      // fold
      if ( (cp.x+cp.y)<0.) { // fold 1
        float t=cp.x;
        cp.x=-cp.y;
        cp.y=-t;
      }
      if ( (cp.x+cp.z)<0.) { // fold 2
        float t=cp.x;
        cp.x=-cp.z;
        cp.z=-t;
      }
      if ( (cp.y+cp.z)<0.) { // fold 3
        float t=cp.z;
        cp.z=-cp.y;
        cp.y=-t;
      }
 
      // Reverse rotation around z-axis
      x2 = cosangle1*cp.x + sinangle1*cp.y;
      y2 = -sinangle1*cp.x + cosangle1*cp.y;
      cp.x = x2;
      cp.y = y2;
 
      // scale
      cp.x=cp.x*Scale-cx.x*(Scale-1.);
      cp.y=cp.y*Scale-cx.y*(Scale-1.);
      cp.z=cp.z*Scale-cx.z*(Scale-1.);   
      ss*=1./Scale;
    }
    return  (cp.mag()-2.)* ss;//pow( Scale,-(float)iterations);
  }
  void update() {
    float a0=sin(time/8+121.78)*PI/16;
    float a1=cos(time/8+25.18)*PI/16;
    setAngles(a0,-a0);
  }
  void colormap( PVector p, PVector col, PVector surf,float ddx) {
  }
  boolean isinterior() {
    return false;
  }
  float fudgefactor() {
    return 1.;
  }
  PVector glowCol() {
    return new PVector(0.1,0.2,1.);
  }
}
class Orbit
{
  float[] orbit=new float[4];
  Orbit( PVector z0) {
    orbit[0]=abs(z0.x);
    orbit[1]=abs(z0.y);
    orbit[2]=abs(z0.z);
    orbit[3]=z0.mag();
  }
  void sample(PVector z0) {
    orbit[0]=min(abs(z0.x),orbit[0]);
    orbit[1]=min(abs(z0.y),orbit[1]);
    orbit[2]=min(abs(z0.z),orbit[2]);
    orbit[3]=min(z0.mag(),orbit[3]);
  }
  float getMinAxis() {
    return min(min(orbit[2],orbit[1]),orbit[0]);
  }
};
 
// http://www.fractalforums.com/ifs-iterated-function-systems/kaleidoscopic-(escape-time-ifs)/15/
class menger  implements isofunction
{
  final int iterations=16;
  float cosangle0;
  float sinangle0;
  float cosangle1;
  float sinangle1;
 
  void setAngles(float angle, float angle1) {
    cosangle0=cos(angle);
    sinangle0=sin(angle);
    cosangle1=cos(angle1);
    sinangle1=sin(angle1);
  }
  float scale = 3.1;
  float CX = 1.0;
  float CY = 1.0;
  float CZ = 1.0;
 
  int sc=0;
  PVector dcol0=new PVector(1.,0.,0.);
  PVector dcol1=new PVector(1.,1.,1.);
 
  menger(int scene) {
    sc=scene;
    setAngles( -0.1,0.1);
    if (scene==1) {
      setAngles( 0.,radians(25.));
      scale=1.3;
      CX=2.;
      CY=4.8;
      CZ=0.;
      dcol0=new PVector(0.4,0.6,0.1);
    }
    else if (scene==2) {
      setAngles(0.,0.);
      scale=3;
      CX=1.1;
      CY=0.9;
      CZ=2.8;
    }
  }
  float map(PVector z0)
  {   
    float r                = length(z0);
    float t = 0.0;
    int i = 0;
    float ss=1.;
    for (i=0;i<iterations && r<60.0;i++) {
 
      // Rotation around z-axis
      float x2 = cosangle0*z0.x + sinangle0*z0.z;
      float y2 = -sinangle0*z0.x + cosangle0*z0.z;
      z0.x = x2;
      z0.z = y2;
 
 
      z0.x=abs( z0.x);
      z0.y=abs( z0.y);
      z0.z=abs( z0.z);
      if( z0.x- z0.y<0.0) {
        t= z0.y;
        z0.y= z0.x;
        z0.x=t;
      }
      if( z0.x- z0.z<0.0) {
        t= z0.z;
        z0.z= z0.x;
        z0.x=t;
      }
      if( z0.y- z0.z<0.0) {
        t= z0.z;
        z0.z= z0.y;
        z0.y=t;
      }
 
      // Reverse rotation around z-axis
      x2 = cosangle1*z0.x + sinangle1*z0.z;
      y2 = -sinangle1*z0.x + cosangle1*z0.z;
      z0.x = x2;
      z0.z = y2;
 
      z0.x=scale* z0.x-CX*(scale-1.0);
      z0.y=scale* z0.y-CY*(scale-1.0);
      z0.z=scale* z0.z;
      if( z0.z>0.5*CZ*(scale-1.0))  z0.z-=CZ*(scale-1.0);
      r=max(z0.x-1,max(z0.y-1,z0.z-1));
      ss*=1./scale;
    }
    return r*ss;//the estimated distance
  }
  PVector glowCol() {
    return new PVector(0.,0.,0.);
  }
  float orbitTrap(PVector p,float fwidth) { 
    PVector z0=p.get();
    float r                = length(z0);
    float t = 0.0;
    int i = 0;
    float ss=1.;
    float cv=min(min(abs(z0.x),abs(z0.y)),abs(z0.z));
    for (i=0;i<iterations && r<60.0;i++) {
 
      // Rotation around z-axis
      float x2 = cosangle0*z0.x + sinangle0*z0.z;
      float y2 = -sinangle0*z0.x + cosangle0*z0.z;
      z0.x = x2;
      z0.z = y2;
 
 
      z0.x=abs( z0.x);
      z0.y=abs( z0.y);
      z0.z=abs( z0.z);
      if( z0.x- z0.y<0.0) {
        t= z0.y;
        z0.y= z0.x;
        z0.x=t;
      }
      if( z0.x- z0.z<0.0) {
        t= z0.z;
        z0.z= z0.x;
        z0.x=t;
      }
      if( z0.y- z0.z<0.0) {
        t= z0.z;
        z0.z= z0.y;
        z0.y=t;
      }
 
      // Reverse rotation around z-axis
      x2 = cosangle1*z0.x + sinangle1*z0.z;
      y2 = -sinangle1*z0.x + cosangle1*z0.z;
      z0.x = x2;
      z0.z = y2;
 
      z0.x=scale* z0.x-CX*(scale-1.0);
      z0.y=scale* z0.y-CY*(scale-1.0);
      z0.z=scale* z0.z;
      if( z0.z>0.5*CZ*(scale-1.0))  z0.z-=CZ*(scale-1.0);
      r=max(z0.x-1,max(z0.y-1,z0.z-1));
      ss*=1./scale;
 
      float aaFactor=1.+max((fwidth/ss)-1.,0.);
      if ( fwidth<ss)
      {
        float v2=min(min(abs(z0.x),abs(z0.y)),abs(z0.z));     
        cv=min(v2*aaFactor,cv);
      }
    }
    return cv;
  }
  void update() {
    /*   float a0=sin(time/8+121.78)*PI/16;
     float a1=cos(time/8+25.18)*PI/16;
     if ( sc==0)
     setAngles(a0-PI/16,a1-PI/16);
     else if (sc==2)
     setAngles(a0,0.); 
     else if (sc==1)
     setAngles( 0.,radians(25.)+a0*0.2);
     //      else if (sc==2)
     //          setAngles(a0,a0);
    
     */
  }
 
  void colormap( PVector p, PVector col, PVector surf,float fwidth) {
    float fv=orbitTrap(p,fwidth*2.);
    fv=smoothstep(0.0,0.1,fv);//1.-(smoothstep(0.0,0.015,fv)-smoothstep(0.05,0.1,fv));
    lerp(col,dcol0,dcol1,fv);//smoothstep(0.0,0.1,orbitTrap(p,fwidth*2.)));
  }
  boolean isinterior() {
    return false;
  }
  float fudgefactor() {
    return 1.;
  }
}
void boxFold(PVector z, float l) {
  if ( z.x>l)
    z.x=2.*l-z.x;
  else if ( z.x<-l)
    z.x=-2.*l-z.x;
 
  if ( z.y>l)
    z.y=2.*l-z.y;
  else if ( z.y<-l)
    z.y=-2.*l-z.y;
 
  if ( z.z>l)
    z.z=2.*l-z.z;
  else if ( z.z<-l)
    z.z=-2.*l-z.z;
}
float sphereFold(PVector z, float dz, float minRad2, float fixRad2 ) {
  float r2=z.dot(z);
  float ddz=dz;
  if ( r2<minRad2) {
    // linear inner sacling
    float t=fixRad2/minRad2;
    z.mult(t);
    ddz*=t;
  }
  else if (r2<fixRad2) {
    float t=fixRad2/r2;
    z.mult(t);
    ddz*=t;
  }
  return ddz;
}
class fbox implements isofunction {
  final int iterations=5;
  float Scale=3.8925;
  float Offset=1.25;
 
  float map(PVector p) {
    Scale=4.;
    PVector cp=p.get();  
 
    // cp.x-=3.;
    float ss=1.;
    for(int i=0;i<iterations;i++) {
 
      // boxFold(cp,1);
      cp.x=abs(cp.x);
      cp.y=abs(cp.y);
      cp.z=abs(cp.z);
      cp.x=cp.x*Scale-Offset*(Scale-1.);
      cp.y=cp.y*Scale-Offset*(Scale-1.);
      cp.z=cp.z*Scale-Offset*(Scale-1.);   
      ss*=1./Scale;
    }
    float pd=min(sqrt(cp.y*cp.y+cp.z*cp.z),
    min(sqrt(cp.x*cp.x+cp.z*cp.z),sqrt(cp.x*cp.x+cp.y*cp.y)))-0.2;
    return max(sdSphere(cp,128),pd)*ss;
    //return (min(sqrt(cp*cp.x+cp.z*cp.z),sqrt(cp.x*cp.x+cp.y*cp.y))-0.01)* ss;//pow( Scale,-(float)iterations);
  }
  void update() {
  }
  void colormap( PVector p, PVector col, PVector surf,float ddx) {
  }
  boolean isinterior() {
    return false;
  }
  float fudgefactor() {
    return 1.;
  }
  PVector glowCol() {
    return new PVector(0.,0.,0.);
  }
}
//http://blog.hvidtfeldtnet/index.php/2011/09/distance-estimated-3d-fractals-v-the-mandelbulb-different-de-approximations/
class mandelbulb implements isofunction
{
  final int iterations=24;
  float map(PVector p) {
 
    // cp.x-=3.;
    float scale=2.f;
    p.mult(1./scale);
 
    float ss=1.;
    PVector w=p;
    float dr=1.;
    float r=1.;
    float Bail=8.;
    //    Bail*=Bail;
    float cxx=p.x;
    float cxy=p.y;
    float cxz=p.z;
    int i=0;
    while(true) {
      r = w.dot(w);
      if (r>Bail || i==iterations)
        break;
      i++;
      r=sqrt(r);
      float r2=r*r;
      r2=r2*r2;
      //  r=sqrt(r);
      r2=r2*r*r*r;
      dr=r2*8.*dr+1.;
 
      //http://www.iquilezles.org/www/articles/mandelbulb/mandelbulb.htm
      float x = w.x;
      float x2 = x*x;
      float x4 = x2*x2;
      float y = w.y;
      float y2 = y*y;
      float y4 = y2*y2;
      float z = w.z;
      float z2 = z*z;
      float z4 = z2*z2;
 
      float k3 = x2 + z2;
      float k2 = 1./sqrt( k3*k3*k3*k3*k3*k3*k3 );
      float k1 = x4 + y4 + z4 - 6.0*y2*z2 - 6.0*x2*y2 + 2.0*z2*x2;
      float k4 = x2 - y2 + z2;
 
      w.x =  64.0*x*y*z*(x2-z2)*k4*(x4-6.0*x2*z2+z4)*k1*k2;
      w.y = -16.0*y2*k3*k4*k4 + k1*k1;
      w.z = -8.0*y*k4*(x4*x4 - 28.0*x4*x2*z2 + 70.0*x4*z4 - 28.0*x2*z2*z4 + z4*z4)*k1*k2;
      w.x+=cxx;
      w.y+=cxy;
      w.z+=cxz;
    }  
    r=sqrt(r);
    return  .5*log(r)*r/dr*scale;
  }
  void update() {
    /*  cx.x=sin(time/15.)*.66;
     cx.y=sin(time/15.)*.1;
     cx.z=sin(time/15.)*1.;*/
  }
 
  PVector dicol0=new PVector(1.,0.8,0.4);
  PVector dicol1=new PVector(0.6,0.6,0.3);
  void colormap( PVector p, PVector col, PVector surf,float ddx)
  {
    // need derivatives
    float v=Noise.fbm((p.x+12905.)*4.16,(p.y+12905.)*4.34,(p.z+12905.)*4.25,16);
    v=smoothstep(-0.1,0.1,v);
    float sf=120.;
    v+=Noise.noise((p.x+12905.)*sf,(p.y+12905.)*sf,(p.z+12905.)*sf );
    v=smoothstep(0.,1.,v);
    surf.x*=(v*.5+.5);
    lerp(col,dicol0,dicol1,smoothstep(0.,1.,v));
  }
  PVector glowCol() {
    return new PVector(0.,0.,0.);
  }
 
  boolean isinterior() {
    return false;
  }
  float fudgefactor() {
    return 1.;
  }
}
//http://blog.hvidtfeldtnet/index.php/2011/09/distance-estimated-3d-fractals-v-the-mandelbulb-different-de-approximations/
//http://www.fractalforums.com/3d-fractal-generation/a-mandelbox-distance-estimate-formula/30/
class mandelbox implements isofunction
{
  final int iterations=16;//12;
  float Scale=2.7;
  float minRad=0.5;
  float fixRad=1.;
  float minRad2;
  float abScale;
  float fixRad2;
  float ratioFM;
  float clipDist=4.;
  int  cscene=0;
  mandelbox(int scene)
  {
    if (scene==1)
      Scale=-2.;
    else  if (scene==2) {
      clipDist=3.5;
      Scale=3.;
    }
    else  if (scene==3)
      Scale=-1.5;
   else  if (scene==4)
      Scale=2.;
     else  if (scene==5){
      Scale=2.;
      minRad=0.5;
     }
     cscene=scene;
    abScale=abs(Scale);
    minRad2=minRad*minRad;
    fixRad2 =fixRad*fixRad;
    ratioFM=fixRad2/minRad2;
  }
  float map(PVector p) {
    float Bail=64.f;
    float cxx=p.x;
    float cxy=p.y;
    float cxz=p.z;
    PVector z=p;
    float dr=1.;//Scale;
    //float clipmag=p.mag();
    for(int i=0;i<iterations;i++) {   
      if (z.x > 1.0)
        z.x = 2.0 - z.x;
      else if (z.x < -1.0)
        z.x = -2.0 - z.x;
      if (z.y > 1.0)
        z.y = 2.0 - z.y;
      else if (z.y < -1.0)
        z.y = -2.0 - z.y;
      if (z.z > 1.0)
        z.z = 2.0 - z.z;
      else if (z.z < -1.0)
        z.z = -2.0 - z.z;
 
      // sphere fold
      float r2=z.dot(z);
      if ( r2>Bail){
        break;
      }
      if ( r2<minRad2) {
        // linear inner sacling
        z.mult(ratioFM);
        dr*=ratioFM;
      }
      else if (r2<fixRad2) {
        float t=fixRad2/r2;
        z.mult(t);
        dr*=t;
      }
 
      z.x=z.x*Scale+cxx;
      z.y=z.y*Scale+cxy;
      z.z=z.z*Scale+cxz; 
      dr=dr*abScale+1.;
    }
    float BVR=1.;
    float r=z.mag()-BVR;//abs(Scale-1);
    r= r/abs(dr);
 
    // should have a clip box
    return r;//max(r,clipmag-clipDist);
  }
  void update() {
  }
 //http://www.fractalforums.com/3d-fractal-generation/amazing-fractal/75/
  PVector getColor( PVector p) {
    float pers=0.7f;
    float ipers=0.8f;
    float cxx=p.x;
    float cxy=p.y;
    float cxz=p.z;
    PVector z=p.get();
    float colourScale = 1.;//1./iterations;
    PVector totalColour = new PVector(0.,0.,0.);
    for(int i=0;i<iterations;i++) { 
      PVector colour = new PVector(0.,0.,0.);
      int numOutside = 0;
      if (abs(z.x)>1.)   numOutside++;
      if (abs(z.y)>1.)   numOutside++;
      if (abs(z.z)>1.)   numOutside++;
 
      // box fold
      if (z.x > 1.0)
        z.x = 2.0 - z.x;
      else if (z.x < -1.0)
        z.x = -2.0 - z.x;
      if (z.y > 1.0)
        z.y = 2.0 - z.y;
      else if (z.y < -1.0)
       z.y = -2.0 - z.y;
      if (z.z > 1.0)
        z.z = 2.0 - z.z;
      else if (z.z < -1.0)
        z.z = -2.0 - z.z;
 
      if (numOutside==3) {
        colour.y=.75;
        colour.x=0.25f;
      }
      else if (numOutside==2) {
        colour.y=.5;
        colour.z=0.25f;
      }
      else if (numOutside==1) {
        colour.y=.25;
        colour.x=0.25f;
        colour.z=0.25f;
      }
      // else
      {
        float r2 = z.dot(z);
        if ( r2<minRad2)
          colour.z+=2.;
        else if( r2<fixRad2)
          colour.x+=3.;
        else
          colour.y=.5;
      }
      // sphere fold
      float r2=z.dot(z);
      if ( r2<minRad2) {
        // linear inner sacling
        z.mult(ratioFM);
      }
      else if (r2<fixRad2) {
        float t=fixRad2/r2;
        z.mult(t);
      }
 
      colourScale*=pers;
      totalColour.mult(ipers);
      colour.mult(colourScale);
      totalColour.add(colour);
 
      z.x=z.x*Scale+cxx;
      z.y=z.y*Scale+cxy;
      z.z=z.z*Scale+cxz;
    }
    return totalColour;
  }
  PVector saturate(PVector p) {
    return new PVector(min(max(p.x,0.),1.),min(max(p.y,0.),1.),min(max(p.z,0.),1.));
  }
 
  PVector dcol0=new PVector(1.,0.,0.);
  PVector dcol1=new PVector(0.,1.,1.);
  void colormap( PVector p, PVector col, PVector surf,float ddx) {
    col.set(getColor(p));
     if (cscene==3){
       float lum=col.mag();
       lerp(col, new PVector(1.,1.,0.1),new PVector(0.1,0.1,1.), smoothstep(0.0,.3,lum));
     }
    //   col.normalize();
    // col.x=sqrt(col.x);col.y=sqrt(col.y); col.z=sqrt(col.z);
    // col.set(1.,1.,1.);
    //  lerp(col,dcol0,col,smoothstep(0.0,0.1,orbitTrap(p).getMinAxis()));
  }
  boolean isinterior() {
    return false;
  }
  float fudgefactor() {
    return 1.;
  }
  PVector glowCol() {
    return new PVector(0.,0.,0.);
  }
}
class HartverdratFract implements isofunction
{
  float fu; //fractal_spheresubstract
  float fd; //fractal_distancemult
  PVector cs;  //fractal_csize
  float fs; //fractal_size
  PVector fc;  //fractal_c
  PVector dicol0=new PVector(.5,0.8,0.9);
  PVector dicol1=new PVector(0.3,0.8,0.3);
  PVector gcol=new PVector(0.02,0.2,0.05);
 
  final int fractal_iterations = 11;
 
  HartverdratFract(int scene) {
    fd = 0.763;
    fu = 10.0;
    fs = 1.0;
    fc = new PVector(0.,0.,0.);
    cs = new PVector(0.80800,0.80800,1.16700);
 
    switch(scene) {
    case 1:
      fd = 0.70000;
      fs = 1.34000;
      cs.x=.5;
      cs.y=.5;
      break;
    case 2:
      cs.x=.9;
      fu=1.;
      dicol0=new PVector(.8,0.6,0.2);
      dicol1=new PVector(0.8,0.3,1.);
      gcol=new PVector(.01,0.05,0.3);
      break;
    case 3:
      fu=1.2;
      fc.z=0.2584;
      dicol0=new PVector(.8,0.6,0.2);
      dicol1=new PVector(0.8,0.3,1.);
      gcol=new PVector(0.3,0.05,0.01);
      break;
    case 4:
      fc.z=-0.38;
      dicol0=new PVector(.8,0.3,0.2);
      dicol1=new PVector(0.6,0.5,0.4);
      gcol=new PVector(.01,0.2,0.4);
 
      break;
    default:
      break;
    }
    gcol.mult(.8);
  }
  float map(PVector p) {
    float dEfactor=1.;
    float t=p.z;
    p.z=p.y;
    p.y=t;
    float Bail=1000.f;
    for(int i=0;i<fractal_iterations;i++) {
      // box fold
      if (p.x > cs.x)
        p.x = 2.0*cs.x - p.x;
      else if (p.x < -cs.x)
        p.x = -2.0*cs.x - p.x;
      if (p.y > cs.y)
        p.y = 2.0*cs.y - p.y;
      else if (p.y < -cs.y)
        p.y = -2.0*cs.y - p.y;
      if (p.z > cs.z)
        p.z = 2.0*cs.z - p.z;
      else if (p.z < -cs.z)
        p.z = -2.0*cs.z - p.z;
 
      //inversion
      float r2=p.dot(p);
     if ( r2>Bail)
        break;
      if (r2<fs) {
        float k=fs/r2;
        p.mult(k);
        dEfactor*=k;
      }
      //julia seed
      p.add(fc);
    }
    //call basic shape and scale its DE
    //need to adjust fractal_distancemult with non zero julia seed
    float lpxy=sqrt(p.x*p.x + p.y*p.y);
    float rxy=lpxy-fu;
    //distance from pos to the pseudo kleinian basic shape ...
    return (fd*max(rxy,abs(lpxy*p.z)/sqrt(p.dot(p)))/abs(dEfactor));
  }
  Orbit orbitTrap(PVector z) {
 
    PVector p=z.get();
    Orbit orb=new Orbit(p);
 
    float t=p.z;
    p.z=p.y;
    p.y=t;
    //
    for(int i=0;i<fractal_iterations;i++) {
      //box folding
      if (p.x > cs.x)
        p.x = 2.0*cs.x - p.x;
      else if (p.x < -cs.x)
        p.x = -2.0*cs.x - p.x;
      if (p.y > cs.y)
        p.y = 2.0*cs.y - p.y;
      else if (p.y < -cs.y)
        p.y = -2.0*cs.y - p.y;
      if (p.z > cs.z)
        p.z = 2.0*cs.z - p.z;
      else if (p.z < -cs.z)
        p.z = -2.0*cs.z - p.z;
 
      //inversion
      float k=max(fs/p.dot(p),1.);
      p.mult(k);
      //julia seed
      p.add(fc);  
      orb.sample(p);
    }
    return orb;
  }
  void update() {
  }
 
  void colormap( PVector p, PVector col, PVector surf,float ddx) {
    float fv=orbitTrap(p).getMinAxis();
    col.set(fv,fv,fv);
    fv=smoothstep(0.0,0.005,fv)-smoothstep(0.05,0.055,fv);
    surf.x=1.-fv;
    lerp(col,dicol0,dicol1,fv);// lerp on distance?
  }
 
  PVector glowCol() {
    return gcol;
  }
 
  boolean isinterior() {
    return true;
  }
  float fudgefactor() {
    return 1.;
  }
}
void lerp(PVector c, PVector a, PVector b, float v) {
  float iv=1.-v;
  c.x=v*b.x+iv*a.x;
  c.y=v*b.y+iv*a.y;
  c.z=v*b.z+iv*a.z;
}
