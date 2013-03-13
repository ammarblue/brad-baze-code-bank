 
// camera shots
 
// shots
 
 
class PolarCamera
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
class Shot
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
class Menu
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
