import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class PLAYER_CREATION_IMAGES {
	public static Image[] PCI = new Image[new File("PLAYER_CREATION_IMAGES").listFiles().length];
	public static Image[] PCTI = new Image[new File("PLAYER_CREATION_TEXT_IMAGES").listFiles().length];
	public static Image temp_image;
	
	
	
	
	
	public static void initializePLAYER_CREATION_IMAGES(){
		for (int i = 0; i < PCI.length;i++){
		temp_image = new ImageIcon(PLAYER_CREATION_PANEL.class.getResource("PCI_"+i+".png")).getImage();
		PCI[i] = temp_image.getScaledInstance(temp_image.getWidth(null) * GAME_FRAME.SCALE, temp_image.getHeight(null)* GAME_FRAME.SCALE, Image.SCALE_FAST);
		
		
		}
	
		//Text scaling images
		for (int i = 0; i < PCTI.length;i++){
		temp_image = new ImageIcon(PLAYER_CREATION_PANEL.class.getResource("PCTI_"+i+".png")).getImage();
		PCTI[i] = temp_image.getScaledInstance(temp_image.getWidth(null) * GAME_FRAME.SCALE / 3, temp_image.getHeight(null)* GAME_FRAME.SCALE / 3, Image.SCALE_FAST);
		
		}
	}
	public static void LOAD(JPanel panel)  
	{  
	
	PLAYER_CREATION_IMAGES.initializePLAYER_CREATION_IMAGES();
	MediaTracker tracker = new MediaTracker(panel);  
		for (int i = 0; i < PLAYER_CREATION_IMAGES.PCI.length; i++) {
				tracker.addImage( PLAYER_CREATION_IMAGES.PCI[i], 0 );  

					try  
					{  
						tracker.waitForAll();  
					}  
					catch( InterruptedException x )  
					{  
						System.err.println( "MediaTracker interrupted!" );  
					}  
     }  
		for (int i = 0;i < PLAYER_CREATION_IMAGES.PCTI.length; i++) {
			 
			tracker.addImage( PLAYER_CREATION_IMAGES.PCTI[i], 0 );  
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
    
    