package com.software.reuze;


/*
** Copyright 2005 Huxtable.com. All rights reserved.
*/

/**
 * A convenience class which implements those methods of BufferedImageOp which are rarely changed.
 */
public abstract class ib_a_Ops implements z_BufferedImageOp {

    public z_BufferedImage createCompatibleDestImage(z_BufferedImage src, z_ColorModel dstCM) {
        if ( dstCM == null )
            dstCM = src.getColorModel(null);
        return new z_BufferedImage(dstCM,src.getWidth(),src.getHeight());
    }
    
    public z_Rectangle2D getBounds2D( z_BufferedImage src ) {
        return new z_Rectangle2D(0, 0, src.getWidth(), src.getHeight());
    }
    
    public z_Point2D getPoint2D( z_Point2D srcPt, z_Point2D dstPt ) {
        if ( dstPt == null )
            dstPt = new z_Point2D();
        dstPt.setLocation( srcPt.getX(), srcPt.getY() );
        return dstPt;
    }

    public z_RenderingHints getRenderingHints() {
        return null;
    }

	/**
	 * A convenience method for getting ARGB pixels from an image. This tries to avoid the performance
	 * penalty of z_BufferedImage.getRGB un-managing the image.
	 */
	public static int[] getRGB( z_BufferedImage image, int x, int y, int width, int height, int[] pixels ) {
		/*int type = image.getType();
		if ( type == z_BufferedImage.TYPE_INT_ARGB || type == z_BufferedImage.TYPE_INT_RGB )
			return (int [])image.getRaster().getDataElements( x, y, width, height, pixels );*/
		return image.getRGB( x, y, width, height, pixels, 0, width );
    }

	/**
	 * A convenience method for setting ARGB pixels in an image. This tries to avoid the performance
	 * penalty of z_BufferedImage.setRGB unmanaging the image.
	 */
	public static void setRGB( z_BufferedImage image, int x, int y, int width, int height, int[] pixels ) {
		/*int type = image.getType();
		if ( type == z_BufferedImage.TYPE_INT_ARGB || type == z_BufferedImage.TYPE_INT_RGB )
			image.getRaster().setDataElements( x, y, width, height, pixels );
		else*/
			image.setRGB( x, y, width, height, pixels, 0, width );
    }
}
