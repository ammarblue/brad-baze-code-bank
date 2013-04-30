/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/13287*@* */
/* !do not delete the line above, required for linking your tweak if you re-upload */
square sqr;
int scaleFactor = 1024;
void setup(){
  size(600,400,P3D);
  sphereDetail(10);
  getData();
  int offset = 128;
  sqr = new square(offset,offset,1024+offset,1024+offset);
  car = new Car(new PVector(784.0194, 671.03815, 20.022648));
  dpad = new PVector();
  camX = new PVector(car.X.x,car.X.y,car.X.z);
}
PVector camX;
void draw(){
  color sky = color(#89AFFF);
  background(sky);
  car.cam();
  directionalLight(255, 255, 255, 1, -1, -1);
  directionalLight(red(sky)/2, green(sky)/2, blue(sky)/2,
                            -1, 1, -1);
  noStroke();
  fill(#675550);
  sqr.clear();
  sqr.makeParent(
    new PVector(car.X.x,car.X.y),10);
  sqr.draw();
  fill(0);
  beginShape(TRIANGLE_STRIP);
  vertex(0,0,-2.0*scaleFactor);
  vertex(0,nrows*scaleFactor,-2.0*scaleFactor);
  vertex(ncols*scaleFactor,0,-2.0*scaleFactor);
  vertex(ncols*scaleFactor,nrows*scaleFactor,-2.0*scaleFactor);
  endShape();
  for(int i=0;i<2;i++){
    car.iterate();
  }
  car.draw();
}
void drawSector(int a, int b, int c, int d){
  if((a>=0)&&(a<ncols)&&(b>=0)&&(b<nrows)&&
     (c>=0)&&(c<ncols)&&(d>=0)&&(d<nrows)){
    beginShape(TRIANGLE_STRIP);
    vertex(a*scaleFactor,b*scaleFactor,data[b][a]*scaleFactor);
    vertex(a*scaleFactor,d*scaleFactor,data[d][a]*scaleFactor);
    vertex(c*scaleFactor,b*scaleFactor,data[b][c]*scaleFactor);
    vertex(c*scaleFactor,d*scaleFactor,data[d][c]*scaleFactor);
    endShape();
  }
}
PVector dpad;
void keyPressed(){
  float accel = 0.1;
  float steer = PI/8;
  switch(keyCode){
    case UP:
      dpad.y = accel;
      break;
    case DOWN:
      dpad.y = -accel;
      break;
    case RIGHT:
      dpad.x = steer;
      break;
    case LEFT:
      dpad.x = -steer;
      break;
  }
}
void keyReleased(){
  switch(keyCode){
    case UP:
      dpad.y = 0;
      break;
    case DOWN:
      dpad.y = 0;
      break;
    case RIGHT:
      dpad.x = 0;
      break;
    case LEFT:
      dpad.x = 0;
      break;
  }
}
