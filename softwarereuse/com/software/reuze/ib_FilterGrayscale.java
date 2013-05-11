package com.software.reuze;


/*
 * A filter which converts an image to gray-scale using the NTSC calculation.
 */
public class ib_FilterGrayscale extends ib_a_FilterPoint {

	public ib_FilterGrayscale() {
		canFilterIndexColorModel = true;
	}

	public int filterRGB(int x, int y, int rgb) {
		int a = rgb & 0xff000000;
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >> 8) & 0xff;
		int b = rgb & 0xff;
//		rgb = (r + g + b) / 3;	// simple average
		rgb = (r * 77 + g * 151 + b * 28) >> 8;	// NTSC luma
		return a | (rgb << 16) | (rgb << 8) | rgb;
	}

	public String toString() {
		return "Colors/Grayscale";
	}

}


