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

public class pma_ConstraintParticleRect implements pma_i_ConstraintParticle {

    protected ga_Rectangle rect;
    protected ga_Ray2D intersectRay;
    protected boolean isContainer;

    public pma_ConstraintParticleRect(ga_Rectangle rect) {
        this(rect, false);
    }

    public pma_ConstraintParticleRect(ga_Rectangle rect, boolean isContainer) {
        this.isContainer = isContainer;
        setBox(rect);
    }

    public pma_ConstraintParticleRect(ga_Vector2 min, ga_Vector2 max) {
        this(new ga_Rectangle(min, max), false);
    }

    public void apply(pma_ParticleVerlet p) {
        if (isContainer) {
            if (!rect.contains(p)) {
                rect.constrain(p);
            }
        } else if (rect.contains(p)) {
                p.set(rect.intersect(
                        intersectRay.setDirection(intersectRay.origin.tmp().sub(p)), 0,
                        Float.MAX_VALUE));
        }
    }

    public ga_Rectangle getBox() {
        return new ga_Rectangle(rect);   //TODO probably should not be a copy
    }

    public void setBox(ga_Rectangle rect) {
        this.rect = new ga_Rectangle(rect);
        this.intersectRay = new ga_Ray2D(rect.getCentroid(), new ga_Vector2());
    }
}
