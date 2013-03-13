import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;




public class STAR_ANIMATION{
	static Timer timer = new Timer();
	static Random rand = new Random();
	static int max_num_of_stars_FRONT = 20*GAME_FRAME.SCALE;
	static int max_num_of_stars_BACK = 50*GAME_FRAME.SCALE;
	static int max_star_size_FRONT = 3;
	static int max_star_size_BACK = 2;
	static int[] xLoc_FRONT = new int[max_num_of_stars_FRONT];
	static int[] yLoc_FRONT = new int[max_num_of_stars_FRONT];
	static int[] xLoc_BACK = new int[max_num_of_stars_BACK];
	static int[] yLoc_BACK = new int[max_num_of_stars_BACK];
	static int[] size_FRONT = new int[max_num_of_stars_FRONT];
	static int[] size_BACK = new int[max_num_of_stars_BACK];
	static int[] color_FRONT = new int[max_num_of_stars_FRONT];
	static int[] color_BACK = new int[max_num_of_stars_BACK];
	static int twinkle_star_Loc_FRONT;
	static int twinkle_star_Loc_BACK;
	
	
	static TimerTask star_movement_FRONT = new TimerTask(){
		public void run() {
			//make stars move left
			for(int i = 0; i < max_num_of_stars_FRONT; i++){
				if(xLoc_FRONT[i] > GAME_FRAME.WIDTH ){
					xLoc_FRONT[i] = 0;
				}else{
					xLoc_FRONT[i]+=1;
				}
				
				if(yLoc_FRONT[i] > 0){
					yLoc_FRONT[i]-=1;
				}else{
					yLoc_FRONT[i]=GAME_FRAME.HEIGHT ;
				}
				
			}
			
		}
	};
	//star movement animatino for back stars
	static TimerTask star_movement_BACK = new TimerTask(){
		public void run() {
			//make stars move left
			for(int i = 0; i < max_num_of_stars_BACK; i++){
				if(xLoc_BACK[i] > GAME_FRAME.WIDTH ){
					xLoc_BACK[i] = 0;
				}else{
					xLoc_BACK[i]+=1;
				}
				
				if(yLoc_BACK[i] > 0){
					yLoc_BACK[i]
							-=1;
				}else{
					yLoc_BACK[i]=GAME_FRAME.HEIGHT ;
				}
				
			}
			
		}
	};
	
	static TimerTask star_twinkle = new TimerTask(){
		public void run() {
			twinkle_star_Loc_FRONT = rand.nextInt(max_num_of_stars_FRONT);
			twinkle_star_Loc_BACK = rand.nextInt(max_num_of_stars_BACK);
			
		}
	};
	public static void generateLocationArrays(){
		for (int i = 0; i < max_num_of_stars_FRONT; i++){
			xLoc_FRONT[i] = rand.nextInt(GAME_FRAME.WIDTH );
		}
		for (int i = 0; i < max_num_of_stars_FRONT; i++){
			yLoc_FRONT[i] = rand.nextInt(GAME_FRAME.HEIGHT );
		}
		for (int i = 0; i < max_num_of_stars_FRONT; i++){
			size_FRONT[i] = rand.nextInt(max_star_size_FRONT);
		}
		for (int i = 0; i < max_num_of_stars_FRONT; i++){
			color_FRONT[i] = rand.nextInt(2);
		}
		
		for (int i = 0; i < max_num_of_stars_BACK; i++){
			xLoc_BACK[i] = rand.nextInt(GAME_FRAME.WIDTH );
		}
		for (int i = 0; i < max_num_of_stars_BACK; i++){
			yLoc_BACK[i] = rand.nextInt(GAME_FRAME.HEIGHT );
		}
		for (int i = 0; i < max_num_of_stars_BACK; i++){
			size_BACK[i] = rand.nextInt(max_star_size_BACK);
		}
		for (int i = 0; i < max_num_of_stars_BACK; i++){
			color_BACK[i] = rand.nextInt(2);
		}
	}
	public static void ANIMATE_STARS(){
		generateLocationArrays();
		star_twinkle = new TimerTask(){
			public void run() {
				twinkle_star_Loc_FRONT = rand.nextInt(max_num_of_stars_FRONT);
				twinkle_star_Loc_BACK = rand.nextInt(max_num_of_stars_BACK);
				
			}
		};
		star_movement_FRONT = new TimerTask(){
			public void run() {
				//make stars move left
				for(int i = 0; i < max_num_of_stars_FRONT; i++){
					if(xLoc_FRONT[i] > GAME_FRAME.WIDTH ){
						xLoc_FRONT[i] = 0;
					}else{
						xLoc_FRONT[i]+=1;
					}
					
					if(yLoc_FRONT[i] > 0){
						yLoc_FRONT[i]-=1;
					}else{
						yLoc_FRONT[i]=GAME_FRAME.HEIGHT ;
					}
					
				}
				
			}
		};
		star_movement_BACK = new TimerTask(){
			public void run() {
				//make stars move left
				for(int i = 0; i < max_num_of_stars_BACK; i++){
					if(xLoc_BACK[i] > GAME_FRAME.WIDTH ){
						xLoc_BACK[i] = 0;
					}else{
						xLoc_BACK[i]+=1;
					}
					
					if(yLoc_BACK[i] > 0){
						yLoc_BACK[i]
								-=1;
					}else{
						yLoc_BACK[i]=GAME_FRAME.HEIGHT ;
					}
					
				}
				
			}
		};
		timer.schedule(star_twinkle, 0, 120);
		timer.schedule(star_movement_FRONT, 0, 30);
		timer.schedule(star_movement_BACK, 0, 40);
	}
	public static void CANCEL_ANIMATE_STARS(){
		timer.purge();
		timer.cancel();
		
		star_twinkle.cancel();
		star_movement_FRONT.cancel();
		star_movement_BACK.cancel();
		
	}
	
	public static void RENDER_STARS(Graphics g){
		
		for(int i = 0; i < max_num_of_stars_FRONT; i++){
			if (color_FRONT[i] == 0){
				g.setColor(Color.WHITE);
				g.fillRect(xLoc_FRONT[i], yLoc_FRONT[i], size_FRONT[i], size_FRONT[i]);
			}else{
				g.setColor(new Color(84,84,84));
				g.fillRect(xLoc_FRONT[i], yLoc_FRONT[i], size_FRONT[i], size_FRONT[i]);
			}
		
		}
		for(int i = 0; i < max_num_of_stars_BACK; i++){
			if (color_BACK[i] == 0){
				g.setColor(Color.WHITE);
				g.fillRect(xLoc_BACK[i], yLoc_BACK[i], size_BACK[i], size_BACK[i]);
			}else{
				g.setColor(new Color(84,84,84));
				g.fillRect(xLoc_BACK[i], yLoc_BACK[i], size_BACK[i], size_BACK[i]);
			}
		
		
		}
		g.setColor(Color.WHITE);
		g.fillRect(xLoc_FRONT[twinkle_star_Loc_FRONT], yLoc_FRONT[twinkle_star_Loc_FRONT], size_FRONT[twinkle_star_Loc_FRONT], size_FRONT[twinkle_star_Loc_FRONT]);
		g.fillRect(xLoc_BACK[twinkle_star_Loc_BACK], yLoc_BACK[twinkle_star_Loc_BACK], size_BACK[twinkle_star_Loc_BACK], size_BACK[twinkle_star_Loc_BACK]);
	}
	
}
