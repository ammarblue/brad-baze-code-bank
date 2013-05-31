/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/50864*@* */
/* !do not delete the line above, required for linking your tweak if you re-upload */
//WorldVis///////////////////////////////////////////////////////////////////////////////
//A generic tool to display some cuantitative data onto Earth surface////////////////////
//Inspired by Flink Labs visualization on climate change:////////////////////////////////
//www.flinklabs.com/projects/climatedata/////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
//This example display most populated metropolis (on this world :-)//////////////////////
//Data: cityPopulation + geoNames//////////////////////////////////////////////////////// 
//http://es.wikipedia.org/wiki/Anexo:Aglomeraciones_urbanas_m%C3%A1s_pobladas_del_mundo//
/////////////////////////////////////////////////////////////////////////////////////////
//Custom image based on work from [http://commons.wikimedia.org/wiki/User:Thesevenseas]//
//Font-family: Lato // Author: Lukasz Dziedzic///////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////Ale González · 60rpm.tv/i
/////////////////////////////////////////////////////////////////////////////Pubic Domain
/////////////////////////////////////////////////////////////////////////////////////////

int 
X,Y;
final int
TEXT_COL      = 0xaa000000,
DATA_COL      = 0x99ff0000,
HOVER_COL     = 0xffff0000,
WORLD_TINT    = 0xffffffff,
LINES_WEIGHT  = 2,
BUFF_LINES_W  = 6;
float 
a,b;
PGraphics 
bg, 
hover;
PFont 
h0,h1,h2,h3;
//
Globe w;
Table t;
DataHolder[] data;

////////////////////////////////////////////////////////////////////////////////////////

void setup(){
   //Buffers
  size(900,700,P3D); 
  bg= createGraphics(width,height, P2D);    //Buffer for storing the background
  hover=  createGraphics(width,height,P3D); //Color picking buffer
   //Fonts
  h1= loadFont("Lato-Regular-24.vlw");
  h2= loadFont("Lato-Light-24.vlw");
   //General settings
  X=  width/2+100;
  Y= height/2;
  createBackground (bg,X,Y,.1);
  frameRate(30);
  cursor(CROSS);
  textMode(SCREEN);
   //Objects
  w= new Globe(250,24,"w.png");
  t= new Table("coords.csv");
  data= new DataHolder[t.getNumRows()-1];
  for(int i=0;i<data.length;i++) data[i]= new DataHolder(i);   
}
  
  void createBackground (PGraphics pg, int X, int Y,float f){ 
      int x,y;
      pg.beginDraw();
      pg.smooth();
      for(int i=0;++i<pg.width*pg.height;) 
       {pg.set (x=i%pg.width, y=i/pg.width, (255-round(dist(x,y,X,Y)*f))*0x010101);}
      pg.endDraw();
      background(pg);
  }

////////////////////////////////////////////////////////////////////////////////////////

void draw(){
  background(bg);
  hover.beginDraw(); hover.background(0); hover.endDraw();
  lights();
  w.update();
  render(X,Y); 
  detectHover();
}

void render(int x, int y){
  hover.beginDraw();
  pushMatrix();
    translate(x,y);
    hover.translate(x,y);
    pushMatrix();
      rotateX(w.rotation.x);
      rotateY(w.rotation.y);
      hover.rotateX(w.rotation.x);
      hover.rotateY(w.rotation.y);
      fill(WORLD_TINT);
      w.render();
      for (int i=0;i<data.length;i++){
          data[i].render(g,false);
          data[i].render(hover,true);
      }
    popMatrix();
  popMatrix();
  hover.endDraw();
}

////////////////////////////////////////////////////////////////////////////////////////

void mouseDragged(){
  if (mouseButton==LEFT)  w.addRotation(mouseX,mouseY,pmouseX,pmouseY);
}

void detectHover(){
  int c=hover.get(mouseX,mouseY);
  int index= c/0x010101 + 254; 
  for(int i=0;i<data.length;i++){
    if (i==index) {
      data[i].setHoveredTo(true);
      fill(TEXT_COL);
      textFont(h1);
      text(data[i].NAME+", "+data[i].COUNTRY,75,height-175);
      textFont(h2);
      text("Población: "+nfc(data[i].VALUE),75,height-150);
      noFill();
    }else{
      data[i].setHoveredTo(false);}
  }
}
