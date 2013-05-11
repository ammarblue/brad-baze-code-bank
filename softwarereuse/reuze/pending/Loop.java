package reuze.pending;

import com.software.reuze.dg_i_AnimationLoop;
import com.software.reuze.pt_TimeCounter;

/*
 * Copyright (c) 2008, Keith Woodward
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of Keith Woodward nor the names
 *    of its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */


//import straightedge.geom.util.CodeTimer;

/**
 *
 * @author Keith
 */
public class Loop extends Thread{
	public demoGameShooterMain main;
	public volatile boolean keepRunning = true;
	int sleepMillis = 1;
	public pt_TimeCounter fpsCounter;
	public dg_i_AnimationLoop animation;
	public volatile boolean restartAnimation = false;
	dg_i_AnimationLoop newAnimation;
	Object mutex = new Object();

	public Loop(demoGameShooterMain main){
		this.setName("Loop");
		this.main = main;
		fpsCounter = new pt_TimeCounter();
	}

	public void run(){
		OuterLoop:
		while (keepRunning){
			synchronized (mutex){
				restartAnimation = false;
				animation = newAnimation;
				newAnimation = null;
			}
			long startNanos = System.nanoTime();
//			System.out.println(this.getClass().getSimpleName()+": startNanos == "+startNanos);
			animation.setSystemNanosAtStart(startNanos);
			long lastUpdateNanos = startNanos;
//			CodeTimer codeTimer = new CodeTimer("loop.run", CodeTimer.Output.Millis, CodeTimer.Output.Millis);
//			codeTimer.setEnabled(true);
			InnerLoop:
			while(keepRunning){
				long currentNanos = System.nanoTime();
//				System.out.println(this.getClass().getSimpleName()+": currentNanos == "+currentNanos+", anim == "+animation);
				long nanosElapsed = currentNanos - lastUpdateNanos;
//				codeTimer.click("update");
				animation.update(nanosElapsed);
				if (restartAnimation == true){
					continue OuterLoop;
				}
				fpsCounter.update();
//				codeTimer.click("render");
				animation.render();
//				codeTimer.click("sleep");
				Thread.yield();
				try{ Thread.sleep(sleepMillis); }catch(Exception e){}
				lastUpdateNanos = currentNanos;
//				codeTimer.lastClick();
			}
		}
	}

	public void setAnimationAndRestart(dg_i_AnimationLoop newAnimation, int sleepMillis){
		synchronized (mutex){
			this.newAnimation = newAnimation;
			this.sleepMillis = sleepMillis;
			this.restartAnimation = true;
		}
	}
	public void setAnimationAndRestart(dg_i_AnimationLoop newAnimation){
		setAnimationAndRestart(newAnimation, 1);
	}

	public void close(){
		keepRunning = false;
	}
}
