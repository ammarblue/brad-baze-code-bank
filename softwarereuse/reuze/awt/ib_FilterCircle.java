package reuze.awt;

import com.software.reuze.ib_a_FilterTransform;
import com.software.reuze.m_MathUtils;
import com.software.reuze.z_BufferedImage;
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

public class ib_FilterCircle extends ib_a_FilterTransform {

	private float radius = 10;
	private float height = 20;
	private float angle = 0;
	private float spreadAngle = (float)Math.PI;
	private float centreX = 0.5f;
	private float centreY = 0.5f;

	private float icentreX;
	private float icentreY;
	private float iWidth;
	private float iHeight;

	public ib_FilterCircle() {
		setEdgeAction( ZERO );
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getHeight() {
		return height;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getAngle() {
		return angle;
	}

	public void setSpreadAngle(float spreadAngle) {
		this.spreadAngle = spreadAngle;
	}

	public float getSpreadAngle() {
		return spreadAngle;
	}

	public void setRadius(float r) {
		this.radius = r;
	}

	public float getRadius() {
		return radius;
	}

	public void setCentreX( float centreX ) {
		this.centreX = centreX;
	}

	public float getCentreX() {
		return centreX;
	}
	
	public void setCentreY( float centreY ) {
		this.centreY = centreY;
	}

	public float getCentreY() {
		return centreY;
	}
	
	public void setCentre( z_Point2D centre ) {
		this.centreX = (float)centre.getX();
		this.centreY = (float)centre.getY();
	}

	public z_Point2D getCentre() {
		return new z_Point2D( centreX, centreY );
	}
	
    public z_BufferedImage filter( z_BufferedImage src, z_BufferedImage dst ) {
		iWidth = src.getWidth();
		iHeight = src.getHeight();
		icentreX = iWidth * centreX;
		icentreY = iHeight * centreY;
		iWidth--;
		return super.filter( src, dst );
	}
	
	protected void transformInverse(int x, int y, float[] out) {
		float dx = x-icentreX;
		float dy = y-icentreY;
		float theta = (float)Math.atan2( -dy, -dx ) + angle;
		float r = (float)Math.sqrt( dx*dx + dy*dy );
/*
		if ( theta < 0 )
			theta += 2*(float)Math.PI;
		else if ( theta > 2*(float)Math.PI )
			theta -= 2*(float)Math.PI;
*/
		theta = m_MathUtils.mod( theta, 2*(float)Math.PI );

		out[0] = iWidth * theta/(spreadAngle+0.00001f);
		out[1] = iHeight * (1-(r-radius)/(height+0.00001f));
	}

	public String toString() {
		return "Distort/Circle...";
	}

}
