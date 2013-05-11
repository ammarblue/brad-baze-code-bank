package reuze.awt;

import com.software.reuze.ib_a_FilterPoint;
import com.software.reuze.m_MathUtils;
import com.software.reuze.m_Noise;
import com.software.reuze.m_i_Function2D;
import com.software.reuze.vc_ColorOps;
import com.software.reuze.vc_Map256Gradient;
import com.software.reuze.vc_i_ColorMap;

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

public class ib_FilterTexture extends ib_a_FilterPoint implements java.io.Serializable {

	static final long serialVersionUID = -7538331862272404352L;
	
	private float scale = 32;
	private float stretch = 1.0f;
	private float angle = 0.0f;
	public float amount = 1.0f;
	public float turbulence = 1.0f;
	public float gain = 0.5f;
	public float bias = 0.5f;
	public int operation;
	private float m00 = 1.0f;
	private float m01 = 0.0f;
	private float m10 = 0.0f;
	private float m11 = 1.0f;
	private vc_i_ColorMap colormap = new vc_Map256Gradient();
	private m_i_Function2D function = new m_Noise();

	public ib_FilterTexture() {
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public float getAmount() {
		return amount;
	}

	public void setFunction(m_i_Function2D function) {
		this.function = function;
	}

	public m_i_Function2D getFunction() {
		return function;
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
		float cos = (float)Math.cos(angle);
		float sin = (float)Math.sin(angle);
		m00 = cos;
		m01 = sin;
		m10 = -sin;
		m11 = cos;
	}

	public float getAngle() {
		return angle;
	}

	public void setTurbulence(float turbulence) {
		this.turbulence = turbulence;
	}

	public float getTurbulence() {
		return turbulence;
	}

	public void setColormap(vc_i_ColorMap colormap) {
		this.colormap = colormap;
	}
	
	public vc_i_ColorMap getColormap() {
		return colormap;
	}
	
	public int filterRGB(int x, int y, int rgb) {
		float nx = m00*x + m01*y;
		float ny = m10*x + m11*y;
		nx /= scale;
		ny /= scale * stretch;
		float f = turbulence == 1.0 ? m_Noise.noise2(nx, ny) : m_Noise.turbulence2(nx, ny, turbulence);
		f = (f * 0.5f) + 0.5f;
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
		return "Texture/Noise...";
	}
	
}
