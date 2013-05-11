package reuze.awt;

import com.software.reuze.ib_a_FilterTransform;
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
//grabs image at the center and pulls it away from the viewer
public class ib_FilterPinch extends ib_a_FilterTransform {

	private float angle = 0;
	private float centreX = 0.5f;
	private float centreY = 0.5f;
	private float radius = 100;
	private float amount = 0.5f;

	private float radius2 = 0;
	private float icentreX;
	private float icentreY;
	private float width;
	private float height;
	
	public ib_FilterPinch() {
	}

	/**
	 * Set the angle of twirl in radians. 0 means no distortion.
	 * @param angle the angle of twirl. This is the angle by which pixels at the nearest edge of the image will move.
	 */
	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	/**
	 * Get the angle of twist.
	 * @return the angle in radians.
	 */
	public float getAngle() {
		return angle;
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
	
	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public float getAmount() {
		return amount;
	}

    public z_BufferedImage filter( z_BufferedImage src, z_BufferedImage dst ) {
		width = src.getWidth();
		height = src.getHeight();
		icentreX = width * centreX;
		icentreY = height * centreY;
		if ( radius == 0 )
			radius = Math.min(icentreX, icentreY);
		radius2 = radius*radius;
		return super.filter( src, dst );
	}
	
	protected void transformInverse(int x, int y, float[] out) {
		float dx = x-icentreX;
		float dy = y-icentreY;
		float distance = dx*dx + dy*dy;

		if ( distance > radius2 || distance == 0 ) {
			out[0] = x;
			out[1] = y;
		} else {
			float d = (float)Math.sqrt( distance / radius2 );
			float t = (float)Math.pow( Math.sin( Math.PI*0.5 * d ), -amount);

			dx *= t;
			dy *= t;

			float e = 1 - d;
			float a = angle * e * e;

			float s = (float)Math.sin( a );
			float c = (float)Math.cos( a );

			out[0] = icentreX + c*dx - s*dy;
			out[1] = icentreY + s*dx + c*dy;
		}
	}

	public String toString() {
		return "Distort/Pinch...";
	}

}
