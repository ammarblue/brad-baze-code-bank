package com.software.reuze;
import java.util.Random;



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
 * A filter which simulates underwater caustics. This can be animated to get a bottom-of-the-swimming-pool effect.
 */
public class ib_FilterCaustics extends ib_a_FilterWholeImage {

	private float scale = 32;
	private float angle = 0.0f;
	public int brightness = 10;
	public float amount = 1.0f;
	public float turbulence = 1.0f;
	public float dispersion = 0.0f;
	public float time = 0.0f;
	private int samples = 2;
	private int bgColor = 0xff799fff;

	private float s, c;

	public ib_FilterCaustics() {
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getScale() {
		return scale;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	public int getBrightness() {
		return brightness;
	}

	public void setTurbulence(float turbulence) {
		this.turbulence = turbulence;
	}

	public float getTurbulence() {
		return turbulence;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	public float getAmount() {
		return amount;
	}
	
	public void setDispersion(float dispersion) {
		this.dispersion = dispersion;
	}
	
	public float getDispersion() {
		return dispersion;
	}
	
	public void setTime(float time) {
		this.time = time;
	}
	
	public float getTime() {
		return time;
	}
	
	public void setSamples(int samples) {
		this.samples = samples;
	}
	
	public int getSamples() {
		return samples;
	}
	
	public void setBgColor(int c) {
		bgColor = c;
	}

	public int getBgColor() {
		return bgColor;
	}

	protected int[] filterPixels( int width, int height, int[] inPixels, ga_Rectangle transformedSpace ) {
		Random random = new Random(0);

		s = (float)Math.sin(0.1);
		c = (float)Math.cos(0.1);

		int srcWidth = (int)originalSpace.width;  //RPC
		int srcHeight = (int)originalSpace.height;
		int outWidth = (int)transformedSpace.width;
		int outHeight = (int)transformedSpace.height;
		int index = 0;
		int[] pixels = new int[outWidth * outHeight];

		for (int y = 0; y < outHeight; y++) {
			for (int x = 0; x < outWidth; x++) {
				pixels[index++] = bgColor;
			}
		}
		
		int v = brightness/samples;
		if (v == 0)
			v = 1;

		float rs = 1.0f/scale;
		float d = 0.95f;
		index = 0;
		for (int y = 0; y < outHeight; y++) {
			for (int x = 0; x < outWidth; x++) {
				for (int s = 0; s < samples; s++) {
					float sx = x+random.nextFloat();
					float sy = y+random.nextFloat();
					float nx = sx*rs;
					float ny = sy*rs;
					float xDisplacement, yDisplacement;
					float focus = 0.1f+amount;
					xDisplacement = evaluate(nx-d, ny) - evaluate(nx+d, ny);
					yDisplacement = evaluate(nx, ny+d) - evaluate(nx, ny-d);

					if (dispersion > 0) {
						for (int c = 0; c < 3; c++) {
							float ca = (1+c*dispersion);
							float srcX = sx + scale*focus * xDisplacement*ca;
							float srcY = sy + scale*focus * yDisplacement*ca;

							if (srcX < 0 || srcX >= outWidth-1 || srcY < 0 || srcY >= outHeight-1) {
							} else {
								int i = ((int)srcY)*outWidth+(int)srcX;
								int rgb = pixels[i];
								int r = (rgb >> 16) & 0xff;
								int g = (rgb >> 8) & 0xff;
								int b = rgb & 0xff;
								if (c == 2)
									r += v;
								else if (c == 1)
									g += v;
								else
									b += v;
								if (r > 255)
									r = 255;
								if (g > 255)
									g = 255;
								if (b > 255)
									b = 255;
								pixels[i] = 0xff000000 | (r << 16) | (g << 8) | b;
							}
						}
					} else {
						float srcX = sx + scale*focus * xDisplacement;
						float srcY = sy + scale*focus * yDisplacement;

						if (srcX < 0 || srcX >= outWidth-1 || srcY < 0 || srcY >= outHeight-1) {
						} else {
							int i = ((int)srcY)*outWidth+(int)srcX;
							int rgb = pixels[i];
							int r = (rgb >> 16) & 0xff;
							int g = (rgb >> 8) & 0xff;
							int b = rgb & 0xff;
							r += v;
							g += v;
							b += v;
							if (r > 255)
								r = 255;
							if (g > 255)
								g = 255;
							if (b > 255)
								b = 255;
							pixels[i] = 0xff000000 | (r << 16) | (g << 8) | b;
						}
					}
				}
			}
		}
		return pixels;
	}

	private static int add(int rgb, float brightness) {
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >> 8) & 0xff;
		int b = rgb & 0xff;
		r += brightness;
		g += brightness;
		b += brightness;
		if (r > 255)
			r = 255;
		if (g > 255)
			g = 255;
		if (b > 255)
			b = 255;
		return 0xff000000 | (r << 16) | (g << 8) | b;
	}
	
	private static int add(int rgb, float brightness, int c) {
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >> 8) & 0xff;
		int b = rgb & 0xff;
		if (c == 2)
			r += brightness;
		else if (c == 1)
			g += brightness;
		else
			b += brightness;
		if (r > 255)
			r = 255;
		if (g > 255)
			g = 255;
		if (b > 255)
			b = 255;
		return 0xff000000 | (r << 16) | (g << 8) | b;
	}
	
	public static float turbulence2(float x, float y, float time, float octaves) {
		float value = 0.0f;
		float remainder;
		float lacunarity = 2.0f;
		float f = 1.0f;
		int i;
		
		// to prevent "cascading" effects
		x += 371;
		y += 529;
		
		for (i = 0; i < (int)octaves; i++) {
			value += m_Noise.noise3(x, y, time) / f;
			x *= lacunarity;
			y *= lacunarity;
			f *= 2;
		}

		remainder = octaves - (int)octaves;
		if (remainder != 0)
			value += remainder * m_Noise.noise3(x, y, time) / f;

		return value;
	}

	protected float evaluate(float x, float y) {
		float xt = s*x + c*time;
		float tt = c*x - c*time;
		float f = turbulence == 0.0 ? m_Noise.noise3(xt, y, tt) : turbulence2(xt, y, tt, turbulence);
		return f;
	}
	
	public String toString() {
		return "Texture/Caustics...";
	}
	
}
