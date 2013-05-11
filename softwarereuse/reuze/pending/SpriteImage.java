package reuze.pending;

public class SpriteImage {
	//SPRITES MUST be placed in bottom right unless all are wPic wide, sprites assumed to face right
	//instances of this class are designed to be shared by all Sprites with the same image unless they have different orientation
    public int xPic, yPic;   //top-left offset (in pixels) on sprite sheet to a horizontal row of images
    public int wPic;         //width and height of each image block in the row
    public int hPic;
    public int xPic0, yPic0;  //may be smaller than block, so offsets mark top-left of every sub-image
    public boolean xFlipPic, yFlipPic;
    public Object sheet;      //image object for sprite sheet
    public int width, height; //of every sub-image
    public int count;         //number of images
    public Object[] cache;    //high-level renderers can use this array as a cache
    public SpriteManagerI draw;
    public SpriteImage(Object sheet, int xOffset, int yOffset, int width, int height, int xSub, int ySub, int count, SpriteManagerI renderer) {
    	this.sheet=sheet;  xPic=xOffset;  yPic=yOffset; wPic=width; hPic=height; xPic0=xSub; yPic0=ySub;
    	this.width=wPic-xSub;  this.height=hPic-ySub; this.count=count;  draw=renderer;
    }
    public void setCache(int index, Object o) {
    	if (cache==null) cache=new Object[count];
    	cache[index]=o;
    }
	public void render(Object graphics, int index, int screenX, int screenY) {
		if (cache!=null) draw.render(graphics, screenX, screenY, cache[index]);
		else
			draw.render(graphics, screenX, screenY, xPic+wPic*index+xPic0, yPic+yPic0, xFlipPic?-width:width, yFlipPic?-height:height, sheet);
	}
	public void render(Object graphics, int index, int screenX, int screenY,
			float rotate, float scale) {
		if (cache!=null) draw.render(graphics, screenX, screenY, rotate, scale, cache[index]);
		else
			draw.render(graphics, screenX, screenY, xPic+wPic*index+xPic0, yPic+yPic0, xFlipPic?-width:width, yFlipPic?-height:height, rotate, scale, sheet);
	}
}
