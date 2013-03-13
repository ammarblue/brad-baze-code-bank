import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;


public class TRANSITION1 {
	static int xChange = 0;
	static int yChange = 0;
	static Timer timer = new Timer();
	static int t0_initialX = -TRANSITION1_IMAGES.T1I[0].getWidth(null);
	static int t0_initialY = GAME_FRAME.HEIGHT ;
	
	static int t1_initialX = -TRANSITION1_IMAGES.T1I[1].getWidth(null);
	static int t1_initialY = -TRANSITION1_IMAGES.T1I[1].getHeight(null);
	
	static int t2_initialX = GAME_FRAME.WIDTH;
	static int t2_initialY = -TRANSITION1_IMAGES.T1I[2].getHeight(null);
	
	static int t3_initialX = GAME_FRAME.WIDTH;
	static int t3_initialY = GAME_FRAME.HEIGHT;
	
	static double t0_X = t0_initialX;
	static double t0_Y = t0_initialY;
	
	static double t1_X = t1_initialX;
	static double t1_Y = t1_initialY;
	
	static double t2_X = t2_initialX;
	static double t2_Y = t2_initialY;
	
	static double t3_X = t3_initialX;
	static double t3_Y = t3_initialY;
	static TimerTask close = new TimerTask(){
		public void run() {	
		}
	};
	static TimerTask open = new TimerTask(){
		public void run() {
		}
	};
	static TimerTask loadPanel = new TimerTask(){
		public void run() {
		}
	};
	public static void ANIMATE_TRANSITIION1(final JPanel panel){
		t0_X = t0_initialX;
		t0_Y = t0_initialY;
		t1_X = t1_initialX;
		t1_Y = t1_initialY;
		t2_X = t2_initialX;
		t2_Y = t2_initialY;
		t3_X = t3_initialX;
		t3_Y = t3_initialY;
		
		close.cancel();
		open.cancel();
		close = new TimerTask(){
			public void run() {
				if(t0_X != 0){
					t0_X++;
				}
				if(t0_Y != GAME_FRAME.HEIGHT - TRANSITION1_IMAGES.T1I[0].getHeight(null)){
					t0_Y--;
				}
				if(t1_X != 0){
					t1_X++;
				}
				if(t1_Y != 0){
					t1_Y++;
				}
				if(t2_X != GAME_FRAME.WIDTH - TRANSITION1_IMAGES.T1I[2].getWidth(null)){
					t2_X--;
				}
				if(t2_Y != 0){
					t2_Y++;
				}
				if(t3_X != GAME_FRAME.WIDTH - TRANSITION1_IMAGES.T1I[3].getWidth(null)){
					t3_X--;
				}
				if(t3_Y != GAME_FRAME.HEIGHT - TRANSITION1_IMAGES.T1I[3].getHeight(null)){
					t3_Y--;
				}
				
				
			}
		};
		open = new TimerTask(){
			public void run() {
				close.cancel();
				if(t0_X != t0_initialX){
					t0_X--;
				}
				if(t0_Y != t0_initialY){
					t0_Y++;
				}
				if(t1_X != t1_initialX){
					t1_X--;
				}
				if(t1_Y != t1_initialY){
					t1_Y--;
				}
				if(t2_X != t2_initialX){
					t2_X++;
				}
				if(t2_Y != t2_initialY){
					t2_Y--;
				}
				if(t3_X != t3_initialX){
					t3_X++;
				}
				if(t3_Y != t3_initialY){
					t3_Y++;
				}
			}
		};
		//TimerTask loadPanel = new TimerTask(){
			//public void run() {
				//GAME_FRAME.displayPanel.removeAll();
            	//GAME_FRAME.displayPanel.add(panel);
			//}
		//};
		timer.schedule(close, 0, 6/GAME_FRAME.SCALE);
		timer.schedule(open, 2000, 3/GAME_FRAME.SCALE);
		
	}
	
	public static void RENDER_TRANSITION1(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(TRANSITION1_IMAGES.T1I[4], (int)t3_X-TRANSITION1_IMAGES.T1I[4].getWidth(null), (int)t3_Y+TRANSITION1_IMAGES.T1I[3].getHeight(null)-TRANSITION1_IMAGES.T1I[4].getHeight(null), null);
		g2d.drawImage(TRANSITION1_IMAGES.T1I[0], (int)t0_X, (int)t0_Y, null);
		g2d.drawImage(TRANSITION1_IMAGES.T1I[1], (int)t1_X, (int)t1_Y, null);
		g2d.drawImage(TRANSITION1_IMAGES.T1I[2], (int)t2_X, (int)t2_Y, null);
		g2d.drawImage(TRANSITION1_IMAGES.T1I[3], (int)t3_X, (int)t3_Y, null);
	}
}
