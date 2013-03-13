import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;


public class MANIPULATE_IMAGE {
	public static Image FLIP(Image img) {
		BufferedImage buffimg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		int w = buffimg.getWidth();
		int h = buffimg.getHeight();
		BufferedImage dimg = new BufferedImage(w, h, buffimg.getType());
		Graphics2D g = dimg.createGraphics();
		g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
		g.dispose();
		Image b = dimg;
		return b;
	}
	public static Image ROTATE(Image img, int angle) {
		BufferedImage buffimg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		int w = buffimg.getWidth();
		int h = buffimg.getHeight();
		BufferedImage dimg = new BufferedImage(w, h, buffimg.getType());
		Graphics2D g = dimg.createGraphics();
		g.rotate(Math.toRadians(angle), w/2, h/2);
		g.drawImage(img, 0, 0, null);
		g.dispose();
		Image b = dimg;
		return b;
	}
}
