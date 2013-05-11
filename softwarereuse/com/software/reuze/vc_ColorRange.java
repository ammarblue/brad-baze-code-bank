package com.software.reuze;
/*
 * Some classes in this package have been partly inspired by & bits ported from
 * Python code written by Tom De Smedt & Frederik De Bleser for the "colors" library
 * of Nodebox.net.
 * 
 * http://nodebox.net/code/index.php/Colors
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */


import java.lang.reflect.Field;
import java.util.HashMap;

import com.software.reuze.d_SetGeneric;

/**
 * A ColorRange is a set of constraints to specify possible ranges for hue,
 * saturation, brightness and alpha independently and use these as creation
 * rules for new {@link z_Colors}s or {@link vc_ColorList}s. The class comes with 11
 * preset ranges reflecting common demands and color characters. You can also
 * construct new ranges and manually add additional constraints. Unless the
 * constraints in a range are very narrow the class will always create random
 * variations within the constraints. Please see the examples for further
 * details.
 * 
 * {@link vc_ColorRange}s are a key ingredient for defining {@link vc_ColorTheme}s but
 * can also be used individually.
 */
public class vc_ColorRange {

    /**
     * Default hue variance for {@link #getColor(ReadonlyTColor, float)}
     */
    public static final float DEFAULT_VARIANCE = 0.035f;

    /**
     * Shade definition: saturation 30-70%, brightness: 90-100%
     */
    public static final vc_ColorRange LIGHT = new vc_ColorRange(null, new m_RangeFloat(
            0.3f, 0.7f), new m_RangeFloat(0.9f, 1.0f), null, new m_RangeFloat(
            0.15f, 0.30f), null, "light");

    /**
     * Shade definition: saturation 70-100%, brightness: 15-40%
     */
    public static final vc_ColorRange DARK = new vc_ColorRange(null, new m_RangeFloat(
            0.7f, 1.0f), new m_RangeFloat(0.15f, 0.4f), null, null,
            new m_RangeFloat(0.5f, 0.75f), "dark");

    /**
     * Shade definition: saturation 80-100%, brightness: 80-100%
     */
    public static final vc_ColorRange BRIGHT = new vc_ColorRange(null,
            new m_RangeFloat(0.8f, 1.0f), new m_RangeFloat(0.8f, 1.0f), "bright");

    /**
     * Shade definition: saturation 15-30%, brightness: 70-100%
     */
    public static final vc_ColorRange WEAK = new vc_ColorRange(null, new m_RangeFloat(
            0.15f, 0.3f), new m_RangeFloat(0.7f, 1.0f), null, new m_RangeFloat(
            0.2f, 0.2f), null, "weak");

    /**
     * Shade definition: saturation 25-35%, brightness: 30-70%
     */
    public static final vc_ColorRange NEUTRAL = new vc_ColorRange(null,
            new m_RangeFloat(0.25f, 0.35f), new m_RangeFloat(0.3f, 0.7f), null,
            new m_RangeFloat(0.15f, 0.15f), new m_RangeFloat(0.9f, 1), "neutral");

    /**
     * Shade definition: saturation 40-80%, brightness: 80-100%
     */
    public static final vc_ColorRange FRESH = new vc_ColorRange(null, new m_RangeFloat(
            0.4f, 0.8f), new m_RangeFloat(0.8f, 1.0f), null, new m_RangeFloat(
            0.05f, 0.3f), new m_RangeFloat(0.8f, 1.0f), "fresh");

    /**
     * Shade definition: saturation 20-30%, brightness: 60-90%
     */
    public static final vc_ColorRange SOFT = new vc_ColorRange(null, new m_RangeFloat(
            0.2f, 0.3f), new m_RangeFloat(0.6f, 0.9f), null, new m_RangeFloat(
            0.05f, 0.15f), new m_RangeFloat(0.6f, 0.9f), "soft");

    /**
     * Shade definition: saturation 90-100%, brightness: 40-100%
     */
    public static final vc_ColorRange HARD = new vc_ColorRange(null, new m_RangeFloat(
            0.9f, 1.0f), new m_RangeFloat(0.4f, 1.0f), "hard");

    /**
     * Shade definition: saturation 60-90%, brightness: 40-90%
     */
    public static final vc_ColorRange WARM = new vc_ColorRange(null, new m_RangeFloat(
            0.6f, 0.9f), new m_RangeFloat(0.4f, 0.9f), null, new m_RangeFloat(0.2f,
            0.2f), new m_RangeFloat(0.8f, 1.0f), "warm");

    /**
     * Shade definition: saturation 5-20%, brightness: 90-100%
     */
    public static final vc_ColorRange COOL = new vc_ColorRange(null, new m_RangeFloat(
            0.05f, 0.2f), new m_RangeFloat(0.9f, 1.0f), null, null,
            new m_RangeFloat(0.95f, 1.0f), "cool");

    /**
     * Shade definition: saturation 90-100%, brightness: 20-35% or 80-100%
     */
    public static final vc_ColorRange INTENSE = new vc_ColorRange(null,
            new m_RangeFloat(0.9f, 1.0f), new m_RangeFloat(0.2f, 0.35f), "intense")
            .addBrightnessRange(new m_RangeFloat(0.8f, 1.0f));

    /**
     * List of ColorRange presets.
     */
    public static final HashMap<String, vc_ColorRange> PRESETS = new HashMap<String, vc_ColorRange>();

    private static int UNTITLED_ID = 1;

    protected d_SetGeneric<m_RangeFloat> hueConstraint;

    protected d_SetGeneric<m_RangeFloat> saturationConstraint;
    protected d_SetGeneric<m_RangeFloat> brightnessConstraint;
    protected d_SetGeneric<m_RangeFloat> alphaConstraint;
    protected m_RangeFloat white;

    protected m_RangeFloat black;
    protected String name;

    static {
        Field[] fields = vc_ColorRange.class.getDeclaredFields();
        try {
            for (Field f : fields) {
                if (f.getType() == vc_ColorRange.class) {
                    String id = f.getName();
                    PRESETS.put(id, (vc_ColorRange) f.get(null));
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves one of the predefined ranges by name.
     * 
     * @param name
     * @return color range or null if name not registered
     */
    public static vc_ColorRange getPresetForName(String name) {
        return PRESETS.get(name.toUpperCase());
    }

    /**
     * Only used internally by {@link #copy()}, doesn't initialize anything.
     */
    private vc_ColorRange() {

    }

    /**
     * Constructs a new range using the given colors as HSV constraints.
     * 
     * @param list
     *            list base colors
     */
    public vc_ColorRange(vc_ColorList list) {
        this(list.get(0));
        hueConstraint.clear();
        for (z_Colors c : list) {
            add(c);
        }
    }

    /**
     * Constructs a new range with the supplied constraints (if an HSV argument
     * is null, a range of 0.0 ... 1.0 is created automatically for that
     * constraint). If alpha is left undefined, it'll be initialized to fully
     * opaque only. You can also specify ranges for possible black and white
     * points which are used if the range is later applied to a gray-scale color.
     * The default black point is at 0.0 and white at 1.0.
     * 
     * @param hue
     * @param sat
     * @param bri
     * @param alpha
     * @param black
     * @param white
     * @param name
     */
    public vc_ColorRange(m_RangeFloat hue, m_RangeFloat sat, m_RangeFloat bri,
            m_RangeFloat alpha, m_RangeFloat black, m_RangeFloat white, String name) {
        super();
        hueConstraint = new d_SetGeneric<m_RangeFloat>(hue != null ? hue
                : new m_RangeFloat(0, 1));
        saturationConstraint = new d_SetGeneric<m_RangeFloat>(sat != null ? sat
                : new m_RangeFloat(0, 1));
        brightnessConstraint = new d_SetGeneric<m_RangeFloat>(bri != null ? bri
                : new m_RangeFloat(0, 1));
        alphaConstraint = new d_SetGeneric<m_RangeFloat>(alpha != null ? alpha
                : new m_RangeFloat(1, 1));
        if (black == null) {
            this.black = new m_RangeFloat(0, 0);
        } else {
            this.black = black;
        }
        if (white == null) {
            this.white = new m_RangeFloat(1, 1);
        } else {
            this.white = white;
        }
        this.name = name != null ? name : "untitled" + (UNTITLED_ID++);
    }

    /**
     * Constructs a new range with the supplied constraints (if an argument is
     * null, a range of 0.0 ... 1.0 is created automatically for that
     * constraint).
     * 
     * @param hue
     *            min/max hue range
     * @param sat
     *            min/max saturation range
     * @param bri
     *            min/max brightness range
     * @param alpha
     *            min/max alpha range (if null, initialized to 100% only)
     * @param name
     */
    public vc_ColorRange(m_RangeFloat hue, m_RangeFloat sat, m_RangeFloat bri,
            m_RangeFloat alpha, String name) {
        this(hue, sat, bri, alpha, null, null, name);
    }

    /**
     * Constructs a new range with the supplied constraints (if an argument is
     * null, a range of 0.0 ... 1.0 is created automatically for that
     * constraint). Alpha constraint will be set to 100%.
     * 
     * @param hue
     *            min/max hue range
     * @param sat
     *            min/max saturation range
     * @param bri
     *            min/max brightness range
     * @param name
     */
    public vc_ColorRange(m_RangeFloat hue, m_RangeFloat sat, m_RangeFloat bri,
            String name) {
        this(hue, sat, bri, null, null, null, name);
    }

    /**
     * Constructs a new range using the given hue as constraint, but saturation
     * and brightness are fully flexible. The resulting range will produce any
     * shade of the given hue.
     * 
     * @param hue
     *            base hue
     */
    /*public ColorRange(Hue hue) {
        this(new FloatRange(hue.getHue(), hue.getHue()), null, null, null,
                null, null, null);
    }*/

    /**
     * Constructs a new range using the hue of the given color as hue
     * constraint, but saturation and brightness are fully flexible. The
     * resulting range will produce any shade of the given color.
     * 
     * @param c
     *            base color
     */
    public vc_ColorRange(z_Colors c) {
        this(new m_RangeFloat(c.hue(), c.hue()), null, null, null, null, null,
                null);
    }

    /**
     * Adds the contraints of the given range to this range and forms unions for
     * the black and white point ranges.
     * 
     * @param range
     *            color range to add
     * @return itself
     */
    public vc_ColorRange add(vc_ColorRange range) {
        hueConstraint.addAll(range.hueConstraint.getItems());
        saturationConstraint.addAll(range.saturationConstraint.getItems());
        brightnessConstraint.addAll(range.brightnessConstraint.getItems());
        alphaConstraint.addAll(range.alphaConstraint.getItems());
        black.min = m_MathUtils.min(black.min, range.black.min);
        black.max = m_MathUtils.max(black.max, range.black.max);
        white.min = m_MathUtils.min(white.min, range.white.min);
        white.max = m_MathUtils.max(white.max, range.white.max);
        return this;
    }

    /**
     * Adds the HSV color components as constraints.
     * 
     * @param c
     *            color to use as constraint
     * @return itself
     */
    public vc_ColorRange add(z_Colors c) {
        hueConstraint.add(new m_RangeFloat(c.hue(), c.hue()));
        saturationConstraint
                .add(new m_RangeFloat(c.saturation(), c.saturation()));
        brightnessConstraint
                .add(new m_RangeFloat(c.brightness(), c.brightness()));
        alphaConstraint.add(new m_RangeFloat(c.alpha(), c.alpha()));
        return this;
    }

    /**
     * Adds the range between min-max as possible alpha values for this range.
     * 
     * @param min
     * @param max
     * @return itself
     */
    public vc_ColorRange addAlphaRange(float min, float max) {
        return addAlphaRange(new m_RangeFloat(min, max));
    }

    /**
     * Adds an additional alpha constraint.
     * 
     * @param alpha
     *            min/max alpha values
     * @return itself
     */
    public vc_ColorRange addAlphaRange(m_RangeFloat alpha) {
        alphaConstraint.add(alpha);
        return this;
    }

    /**
     * Adds the range between min-max as possible brightness values for this
     * range.
     * 
     * @param min
     * @param max
     * @return itself
     */
    public vc_ColorRange addBrightnessRange(float min, float max) {
        return addBrightnessRange(new m_RangeFloat(min, max));
    }

    /**
     * Adds an additional brightness constraint.
     * 
     * @param bri
     *            min/max brightness values
     * @return itself
     */
    public vc_ColorRange addBrightnessRange(m_RangeFloat bri) {
        brightnessConstraint.add(bri);
        return this;
    }

    /**
     * Add the given hue as hue constraint.
     * 
     * @param hue
     * @return itself
     */
    /*public ColorRange addHue(Hue hue) {
        hueConstraint.add(new FloatRange(hue.getHue(), hue.getHue()));
        return this;
    }*/

    /**
     * Adds the range between min-max as possible hue values for this range. If
     * max < min then two intervals are added: {min ... 1.0} and {0.0 ... max}
     * 
     * @param min
     * @param max
     * @return itself
     */
    public vc_ColorRange addHueRange(float min, float max) {
        if (max >= min) {
            addHueRange(new m_RangeFloat(min, max));
        } else {
            addHueRange(new m_RangeFloat(min, 1));
            addHueRange(new m_RangeFloat(0, max));
        }
        return this;
    }

    /**
     * Adds an additional hue constraint.
     * 
     * @param hue
     *            min/max hue values
     * @return itself
     */
    public vc_ColorRange addHueRange(m_RangeFloat hue) {
        hueConstraint.add(hue);
        return this;
    }

    /**
     * Adds the range between min-max as possible saturation values for this
     * range.
     * 
     * @param min
     * @param max
     * @return itself
     */
    public vc_ColorRange addSaturationRange(float min, float max) {
        return addAlphaRange(new m_RangeFloat(min, max));
    }

    /**
     * Adds an additional saturation constraint.
     * 
     * @param sat
     *            min/max saturation values
     * @return itself
     */
    public vc_ColorRange addSaturationRange(m_RangeFloat sat) {
        saturationConstraint.add(sat);
        return this;
    }

    /**
     * Checks if all HSVA components of the given color are within the
     * constraints defined for this range.
     * 
     * @param c
     *            color to check
     * @return true, if color is contained
     */
    public boolean contains(z_Colors c) {
        boolean isInRange = isValueInConstraint(c.hue(), hueConstraint);
        isInRange &= isValueInConstraint(c.saturation(), saturationConstraint);
        isInRange &= isValueInConstraint(c.brightness(), brightnessConstraint);
        isInRange &= isValueInConstraint(c.alpha(), alphaConstraint);
        return isInRange;
    }

    /**
     * Creates a shallow copy of the range.
     * 
     * @return copy
     */
    public vc_ColorRange copy() {
        return copy(null, 0);
    }

    /**
     * Creates a copy of the range but overrides the hue and alpha constraints
     * taken from the given color (if specified).
     * 
     * @param c
     *            color, if the new range is to be used to create specific
     *            shades of that color only
     * @param variance
     *            hue variance (use {@link #DEFAULT_VARIANCE} for default)
     * @return copy
     */
    public vc_ColorRange copy(z_Colors c, float variance) {
        vc_ColorRange range = new vc_ColorRange();
        range.name = name;

        if (c != null) {
            float hue = c.hue() + variance * m_MathUtils.normalizedRandom();
            range.hueConstraint = new d_SetGeneric<m_RangeFloat>(new m_RangeFloat(
                    hue, hue));
            range.alphaConstraint = new d_SetGeneric<m_RangeFloat>(new m_RangeFloat(
                    c.alpha(), c.alpha()));
        } else {
            range.hueConstraint = hueConstraint.copy();
            range.alphaConstraint = alphaConstraint.copy();
        }
        range.saturationConstraint = saturationConstraint.copy();
        range.brightnessConstraint = brightnessConstraint.copy();

        range.black = black.copy();
        range.white = white.copy();
        return range;
    }

    /**
     * Creates a new color based on the flexible constraints of the range.
     * 
     * @return color
     */
    public z_Colors getColor() {
        return getColor(null, 0);
    }

    /**
     * Creates a new shade of the given hue based on the other constraints of
     * the range.
     * 
     * @param hue
     * @return color
     */
    /*public Colors getColor(Hue hue) {
        return Colors.newHSVA(hue.getHue(), saturationConstraint.pickRandom()
                .pickRandom(), brightnessConstraint.pickRandom().pickRandom(),
                alphaConstraint.pickRandom().pickRandom());
    }*/

    /**
     * Creates a new color based on the constraints defined in the range. If an
     * input color is specified, the method will use the hue of that color and
     * the given variance to create a shade of a hue within the tolerance.
     * 
     * @param c
     * @param variance
     * @return color
     */
    public z_Colors getColor(z_Colors c, float variance) {
        float h, s, b, a;
        if (c != null) {
            if (c.isBlack()) {
                return z_Colors
                        .newHSVA(c.hue(), 0, black.pickRandom(), c.alpha());
            } else if (c.isWhite()) {
                return z_Colors
                        .newHSVA(c.hue(), 0, white.pickRandom(), c.alpha());
            }
            if (c.isGray()) {
                return z_Colors.newHSVA(
                        c.hue(),
                        0,
                        m_MathUtils.flipCoin() ? black.pickRandom() : white
                                .pickRandom(), c.alpha());
            }
            h = c.hue() + variance * m_MathUtils.normalizedRandom();
            a = c.alpha();
        } else {
            h = hueConstraint.pickRandom().pickRandom();
            a = alphaConstraint.pickRandom().pickRandom();
        }
        s = saturationConstraint.pickRandom().pickRandom();
        b = brightnessConstraint.pickRandom().pickRandom();
        return z_Colors.newHSVA(h, s, b, a);
    }

    /**
     * Creates a new {@link vc_ColorList} of colors based on the constraints of
     * this range.
     * 
     * @see #getColor()
     * @param num
     *            number of colors to create
     * @return color list
     */
    public vc_ColorList getColors(int num) {
        return getColors(null, num, DEFAULT_VARIANCE);
    }

    /**
     * Creates a new {@link vc_ColorList} of shades of the given {@link z_Colors}
     * based on the other constraints of the range.
     * 
     * @see #getColor(ReadonlyTColor, float)
     * @param c
     *            base color
     * @param num
     *            number of colors to create
     * @param variance
     *            hue variance
     * @return color list
     */
    public vc_ColorList getColors(z_Colors c, int num, float variance) {
        vc_ColorList list = new vc_ColorList();
        for (int i = 0; i < num; i++) {
            list.add(getColor(c, variance));
        }
        return list;
    }

    /**
     * Creates a new shade of gray based on the input brightness and the black
     * and white constraints of the range.
     * 
     * @param brightness
     *            input brightness
     * @param variance
     *            hue variance (this might seem irrelevant, but might be
     *            important if the created color is being saturated later on)
     * @return color/shade of gray
     */
    public z_Colors getGrayscale(float brightness, float variance) {
        return getColor(z_Colors.newGray(brightness), variance);
    }

    /**
     * @return name of the range
     */
    public String getName() {
        return name;
    }

    /**
     * Creates a copy of the current range and adds the given one to it.
     * 
     * @see #add(vc_ColorRange)
     * @param range
     *            range to add
     * @return summed copy
     */
    public vc_ColorRange getSum(vc_ColorRange range) {
        return copy().add(range);
    }

    /**
     * @param val
     * @param rangeSet
     * @return true, if value is within range
     */
    protected boolean isValueInConstraint(float val,
            d_SetGeneric<m_RangeFloat> rangeSet) {
        boolean isValid = false;
        for (m_RangeFloat r : rangeSet) {
            isValid |= r.isValueInRange(val);
        }
        return isValid;
    }
}