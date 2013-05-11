package reuze.demo;
import com.software.reuze.dg_ParticleEmitter;
import com.software.reuze.f_Handle;
import com.software.reuze.ga_Vector2;
import com.software.reuze.ied_ParticleEffect;
import com.software.reuze.z_SpritePS;
import com.software.reuze.z_ToxiclibsSupport;

import processing.core.PApplet;

public class demoToxiEffectParticles  extends PApplet {

	ied_ParticleEffect pe;
	dg_ParticleEmitter em;
	z_SpritePS awt;
	z_ToxiclibsSupport g;
	boolean stop=true;
	public void setup() {
	  size(640, 720);
	  pe=new ied_ParticleEffect();
	  g = new z_ToxiclibsSupport(this);
	  awt = new z_SpritePS();
	  z_SpritePS.g = g;
	  pe.sprite=awt;
	  pe.load(new f_Handle("/Users/bobcook/Desktop/foo.p"), new f_Handle("./"));
	  pe.setPosition(width/2, height/2);
	  stop=false;
	}

	public void draw() {
		background(0);
		if (stop || pe==null || awt==null) return;
		pe.draw(awt);
		pe.update(0.04f);
	}

	public void keyPressed() {

	}

	// Add a vertex!
	public void mousePressed() {
	  ga_Vector2 mouse = new ga_Vector2(mouseX, mouseY);
	}

}