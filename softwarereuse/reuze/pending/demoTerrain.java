package reuze.pending;

import processing.core.PApplet;
import processing.core.PImage;

public class demoTerrain extends PApplet {
	/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/16704*@* */
	/* !do not delete the line above, required for linking your tweak if you re-upload */
	/**
	 * binarymillenium 
	 * November-December 2010
	 * GNU GPL v3 
	 *
	 * 'w','a','s','d' to scroll the landscape.
	 * 
	 * Art from http://opengameart.org/content/isometric-64x64-outside-tileset
	 * Yar
	 * CC-BY 3.0
	 * Updated by bobcgausa 2012 to fix water and add ijkl to move from tile to tile
	 */ 


	PImage tiles[];
	PImage dec[],dec12[],dec13[],dec33[];
	PImage elev[];
	PImage slope[];
	PImage water[];


	int[] dec_names = {66,67,68,69,70,110,111,112,113,114,115,116,117,118,119,
			120,121,124,125,126,127,128,129};

	int[] dec12_names = {122,123,142,143}; //1x2 top-down
	int[] dec13_names = {130,131}; //1x3 top-down
	int[] dec33_names = {134,137}; //3x3 top-down

	int[] elev_names = {34,54,55,60,61,62,63,64,65,71,72,73,74,75,76,77,78,79};

	int[] slope_names = {30,31,32,33,35,36,37,38,40,41,42,43,50,51,52,53,56,57,58,59};

	int[] tile_names = {0,1,2,3,4,5,6,10,11,12,13,14,15,16,17,20,21,22,23};

	int[] water_names = {80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102};

	public void setup() {
		size(940,640);
		PImage img = loadImage("iso-64x64-outside.png");
		elev = new PImage[elev_names.length];
		for (int i=0; i<elev_names.length; i++) elev[i]=img.get(elev_names[i]%10*64,elev_names[i]/10*64,64,64);
		slope = new PImage[slope_names.length];
		for (int i=0; i<slope_names.length; i++) slope[i]=img.get(slope_names[i]%10*64,slope_names[i]/10*64,64,64);
		tiles = new PImage[tile_names.length];
		for (int i=0; i<tile_names.length; i++) tiles[i]=img.get(tile_names[i]%10*64,tile_names[i]/10*64,64,64);
		dec = new PImage[dec_names.length];
		for (int i=0; i<dec_names.length; i++) dec[i]=img.get(dec_names[i]%10*64,dec_names[i]/10*64,64,64);
		dec33=new PImage[dec33_names.length];
		for (int i=0; i<dec33_names.length; i++) dec33[i]=img.get(dec33_names[i]%10*64,dec33_names[i]/10*64,64*3,64*3);
		dec13=new PImage[dec13_names.length];
		for (int i=0; i<dec13_names.length; i++) dec13[i]=img.get(dec13_names[i]%10*64,dec13_names[i]/10*64,64,64*3);
		dec12=new PImage[dec12_names.length];
		for (int i=0; i<dec12_names.length; i++) dec12[i]=img.get(dec12_names[i]%10*64,dec12_names[i]/10*64,64,64*2);
		water = new PImage[water_names.length];
		for (int i=0; i<water_names.length; i++) water[i]=img.get(water_names[i]%10*64,water_names[i]/10*64,64,64);
		frameRate(8);
		noiseSeed(7);
	}
	public void draw2() {
		background(0);
		scale(2f);
		for (int i=0; i<tiles.length; i++) {
			image(tiles[i],i%5*70,i/5*70);
		}
		image(dec13[0],70,70);
		image(dec12[2],140,70);
		image(dec12[3],210,70);
	}
	int ew_mv_size = 32;
	int ns_mv_size = 0;
	int playerX=6, playerY=6;
	public void keyPressed() 
	{
		if (key=='i') {
			playerY--; if (playerY<0) {playerY=0; key='w';}
		}
		if (key=='k') {
			playerY++; if (playerY>14) {playerY=14; key='s';}
		}
		if (key=='j') {
			playerX--; if (playerX<0) {playerX=0; key='a';}
		}
		if (key=='l') {
			playerX++; if (playerX>14) {playerX=14; key='d';}
		}
		int maxmv = 32;	  
		if (key == 's') {
			ew_mv_size -= 32;	    
			if (ew_mv_size > maxmv) ew_mv_size = maxmv;
		} 
		if (key == 'w') {
			ew_mv_size += 32;	    
			if (ew_mv_size < -maxmv) ew_mv_size = -maxmv;
		} 	  
		if (key == 'a') {
			ns_mv_size += 16;	    
			if (ns_mv_size > maxmv) ns_mv_size = maxmv;
		} 
		if (key == 'd') {
			ns_mv_size -= 16;	    
			if (ns_mv_size < -maxmv) ns_mv_size = -maxmv;
		}
	}


	float t = 0.0f;
	int xoff = 0;
	int yoff = 0;

	final int MAX_HEIGHT=8;
	int getElevation(int x_noise,int y_noise) {
		float frac = 3.0f;	      
		int elevation = (int) (MAX_HEIGHT*noise( x_noise/frac+2000, y_noise/frac,t));	   
		elevation -=4;	   
		if (elevation <0) elevation = 0;	   
		return elevation;	      
	}
	int[] layout=new int[15*15];

	public void draw() {
		background(0);

		//t += 0.00001;
		xoff += ew_mv_size;
		yoff -= ns_mv_size;

		ew_mv_size =0;
		ns_mv_size =0;

		int x_part = xoff % 32;
		int x_rnd  = xoff/32;

		int y_part = yoff % 16;
		int y_rnd  = yoff/16;

		for (int ii = 14; ii>= 0/*-2; i < width/32+1*/; ii--) {
			int i=ii-7;
			// diagonal down to right
			for (int jj = 0; jj <=14; jj++) {
				int j=jj-7;
				int kk=(14-ii)*15+jj;
				float x =  j*32 - x_part;
				float y = -i*32 + y_part;

				float x_rot = x   - y/2 + width/2 + i*16 -32;
				float y_rot = x/2 + y   + height/2 + i*16 -16*3;

				int x_noise = (i + x_rnd);
				int y_noise = (j + y_rnd);

				int offset = 0;
				//////////////////////////////////////
				// get a random flat tile for base
				float frac = 9.0f;  
				// random(tiles.length); 
				int tile_ind = offset+ (int) ((tiles.length-offset) * 3.0*noise( x_noise/frac, y_noise/frac,t))%(tiles.length-offset);
				layout[kk]=tile_ind;
				if (kk/15==playerY && kk%15==playerX) tint(255,0,0); else tint(-1);
				if (tiles[tile_ind] != null) {
					image(tiles[tile_ind], x_rot, y_rot);
				}
				text(""+kk,x_rot+32,y_rot+48);
				if (true) {
					///////////////////////////////////////
					// raise the elevation
					int elevation = getElevation(x_noise,y_noise);
					boolean is_slope = false;
					int elev_factor = 32;
					// draw elev tiles upwards
					for (int k = 0; k < elevation; k++) {

						float nval =  3.0f*noise( x_noise/frac,y_noise/frac, k/frac + t);
						int ind = (int) (elev.length * nval) % elev.length;
						if (elev[ind] != null) {
							image(elev[ind], x_rot, y_rot - k*elev_factor);
						}
					}

					/// put a slope if neighboring tiles are elevated differently
					if        (getElevation(x_noise+1,y_noise) > elevation) {
						image(slope[1], x_rot, y_rot - elevation*elev_factor );
						is_slope = true;
					} else if (getElevation(x_noise, y_noise-1) > elevation) {
						image(slope[2], x_rot, y_rot - elevation*elev_factor );
						is_slope = true;
					}  else if (getElevation(x_noise-1,y_noise) > elevation) {
						image(slope[6], x_rot, y_rot - elevation*elev_factor );
						is_slope = true;
					} else if (getElevation(x_noise, y_noise+1) > elevation) {
						image(slope[5], x_rot, y_rot - elevation*elev_factor );
						is_slope = true;
					} 

					else if (getElevation(x_noise+1,y_noise-1) > elevation) {
						/// directly above
						image(slope[0], x_rot, y_rot - elevation*elev_factor );
						is_slope = true;
					} else if (getElevation(x_noise+1,y_noise+1) > elevation) {
						///
						image(slope[3], x_rot, y_rot - elevation*elev_factor );
						is_slope = true;
					} else if (getElevation(x_noise-1,y_noise-1) > elevation) {
						///
						image(slope[4], x_rot, y_rot - elevation*elev_factor );
						is_slope = true;
					} 

					else
						//////////////////////////
						/// now put decoration on it
						if ((!is_slope) && (noise( 500 + x_noise/frac, y_noise/frac,t) > 0.55f)) {
							frac = 11.0f;
							PImage[] tree = dec;
							if (elevation!=0) {
								if (x_noise%5 == 0) tree=dec33;
								if (x_noise%3 == 0) tree=dec13;
								if (x_noise%2 == 0) tree=dec12;
							} else {
								if ((x_noise^y_noise)%5 == 0) tree=water;
								if ((x_noise^y_noise)%2 == 0) tree=dec12;
							}
							float nval =  3.0f*noise( 100 + x_noise/frac,y_noise/frac,t);
							int ind = (int) ((tree.length) * nval) % tree.length;
							float xxx=x_rot-(tree[ind].width>64?64:0);
							float yyy = y_rot - ((elevation-2)*elev_factor + tree[ind].height);
							if (tree[ind] != null) {
								image(tree[ind], xxx, yyy);
							}
						}

				} // extra terrain

			}  // i loop
		} // j loop

	}
}
