/* Ashleigh Amrine
 * CSCI 1302A
 * 9/20/2012
 */

import javax.swing.JOptionPane;
import java.util.*;
public class Driver {

	public static void main(String [] args){
		String galaxy_name = JOptionPane.showInputDialog("Enter a galaxy name: ");
		String string_num_of_stars = JOptionPane.showInputDialog("Enter a number of stars: ");
		String string_diam_of_galaxy = JOptionPane.showInputDialog("Enter diamater of Galaxy: ");
		
		int num_of_stars = Integer.parseInt(string_num_of_stars);
		int diam_of_galaxy = Integer.parseInt(string_diam_of_galaxy);
		
		Galaxy galaxy = new Galaxy(galaxy_name, num_of_stars, diam_of_galaxy);
		
		JOptionPane.showMessageDialog(null, galaxy.getName() + " Galaxy has " 
		+ galaxy.getNum_of_stars() + "# of stars. And a diamater of " + galaxy.getDiam_of_galaxy() + ".");
		
		Star[] stars_in_galaxy=galaxy.getStars_in_galaxy();		
		
		for(int i = 0; i < galaxy.getStars_in_galaxy().length; i++){
			JOptionPane.showMessageDialog(null, stars_in_galaxy[i].getName() 
					+ " star has a diameter of " + stars_in_galaxy[i].getDiameter() + 
					" and an age of " + stars_in_galaxy[i].getAge() + ". The surface temperature is "
					+ stars_in_galaxy[i].getSurf_temp());
		}
	}
}
