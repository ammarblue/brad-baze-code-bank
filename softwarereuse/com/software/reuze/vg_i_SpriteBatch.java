package com.software.reuze;

public interface vg_i_SpriteBatch {
	public final static int GL_SRC_ALPHA=1;
	public final static int GL_ONE_MINUS_SRC_ALPHA=2;
	public final static int GL_ONE=3;
	/** Sets the blending function to be used when rendering sprites.
	 * 
	 * @param srcFunc the source function, e.g. GL11.GL_SRC_ALPHA
	 * @param dstFunc the destination function, e.g. GL11.GL_ONE_MINUS_SRC_ALPHA */
	public void setBlendFunction (int srcFunc, int dstFunc);
}
