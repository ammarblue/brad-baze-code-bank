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

/**
 * <p>
 * Amplitude and frequency modulated sine wave. Uses 2 secondary waves to
 * modulate the shape of the main wave.
 * </p>
 * 
 * <p>
 * <strong>Note:</strong> You must NEVER call the update() method on the
 * modulating waves.
 * </p>
 */
public class m_WaveAMFMSine extends m_a_Wave {

    public m_a_Wave fmod;
    public m_a_Wave amod;

    /**
     * Creates a new instance from
     * 
     * @param phase
     * @param freq
     * @param fmod
     * @param amod
     */
    public m_WaveAMFMSine(float phase, float freq, m_a_Wave fmod,
            m_a_Wave amod) {
        super(phase, freq);
        this.amod = amod;
        this.fmod = fmod;
    }

    /**
     * @param phase
     * @param freq
     * @param offset
     * @param fmod
     * @param amod
     */
    public m_WaveAMFMSine(float phase, float freq, float offset,
            m_a_Wave fmod, m_a_Wave amod) {
        super(phase, freq, 1, offset);
        this.amod = amod;
        this.fmod = fmod;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.math.waves.AbstractWave#pop()
     */
    @Override
    public void pop() {
        super.pop();
        amod.pop();
        fmod.pop();
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.math.waves.AbstractWave#push()
     */
    @Override
    public void push() {
        super.push();
        amod.push();
        fmod.push();
    }

    /**
     * Resets this wave and its modulating waves as well.
     * 
     * @see m_a_Wave.math.waves.AbstractWave#reset()
     */
    public void reset() {
        super.reset();
        fmod.reset();
        amod.reset();
    }

    /**
     * Progresses the wave and updates the result value. You must NEVER call the
     * update() method on the 2 modulating wave since this is handled
     * automatically by this method.
     * 
     * @see m_a_Wave.math.waves.AbstractWave#update()
     */
    public float update() {
        amp = amod.update();
        value = amp * (float) Math.sin(phase) + offset;
        cyclePhase(frequency + fmod.update());
        return value;
    }
}
