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


import java.util.Comparator;


/**
 * Defines standard color component access criteria and associated comparators
 * used to sort colors based on component values. If a new custom accessor is
 * needed (e.g. for sub-classes TColor's), then simply sub-class this class and
 * implement the {@link Comparator} interface and the 2 abstract getter & setter
 * methods defined by this class.
 */
public abstract class vc_a_CriteriaAccess implements Comparator<z_Colors> {

    public static final vc_a_CriteriaAccess HUE = new vc_CriteriaAccessHSV(0);
    public static final vc_a_CriteriaAccess SATURATION = new vc_CriteriaAccessHSV(1);
    public static final vc_a_CriteriaAccess BRIGHTNESS = new vc_CriteriaAccessHSV(2);

    public static final vc_a_CriteriaAccess RED = new vc_CriteriaAccessRGB(0);
    public static final vc_a_CriteriaAccess GREEN = new vc_CriteriaAccessRGB(1);
    public static final vc_a_CriteriaAccess BLUE = new vc_CriteriaAccessRGB(2);

    public static final vc_a_CriteriaAccess CYAN = new vc_CriteriaAccessCMYK(0);
    public static final vc_a_CriteriaAccess MAGENTA = new vc_CriteriaAccessCMYK(1);
    public static final vc_a_CriteriaAccess YELLOW = new vc_CriteriaAccessCMYK(2);
    public static final vc_a_CriteriaAccess BLACK = new vc_CriteriaAccessCMYK(3);

    public static final vc_a_CriteriaAccess ALPHA = new vc_AlphaOpCompare();
    public static final vc_a_CriteriaAccess LUMINANCE = new vc_CriteriaAccessLuminance();

    public abstract float getComponentValueFor(z_Colors col);

    public abstract void setComponentValueFor(z_Colors col, float value);//TODO
}
