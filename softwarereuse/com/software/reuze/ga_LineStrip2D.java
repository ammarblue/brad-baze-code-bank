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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

//import javax.xml.bind.annotation.XmlElement;


public class ga_LineStrip2D implements Iterable<ga_Vector2> {

    //@XmlElement(name = "v")
    protected List<ga_Vector2> vertices = new ArrayList<ga_Vector2>();

    protected float[] arcLenIndex;

    public ga_LineStrip2D() {
    }

    public ga_LineStrip2D(Collection<? extends ga_Vector2> vertices) {
        this.vertices = new ArrayList<ga_Vector2>(vertices);
    }

    public ga_LineStrip2D add(float x, float y) {
        vertices.add(new ga_Vector2(x, y));
        return this;
    }

    public ga_LineStrip2D add(ga_Vector2 p) {
        vertices.add(p.copy());
        return this;
    }

    /**
     * Returns the vertex at the given index. This function follows Python
     * convention, in that if the index is negative, it is considered relative
     * to the list end. Therefore the vertex at index -1 is the last vertex in
     * the list.
     * 
     * @param i
     *            index
     * @return vertex
     */
    public ga_Vector2 get(int i) {
        if (i < 0) {
            i += vertices.size();
        }
        return vertices.get(i);
    }
    
    public ga_Vector2 getCentroid() {
        int num = vertices.size();
        if (num > 0) {
        	ga_Vector2 centroid = new ga_Vector2();
            for (ga_Vector2 v : vertices) {
                centroid.add(v);
            }
            return centroid.mul(1f / num);
        }
        return null;
    }

    /**
     * Computes a list of points along the spline which are uniformly separated
     * by the given step distance.
     * 
     * @param step
     * @return point list
     */
    public List<ga_Vector2> getDecimatedVertices(float step) {
        return getDecimatedVertices(step, true);
    }

    /**
     * Computes a list of points along the spline which are close to uniformly
     * separated by the given step distance. The uniform distribution is only an
     * approximation and is based on the estimated arc length of the polyline.
     * The distance between returned points might vary in places, especially if
     * there're sharp angles between line segments.
     * 
     * @param step
     * @param doAddFinalVertex
     *            true, if the last vertex computed should be added regardless
     *            of its distance.
     * @return point list
     */
    public List<ga_Vector2> getDecimatedVertices(float step, boolean doAddFinalVertex) {
        ArrayList<ga_Vector2> uniform = new ArrayList<ga_Vector2>();
        if (vertices.size() < 3) {
            if (vertices.size() == 2) {
                new ga_Line2D(vertices.get(0), vertices.get(1)).splitIntoSegments(
                        uniform, step, true);
                if (!doAddFinalVertex) {
                    uniform.remove(uniform.size() - 1);
                }
            } else {
                return null;
            }
        }
        float arcLen = getLength();
        if (arcLen > 0) {
            double delta = step / arcLen;
            int currIdx = 0;
            for (double t = 0; t < 1.0; t += delta) {
                double currT = t * arcLen;
                while (currT >= arcLenIndex[currIdx]) {
                    currIdx++;
                }
                ga_Vector2 p = vertices.get(currIdx - 1);
                ga_Vector2 q = vertices.get(currIdx);
                float frac = (float) ((currT - arcLenIndex[currIdx - 1]) / (arcLenIndex[currIdx] - arcLenIndex[currIdx - 1]));
                ga_Vector2 i = p.interpolateTo(q, frac);
                uniform.add(i);
            }
            if (doAddFinalVertex) {
            	uniform.add(vertices.get(vertices.size() - 1).copy());
            }
        }
        return uniform;
    }

    public float getLength() {
        if (arcLenIndex == null
                || (arcLenIndex != null && arcLenIndex.length != vertices
                        .size())) {
            arcLenIndex = new float[vertices.size()];
        }
        float arcLen = 0;
        for (int i = 1; i < arcLenIndex.length; i++) {
            ga_Vector2 p = vertices.get(i - 1);
            ga_Vector2 q = vertices.get(i);
            arcLen += p.dst(q);
            arcLenIndex[i] = arcLen;
        }
        return arcLen;
    }
    
    /**
     * Computes point at position t, where t is the normalized position along
     * the strip. If t&lt;0 then the first vertex of the strip is returned. If
     * t&gt;=1.0 the last vertex is returned. If the strip contains less than 2
     * vertices, this method returns null.
     * 
     * @param t
     * @return
     */
    public ga_Vector2 getPointAt(float t) {
        int num = vertices.size();
        if (num > 1) {
            if (t <= 0.0) {
                return vertices.get(0);
            } else if (t >= 1.0) {
                return vertices.get(num - 1);
            }
            float totalLength = this.getLength();
            double offp = 0, offq = 0;
            for (int i = 1; i < num; i++) {
            	ga_Vector2 p = vertices.get(i - 1);
            	ga_Vector2 q = vertices.get(i);
                offq += q.dst(p) / totalLength;
                if (offp <= t && offq >= t) {
                    return p.interpolateTo(q, (float) m_MathUtils.mapInterval(t,
                            offp, offq, 0.0, 1.0));
                }
                offp = offq;
            }
        }
        return null;
    }
    
    public List<ga_Line2D> getSegments() {
        final int num = vertices.size();
        List<ga_Line2D> segments = new ArrayList<ga_Line2D>(num - 1);
        for (int i = 1; i < num; i++) {
            segments.add(new ga_Line2D(vertices.get(i - 1), vertices.get(i)));
        }
        return segments;
    }

    /**
     * @return the vertices
     */
    public List<ga_Vector2> getVertices() {
        return vertices;
    }

    public ga_Line2D.LineIntersection intersectLine(ga_Line2D line) {
        ga_Line2D l = new ga_Line2D(new ga_Vector2(), new ga_Vector2());
        for (int i = 1, num = vertices.size(); i < num; i++) {
            l.set(vertices.get(i - 1), vertices.get(i));
            ga_Line2D.LineIntersection isec = l.intersectLine(line);
            if (isec.getType() == ga_Line2D.LineIntersection.Type.INTERSECTING
                    || isec.getType() == ga_Line2D.LineIntersection.Type.COINCIDENT) {
                return isec;
            }
        }
        return null;
    }

    public Iterator<ga_Vector2> iterator() {
        return vertices.iterator();
    }
    
    public ga_LineStrip2D rotate(float theta) {
        for (ga_Vector2 v : vertices) {
            v.rotate(theta, true);
        }
        return this;
    }

    public ga_LineStrip2D scale(float scale) {
        return scale(scale, scale);
    }

    public ga_LineStrip2D scale(float x, float y) {
        for (ga_Vector2 v : vertices) {
            v.mul(x,y);
        }
        return this;
    }

    public ga_LineStrip2D scale(final ga_Vector2 scale) {
        return scale(scale.x, scale.y);
    }
    
    public ga_LineStrip2D scaleSize(float scale) {
        return scaleSize(scale, scale);
    }

    public ga_LineStrip2D scaleSize(float x, float y) {
        ga_Vector2 centroid = getCentroid();
        for (ga_Vector2 v : vertices) {
            v.sub(centroid).mul(x, y).add(centroid);
        }
        return this;
    }

    public ga_LineStrip2D scaleSize(final ga_Vector2 scale) {
        return scaleSize(scale.x, scale.y);
    }
    
    public ga_LineStrip2D translate(float x, float y) {
        for (ga_Vector2 v : vertices) {
            v.add(x, y);
        }
        return this;
    }

    public ga_LineStrip2D translate(final ga_Vector2 offset) {
        return translate(offset.x, offset.y);
    }

    /**
     * @param vertices
     *            the vertices to set
     */
    public void setVertices(List<ga_Vector2> vertices) {
        this.vertices = vertices;
    }
}
