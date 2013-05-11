package com.software.reuze;
import com.software.reuze.gb_Vector3;

/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class m_InterpolateLerp {
    
    public static float lerp(float start, float target, float duration, float timeSinceStart)
    {
        float value = start;
        if (timeSinceStart > 0.0f && timeSinceStart < duration)
        {
            final float range = target - start;
            final float percent = timeSinceStart / duration;
            value = start + (range * percent);
        }
        else if (timeSinceStart >= duration)
        {
            value = target;
        }
        return value;
    }

    public static float ease(float start, float target, float duration, float timeSinceStart)
    {
        float value = start;
        if (timeSinceStart > 0.0f && timeSinceStart < duration)
        {
            final float range = target - start;
            final float percent = timeSinceStart / (duration / 2.0f);
            if (percent < 1.0f)
            {
                value = start + ((range / 2.0f) * percent * percent * percent);
            }
            else
            {
                final float shiftedPercent = percent - 2.0f;
                value = start + ((range / 2.0f) * 
                        ((shiftedPercent * shiftedPercent * shiftedPercent) + 2.0f));
            }
        }
        else if (timeSinceStart >= duration)
        {
            value = target;
        }
        return value;
    }
    
	/**
	 * Linear interpolation.
	 * @param t the interpolation parameter
	 * @param a the lower interpolation range
	 * @param b the upper interpolation range
	 * @return the interpolated value
	 */
	public static float lerp(float t, float a, float b) {
		return a + t * (b - a);
	}
	public static double lerp(double t, double a, double b) {
		return a + t * (b - a);
	}
	
	/**
	 * Linear interpolation.
	 * @param t the interpolation parameter
	 * @param a the lower interpolation range
	 * @param b the upper interpolation range
	 * @return the interpolated value
	 */
	public static int lerp(float t, int a, int b) {
		return (int)(a + t * (b - a));
	}
	//TODO move to Vector3
	public static gb_Vector3 lerp(float t, gb_Vector3 a, gb_Vector3 b)
	{
		  return new gb_Vector3( lerp(t,a.x,b.x),lerp(t,a.y,b.y),lerp(t,a.z,b.z));
	}
}
