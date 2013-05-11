package reuze.awt;


import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.software.reuze.gb_a_HeightMap;

/**
 * <code>ImageBasedHeightMap</code> is a height map created from the gray-scale
 * conversion of an image. The image used currently must have an equal height
 * and width, although future work could scale an incoming image to a specific
 * height and width.
 * 
 * @author Mike Kienenberger
 * @version $id$
 */
public class gb_HeightMapImage extends gb_a_HeightMap {
    
    
    protected BufferedImage colorImage;

    
    public void setImage(BufferedImage image) {
        this.colorImage = image;
    }
    public gb_HeightMapImage() {}
    /**
     * Creates a HeightMap from an Image. The image will be converted to
     * grayscale, and the grayscale values will be used to generate the height
     * map. White is highest point while black is lowest point.
     * 
     * Currently, the Image used must be square (width == height), but future
     * work could rescale the image.
     * 
     * @param colorImage
     *            Image to map to the height map.
     */
    public gb_HeightMapImage(BufferedImage colorImage) {
        this.colorImage = colorImage;
    }
    
    public gb_HeightMapImage(BufferedImage colorImage, float heightScale) {
    	this.colorImage = colorImage;
        this.heightScale = heightScale;
    }

    /**
     * Loads the image data from top left to bottom right
     */
    public boolean load() {
        return load(false, false);
    }

    /**
     * Get the grayscale value, or override in your own sub-classes
     */
    protected float calculateHeight(float red, float green, float blue) {
        return (float) (0.299 * red + 0.587 * green + 0.114 * blue);
    }
    public static int[] getRGB( BufferedImage image, int x, int y, int width, int height, int[] pixels ) {
		int type = image.getType();
		if ( type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB )
			return (int [])image.getRaster().getDataElements( x, y, width, height, pixels );
		return image.getRGB( x, y, width, height, pixels, 0, width );
    }
    public boolean load(boolean flipX, boolean flipY) {

        int imageWidth = colorImage.getWidth();
        int imageHeight = colorImage.getHeight();

        if (imageWidth != imageHeight)
                throw new RuntimeException("imageWidth: " + imageWidth
                        + " != imageHeight: " + imageHeight);

        size = imageWidth;

        int[] inPixels = getRGB( colorImage, 0, 0, imageWidth, imageHeight, null );
        heightData = new float[imageWidth*imageHeight];
        for (int i=0; i<inPixels.length; i++)
        	heightData[i] = getHeightAtPostion(inPixels[i], 0);
        return true;
    }
    
    protected float getHeightAtPostion(int color, int format) {
/*        switch (image.getFormat()){
            case RGBA8:
                buf.position( position * 4 );
                store.set(byte2float(buf.get()), byte2float(buf.get()), byte2float(buf.get()), byte2float(buf.get()));
                return calculateHeight(store.r, store.g, store.b);
            case ABGR8:*/
    	//int a1 = (color >> 24) & 0xff;
		int r1 = (color >> 16) & 0xff;
		int g1 = (color >> 8) & 0xff;
		int b1 = color & 0xff;
                return calculateHeight(r1, g1, b1);
/*            case RGB8:
                buf.position( position * 3 );
                store.set(byte2float(buf.get()), byte2float(buf.get()), byte2float(buf.get()), 1);
                return calculateHeight(store.r, store.g, store.b);
            case Luminance8:
                buf.position( position );
                return byte2float(buf.get())*255*heightScale;
            case Luminance16:
                ShortBuffer sbuf = buf.asShortBuffer();
                sbuf.position( position );
                return (sbuf.get() & 0xFFFF) / 65535f * 255f * heightScale;
            default:
                throw new UnsupportedOperationException("Image HeightMap");
        }*/
    }
    
    /*private float byte2float(byte b){
        return ((float)(b & 0xFF)) / 255f;
    }*/
    
    public static void main(String args[]) {
    	gb_HeightMapImage m= new gb_HeightMapImage();
    	try {
    		BufferedImage big=ImageIO.read(new File("data/badlogic.jpg"));
    		m.setImage(big);
    		m.load();
			m.save(System.out);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}