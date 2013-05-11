package reuze.awt;

import com.software.reuze.ib_a_FilterPoint;
import com.software.reuze.m_MathUtils;
import com.software.reuze.m_Noise;
import com.software.reuze.m_i_Function2D;
import com.software.reuze.vc_ColormapLinear;
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

public class ib_FilterWood extends ib_a_FilterPoint {

	private float scale = 200;
	private float stretch = 10.0f;
	private float angle = (float)Math.PI/2;
	public float rings = 0.5f;
	public float turbulence = 0.8f;
	public float fibres = 0.5f;
	public float gain = 0.8f;
	private float m00 = 1.0f;
	private float m01 = 0.0f;
	private float m10 = 0.0f;
	private float m11 = 1.0f;
	private vc_i_ColorMap colormap = new vc_ColormapLinear( 0xffe5c494, 0xff987b51 );
	private m_i_Function2D function = new m_Noise();

	public ib_FilterWood() {
	}

	public void setRings(float rings) {
		this.rings = rings;
	}

	public float getRings() {
		return rings;
	}

	public void setFunction(m_i_Function2D function) {
		this.function = function;
	}

	public m_i_Function2D getFunction() {
		return function;
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

	public void setFibres(float fibres) {
		this.fibres = fibres;
	}

	public float getFibres() {
		return fibres;
	}

	public void setgain(float gain) {
		this.gain = gain;
	}

	public float getGain() {
		return gain;
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
		float f = m_Noise.noise2(nx, ny);
		f += 0.1f*turbulence * m_Noise.noise2(nx*0.05f, ny*20);
		f = (f * 0.5f) + 0.5f;

		f *= rings*50;
		f = f-(int)f;
		f *= 1-m_MathUtils.smoothStep(gain, 1.0f, f);

		f += fibres*m_Noise.noise2(nx*scale, ny*50);

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

		return v;
	}

	public String toString() {
		return "Texture/Wood...";
	}
	
}
