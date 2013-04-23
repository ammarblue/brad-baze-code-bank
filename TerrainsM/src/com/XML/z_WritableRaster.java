package com.XML;


import java.awt.Point;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;


public class z_WritableRaster {
    WritableRaster raster;
	protected z_WritableRaster(Object sampleModel, z_Point2D origin) {
		raster=WritableRaster.createWritableRaster((SampleModel)sampleModel, new Point((int)origin.x,(int)origin.y));
	}
	public z_WritableRaster() {
	}

}
