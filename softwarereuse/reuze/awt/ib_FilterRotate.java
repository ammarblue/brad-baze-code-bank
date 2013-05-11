package reuze.awt;

import com.software.reuze.ga_Rectangle;
import com.software.reuze.ib_a_FilterTransform;
import com.software.reuze.m_MathUtils;
import com.software.reuze.z_Point2D;


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

public class ib_FilterRotate extends ib_a_FilterTransform {

	static final long serialVersionUID = 1166374736665848180L;
	
	private float angle;
	private float cos, sin;
	private boolean resize = true;

	public ib_FilterRotate() {
		this(m_MathUtils.PI);
	}

	public ib_FilterRotate(float angle) {
		this(angle, true);
	}

	public ib_FilterRotate(float angle, boolean resize) {
		setAngle(angle);
		this.resize = resize;
	}

	public void setAngle(float angle) {
		this.angle = angle;
		cos = (float)Math.cos(this.angle);
		sin = (float)Math.sin(this.angle);
	}

	public float getAngle() {
		return angle;
	}

	protected void transformSpace(ga_Rectangle rect) {
		if (resize) {
			z_Point2D out = new z_Point2D(0, 0);
			int minx = Integer.MAX_VALUE;
			int miny = Integer.MAX_VALUE;
			int maxx = Integer.MIN_VALUE;
			int maxy = Integer.MIN_VALUE;
			int w = (int) rect.width;
			int h = (int) rect.height;
			int x = (int) rect.position.x;
			int y = (int) rect.position.y;

			for (int i = 0; i < 4; i++)  {
				switch (i) {
				case 0: transform(x, y, out); break;
				case 1: transform(x + w, y, out); break;
				case 2: transform(x, y + h, out); break;
				case 3: transform(x + w, y + h, out); break;
				}
				minx = (int) Math.min(minx, out.x);
				miny = (int) Math.min(miny, out.y);
				maxx = (int) Math.max(maxx, out.x);
				maxy = (int) Math.max(maxy, out.y);
			}

			rect.position.x = minx;
			rect.position.y = miny;
			rect.width = maxx - rect.position.x;
			rect.height = maxy - rect.position.y;
		}
	}

	protected void transform(int x, int y, z_Point2D out) {
		out.x = (int)((x * cos) + (y * sin));
		out.y = (int)((y * cos) - (x * sin));
	}

	protected void transformInverse(int x, int y, float[] out) {
		out[0] = (x * cos) - (y * sin);
		out[1] = (y * cos) + (x * sin);
	}

	public String toString() {
		return "Rotate "+(int)(angle * 180 / Math.PI);
	}

}
