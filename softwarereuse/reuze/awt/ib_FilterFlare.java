package reuze.awt;

import com.software.reuze.i_MathImageUtils;
import com.software.reuze.ib_a_FilterPoint;
import com.software.reuze.m_InterpolateLerp;
import com.software.reuze.m_MathUtils;
import com.software.reuze.m_Noise;
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

/**
 * An experimental filter for rendering lens flares.
 */
public class ib_FilterFlare extends ib_a_FilterPoint {

	private int rays = 50;
	private float radius;
	private float baseAmount = 1.0f;
	private float ringAmount = 0.2f;
	private float rayAmount = 0.1f;
	private int color = 0xffffffff;
	private int width, height;
	private float centreX = 0.5f, centreY = 0.5f;
	private float ringWidth = 1.6f;
	
	private float linear = 0.03f;
	private float gauss = 0.006f;
	private float mix = 0.50f;
	private float falloff = 6.0f;
	private float sigma;

	private float icentreX, icentreY;

	public ib_FilterFlare() {
		setRadius(50.0f);
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	public void setRingWidth(float ringWidth) {
		this.ringWidth = ringWidth;
	}

	public float getRingWidth() {
		return ringWidth;
	}
	
	public void setBaseAmount(float baseAmount) {
		this.baseAmount = baseAmount;
	}

	public float getBaseAmount() {
		return baseAmount;
	}

	public void setRingAmount(float ringAmount) {
		this.ringAmount = ringAmount;
	}

	public float getRingAmount() {
		return ringAmount;
	}

	public void setRayAmount(float rayAmount) {
		this.rayAmount = rayAmount;
	}

	public float getRayAmount() {
		return rayAmount;
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
		sigma = radius/3;
	}

	public float getRadius() {
		return radius;
	}

	public void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;
		icentreX = centreX*width;
		icentreY = centreY*height;
		super.setDimensions(width, height);
	}
	
	public int filterRGB(int x, int y, int rgb) {
		float dx = x-icentreX;
		float dy = y-icentreY;
		float distance = (float)Math.sqrt(dx*dx+dy*dy);
		float a = (float)Math.exp(-distance*distance*gauss)*mix + (float)Math.exp(-distance*linear)*(1-mix);
		float ring;

		a *= baseAmount;

		if (distance > radius + ringWidth)
			a = m_InterpolateLerp.lerp((distance - (radius + ringWidth))/falloff, a, 0);

		if (distance < radius - ringWidth || distance > radius + ringWidth)
			ring = 0;
		else {
	        ring = Math.abs(distance-radius)/ringWidth;
	        ring = 1 - ring*ring*(3 - 2*ring);
	        ring *= ringAmount;
		}

		a += ring;

		float angle = (float)Math.atan2(dx, dy)+m_MathUtils.PI;
		angle = (m_MathUtils.mod(angle/m_MathUtils.PI*17 + 1.0f + m_Noise.noise1(angle*10), 1.0f) - 0.5f)*2;
		angle = Math.abs(angle);
		angle = (float)Math.pow(angle, 5.0);

		float b = rayAmount * angle / (1 + distance*0.1f);
		a += b;

		a = m_MathUtils.clamp(a, 0, 1);
		return i_MathImageUtils.mixColors(a, rgb, color);
	}

	public String toString() {
		return "Stylize/Flare...";
	}
}
