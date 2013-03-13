import java.awt.Image;
import java.awt.MediaTracker;
import java.io.File;

import javax.swing.ImageIcon;

import javax.swing.JPanel;


public class TRANSITION1_IMAGES {
	public static Image[] T1I = new Image[new File("TRANSITION1_IMAGES").listFiles().length];
	public static Image temp_image;
	
	public static void initializeTRANSITION1_IMAGES(){
		for (int i = 0; i < T1I.length;i++){
			temp_image = new ImageIcon(TRANSITION1_IMAGES.class.getResource("T1I_"+i+".png")).getImage();
			T1I[i] = temp_image.getScaledInstance(temp_image.getWidth(null) * GAME_FRAME.SCALE, temp_image.getHeight(null)* GAME_FRAME.SCALE, Image.SCALE_FAST);
		}
	}
	
	public static void LOAD(JPanel panel)  
	{  
	
	TRANSITION1_IMAGES.initializeTRANSITION1_IMAGES();
	MediaTracker tracker = new MediaTracker(panel);  
		for (int i = 0; i < TRANSITION1_IMAGES.T1I.length; i++) {
				tracker.addImage( TRANSITION1_IMAGES.T1I[i], 0 );  

					try  
					{  
						tracker.waitForAll();  
					}  
					catch( InterruptedException x )  
					{  
						System.err.println( "MediaTracker interrupted!" );  
					}  
     }  
	
	}
}
