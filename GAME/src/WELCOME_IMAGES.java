import java.awt.Image;
import java.awt.MediaTracker;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class WELCOME_IMAGES {

	public static Image[] WI = new Image[new File("WELCOME_IMAGES").listFiles().length];
	public static Image[] WTI = new Image[new File("WELCOME_TEXT_IMAGES").listFiles().length];
	public static Image temp_image;
	//public static Image[] ROTATING_RAY1 = new Image[359];
	//public static Image[] ROTATING_RAY2 = new Image[359];
	
	
	
	public static void initializeWELCOME_IMAGES(){
		for (int i = 0; i < WI.length;i++){
		temp_image = new ImageIcon(WELCOME_PANEL.class.getResource("WI_"+i+".png")).getImage();
		WI[i] = temp_image.getScaledInstance(temp_image.getWidth(null) * GAME_FRAME.SCALE, temp_image.getHeight(null)* GAME_FRAME.SCALE, Image.SCALE_FAST);
		
		
		}
	
		//Text scaling images
		for (int i = 0; i < WTI.length;i++){
		temp_image = new ImageIcon(WELCOME_PANEL.class.getResource("WTI_"+i+".png")).getImage();
		WTI[i] = temp_image.getScaledInstance(temp_image.getWidth(null) * GAME_FRAME.SCALE / 3, temp_image.getHeight(null)* GAME_FRAME.SCALE / 3, Image.SCALE_FAST);
		
		}
	}
	//public static void initializeRAY_ROTATION_IMAGES(){
		//for (int i = 0; i < 360;i++){
			//ROTATING_RAY1[i] = MANIPULATE_IMAGE.ROTATE(WI[23], i);
			//ROTATING_RAY2[i] = MANIPULATE_IMAGE.ROTATE(WI[24], i);
		//}	
	//}
	public static void LOAD(JPanel panel)  
	{  
	initializeWELCOME_IMAGES();
	MediaTracker tracker = new MediaTracker(panel);  
		for (int i = 0; i < WI.length; i++) {
				tracker.addImage( WI[i], 0 );  
					try  
					{  
						tracker.waitForAll();  
					}  
					catch( InterruptedException x )  
					{  
						System.err.println( "MediaTracker interrupted!" );  
					}  
     }  
		for (int i = 0;i < WTI.length; i++) {
			 
			tracker.addImage(WTI[i], 0 );  
				try  
				{  
					tracker.waitForAll();  
				}  
				catch( InterruptedException x )  
				{  
					System.err.println( "MediaTracker interrupted!" );  
				}  
 }  
		//initializeRAY_ROTATION_IMAGES();
		/*for (int i = 0;i < ROTATING_RAY1.length; i++) {
			 
			tracker.addImage( ROTATING_RAY1[i], 0 );  
				try  
				{  
					tracker.waitForAll();  
				}  
				catch( InterruptedException x )  
				{  
					System.err.println( "MediaTracker interrupted!" );  
				}  
 }  
		for (int i = 0;i < ROTATING_RAY2.length; i++) {
			 
			tracker.addImage( ROTATING_RAY2[i], 0 );  
				try  
				{  
					tracker.waitForAll();  
				}  
				catch( InterruptedException x )  
				{  
					System.err.println( "MediaTracker interrupted!" );  
				}  
 }  */

}
}