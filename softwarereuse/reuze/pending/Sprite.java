package reuze.pending;

import com.software.reuze.ga_Rectangle;

public class Sprite
{
    public float xOld, yOld,
                 x, y;       //position on the screen, top left?
    public float xv, yv;     //velocity
    public float rotate,     //in radians, about center of bounding rectangle
                 scale;      //both values, positive only
    
    public SpriteImage image;
    public boolean visible = true;
    public int layer;   //drawing layer, 0 is drawn first
    public int counter;
    public Sprite(SpriteImage si, int layer, int x, int y, float xVelocity, float yVelocity) {
    	image=si; this.x=x; this.y=y; xv=xVelocity; yv=yVelocity; this.layer=layer;
    }
    public void render(Object graphics, float alpha)
    {
        if (!visible) return;
        
        int xPixel = (int)(xOld+(x-xOld)*alpha);
        int yPixel = (int)(yOld+(y-yOld)*alpha);
        if (rotate+scale==0) image.render(graphics, counter, xPixel, yPixel);
        else image.render(graphics, counter, xPixel, yPixel, rotate, scale);
    }

    public final void tick()
    {
        xOld = x;
        yOld = y;
        x+=xv;
        y+=yv;
        counter=(counter+1)%image.count;
    }
    //TODO may be to adjust if vx or vy is larger than a reasonable fraction of width or height
	public void getFront(ga_Rectangle temp/*out*/) { //manhattan motion only
		temp.position.x=x; temp.position.y=y; temp.width=image.width; temp.height=image.height;
	}

}