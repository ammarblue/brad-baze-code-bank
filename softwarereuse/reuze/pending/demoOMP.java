package reuze.pending;

import com.software.reuze.m_NoisePerlins;

import processing.core.PApplet;
import reuze.pending.lc_omp.Work;

public class demoOMP extends PApplet {
	/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/68216*@* */
	/* !do not delete the line above, required for linking your tweak if you re-upload */
	float dx=0.0f;
	float dy=0.0f;
	float[] basetransform= {  
	  1.f,0.f,0,0,1,0
	};

	int g_targetScene=0;
	SceneState ss;

	float[][] res;

	float[] ptransform=new float[6];
	float[] pstransform=new float[6];
	float[][] pscene=null;

	int wHeight;
	int maxThreads=6;
	int currentThreads=4;
	public void setup() {  
	  size(800,600,P3D);
	  wHeight=currentThreads>=4 ? height : (height*3/4);

	  basetransform[4]=(float)width/(float)height;  // aspect ratio
	  ss=new SceneState();
	  res=new float[maxThreads][width*wHeight*3];
	  background(0);
	}

	public void mouseDragged()
	{
	  if ( mouseButton==CENTER)
	  {
	    float scv=basetransform[0];
	    float mouseScale=((float)(pmouseX- mouseX)/ (float)width )*scv
	      +((float)(pmouseY- mouseY)/ (float)height ) *scv;
	    basetransform[0]+=mouseScale;
	    basetransform[4]+=mouseScale;
	  }
	  else
	  {
	    dx+=((float)(pmouseX- mouseX)/ (float)width);
	    dy+= ((float)(pmouseY- mouseY)/ (float)height);
	  }
	  basetransform[2]=dx*basetransform[0];
	  basetransform[5]=dy*basetransform[0];
	}

	public void keyPressed() {
	  if (  keyCode != SHIFT) g_targetScene++;
	}

	public void draw()
	{
	  int t=millis();

	  // setup camera
	  boolean iscut=ss.update();
	  float[] bt=ss.setCamera();

	  int scidx=g_targetScene%ifss.length;

	  arrayCopy(bt,pstransform);
	  // do background 
	  bt[0]*=gbaseTransform[scidx][0];
	  bt[4]*=gbaseTransform[scidx][0];
	  bt[2]+=gbaseTransform[scidx][1];
	  bt[5]+=gbaseTransform[scidx][2];

	  float[][] scene=ifss[scidx]; 
	  scene=Animate(scene,ss.getAngle(),0.f);
	 scene=Distort(scene,ss.distAmt,((float)millis()/1000.f)*ss.distSpeed,ss.distOctaves);
	 
	  if ( pscene== null || iscut || pscene.length != scene.length)
	    pscene=CopyTransform(scene);

	  if ( iscut)
	    arrayCopy(bt,ptransform);

	  Weights weights=new Weights(scene);

	  int ns=ss.numSamples;
	  int ni=ss.numIterations;
	  long[] randxor= {
	    2463534242L,123456789L,362436069L,77465321L
	  };
	  lc_omp.Jobs j=new lc_omp.Jobs(4);
	  for (int i=0;i<4;i++) { 
	    int start=(i*ns)/4;
	    int end=((i+1)*ns)/4;

	    lc_omp.IWork jb=new IfsJob(scene,pscene,weights, bt,ptransform,res,end,ni,ss.outofFocus, start,randxor[i] );
	    j.add(jb,i);
	  }
	  j.await();

	  if (ss.updateMB)
	    pscene=CopyTransform(scene);
	  else
	    pscene=Animate(ifss[scidx],0,0);

	  arrayCopy(bt,ptransform);

	  int t2=millis();
	  loadPixels();
	  float v=width>256 ? 48 : 64;
	  v=width>512 ? 12 : 64;
	  float sc=basetransform[0]/v;
	  ConvertToScreen(sc);

	  updatePixels();
	  ss.setPerf((float)(millis()-t));
	}
	int startOffset;
	void ConvertToScreen(float sc) {
	  int jsize=wHeight*width;//
	  startOffset=((height-wHeight)/2)*width;
	  //pixels.length/8;
	  lc_omp.Jobs j=new lc_omp.Jobs(8);
	  for(int i=0;i<8;i++) {
	    lc_omp.IWork jb=new ConvertToScreenJob(sc, i*jsize/8, min((i+1)*jsize/8,jsize),pixels); 
	    j.add(jb, i);
	  }
	  j.await();
	}
	class ConvertToScreenJob implements lc_omp.IWork
	{
	  int st;
	  int ed;
	  float sc;
	  int[] pix;
	  ConvertToScreenJob(float _sc, int _st, int _ed, int[] _pix) {
	    st=_st;
	    ed=_ed;
	    sc=_sc;
	    pix=_pix;
	  }
	  public boolean evaluate(Work w) {
		int ThreadId=w.task;
	    float[] r0=res[0];
	    float[] r1=res[1];
	    float[] r2=res[2];
	    float[] r3=res[3];
	    float[] r4=res[4];
	    float[] r5=res[5];
	    int i=st*3;   
	    int io=((height-wHeight)/2)*width;
	    if ( currentThreads<=2)
	    {
	      for(int j=st;j<ed;j++) {
	        float r= (r0[i]+r1[i])*sc;
	        r0[i]=r1[i]=0;
	        float fr= min(sqrt(r),1);
	        i+=1;
	        float g= (r0[i]+r1[i])*sc;
	        r0[i]=r1[i]=0;
	        float fg=min(sqrt(g),1);
	        i+=1;
	        float b= (r0[i]+r1[i])*sc;
	        r0[i]=r1[i]=0;
	        float fb=min(sqrt(b),1);
	        pix[io+j]=((int)(fr*255.)<<16)|((int)(fg*255.)<<8)|(int)(fb*255.)|(0xFF<<24);
	        i++;
	      }
	    }
	    else if ( currentThreads<=4)
	    {
	      for(int j=st;j<ed;j++) {
	        float r= (r0[i]+r1[i]+r2[i]+r3[i])*sc;
	        r0[i]=r1[i]=r2[i]=r3[i]=0;
	        float fr= min(sqrt(r),1);
	        i+=1;
	        float g= (r0[i]+r1[i]+r2[i]+r3[i])*sc;
	        r0[i]=r1[i]=r2[i]=r3[i]=0;
	        float fg=min(sqrt(g),1);
	        i+=1;
	        float b= (r0[i]+r1[i]+r2[i]+r3[i])*sc;
	        r0[i]=r1[i]=r2[i]=r3[i]=0;
	        float fb=min(sqrt(b),1);
	        pix[io+j]=((int)(fr*255.)<<16)|((int)(fg*255.)<<8)|(int)(fb*255.)|(0xFF<<24);
	        i++;
	      }
	    }
	    else
	    {
	      for(int j=st;j<ed;j++) {
	        float r= (r0[i]+r1[i]+r2[i]+r3[i]+r4[i]+r5[i])*sc;
	        r0[i]=r1[i]=r2[i]=r3[i]=r4[i]=r5[i]=0;
	        float fr= min(sqrt(r),1);
	        i+=1;
	        float g= (r0[i]+r1[i]+r2[i]+r3[i]+r4[i]+r5[i])*sc;
	        r0[i]=r1[i]=r2[i]=r3[i]=r4[i]=r5[i]=0;
	        float fg=min(sqrt(g),1);
	        i+=1;
	        float b= (r0[i]+r1[i]+r2[i]+r3[i]+r4[i]+r5[i])*sc;
	        r0[i]=r1[i]=r2[i]=r3[i]=r4[i]=r5[i]=0;
	        float fb=min(sqrt(b),1);
	        pix[io+j]=((int)(fr*255.)<<16)|((int)(fg*255.)<<8)|(int)(fb*255.)|(0xFF<<24);
	        i++;
	      }
	    }
		return true;
	  }
	}

	float VanDerCorput(int n2) {
	  long n = (long)n2;
	  n = (n << 16L) | (n >> 16L);
	  n = ((n & 0x00ff00ffL) << 8L) | ((n & 0xff00ff00L) >> 8L);
	  n = ((n & 0x0f0f0f0fL) << 4L) | ((n & 0xf0f0f0f0L) >> 4L);
	  n = ((n & 0x33333333L) << 2L) | ((n & 0xccccccccL) >> 2L);
	  n = ((n & 0x55555555L) << 1L) | ((n & 0xaaaaaaaaL) >> 1L);
	  n = (n &0xFFFFFFFFL);
	  return (float)n /  ( (float)((long)0x100000000L));
	}

	final int TransformLength=10;
	float[][] CopyTransform( final float[][] transforms) {
	  float[][] fv=new float[transforms.length][TransformLength];
	  arrayCopy(transforms,fv);
	  return fv;
	}
	float[][] Animate(float[][] transforms, float ang,float scale) {
	  float[] rmat=new float[TransformLength];
	  float[][] r = new float[transforms.length][TransformLength];
	  for(int i=0;i<transforms.length;i++) {
	    float a = ang*transforms[i][9];
	    rmat[0]=cos(a);
	    rmat[1]=-sin(a);
	    rmat[2]=sin(a);
	    rmat[3]=cos(a);
	    rmat[4]=0.5f*ang;
	    rmat[5]=0.5f*ang;
	    rmat[6]=1;
	    rmat[7]=1;
	    rmat[8]=1;   
	    r[i] = concatTrans( transforms[i], rmat);
	  }
	  return r;
	}
	float[][] Distort(float[][] transforms, float amt, float f, int numOctaves) {
	  float[][] r = new float[transforms.length][TransformLength];
	  for(int i=0;i<transforms.length;i++) {
	    for(int j=0;j<9;j++, f+=134.345f)
	      r[i][j]=transforms[i][j]+m_NoisePerlins.fbm(f,numOctaves)*amt;
	  }
	  return r;
	}
	float[] concatTrans( float[] a, float[] b) {
	  float[] r=new float[TransformLength];
	  //concat mtx
	  r[0]=a[0]*b[0]+a[1]*b[2];
	  r[1]=a[0]*b[1]+a[1]*b[3];
	  r[4]=a[0]*b[4]+a[1]*b[5]+a[4]; 
	  r[2]=a[2]*b[0]+a[3]*b[2];
	  r[3]=a[2]*b[1]+a[3]*b[3]; 
	  r[5]=a[2]*b[4]+a[3]*b[5]+a[5];

	  r[6]=a[6]*b[6];       
	  r[7]=a[7]*b[7];       
	  r[8]=a[8]*b[8];
	  r[9]=b[9];
	  return r;
	}

	class Weights
	{
	  int[] wtab;
	  float[] ptab;
	  float[][] wcols;


	  Weights(float[][] transforms) {

	    float[] w=new float[transforms.length];
	    ptab=new float[(transforms.length+1)*2];
	    wcols = new float[transforms.length][3];
	    float sum=0.f;

	    for (int i=0;i<w.length;i++) {
	      float v=getDeterminant(transforms[i])+0.0000001f;
	      float pv=1.f;//1./8.
	      float r=pow(transforms[i][6],pv);
	      float g=pow(transforms[i][7],pv);
	      float b=pow(transforms[i][8],pv);
	      float lum=1;//r*0.3+g*0.6+b*0.1;
	      // rescale
	      wcols[i][0]=r/lum;
	      wcols[i][1]=g/lum;
	      wcols[i][2]=b/lum;
	      w[i]=v*lum;
	      sum+=w[i];
	    }
	    for (int i=0;i<w.length;i++) {
	      float f=w[i];
	      w[i]/=sum;
	      w[i]*=512.f;
	      if ( i>0) {
	        w[i]+=w[i-1];
	      }
	      ptab[i*2+2]=w[i]/512.f;
	    }
	    ptab[0]=0.f;

	    // calculate ptab for stratified sampling
	    for (int i=0;i<w.length;i++) {
	      ptab[i*2+1]=1/(ptab[i*2+2]-ptab[i*2]);
	    }

	    wtab= new int[512];
	    int ci=0;
	    for (int i=0;i<512;i++) {
	      while( w[ci]<((float)i))
	        ci++;
	      wtab[i]=ci;
	    }
	  }
	}

	float getDeterminant( float[] t) {
	  return abs(t[0]*t[3]-t[1]*t[2]);
	}
	class IteratedTransform
	{
	  float m0,m1,m2,m3,m4,m5;
	  float r,g,b;
	}

	class IfsJob implements lc_omp.IWork
	{
	  final float[][] transforms;
	  final float[][] ptransforms;
	  final Weights weights;
	  final float[] base;
	  final float[] pbase;
	  final  float[][] res;
	  final int numSamples;
	  int numiterations;
	  float dofAmt;
	  int stamples;
	  long randxorseed;

	  IfsJob( float[][] _transforms,float[][] _ptransforms, Weights _weights,float[] _base, float[] _pbase,
	  float[][] _res,  int _numSamples, int _numiterations, float _dofAmt, int _stamples, long _randxor)
	  {
	    transforms=_transforms;
	    ptransforms=_ptransforms;
	    weights=_weights;
	    base=_base;
	    pbase=_pbase;
	    res=_res;
	    numSamples=_numSamples;
	    numiterations=_numiterations;
	    dofAmt=_dofAmt;
	    stamples=_stamples;
	    randxorseed=_randxor;
	  }
	  public boolean evaluate(Work w) {
	    ifs(transforms,ptransforms,weights, base,pbase,res[w.task],numSamples,numiterations,dofAmt, stamples,randxorseed);
	    return true;
	  }
	};
	void ifs( float[][] transforms,float[][] ptransforms, Weights weights,float[] base, float[] pbase, float[] res,
	int numSamples, int numiterations, float dofAmt, int stamples, long randxor)
	{
	  IteratedTransform[] ttrans= new IteratedTransform[transforms.length]; 
	  for (int i=0;i<ttrans.length;i++) {
	    ttrans[i]=new IteratedTransform();
	    ttrans[i].r=weights.wcols[i][0];
	    ttrans[i].g=weights.wcols[i][1];
	    ttrans[i].b=weights.wcols[i][2];
	  }     

	  IteratedTransform[] twtab=new IteratedTransform[weights.wtab.length];
	  for (int i=0;i<weights.wtab.length;i++)
	    twtab[i]=ttrans[weights.wtab[i]];

	  float ww=(width-1);
	  float hh=(wHeight-1);
	  dofAmt*=10.f;
	  final float nscale=1/65535.f;


	  for (int j=stamples;j<numSamples;j++) {   
	    float cx=VanDerCorput(j);  
	    float cy=(float)j/(float)numSamples;

	    float blurx=dofAmt*(float)(randxor&0xFFFF)*nscale;
	    randxor^=(randxor<<13);
	    randxor=(randxor>>17);
	    randxor^=(randxor<<5);
	    float blury=dofAmt*(float)(randxor&0xFFFF)*nscale;
	    randxor^=(randxor<<13);
	    randxor=(randxor>>17);
	    randxor^=(randxor<<5);
	    float time=(float)(randxor&0xFFFF)*nscale;
	    float itime=1-time;

	    float scx=pbase[0]*itime*ww+base[0]*time*ww;
	    float scy=pbase[4]*itime*hh+base[4]*time*hh;
	    float tranx=pbase[2]*itime*ww+base[2]*time*ww;
	    float trany=pbase[5]*itime*hh+base[5]*time*hh;

	    for (int i=0;i<ttrans.length;i++) {
	      ttrans[i].m0=ptransforms[i][0]*itime+transforms[i][0]*time;
	      ttrans[i].m1=ptransforms[i][1]*itime+transforms[i][1]*time;
	      ttrans[i].m2=ptransforms[i][2]*itime+transforms[i][2]*time;
	      ttrans[i].m3=ptransforms[i][3]*itime+transforms[i][3]*time;
	      ttrans[i].m4=ptransforms[i][4]*itime+transforms[i][4]*time;
	      ttrans[i].m5=ptransforms[i][5]*itime+transforms[i][5]*time;
	    }

	    float r=1,g=1,b=1,a=1;//,sx=scx,sy=scy;
	    // float time=randxor
	    for(int i=0;i<numiterations;i++) {

	      // http://www.jstatsoft.org/v08/i14/paper
	      randxor^=(randxor<<13);
	      randxor=(randxor>>17);
	      randxor^=(randxor<<5);

	      final IteratedTransform t=twtab[(int)randxor&511];
	      float nx=t.m0*cx+t.m1*cy+t.m4;
	      cy=t.m2*cx+t.m3*cy+t.m5;
	      cx=nx;

	      if ( i>17) { 
	        float ia=1-a;
	        r=t.r*a +ia*r;
	        g=t.g*a +ia*g;
	        b=t.b*a +ia*b;
	        a*=0.99f;

	        float px=cx*scx+tranx +blurx;
	        float py=cy*scy+trany+blury;
	        if ( px>0. && px<ww && py>0. && py<hh) {
	          int idx=((int)(px)+(int)(py)*width)*3;
	          res[idx+0]+=r;
	          res[idx+1]+=g;
	          res[idx+2]+=b;
	        }
	      }
	    }
	  }
	}
	float[][] BarnsleyFern= {
			  {
			    0,0,0,0.16f,0,0,0.8f,1,0.6f,1
			  }
			  ,
			  {
			    0.2f,-0.26f, 0.23f, 0.22f,0.0f,1.60f,1.f,1,0.5f,1
			  }
			  ,
			  {
			    -0.15f,0.28f, 0.26f, 0.24f,0.0f,0.44f,0.6f,0.4f,0.3f,0.6f
			  }
			  ,
			  {
			    0.85f, 0.04f,-0.04f,0.85f,0.0f,1.60f,0.7f,0.8f,0.6f,0.6f
			  }
			  ,
			};
			float[][] SierpinskiTri= {
			  {
			    0.5f,0.f,0.f,0.5f,0.010f,0.010f, 1,0.3f,0.3f,1.f
			  }
			  ,
			  {
			    0.5f,0.f,0.f,0.5f,0.50f,0.010f, 0.3f,0.3f,1,-1
			  }
			  ,
			  {
			    0.5f,0.f,0.f,0.5f,0.50f,0.50f, 0.3f,1,0.3f,.5f
			  }
			  ,
			};
			float[][] SierpinskiCarpet= {
			  {
			    0.33f,0.f,0.f,0.33f,0.00f,0.00f, 1f,0.6f,0.6f,-1.f
			  },
			  {
			    0.33f,0.f,0.f,0.33f,0.33f,0.00f, 0.6f,0.6f,1f,0.4f
			  }
			  ,
			  {
			    0.33f,0.f,0.f,0.33f,0.66f,0.00f, 0.6f,1f,0.6f,1
			  }
			  ,
			  {
			    0.33f,0.f,0.f,0.33f,0.00f,0.33f, 1f,0.6f,0.6f,-1.f
			  }
			  ,
			  {
			    0.33f,0.f,0.f,0.33f,0.66f,0.33f, 0.6f,0.6f,1,1
			  }
			  ,
			  {
			    0.33f,0.f,0.f,0.33f,0.0f,0.66f, 0.6f,1f,0.6f,-1
			  }
			  ,
			  {
			    0.33f,0.f,0.f,0.33f,0.33f,0.66f, 1f,0.6f,0.6f,0.4f
			  }
			  ,
			  {
			    0.33f,0.f,0.f,0.33f,0.66f,0.66f, 0.6f,0.6f,1,1
			  }
			  ,
			};
			// from http://www.inf.uni-konstanz.de/gk/pubsys/publishedFiles/WiSa04.pdf
			float[][] image4AFrame0= {
			  {
			    -0.2960f,0.0469f,-0.0469f,-0.2960f,0.3791f,0.5687f, 0.4f,0.5f,1.f,0
			  }
			  ,
			  {
			    0.8302f,0.4091f,-0.4091f,0.8302f,-0.0674f,0.3319f,0.8f,0.3f,0.9f,-1
			  }
			};
			float[][] image2CFrame0= {
			  {
			    0.6416f, 0.3591f, -0.3591f, 0.6416f, 0.1480f, 0.3403f, 1.1901f, 1.0f, 1.3398f,-1
			  }
			  ,
			  {
			    0.1906f, -0.2554f, 0.2554f, 0.1906f, 0.4162f, 0.6122f, 1.1901f, 1.0f, 0.0000f,0
			  }
			  ,
			  {
			    0.1681f, -0.2279f, 0.2279f, 0.1681f, 0.4531f, -0.0205f, 1.1901f, 0.4087f, 0.4256f,-1
			  }
			  ,
			  {
			    -0.2848f, -0.0141f,0.0141f, -0.2848f, 0.3362f, 0.8164f,0.3780f, 0.4087f, 1.3398f,0
			  }
			  ,
			  {
			    0.3672f, 0.0051f, -0.0051f, 0.3672f, 0.0776f, 0.1726f, 1.1901f, 1.2867f, 1.3398f,1
			  }
			  ,
			};
			float[][] image3BFrame0= {
			  {
			    0.7155f, -0.4589f, 0.4589f, 0.7155f, 0.3412f, -0.0939f, 0.9988f, 1.0624f, 1.1281f,1
			  }
			  ,
			  {
			    0.2362f, -0.1849f, 0.1849f, 0.2362f, 0.2160f, 0.0852f, 0.9988f, 1.0624f, 0.4910f,-1
			  }
			  ,
			  {
			    0.2819f, 0.1849f, 0.1025f, -0.3205f, 0.5670f, 0.3792f, 0.9988f, 0.4625f, 0.4910f,1
			  }
			  ,
			  {
			    0.1080f, 0.2799f, -0.2799f, 0.1080f, 0.3303f, 0.9098f, 0.9988f, 1.0624f, 1.1281f,-1
			  }
			  ,
			};
			float[][] dragon= {
			  {
			    0.824074f, 0.281482f,     -0.212346f, 0.864198f,-1.882290f,-0.110607f, 0.9f,0.3f,0.1f,-1
			  }
			  ,
			  {
			    0.088272f, 0.520988f,     -0.463889f,-0.377778f,0.785360f, 8.095795f, 0.6f,0.5f,1.2f,1
			  }
			};
			float[][] dragon2= {
			  {
			    0.5f, -0.5f,           0.5f,0.5f,0f,0f, 0.9f,0.9f,0.4f,1
			  }
			  ,
			  {
			    0.5f, 0.5f,            -0.5f,0.5f,0.5f, 0.65f, 1.1f,0.3f,0.2f,-1
			  }
			};
			float[][][] ifss= {
			  dragon2,SierpinskiCarpet,dragon,image4AFrame0,SierpinskiTri,BarnsleyFern,image2CFrame0,image3BFrame0
			};
			float[][] gbaseTransform= {
			  {
			    0.5f,0.25f,0.25f
			  }
			  , 
			  {
			    1.f,0.f,0.f
			  }
			  ,
			  {
			    0.06f,0.5f,0.1f
			  }
			  ,
			  {
			    1.f,0.f,0.f
			  }
			  ,
			  {
			    1.f,0.f,0.f
			  }
			  ,
			  {
			    0.1f,0.5f,0.0f
			  }
			  ,
			  {
			    1.f,0.f,0.f
			  }
			  ,
			  {
			    1.f,0.f,0.f
			  }
			  ,
			};
			class SceneState
			{
			  boolean useHandHeld=false;
			  boolean useAutoPan=false;
			  boolean updateMB=true;
			  boolean useRotate=false;//false;
			  boolean useNoiseRotate=false;
			  boolean useRandomSpaz=false;
			  float rotationSpeed=1.f;
			  float timeOffset;
			  float time;
			  float dt;
			  float lastcut=0.f;
			  float lasttime=0;
			  float outofFocus=0.f;
			  float panSpeed=1.f;
			  float pillisec=0.f;
			  float backDof=1.f;
			  int numSamples=0;
			  int numIterations=150;
			  float angFlip=1;
			  int initalDisplay=0;
			boolean usedef=false;
			  
			 // distort anim
			 int   distOctaves=2;
			 float distAmt=0.0f;
			 float distSpeed=1.f;
			  
			  void setState() {
			    if (usedef)
			      return;
			    useHandHeld=random(1.f)<.3f;
			    if (!useHandHeld) {
			      useAutoPan=true;
			      panSpeed=random(0.1f,1.f);
			      backDof=1.f + random(1.f)<.2f ? 1.f : 0.f;
			    }
			    timeOffset=random(60.f);
			    if (initalDisplay<ifss.length){
			      g_targetScene = initalDisplay;
			      initalDisplay++;
			      return;
			    }
			    updateMB = random(1.f)>.3f;
			  
			     
			    useRotate=random(1.f)<.5f;
			    if (!useRotate){
			      useNoiseRotate=random(1.f)<.5f;
			    }
			    distAmt=random(1.f)<0.4f ? random(0.01f,0.25f) : 0.f;
			    distOctaves=(int)random(1.f,3.99f);
			    distSpeed=random(0.3f,2.f);
			 
			    useRandomSpaz=random(1.f)<0.15f;
			    rotationSpeed=random(0.1f,3.f);
			    angFlip = random(1.f)<0.3f ? -1 : 1;
			    if (random(1.f)<.3f)
			      g_targetScene = (int)random(ifss.length);
			  
			    if (random(1.f)<0.1f ){ // fade in
			      numSamples=-1600;
			      numIterations=150;
			    }
			  }
			  SceneState() {
			    setState();
			  }
			  
			  boolean update() {  
			    boolean iscut=false;
			    float ctime=(float)millis()/10000.f;
			    if ( random(1.f)<0.025f && (ctime-lastcut)>0.4f ) {
			      setState();
			      iscut=true;
			      lastcut=ctime;
			      println("update ");
			    }
			    dt=ctime-lasttime;
			    lasttime=ctime;
			    time=ctime + timeOffset;
			    return iscut;
			  }
			  float getAngle() {
			    float angrange=PI*angFlip; 
			    float ang=0.f;   
			    if ( useRotate ) {
			      float stime=(sin(PI*time*rotationSpeed))*.5f+.5f;//*dir;
			      ang+=stime*angrange;
			    }
			    
			     if ( useNoiseRotate ) {
			      float stime=(m_NoisePerlins.fbm(time*rotationSpeed*2,3))*.5f+.5f;//*dir;
			      ang+=stime*angrange;
			    }
			    if ( useRandomSpaz && false) {
			     float spaz=  min((m_NoisePerlins.fbm(time*32+6690.56f,4)*.5f+.5f)*1.3f,1.f);
			      spaz = pow(spaz,16);
			      ang+= spaz*PI/2;
			    }
			    return ang;
			  }
			  float[] setCamera() {
			  
			    float[] bt= new float[6];
			    arrayCopy(basetransform,bt);
			    outofFocus=0.f;
			    backDof=1.f;
			    if (useHandHeld ) {
			      noiseDetail(6,0.6f);
			      float camsc=  m_NoisePerlins.fbm(time,4)*0.8f;
			      bt[2]+=(m_NoisePerlins.fbm(time*4,4))*0.1;
			      bt[5]+=(m_NoisePerlins.fbm(time*4+12890.56f,4))*0.2;
			      
			      bt[5]-=bt[0]*camsc*.5;
			      bt[2]-=bt[4]*camsc*.5;
			      bt[0]*=(1+camsc);
			      bt[4]*=(1+camsc);
			      outofFocus =  max(m_NoisePerlins.fbm(time*8+6690.56f,4)*1.5f,0.f);
			      backDof= 1.f+max(1.f-outofFocus*3.f,0)*1.f;
			      //     outofFocus=outofFocus.;
			    }
			    if ( useAutoPan ) {
			      float camsc=  (m_NoisePerlins.fbm(time*1*panSpeed+6690.56f,2));
			      camsc=camsc*1.5f;
			      bt[2]+=m_NoisePerlins.fbm(time*4*panSpeed+12890.56f,2)*.5;
			      bt[5]+=m_NoisePerlins.fbm(time*4*panSpeed,2)*.5;
			      bt[5]-=bt[0]*camsc*.5;
			      bt[2]-=bt[4]*camsc*.5;
			      
			      bt[0]*=(1+camsc);
			      bt[4]*=(1+camsc);
			    }
			    return bt;
			  }
			  void setPerf(float millisecs) {
			    //if ( true)      return;
			    float m=pillisec*0.7f+millisecs*0.3f;
			    float target=29;//29;
			    if ( m>(target+2)) {
			      numSamples-=100;
			      numIterations-=1;
			  //    println("("+m+")"+" down "+numSamples + " " +numIterations);
			    }
			    else if ( m<(target-4)) {
			      numSamples+=100;
			      numIterations+=1;
//			      println("("+m+")"+" up "+numSamples + " " +numIterations);
			    }
			    pillisec=m;
			  }
			};

}
