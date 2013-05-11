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

public abstract class m_a_Function2DCompound implements m_i_Function2D {

	protected m_i_Function2D basis;
	
	public m_a_Function2DCompound(m_i_Function2D basis) {
		this.basis = basis;
	}
	
	public void setBasis(m_i_Function2D basis) {
		this.basis = basis;
	}

	public m_i_Function2D getBasis() {
		return basis;
	}

}

