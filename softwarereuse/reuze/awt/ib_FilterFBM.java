package reuze.awt;
import java.util.Random;

import com.software.reuze.ga_CellFunction;
import com.software.reuze.ib_a_FilterPoint;
import com.software.reuze.m_FBM;
import com.software.reuze.m_Function2DNoiseVL;
import com.software.reuze.m_Function2DRidgedFBM;
import com.software.reuze.m_MathUtils;
import com.software.reuze.m_Noise;
import com.software.reuze.m_NoiseConvolutionSparse;
import com.software.reuze.m_i_Function2D;
import com.software.reuze.vc_ColorOps;
import com.software.reuze.vc_Map256Gradient;
import com.software.reuze.vc_i_ColorMap;
import com.software.reuze.z_BufferedImage;


/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

/**
 * A filter which produces textures from fractal Brownian motion.
 */
public class ib_FilterFBM extends ib_a_FilterPoint implements Cloneable {

	public final static int NOISE = 0;
	public final static int RIDGED = 1;
	public final static int VLNOISE = 2;
	public final static int SCNOISE = 3;
	public final static int CELLULAR = 4;

	private float scale = 32;
	private float stretch = 1.0f;
	private float angle = 0.0f;
	private float amount = 1.0f;
	private float H = 1.0f;
	private float octaves = 4.0f;
	private float lacunarity = 2.0f;
	private float gain = 0.5f;
	private float bias = 0.5f;
	private int operation;
	private float m00 = 1.0f;
	private float m01 = 0.0f;
	private float m10 = 0.0f;
	private float m11 = 1.0f;
	private float min;
	private float max;
	private vc_i_ColorMap colormap = new vc_Map256Gradient();
	private boolean ridged;
	private m_FBM fBm;
	protected Random random = new Random();
	private int basisType = NOISE;
	private m_i_Function2D basis;

	public ib_FilterFBM() {
		setBasisType(NOISE);
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public float getAmount() {
		return amount;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}
	
	public int getOperation() {
		return operation;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getScale() {
		return scale;
	}

	public void setStretch(float stretch) {
		this.stretch = stretch;
	}

	public float getStretch() {
		return stretch;
	}

	public void setAngle(float angle) {
		this.angle = angle;
		float cos = (float)Math.cos(this.angle);
		float sin = (float)Math.sin(this.angle);
		m00 = cos;
		m01 = sin;
		m10 = -sin;
		m11 = cos;
	}

	public float getAngle() {
		return angle;
	}

	public void setOctaves(float octaves) {
		this.octaves = octaves;
	}

	public float getOctaves() {
		return octaves;
	}

	public void setH(float H) {
		this.H = H;
	}

	public float getH() {
		return H;
	}

	public void setLacunarity(float lacunarity) {
		this.lacunarity = lacunarity;
	}

	public float getLacunarity() {
		return lacunarity;
	}

	public void setGain(float gain) {
		this.gain = gain;
	}

	public float getGain() {
		return gain;
	}

	public void setBias(float bias) {
		this.bias = bias;
	}

	public float getBias() {
		return bias;
	}

	public void setColormap(vc_i_ColorMap colormap) {
		this.colormap = colormap;
	}
	
	public vc_i_ColorMap getColormap() {
		return colormap;
	}
	
	public void setBasisType(int basisType) {
		this.basisType = basisType;
		switch (basisType) {
		default:
		case NOISE:
			basis = new m_Noise();
			break;
		case RIDGED:
			basis = new m_Function2DRidgedFBM();
			break;
		case VLNOISE:
			basis = new m_Function2DNoiseVL();
			break;
		case SCNOISE:
			basis = new m_NoiseConvolutionSparse();
			break;
		case CELLULAR:
			basis = new ga_CellFunction();
			break;
		}
	}

	public int getBasisType() {
		return basisType;
	}

	public void setBasis(m_i_Function2D basis) {
		this.basis = basis;
	}

	public m_i_Function2D getBasis() {
		return basis;
	}

	protected m_FBM makeFBM(float H, float lacunarity, float octaves) {
		m_FBM fbm = new m_FBM(H, lacunarity, octaves, basis);
		float[] minmax = m_Noise.findRange(fbm, null);
		min = minmax[0];
		max = minmax[1];
		return fbm;
	}
	
	public z_BufferedImage filter( z_BufferedImage src, z_BufferedImage dst ) {
		fBm = makeFBM(H, lacunarity, octaves);
		return super.filter( src, dst );
	}

	public int filterRGB(int x, int y, int rgb) {
		float nx = m00*x + m01*y;
		float ny = m10*x + m11*y;
		nx /= scale;
		ny /= scale * stretch;
		float f = fBm.evaluate(nx, ny);
		// Normalize to 0..1
		f = (f-min)/(max-min);
		f = m_MathUtils.gain(f, gain);
		f = m_MathUtils.bias(f, bias);
		f *= amount;
		int a = rgb & 0xff000000;
		int v;
		if (colormap != null)
			v = colormap.getColor(f);
		else {
			v = m_MathUtils.clampToByte((int)(f*255));
			int r = v << 16;
			int g = v << 8;
			int b = v;
			v = a|r|g|b;
		}
		if (operation != vc_ColorOps.REPLACE)
			v = vc_ColorOps.combinePixels(rgb, v, operation);
		return v;
	}

	public String toString() {
		return "Texture/Fractal Brownian Motion...";
	}
	
}
