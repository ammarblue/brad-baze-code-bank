package com.software.reuze;

import java.awt.image.ColorModel;

public class z_ColorModel {
    public ColorModel color;
	public z_ColorModel(Object arg0) {
		color=(ColorModel)arg0;
	}
	public z_ColorModel() {
	}
	public z_WritableRaster createCompatibleWritableRaster(int width, int height) {
		z_WritableRaster z=new z_WritableRaster();
		z.raster=color.createCompatibleWritableRaster(width, height);
		return z;
	}

}
