package reuze.demo;
import java.io.IOException;
import processing.core.*; 
import java.util.concurrent.atomic.AtomicInteger; 
import java.io.*;  
import java.util.*; 

public class demoEffects extends PApplet {

// ftp://69.31.121.43/developer/presentations/2005/GDC/Sponsored_Day/GDC_2005_VolumeRenderingForGames.pdf


WorkQueue  g_workers;
float[]      g_randTableInterleaved;
float[]      g_randTable;
PImage[]     g_frameBuff=new PImage[2];
AtomicInteger[] g_frameCounts=new AtomicInteger[2];
SelectScreen  g_iconSelect;
boolean      g_showIcons=true;

public void improveDistribution( float[] rt)
{
  for (int i=0;i<64;i++)
    for(int j=0;j<64;j++)
    {
      int idx=(i<<6)+j;
      float rv=rt[idx];
      boolean done=false;
      while( !done)
      {
        float d0=rt[ (i<<6)+((j+1)&63)]-rv;
        float d1=rt[ (((i+1)&63)<<6)+j]-rv;
        float d2=rt[ (((i+1)&63)<<6)+((j+1)&63)]-rv;
        d0*=d0;
        d1*=d1;
        d2*=d2;
        float md=min(min(d0,d1),d2);
        done=true;
        if ( md<(.15f*.15f))
        {
          // print("md "+md +" ix "+idx);
          rv=random(0.f,1.f);
          done=false;
        }
      }
      rt[(i<<6)+j]=rv;
    }
}
final int sc_Volume=0;
final int sc_Render=1;
final int sc_Height2=2;
final int sc_DiscoBall=3;
final int sc_Fire=4;
final int sc_SpotFog=5;
final int sc_GroovyTerrain=6;
final int sc_CrabNebula=7;
final int sc_WaavySmoke=8;
final int sc_fireball=9;
final int sc_HPSmoke=10;
final int sc_Cloudy=11;
final int sc_CubeSphere=12;
final int sc_FireBlue=13;
final int sc_FireGreen=14;
final int sc_Blobbies=15;
final int sc_FireBall2=16;
final int sc_CloudPuff=17;
final int sc_ByeBye=18;
final int sc_FireBallBlue=19;
final int sc_World=20;

public void setup()
{
  g_workers=new WorkQueue();
   size(640,480,P2D);
  int sw=640;
  int sh=480;
  if ( g_workers.IsDecentMachine()==false) {
    sw=320;
    sh=320;
  }    

  g_frameBuff[0]=createImage(sw,sh,RGB);
  g_frameBuff[1]=createImage(sw,sh,RGB);

  g_randTable=new float[64*64];
  g_randTableInterleaved=new float[64*64];
  for(int i=0;i<64;i++)
    for(int j=0;j<64;j++)
    {
      int id=i>>1;
      int jd=j>>1;
      int seg=((id&1)<<1)+(jd&1);
      float rv=(float)((((id>>1)&1)<<1)+((jd>>1)&1));//v=.5;//random(0.,1.)
      rv/=4.f;
      rv=0.f;
      g_randTableInterleaved[i*64+j]=((float)seg+rv)/4.f;
      g_randTable[i*64+j]=random(0.f,1);
    }
  improveDistribution(g_randTable);
  for (int i=0;i<g_randTableInterleaved.length;i++)
    g_randTableInterleaved[i]+=g_randTable[i/2]*1.f/4.f;
    
  Volumes=new Volume[22];
  int idx=0;
  Volumes[idx++]=new TextV("Volume");
  Volumes[idx++]=new TextV("Render");
  Volumes[idx++]=new Hieght2();
  Volumes[idx++]=new DiscoBall2();
  Volumes[idx++]=new Fire(new PVector(20, 7.776f, 3.702f));
  Volumes[idx++]=new DiscoBall();
  Volumes[idx++]=new HieghtField();
  Volumes[idx++]=new CrabNebula();
  Volumes[idx++]=new Eskimo();
  Volumes[idx++]=new ColouredSphere2();
  Volumes[idx++]=new Nebula2();
  Volumes[idx++]=new Cloud();
  Volumes[idx++]=new Sphere();
  Volumes[idx++]=new Fire(new PVector(3.702f, 7.776f, 20));
  Volumes[idx++]=new Fire(new PVector(7.776f, 20, 3.702f)); 
  Volumes[idx++]=new Blobbies();
  Volumes[idx++]=new FireBall2(new PVector(4.f,2.f,1.f));
  Volumes[idx++]=new CloudPuff();  
  Volumes[idx++]=new TextV("Bye Bye");
  Volumes[idx++]=new FireBall2(new PVector(1.f,2.f,4.f));
  Volumes[idx++]=new World();
  Volumes[idx++]=new TextV("DEMO");

  g_iconSelect=new SelectScreen();  
  st = 0;
    SetupAudio();
}

public int encodeColor(float x, float y, float z) {
  x = constrain(x,0.f,255.f);
  y = constrain(y,0.f,255.f);
  z = constrain(z,0.f,255.f);
  return  ((((int)x)&0xFF)<<16) |
    ((((int)y)&0xFF)<<8) |
    (((int)z)&0xFF);
}

public int averageCol( int a, int b )
{
  int ob = ((a&0xff)+(b&0xff))>>1;
  int og = (((a>>8)&0xff)+((b>>8)&0xff))>>1;
  int or = (((a>>16)&0xff)+((b>>16)&0xff))>>1;
  return (or<<16)|(og<<8)|ob;
  //return a;
}
class Scam
{
  float corigin = 4.f;
  float   camAngle = 0;
  PVector  cacDir;

  Scam() {
    update();
  }
  Scam(Scam other)
  {
    corigin=other.corigin;
    camAngle=other.camAngle;
    update();
  }
  public void update()
  {
    cacDir = new PVector( sin(camAngle),0.f,cos(camAngle));
  }
  public void dragged()
  {
    float dx = pmouseX-mouseX;
    float dy= pmouseY-mouseY;

    corigin += dy * 0.5f/(float)height;
    camAngle += dx * 0.5f/(float)width;
    update();
  }
  public void AddRotation( float r)
  {
    camAngle+=r;
    update();
  }
  public PVector getOrigin()
  {
    PVector  orig = new PVector(-cacDir.x,0.f,cacDir.z);
    orig.mult(-corigin);
    return orig;
  }
  public PVector getDir( int ppx, int ppy, PVector tcdir, int w, int h)
  {
    // note could change to add and normalize
    PVector  cdir = new PVector( -w/2+ppx,-h/2+ppy,w);

    // 2D rotate
    PVector camDir = tcdir;
    camDir.x = cdir.x * cacDir.z - cdir.z * cacDir.x;
    camDir.z = cdir.z * cacDir.z + cdir.x * cacDir.x;
    camDir.y= cdir.y;  
    camDir.normalize();
    return camDir;
  }
}
Scam g_scam=new Scam();

public int doRay( Raymarcher marcher, int x, int y,PVector ro, PVector rd, int step,int[] pix, final float[] rt,
PVector bg ) {

  float f = marcher.m_cs.fadeV*255.f;
  PVector c =marcher.integrate( ro, rd,rt[ (x&63)+((y&63)*64)],bg, step ==1);

  return encodeColor(c.x*f,c.y*f,c.z*f);
} 
int g_CurrentVolume=0;
float oldtime = 0;

final int DState_Interactive=0;
final int DState_Static=1;
float st=0;
int   quality=DState_Interactive;
public void ToggleDemo()
{
  g_workers.Wait();
  if ( g_demo ==null)
    g_demo = new Demo();
  else
  {
    g_demo.stop();
    g_demo=null;
  }
}
public void mousePressed()
{
  if ( g_showIcons)
  {
    g_CurrentVolume=g_iconSelect.GetCurrentSelection();
    st=0;
    g_showIcons=false;
    if (g_CurrentVolume==Volumes.length-1){
      g_workers.Wait();
      if ( g_demo ==null)
        g_demo = new Demo();
        
    }
  }
  else
  {
    if ( g_iconSelect.SelectedMini())
    {
      g_workers.Wait();
      g_showIcons=true;
      if ( g_demo !=null) {
        g_demo.stop();
        g_demo=null;
      }
    }
  }
} 
public void mouseDragged()
{
  if ( quality==DState_Static)
  {
    g_workers.Wait();
    quality=DState_Interactive;
    st = 0;
  }
  g_scam.dragged();
}
Demo g_demo = null;

public void keyPressed()
{
  if ( key==' ' && quality==DState_Interactive)
  {
    g_workers.Wait();
    quality=1-quality;
    return;
  }
  if ( key=='s')
  {
    g_DebugShowSteps=!g_DebugShowSteps;
    return;
  }
  if ( key=='a')
  {
    g_DebugUseAdaptive=!g_DebugUseAdaptive;
    return;
  }
  if ( key=='d')
  {
    g_workers.Wait();
    if ( g_demo ==null)
      g_demo = new Demo();
    else
    {
      g_demo.stop();
      g_demo=null;
    }
  }

  g_workers.Wait();

  g_CurrentVolume= (g_CurrentVolume+1)%Volumes.length;

  if ( quality==1)
  {
    st = 0;
  }
}

PImage g_CopyImage=null;

class CopyTask implements Runnable
{
  PImage fbuff;
  CopyTask( PImage fb) {
    fbuff=fb;
  }

  public void run()
  {      
    g_CopyImage=fbuff;
  }
}
class SleepTask implements Runnable
{
  public void run(){   
    //sleep to allow audio to kick in
    try {
        Thread.sleep(8);
      }
      catch (InterruptedException e) {
        // You might want to log something here
        println("TASK ERROR");
      }
  }
}
    
class BlurEdgesTask implements Runnable
{
  PImage fbuff;
  BlurEdgesTask( PImage fb) {
    fbuff=fb;
  }

  public void run()
  {   
    int[] pix= fbuff.pixels;

    for (int y=0;y<fbuff.height-2;y+=2)
    {
      int cy=y*fbuff.width;
      int ny=(y+2)*fbuff.width;
      // blur pixels with ones below for interleaved sampling blur

      for (int x =0; x < fbuff.width; x+=2)
        pix[cy+x]=averageCol(pix[cy+x],pix[ny+x]);
      //now generate pixels inbetween
      for(int x=1;x<fbuff.width-1;x+=2)
        pix[cy+x]=averageCol(pix[cy+x-1], pix[cy+x+1]);
    }
    // now fill in missing scanlines   
    for (int y=1;y<fbuff.height-1;y+=2)
    {
      int cy=y*fbuff.width;
      int py=(y-1)*fbuff.width;
      int ny=(y+1)*fbuff.width;
      for(int x=0;x<fbuff.width;x++)
        pix[cy+x]=averageCol(pix[ny+x],pix[py+x]);
    }     
    g_CopyImage=fbuff;
  }
}
class BackgroundPainter
{
  public PVector getv( float bc[], float y,float t)
  {
    float ny=y+g_lattice.tableNoise2d( y+124.f,t*.2f)*.75f;
    ny=constrain(ny,0.f,1.f);
    ny=smoothstep(0.f,1.f,ny);
    return new PVector( lerp(bc[0],bc[3],ny),
    lerp(bc[1],bc[4],ny),
    lerp(bc[2],bc[5],ny));
  }
};
class TileTask implements Runnable
{
  int sy;
  int ey;
  float step;
  int volId;
  float ox;
  float oz;
  int dbstep;
  State cs;
  PImage fbuff;
  Scam    scam;

  AtomicInteger refCount;
  Runnable      childTask;

  TileTask( int s,int e, float stepSize,
  int _vId, Scam cam, int q, State _cs, PImage fb,
  AtomicInteger rc, Runnable ct )
  {
    sy=s;
    ey=e;
    step=stepSize*stepSizeModifiers[_vId];
    volId=_vId;
    dbstep = (1-q)+1;
    cs=_cs;
    fbuff=fb;
    refCount=rc;
    childTask=ct;
    scam=new Scam(cam);
  }
  public void run()
  {
    BackgroundPainter back=new BackgroundPainter();
    int[] pix= fbuff.pixels;
    int w = fbuff.width;
    int h=fbuff.height;
    Raymarcher marcher = new Raymarcher( Volumes[volId], step,cs);
    scam.AddRotation( Volumes[volId].getRotation(cs));
    PVector ro=scam.getOrigin();
    float[] bcols=Volumes[volId].getBGColors();

    PVector cdir=new PVector();
    float[] rt= ( dbstep==1) ? g_randTable : g_randTableInterleaved;
    for (int y =sy; y < ey; y+=dbstep) {
      PVector bg=back.getv(bcols, (float)y/(float)h, cs.time);
      for (int x =0; x < w; x+=dbstep){
        PVector rd=scam.getDir(x,y, cdir,w,h);
        pix[y*w+x]=doRay(marcher, x, y,ro,rd, dbstep,pix,rt,bg);
      }
      if (dbstep>1){
        // do a horizontal box blur first
        int cy=y*w;
        for (int x =0; x < w; x+=2)
          pix[cy+x]=averageCol(pix[cy+x],pix[cy+x+2]);
      }
    }
    if ( dbstep==1 && refCount!=null)
    {
      stroke(color(0.f,0.f,255.f));
      fill(color(0.f,255,0.f));
      int yv = refCount.get()*(ey-sy);
      rect( 10,yv,8,8);
    }
    if (refCount==null)
      return;
    int v=refCount.decrementAndGet();
    if ( v==0)
    {
      childTask.run();
    }
    else if ( v==5 && dbstep>1 && ( g_demo ==null))
    {
      st=0;
    }
  }
}
int startMillis=0;
int g_fbIdx=0;
int step=16;


public void draw()
{
  if ( g_showIcons) {
    g_iconSelect.draw();
    return;
  }
  if ( st==0 )
  { 

    float time=millis()/1000.0f;

    if ( g_frameCounts[g_fbIdx]!=null && g_frameCounts[g_fbIdx].get()>0)
    {
      print("Waiting");
      return;
    }
    st=2;

    float stateTime=time;
    if ( g_demo !=null)
    {
      g_demo.update(time);
      stateTime-=g_demo.startSongTime;
    }
    State  cs=new State(stateTime,1-quality, 
    g_demo !=null ? g_demo.beatDuration(): 1.f, 
    g_demo !=null ? g_demo.Fade(): 1.f);
    float stepSize=quality == 0 ? 0.08f : 0.025f;
    Scam ccam=new Scam(g_scam);
    if ( g_demo!=null)
    {
      ccam.corigin=g_demo.getPosition();
    }
    Runnable copyTask;
    PImage fb=g_frameBuff[g_fbIdx];
    if ( quality==DState_Interactive)
      copyTask= new BlurEdgesTask(fb);
    else
      copyTask= new  CopyTask(fb);

    g_frameCounts[g_fbIdx]=new AtomicInteger(fb.height/step);
    Volumes[g_CurrentVolume].update(cs);

    int tskCount=0;
    for(int y=0;y<fb.height;y+=step)  {
      TileTask tsk=new TileTask( y,y+step,stepSize,
      g_CurrentVolume,ccam,quality,cs,g_frameBuff[g_fbIdx],g_frameCounts[g_fbIdx], copyTask);
      g_workers.execute(tsk);
      if (( tskCount&7)==0 && g_demo !=null )
        g_workers.execute(new SleepTask());
      tskCount++;
    }
    g_fbIdx = 1-g_fbIdx;
  }


  if (  g_CopyImage!=null)
  {
    if ( g_CopyImage.width != width)
      background(color(32,32,32));
      
    image( g_CopyImage,(width-g_CopyImage.width)/2,(height-g_CopyImage.height)/2);
    g_CopyImage=null;

    g_iconSelect.showMini();

    //    fill(~0);
    //    text(" Time "+(millis()-startMillis),10,20);
    startMillis=millis();
    if ( g_demo !=null)
      st=0;
  }
}

CommandVal[] timeComs=new CommandVal[]{
  
new CommandVal(0.087740f, "smoke"),
new CommandVal(4.540567f, "volume;starttime"),
new CommandVal(13.972662f, "smoke"),
new CommandVal(23.404757f, "fire"),
new CommandVal(42.268947f, "faster;terrain"),
new CommandVal(61.155073f, "harry"),
new CommandVal(71.376832f, "fire"),
new CommandVal(88.661694f, "smoke"),
new CommandVal(98.422816f, "slower;terrain"),
new CommandVal(108.622640f, "harry"),
new CommandVal(117.725708f, "fire;faster"),
new CommandVal(136.633769f, "volume"),
new CommandVal(155.607635f, "terrain"),
new CommandVal(166.706798f, "smoke"),
new CommandVal(174.713111f, "smoke;slower"),
new CommandVal(183.969725f, "harry"),
new CommandVal(191.493466f, "bye")
 };

TimeLine    tline;

/*
final int sc_Volume=0;
 final int sc_Render=1;
 final int sc_Height2=2;
 final int sc_Crap=3;
 final int sc_Fire=4;
 final int sc_SpotFog=5;
 final int sc_GroovyTerrain=6;
 final int sc_CrabNebula=7;
 final int sc_WaavySmoke=8;
 final int sc_fireBall=9;
 final int sc_HPSmoke=10;
 final int sc_Cloudy=11;
 final int sc_CubeSphere=12;
 final int sc_Blobbies=12;
 final int sc_FireBall2=13;
 final int sc_CloudPuff=14;
 
 */

int wavy[]= {
  sc_WaavySmoke,
  sc_CloudPuff,
  sc_CubeSphere,
  sc_Blobbies
};
int smokeTypes[]= {
  sc_HPSmoke,
  sc_Blobbies,
  sc_WaavySmoke,
  sc_SpotFog,
  sc_CloudPuff,
  sc_DiscoBall
};
int title[]= {
  sc_Volume,
  sc_Render,
  sc_CubeSphere,
  sc_World,
};
int fire[]= {
  sc_Fire,
  sc_FireBall2,
  sc_FireBlue,
  sc_fireball,
  sc_FireGreen,
  sc_FireBallBlue,
};
int harry[]= {
  sc_HPSmoke,
  sc_CloudPuff,
  sc_WaavySmoke,
  sc_Blobbies,
  sc_DiscoBall
};
int terrain[]= {
  sc_GroovyTerrain,
  sc_Height2,
  sc_CubeSphere,
  sc_World,
};
int bye[]= {
  sc_ByeBye,
  sc_FireBall2,
  sc_ByeBye,
  sc_Blobbies,
  sc_ByeBye,
  sc_CloudPuff,
};

class Demo
{
  float startTime=0;
  float startSongTime=0;
  
  float nextTransTime=0;
  float lastTransTime;
  float beatTime=(42.2f-23.4f)/16.f;
  int[] m_scenesel;
  int    m_scIdx;
  int    m_speed=2;

  float m_startz;
  float m_endz;
  boolean m_useTrackTime=true;
  Demo()
  {
    nextTransTime=10000.f;
    lastTransTime=0;
    //if ( player!=null ) player.play();
    startTime=millis()/1000.0f;

  }
  public float beatDuration()
  {
    return m_speed*beatTime;
  }
  public void transition(float ltime)
  {
    g_CurrentVolume=m_scenesel[m_scIdx%m_scenesel.length];
    lastTransTime=ltime;
    nextTransTime = ltime+ (float)((int)random(1.f,2.9f))*beatTime*m_speed;


    float nextComTime=tline.GetNextCommandTime();
    if ( nextComTime < (nextTransTime+(beatTime*m_speed)))
    {
      nextTransTime=nextComTime+0.01f;
      //print("Overriding trans time \n");
    }
    //print(" Setting Scene "+g_CurrentVolume+"\n");
    m_scIdx++;

    m_startz=random(2.8f,5.f);
    m_endz=random(2.8f,3.5f);
  }
  public float getPosition()
  {
    float t=0;//(float)player.position()/1000.f;
    float dt=(t-lastTransTime)/(nextTransTime-lastTransTime);
    return lerp(m_startz,m_endz,dt);
  }
  public float Fade()
  {
    if ( m_speed == 1)
      return 1.f;
     float t=0;//(float)player.position()/1000.f;
     float f=constrain(nextTransTime-t,0.f,1.f);
    f = min( constrain(t-lastTransTime,0.f,1.f),f);
    return f;
  }
  public void update( float time)
  {
    float ltime=0;//(float)player.position()/1000.f;
    CommandVal[] coms=UpdateBeat(ltime);
    int[] lastSel=m_scenesel;
    for(int i=0;i<coms.length;i++)
    {
      String c = coms[i].com;

      print("C '"+c+"'"  +" : "+ coms[i].val+" ");
      if (c.equals("smoke"))
        m_scenesel=smokeTypes;
      else if ( c.equals("volume"))
        m_scenesel=title;
      else if ( c.equals("fire"))
        m_scenesel=fire;
      else if ( c.equals("harry"))
        m_scenesel=harry;
      else if ( c.equals("terrain"))
        m_scenesel=terrain;
      else if (c.equals("wavy"))
        m_scenesel=wavy;
      else if ( c.equals("faster"))
        m_speed=1;
      else if ( c.equals("slower"))
        m_speed=2;
      else if ( c.equals("bye"))
      {
        m_scenesel=bye;
        m_speed=1;
      }
       else if ( c.equals("starttime"))
         startSongTime=time;
    }
    if ( m_scenesel!=lastSel)
    {
      m_scIdx=0;
      transition(ltime);
      return;
    }
    if ( ltime< nextTransTime)
      return;
    transition(ltime);
  }
  public void stop()
  {
    //if ( player!=null) player.pause();
  }
}
public void SetupAudio()
{
  //minim = new Minim(this);
  //player = minim.loadFile("412396_Jay_B___Heavenly_Inc.__NAC.mp3",2048);
//  tline=new TimeLine("labels.txt");//timeComs);
  tline=new TimeLine(timeComs);
}

public CommandVal[] UpdateBeat(float time)
{
  //  return new CommandVal[0];

  String[] com=tline.GetCommands(time);
  CommandVal[] cvs=new CommandVal[com.length];
  for (int i=0;i<com.length;i++)
    cvs[i] = new CommandVal(com[i]);
  return cvs;
}
class CommandVal
{
  String com;
  float val=0.f;
  CommandVal( float t, String v)
  {
    com=v;
    val=t;
  }
  CommandVal( String v)
  {
    String[] els=v.trim().split("_");
    com = els[0];
    if ( els.length>1)
      val= Float.valueOf(els[1]).floatValue();
  }
}
class TimeLine
{
  CommandVal[] commands;
  int     l_idx;

  TimeLine(  CommandVal[] coms )
  {
    l_idx=0;
    commands=coms;
  }
  TimeLine( String fname )
  {
    BufferedReader in = createReader(fname);

    commands = new CommandVal[0];
    String l =null;
    try {
      l=in.readLine();
    }
    catch(IOException e) 
    {
      e.printStackTrace();
      l=null;
    }

    while( l !=null)
    {
      String els[] =l.trim().split("\\s+");   
      if ( els.length>1)
      {
        commands =(CommandVal[])append(commands,new CommandVal( Float.valueOf(els[0]).floatValue(),els[1]));
      }
      try {
        l=in.readLine();
      }
      catch(IOException e) 
      {
        e.printStackTrace();
        l=null;
      }
    }

    l_idx=0;
  }
  public String[] GetCommands( float ctime)
  {
    String[] coms=new String[0];
    while(l_idx < commands.length && commands[l_idx].val<=ctime)
    {
      String els[] =commands[l_idx++].com.split(";"); 
      coms=concat( coms,els);
    }
    return coms;
  }
  public float GetNextCommandTime()
  {
    return l_idx < commands.length ? commands[l_idx].val : -1.f;
  }
}
public void stop() {
  //if ( player!=null) player.close();
  //if ( minim!=null) minim.stop();
  super.stop();
}


// lattice noise
//http://www.gamedev.net/community/forums/topic.asp?topic_id=479101

LatticeNoiseTable g_lattice = new LatticeNoiseTable();

public float IntNoise( int x, int y )
{
  int n =x + y *57;
  return IntNoise(n);
}
public float IntNoise( int n )
{
  final float ratio = 1.f /32767.f;
  n = (n<<13)^n;
  float ret = 1-(float)((n*(n *n*19417+189851)+4967243)&0xffff)*ratio;
  return ret;
}
 
class LatticeNoiseTable
{
  float[] table;
  int wMask=255;
  int wShift=8;
  int wid=256;
  float[] blendTable;
 
 
  float[]  fmbTable;
  public float[] genTab(int w)
  {           
    // build 2x2 nearbourhood
 
    float[] ptab  = new float[w*w*4];
    println(w +" Table len " + ptab.length );
    for (int y =0; y < w; y++)
    {
      int idx=y*w*4;
      for (int x =0; x < w; x++)
      {       
        float a = IntNoise( x,y );
       float b = IntNoise( ((x+1)&wMask),y );
        float c = IntNoise( x,((y+1)&wMask) );
        float d = IntNoise( ((x+1)&wMask),((y+1)&wMask) );
 
        float k1 =   b - a;
        float k2 =   c - a;
        float k4 =   a - b - c + d;
        ptab[idx+0]=a;
        ptab[idx+1]=k1;
        ptab[idx+2]=k2;
        ptab[idx+3]=k4;
        idx+=4;
      }
    }
    return ptab;
  }
  LatticeNoiseTable()
  {
    table=genTab(wid);
    blendTable = new float[256];
    for (int i=0;i<256;i++)
    {
      float u=(float)i/256.f;
      u = u*u*u*(u*(u*6.0f-15.0f)+10.0f);
      blendTable[i]=u;
    } 
 
    fmbTable=new float[wid*wid];
    for (int i=0;i<wid;i++)
      for (int j=0;j<wid;j++)
        fmbTable[i*wid+j]=fbmWrap((float)i/64.f,(float)j/64.f,6,.5f,4);
  }
   public float fbmWrap( float x,float y, int oct, float g, int wrap)
  {
    float s=0.f;
    float amp=1.f;
    int fxi=(int)(x*255.f*255.f);
    int fyi=(int)(y*255.f*255.f);

    // do in reverse
    for(int i=0;i<oct;i++)
    {
      int ofW=(16-i);
      int ofF=(8-i);
      int xi=fxi>>ofW;
      int yi=fyi>>ofW;
      int fracx=(fxi>>ofF)&0xff;
      int fracy=(fyi>>ofF)&0xff;
 
      float u=blendTable[fracx];
      float v=blendTable[fracy];
     
      int mask2=min( wrap-1,wMask);
      int i0=(xi&mask2)<<2;
      int i1=((xi+1)&mask2)<<2;
      int j0=(yi&mask2)<<(wShift+2) ;
      int j1=((yi+1)&mask2)<<(wShift+2) ;
     
       float a = table[i0+j0 ];
      float b = table[ i1+j0];
      float c = table[ i0+j1 ];
      float d = table[ i1+j1 ];
 
      float k1 =   b - a;
      float k2 =   c - a;
      float k4 =   a - b - c + d;
     
      float ns=a+k1*u+k2*v+k4*u*v; 
      s+=ns*amp;
      amp*=g;
      wrap<<=1;
    }
    return s;
  }
  public float tableNoise2d( float x, float y)
  { 
    x*=64.f;
    y*=64.f;
    int i =(int)(x);
    int j = (int)(y);
    float u = x -(float)i;
    float v = y -(float)j;
 
    u =u*u*(3.f - 2.f*u);
    v =v*v*(3.f - 2.f*v);
 
    int i0=(i&wMask);
    int i1=((i+1)&wMask);
    int j0=(j&wMask)<<wShift ;
    int j1=((j+1)&wMask)<<wShift ;
    float a = fmbTable[i0+j0 ];
    float b = fmbTable[ i1+j0];
    float c = fmbTable[ i0+j1 ];
    float d = fmbTable[ i1+j1 ];
 
    float k1 =   b - a;
    float k2 =   c - a;
    float k4 =   a - b - c + d;
 
    return a + k1*u + k2*v + k4*u*v ;
  }
  public float noise2d(  float x,  float y )
  {
    assert( x>0.f);
    assert( y>0.f);
    int i = (int)(x);
    int j = (int)(y);
    float u = x -(float)i;
    float v = y -(float)j;
 
    // u = u*u*u*(u*(u*6.0f-15.0f)+10.0f);
    // v = v*v*v*(v*(v*6.0f-15.0f)+10.0f);
    u =u*u*(3.f - 2.f*u);
    v =v*v*(3.f - 2.f*v);    
    return getPre( (((i&wMask)<<wShift) + (j&wMask))<<2, v,u);
  }
  public float tableNoise3d( float x, float y,float z)
  {
    assert( x>0.f);
    assert( y>0.f);
    assert( z>0.f);
    float uv0=tableNoise2d(x,y);
    float uv1=tableNoise2d(x,z);
 
    return uv0+uv1;
  }
  public float noise3d(  float x,  float y,float z )
  {
    assert( x>0.f);
    assert( y>0.f);
    int i = (int)(x);
    int j = (int)(y);
    int k=(int)(z);
    float u = x -(float)i;
    float v = y -(float)j;
    float w = z-(float)k;
 
    // u = u*u*u*(u*(u*6.0f-15.0f)+10.0f);
    // v = v*v*v*(v*(v*6.0f-15.0f)+10.0f);
    u =u*u*(3.f - 2.f*u);
    v =v*v*(3.f - 2.f*v);  
    w=w*w*(3.f - 2.f*w); 
 
    i+=k*13;
    j+=k*107;
    float uv0= getPre( (((i&wMask)<<wShift) + (j&wMask))<<2, v,u);
    i+=13;
    j+=107;
    float uv1= getPre( (((i&wMask)<<wShift) + (j&wMask))<<2, v,u);
    return lerp(uv0,uv1,w);
  }
  public float fbm( float x,float y, int oct, float g)
  {
    float s=0.f;
    float amp=1.f;
    int fxi=(int)(x*255.f*255.f);
    int fyi=(int)(y*255.f*255.f);
 
    // do in reverse
    for(int i=0;i<oct;i++)
    {
      int ofW=(16-i);
      int ofF=(8-i);
      int xi=fxi>>ofW;
      int yi=fyi>>ofW;
      int fracx=(fxi>>ofF)&0xff;
      int fracy=(fyi>>ofF)&0xff;
 
      float u=blendTable[fracx];
      float v=blendTable[fracy];
      int idx=(((yi&wMask)<<wShift) + (xi&wMask))<<2;
      float ns=table[idx+0]+table[idx+1]*u+table[idx+2]*v+table[idx+3]*u*v; 
      s+=ns*amp;
      amp*=g;
    }
    return s;
  }
  public float fbm( float x,float y,float z, int oct, float g)
  {
    float s=0.f;
    float amp=1.f;
    for(int i=0;i<oct;i++)
    {
      s+=noise3d(x,y,z)*amp;
      x*=2.f;
      y*=2.f;
      z*=2.f;
      amp*=g;
    }
    return s;
  }
  public float getPre(  int idx, float u, float v)
  {
    //a + k1*u + k2*v + k4*u*v ;   
    return table[idx+0]+table[idx+1]*u+table[idx+2]*v+table[idx+3]*u*v;
  }
}

boolean g_DebugShowSteps=false;
boolean g_DebugUseAdaptive=true;

class vRes
{
  PVector c;
  float   a;
  vRes() {
  }
  vRes( vRes o){
    c=o.c;
    a=o.a;
  }
  vRes( PVector _c, float _a )
  {
    c = _c;
    a = _a;
  }
  public void set( PVector _c, float _a )
  {
    c = _c;
    a = _a;
  }
  public void setCopy( PVector _c, float _a )
  {
    c.set(_c);
    a = _a;
  }
}


class State
{
  float time;
  int    quality;
  float  pulse;
  float  pulse2;
  float  halfpulse;

  float  cosang;
  float sinang;
  float pulseSqrt;
  float fadeV;


  // general
  int numOct;
  float ox;
  PVector color0;
  PVector p0;
  PVector p1;
  PVector p2;

  PVector bmin;
  PVector bmax;
  
  State( float t, int q, float beatDuration, float fade )
  {
    time=t* 2.f/beatDuration;
    quality=q;
    halfpulse=sin(.5f*t*PI/beatDuration)*.5f+.5f;
    pulse=sin(t*PI/beatDuration)*.5f+.5f;
    pulse2=sin(2.f*t*PI/beatDuration)*.5f+.5f;
    float ang=time*.25f;
    cosang=cos(ang);
    sinang=sin(ang);
    pulseSqrt=sqrt(pulse);
    fadeV=fade;
  }
}

// intersection volumes
public PVector intersectBoxCZero(final PVector r_o, final PVector invr_d,final PVector boxmin, final PVector boxmax) 
{

  // compute intersection of ray with all six bbox planes
  PVector invR = invr_d;
  PVector tbot = new PVector();
  tbot.set(boxmin);
  tbot.sub(r_o);
  tbot.mult(invR);

  PVector ttop = new PVector();
  ttop.set(boxmax);
  ttop.sub(r_o);
  ttop.mult(invR);
  // re-order intersections to find smallest and largest on each axis

  PVector tmin = new PVector();
  tmin.x = min(ttop.x, tbot.x);
  tmin.y = min(ttop.y, tbot.y);
  tmin.z = min(ttop.z, tbot.z);
  PVector tmax= new PVector();
  tmax.x = max(ttop.x, tbot.x);
  tmax.y = max(ttop.y, tbot.y);
  tmax.z = max(ttop.z, tbot.z);

  // find the largest tmin and the smallest tmax
  float largest_tmin = max(max(tmin.x, tmin.y), max(tmin.x, tmin.z));
  float smallest_tmax = min(min(tmax.x, tmax.y), min(tmax.x, tmax.z));

  if(  smallest_tmax > largest_tmin )
    return new PVector(largest_tmin,smallest_tmax,0.f);
  return  new PVector(0.f,0.f,0.f);
}

public PVector intSphereBoundCZero(  float r2, PVector ro, PVector dir) {
  float c = ro.dot(ro) - r2*r2;
  float b = ro.dot(dir);
  float t = b*b-c;
  if ( t > 0.0f) {
    float st =sqrt(t);
    t = -b-st;
    return new PVector( max( -b-st, 0.f), max( -b+st, 0.f),0.f);
  }
  return new PVector( -1.f, -1.f);
}

class Raymarcher
{
  Volume m_vol;
  float m_stepsize;
  State m_cs;
  float[] expTable;
  vRes br;
  PVector ct;
   PVector m_rp=new PVector();

  Raymarcher( Volume vol, float stepsize, State cs )
  {
    m_vol = vol;
    m_stepsize = stepsize;
    m_cs=cs;
    
    br=new vRes();
    br.c=new PVector();
    ct = new PVector(0.f,0.f,0.f);

  }
  public PVector integrate( PVector ro, PVector rd, float randv, PVector bg, boolean useAdaptive )
  {
    float absorption=1.f;//2.;
    PVector tvals = m_vol.getIntersectionVolume(ro, rd,m_cs );  
    if ( tvals.x >= tvals.y)
      return bg;
    float rhomult = -absorption;
    float T = 1.f;

    //vRes brOld=new vRes();
    //brOld.c=new PVector();
    ct.set(0.f,0.f,0.f);
    //float rhoMaxThreshold=0.1f*0.01f;
    float rhoMaxThreshold=0.1f*0.005f;
    float rhoMinThreshold=rhoMaxThreshold*.25f;
    int  numsteps=0;

    if ( g_DebugUseAdaptive )// || useAdaptive)
    {
      float ds =m_stepsize;
      float step_t0=tvals.x + ds*randv;
      float maxStep=useAdaptive ? ds*4.f : ds*2.f;
      float minStep=ds;
      float stepLength = maxStep;
      float step_t1=step_t0+stepLength;
      float t1=tvals.y;
      float doneT=-100.f;
      while( step_t1<t1) 
      {
        float t = (step_t0+step_t1)*.5f;
        m_rp.x=rd.x*t+ro.x;
        m_rp.y=rd.y*t+ro.y;
        m_rp.z=rd.z*t+ro.z;
/*        if ( abs(doneT-t)<0.02f){
          r=brOld;
          numsteps+=5;
        }
        else*/
        vRes  r =  m_vol.sample( m_rp,br,m_cs);
          numsteps++;
        float rho = r.a*stepLength;
        if ( rho >rhoMaxThreshold && stepLength>minStep)
        {
          
     //     brOld.setCopy(r.c,r.a);
          doneT=t;
          stepLength*=.5f;
          step_t1=step_t0+stepLength;  
          continue;
        }
        T *= exp( rhomult* rho);
        r.c.mult(T*rho);
        ct.add(r.c);
        if ( rho<rhoMinThreshold&& stepLength<maxStep)
        {// bigger steps please
          stepLength*=2.f;
        }
        step_t0=step_t1;
        step_t1+=stepLength;
        //        if ( T < 0.0001f)
        if ( T < 0.1f)
          break;
      }
    }
    else
    {
      numsteps = (int)(ceil((tvals.y-tvals.x)/m_stepsize));
      float ds = (tvals.y-tvals.x)/numsteps;
      if ( ds <=0.00001f)
      return bg;

    
      PVector stepdir = new PVector();
      stepdir.set(rd);
      stepdir.mult(ds);

      PVector raypos = new PVector();
      raypos.set(rd);
      raypos.mult(tvals.x + ds*randv);
      raypos.add(ro);
      raypos.add(stepdir); 

      for (int step=0; step < numsteps; ++step) {
        vRes r =  m_vol.sample( raypos,br,m_cs);
        float rho = r.a*ds;
        T *= exp(  -rho);
        if ( T < 0.01f)
          break;
        PVector ci =r.c;
        ci.mult( T *rho);
        ct.add(ci);
        raypos.add(stepdir);
      }
    }
    if ( g_DebugShowSteps)    
      ct.set( (float)(numsteps-15)/20.f,(float)(numsteps-5)/10.f,(float)numsteps/5.f);

    ct.x=ct.x + T* bg.x;
    ct.y=ct.y + T* bg.y;
    ct.z=ct.z + T* bg.z;
    return ct;
  }
};

class CopyImageTask implements Runnable
{
  PImage icon;
  int[] pos;
 
  CopyImageTask( int[]  l, PImage ic) {
    pos=l;
    icon=ic;
  } 
  public void run()
  {
    icon.loadPixels();
    for (int y=0;y<icon.height;y+=2) //now generate pixels inbetween
    {   
      int cy=y*icon.width;
      for(int x=1;x<icon.width-1;x+=2)
        icon.pixels[cy+x]=averageCol(icon.pixels[cy+x-1], icon.pixels[cy+x+1]);
    }
    // now fill in missing scanlines   
    for (int y=1;y<icon.height-1;y+=2)
    {
      int py=(y-1)*icon.width;
      int cy=y*icon.width;
      int ny=(y+1)*icon.width;
      for(int x=0;x<icon.width;x++)
        icon.pixels[cy+x]=averageCol(icon.pixels[ny+x],icon.pixels[py+x]);
    }     
    icon.updatePixels();
    image(icon,pos[0],pos[1]);
  }
};
 
 
class SelectScreen
{
  PImage[]     icons;
  PImage       mini;
  int[]        tiling;
 
  SelectScreen()
  { 
    icons = new PImage[Volumes.length];
    tiling=CalcIconTiling( icons.length);
 
    int wm=min( width/tiling[0], height/tiling[1])-16;
    // round down by to 16
   print(" TX " +tiling[0]+" "+tiling[1] +"\n");
      for (int i=0;i<icons.length;i++)
      icons[i]=createImage(wm,wm,RGB);
  }
 
  private int[] getIconLocation( int i, int[] tiling, int icw, int ich )
  {
    int iw=i%tiling[0];
    int ih=i/tiling[0];
 
    int tw=width/tiling[0];
    int th=height/tiling[1];
 
    int ow = (tw-icw)/2;
    int oh = (th-ich)/2;
 
    int[] p=new int[2];
    p[0]=iw*tw+ow;
    p[1]=ih*th+oh;
    return p;
  }
  private  int[] CalcIconTiling( int numIcons)
  {
    float aspectRatio=(float)height/(float)width;
    // best tiling to fit
    int numCols=0;
    int numrows=0;
    boolean fit=false;
    while( !fit)
    {
      numrows+=1;
      numCols = (int)(aspectRatio*(float)numrows);
      fit= (( numrows*numCols)>=numIcons);
    }
    // now space them out
    int[] s=new int[2];
    s[0]=numrows;
    s[1]=numCols;   
    return s;
  }
 
  public int GetCurrentSelection()
  {   
    int tw=(width/tiling[0]);
    int th=(height/tiling[1]);
    int sx=mouseX/tw;
    int sy=mouseY/th;
    return min( sx+sy*tiling[0], icons.length-1);
  }
  public void showMini()
  {
      image(mini,0,0);
  }
  public boolean SelectedMini()
  {
    return mouseX < mini.width && mouseY < mini.height;
  }
  public void draw()
  {
 
    Scam iconcam=new Scam();
    background(0);
    int bx=icons[0].width+6;
    int by=icons[0].height+6;
    g_CurrentVolume=GetCurrentSelection();
    int[] bloc=getIconLocation(GetCurrentSelection(), tiling, bx+6,by+6);
    fill(~0);
    rect( bloc[0],bloc[1], bx+6,by+6);
    for(int i=0;i<icons.length;i++)
    {
      int[] bloc2=getIconLocation(i, tiling, bx,by);
      fill(color(64,64,64));
      rect( bloc2[0],bloc2[1], bx,by);
    }
    for(int i=0;i<icons.length;i++)
    {
      PImage ic=icons[i];
      State  cs=new State((float)millis()/1000.f,1-quality,1.f,1.f);
      Volumes[i].update(cs);
 
      int[] loc=getIconLocation( i, tiling, ic.width, ic.height );
      Runnable copyTask= new CopyImageTask(loc,ic);    
 
      TileTask tsk=new TileTask( 0,ic.height, 0.1f,
      i,iconcam,0,cs, ic,new AtomicInteger(1),copyTask);
      g_workers.execute(tsk);
    }
    g_workers.Wait();
    if ( mini==null)
    {
      mini=createImage(48,48,RGB);
      int dw=width/mini.width;
      int dh=height/mini.height;
     
      for( int i=0;i<mini.height;i++)
        for (int j=0;j<mini.width;j++)
        {
          mini.set(i,j, get(i*dw,j*dh));
        }
    }
  }
};
// volume
interface Volume
{
  public vRes sample( PVector p, vRes br, State cs );
  public float getRange();
  public float getRotation(State cs);
  public void update(State cs);
  public float[] getBGColors();
  public PVector getIntersectionVolume(PVector ro, PVector rd, State cs );
}
class BaseVolume implements Volume
{
  public vRes sample( PVector p, vRes br, State cs ) { 
    return br;
  }
  public float getRange() { 
    return 1.0f;
  }
  public PVector getIntersectionVolume(PVector ro, PVector rd, State cs )
  {
    float spRad = getRange();
    return  intSphereBoundCZero( spRad, ro, rd);
  }
  public float getRotation(State cs) { 
    return 0;
  }
  public void update(State cs) {
  };
  public float[] getBGColors() { 
    return bgBlue;
  }
}
float[] bgBlue= {
  0.0f,0.0f,0.0f,  0.2f,0.2f,0.4f,
};
float[] bgRed= {
  0.0f,0.0f,0.0f,  0.35f,0.2f,0.2f
};
float[] bgGreen= {
  0.0f,0.0f,0.0f, 0.2f,0.35f,0.2f
};
float[] bgSky= {
  0.1f,0.2f,0.5f, 0.3f,0.7f,1.f
};
float[] bgYellow= {
  0.0f,0.0f,0.0f, 0.35f,0.35f,0.2f
};
public float intSphereThickness( float r2, PVector ro, float ro2, PVector dir)
{
  float c = ro2 - r2;
  float b = ro.dot(dir);
  float t = b*b-c;
  if ( t > 0.0f) {
    float st =sqrt(t);
    float t0 = max(-b-st,0.f);
    float t1=  max(-b+st,0.f);
    return t1 -t0;
  }
  return 0.f;
}
// get to work with clouds too
  public float BlobDensity( PVector cp, PVector np, float radius)
  {
    PVector rp=PVector.sub(cp,np);
   return radius/rp.dot(rp);
  }

class blob
{
  float shadScale=1.f/0.3f;
  PVector p;
  PVector v;

  PVector ppos;
  float radius;
  blob()
  {
    p=new PVector( random(-.5f,.5f),
    random(-.5f,.5f),
    random(-.5f,.5f));
    v=new PVector( random(-.5f,.5f),
    random(-.5f,.5f),
    random(-.5f,.5f));
    radius=0.1f;
  }
   public PVector getPos( State cs)
   {
     return PVector.add(p,PVector.mult(v,cs.pulse*2.f));
   }
  public float density( State cs,PVector cp)
  {
    PVector np=PVector.add(p,PVector.mult(v,cs.pulse*2.f));
    PVector rp=PVector.sub(cp,np);
   return radius/rp.dot(rp);
  }
  public float shadow( State cs, PVector cp, PVector ld )
  {
   return GetShadow(cs,cp,cp.dot(cp), ld);
  }
  public float GetShadow( State cs,PVector cp, float ro2, PVector d)
  {
    PVector np=PVector.add(p,PVector.mult(v,cs.pulse*2.f));
    PVector o=PVector.sub(cp,np);
    return intSphereThickness( radius, o, o.dot(o), d)*shadScale;
  }
  public void update(State cs) {
  }
};

class CloudPuff extends BaseVolume
{
  PVector lpos=new PVector(1.f,-1.f,1.f);
  float shadRadius=1.f;

  PVector lcol=new PVector(1.f,0.8f,0.5f);
  PVector abcol=new PVector(0.1f,0.4f,0.6f);
  float[] translucency=new float[1024];
  CloudPuff()
  {
    //  lpos.normalize();
    for(int i=0;i<1024;i++)
      translucency[i]=constrain( exp(- ((float)i/1023.f)*1.f/.55f),0.f,2.f);
  }

  // to make better clouds
  public vRes  sample( PVector dp, vRes br, State cs )
  {
    // make eplisoid
    PVector p=br.c;
    p.y=dp.y;
    p.z=dp.z;
    p.x=dp.x*.82f;

    float d=p.dot(p);
    float d0=d;
    float nf = 4.f;
    float namp = 0.15f;
    int numOct =5- cs.quality*2;
    float ox=57.f+cs.time*.25f;
    float ns=g_lattice.fbm(p.x*nf+ox,p.y*nf+513.f+ox,p.z*nf+13.f+ox,numOct,.6f)*namp;

    d+=ns;
    float a = smoothstep( 1.f,0.9f,d)*4.f;

    float th=constrain(intSphereThickness( 1.f, p,d0*d0, lpos),0.f,1.f);
    float shadow = translucency[ (int)(th*1023.f)];
    PVector lgtv = PVector.mult(lcol,shadow);
    float ao=smoothstep(0.5f,1.f,d)*.6f+.4f;
    lgtv.add(PVector.mult(abcol,ao));
    br.set(lgtv, a);
    return br;
  }
  public float getRange() {
    return (1.f/.82f)+ 0.25f;
  }
  final float eppRangex=((1.f/.82f)+ 0.15f)*.92f;
  final float eppRange=(1.f+.15f)*.92f;
  final PVector m_boxmin=new PVector(-eppRangex,-eppRange,-eppRange);
  final PVector m_boxmax=new PVector(eppRangex,eppRange,eppRange);

  public PVector getIntersectionVolume(PVector ro, PVector rd, State cs )
  {
    PVector invrd=new PVector( 1.f/rd.x,1.f/rd.y,1.f/rd.z);
    return intersectBoxCZero(ro, invrd, m_boxmin, m_boxmax);
  }
  public float getRotation(State cs) {
    return cs.time*.25f;
  }

  public float[] getBGColors()
  {
    return bgSky;
  }
  public void update(State cs) {
  }
}
// pyroplastic noise from
// http://magnuswrenninge.com/content/pubs/VolumetricMethodsInVisualEffects2010.pdf
 
class FireBall2 extends BaseVolume
{
  PVector fcol=new PVector(4.f,2.f,1.f);
  FireBall2( PVector c)
  {
    fcol=c;
  }
  
    public void update(State cs) 
    {
      cs.numOct=5- cs.quality*2;
      cs.ox=57.f+cs.time*.5f;
      
    }
  public vRes  sample( PVector p, vRes br, State cs )
  {
    float nf = 1.5f;
    float namp = cs.pulse*.75f+.25f;
    int numOct =cs.numOct;
    float ox=cs.ox;
    float ns=g_lattice.fbm(p.x*nf+ox,p.y*nf+513.f+ox,p.z*nf+13.f+ox,numOct,.6f)*namp;
 
    // make eplisoid
    float pyroden=max(1.f-(p.dot(p))+abs(ns),0);
    float a = smoothstep(0.0f,0.1f,pyroden)- smoothstep(0.1f,.3f,pyroden);
    br.setCopy(fcol, a);
    return br;
  }
  public float getRange() {
    return 1.4f;
  }  
   public PVector getIntersectionVolume(PVector ro, PVector rd, State cs )
  {
    return intSphereBoundCZero( 1.4f, ro, rd);
  }

  public float getRotation(State cs) {
    return cs.time*.25f;
  }
 
  public float[] getBGColors()
  {
    return bgYellow;
  }
}
public PVector minv( PVector a, PVector b)
{
  return new PVector( min(a.x,b.x), min(a.y,b.y), min(a.z,b.z));
}
public PVector maxv( PVector a, PVector b)
{
  return new PVector( max(a.x,b.x), max(a.y,b.y), max(a.z,b.z));
}
class Blobbies extends BaseVolume
{
  blob b0=new blob(); 
  blob b1=new blob(); 
  blob b2=new blob();   
  PVector c0=new PVector(2.f,0.5f,0.5f);
  PVector c1=new PVector(0.5f,2.f,0.5f);
  PVector c2=new PVector(0.5f,.5f,2.f);
 
 
  public void update( State cs)
  {
    cs.p0=b0.getPos(cs);
    cs.p1=b1.getPos(cs);
    cs.p2=b2.getPos(cs);
    
    float brad=0.6f;
    cs.bmin=minv(minv(cs.p0,cs.p1),cs.p2);
    cs.bmin.add(-brad,-brad,-brad);
    cs.bmax=maxv(maxv(cs.p0,cs.p1),cs.p2);
    cs.bmax.add(brad,brad,brad);
  }
  public vRes  sample( PVector p, vRes br, State cs )
  {
    float d0= BlobDensity(p, cs.p0, b0.radius);
    float d1=BlobDensity(p, cs.p1, b1.radius);
    float d2=BlobDensity(p, cs.p2, b2.radius);
 
    float d=d0+d1+d2;
 
    PVector col=br.c;
    col.x =c0.x*d0+c1.x*d1+c2.x*d2;
    col.y =c0.y*d0+c1.y*d1+c2.y*d2;
    col.z =c0.z*d0+c1.z*d1+c2.z*d2;
    col.mult(1.f/d);
 
    float namp = 0.1f;
    int numOct =5- cs.quality*3;
    float ox=57.f+cs.time*.1f;
    float nf=3.f;
    d +=  g_lattice.fbm(p.x*nf+ox,p.y*nf+513.f+ox,p.z*nf+13.f+ox,numOct,.6f)*namp;
 
    float a=smoothstep(0.3f,0.5f,d)-smoothstep(0.6f,.7f,d);
    br.a= a; 
    return br;
  }
  public float getRange() {
    return 1.3f;
  }
  public float[] getBGColors()
  {
    return bgGreen;
  }
  public float getRotation(State cs) {
    return cs.time*.25f;
  }
  
  public PVector getIntersectionVolume(PVector ro, PVector rd, State cs )
  {
    PVector invrd=new PVector( 1.f/rd.x,1.f/rd.y,1.f/rd.z);
    return intersectBoxCZero(ro, invrd, cs.bmin, cs.bmax);
  }

}
class Sphere extends BaseVolume
{
  public vRes  sample( PVector p, vRes br, State cs )
  {
    float bx=max(max(abs(p.x),abs(p.y)),abs(p.z));
    float bxl = lerp(1.f,bx,cs.pulse);

    float a = smoothstep( 1.f,0.9f,lerp(p.mag(),bx,cs.pulse2))*(0.5f);

    br.set( new PVector(1.f+cs.pulse2*3,3.f,4.f-cs.pulse2*3), a);
    return br;
  }
  PVector m_boxmin=new PVector(-1,-1.f,-1.f);
  PVector m_boxmax=new PVector(1,1.f,1.f);

  public PVector getIntersectionVolume(PVector ro, PVector rd, State cs )
  {
    PVector invrd=new PVector( 1.f/rd.x,1.f/rd.y,1.f/rd.z);
    return intersectBoxCZero(ro, invrd, m_boxmin, m_boxmax);
  }

  public float getRotation(State cs)
  {
    return cs.time*2.f*PI/8.f;
  }
  public float getRange() {
    return 1.7f;
  }
  public float[] getBGColors()
  {
    return bgRed;
  }
}

class Cloud  extends BaseVolume
{
  Cloud()
  {
    // generate 32x32x32 shadow cube based on range
  }
  PVector lcol=new PVector(1.f,0.8f,0.5f);
  PVector abcol=new PVector(0.2f,0.2f,0.4f);

  public vRes  sample( PVector p, vRes br, State cs )
  {
    float pm= p.mag();
    ;
    float d = pm;
    float nf = 2.f;
    float namp = 1.f;
    int numOct =6- cs.quality*2;
    float ox=57.f+cs.time*.1f;
    d +=  (g_lattice.fbm(p.x*nf+ox,p.y*nf+513.f+ox,p.z*nf+13.f+ox,numOct,.7f)+(1.f-cs.pulseSqrt))*namp;

    float a = smoothstep( 1.f,0.5f, d)*2.f;
    a*=min((1.5f-pm)*8,1.f);
    // need shadow term??

    // do approx lighting on p.y
    float lgt = constrain( ((1.f-p.y)*d)*.5f+.5f,0.f,1.f);
    PVector lgtv = PVector.mult(lcol,lgt);
    lgtv.add(abcol);
    br.set(lgtv, a);
    return br;
  }
  public PVector getIntersectionVolume(PVector ro, PVector rd, State cs )
  {
    float spRad = 1.5f-(1.f-cs.pulseSqrt)*.5f;
    return  intSphereBoundCZero( spRad, ro, rd);
  }

  public float getRange() {
    return 1.5f;
  }
  public float[] getBGColors()
  {
    return bgSky;
  }
}
// does planet amtosphere
class EdgedSphere  extends BaseVolume
{
  public vRes  sample( PVector p, vRes br, State cs )
  {
    float d =p.mag();

    float s = 1.f;
    float e = 0.9f;
    if ( d < e )
      return  new vRes( new PVector(0.8f,0.4f,0.1f),1.f);
    float a = constrain((d - s)/(e-s),0.f,1);
    a*=a;
    float den = 0.8f;
    br.set( new PVector(1.f,1.f,4.f), a*den); 
    return br;
  }
  public float getRange() {
    return 1.f;
  }
  
  public float[] getBGColors()
  {
    return bgSky;
  }
}
// does planet amtosphere
class CrabNebula  extends BaseVolume
{
  public vRes  sample( PVector p, vRes br, State cs)
  {
    //noiseDetail(6,0.6);
    float d =p.mag();
    d *=2.f;
    float nf = 1.f;
    float namp = 6.f;
    int numOct =6- cs.quality*2;
    float ox=57.f+cs.time*.1f;
    d += (abs(g_lattice.fbm(p.x+57.f+ox,p.y+513.f+ox,p.z+13.f+ox, numOct,.6f)*.5f)-.25f)*namp;
    //  d += (abs(g_lattice.tableNoise3d(p.x*nf+57.f,p.y*nf+513.f,p.z*nf+13.f)*.5)-.25)*namp;

    //    d += (abs(noise(p.x*nf+57.f,p.y*nf+513.f,p.z*nf+13.f)-.5)-.25)*namp;
    d = max(d,0.f);
    float s = 1.f;
    float e = 0.9f;
    float a = constrain((d - s)/(e-s),0.f,1);

    float a2 = constrain(1.f -d *10.f,0.f,1);

    float a3 = constrain(1.f-abs((d-0.3f)*10.f),0.f,1);
    a3 = a3*5.f;

    float c = a;
    a*=a;
    float da = max(a,a2);
    da= max(a3,da);
    float den = 0.1f;
    PVector cExterior = new PVector(4.f+a2*2.f,4.f+a2*2.f,8.f);
    PVector cInterior = new PVector(4.f,1.f,1.f);
    PVector cLine = new PVector(4.f,4.f,1.f);
    PVector cr = a > a2 ? cInterior : cExterior;
    cr = a3 > 0 ? cLine : cr;

    br.set( cr, da*den);
    return br;
  }
  public float getRotation(State cs)
  {
    return cs.time*2.f*PI/32.f;
  }
  public float getRange() {
    return 1.15f;
  }
  
  public float[] getBGColors()
  {
    return bgBlue;
  }
}

class Nebula2  extends BaseVolume
{
  
  final float radius=1.5f;
  final float invradius=1.f/radius;
  
  
  public vRes  sample( PVector p, vRes br, State cs )
  {
    float d =p.dot(p);
    float namp = 6.f;
    int numOct =6- cs.quality*2;
    float ox=57.f+cs.time*.1f;

    d += (abs(g_lattice.fbm(p.x+357.f+ox,p.y+53.f+ox,p.z+13.f+ox,numOct,.5f)*.5f)-.25f)*namp;
    //float a3 = 1. - min(abs(d-0.5)*2.,1.);
    float a3 =  smoothstep(0.25f,0.5f,d)-smoothstep(0.5f,0.75f,d);
    float ba = a3*a3;
    a3 = a3*1.5f;

    br.c.set(2.f+ba*2.f,2.f+ba*2.f,4.f);   
    br.a= a3*0.3f;   
    return br;
  }
  public float getRotation(State cs)
  {
    return cs.time*2.f*PI/32.f;
  }
  public float getRange() {
    return 1.6f;
  }
  
  public float[] getBGColors()
  {
    return bgBlue;
  }
}

class Eskimo  extends BaseVolume
{
  final float radius=1.5f;
  final float invradius=1.f/radius;
  
  public vRes  sample( PVector p, vRes br, State cs)
  {
    // a hollow sphere with slight wobble
    float d = p.mag()*invradius;

    float c= smoothstep( 0.2f,0.0f,d);

    float nf = 1.f;
    float namp = 1.f;
    int numOct =4- cs.quality*2;
    float ox=57.f+cs.time*.1f;
    d += (abs(g_lattice.fbm(p.x*nf+ox,p.y*nf*2.f+513.f+ox*2.f,p.z*nf+13.f+ox,numOct,.4f)*.5f)-.25f)*namp;

    float c3= smoothstep( 0.f,1.1f,d) - smoothstep( 1.1f,1.3f,d);   
    d = smoothstep( 0.6f-cs.pulse*.2f,0.7f,d)-smoothstep(0.7f,0.8f,d);
    d +=c*2.f*(cs.pulse+1.f);//*min(cs.pulse*4.,2.);
    PVector col = new PVector(2.f+c*8.f,2.f+c*3.f,24.f);
    col.mult(d);
    col.x += c3*5.f;
    //d += c3*0.1;
    br.set( col,d *.1f);
    return br;
  }

  public float getRotation(State cs)
  {
    return -cs.time*2.f*PI/32.f;
  }
  public float getRange() {
    return radius+0.1f;
  }
  
  public float[] getBGColors()
  {
    return bgYellow;
  }
};

public float[] smoothstepSetup(float edge0, float edge1)
{
  float denom=1.f/(edge1 - edge0);
  float offset= -edge0*denom;
  float[] res = new float[2];
  res[0]=denom;
  res[1]=offset;
  return res;
}
public float smoothstep (float edge0, float edge1, float x)
{
  x =  min(max((x - edge0) / (edge1 - edge0),0.0f),1.0f);
  return x*x*(3-2*x);
}
public float smoothstepPre(float[] p, float x)
{
  x =  min(max(x*p[0] +p[1],0.0f),1.0f);
  return x*x*(3-2*x);
}

class HieghtField  extends BaseVolume
{

  public vRes  sample( PVector p, vRes br, State cs )
  {
    //PVector p =rotatex(dp, cs );
    float d = abs(p.y);

    float ox=57.f+cs.time*.1f;
    //  d+=abs(g_lattice.fbm( p.x+2057.f,p.z+1013.f, 2, .5f)*.5)-.25f;
    d +=abs(g_lattice.tableNoise2d( p.x+2057.f+ox,p.z+1013.f+ox*4.f)*.5f)-cs.pulse*.4f;

    float a = smoothstep( 0.45f,0.5f,d) - smoothstep(0.5f,0.8f,d);
    a += smoothstep( cs.pulse,0.0f,abs(p.x)) * smoothstep( cs.pulse,0.0f,abs(p.z)) * 2.f;
    a += smoothstep( max(0.5f-cs.pulse,0.001f),0.0f,abs(p.y))*(1.f-cs.pulse)*.5f;
    PVector col=new PVector(1.f+d*8.f,6.f,1.f);
    br.set( col, a*.1f);
    return br;
  }

  PVector m_boxmin=new PVector(-1,-.8f,-.8f);
  PVector m_boxmax=new PVector(1,0.8f,0.8f);


  public float getRange() {
    return 2.0f;
  };
  
  public float[] getBGColors()
  {
    return bgYellow;
  }
}
class ColouredSphere   extends BaseVolume
{
  PVector[] colTable=new PVector[256];
  ColouredSphere()
  {   
    for(int i=0;i<256;i++)
      colTable[i]=col((float)i/64.f);
  }
  public PVector col( float d )
  {
    float id = constrain( 2.0f - d*2.f, 0.f,1.f);
    float r = id;
    float b = 1.f-abs(id-.5f)*2.f;
    float g = id*id*id;
    float i = 2.f;
    return new PVector(r*i,g*i,b*i);
  }
  public PVector getIntersectionVolume(PVector ro, PVector rd, State cs )
  {
    float spRad = 1.5f;//-(1.-cs.pulse)*.5;
    return  intSphereBoundCZero( spRad, ro, rd);
  }
  public vRes  sample( PVector p, vRes br, State cs )
  {
    float d = p.mag();
    float nf = 3.f;
    int numOct =5- cs.quality*2;
    float ox=57.f+cs.time*.2f;
    float hotness=cs.pulse*.25f+.75f;
    d += (abs(g_lattice.fbm(p.x*nf+ox,p.y*nf+513.f+ox,p.z*nf+13.f+ox,numOct,.6f)*.5f)-.25f)*2.f*hotness;
    float a = smoothstep( 1.f,0.9f,d);
    PVector cv=col(d+(.5f-hotness*.5f));
    //cv.set(colTable[(int)max((d+(.5-hotness*.5))*64.,0.)] );
    br.set(cv, a);
    return br;
  }
  public float[] getBGColors()
  {
    return bgGreen;
  }
  
}
class DiscoBall   extends BaseVolume
{
  PVector ldir=new PVector(0.2f,1.f,0.f);
  PVector lpos=new PVector(0.f,-1.f,0.f);
  PVector lcol=new PVector(1.f,0.8f,0.5f);
  PVector abcol=new PVector(0.2f,0.2f,0.4f);

  DiscoBall()
  {  
    ldir.normalize();
  }
  public vRes  sample( PVector p, vRes br, State cs)
  {
    PVector spdir=PVector.sub(p,lpos);
    spdir.x+=cs.pulse*.5f-.5f;
    float mag2 = spdir.x*spdir.x+spdir.y*spdir.y+spdir.z*spdir.z;
    float invd=1.f/sqrt(mag2);
    float l=constrain(invd*invd*(1.f+cs.pulse*2.f),0.f,6.f);
    spdir.mult(invd);
    float d = spdir.dot(ldir);

    int numOct =4- cs.quality*2;
    float ox=57.f+cs.time*.1f;
    float nf = 5.f;
    float acone =smoothstep(0.4f+cs.pulse*.5f,0.9f,d);
    float a = ((g_lattice.fbm(p.x*nf+57.f+ox,p.y*nf+513.f+ox,p.z*nf+13.f+ox,numOct,.6f)+1.f)*.5f);
    a = p.y>1.f ? 0 : a;

    PVector lgt=PVector.mult(lcol,acone*l);   
    lgt.add(abcol);
    br.set( lgt, a);
    return br;
  }

  public float[] getBGColors()
  {
    return bgBlue;
  }

  public float getRange() {
    return 1.7f;
  };
}
// from http://sfdm.ca.scad.edu/faculty/mkesson/docs_pixar/volume_rendering.html
class Fire   extends BaseVolume
{
  float[] icanTempTable;
  float[] biasDensityTable;
  PVector incancol;
  PVector[] incanlghtCol=new PVector[1024];
  
  
  
  public float[] getBGColors()
  {
    return bgRed;
  }

  Fire(PVector incandensce)
  {
    incancol=incandensce;
    icanTempTable = new float[1024];
    biasDensityTable = new float[1024];
    for (int i=0;i<1024;i++)
    {
      // most of shader moved here
      float temp=(float)i/255.f;

      float biasedtemp=inputbias(temp, -0.2f);
      float incan= (1.0f - smoothstep(0.143f, 0.857f, biasedtemp));
      icanTempTable[i]=incan;
      incanlghtCol[i]=PVector.mult( incancol,incan);
      float Oi;

      float density=temp*2.f;
      float biaseddensity = inputbias(density, 0.315f);
      if (biaseddensity < 0.15f) 
        Oi = 0.9f * smoothstep(0.136f, 0.15f, biaseddensity);
      else 
        Oi = 0.9f * (1 - smoothstep(0.15f, 0.857f, biaseddensity));
      biasDensityTable[i]=Oi;
    }
  }


  PVector m_boxmin=new PVector(-.5f,-.8f,-.5f);
  PVector m_boxmax=new PVector(.5f,0.8f,.5f);

  public PVector getIntersectionVolume(PVector ro, PVector rd, State cs )
  {
    PVector invrd=new PVector( 1.f/rd.x,1.f/rd.y,1.f/rd.z);
    return intersectBoxCZero(ro, invrd, m_boxmin, m_boxmax);
  }

  public float inputbias(float x, float bias) {
    return pow(x, -log(0.5f + bias) / log(2));
  }

  public vRes  sample( PVector p, vRes br, State cs)
  {
    //   PVector p=rotatea(cs.time*PI*.25,dp);
    float d =( p.x*p.x+p.z*p.z)+(1.f-p.y)*.15f;
    d = p.y<1 ? d : 1;

    //d=p.mag()*.5;
    int numOct =4- cs.quality*1;
    float ox=57.f+cs.time;

    d += abs((g_lattice.fbm(p.x+ox,p.y+513.f+ox,p.z+13.f+ox,numOct,.6f))*.6f);

    // density=constrain( density+a,0.,8.);
   // float density= smoothstep( 0.7f,0.2,d) - smoothstep(0.7,0.85f,d);
    float density= smoothstep(  0.8f,0.0f,d);// - smoothstep(0.9,0.95f,d);
    density=1.f-density;
    float temp =cs.pulse*.5f+0.f;
    float incan=icanTempTable[(int)(temp*255.f)];
    PVector incandescence = PVector.mult( incancol,incan);
    float Oi=biasDensityTable[(int)(density*128.f)];
    br.set( incandescence,Oi);
    return br;
  }
  public float getRange() {
    return 1.f;
  };
}
public PVector rotatea(PVector p, State cs )
{
  PVector rp=new PVector();
  rp.y=p.y;
  rp.x = cs.cosang*p.x-cs.sinang*p.z;
  rp.z = cs.cosang*p.z+cs.sinang*p.x;
  return rp;
}

class Hieght2   extends BaseVolume
{
  public vRes  sample( PVector p, vRes br, State cs)
  {
    float ox=cs.time*.4f;
    float nf=g_lattice.tableNoise2d( p.x+13.8f+ox*.1f,p.z+153.8f+ox);
    float hn=nf*.8f+cs.pulse*.5f;
    float d=smoothstep( p.y,p.y-.05f,hn)*2.f;//>hn ? 1.: 0;
    float pz=p.z*6;
//    float grid=pz-(int)pz;
  //  float gv=smoothstep( 0.4,0.5,grid)-smoothstep( 0.5,0.6,grid);
    PVector col=new PVector(3.f,1.f,0.5f);
    br.set( col,d);

    return br;
  }
  
  public float[] getBGColors()
  {
    return bgGreen;
  }

  PVector m_boxmin=new PVector(-1,-.8f,-1.5f);
  PVector m_boxmax=new PVector(1,0.8f,1.5f);

  public PVector getIntersectionVolume(PVector ro, PVector rd, State cs )
  {
    PVector invrd=new PVector( 1.f/rd.x,1.f/rd.y,1.f/rd.z);
    return intersectBoxCZero(ro, invrd, m_boxmin, m_boxmax);
  }

  public float getRotation(State cs)
  {
    return (cs.halfpulse*2.f-1.f)*PI/8.f;
  }
  public float getRange() {
    return 1.5f;
  };
}
class TextV   extends BaseVolume
{
  PGraphics pg;
  int[]    pix;
  PFont font;
  PVector m_boxmin=new PVector(-1.8f,-1.f,-.3f);
  PVector m_boxmax=new PVector(1.8f,1.f,.3f);
  TextV( String val )
  {
    //font=loadFont("Andalus-48.vlw");

    pg = createGraphics(196, 64, P2D);
    pg.beginDraw();
    pg.background(0);
    PFont font;
	font = createFont("SansSerif.plain", 48); 
	textFont(font);
    pg.fill(~0);
    pg.line(20,20,40,40);
    pg.textSize(48);
    pg.text(val,0,48);
    pg.endDraw();

    pg.loadPixels();
    pix=pg.pixels;
    pg.updatePixels();

    boolean anyset=false;
    for(int i=0;i<pix.length;i++)
      if ( ((pg.pixels[i])&0xff)!=0)
        anyset=true;
    // percompute bilinear frac
    assert( anyset);
  }

  public PVector getIntersectionVolume(PVector ro, PVector rd, State cs )
  {
    PVector invrd=new PVector( 1.f/rd.x,1.f/rd.y,1.f/rd.z);
    return intersectBoxCZero(ro, invrd, m_boxmin, m_boxmax);
  }

  public float getRotation(State cs)
  {
    return sin(cs.time*2.f*PI*.15f)*.25f;
  }

  public vRes  sample( PVector p, vRes br, State cs)
  {
    // do 3D noise on p
    PVector np = new PVector();
    np.set(p);
    //    PVector np=rotatea(p,cs);
    float ox=57.f+cs.time*.4f;
    float amp = cs.pulse*0.5f;
    np.x += g_lattice.tableNoise2d( np.x+13.8f+ox,np.y+153.8f+ox)*amp;
    np.y += g_lattice.tableNoise2d( np.y+13.8f+ox*2.f,np.z+153.8f+ox*2.f)*amp;
    np.z += g_lattice.tableNoise2d( np.z+13.8f+ox,np.x+153.8f+ox)*amp;

    float x =constrain( (np.x+1.7f)*.25f*(float)pg.width,0,pg.width-2);
    float y=constrain((np.y+2.f)*.25f*(float)pg.height,0,pg.height-2);
    int ix=(int)x;
    int iy=(int)y;
    float u=x-(float)ix;
    float v =y-(float)iy;
    int i0=ix;
    int i1=ix+1;
    int j0=iy*pg.width ;
    int j1=(iy+1)*pg.width ;

    float a =(float)( pix[i0+j0 ]&0xff);
    float b =(float)( pix[ i1+j0]&0xff);
    float c = (float)(pix[ i0+j1 ]&0xff);
    float d =(float)( pix[ i1+j1 ]&0xff);
    float k1 =   b - a;
    float k2 =   c - a;
    float k4 =   a - b - c + d;

    float dv=a+k1*u+k2*v+k4*u*v; 
    dv *=1.f/255.f * ( 1.f-cs.pulse);

    int numOct =4- cs.quality*2;
    dv *=smoothstep(0.3f,0.25f, abs(np.z));
    PVector incancol=new PVector(7.776f*(.5f+(cs.pulse)),20*.5f, 3.702f*(.5f+(1.f-cs.pulse)) );
    br.set( incancol,dv*(cs.pulse*.5f+.25f));
    return br;
  }
  public float getRange() {
    return 1.8f;
  };
}


class World extends BaseVolume
{
  public vRes  sample( PVector p, vRes br, State cs)
  {
    float d=p.mag();
    d = d/1.3f;
     float nf=3.5f;
    float ox=cs.time*.1f;
    int numOct=cs.numOct;
     float a = g_lattice.fbm(p.x*nf+57.f+ox,p.y*nf+513.f+ox,p.z*nf+13.f+ox,numOct,.6f);
    float a2=max(a,0.f);
    d-= a2*0.07f;
    d = smoothstep(1.f,0.8f-cs.pulse*.5f,d);
    d *=d;
    d*=6.f;
   
    br.c.set(0.6f,1.f,2.f);
    if ( d>3.f)
    {
      d*=2.f;
      float sblend=constrain( a*8+.5f,0.f,1.f);
      br.c.set(sblend*a*2.f,sblend*(a+.5f),1.f - sblend* 0.5f);
    }   
    br.a=d;
    return br;
  }
  public float getRange() {
    return 1.3f;
  };
  public float getRotation(State cs) {
    return cs.time*.15f;
  }
  public float[] getBGColors()
  {
    return bgBlue;
  }
  public void update(State cs) {
     cs.numOct=5- cs.quality*2;
     cs.ox=57.f+cs.time*.1f;
  }
}
 

class ColouredSphere2 extends BaseVolume
{
  public PVector col( float d )
  {
    float id = constrain( 2.0f - d*2.f, 0.f,1.f);
    float r = id;
    float b = 1.f-abs(id-.5f)*2.f;
    float g = id*id*id;
    float i = 2.f;
    return new PVector(r*i+.1f,g*i+.2f,b*i+.5f);
  }
  public vRes  sample( PVector p, vRes br, State cs )
 {
    float d = p.dot(p);
    float nf = 2.f;
    int numOct =6- cs.quality*2;
    float ox=57.f+cs.time;
    float hotness=(1.f-p.y)*.5f+cs.pulse*0.5f;
    d += (abs(g_lattice.fbm(p.x*nf+ox,p.y*nf*0.75f+513.f+ox*2.f,p.z*nf+13.f+ox,numOct,.6f)*.5f)-.25f)*2.f*hotness;
    float a = smoothstep( 1.f,0.9f,d);
    PVector cv=col(d*1.25f);//+(.5-hotness*.5));
    br.set(cv, a);
    return br;
  }
  public float getRange() {
    return 1.25f;
  };
  public float[] getBGColors()
  {
    return bgBlue;
  }
} 
class DiscoBall2 extends BaseVolume
{
  PVector insideCol=new PVector(1.f,0.f,0.25f);
  PVector outsideCol=new PVector(0.f,0.1f,2.f);

  DiscoBall2()
  {
    outsideCol.sub(insideCol);
  }
  public void update( State cs )
  {
    cs.numOct=6- cs.quality*3;
    cs.ox=57.f+cs.time;
  }
  public vRes  sample( PVector p, vRes br, State cs)
  {
    float len=p.mag();
    float strength =len/1.5f;
    float d = smoothstep(1.f,.8f,strength);
    PVector np=br.c;
    np.set(p);
    np.mult(1.f/len);
 
    int numOct=cs.numOct;
    float ox=cs.ox;
    float nf=3.f;
      float ns=g_lattice.fbm(np.x*nf+57.f+ox,np.y*nf+513.f+ox,np.z*nf+13.f+ox,numOct,.6f);
    float cf = ns-.5f;
    cf = constrain(cf*4.f,0.f,2.f);
 
    PVector cval=PVector.add(insideCol,PVector.mult(outsideCol,strength));
    float star = ((1.f-strength)+ns*.25f)*(cs.pulse2+1.f);
    cval.add(0.f,star,0.f);
    br.set(cval,d*(cf+star));
    return br;
  }
  public float getRange() {
    return 1.5f;
  };
  public float[] getBGColors()
  {
    return bgBlue;
  }
}


Volume Volumes[];

float stepSizeModifiers[]= {
  0.8f,//Text0
  0.8f,//Text1
  1.f,//Height2

  1.8f,// EdgedSphere(),

  0.9f,//fire
  1.7f, // spotlight
  1.3f, // HieghtField(),
  0.85f, // CrabNebula(),

  1.1f,// Eskimo(),
  1.2f,// ColouredSphere(),
  0.85f,// Nebula2(),

  0.9f,// Cloud(),
  1.2f,// Sphere(),
 1.f,//fire
  1.f,//fire
   0.9f,//blobbiess
   0.9f,//fireball
  0.9f,//cloud puff
 1.2f,
 0.9f,//blue fireball
0.8f,
1.f,
};

public class WorkQueue
{
  private final int nThreads;
  private final PoolWorker[] threads;
  private final LinkedList queue;

  public boolean IsDecentMachine() 
  {
    return Runtime.getRuntime().availableProcessors()>2;
  }
  public WorkQueue()
  {
    // Need to dynamically kill one of the worker threads?
    
// due to audio?
    int nThreads = Runtime.getRuntime().availableProcessors();
    println("Using "+ nThreads+ " Threads");
    this.nThreads = nThreads;
    queue = new LinkedList();
    threads = new PoolWorker[nThreads];

    for (int i=0; i<nThreads; i++) {
      threads[i] = new PoolWorker();
      threads[i].start();
    }
  }
  public void clear() {
    synchronized(queue) {
      queue.clear();
    }
  }
  public int NumWorkItems()
  {
    return queue.size();
  }
 
// workqueue

public boolean grabTask()
  {
      Runnable r = null;
 
     synchronized(queue) {
        if(!queue.isEmpty()) {
          r =(Runnable) queue.removeFirst();
        }
        else r = null;
      }    
      if ( r==null)
        return false;
      r.run();
      return true;
  }

  
  public boolean IsFinished() {
    for (int i=0; i<threads.length; i++)
      if ( threads[i].working )
        return false;
    return queue.isEmpty();
  }
  public void Wait()
  {
    while(grabTask());
    
    while(!IsFinished()) {
      try {
        Thread.sleep(20);
      }
      catch (InterruptedException e) {
        // You might want to log something here
        println("TASK ERROR");
      }
    }
  }
  public void execute(Runnable r) {
    synchronized(queue) {
      queue.addLast(r);
      queue.notify();
    }
  }

  private class PoolWorker extends Thread {
    boolean working;

    public void run() {
      working = true;
      Runnable r;

      while (true) {
        synchronized(queue) {
          while (queue.isEmpty()) {
            working = false;
            try {                           
              queue.wait();
            }
            catch (InterruptedException ignored)
            {
            }
          }                   
          working = true;
          r = (Runnable) queue.removeFirst();
        }
        // If we don't catch RuntimeException,
        // the pool could leak threads
        //try {
        r.run();
        // }
        // catch (RuntimeException e) {
        // You might want to log something here
        //   println("TASK ERROR");
        //  }
      }
    }
  }
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "reuze.demo.demoEffects" });
  }
}
