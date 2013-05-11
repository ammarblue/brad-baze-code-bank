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

public class m_Function2DMarble extends m_a_Function2DCompound {
	
	public m_Function2DMarble() {
		super(new m_Function2DTurbulence(new m_Noise(), 6));
	}
	
	public m_Function2DMarble(m_i_Function2D basis) {
		super(basis);
	}
	
	public float evaluate(float x, float y) {
		return (float)Math.pow(0.5 * (Math.sin(8. * basis.evaluate(x, y)) + 1), 0.77);
	}

}
