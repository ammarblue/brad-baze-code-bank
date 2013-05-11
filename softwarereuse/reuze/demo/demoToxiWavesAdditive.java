package reuze.demo;
	/**
	 * <p>AdditiveWaves demo is showing how to add 2 randomly chosen waveforms
	 * to create a 3D terrain. One wave is moving along the X axis and is mapped
	 * to the red color channel, the other is propagating along Y and is mapped to
	 * blue. In this demo each wave's frequency is modulated by a secondary wave
	 * (here hardcoded as sine wave). Three of the possible waveforms chosen have
	 * additional options/special behaviour:
	 * 
	 * <ul><li>AMFMSineWave also modulates the wave's overall amplitude on top of
	 * frequency modulation</li>
	 * <li>FMHarmonicSquareWave's shape can tweaked by adjusting the number of harmonics
	 * used (the higher the more square-like the wave becomes).</li>
	 * <li>ConstantWave is simply representing a fixed value</li></ul>
	 * </p>
	 *
	 * <p>Currently available wave forms are:
	 * <ul><li>SineWave, FMSineWave, AMFMSineWave</li>
	 * <li>FMTriangleWave</li>
	 * <li>FMSawtoothWave</li>
	 * <li>FMSquareWave, FMHarmonicSquareWave</li>
	 * <li>ConstantWave</li></ul>
	 * 
	 * <p>For a demonstration how to use these wave generators as oscillators to
	 * synthesize audio samples, please have a look at the toxiclibs audioutils sub-package
	 * which is being distributed separately.</p>
	 * 
	 * <p>You can also create entirely new waveforms by subclassing the parent AbstractWave
	 * type and overwriting the update() method.</p>
	 * 
	 * <p>Usage: move mouse to rotate view, click to regenerate 2 random waves.</p>
	 */

	/* 
	 * Copyright (c) 2009 Karsten Schmidt
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
	 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
	 */
import com.software.reuze.m_WaveAMFMSine;
import com.software.reuze.m_WaveConstant;
import com.software.reuze.m_WaveFMHarmonicSquare;
import com.software.reuze.m_WaveFMSawtooth;
import com.software.reuze.m_WaveFMSine;
import com.software.reuze.m_WaveFMSquare;
import com.software.reuze.m_WaveFMTriangle;
import com.software.reuze.m_WaveSine;
import com.software.reuze.m_a_Wave;

import processing.core.PApplet;
public class demoToxiWavesAdditive extends PApplet {

	m_a_Wave waveX,waveY;

	int STEP = 10;
	int DIM = 800;
	int D2 = DIM/2;
	int AMP = 50;

	public void setup() {
	  size(1024, 576, P3D);
	  noStroke();
	  waveX=createRandomWave();
	  waveY=createRandomWave();
	  textFont(createFont("SansSerif", 10));
	}

	public void draw() {
	  background(0);
	  lights();
	  pushMatrix();
	  translate(width*0.5f, height*0.4f, 0);
	  rotateX(0.8f);
	  rotateZ(mouseX*0.01f);
	  scale(0.5f);
	  float prevY = waveY.update();
	  float colPrevY = 0;
	  waveY.push();
	  for(int y = 0; y < DIM; y += STEP) {
	    float valueY = waveY.update();
	    float colY = valueY * 128 + 128;
	    waveX.push();
	    beginShape(TRIANGLE_STRIP);
	    for(int x = 0; x < DIM; x += STEP) {
	      float valueX = waveX.update();
	      float colX = valueX * 128 + 128;
	      fill(colX, 0, colPrevY);
	      vertex(x - D2, y - STEP - D2, (valueX + prevY) * AMP);
	      fill(colX, 0, colY);
	      vertex(x - D2, y - D2, (valueX + valueY) * AMP);
	    }
	    endShape();
	    waveX.pop();
	    prevY = valueY;
	    colPrevY = colY;
	  }
	  waveY.pop();
	  waveX.update();
	  waveY.update();
	  popMatrix();
	  fill(255);
	  text(waveX.getClass().getName(), 20, 30);
	  text(waveY.getClass().getName(), 20, 42);
	}

	public void mousePressed() {
	  waveX=createRandomWave();
	  waveY=createRandomWave();
	}

	m_a_Wave createRandomWave() {
	  m_a_Wave w=null;
	  m_a_Wave fmod=new m_WaveSine(0, random(0.005f, 0.02f), random(0.1f, 0.5f), 0);
	  float freq=random(0.005f, 0.05f);
	  switch((int)random(7)) {
	  case 0:
	    w = new m_WaveFMTriangle(0, freq, 1, 0, fmod);
	    break;
	  case 1:
	    w = new m_WaveFMSawtooth(0, freq, 1, 0, fmod);
	    break;
	  case 2:
	    w = new m_WaveFMSquare(0, freq, 1, 0, fmod);
	    break;
	  case 3:
	    w = new m_WaveFMHarmonicSquare(0, freq, 1, 0, fmod);
	    ((m_WaveFMHarmonicSquare)w).maxHarmonics=(int)random(3,30);
	    break;
	  case 4:
	    w = new m_WaveFMSine(0, freq, 1, 0, fmod);
	    break;
	  case 5:
	    w = new m_WaveAMFMSine(0, freq, 0, fmod, new m_WaveSine(0, random(0.01f,0.2f), random(2f, 3f), 0));
	    break;
	  case 6:
	    w = new m_WaveConstant(random(-1,1));
	    break;
	  }
	  return w;
	}

}
