package reuze.awt;

import com.software.reuze.ib_a_FilterPoint;
import com.software.reuze.m_MathUtils;

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
 * A filter which allows the red, green and blue channels of an image to be mixed into each other.
 */
public class ib_FilterChannelMix extends ib_a_FilterPoint {
	
	public int blueGreen, redBlue, greenRed;
	public int intoR, intoG, intoB;
	
	public ib_FilterChannelMix() {
		canFilterIndexColorModel = true;
	}

	public void setBlueGreen(int blueGreen) {
		this.blueGreen = blueGreen;
	}

	public int getBlueGreen() {
		return blueGreen;
	}

	public void setRedBlue(int redBlue) {
		this.redBlue = redBlue;
	}

	public int getRedBlue() {
		return redBlue;
	}

	public void setGreenRed(int greenRed) {
		this.greenRed = greenRed;
	}

	public int getGreenRed() {
		return greenRed;
	}

	public void setIntoR(int intoR) {
		this.intoR = intoR;
	}

	public int getIntoR() {
		return intoR;
	}

	public void setIntoG(int intoG) {
		this.intoG = intoG;
	}

	public int getIntoG() {
		return intoG;
	}

	public void setIntoB(int intoB) {
		this.intoB = intoB;
	}

	public int getIntoB() {
		return intoB;
	}

	public int filterRGB(int x, int y, int rgb) {
		int a = rgb & 0xff000000;
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >> 8) & 0xff;
		int b = rgb & 0xff;
		int nr = m_MathUtils.clampToByte((intoR * (blueGreen*g+(255-blueGreen)*b)/255 + (255-intoR)*r)/255);
		int ng = m_MathUtils.clampToByte((intoG * (redBlue*b+(255-redBlue)*r)/255 + (255-intoG)*g)/255);
		int nb = m_MathUtils.clampToByte((intoB * (greenRed*r+(255-greenRed)*g)/255 + (255-intoB)*b)/255);
		return a | (nr << 16) | (ng << 8) | nb;
	}

	public String toString() {
		return "Colors/Mix Channels...";
	}
}

