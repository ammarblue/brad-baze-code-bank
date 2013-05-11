package com.software.reuze;

import java.awt.image.BufferedImage;

public class z_BufferedImage extends BufferedImage {
    z_ColorModel color;
	public z_BufferedImage(int width, int height, int imageType) {
		super(width, height, imageType);
		color=new z_ColorModel();
		color.color=super.getColorModel();
	}
	public z_BufferedImage(z_ColorModel dstCM, int width, int height) {
		super(dstCM.color, dstCM.color.createCompatibleWritableRaster(width, height), dstCM.color.isAlphaPremultiplied(), null);
		color=new z_ColorModel();
		color.color=super.getColorModel();
	}
	public z_ColorModel getColorModel(Object mustBeNull) {
		return color;
	}
}
