package com.software.reuze;
import com.software.reuze.ga_Vector2;
public interface m_i_InterpolatePoint extends d_i_PropertyFloat {
    
    /**
     * @param x
     *            x coord of point to filter
     * @param y
     *            y coord of point to filter
     * @param x1
     *            x coord of top-left corner
     * @param y1
     *            y coord of top-left corner
     * @param x2
     *            x coord of bottom-right corner
     * @param y2
     *            y coord of bottom-right corner
     * @param tl
     *            top-left value
     * @param tr
     *            top-right value
     * @param bl
     *            bottom-left value
     * @param br
     *            bottom-right value
     * @return interpolated value
     */
    public float bilinear(float x, float y, float x1, float y1,
    		float x2, float y2, float tl, float tr, float bl, float br);

    /**
     * @param p
     *            point to filter
     * @param p1
     *            top-left corner
     * @param p2
     *            bottom-right corner
     * @param tl
     *            top-left value
     * @param tr
     *            top-right value
     * @param bl
     *            bottom-left value
     * @param br
     *            bottom-right value
     * @return interpolated value
     */
    public float bilinear(ga_Vector2 p, ga_Vector2 p1, ga_Vector2 p2, float tl,
            float tr, float bl, float br);

}
