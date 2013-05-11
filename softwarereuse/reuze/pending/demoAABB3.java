package reuze.pending;

import com.software.reuze.gb_AABB3;
import com.software.reuze.gb_Vector3;
import com.software.reuze.gb_i_Mesh;
import com.software.reuze.z_ToxiclibsSupport;

import processing.core.PApplet;
public class demoAABB3 extends PApplet {
	gb_AABB3 a,b;
	z_ToxiclibsSupport gfx;
	gb_i_Mesh m;
	gb_Vector3 sphere,
			boxVelocity=new gb_Vector3(01.0f,03.0f,0),
			box2Velocity=new gb_Vector3(-01.5f,-02.0f,0),
			sphereVelocity=new gb_Vector3(02.5f,-03.0f,0),
			spherePosition=new gb_Vector3(0,0,0);
   public void setup() {
	   size(400,400,P3D);
	   a=new gb_AABB3(new gb_Vector3(5,5,5), 10);
	   b=new gb_AABB3(new gb_Vector3(5,5,5), 15);
	   gfx = new z_ToxiclibsSupport(this);
	   a.updateBounds();  b.updateBounds();
	   smooth();
   }
   public void draw() {
	   background(0);
	   lights();
	// Change height of the camera with mouseY
	   camera(30.0f, mouseY, 220.0f, // eyeX, eyeY, eyeZ
	          0.0f, 0.0f, 0.0f, // centerX, centerY, centerZ
	          0.0f, 1.0f, 0.0f); // upX, upY, upZ
	   int color1=color(0,0,255),color2,color3;
	   color2=color1; color3=color1;
	   noFill();
	   stroke(255,0,0);
	   beginShape();
	   vertex(-width/N, -height/N);
	   vertex(width/N, -height/N);
	   vertex(width/N, height/N);
	   vertex(-width/N, height/N);
	   endShape(CLOSE);
	   noStroke();
	   if (a.intersectsSphere(spherePosition, 20)) {color1=color(255,0,0); color3=color(255,0,0);}
	   if (b.intersectsSphere(spherePosition, 20)) {color2=color(255,0,0); color3=color(255,0,0);}
	   if (a.intersectsBox(b)) {color1=color(255,0,0); color2=color(255,0,0);}
	   fill(color1);
	   gfx.box(a, true);
	   fill(color2);
	   gfx.box(b, true);
	   fill(color3);
	   translate(spherePosition.x,spherePosition.y,spherePosition.z);
	   sphere(20);
	   updatePosition(a.position,boxVelocity);
	   updatePosition(spherePosition,sphereVelocity);
	   updatePosition(b.position,box2Velocity);
	   a.updateBounds(); b.updateBounds();
   }
   public static final int N=4;
   public void updatePosition(gb_Vector3 position/*inout*/, gb_Vector3 velocity/*inout*/) {
	   boolean flipX=false, flipY=false;
	   position.add(velocity);
	   if (position.x<=-width/N) {position.x=-width/N; flipX=true;}
	   if (position.y<=-height/N) {position.y=-height/N; flipY=true;}
	   if (position.x>=width/N) {position.x=width/N; flipX=true;}
	   if (position.y>=height/N)  {position.y=height/N; flipY=true;}
	   if (flipX) velocity.x=-velocity.x;
	   if (flipY) velocity.y=-velocity.y;
   }
}
