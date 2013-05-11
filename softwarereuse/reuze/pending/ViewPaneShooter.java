package reuze.pending;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JComponent;

import reuze.awt.ib_Accelerated;




/**
 *
 * @author Keith
 */
public class ViewPaneShooter extends JComponent {
	ib_Accelerated acceleratedImage;
	demoGameShooterMain main;

	public ViewPaneShooter(demoGameShooterMain main){
		this.main = main;
	}

	public Image getBackImage() {
		if (getWidth() <= 0 || getHeight() <= 0) {
			System.out.println(this.getClass().getSimpleName() + ": width &/or height <= 0!!!");
			return null;
		}
		if (acceleratedImage == null || acceleratedImage.getImage() == null || getWidth() != acceleratedImage.getWidth() || getHeight() != acceleratedImage.getHeight()){
			acceleratedImage = new ib_Accelerated(getWidth(), getHeight());
		}
		Image vi = acceleratedImage.getImage();
		return vi;
	}
	public void displayBackImage() {
		Graphics2D componentGraphics = (Graphics2D)getGraphics();
		if (componentGraphics != null) {
			componentGraphics.drawImage(acceleratedImage.getImage(), 0, 0, null);
			Toolkit.getDefaultToolkit().sync(); // to flush the graphics commands to the graphics card.  see http://www.javagaming.org/forums/index.php?topic=15000.msg119601;topicseen#msg119601
			//System.out.println(this.getClass().getSimpleName()+": rendered");
		}else{
			System.out.println(this.getClass().getSimpleName()+": getGraphics() == null");
		}
	}

}
