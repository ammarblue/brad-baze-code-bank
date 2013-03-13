import java.awt.Image;
import java.awt.MediaTracker;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class CHARACTER_LIST_IMAGES {
	public static Image[] CLI = new Image[new File("CHARACTER_LIST_IMAGES").listFiles().length];
	public static Image[] CLTI = new Image[new File("CHARACTER_LIST_TEXT_IMAGES").listFiles().length];
	public static Image temp_image;
	
	
	
	
	
	public static void initializeCHARACTER_LIST_IMAGES(){
		for (int i = 0; i < CLI.length;i++){
		temp_image = new ImageIcon(CHARACTER_LIST_IMAGES.class.getResource("CLI_"+i+".png")).getImage();
		CLI[i] = temp_image.getScaledInstance(temp_image.getWidth(null) * GAME_FRAME.SCALE, temp_image.getHeight(null)* GAME_FRAME.SCALE, Image.SCALE_FAST);
		
		
		}
	
		//Text scaling images
		for (int i = 0; i < CLTI.length;i++){
		temp_image = new ImageIcon(CHARACTER_LIST_IMAGES.class.getResource("CLTI_"+i+".png")).getImage();
		CLTI[i] = temp_image.getScaledInstance(temp_image.getWidth(null) * GAME_FRAME.SCALE / 3, temp_image.getHeight(null)* GAME_FRAME.SCALE / 3, Image.SCALE_FAST);
		
		}
	}
	public static void LOAD(JPanel panel)  
	{  
	
	CHARACTER_LIST_IMAGES.initializeCHARACTER_LIST_IMAGES();
	MediaTracker tracker = new MediaTracker(panel);  
		for (int i = 0; i < CHARACTER_LIST_IMAGES.CLI.length; i++) {
				tracker.addImage( CHARACTER_LIST_IMAGES.CLI[i], 0 );  

					try  
					{  
						tracker.waitForAll();  
					}  
					catch( InterruptedException x )  
					{  
						System.err.println( "MediaTracker interrupted!" );  
					}  
     }  
		for (int i = 0;i < CHARACTER_LIST_IMAGES.CLTI.length; i++) {
			 
			tracker.addImage( CHARACTER_LIST_IMAGES.CLTI[i], 0 );  
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