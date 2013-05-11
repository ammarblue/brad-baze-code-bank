package reuze.pending;
import java.io.File;
import java.util.Iterator;

import com.software.reuze.ff_3DSModel;
import com.software.reuze.gb_Vector3;
import com.software.reuze.ff_3DSModel.MaterialInfo3DS;
import com.software.reuze.ff_3DSModel.Object3DS;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public class demo3DS extends PApplet {
	ff_3DSModel model;
	public void setup() {
		size(500,500,P3D);
		model=new ff_3DSModel(new File("./data/fish2.3ds"));
	}
	float x,y,z,s=25;
	public void draw() {
		background(200,255,200);
		translate(200,200);
		scale(s);
		rotateX(x); rotateY(y); rotateZ(z);
		render(0);
	}
	public void keyPressed() {
		if (key=='x') {
			x+=5; if (x>360) x=0;
		}
		if (key=='y') {
			y+=5; if (y>360) y=0;
		}
		if (key=='z') {
			z+=5; if (z>360) z=0;
		}
		if (key=='r') {
			x=0; y=0; z=0;
		}
		if (key=='s') {
			s+=s;
		}
		if (key=='S') s/=2;
	}
    public void render(float percent) {
        // Since we know how many objects our model has, go through each of them.
        for(Iterator iterator = model.objects.iterator(); iterator.hasNext(); ) {

            // Get the current object that we are displaying
            Object3DS object = (Object3DS)iterator.next();
            // LOG.fine("object.name: " + object.name);

            // Go through all of the faces (polygons) of the object and draw them
            int materialID = -1;
            PImage t=null;
            noStroke();
            textureMode(NORMALIZED);
            for (int j = 0; j < object.numberOfFaces; j++) {
                // check if we use the right material
                if (object.faces[j].materialID != materialID) {
                    // this face has a different material,
                    // thus we must set the correct material/texture
                    materialID = object.faces[j].materialID;
                    MaterialInfo3DS texture = model.materials.get(materialID);
                    t=(PImage)texture.texture;
                    if (t==null) {
                    	//System.out.println(materialID+" "+texture);
                    	String s=texture.file;
                    	if (s!=null) {
                    		t = loadImage("./data/"+s);
                    		texture.texture=t;
                    	} else {
                    		t=null;
                    		fill(texture.color[0],texture.color[1],texture.color[2]);
                    	}
                    }
                }
                // Go through each corner of the triangle and draw it.
                beginShape(PConstants.TRIANGLE_STRIP);
                if (t!=null) texture(t);
                for (int whichVertex = 0; whichVertex < 3; whichVertex++) {
                    // Get the index for each point of the face
                    int index = object.faces[j].vertexIndex[whichVertex];
                    // Give OpenGL the normal for this vertex.
//                    gl.glNormal3f(object.normals[index].x, object.normals[index].y, object.normals[index].z);
                    // set texture coordinates
                    // Pass in the current vertex of the object (Corner of current face)
                    if (t!=null)
                    	vertex(object.vertices[index].x, object.vertices[index].y, object.vertices[index].z,object.vertices[index].u, object.vertices[index].v);
                    else vertex(object.vertices[index].x, object.vertices[index].y, object.vertices[index].z);
                } // END for(int whichVertex = 0; whichVertex < 3; whichVertex++)
                endShape(); // End the drawing
            } // END for (int j = 0; j < object.numberOfFaces; j++)
        } // END for(int i = 0; i < g_3DModel.numOfObjects; i++)
    } // END drawModel
}
