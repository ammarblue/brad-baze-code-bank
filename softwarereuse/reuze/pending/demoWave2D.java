package reuze.pending;

import com.software.reuze.m_Wave2D;

import processing.core.PApplet;

public class demoWave2D extends PApplet {
m_Wave2D x, y;
float theta = 0.0f; 
float amp = 77.5f; 
float prd = 125f;
int wid;
float[] wavearray; 
int wavesp = 5; 
double dim2d; 


public void draw() {
background(-1);
x=DimWave();
y=createWave();

}


m_Wave2D DimWave() {
m_Wave2D r = null;
theta += 0.02f;
float thta = theta;
for (int i = 0; i < wavearray.length; i++) {
wavearray[i] = tan(thta)*amp;
thta+=dim2d;
}
return r;
}

public void setup() {
size(680, 320);
smooth();
noStroke();
wid = width+16;
dim2d = (6.28 / prd) * wavesp;
wavearray = new float[wid/wavesp];
println(wavearray.length);
frameRate(5);
}


m_Wave2D createWave() {
m_Wave2D r2 = null;	
for ( int i = 0; i < wavearray.length; i++ ) {
noStroke();
fill( wavearray[i], 25, 255, 255 );
ellipse( i*wavesp,width/2+wavearray[i], height/2+wavearray[i],4 );
}
return r2;
}
}