import processing.core.*;

public class Main extends PApplet{
	public static void main(String args[]){
		PApplet.main(new String[] {"--present","Main"});
	}
	/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/73427*@* */
	/* !do not delete the line above, required for linking your tweak if you re-upload */
	// distance fractals
	//http://iquilezles.org/www/articles/menger/menger.htm
	//http://iquilezles.org/www/articles/distfunctions/distfunctions.htm
	// See http:www.iquilezles.org/articles/menger/menger.htm for the // full explanation of how this was done
	 
	// camera shots
	 
	// shots
	 
	 
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
	  return min(mc,maxv(di,(float) 0.).mag());
	}
	float udRoundBox( PVector p, PVector b, float r ) {
	  return maxv(PVector.sub(abs(p),b),(float) 0.0).mag()-r;
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
	 
	class Mine extends PApplet implements isofunction
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
	class Mine2 extends PApplet implements isofunction
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
	class surf2 extends PApplet implements isofunction {
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
	 
	class ballWorld extends PApplet implements isofunction {
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
	class boxWorld extends PApplet implements isofunction {
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
	class pyramid extends PApplet implements isofunction {
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
	class Orbit extends PApplet
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
	class menger extends PApplet implements isofunction
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
	class fbox extends PApplet implements isofunction {
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
	class mandelbulb extends PApplet implements isofunction
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
	class mandelbox extends PApplet implements isofunction
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
	class HartverdratFract extends PApplet implements isofunction
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
 
	class PolarCamera extends PApplet
	{
	  PVector  m_camPos = new PVector(0.0f,0.,-6.f);
	  float   m_angX = 0.f;
	  float   m_angY = 0.f;
	  PVector  m_camDir;
	  PolarCamera()
	  {
	    update(0,0);
	  }
	  PolarCamera(PVector cp, float dx, float dy)
	  {
	    m_camPos.set(cp);
	    update(dx,-dy);
	    highquality=true;
	    g_genBlkInfo=true;
	  }
	  void update(float dy, float dz)
	  {
	    m_angX += dy;
	    m_angY += -dz;
	    m_camDir = new PVector(sin(m_angX)*cos(m_angY),
	    sin(m_angY),cos(m_angX)*cos(m_angY));
	    m_camDir.normalize();
	    highquality=false;
	  }
	  float GetStrength() {
	    isofunction scene=Scenes[scIndex];
	    float minDist=scene.map(m_camPos.get());
	    float d= max(min(abs(minDist*.25),0.2),0.000001);
	    // println(" minDist "+minDist + " d "+d);
	    return d;
	  }
	  void forward(float v )
	  {
	    m_camPos.add(PVector.mult(m_camDir,v*GetStrength()));
	    highquality=false;
	  }
	  void side(float v )
	  {
	    PVector f =m_camDir.cross(new PVector(0,1,0));
	    if (abs(m_camDir.y)>0.99)
	      f=m_camDir.cross(new PVector(1,0.,0));
	 
	    f.normalize();
	    m_camPos.add(PVector.mult(f,v*GetStrength()));
	    highquality=false;
	  }
	}
	PolarCamera g_camControl=new PolarCamera();
	void mouseDragged()
	{
	  float dz=(float)(mouseY-pmouseY)/(float)height;
	  float dy=(float)(mouseX-pmouseX)/(float)width;
	  g_camControl.update(dy,-dz);
	}
	void mousePressed() {
	  if (g_menu.active) {
	    g_menu.select((mouseButton == RIGHT));
	  }
	}
	 
	// TODO: just use this class
	class Shot extends PApplet
	{
	  String name="<enter name>";
	  String desc="<enter desc>";
	  String links="<enter hlink>";
	  int    sceneId;
	  PVector cpos;
	  float   crx;
	  float   cry;
	  boolean flight;
	  boolean reflection;
	  void select() {
	    println("selected "+name );
	    g_camControl=new PolarCamera(cpos,crx,cry);
	    flashlight=flight;
	    doreflection=reflection;
	    scIndex=sceneId;
	    g_frameStep=0;
	  }
	  void save() {
	    cpos=g_camControl.m_camPos.get();
	    crx=g_camControl.m_angX;
	    cry=g_camControl.m_angY;
	    flight=flashlight;
	    sceneId=scIndex;
	    reflection=doreflection;
	  }
	  String write() {
	    String s=name+","+desc+","+links+","+sceneId+",";
	    s=s+cpos.x+","+cpos.y+","+cpos.z+","+crx+","+cry+",";
	    s=s+flight+","+reflection;
	    return s;
	  }
	  Shot() {
	  }
	  Shot(String sline) {
	    String[] s=sline.split(","); 
	    int v=0;
	    name=s[v++];
	    desc=s[v++];
	    links=s[v++];
	    sceneId=(int)Float.valueOf(s[v++]).floatValue();
	    cpos=new PVector();
	    cpos.x=Float.valueOf(s[v++]).floatValue();
	    cpos.y=Float.valueOf(s[v++]).floatValue();  
	    cpos.z=Float.valueOf(s[v++]).floatValue();
	    crx=Float.valueOf(s[v++]).floatValue();
	    cry=Float.valueOf(s[v++]).floatValue();
	    flight=s[v++].charAt(0)=='t';
	    reflection=s[v++].charAt(0)=='t';
	  }
	};
	Shot[] g_cameraShots;
	Menu   g_menu;
	void setupCameraShots() {
	  String[] sshots=loadStrings("camerashots.txt");
	  g_cameraShots=new Shot[sshots.length];
	  for(int i=0;i<sshots.length;i++)
	    g_cameraShots[i]=new Shot(sshots[i]);
	  g_menu= new Menu(g_cameraShots);
	}
	class Menu extends PApplet
	{
	  String[] items;
	  String[] desc;
	  String[] links;
	  boolean active=false;
	  int selected=0;
	  Shot[]  m_shots;
	  final int fontSize=24;
	  Menu( Shot[] shots) {
	    m_shots=shots;
	    items=new String[shots.length+1];
	    desc=new String[shots.length+1];
	    links=new String[shots.length+1];
	    for(int i=0;i<shots.length;i++) {
	      items[i+1]=shots[i].name;
	      desc[i+1]=shots[i].desc;
	      links[i+1]=shots[i].links;
	    }
	    items[0]="Demo Mode";
	    desc[0]="Cycle through all scenes";
	   links[0]="http://blog.hvidtfeldts.net/index.php/2011/06/distance-estimated-3d-fractals-part-i/";
	    PFont fontA = loadFont("ArialMT-24.vlw"); 
	    textFont( fontA,fontSize);
	  }
	  int over() {
	    return min((mouseY)/fontSize, items.length-1);
	  }
	  void draw() {  
	    if (!active) {
	      return;
	    }
	    textMode(SCREEN);
	    int h=min(items.length*fontSize+fontSize/2,height);
	    int cs=60;
	    noStroke();
	    fill(color(255,255,255,128));
	    rect(0,0,width/2,h);
	    for(int i=0;i<items.length;i++) {     
	      fill( over()==i ? 0: color(32,32,32,196));
	      text(items[i],20,fontSize+i*fontSize);
	    }
	 
	    fill(color(255,255,255,128));
	    rect(0,height-fontSize-fontSize/2,width,height-fontSize/2);
	    fill(color(0,0,0,196));
	 
	    text(desc[ over()],fontSize/2,height-(fontSize/2));
	  }
	  boolean select(boolean rightOption) {
	     if ( !active) {
	      active=true;
	      return false;
	    }
	   
	    if (mouseX>width/2 || mouseY/fontSize > items.length-1)  {
	      active=false;
	      return false;
	    }
	     
	    selected =  over();
	    if ( rightOption) {
	      link(links[selected]);
	    }
	   
	    if ( selected==0) {
	        g_demoMode=g_demoMode>0 ? 0: 1;
	        g_frameStep=10000;
	      }
	      else {
	        m_shots[selected-1].select();
	        g_demoMode=0;
	      }
	   
	    active=false;
	    return true;
	  }
	};
	 
	// surfaces
	 
	// pov scenes
	interface isofunction
	{
	 void update();
	  void colormap( PVector p, PVector col, PVector surf, float ddx);
	  float map(PVector p);
	  boolean isinterior();
	  float fudgefactor();
	  PVector glowCol();
	}
 
	float intersect( isofunction f, PVector ro, PVector rd, float maxt, float eps_factor, int glow[], float ff, float st) {
	  int s=0;
	  int maxsteps=200;
	  float lh=0.;
	 
	  PVector p=ro.get();
	  for(float t=st;t<maxt;)
	  {
	    float eps=t*eps_factor;
	    p.x=ro.x+rd.x*t;
	    p.y=ro.y+rd.y*t;
	    p.z=ro.z+rd.z*t;
	    float h = f.map(p);
	    if( h<eps)
	    {
	      glow[0]=s;
	      if ( h<0.)
	        t+=h;
	      t-=lh;
	      return  t;
	    }
	    if( t>maxt || s ==maxsteps) { 
	      glow[0]=s;
	      return -1.;
	    }
	    t += h*ff;
	    lh=h*ff;
	    s++;
	  }
	  glow[0]=s;
	  return -1.;
	}
	float g_eps_factor;
	float stFactorFov;
	 
	float intersectStepback( isofunction f, PVector ro, PVector rd,
	float maxt, float[] glow, float ff, float eps_factor, float minT) {
	  int s=0;
	  int maxsteps=200;
	  float eps=0.000006;
	  boolean hit=false;
	  PVector p=ro.get();
	 
	  for(float t=minT;t<maxt;)
	  {
	    p.x=ro.x+rd.x*t;
	    p.y=ro.y+rd.y*t;
	    p.z=ro.z+rd.z*t;
	    float h = f.map(p);
	    if( hit && h<eps )
	    {
	      glow[0]=(float)(s-1)/32.;    
	      return  t;
	    }
	    if( s ==maxsteps) { 
	      glow[0]=(float)s/32.;
	      return -1.;
	    }
	    t += h*ff;
	    eps=t*eps_factor;
	    if ( h<eps) {
	      hit=true;
	    }
	    s++;
	  }
	  glow[0]=(float)s/32.;
	  return -1.;
	}
	float shadow( isofunction f, PVector ro, PVector rd, float mint, float maxt, float k, float startt, float ff) {
	  float r = 1.0;
	  PVector p=ro.get();
	  for(float t=mint;t<maxt;)
	  {
	    float  eps_factor = (startt+t*3.) * g_eps_factor;
	    p.x=ro.x+rd.x*t;
	    p.y=ro.y+rd.y*t;
	    p.z=ro.z+rd.z*t;
	    float h = f.map(p);
	    if( h<eps_factor || r<0.01) {
	      return 0.f;
	    }
	    r=min(r,k*h/t);
	    t += h*ff;
	  }
	  return r;
	}
	final float ttDx=-1./sqrt(6.);
	final float ttDx2=sqrt(2)/sqrt(3.);
	final float ttDy=1./sqrt(2.);
	final float ttDz=1./sqrt(3);
	final float coneAngle=tan(radians(30.));
	 
	// mm, could do sky lighting with this
	float aoTriTap( isofunction f, PVector ro, PVector nrm, float eps, float toff, float ff) {
	  PVector tg = new PVector();
	  PVector bi=new PVector();
	  if ( abs(nrm.y)<0.99f)
	  {
	    // cross 
	    bi.x=nrm.z;
	    bi.z=-nrm.x;
	    bi.y=0.;
	    float bimag=1./sqrt(bi.x*bi.x+bi.z*bi.z);
	    bi.x*=bimag;
	    bi.z*=bimag;
	 
	    tg.x=nrm.y*bi.z;
	    tg.y=nrm.z*bi.x-nrm.x*bi.z;
	    tg.z=-nrm.y*bi.x;
	  }
	  else
	  {
	    tg = new PVector(1.f,0.f,0.f);
	    bi = tg.cross(nrm);
	    bi.normalize(); 
	 
	    tg = nrm.cross(bi);
	  }
	   int numsteps=3;
	  PVector rdir0=new PVector();  
	  PVector rdir1=new PVector(); 
	  PVector rdir2=new PVector();
	  float dx,dy,dz;
	 //http://www.valvesoftware.com/publications/2007/SIGGRAPH2007_EfficientSelfShadowedRadiosityNormalMapping.pdf
	  dx=ttDx;
	  dy=ttDy;
	  dz=ttDz;
	  rdir0.x = tg.x*dx + nrm.x*dz + bi.x*dy;
	  rdir0.y = tg.y*dx + nrm.y*dz + bi.y*dy;
	  rdir0.z = tg.z*dx + nrm.z*dz + bi.z*dy;   
	 
	  dy=-dy;
	  rdir1.x = tg.x*dx + nrm.x*dz + bi.x*dy;
	  rdir1.y = tg.y*dx + nrm.y*dz + bi.y*dy;
	  rdir1.z = tg.z*dx + nrm.z*dz + bi.z*dy;     
	  dx=ttDx2;
	  rdir2.x = tg.x*dx + nrm.x*dz;
	  rdir2.y = tg.y*dx + nrm.y*dz;
	  rdir2.z = tg.z*dx + nrm.z*dz;       
	  float dst=150.f;
	  return aoCone(f,ro,rdir0,coneAngle,eps*dst,0.75f*.5,numsteps)*.33+
	    aoCone(f,ro,rdir1,coneAngle,eps*dst,0.5f*.5,numsteps)*.33+
	    aoCone(f,ro,rdir2,coneAngle,eps*dst,0.25f*.5,numsteps)*.33;
	}
	 
	float aoCone(isofunction f, PVector ro, PVector rd, float tanangle, float sampdist, float toff, int numSamples) {
	  float fv=1.;
	  float dv=sampdist/((float)numSamples);
	  float sd=dv*(toff/((float)numSamples));//+toff;
	   PVector p=ro.get();
	  for(int i=0;i<numSamples;i++) {
	    p.x=ro.x+rd.x*sd;
	    p.y=ro.y+rd.y*sd;
	    p.z=ro.z+rd.z*sd;
	    float d= f.map(p);
	    float r= sd*tanangle;   
	    float f0=smoothblend(max(min((d+r)/(2.*r),1.),0.));
	    sd+=dv;
	    fv*=f0;
	  }
	  return fv;
	}
	// TODO : create cone function using half life cone angles
	float ao( isofunction f, PVector ro, PVector rd, float eps, float toff, float aocf, float ff, float cangle) {
	  float occ=1.;
	  int nsteps= cangle==1. ? 4 : 5;
	  float aos=2.;
	  float so=eps*9. * aos;
	  float aoc=.15/so;
	  float sd=so*(2.-toff);
	  PVector p=ro.get();
	 
	  for(int i=0;i<nsteps;i++) {
	    p.x=ro.x+rd.x*sd;
	    p.y=ro.y+rd.y*sd;
	    p.z=ro.z+rd.z*sd;
	   
	    float d=f.map(p);//*ff;
	    occ-=max(( sd - d*cangle)*aoc,0.);
	    sd += so;
	    aoc *=aocf;
	  }
	  return max(min(occ,1.),0.);
	}
	 
	PVector calcNormal(isofunction f, PVector p, float epsf) {
	  float eps=max(epsf*.5,1.5e-7);
	  PVector nor=new PVector();
	  PVector p0=new PVector(p.x+eps,p.y,p.z);
	  PVector p1=new PVector(p.x-eps,p.y,p.z);
	  nor.x = f.map(p0) - f.map(p1);
	  p0.set(p.x,p.y+eps,p.z);
	  p1.set(p.x,p.y-eps,p.z);
	  nor.y = f.map(p0) - f.map(p1);
	  p0.set(p.x,p.y,p.z+eps);
	  p1.set(p.x,p.y,p.z-eps);
	  nor.z = f.map(p0) - f.map(p1);
	  nor.normalize();
	  return nor;
	}
	class traceBatch extends PApplet implements WorkItem
	{
	  int s;
	  int e;
	  traceBatch( int _s,int _e) {
	    s=_s;
	    e=_e;
	  }
	  void run( int tid) {
	    trace(pixels,s,e);
	  }
	}
	final float fog(float t, float b )  // eq to exp(-b*t)
	{
	  double val = (double)(-b*t);
	  final long tmp = (long) (1512775 * val + 1072632447);
	  return (float)Double.longBitsToDouble(tmp << 32);
	}
	float pow_schlick(float a, float b)
	{
	  return (a / (b - a * b + a));
	}
	float gf=1.;
	float skyf=1.;
	float horf=1.;
	 
	PVector bgcol=new PVector(0.1*skyf,0.45*skyf,0.95*skyf);
	PVector horcol=new PVector(0.9*horf,0.9*horf,0.9*horf);
	PVector gcol=new PVector(0.4*gf,0.3*gf,0.1*gf);
	 
	void getDirAmbient(PVector c,float v) {
	  float iv=1.-v;
	  c.x=gcol.x*iv+bgcol.x*v;
	  c.y=gcol.y*iv+bgcol.y*v;
	  c.z=gcol.z*iv+bgcol.z*v;
	}
	void getSky(PVector c,float v) {
	  float lv= abs(v);
	  lv=sqrt(lv);
	  float ilv=1.-lv;
	  if (v<0.) {
	    c.x=horcol.x*ilv+bgcol.x*lv;
	    c.y=horcol.y*ilv+bgcol.y*lv;
	    c.z=horcol.z*ilv+bgcol.z*lv;
	  }
	  else {
	    c.x=horcol.x*ilv+gcol.x*lv;
	    c.y=horcol.y*ilv+gcol.y*lv;
	    c.z=horcol.z*ilv+gcol.z*lv;
	  }
	}
	class TraceState extends PApplet
	{
	  float reflectivity=1.;
	  float specularity=1.0;
	  float diffuse=1.;
	  float slight=1.f;
	  PVector lightpos =new PVector(-1.0*16.,-0.8*16.,-0.6*16.);
	  // bgcol.set(0.,0.,0.);
	  PVector lcol=new PVector(1.0,0.8,0.7);
	  PVector albedo=new PVector(1.,0.3,0.1);
	  PVector surf=new PVector(0.,0.,0.);
	  float ff;
	 PVector glowcol;
	  boolean interior;
	 
	  TraceState( isofunction scene) {   
	    lcol.mult(slight);
	    glowcol=scene.glowCol();
	    ff=scene.fudgefactor();
	    if ( flashlight) {
	      // shoot out shadow ray
	      PVector fld=new PVector(0.2,-0.2,-0.2);
	      fld.normalize();
	      int[] glow=new int[2];
	      float tv = intersect(scene, g_camControl.m_camPos.get(), fld, 0.25, stFactorFov, glow,ff,0.);
	      if ( tv<0.)
	        tv=0.25;
	      if ( tv<0.2) {
	        tv=tv*0.75;
	      }
	      fld.mult(tv);
	      lightpos=PVector.add( g_camControl.m_camPos.get(), fld);
	    }
	    interior=scene.isinterior();
	  }
	}
	boolean calcAO=true;
	final float refOccAngle=tan(radians(15.));
	PVector traceRay( isofunction scene,PVector col, PVector ro, PVector rd, TraceState ts,
	int depth, float minT, int iglow ) {
	  float[] glow=new float[2];
	  PVector skycol=new PVector();
	  PVector surf=new PVector();
	  float epsf=g_eps_factor;
	  float t =-1;
	  if ( minT>=0)
	    t= intersectStepback(scene, ro,rd,64.,glow,ts.ff,epsf,minT);
	  glow[0]+=(float)iglow/32.;
	 
	  if( t>0.0 )
	  {
	    float aof =1.;
	    PVector pos = PVector.add(ro,PVector.mult( rd,t));
	    PVector nor = calcNormal(scene, pos,epsf*t*.5); // allow for 4X ss
	 
	    // shadow
	    getDirAmbient(col,-nor.y*.5+.5);
	    scene.colormap(pos,ts.albedo, surf,t*epsf);
	 
	    if (!openglmode && !diffonly)
	    {
	      if (tritapao && depth==0)
	        aof=aoTriTap( scene, pos, nor, stFactorFov*t,1.,ts.ff);
	      else
	        aof=ao( scene, pos, nor, stFactorFov*t,1., .5,ts.ff,1.);
	    }
	    PVector ldir=PVector.sub(ts.lightpos,pos);
	    float llen=ldir.dot(ldir);
	    float falloff=flashlight ? min(.5/llen,1.) : 1.01;
	    llen=sqrt(llen);
	    ldir.mult(1./llen);
	    float dif1 = max(nor.dot(ldir),0.0)*max(falloff-0.01,0.);
	 
	    float spec=0.;
	    float eps=epsf*t*1.5;
	    pos.x+=nor.x*eps;
	    pos.y+=nor.y*eps;
	    pos.z+=nor.z*eps;
	 
	    if (approxbounce)   
	    {
	      PVector bcol=new PVector(1.,0.5,0.25);
	      bcol.add(0.25,0.25,0.25);
	      bcol.mult(aof*aof*2.);
	      col.mult(new PVector(min(bcol.x,1.),min(bcol.y,1.),min(bcol.z,1.)));
	    }
	    else
	      col.mult(aof);
	 
	    surf.x=ts.reflectivity;
	 
	 
	    // change to slider
	    float ismetal=0.;// should do as part of the surface
	    float isplastic=1.;
	    surf.x*=max(ismetal,isplastic);
	 
	    // if isplastic reduce diffuse by fresnel (energy conservation)
	    ts.albedo.mult(ts.diffuse);
	 
	 
	    if (diffonly||aoonly)
	      surf.x=0;
	 
	 
	    if ( dif1>0. && !diffonly && !aoonly) {
	 
	      float shadowf =1.;
	      if (!openglmode){
	        shadowf=shadow( scene, pos, ldir,0.0,llen,64.,t,ts.ff);
	      //  shadowf=aoCone(scene,pos,ldir,refOccAngle,llen,0.5f,12);
	    }
	        
	      dif1*=shadowf;
	      // blkparams[1]+=shadowf;
	      col =PVector.add(PVector.mult(ts.lcol,dif1),col);
	      PVector rdinv=rd.get();
	      rdinv.mult(-1.);
	      // super sample normals?
	      PVector lhalf=PVector.add(rdinv,ldir);
	      lhalf.normalize();
	      spec=dif1*pow_schlick( max( nor.dot(lhalf),0.),64.)*64.*ts.specularity*surf.x;
	    }
	 
	    surf.x+=ismetal;
	    PVector specalbedo=ts.albedo.get();
	 
	    lerp(specalbedo, new PVector(1.,1.,1.), ts.albedo,ismetal);
	    if (noalbedo)
	      ts.albedo.set(0.7,0.7,0.7);
	 
	    //   lerp(ts.albedo, new PVector(.5,.5,.5), ts.albedo,1.-ismetal);
	    // lerp to zero on ismetal. ( can't do it due to lack of proper reflections)
	    col.mult(ts.albedo);
	 
	    PVector I=rd.get();
	    float facing=I.dot(nor);
	    float fresnel=0.;
	    float fv=lerp(0.035,0.5,ismetal);
	    fresnel=min(max( (fv+ (1.-fv)*pow_schlick(1+facing,5)),0),1.);
	 
	    //col.set(0.,0.,0.);
	    if (depth==0 && surf.x>0.00001 && doreflection && fresnel>0.1 && !ts.interior) {
	      PVector rdr=new PVector();
	      float rfresnel=max(fresnel-0.1,0.)*1.1;
	 
	 
	      float f2=facing*2.;
	 
	 
	      rdr.x=I.x-nor.x*f2;
	      rdr.y=I.y-nor.y*f2;
	      rdr.z=I.z-nor.z*f2;
	      rdr.normalize();
	      // TODO : use roughness to change refleciton occlusion factor
	      float reflect=1.;
	 
	      PVector rfcol=new PVector();
	      getSky(rfcol,rdr.y) ;
	      if (!openglmode) {
	        if (approxrefOcc)
	          reflect=aoCone(scene,pos,rdr,refOccAngle,eps*100.,0.0f,8);
	        else
	          reflect=shadow( scene, pos, rdr,0.,10.,16.,t,ts.ff)*1.5;  // cheap reflections would be nice?        
	        rfcol.mult(reflect);
	      }
	      // blkparams[0]+=reflect;
	 
	      col.x+=rfcol.x*rfresnel*surf.x;
	      col.y+=rfcol.y*rfresnel*surf.x;
	      col.z+=rfcol.z*rfresnel*surf.x;
	    }
	 
	    if (spec>0.)
	      col =PVector.add(PVector.mult(ts.lcol,spec*fresnel*surf.x),col);
	 
	 
	    col.mult(specalbedo);
	 
	    //if (fresnel>0.2)
	    //col.set(fresnel,fresnel,fresnel);
	 
	    float fgv= fog(t,0.09);
	    float ifv=1.-fgv;
	    getDirAmbient(skycol,-rd.y*.5+.5);
	    col.x =col.x*fgv + ifv*skycol.x;
	    col.y =col.y*fgv + ifv*skycol.y;
	    col.z =col.z*fgv + ifv*skycol.z;
	 
	    if (aoonly) {
	      if (approxbounce) {
	        PVector bcol=new PVector(1.,0.5,0.25);
	        bcol.add(0.25,0.25,0.25);
	        bcol.mult(aof*aof*2.);
	        col.set(new PVector(min(bcol.x,1.),min(bcol.y,1.),min(bcol.z,1.)));
	      }
	      else
	        col.set(aof,aof,aof);
	    }
	    else if (diffonly)
	      col.set(ts.albedo.x,ts.albedo.y,ts.albedo.z);
	 
	    //  shadowf=shadowf*.5+.5;
	 
	    if (shownormal) {
	      getDirAmbient(col,-nor.y*.5+.5);
	      // col.set(nor.x*.5+.5,nor.y*.5+.5,nor.z*.5+.5);
	    }
	    // col.set(rdr.x*.5+.5,rdr.y*.5+.5,rdr.z*.5+.5);
	    // col.set(t/12.,t/12.,t/12.);
	  }
	  else
	  {
	     getSky(skycol,rd.y);
	    col.set(skycol.x,skycol.y,skycol.z);
	  }
	  //ts.glowcol.set(1.,1.,1.);
	  col.add( glow[0]*ts.glowcol.x,glow[0]*ts.glowcol.y,glow[0]*ts.glowcol.z);
	  return col;
	}
	final int skipStep=4;
	int[] sampleOrder= {
	  0,0, 2,2, 0,2, 2,0,
	  1,1, 3,3, 1,3, 3,1,
	  0,1, 2,3, 0,3, 2,1,
	  1,0, 3,2, 3,0, 1,2,
	};
	 
	float[] g_mint=null;
	int[] g_glow=null;
	void setupMinTBuffers() {
	  int blksize=(skipStep/2)*(skipStep/2);
	  g_mint=new float[width*height/blksize];//2];
	  g_glow=new int[width*height/blksize];
	}
	void trace(color[] pix, int sy, int ey)
	{
	  // light
	  //PVector light = normalize(new PVector(1.0,-0.8,-0.6));
	 
	  float ctime = time;
	  // camera
	  PVector ro = g_camControl.m_camPos.get();
	 
	  // ro.mult(1.1);
	  PVector ww = normalize(g_camControl.m_camDir);
	  PVector uu = normalize((new PVector(0,1.,0.)).cross( ww ));
	  PVector vv = normalize(ww.cross(uu));
	  ww.mult(1.5);
	  // ww.normalize();
	  PVector view=ww.get();
	  view.mult(-1.);
	  view.normalize();
	  int step=skipStep;
	  int offx=g_frameStep%(skipStep*skipStep);
	  int offy=sampleOrder[offx*2+1];
	  offx=sampleOrder[offx*2+0];  // Try halton type order
	 
	  float sampleOffsetx=0.f;
	  float sampleOffsety=0.f;
	  float wieghtold=1.f;
	  float wieghtnew=0.f;
	  if ( g_frameStep>=step*step && highquality) {
	    int sampleset=g_frameStep/(step*step);
	    wieghtold=1./(sampleset+1);
	    wieghtnew=1.-wieghtold;
	    sampleOffsetx=((sampleset)%2)*.5;
	    sampleOffsety=((sampleset)/2)*.5;
	    // end on 4*(step*step);
	  }
	  if (!highquality) {
	    offx=offy=0;
	  }
	 
	  if ( g_frameStep>= 4*(step*step))// could do guassian blur now
	    return;
	 
	  float aspectRatio=(float)width/(float)height;  
	  int[] gv=new int[1]; 
	  isofunction scene=Scenes[scIndex];
	  TraceState  ts=new TraceState(scene);
	  assert( highquality  ||  g_frameStep==0);
	  int blkstep=step/2;
	  if (earlystep && g_genBlkInfo || !highquality)
	  {
	    if (!highquality)
	      blkstep *=4;
	    int blkwidth=(width/blkstep);
	    float eps=g_eps_factor*(float)blkstep*0.5f*1.7320508f;
	    for (int py=sy;py<ey;py+=blkstep*2)
	    {
	      float y = -1.0 + 2.0 *( (float)py+((float)blkstep))/(float)height;
	      PVector bray=PVector.add(PVector.mult(vv,y),ww );   
	     
	      for (int px=0;px<width;px+=blkstep*2)
	      {    
	        float x = -1.0 + 2.0 *( (float)px+((float)blkstep)) /(float)width;
	        PVector rd = normalize( PVector.add(PVector.mult(uu,x*aspectRatio), bray));
	 
	        float st=intersect( scene, ro, rd, 64.,eps*2.,gv,ts.ff,0.);
	        int igv=gv[0];
	        float ystep=((float)blkstep/2.f);
	        int idx=(py/blkstep)*blkwidth + px/blkstep;   
	        for (int i=0;i<2;i++) {
	          x = -1.0 + 2.0 *( (float)px+((float)blkstep)/2.f) /(float)width;         
	          y = -1.0 + 2.0 *( (float)py+ystep)/(float)height;
	          bray=PVector.add(PVector.mult(vv,y),ww ); 
	          rd = normalize( PVector.add(PVector.mult(uu,x*aspectRatio), bray));
	         
	          g_mint[idx]=intersect( scene, ro, rd,64.,g_eps_factor*(float)blkstep*0.5f*1.7320508f,gv,ts.ff,st);
	          g_glow[idx++]=gv[0]+igv;
	         
	          x = -1.0 + 2.0 *( (float)px+((float)blkstep)*3./2.) /(float)width;
	          rd = normalize( PVector.add(PVector.mult(uu,x*aspectRatio), bray));
	         
	          g_mint[idx]=intersect( scene, ro, rd,64.,g_eps_factor*(float)blkstep*0.5f*1.7320508f,gv,ts.ff,st);
	          g_glow[idx++]=gv[0]+igv;
	          idx+=blkwidth-2;
	          ystep+=(float)blkstep;
	         
	        }
	      }
	    }
	  }
	  PVector surf=new PVector(0.,0.,0.);
	  for (int py=sy+offy;py<ey;py+=step)
	  {
	    float y = -1.0 + 2.0 * ((float)py+sampleOffsety) /(float)height;
	    PVector col=new PVector(0.,0.,0.);
	    PVector bray=PVector.add(PVector.mult(vv,y),ww );
	    int midx=(py/blkstep)*(width/blkstep);   
	    for (int px=offx;px<width-step;px+=step)
	    {
	      float x = -1.0 + 2.0 * ((float)px+sampleOffsetx) /(float)width;
	      x *= aspectRatio;
	      PVector rd = normalize( PVector.add(PVector.mult(uu,x), bray));
	      col.set(0.,0.,0.);
	      float mint=0.;
	      int glowv=0;
	      if (  earlystep ) {
	        int xidx=px/blkstep;
	        mint=g_mint[midx+xidx];
	        glowv=g_glow[midx+xidx];
	      }
	      col=traceRay( scene,col,ro, rd,ts,0,mint,glowv);
	 
	      if (wieghtold !=1.) {  /// do progressive AA
	        int cin=pix[py*width+px];
	        float cr=(float)((cin>>16)&0xFF)*1./255.f;
	        float cg=(float)((cin>>8)&0xFF)*1./255.f;
	        float cb=(float)((cin)&0xFF)*1./255.f;
	        col.x=min(col.x,1.)*wieghtold+cr*cr*wieghtnew;
	        col.y=min(col.y,1.)*wieghtold+cg*cg*wieghtnew;
	        col.z=min(col.z,1.)*wieghtold+cb*cb*wieghtnew;
	      }
	      int r=(int)(min(sqrt(col.x),1.)*255.);
	      int g=(int)(min(sqrt(col.y),1.)*255.);
	      int b=(int)(min(sqrt(col.z),1.)*255.);
	      int c= r<<16|g<<8|b|0xFF<<24;
	      pix[py*width+px] =c;
	      if ( g_frameStep<4) {
	        int idx=py*width+px;
	        pix[idx+1] =c;
	        pix[idx+width] =c;
	       pix[idx+width+1] =c;
	      }
	      if ( !highquality) {  // TODO : could do in chunks
	        // c=0xFF<<24;
	        int idx=py*width+px;
	        for(int j=0;j<skipStep;j++) {
	          pix[idx+0] =c;
	          pix[idx+1] =c;
	          pix[idx+2] =c;
	          pix[idx+3] =c;
	          pix[idx+4] =c;
	          idx+=width;
	        }
	      }
	    }
	  }
	}
	WorkQueue g_rqueue;
	boolean highquality=false;
	boolean tritapao=true;
	boolean diffonly=false;
	boolean aoonly=false;
	boolean aa=false;
	boolean earlystep=true;
	boolean approxrefOcc=false;
	boolean approxbounce=true;
	boolean shownormal=false;
	boolean noalbedo=false;
	boolean flashlight=false;
	boolean openglmode=false;
	float[] fbuffer;
	boolean doreflection=true;
	int scIndex=0;
	void keyPressed() {
	  if (key=='f')
	    flashlight=!flashlight;
	  if (key=='p') {
	    tritapao=!tritapao;
	    println(" tri tap ao "+tritapao);
	  }
	  if (key=='t')
	    diffonly=!diffonly;
	  if (key=='i')
	    aoonly=!aoonly;
	  if (key=='e')
	    earlystep=!earlystep;
	  if (key=='r')
	    doreflection=!doreflection;
	  if (key=='b')
	    approxbounce=!approxbounce;
	  if (key=='w')
	    g_camControl.forward(1.);
	  if (key=='s')
	    g_camControl.forward(-1);
	  if (key=='a')
	    g_camControl.side(1.);
	  if (key=='d')
	    g_camControl.side(-1);
	  if (key=='n')
	    scIndex=(scIndex+1)%Scenes.length;
	  if(key=='k')
	    shownormal=!shownormal;
	  if(key=='l')
	    noalbedo=!noalbedo;
	  if (key==' ')
	    g_menu.select(false);
	  if (key=='c')
	    calcAO=!calcAO;
	  if (key=='m')
	  {
	    Shot s=new Shot();
	    s.save();
	    println(s.write());
	  }
	  if (key=='o')
	    openglmode=!openglmode;
	  g_frameStep=0;
	  g_genBlkInfo=true;
	}
	public void setup() {
	  int numProc=Runtime.getRuntime().availableProcessors();
	  g_rqueue=new WorkQueue(numProc);
	  size(1580,950,P3D);
	  stFactorFov=2.0*.743294*1.0*.6/((float)height);
	  // stFactorFov*=.8f;// allow for 4X SS
	  setupCameraShots();
	  setupMinTBuffers();
	  background(~0);
	  int startScene=(int)random(0,g_cameraShots.length);
	  g_cameraShots[startScene].select();
	}
	 
	int g_frameStep=0;
	boolean  g_genBlkInfo=true;
	int g_demoMode=0;
	int[] oldPix=null;
	public void draw() {
	  g_eps_factor = stFactorFov;
	  int t=millis();
	  time=millis()/1000.f;
	 
	  Scenes[scIndex].update();
	 
	  if (!highquality) {
	    g_frameStep=0;
	    g_genBlkInfo=false;
	  }
	  loadPixels();
	  if (oldPix!=null) {
	    arraycopy(oldPix,0,pixels,0,oldPix.length);
	    oldPix=null;
	  }
	  int step=skipStep*8;  
	  for (int i=0;i<height;i+=step)
	    g_rqueue.execute( new traceBatch(i,min(i+step,height-skipStep)));
	  g_rqueue.Wait();
	 
	  updatePixels();
	  g_frameStep++;

	  if (false){ 
	    fill(0);
	    rect(width-60,0,50,20);
	    fill(~0);
	    text((millis()-t),width-60+10,20);
	  }
	  if ( g_menu.active) {
	    oldPix=new color[pixels.length];
	    arraycopy(pixels,0,oldPix,0,oldPix.length);
	  }
	  g_menu.draw();
	  if (!highquality) {
	    highquality=true;
	    g_genBlkInfo=true;
	  }
	  else {
	    g_genBlkInfo=false;
	  }
	 
	  if (g_demoMode>0 && g_frameStep>8*skipStep*skipStep) {
	    g_cameraShots[(g_demoMode-1)%g_cameraShots.length].select();
	    g_demoMode++;
	  }
	}

}
