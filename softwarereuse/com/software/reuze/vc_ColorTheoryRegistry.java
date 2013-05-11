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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Registry & object factory for default {@link vc_i_ColorTheoryStrategy}
 * implementations as well as custom ones.
 */
public class vc_ColorTheoryRegistry {

    private static final HashMap<String, vc_i_ColorTheoryStrategy> implementations = new HashMap<String, vc_i_ColorTheoryStrategy>();

    //public static final ColorTheoryStrategy SINGLE_COMPLEMENT = new SingleComplementStrategy();
    //public static final ColorTheoryStrategy COMPLEMENTARY = new ComplementaryStrategy();
    public static final vc_i_ColorTheoryStrategy SPLIT_COMPLEMENTARY = new vc_ColorTheoryStrategySplitComplementary();
    //public static final ColorTheoryStrategy LEFT_SPLIT_COMPLEMENTARY = new LeftSplitComplementaryStrategy();
    //public static final ColorTheoryStrategy RIGHT_SPLIT_COMPLEMENTARY = new RightSplitComplementaryStrategy();
   // public static final ColorTheoryStrategy ANALOGOUS = new AnalogousStrategy();
    public static final vc_i_ColorTheoryStrategy MONOCHROME = new vc_ColorTheoryStrategyMonochrome();
    //public static final ColorTheoryStrategy TRIAD = new TriadTheoryStrategy();
    //public static final ColorTheoryStrategy TETRAD = new TetradTheoryStrategy();
    //public static final ColorTheoryStrategy COMPOUND = new CompoundTheoryStrategy();

    static {
        Field[] fields = vc_ColorTheoryRegistry.class.getDeclaredFields();
        try {
            for (Field f : fields) {
                if (f.getType() == vc_i_ColorTheoryStrategy.class) {
                    String id = f.getName();
                    implementations.put(id, (vc_i_ColorTheoryStrategy) f.get(null));
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getRegisteredNames() {
        return new ArrayList<String>(implementations.keySet());
    }

    public static ArrayList<vc_i_ColorTheoryStrategy> getRegisteredStrategies() {
        return new ArrayList<vc_i_ColorTheoryStrategy>(implementations.values());
    }

    public static vc_i_ColorTheoryStrategy getStrategyForName(String id) {
        return implementations.get(id);
    }

    public static void registerImplementation(vc_i_ColorTheoryStrategy impl) {
        implementations.put(impl.getName().toUpperCase(), impl);
    }
}
