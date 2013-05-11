package reuze.aifiles;

import processing.core.PApplet;

public final class asteroids extends PApplet {
	dg_GameSession game;
	dg_Control control;
	public void setup() {
		size(800,600);
		z_app.app=this;
		game=new dg_GameSession("");
		z_app.game=game;
		game.Init();
		control=new dg_ControlHuman(null);
		game.StartGame();
		frameRate(30);
	}
	public void draw() {
		background(0);
		if (game.m_state==dg_GameSession.States.STATE_PLAY) {
			game.Update(0.016f*game.m_timeScale);//lock at 60
		} else game.Update(0.016f);
		game.Draw();
	}
	public void keyTyped() {
 		control.Key(key, mouseX, mouseY);
	}
	public void keyPressed() {
		if (key == CODED) {
		    if (keyCode == UP) {
		    	control.Key(-1, mouseX, mouseY);
		    } else if (keyCode == DOWN) {
		    	control.Key(-2, mouseX, mouseY);
		    } else if (keyCode == RIGHT) {
		    	control.Key(-3, mouseX, mouseY);
		    } else if (keyCode == LEFT) {
		    	control.Key(-4, mouseX, mouseY);
		    }
		}
	}
	public void keyReleased() {
		if (key == CODED) {
		    if (keyCode == UP) {
		    	control.Key(-5, mouseX, mouseY);
		    } else if (keyCode == DOWN) {
		    	control.Key(-6, mouseX, mouseY);
		    } else if (keyCode == RIGHT) {
		    	control.Key(-7, mouseX, mouseY);
		    } else if (keyCode == LEFT) {
		    	control.Key(-8, mouseX, mouseY);
		    }
		}
	}
}
