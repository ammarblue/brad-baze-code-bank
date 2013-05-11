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

public class ib_FilterWater extends ib_a_FilterTransform {

	static final long serialVersionUID = 8789236343162990941L;
	
	private float wavelength = 16;
	private float amplitude = 10;
	private float phase = 0;
	private float centerX = 0.5f;
	private float centerY = 0.5f;
	private float radius = 50;

	private float radius2 = 0;
	private float icenterX;
	private float icenterY;

	public ib_FilterWater() {
		setEdgeAction( CLAMP );
	}

	public void setWavelength(float wavelength) {
		this.wavelength = wavelength;
	}

	public float getWavelength() {
		return wavelength;
	}

	public void setAmplitude(float amplitude) {
		this.amplitude = amplitude;
	}

	public float getAmplitude() {
		return amplitude;
	}

	public void setPhase(float phase) {
		this.phase = phase;
	}

	public float getPhase() {
		return phase;
	}

	public void setCentreX( float centerX ) {
		this.centerX = centerX;
	}

	public float getCentreX() {
		return centerX;
	}
	
	public void setCentreY( float centerY ) {
		this.centerY = centerY;
	}

	public float getCentreY() {
		return centerY;
	}
	
	public void setCentre( z_Point2D center ) {
		this.centerX = (float)center.getX();
		this.centerY = (float)center.getY();
	}

	public z_Point2D getCentre() {
		return new z_Point2D( centerX, centerY );
	}
	
	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}

	private boolean inside(int v, int a, int b) {
		return a <= v && v <= b;
	}
	
    public z_BufferedImage filter( z_BufferedImage src, z_BufferedImage dst ) {
		icenterX = src.getWidth() * centerX;
		icenterY = src.getHeight() * centerY;
		if ( radius == 0 )
			radius = Math.min(icenterX, icenterY);
		radius2 = radius*radius;
		return super.filter( src, dst );
	}
	
	protected void transformInverse(int x, int y, float[] out) {
		float dx = x-icenterX;
		float dy = y-icenterY;
		float distance2 = dx*dx + dy*dy;
		if (distance2 > radius2) {
			out[0] = x;
			out[1] = y;
		} else {
			float distance = (float)Math.sqrt(distance2);
			float amount = amplitude * (float)Math.sin(distance / wavelength * m_MathUtils.TWO_PI - phase);
			amount *= (radius-distance)/radius;
			if ( distance != 0 )
				amount *= wavelength/distance;
			out[0] = x + dx*amount;
			out[1] = y + dy*amount;
		}
	}

	public String toString() {
		return "Distort/Water Ripples...";
	}
	
}
