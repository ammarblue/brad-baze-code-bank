package reuze.pending;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.software.reuze.d_ArrayListFloat;
import com.software.reuze.d_ArrayListInt;
import com.software.reuze.ff_OBJModel;
import com.software.reuze.ff_OBJModelGroup;
import com.software.reuze.ff_OBJModelLoader;

import processing.core.PApplet;
import processing.core.PConstants;

public class demoOBJ extends PApplet {
	ff_OBJModel m=null;
	ff_OBJModelGroup g;
	d_ArrayListInt f;
	d_ArrayListFloat vertices;
	d_ArrayListInt indices;
	float x,y,z,s=1;
	public void setup() {
		size(400,400,P3D);
		try {
			m=ff_OBJModelLoader.load(new Scanner(new File("./data/cube.obj")));
			System.out.println(m);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		g=m.getTopGroup();
		f=g.faces;             //index,count, materialIndex
		System.out.println(f);
		vertices=g.vtn;
		indices=g.vtnIndices;
		System.out.println(indices); //vertex index, normal index, texture index
		System.out.println(vertices);
	}
	public void draw() {
		background(100,100,100);
		translate(200,200);
		scale(s);
		rotateX(x); rotateY(y); rotateZ(z);
		
		for (int fi=0; fi<f.size(); fi+=3) {
			int v=indices.get(fi);
			beginShape(PConstants.TRIANGLE_STRIP);
			for (int j=0; j<9; j+=3) {
				float a=vertices.get(v+j);
				float b=vertices.get(v+j+1);
				float c=vertices.get(v+j+2);
				vertex(a,b,c);
			}
			endShape();
		}
		
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
}
