package com.software.reuze;


public interface m_i_Noise extends d_i_PropertyFloat{
    /**
     * Computes the noise function value at point x.
     */
    public float noise(float x);
    
    /**
     * Computes 2D Simplex Noise.
     * 
     * @param x
     *            coordinate
     * @param y
     *            coordinate
     * @return noise value in range -1 ... +1.
     */
    public float noise(float x, float y);

    /**
     * Computes 3D Simplex Noise.
     * 
     * @param x
     *            coordinate
     * @param y
     *            coordinate
     * @param z
     *            coordinate
     * @return noise value in range -1 ... +1
     */
    public float noise(float x, float y, float z);


    /**
     * Computes 4D Simplex Noise.
     * 
     * @param x
     *            coordinate
     * @param y
     *            coordinate
     * @param z
     *            coordinate
     * @param w
     *            coordinate
     * @return noise value in range -1 ... +1
     */
    public float noise(float x, float y, float z, float w);
}
