package com.software.reuze;
public class m_InterpolatePointBilinear implements m_i_InterpolatePoint {
	public m_InterpolatePointBilinear() {		
	}
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
    		float x2, float y2, float tl, float tr, float bl, float br) {
        double denom = 1.0 / ((x2 - x1) * (y2 - y1));
        double dx1 = (x - x1) * denom;
        double dx2 = (x2 - x) * denom;
        double dy1 = y - y1;
        double dy2 = y2 - y;
        return (float) (tl * dx2 * dy2 + tr * dx1 * dy2 + bl * dx2 * dy1 + br
                * dx1 * dy1);
    }

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
            float tr, float bl, float br) {
        return bilinear(p.x, p.y, p1.x, p1.y, p2.x, p2.y, tl, tr, bl, br);
    }
    public void set(String name, float value) { }
    public float getFloat(String name) {return Float.NaN;}
}
