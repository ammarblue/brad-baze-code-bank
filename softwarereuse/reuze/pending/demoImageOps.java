package reuze.pending;

import com.software.reuze.ib_FilterCaustics;
import com.software.reuze.ib_FilterGrayscale;
import com.software.reuze.z_BufferedImage;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import reuze.awt.ib_EdgeDetectionCanny;
import reuze.awt.ib_EdgeDetectionSobel;
import reuze.awt.ib_FilterAlphaInvert;
import reuze.awt.ib_FilterBlock;
import reuze.awt.ib_FilterBoxBlur;
import reuze.awt.ib_FilterCellular;
import reuze.awt.ib_FilterChannelMix;
import reuze.awt.ib_FilterCheckerboard;
import reuze.awt.ib_FilterCircle;
import reuze.awt.ib_FilterColorHalftone;
import reuze.awt.ib_FilterCompound;
import reuze.awt.ib_FilterContour;
import reuze.awt.ib_FilterContrast;
import reuze.awt.ib_FilterCrystallize;
import reuze.awt.ib_FilterCurves;
import reuze.awt.ib_FilterDeinterlace;
import reuze.awt.ib_FilterDespeckle;
import reuze.awt.ib_FilterDiffuse;
import reuze.awt.ib_FilterDiffusion;
import reuze.awt.ib_FilterDilate;
import reuze.awt.ib_FilterDisplace;
import reuze.awt.ib_FilterDissolve;
import reuze.awt.ib_FilterDither;
import reuze.awt.ib_FilterEdge;
import reuze.awt.ib_FilterEmboss;
import reuze.awt.ib_FilterEqualize;
import reuze.awt.ib_FilterErode;
import reuze.awt.ib_FilterExposure;
import reuze.awt.ib_FilterFBM;
import reuze.awt.ib_FilterFade;
import reuze.awt.ib_FilterFieldWarp;
import reuze.awt.ib_FilterFill;
import reuze.awt.ib_FilterFlare;
import reuze.awt.ib_FilterFlush3D;
import reuze.awt.ib_FilterFourColor;
import reuze.awt.ib_FilterGain;
import reuze.awt.ib_FilterGamma;
import reuze.awt.ib_FilterGradient;
import reuze.awt.ib_FilterGradientWipe;
import reuze.awt.ib_FilterGray;
import reuze.awt.ib_FilterHSBAdjust;
import reuze.awt.ib_FilterHalftone;
import reuze.awt.ib_FilterInterpolate;
import reuze.awt.ib_FilterInvert;
import reuze.awt.ib_FilterIterated;
import reuze.awt.ib_FilterJavaLnF;
import reuze.awt.ib_FilterKaleidoscope;
import reuze.awt.ib_FilterLensBlur;
import reuze.awt.ib_FilterLevels;
import reuze.awt.ib_FilterLife;
import reuze.awt.ib_FilterLookup;
import reuze.awt.ib_FilterMap;
import reuze.awt.ib_FilterMapColors;
import reuze.awt.ib_FilterMarble;
import reuze.awt.ib_FilterMarbleTexture;
import reuze.awt.ib_FilterMaskPoints;
import reuze.awt.ib_FilterMaximum;
import reuze.awt.ib_FilterMedian;
import reuze.awt.ib_FilterMinimum;
import reuze.awt.ib_FilterNoise;
import reuze.awt.ib_FilterNoiseReduction;
import reuze.awt.ib_FilterOffset;
import reuze.awt.ib_FilterOil;
import reuze.awt.ib_FilterOpacity;
import reuze.awt.ib_FilterOutline;
import reuze.awt.ib_FilterPerspective;
import reuze.awt.ib_FilterPinch;
import reuze.awt.ib_FilterPlasma;
import reuze.awt.ib_FilterPointillize;
import reuze.awt.ib_FilterPolar;
import reuze.awt.ib_FilterPosterize;
import reuze.awt.ib_FilterPremultiply;
import reuze.awt.ib_FilterQuantize;
import reuze.awt.ib_FilterQuilt;
import reuze.awt.ib_FilterRGBAdjust;
import reuze.awt.ib_FilterRescale;
import reuze.awt.ib_FilterRipple;
import reuze.awt.ib_FilterRotate;
import reuze.awt.ib_FilterShape;
import reuze.awt.ib_FilterShear;
import reuze.awt.ib_FilterSkeleton;
import reuze.awt.ib_FilterSmear;
import reuze.awt.ib_FilterSolarize;
import reuze.awt.ib_FilterSparkle;
import reuze.awt.ib_FilterSphere;
import reuze.awt.ib_FilterSwim;
import reuze.awt.ib_FilterSwizzle;
import reuze.awt.ib_FilterTexture;
import reuze.awt.ib_FilterThreshold;
import reuze.awt.ib_FilterTritone;
import reuze.awt.ib_FilterTwirl;
import reuze.awt.ib_FilterUnpremultiply;
import reuze.awt.ib_FilterVariableBlur;
import reuze.awt.ib_FilterWarp;
import reuze.awt.ib_FilterWater;
import reuze.awt.ib_FilterWeave;
import reuze.awt.ib_FilterWood;

public class demoImageOps extends PApplet {
	PImage img, img2;
	z_BufferedImage buf;
	public void setup() {
		size(500,500);
		img=loadImage("mearth.jpg");
		buf=new z_BufferedImage(img.width,img.height,z_BufferedImage.TYPE_INT_ARGB);
		int[] rgbArray=img.pixels;
		buf.setRGB(0, 0, img.width, img.height, rgbArray, 0, img.width);
		ib_FilterWeave fil=new ib_FilterWeave();
		fil.filter(buf,buf);
		img2=createImage(buf, img.width, img.height);
	}
	public void draw() {
		image(img2,40,40);
	}
	public PImage createImage(z_BufferedImage frame, int width, int height) {
		PImage img=new PImage(frame.getWidth(),frame.getHeight(),PConstants.ARGB);
		frame.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);
		img.updatePixels();
		img.resize(width,height);
		return img;
	}
}