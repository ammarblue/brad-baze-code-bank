package reuze.demo;
import com.software.reuze.ff_STLLoader;
import com.software.reuze.gb_Vector3;
import com.software.reuze.gb_WETriangleMesh;
import com.software.reuze.z_ToxiclibsSupport;

import processing.core.*;
//import processing.opengl.*;
public class demoToxiSTLloadAudi  extends PApplet {
z_ToxiclibsSupport gfx;
gb_WETriangleMesh box;

public void setup() {
    size(680, 382, P3D);
    gfx = new z_ToxiclibsSupport(this);
    box = new gb_WETriangleMesh().addMesh(new ff_STLLoader().loadBinary("./data/audi.stl",ff_STLLoader.WEMESH));
    // properly orient and scale mesh
    box.rotateX(HALF_PI);
    box.scale(8);
}

public void draw() {
    // update mesh normals
    box.computeFaceNormals();
    // setup lighting
    background(51);
    lights();
    directionalLight(255, 255, 255, -200, 1000, 500);
    specular(255);
    shininess(16);
    // point camera at mesh centroid
    gb_Vector3 c = box.computeCentroid();
    camera(-100, -50, 80, c.x, c.y, c.z, 0, 1, 0);
    // draw coordinate system
    gfx.origin(new gb_Vector3(), 50);
    // draw physics bounding box
    stroke(255, 80);
    noFill();
    // draw car
    fill(160);
    noStroke();
    gfx.mesh(box, false, 0);
}
}