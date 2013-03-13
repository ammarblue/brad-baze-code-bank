import java.awt.MediaTracker;
import javax.swing.JFrame;

public class LOAD {
	public static void LOAD_ALL(JFrame frame)  
	{  
	CHARACTER_LIST_IMAGES.initializeCHARACTER_LIST_IMAGES();
	PLAYER_CREATION_IMAGES.initializePLAYER_CREATION_IMAGES();
	WELCOME_IMAGES.initializeWELCOME_IMAGES();
	TRANSITION1_IMAGES.initializeTRANSITION1_IMAGES();
	MediaTracker tracker = new MediaTracker(frame);  
		for (int i = 0; i < PLAYER_CREATION_IMAGES.PCI.length; i++) {
				tracker.addImage( PLAYER_CREATION_IMAGES.PCI[i], 0 );  

					try  
					{  
						tracker.waitForAll();  
					}  
					catch( InterruptedException x )  
					{  
						System.err.println( "MediaTracker interrupted! PCI" );  
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
					System.err.println( "MediaTracker interrupted! PCTI" );  
				}  
 }  
		
		
		 
			for (int i = 0; i < WELCOME_IMAGES.WI.length; i++) {
					tracker.addImage( WELCOME_IMAGES.WI[i], 0 );  
						try  
						{  
							tracker.waitForAll();  
						}  
						catch( InterruptedException x )  
						{  
							System.err.println( "MediaTracker interrupted! WI" );  
						}  
	     }  
			for (int i = 0;i < WELCOME_IMAGES.WTI.length; i++) {
				 
				tracker.addImage(WELCOME_IMAGES.WTI[i], 0 );  
					try  
					{  
						tracker.waitForAll();  
					}  
					catch( InterruptedException x )  
					{  
						System.err.println( "MediaTracker interrupted! WTI" );  
					}  
	 }  
			for (int i = 0; i < TRANSITION1_IMAGES.T1I.length; i++) {
				tracker.addImage( TRANSITION1_IMAGES.T1I[i], 0 );  

					try  
					{  
						tracker.waitForAll();  
					}  
					catch( InterruptedException x )  
					{  
						System.err.println( "MediaTracker interrupted! TI" );  
					}  
     }  
			for (int i = 0; i < CHARACTER_LIST_IMAGES.CLI.length; i++) {
				tracker.addImage( CHARACTER_LIST_IMAGES.CLI[i], 0 );  

					try  
					{  
						tracker.waitForAll();  
					}  
					catch( InterruptedException x )  
					{  
						System.err.println( "MediaTracker interrupted! CLI" );  
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
					System.err.println( "MediaTracker interrupted! CLTI" );  
				}  
	}}}
