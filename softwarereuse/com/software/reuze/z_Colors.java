package com.software.reuze;

import java.awt.Color;


public class z_Colors extends Color { 
	//isolates connection to awt, replace with other interfaces if needed
	/**
     * Maximum rgb component value for a color to be classified as black.
     * 
     * @see #isBlack()
     */
    public static float BLACK_POINT = 0.08f;
    public static float WHITE_POINT = 1f;
    /**
     * Maximum saturation value for a color to be classified as grey
     * 
     * @see #isGrey()
     */
    public static float GREY_THRESHOLD = 0.01f;
	public z_Colors(int r, int g, int b, int a) {
		super(r&0xff,g&0xff,b&0xff,a&0xff);
	}
	public z_Colors(int argb) {
		super(argb, true);
	}
	public z_Colors(int rgb, int alpha) {
		this(rgb>>16, rgb>>8, rgb, alpha);
	}
	public z_Colors(int r, int g, int b) {
		super(r,g,b);
	}
	public z_Colors(int argb, boolean hasAlpha) {
		super(argb, hasAlpha);
	}
	public z_Colors(float r, float g, float b, float a) {
		super(r,g,b,a);
	}
	public z_Colors(float r, float g, float b) {
		super(r,g,b);
	}
	public static z_Colors getColor(String name) {
		return new z_Colors(vc_ColorNamesInt.getARGB(name));
	}
	public z_Colors copy() {
		int i=this.getRGB();
		return new z_Colors(i,true);
	}
	protected static final ga_Vector2[] RYB_WHEEL = new ga_Vector2[] {
        new ga_Vector2(0, 0), new ga_Vector2(15, 8), new ga_Vector2(30, 17),
        new ga_Vector2(45, 26), new ga_Vector2(60, 34), new ga_Vector2(75, 41),
        new ga_Vector2(90, 48), new ga_Vector2(105, 54), new ga_Vector2(120, 60),
        new ga_Vector2(135, 81), new ga_Vector2(150, 103), new ga_Vector2(165, 123),
        new ga_Vector2(180, 138), new ga_Vector2(195, 155), new ga_Vector2(210, 171),
        new ga_Vector2(225, 187), new ga_Vector2(240, 204), new ga_Vector2(255, 219),
        new ga_Vector2(270, 234), new ga_Vector2(285, 251), new ga_Vector2(300, 267),
        new ga_Vector2(315, 282), new ga_Vector2(330, 298), new ga_Vector2(345, 329),
        new ga_Vector2(360, 0)
};
	/**
     * Factory method. New color from hsv values.
     * 
     * @param h
     * @param s
     * @param v
     * @return new color
     */
    public static final z_Colors newHSV(float h, float s, float v) {
        return new z_Colors(getHSBColor(h, s, v).getRGB(),true);
    }

    public static final z_Colors newHSVA(float h, float s, float v, float a) {
    	return new z_Colors((getHSBColor(h, s, v).getRGB())&0xFFFFFF, (int)(a*255));
    }
    /**
     * Factory method. Creates new random color. Alpha is always 1.0.
     * 
     * @return random color
     */
    public static final z_Colors newRandom() {
        return new z_Colors(m_MathUtils.random(1f), m_MathUtils.random(1f),
                m_MathUtils.random(1f), 1);
    }
    public int toARGB() {
		return getRGB();
	}
	public float alpha() {
		return getAlpha()/255f;
	}
	public boolean isBlack() {
		getColorComponents(temp);
        return (temp[0] <= BLACK_POINT && Float.compare(temp[0], temp[1]) == 0 && 
        		Float.compare(temp[0], temp[2]) == 0);
    }
	public boolean isWhite() {
		getColorComponents(temp);
        return (temp[0] >= WHITE_POINT && Float.compare(temp[0], temp[1]) == 0 && Float
                .compare(temp[0], temp[2]) == 0);
    }
	public boolean isGray() {
		int i=getRGB();
    	RGBtoHSB((i>>16)&0xff, (i>>8)&0xff, i&0xff, temp);
        return temp[1] < GREY_THRESHOLD;
    }
	/**
     * Factory method. Creates a new shade of gray with alpha set to 100%.
     * 
     * @param gray
     * @return new color.
     */
    public static final z_Colors newGray(float gray) {
        return newGrayAlpha(gray, 1);
    }

    /**
     * Factory method. Creates a new shade of gray + alpha.
     * 
     * @param gray
     * @param alpha
     * @return new color.
     */
    public static final z_Colors newGrayAlpha(float gray, float alpha) {
        return new z_Colors(gray,gray,gray,alpha);
    }
	public z_Colors blend(z_Colors c, float t) {
		int i=this.getRGB();
		int r=(i>>16)&0xff;
		int g=(i>>8)&0xff;
		int b=i&0xff;
		int a=(i>>24)&0xff;
		int ii=c.getRGB();
		int rr=(ii>>16)&0xff;
		int gg=(ii>>8)&0xff;
		int bb=ii&0xff;
		int aa=(ii>>24)&0xff;
		r+=(rr-r)*t;
		g+=(gg-g)*t;
		b+=(bb-b)*t;
		a+=(aa-a)*t;
		return new z_Colors(r,g,b,a);
	}
	public float distanceToRGB(z_Colors c) {
        int i=this.getRGB();
		int r=(i>>16)&0xff;
		int g=(i>>8)&0xff;
		int b=i&0xff;
		int ii=c.getRGB();
		int rr=(ii>>16)&0xff;
		int gg=(ii>>8)&0xff;
		int bb=ii&0xff;
		r=(r-rr);
		g=(g-gg);
		b=(b-bb);
        return (float) Math.sqrt(r * r + g * g + b * b);
    }
	private static gb_Vector3 tmp=new gb_Vector3(), tmp2=new gb_Vector3();
	public float distanceToHSV(z_Colors c) {
		int i=getRGB();
		float[] hsv=this.RGBtoHSB((i>>16)&0xff, (i>>8)&0xff, i&0xff, temp);
        float hue = hsv[0] * m_MathUtils.TWO_PI;
        float hue2 = c.hue() * m_MathUtils.TWO_PI;
        tmp.set((m_MathUtils.cos(hue) * hsv[1]),
                (m_MathUtils.sin(hue) * hsv[1]), hsv[2]);
        tmp2.set((m_MathUtils.cos(hue2) * c.saturation()),
                (m_MathUtils.sin(hue2) * c.saturation()), c.brightness());
        return tmp.dst(tmp2);
    }
	public float hue() {
		int i=getRGB();
		float[] hsv=RGBtoHSB((i>>16)&0xff, (i>>8)&0xff, i&0xff, temp);
		return hsv[0];
	}
	public float saturation() {
		int i=getRGB();
		float[] hsv=RGBtoHSB((i>>16)&0xff, (i>>8)&0xff, i&0xff, temp);
		return hsv[1];
	}
	public float brightness() {
		int i=getRGB();
		float[] hsv=RGBtoHSB((i>>16)&0xff, (i>>8)&0xff, i&0xff, temp);
		return hsv[2];
	}
	private static float temp[]=new float[4];
	public float luminance() {
		getColorComponents(temp); //rgb
        return temp[0] * 0.299f + temp[1] * 0.587f + temp[2] * 0.114f;
    }
	public z_Colors invert() {
		getComponents(temp); //rgb
        temp[0] = 1 - temp[0];
        temp[1] = 1 - temp[1];
        temp[2] = 1 - temp[2];
        return new z_Colors(temp[0], temp[1], temp[2], temp[3]);
    }
	/**
     * Adds the given value to the current saturation component.
     * 
     * @param step
     * @return itself
     */
    public z_Colors saturate(float step) {
    	int i=getRGB();
    	float[] hsv=RGBtoHSB((i>>16)&0xff, (i>>8)&0xff, i&0xff, temp);
        hsv[1] += step;
        return new z_Colors(HSBtoRGB(hsv[0],hsv[1],hsv[2]),getAlpha());
    }
    /**
     * Lightens the color by stated amount.
     * 
     * @param step
     *            lighten amount
     * @return itself
     */
    public z_Colors lighten(float step) {
    	int i=getRGB();
    	float[] hsv=RGBtoHSB((i>>16)&0xff, (i>>8)&0xff, i&0xff, temp);
        hsv[2] += step;
        return new z_Colors(HSBtoRGB(hsv[0],hsv[1],hsv[2]),getAlpha());
    }
    public float getComponentValue(vc_a_CriteriaAccess criteria) {
        return criteria.getComponentValueFor(this);
    }
    public z_Colors getRotatedRYB(int angle) {
        return rotateRYB(angle);
    }
    public z_Colors rotateRYB(float theta) {
        return rotateRYB((int) m_MathUtils.degrees(theta));
    }
	/**
     * Rotates the color by x degrees along the <a
     * href="http://en.wikipedia.org/wiki/RYB_color_model">RYB color wheel</a>
     * 
     * @param theta
     * @return itself
     */
    public z_Colors rotateRYB(int theta) {
    	int i=getRGB();
    	float[] hsv=RGBtoHSB((i>>16)&0xff, (i>>8)&0xff, i&0xff, temp);
        float h = hsv[0] * 360;
        theta %= 360;

        float resultHue = 0;
        for (i = 0; i < RYB_WHEEL.length - 1; i++) {
            ga_Vector2 p = RYB_WHEEL[i];
            ga_Vector2 q = RYB_WHEEL[i + 1];
            if (q.y < p.y) {
                q.y += 360;
            }
            if (p.y <= h && h <= q.y) {
                resultHue = p.x + (q.x - p.x) * (h - p.y) / (q.y - p.y);
                break;
            }
        }

        // And the user-given angle (e.g. complement).
        resultHue = (resultHue + theta) % 360;

        // For the given angle, find out what hue is
        // located there on the artistic color wheel.
        for (i = 0; i < RYB_WHEEL.length - 1; i++) {
            ga_Vector2 p = RYB_WHEEL[i];
            ga_Vector2 q = RYB_WHEEL[i + 1];
            if (q.y < p.y) {
                q.y += 360;
            }
            if (p.x <= resultHue && resultHue <= q.x) {
                h = p.y + (q.y - p.y) * (resultHue - p.x) / (q.x - p.x);
                break;
            }
        }

        hsv[0] = (h % 360) / 360.0f;
        return new z_Colors(HSBtoRGB(hsv[0],hsv[1],hsv[2]),getAlpha());
    }
    public z_Colors complement() {
        return rotateRYB(180);
    }
    public z_Colors setBrightness(float brightness) {
    	int i=getRGB();
    	float[] hsv=RGBtoHSB((i>>16)&0xff, (i>>8)&0xff, i&0xff, temp);
        hsv[2] = m_MathUtils.clip(brightness, 0, 1);
        Color c=getHSBColor(hsv[0],hsv[1],hsv[2]);
        return new z_Colors(c.getRGB()&0xffffff,getAlpha());
    }
    public z_Colors setSaturation(float saturation) {
    	int i=getRGB();
    	float[] hsv=RGBtoHSB((i>>16)&0xff, (i>>8)&0xff, i&0xff, temp);
        hsv[1] = m_MathUtils.clip(saturation, 0, 1);
        Color c=getHSBColor(hsv[0],hsv[1],hsv[2]);
        return new z_Colors(c.getRGB()&0xffffff,getAlpha());
    }
    public float red() {
    	getColorComponents(temp);
    	return temp[0];
    }
    public float green() {
    	getColorComponents(temp);
    	return temp[1];
    }
    public float blue() {
    	getColorComponents(temp);
    	return temp[2];
    }
    public static final float[] rgbToCMYK(float r, float g, float b,
            float[] cmyk) {
        cmyk[0] = 1 - r;
        cmyk[1] = 1 - g;
        cmyk[2] = 1 - b;
        cmyk[3] = m_MathUtils.min(cmyk[0], cmyk[1], cmyk[2]);
        cmyk[0] = m_MathUtils.clip(cmyk[0] - cmyk[3], 0, 1);
        cmyk[1] = m_MathUtils.clip(cmyk[1] - cmyk[3], 0, 1);
        cmyk[2] = m_MathUtils.clip(cmyk[2] - cmyk[3], 0, 1);
        cmyk[3] = m_MathUtils.clip(cmyk[3], 0, 1);
        return cmyk;
    }
    public float cyan() {
    	getColorComponents(temp);
    	rgbToCMYK(temp[0],temp[1],temp[2],temp);
    	return temp[0];
    }
    public float magenta() {
    	getColorComponents(temp);
    	rgbToCMYK(temp[0],temp[1],temp[2],temp);
    	return temp[1];
    }
    public float yellow() {
    	getColorComponents(temp);
    	rgbToCMYK(temp[0],temp[1],temp[2],temp);
    	return temp[2];
    }
    public float black() {
    	getColorComponents(temp);
    	rgbToCMYK(temp[0],temp[1],temp[2],temp);
    	return temp[3];
    }
    /**
     * Rotates this color by a random amount (not exceeding the one specified)
     * and creates variations in saturation and brightness based on the 2nd
     * parameter.
     * 
     * @param theta
     *            max. rotation angle (in radians)
     * @param delta
     *            max. sat/bri variance
     * @return itself
     */
    public z_Colors analog(float theta, float delta) {
        return analog((int) m_MathUtils.degrees(theta), delta);
    }

    public z_Colors analog(int angle, float delta) {
        z_Colors c=rotateRYB((int) (angle * m_MathUtils.normalizedRandom()));
        int i=c.getRGB();
    	float[] hsv=RGBtoHSB((i>>16)&0xff, (i>>8)&0xff, i&0xff, temp);
        hsv[1] += delta * m_MathUtils.normalizedRandom();
        hsv[2] += delta * m_MathUtils.normalizedRandom();
        return newHSV(hsv[0],hsv[1],hsv[2]);
    }
	public static final z_Colors black=new z_Colors(0);
}
