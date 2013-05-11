package reuze.awt;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.software.reuze.ga_TiledMap;

public class ga_TiledAWT {
	ga_TiledMap m;
	float zoom;
	ArrayList<Image> images;
    int captionHeight=-8;
    int selectionX, selectionY, selectionWidth, selectionHeight;
    int positionX, positionY;
	public ga_TiledAWT(ga_TiledMap m) {
		this.m=m;
		images=new ArrayList<Image>(0);
		String s=m.tileSets.get(0).imageName;
		try {
			images.add(ImageIO.read(new File("data/tiledmap/"+s)));
		} catch (IOException e) {
			System.out.println(s);
			e.printStackTrace();
		}
	}
	public void paint(Graphics g) {
		paintLayer((Graphics2D)g, 0);
		g.setColor(Color.black);
		paintGrid(g);
		paintCoordinates((Graphics2D)g);
	}
	protected void paintLayer(Graphics2D g2d, int layer) {
        // Determine tile size and offset
		int width=m.tileSets.get(layer).tileWidth, height=m.tileSets.get(layer).tileHeight;
		int tileWidth=images.get(layer).getWidth(null)/(m.tileSets.get(layer).spacing+m.tileSets.get(layer).tileWidth);
        if (width <= 0 || height <= 0) {
            return;
        }
        int firstgid=m.tileSets.get(layer).firstgid;
        int w=width, h=height;

        // Determine area to draw from clipping rectangle
        Rectangle clipRect = g2d.getClipBounds();
        int startX = clipRect.x / width;
        int startY = clipRect.y / height;
        int endX = (clipRect.x + clipRect.width) / width + 1;
        int endY = (clipRect.y + clipRect.height) / height + 3;
        // (endY +2 for high tiles, could be done more properly)
        g2d.translate(8,captionHeight);
        // Draw this map layer
        for (int y = startY, gy = (startY + 1) * height;
                y < endY; y++, gy += height) {
            for (int x = startX, gx = startX * width;
                    x < endX; x++, gx += width) {
                if (x<m.width && y<m.height) {
                    	int i=m.layers.get(layer).tiles[y][x]-firstgid;
                    	int j=i%tileWidth;
                    	int k=i/tileWidth;
                    	System.out.println(i+" "+gx+" "+gy+" "+j+" "+k);
                        g2d.drawImage(images.get(layer), gx, gy, gx+width, gy+height, j*33, k*33, j*33+w, k*33+h, null);
                    }
            }
        }
    }

	protected void paintGrid(Graphics g2d) {
        // Determine tile size
        int width=m.tileWidth, height=m.tileHeight;
        if (width <= 0 || height <= 0) {
            return;
        }

        // Determine lines to draw from clipping rectangle
        Rectangle clipRect = g2d.getClipBounds();
        int startX = clipRect.x / width * width;
        int startY = clipRect.y / height * height;
        int endX = clipRect.x + clipRect.width;
        int endY = clipRect.y + clipRect.height;

        for (int x = startX; x < endX; x += width) {
            g2d.drawLine(x, clipRect.y, x, clipRect.y + clipRect.height - 1);
        }
        for (int y = startY; y < endY; y += height) {
            g2d.drawLine(clipRect.x, y, clipRect.x + clipRect.width - 1, y);
        }
    }
	
	protected void paintCoordinates(Graphics2D g2d) {
		int width=m.tileWidth, height=m.tileHeight;
        if (width <= 0 || height <= 0) {
            return;
        }
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Determine tile size and offset
        Font font = new Font("SansSerif", Font.PLAIN, height / 4);
        g2d.setFont(font);
        FontRenderContext fontRenderContext = g2d.getFontRenderContext();

        // Determine area to draw from clipping rectangle
        Rectangle clipRect = g2d.getClipBounds();
        int startX = clipRect.x / width;
        int startY = clipRect.y / height;
        int endX = (clipRect.x + clipRect.width) / width + 1;
        int endY = (clipRect.y + clipRect.height) / height + 1;

        // Draw the coordinates
        int gy = startY * height;
        for (int y = startY; y < endY; y++) {
            int gx = startX * width;
            for (int x = startX; x < endX; x++) {
                String coords = "(" + x + "," + y + ")";
                Rectangle2D textSize =
                        font.getStringBounds(coords, fontRenderContext);

                int fx = gx + (int) ((width - textSize.getWidth()) / 2);
                int fy = gy + (int) ((height + textSize.getHeight()) / 2);

                g2d.drawString(coords, fx, fy);
                gx += width;
            }
            gy += height;
        }
    }
}
