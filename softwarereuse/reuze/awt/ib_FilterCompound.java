package reuze.awt;

import com.software.reuze.ib_a_Ops;
import com.software.reuze.z_BufferedImage;
import com.software.reuze.z_BufferedImageOp;

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

public class ib_FilterCompound extends ib_a_Ops {
	private z_BufferedImageOp filter1;
	private z_BufferedImageOp filter2;
	
	public ib_FilterCompound( z_BufferedImageOp filter1, z_BufferedImageOp filter2 ) {
		this.filter1 = filter1;
		this.filter2 = filter2;
	}
	
	public z_BufferedImage filter( z_BufferedImage src, z_BufferedImage dst ) {
		z_BufferedImage image = filter1.filter( src, dst );
		image = filter2.filter( image, dst );
		return image;
	}
}
