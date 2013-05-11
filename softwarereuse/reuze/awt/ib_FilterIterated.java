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

public class ib_FilterIterated extends ib_a_Ops {
	private z_BufferedImageOp filter;
	private int iterations;
	
	public ib_FilterIterated( z_BufferedImageOp filter, int iterations ) {
		this.filter = filter;
		this.iterations = iterations;
	}
	
	public z_BufferedImage filter( z_BufferedImage src, z_BufferedImage dst ) {
		z_BufferedImage image = src;

		for ( int i = 0; i < iterations; i++ )
			image = filter.filter( image, dst );
		
		return image;
	}
}
