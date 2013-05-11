package reuze.pending;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.swing.JApplet;

/**
 *
 * @author Keith
 */
public class AppletImpl extends JApplet{
	demoGameShooterApplet mainApplet;
	public AppletImpl(){
		this.mainApplet = new demoGameShooterApplet(this);
	}
	public void init(){
		mainApplet.init();
	}
	public void start(){
		mainApplet.start();
	}
	public void stop(){
		mainApplet.stop();
	}
	public void destroy(){
		mainApplet.destroy();
	}
}
