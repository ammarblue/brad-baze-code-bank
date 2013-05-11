package com.software.reuze;

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
 * A colormap with the colors of the spectrum.
 */
public class vc_ColorMapSpectrum implements vc_i_ColorMap, java.io.Serializable {
	
	/**
	 * Construct a spcetrum color map
	 */
	public vc_ColorMapSpectrum() {
	}

	/**
	 * Convert a value in the range 0..1 to an RGB color.
	 * @param v a value in the range 0..1
	 * @return an RGB color
	 */
	public int getColor(float v) {
		return vc_Spectrum.wavelengthToRGB(380+400*m_MathUtils.clamp(v, 0, 1.0f));
	}
	
}
