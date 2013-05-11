package reuze.awt;

import com.software.reuze.z_Point2D;

/*
Copyright 2010 Laszlo Balazs-Csiki

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
 * A common interface for different types of motion blur
 */
 public interface ib_i_MotionBlur {
    /**
     * Specifies the angle of blur.
     *
     * @param angle the angle of blur.
     * @angle
     * @see #getAngle
     */
    void setAngle(float angle);

    /**
     * Returns the angle of blur.
     *
     * @return the angle of blur.
     * @see #setAngle
     */
    float getAngle();

    /**
     * Set the distance of blur.
     *
     * @param distance the distance of blur.
     * @see #getDistance
     */
    void setDistance(float distance);

    /**
     * Get the distance of blur.
     *
     * @return the distance of blur.
     * @see #setDistance
     */
    float getDistance();

    /**
     * Set the blur rotation.
     *
     * @param rotation the angle of rotation.
     * @see #getRotation
     */
    void setRotation(float rotation);

    /**
     * Get the blur rotation.
     *
     * @return the angle of rotation.
     * @see #setRotation
     */
    float getRotation();

    /**
     * Set the blur zoom.
     *
     * @param zoom the zoom factor.
     * @see #getZoom
     */
    void setZoom(float zoom);

    /**
     * Get the blur zoom.
     *
     * @return the zoom factor.
     * @see #setZoom
     */
    float getZoom();

    /**
     * Set the center of the effect in the X direction as a proportion of the image size.
     *
     * @param centreX the center
     * @see #getCentreX
     */
    void setCenterX(float centreX);

    /**
     * Get the center of the effect in the X direction as a proportion of the image size.
     *
     * @return the center
     * @see #setCentreX
     */
    float getCenterX();

    /**
     * Set the center of the effect in the Y direction as a proportion of the image size.
     *
     * @param centreY the center
     * @see #getCentreY
     */
    void setCenterY(float centreY);

    /**
     * Get the center of the effect in the Y direction as a proportion of the image size.
     *
     * @return the center
     * @see #setCentreY
     */
    float getCenterY();

    /**
     * Set the center of the effect as a proportion of the image size.
     *
     * @param centre the center
     * @see #getCentre
     */
    void setCenter(z_Point2D centre);

    /**
     * Get the center of the effect as a proportion of the image size.
     *
     * @return the center
     * @see #setCentre
     */
    z_Point2D getCenter();
}
