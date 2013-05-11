package reuze.pending;


import com.software.reuze.dg_i_AnimationLoop;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Keith
 */
public class WorldLoopAnimation implements dg_i_AnimationLoop{
	World world; 
	ViewShooter view;
	public WorldLoopAnimation(World world, ViewShooter view){
		this.world = world;
		this.view = view;
	}
	public void setSystemNanosAtStart(long nanos){
		world.setSystemNanosAtStart(nanos);
	}
	public void update(long nanosElapsed){
		world.update(nanosElapsed);
	}
	public void render(){
		view.render();
	}
}
