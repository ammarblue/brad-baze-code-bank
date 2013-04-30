int nrows = 1201;
int ncols = 1201;
int bytesPerPoint = 2;

float[][] data;
float maxData, minData;

void getData(){
  float scaleFactor = .013;
  data = new float[nrows][ncols];
  byte[] all = loadBytes("N35E138.hgt");
  for(int i = 0; i < nrows; i++) {
    int curOff = (i * nrows) * bytesPerPoint;
    for(int j = 0; j < ncols; j++) {
      int curPoint = 
        (((all[curOff + 0]) & 0xff) << 8) |
        ((all[curOff + 1]) & 0xff);
      if(curPoint > 0x0fff) // no data
        data[i][j] = 0;
      else
        data[i][j] = scaleFactor * curPoint;
      curOff += bytesPerPoint;
    }
  }
  // fix hole in side of Fuji
  float holeHeigth = 1;
  for(int i = 1; i < nrows-1; i++) {
    for(int j = 1; j < ncols-1; j++) {
      if(data[i][j]<holeHeigth){
        float replace = 0;
        int devide = 0;
        if(data[i+1][j]>holeHeigth){
          replace += data[i+1][j];
          devide++;
        }
        if(data[i][j+1]>holeHeigth){
          replace += data[i][j+1];
          devide++;
        }
        if(data[i-1][j]>holeHeigth){
          replace += data[i-1][j];
          devide++;
        }
        if(data[i][j-1]>holeHeigth){
          replace += data[i][j-1];
          devide++;
        }
        if(devide>1){replace/=devide;}
        data[i][j] = replace;
      }
    }
  }
}
class square{
  int x1;
  int y1;
  int x2;
  int y2;
  boolean isParent;
  square[] children;
  square(int a, int b, int c, int d){
    x1 = a;
    y1 = b;
    x2 = c;
    y2 = d;
    isParent = false;
  }
  void makeParent(PVector r, float weight){
    PVector mid = new PVector((x2+x1)/2,(y2+y1)/2);
    PVector dx = PVector.sub(r,mid);
    if((x2-x1)*weight>dx.mag()){
      if(x2-x1>1){
        isParent = true;
        children = new square[4];
        int xm = (x1+x2)/2;
        int ym = (y1+y2)/2;
        children[0] = new square(x1,y1,xm,ym);
        children[0].makeParent(r, weight);
        children[1] = new square(xm,y1,x2,ym);
        children[1].makeParent(r, weight);
        children[2] = new square(x1,ym,xm,y2);
        children[2].makeParent(r, weight);
        children[3] = new square(xm,ym,x2,y2);
        children[3].makeParent(r, weight);
      }
    }
  }
  void clear(){
    isParent = false;
  }
  void draw(){
    if(isParent){
      for(int i=0;i<4;i++){
        children[i].draw();
      }
    }else{
      drawSector(x1,y1,x2,y2);
    }
  }
}
PVector bound(PVector X){
  float k = 0.1;
  PVector force = new PVector();
  if(X.x<sqr.x1){
    force.x = (sqr.x1-X.x)*k;
  }
  if(X.y<sqr.y1){
    force.y = (sqr.y1-X.y)*k;
  }
  if(X.x>sqr.x2){
    force.x = -(X.x-sqr.x2)*k;
  }
  if(X.y>sqr.y2){
    force.y = -(X.y-sqr.y2)*k;
  }
  return force;
}
