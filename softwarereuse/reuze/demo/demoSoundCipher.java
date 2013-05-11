package reuze.demo;
import com.software.reuze.s_i_SoundCallback;
import com.software.reuze.z_Processing;

import processing.core.PApplet;
import reuze.awt.s_SoundCipher;
import reuze.awt.s_SoundCipherScore;


public class demoSoundCipher extends PApplet implements s_i_SoundCallback {
	/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/6458*@* */
	/* !do not delete the line above, required for linking your tweak if you re-upload */
	/**
	* Water Music - Dec 2009
	* by volts (Tom Blackwell)
	* Left mouse generates a new score.  Right mouse changes instruments.
	* 
	*/

	// Main Constants
	int MIDDLE_C = 60; // per MIDI spec
	int TEMPO =  250; // beats per minute, 
	int X_SIZE = 580, Y_SIZE = 400; //display width and height
	int NOTES = 25, MIDDLE_C_POS = 12; // number of vertical note positions, location of middle C note
	String palletsource = "sarah_chapman_louvre.jpg"; // thanks to http://www.flickr.com/photos/horrorhotel/ for CC image 

	s_SoundCipher sc = new s_SoundCipher();
	s_SoundCipherScore score;
	float[][] instruments = {{sc.TIMPANI, sc.TINKLE_BELL, sc.BOTTLE},
	                       {sc.WOODBLOCKS,sc.XYLOPHONE, sc.BELLS},
	                       {sc.TELEPHONE, sc.TENOR, sc.TAIKO},
	                       {sc.VOICE,sc.WHISTLE,sc.XYLOPHONE},
	                       {sc.BOWED_GLASS, sc.CRYSTAL, sc.FANTASIA, sc.FLUTE, sc.KALIMBA},
	                       {sc.BELL,sc.BELLS},{sc.BOTTLE,sc.BOTTLE_BLOW},
	                       {sc.BRIGHT_ACOUSTIC, sc.BRIGHTNESS},
	                       {sc.SITAR, sc.SHANNAI, sc.SHAMISEN}};
	int ensemble = 0;

	int beats; // number of beats that fit across the display window
	int box_spacing, box_size; // boxes in display window
	int x_error, y_error; // boxes might not divide evenly into width, height.
	int x_margin, y_margin; // margin from edges of display window

	int cur_beat; // the current beat, incremented by call-back
	boolean played; // have we played the current score?
	boolean[][] notes; // beats across, notes vertical

	public void setup(){
	  size(X_SIZE, Y_SIZE);
	  loadPixels();

	  
	  box_spacing = Y_SIZE/NOTES;
	  box_size = box_spacing-2;
	  y_error = Y_SIZE % NOTES;
	  x_error = X_SIZE % NOTES;
	  y_margin = y_error/2;
	  x_margin = x_error/2;
	  
	  beats = X_SIZE/box_spacing;
	  notes = new boolean[beats][NOTES];  
	  score = new s_SoundCipherScore();
	  score.addCallbackListener(this);
	  makeMusic();
	}

	public void draw(){

	}

	public void stop(){
	  score.stop();
	}

	public void mousePressed(){
	  if (mouseButton == LEFT){
	    makeMusic();
	  } else {
	    nextEnsemble();
	    makeScore();
	  }
	}

	// handle soundcipher score callbacks
	public void handleCallbacks(int beat) {
	  if (beat == beats & played) {
	    nextEnsemble();  //Change ensemble each time score has been played
	    if (ensemble==0) {
	      makeMusic();  //New music after playing all ensembles 
	    } else {
	      makeScore();
	    }
	    played = false;
	  } else {
	    played = true;
	  }
	  cur_beat = beat;
	}

	void nextEnsemble(){
	  if (ensemble < instruments.length-1){
	    ensemble++;
	  } else {
	    ensemble = 0;
	  }
	}

	// populate the notes array with Perlin sequences and make the soundcipher score
	void makeMusic(){
	  noLoop();
	  noiseSeed(frameCount);
	  float note_density = 0.2f; 
	  notes = new boolean[beats][NOTES];
	  for (int part=0;part<4;part++){
	    for (int beat=0; beat<beats; beat++){
	      int note = (int) (noise(beat,part)*NOTES);
	      if (random(1)<note_density) notes[beat][note] = true;
	    }
	  }
	  makeScore();
	  loop();
	}

	void makeScore(){
	  initializeScore();
	  for (int beat=0; beat<beats; beat++){
	    for (int note=0; note<NOTES; note++){
	      if (notes[beat][note]) {
	        addToScore(beat, note);
	      }
	    }
	  }
	  score.update();
	  score.play(-1);
	}

	void initializeScore(){
	   score.stop();
	   score.empty();
	   score.tempo(TEMPO);
	   score.instrument(sc.instrument);
	   
	   // anchor start and end
	   score.addNote(0,0,0,1); 
	   score.addNote(beats,0,0,1);

	   // callback eventIDs keep track of the beat
	   for (int i=0; i<=beats; i++){
	     score.addCallback(i, i);
	   }
	   played = false;
	}

	void addToScore(int beat, int note){
	    int pitch = (int)MIDDLE_C - note + MIDDLE_C_POS;
	    int channel = (int)random(instruments[ensemble].length);
	    float instrument = instruments[ensemble][channel];
	    int pan = (int) (((float)beat/((float)beats))*127);
	    //instruments must be on separate channels or only one note is played in each beat 
	    score.addNote(beat,channel, instrument, pitch, 120,1,0.8,pan);
	}
}
