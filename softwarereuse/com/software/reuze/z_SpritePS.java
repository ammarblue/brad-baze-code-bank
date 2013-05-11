package com.software.reuze;
import com.software.reuze.z_ToxiclibsSupport;

import processing.core.PImage;

public class z_SpritePS implements vg_i_Sprite, vg_i_SpriteBatch {
	float x,y,width,height, scale;
	int color, shape, blend;
	PImage texture;
	float rotateRadians;
	boolean flipX, flipY;
	public static z_ToxiclibsSupport g;
	public z_SpritePS() {
		
	}
    public z_SpritePS(z_SpritePS awt) {
    	x=awt.x;  y=awt.y; width=awt.width; height=awt.height; scale=awt.scale; texture=awt.texture;
    	color=awt.color;   shape=awt.shape; rotateRadians=awt.rotateRadians; blend=awt.blend;
    }
	public void setBlendFunction(int srcFunc, int dstFunc) { 
		//System.out.println("setBlendFunction");
		blend=dstFunc;
	}

	public float getWidth() { 
		//System.out.println("getWidth");
		return width;
	}

	public float getHeight() { 
		//System.out.println("getHeight");
		return height;
	}

	public float getOriginX() { 
		//System.out.println("getOriginX");
		return x;
	}

	public float getOriginY() { 
		//System.out.println("getOriginY");
		return y;
	}

	public vg_i_Sprite copy() { 
		//System.out.println("copy");
		return new z_SpritePS(this);
	}

	public void flip(boolean flipX, boolean flipY) {
		this.flipX=flipX; this.flipY=flipY;
	}

	public void setOrigin(float originX, float originY) { 
		//System.out.println("setOrigin");
		x=originX; y=originY;
	}

	public void translate(float xAmount, float yAmount) { 
		//System.out.println("translate");
		x += xAmount; y += yAmount;
	}

	public void setColor(float r, float g, float b, float transparency) { 
		color = ((int)(transparency*255)<<24)+((int)(r*255)<<16)+((int)(g*255)<<8)+(int)(b*255);
	}

	public void setRotation(float degrees) { 
		//System.out.println("setRotation "+degrees);
		rotateRadians=degrees*m_MathUtils.degreesToRadians;
	}

	public void setScale(float scale) { 
		//System.out.println("setScale");
		this.scale=scale;
	}

	public void setBounds(float x, float y, float spriteWidth,
			float spriteHeight) { 
		//System.out.println("setBounds");
		this.x=x; this.y=y; width=spriteWidth; height=spriteHeight;
	}

	public vg_i_Texture getTexture() {
		// TODO 
		System.out.println("getTexture");
		return null;
	}

	public void setTexture(vg_i_Texture tex) {
		// TODO 
		System.out.println("setTexture");
		
	}

	public void draw(vg_i_SpriteBatch spriteBatch) { 
		//System.out.println("draw "+this+" "+scale);
		if (color==0) return;
		g.strokeFill(false, 0, color);
		float w=width>0?width*scale:scale;
		float h=height>0?height*scale:scale;
		if (texture != null) {
			int wt=texture.getImage().getWidth(null);
			int wt2=0;
			int wh=texture.getImage().getHeight(null);
			int wh2=0;
			if (flipX) {wt2=wt; wt=0;}
			if (flipY) {wh2=wh; wh=0;}
			g.gfx.tint(color);
			g.gfx.pushMatrix();
			g.gfx.translate(x, y);
			g.gfx.rotate(rotateRadians);
			g.gfx.noStroke();
			g.gfx.beginShape();
			g.gfx.texture(texture);
			g.gfx.vertex(-w/2, -h/2,wt2,wh2);
			g.gfx.vertex(w/2, -h/2,wt,wh2);
			g.gfx.vertex(w/2, h/2,wt,wh);
			g.gfx.vertex(-w/2, h/2,wt2,wh);
			g.gfx.endShape();
			g.gfx.popMatrix();
		} else {
			if (shape==1) g.gfx.ellipse(x-w/2, y-h/2,w,h);
			else g.rect(new ga_Rectangle(new ga_Vector2(x-w/2,y-h/2), w,h));
		}
	}
	@Override public String toString() {
		 return "id="+hashCode()+" x="+x+" y="+y+" w="+width+" h="+height+" c="+Integer.toHexString(color)+" t="+texture;
	}
	public void setProperty(String string) {
		//System.out.println(string);
		if (string.indexOf("ellipse")>=0) shape=1;
	}
	public void create(String path) {
		texture = g.loadImage(path);
	}
}
