package com.software.reuze;

import java.awt.Point;
import java.awt.image.Raster;
import java.awt.image.SampleModel;


public class z_Raster extends Raster {

	protected z_Raster(Object sampleModel, z_Point2D origin) {
		super((SampleModel)sampleModel, new Point((int)origin.x,(int)origin.y));
	}

}
