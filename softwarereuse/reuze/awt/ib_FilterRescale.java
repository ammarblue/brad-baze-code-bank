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

public class ib_FilterRescale extends ib_a_FilterTransfer {

	static final long serialVersionUID = -2724874183243154495L;
	
	private float scale = 1.0f;
	
	protected float transferFunction( float v ) {
		return m_MathUtils.clampToByte((int)(v * scale));
	}

	public void setScale(float scale) {
		this.scale = scale;
		initialized = false;
	}
	
	public float getScale() {
		return scale;
	}

	public String toString() {
		return "Colors/Rescale...";
	}

}

