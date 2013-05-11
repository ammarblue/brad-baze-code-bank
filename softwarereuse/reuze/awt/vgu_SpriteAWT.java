package reuze.awt;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.software.reuze.m_MathUtils;
import com.software.reuze.vg_i_Sprite;
import com.software.reuze.vg_i_SpriteBatch;
import com.software.reuze.vg_i_Texture;


public class vgu_SpriteAWT implements vg_i_Sprite, vg_i_SpriteBatch { /*one instance per particle*/
	float x,y,width,height, scale;
	int color, shape;
	AlphaComposite blend;
	BufferedImage texture;
	float rotateRadians;
	public static Graphics2D g;
	AffineTransform at;
	public vgu_SpriteAWT() {
		
	}
    public vgu_SpriteAWT(vgu_SpriteAWT awt) {
    	x=awt.x;  y=awt.y; width=awt.width; height=awt.height; scale=awt.scale; texture=awt.texture;
    	color=awt.color;   shape=awt.shape; rotateRadians=awt.rotateRadians; blend=awt.blend;
    }
	public void setBlendFunction(int srcFunc, int dstFunc) { 
		System.out.println("setBlendFunction "+dstFunc);
		if (dstFunc==vg_i_SpriteBatch.GL_ONE) blend = AlphaComposite.SrcOver;
		else blend=null;
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
		System.out.println("copy");
		return new vgu_SpriteAWT(this);
	}

	public void flip(boolean flipX, boolean flipY) {
		// TODO 
		System.out.println("flip");
		
	}

	public void setOrigin(float originX, float originY) { 
		System.out.println("setOrigin");
		x=originX; y=originY;
	}

	public void translate(float xAmount, float yAmount) { 
		System.out.println("translate");
		x += xAmount; y += yAmount;
	}

	public void setColor(float r, float g, float b, float transparency) { 
		color = ((int)(transparency*255)<<24)+((int)(r*255)<<16)+((int)(g*255)<<8)+(int)(b*255);
	}

	public void setRotation(float degrees) { 
		//System.out.println("setRotation "+degrees);
		rotateRadians=degrees*m_MathUtils.degreesToRadians;
		if (rotateRadians!=0 && at==null) at=new AffineTransform();
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
		System.out.println("setTexture");
		texture=(BufferedImage)tex;
	}

	public void draw(vg_i_SpriteBatch spriteBatch) { 
		//System.out.println("draw "+this+" "+scale);
		if (color==0) return;
		g.setColor(new Color(color, true));
		float w=width>0?width*scale:scale;
		float h=height>0?height*scale:scale;
		if (texture != null) {
			if (rotateRadians!=0 && at!=null) {
				at.setToTranslation(x, y);
				at.rotate(rotateRadians);
				at.scale(w/texture.getWidth(), h/texture.getHeight());
				g.drawImage((Image)texture, at, null);
			} else g.drawImage((Image)texture,(int)(x-w/2), (int)(y-h/2), (int)(x+w/2), (int)(y+h/2), 0, 0, texture.getWidth(), texture.getHeight(), null);
		} else {
			g.fillRect((int)(x-w/2),(int)(y-h/2), (int)w,(int)h);
		}
	}
	@Override public String toString() {
		 return "id="+hashCode()+" x="+x+" y="+y+" w="+width+" h="+height+" c="+Integer.toHexString(color)+" t="+texture;
	}
	public void setProperty(String string) {
		System.out.println(string);
		if (string.indexOf("ellipse")>=0) shape=1;
	}
	public void create(String path) {
		try {
		    texture = ImageIO.read(new File(path));
		} catch (IOException e) {
		}
	}
}