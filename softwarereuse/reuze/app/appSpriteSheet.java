package reuze.app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class appSpriteSheet {
	public static void main(String args[]) { // combines all images in directory
												// into a single sprite sheet
		String directory = "/Users/bobcook/Desktop/ptest/tile_oga/data/tiles/";
		File dir = new File(directory);
		int ww = 10000, hh = 10000, n = 0;
		BufferedImage img = null;
		for (String file : dir.list()) {
			File f = null;
			System.out.println(file);
			if (file.charAt(0) == '.')
				continue;
			try {
				f = new File(directory + file);
				img = ImageIO.read(f);
			} catch (IOException e) {
				return;
			}
			System.out.println(img);
			ww = Math.min(ww, img.getWidth());
			hh = Math.min(hh, img.getHeight());
			img.flush();
			n++;
		} // for
		System.out.println(n + " " + ww + " " + hh);
		BufferedImage img2 = new BufferedImage(n * ww, hh,
				BufferedImage.TYPE_INT_ARGB);
		int nn = 0;
		for (String file : dir.list()) {
			File f = null;
			if (file.charAt(0) == '.')
				continue;
			try {
				f = new File(directory + file);
				img = ImageIO.read(f);
				if (ww != img.getWidth())
					return;
				if (hh != img.getHeight())
					return;
				int[] argb = img.getRGB(0, 0, ww, hh, null, 0, ww);
				img2.setRGB(nn * ww, 0, ww, hh, argb, 0, ww);
			} catch (IOException e) {
				return;
			}
			img.flush();
			nn++;
		} // for
		try {
			// retrieve image
			File outputfile = new File("saved.png");
			ImageIO.write(img2, "png", outputfile);
		} catch (IOException e) {
			return;
		}
	}
}
