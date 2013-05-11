package com.software.reuze;
/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
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




import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.software.reuze.m_InterpolateValueLinear;
import com.software.reuze.m_i_InterpolateValue;


/**
 * This class can be used to calculate multi-color gradients with colors
 * positioned along an imaginary axis.
 */
public class vc_ColorGradient {

    protected static final class GradPoint implements Comparable<GradPoint> {

        protected float pos;
        protected z_Colors color;

        GradPoint(float p, z_Colors c) {
            pos = p;
            color = c;
        }

        public int compareTo(GradPoint p) {
            if (Float.compare(p.pos, pos) == 0) {
                return 0;
            } else {
                return pos < p.pos ? -1 : 1;
            }
        }

        public z_Colors getColor() {
            return color;
        }

        public float getPosition() {
            return pos;
        }
    }

    protected TreeSet<GradPoint> gradient;

    protected float maxDither;

    protected m_i_InterpolateValue interpolator = new m_InterpolateValueLinear();

    /**
     * Constructs a new empty gradient.
     */
    public vc_ColorGradient() {
        gradient = new TreeSet<GradPoint>();
    }

    /**
     * Adds a new color at specified position.
     * 
     * @param p
     * @param c
     */
    public void addColorAt(float p, z_Colors c) {
        gradient.add(new GradPoint(p, c));
    }

    public vc_ColorList calcGradient() {
        float start = gradient.first().getPosition();
        return calcGradient(start,
                (int) (gradient.last().getPosition() - start));
    }

    /**
     * Calculates the gradient from specified position.
     * 
     * @param pos
     * @param width
     * @return list of interpolated gradient colors
     */
    public vc_ColorList calcGradient(float pos, int width) {
        vc_ColorList result = new vc_ColorList();

        if (gradient.size() == 0) {
            return result;
        }

        float frac = 0;
        GradPoint currPoint = null;
        GradPoint nextPoint = null;
        float endPos = pos + width;
        // find 1st color needed, clamp start position to positive values only
        for (GradPoint gp : gradient) {
            if (gp.pos < pos) {
                currPoint = gp;
            }
        }
        boolean isPremature = currPoint == null;
        TreeSet<GradPoint> activeGradient = null;
        if (!isPremature) {
            activeGradient = (TreeSet<GradPoint>) gradient.tailSet(currPoint);
        } else {
            // start position is before 1st gradient color, so use whole
            // gradient
            activeGradient = gradient;
            currPoint = activeGradient.first();
        }
        float currWidth = 0;
        Iterator<GradPoint> iter = activeGradient.iterator();
        if (currPoint != activeGradient.last()) {
            nextPoint = iter.next();
            if (isPremature) {
                float d = currPoint.pos - pos;
                currWidth = m_MathUtils.abs(d) > 0 ? 1f / d : 1;
            } else {
                if (nextPoint.pos - currPoint.pos > 0) {
                    currWidth = 1f / (nextPoint.pos - currPoint.pos);
                }
            }
        }
        while (pos < endPos) {
            if (isPremature) {
                frac = 1 - (currPoint.pos - pos) * currWidth;
            } else {
                frac = (pos - currPoint.pos) * currWidth;
            }
            // switch to next color?
            if (frac > 1.0) {
                currPoint = nextPoint;
                isPremature = false;
                if (iter.hasNext()) {
                    nextPoint = iter.next();
                    if (currPoint != activeGradient.last()) {
                        currWidth = 1f / (nextPoint.pos - currPoint.pos);
                    } else {
                        currWidth = 0;
                    }
                    frac = (pos - currPoint.pos) * currWidth;
                }
            }
            if (currPoint != activeGradient.last()) {
                float ditheredFrac = m_MathUtils
                        .clip(frac + m_MathUtils.normalizedRandom() * maxDither,
                                0f, 1f);
                ditheredFrac = interpolator.interpolate(0, 1, ditheredFrac);
                result.add(currPoint.color.blend(nextPoint.color,
                        ditheredFrac));
            } else {
                result.add(currPoint.color.copy());
            }
            pos++;
        }
        return result;
    }

    public List<GradPoint> getGradientPoints() {
        return new ArrayList<GradPoint>(gradient);
    }

    /**
     * @return the interpolator
     */
    public m_i_InterpolateValue getInterpolator() {
        return interpolator;
    }

    /**
     * @return the maximum dither amount.
     */
    public float getMaxDither() {
        return maxDither;
    }

    /**
     * @param interpolator
     *            the interpolator to set
     */
    public void setInterpolator(m_i_InterpolateValue interpolator) {
        this.interpolator = interpolator;
    }

    /**
     * Sets the maximum dither amount. Setting this to values >0 will jitter the
     * interpolated colors in the calculated gradient. The value range for this
     * parameter is 0.0 (off) to 1.0 (100%).
     * 
     * @param maxDither
     */
    public void setMaxDither(float maxDither) {
        this.maxDither = m_MathUtils.clip(maxDither, 0f, 1f);
    }
}
