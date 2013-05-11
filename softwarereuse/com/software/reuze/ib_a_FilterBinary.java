package com.software.reuze;


public abstract class ib_a_FilterBinary extends ib_a_FilterWholeImage {

	protected int newColor = 0xff000000;
	protected vc_i_PredicateIsBlack blackFunction = new vc_PredicateIsBlack();
	protected int iterations = 1;
	protected vc_i_ColorMap colormap;

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public int getIterations() {
		return iterations;
	}

	public void setColormap(vc_i_ColorMap colormap) {
		this.colormap = colormap;
	}

	public vc_i_ColorMap getColormap() {
		return colormap;
	}

	public void setNewColor(int newColor) {
		this.newColor = newColor;
	}

	public int getNewColor() {
		return newColor;
	}

	public void setBlackFunction(vc_i_PredicateIsBlack blackFunction) {
		this.blackFunction = blackFunction;
	}

	public vc_i_PredicateIsBlack getBlackFunction() {
		return blackFunction;
	}

}

