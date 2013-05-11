package reuze.awt;

import com.software.reuze.ib_a_FilterTransfer;
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

public class ib_FilterGain extends ib_a_FilterTransfer {

	private float gain = 0.5f;
	private float bias = 0.5f;
	
	protected float transferFunction( float f ) {
		f = m_MathUtils.gain(f, gain);
		f = m_MathUtils.bias(f, bias);
		return f;
	}

	public void setGain(float gain) {
		this.gain = gain;
		initialized = false;
	}
	
	public float getGain() {
		return gain;
	}

	public void setBias(float bias) {
		this.bias = bias;
		initialized = false;
	}
	
	public float getBias() {
		return bias;
	}

	public String toString() {
		return "Colors/Gain...";
	}

}

