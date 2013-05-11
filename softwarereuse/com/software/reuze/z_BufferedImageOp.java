package com.software.reuze;


public interface z_BufferedImageOp {
	 public z_BufferedImage	createCompatibleDestImage(z_BufferedImage src, z_ColorModel destCM); 
     //Creates a zeroed destination image with the correct size and number of bands.
	 z_BufferedImage	filter(z_BufferedImage src, z_BufferedImage dest);
     //Performs a single-input/single-output operation on a BufferedImage.
	 z_Rectangle2D.Float	getBounds2D(z_BufferedImage src); 
     //Returns the bounding box of the filtered destination image.
	 z_Point2D	getPoint2D(z_Point2D srcPt, z_Point2D dstPt); 
     //Returns the location of the corresponding destination point given a point in the source image.
	 z_RenderingHints	getRenderingHints();
}
