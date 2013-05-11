package reuze.demo;
import com.software.reuze.ga_Rectangle;
import com.software.reuze.ga_Vector2;
import com.software.reuze.gb_AABB3;
import com.software.reuze.gb_IntersectionData;
import com.software.reuze.gb_Quaternion;
import com.software.reuze.gb_Terrain;
import com.software.reuze.gb_TriangleMesh;
import com.software.reuze.gb_Vector3;
import com.software.reuze.gb_i_Mesh;
import com.software.reuze.m_MathUtils;
import com.software.reuze.m_Matrix4;
import com.software.reuze.z_ToxiclibsSupport;

import processing.core.*;
//import processing.opengl.*;
public class demoToxiGLTerrainCar  extends PApplet {
	float NOISE_SCALE = 0.08f;
	int DIM=80;

	gb_Terrain terrain;
	z_ToxiclibsSupport gfx;
	gb_i_Mesh mesh;

	final static gb_Vector3 camOffset = new gb_Vector3(0, 100, 300);
	gb_Vector3 eyePos = new gb_Vector3(0, 1000, 0);
    Car car;
	public void setup() {
	  size(1024, 576, P3D);
	  // create terrain & generate elevation data
	  terrain = new gb_Terrain(DIM,DIM, 50);
	  float[] el = new float[DIM*DIM];
	  noiseSeed(23);
	  for (int z = 0, i = 0; z < DIM; z++) {
	    for (int x = 0; x < DIM; x++) {
	      el[i++] = noise(x * NOISE_SCALE, z * NOISE_SCALE) * 400;
	    }
	  }
	  terrain.setElevation(el);
	  // create mesh
	  mesh = terrain.toMesh();
	  // create car
	  car = new Car(0, 0);
	  gfx = new z_ToxiclibsSupport(this);
	  //noLoop();
	}

	public void draw() {
		  if (keyPressed) {
			    if (keyCode == UP) {
			      car.accelerate(1);
			    }
			    if (keyCode == DOWN) {
			      car.accelerate(-1);
			    }
			    if (keyCode == LEFT) {
			      car.steer(0.1f);
			    }
			    if (keyCode == RIGHT) {
			      car.steer(-0.1f);
			    }
			  }
			  // update steering & position
			  car.update();
	// adjust camera offset & rotate behind car based on current steering angle
	  gb_Vector3 camPos = car.pos.cpy().add(camOffset.getRotatedY(car.currTheta + HALF_PI));
	  gb_AABB3 bb=mesh.getBoundingBox();
	  bb.constrain(camPos);
	  float y = terrain.getHeightAtPoint(camPos.x, camPos.z);
	  if (!Float.isNaN(y)) {
	    camPos.y = max(camPos.y, y + 100);
	  }
	  eyePos.interpolate(camPos, 0.05f);     
	  background(0xffaaeeff);
	  camera(eyePos.x, eyePos.y, eyePos.z, car.pos.x, car.pos.y, car.pos.z, 0, -1, 0);
	  //System.out.println(eyePos+" "+car.pos);
	  //camera(eyePos.x, eyePos.y, eyePos.z, pos.x, pos.y, pos.z, 0, -1, 0);
	  // setup lights
	  directionalLight(192, 160, 128, 0, -1000, -0.5f);
	  directionalLight(255, 64, 0, 0.5f, -0.1f, 0.5f);
	  fill(255);
	  noStroke();
	  // draw mesh & car
	  gfx.mesh(mesh, false);
      car.draw();
	}
	class Car extends ga_Vector2 {

		  gb_Vector3 currNormal = gb_Vector3.Y.cpy();
		  gb_Vector3 pos;
		  gb_IntersectionData isec;

		  float currTheta;
		  float targetTheta;
		  float targetSpeed;
		  float speed;

		  public Car(float x, float y) {
		    super(x, y);
		    pos = new gb_Vector3(500,500,0);
		  }

		  public void accelerate(float a) {
		    targetSpeed += a;
		    targetSpeed = m_MathUtils.clip(targetSpeed, -20, 20);
		  }

		  public void draw() {
		    // create an axis aligned box and convert to mesh
		    gb_TriangleMesh box = (gb_TriangleMesh)new gb_AABB3(new gb_Vector3(), new gb_Vector3(20, 10, 10)).toMesh();
		    gb_AABB3 b = box.getBoundingBox();
		    ga_Rectangle r=new ga_Rectangle(b.getMin().to2DXZ(), b.getMax().to2DXZ()).mul(0.99f);
            //System.out.println(currNormal);
            m_Matrix4 m=new m_Matrix4();
            gb_Quaternion q=gb_Quaternion.getAlignmentQuat(currNormal, gb_Vector3.Z);
            //System.out.println(q);
            q.toMatrix4(m);
            //System.out.println(m);
		    // align to terrain normal
		    box.pointTowards(currNormal);
		    
		    // rotate into direction of movement
		    box.rotateAroundAxis(currNormal, currTheta);
		    //for (Vector3Id v:box.getVertices()) System.out.println(v);
		    // move to correct position
		    box.translate(pos);
		    fill(255, 0, 0);
		    // and draw
		    gfx.mesh(box);
		  }

		  public void steer(float t) {
		    targetTheta += t;
		  }

		  public void update() {
			  if (Math.abs(pos.x-952.625)<=0.01f)
				  pos.z=0;
		    // slowly decay target speed
		    targetSpeed *= 0.992f;
		    // interpolate steering & speed
		    currTheta += (targetTheta - currTheta) * 0.1f;
		    speed += (targetSpeed - speed) * 0.1f;
		    // update position
		    add(ga_Vector2.fromTheta(currTheta).mul(speed));
		    // constrain position to terrain size in XZ plane
		    gb_AABB3 b = mesh.getBoundingBox();
		    ga_Rectangle r=new ga_Rectangle(b.getMin().to2DXZ(), b.getMax().to2DXZ()).mul(0.99f);
		    r.constrain(this);
		    // compute intersection point on terrain surface
		    isec = terrain.intersectAtPoint(x, y);
		    if (isec.isIntersection) {
		      // smoothly update normal
		      currNormal.interpolate(isec.normal, 0.25f);
		      // move bot slightly above terrain
		      gb_Vector3 newPos = isec.pos.add(0, 10, 0);
		      pos.interpolate(newPos, 0.25f);
		    }
		  }
		}
}