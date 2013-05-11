package com.software.reuze;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Keith
 */
public interface dg_i_AnimationLoop {
	public void setSystemNanosAtStart(long nanos);
	public void update(long nanosElapsed);
	public void render();
}
