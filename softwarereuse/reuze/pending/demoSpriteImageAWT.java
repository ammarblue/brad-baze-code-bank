package reuze.pending;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.software.reuze.d_ArrayEfficientRemove;
import com.software.reuze.ga_Line2D;
import com.software.reuze.ga_Rectangle;
import com.software.reuze.ga_Vector2;
import com.software.reuze.m_MathUtils;

import reuze.pending.SpriteImage;
import reuze.pending.SpriteManagerI;

public class demoSpriteImageAWT extends JFrame implements SpriteManagerI,KeyListener {
	BufferedImage im = null;
	SpriteImage si=null;
	Sprite s;
	Image i=null;
	d_ArrayEfficientRemove<Object> o;
	ArrayList<Sprite> movers;
	public demoSpriteImageAWT() {
		super("Test");
		registerListeners();
		pack();
		try {
			im = ImageIO.read(new File("./data/enemysheet.png"));
			i=getImage(this.getGraphicsConfiguration(),"./data/enemysheet.png");
		} catch (IOException e) {
		}
		si=new SpriteImage(im,0,0,16,32,0,4,3,this);
		s=new Sprite(si,1,50,50,0,0);
		o=new d_ArrayEfficientRemove<Object>();
		movers=new ArrayList<Sprite>();
		movers.add(s);
	}
	ga_Rectangle temp=new ga_Rectangle(0,0,0,0);
	@Override public void paint(Graphics g) {
		g.clearRect(0, 0, 400, 400);
		for (int i=0; i<movers.size(); i++) {
			movers.get(i).getFront(temp);
			for (int j=0; j<o.size(); j++) {
				Object ob=o.get(j);
			    if (ob instanceof ga_Line2D) {
			      ga_Line2D p=(ga_Line2D)ob;
			      if (temp.isIntersecting(p.a.x, p.a.y, p.b.x, p.b.y)) {
			    	  o.remove(j);
			      }
			    }
			}
		}
		if (o!=null)
		  for (Object ob:o) {
		    if (ob instanceof ga_Line2D) {
		    	ga_Line2D p=(ga_Line2D)ob;
		      g.drawLine((int)p.a.x,(int)p.a.y,(int)p.b.x,(int)p.b.y);
		      continue;
		    }
		  } //for
		s.render(g,1);
		s.tick();
	}

	private void registerListeners() {
		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		this.addKeyListener(this);
	}

	public void close() {
		System.exit(0);
	}
	BufferedImage getFlipped(BufferedImage bi, int x, int y, int w, int h, boolean flipX, boolean flipY) {
		BufferedImage b=bi.getSubimage(x, y, w, h), bb;
		if (flipX || flipY) {
			AffineTransform tx = AffineTransform.getScaleInstance(flipX?-1:1, flipY?-1:1);
			tx.translate(flipX?-w:0, flipY?-h:0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			bb = op.filter(b, null);
			b.flush();
		} else return b;
		return bb;
	}

	public void render(Object g, int screenX, int screenY, int x, int y,
			int w, int h, Object o) {
		BufferedImage bb=getFlipped((BufferedImage)o, x, y, Math.abs(w),Math.abs(h), w<0,h<0);
		((Graphics)g).drawImage(bb, screenX, screenY, null);			
	}

	public void render(Object graphics, int screenX, int screenY, Object o) {
		((Graphics)graphics).drawImage((BufferedImage)o, screenX, screenY, null);			
	}
	public void render(Object graphics, int screenX, int screenY, float rotate,
			float scale, Object o) {
		if (rotate !=0) ((Graphics2D)graphics).rotate(rotate,screenX,screenY);
		if (scale !=0) ((Graphics2D)graphics).scale(scale,scale);
		((Graphics)graphics).drawImage((BufferedImage)o, screenX, screenY, null);
	}
	public void render(Object graphics, int screenX, int screenY, int x, int y,
			int w, int h, float rotate, float scale, Object o) {
		BufferedImage bb=getFlipped((BufferedImage)o, x, y, Math.abs(w),Math.abs(h), w<0,h<0);
		if (rotate !=0) ((Graphics2D)graphics).rotate(rotate,screenX,screenY);
		if (scale !=0) ((Graphics2D)graphics).scale(scale,scale);
		((Graphics)graphics).drawImage(bb, screenX, screenY, null);
	}
	private static Image getImage(GraphicsConfiguration gc, String imageName) {
		BufferedImage source = null;
		try {
			source = ImageIO.read(demoSpriteImageAWT.class.getResourceAsStream(imageName));
		} catch (Exception e) {
			source=null;
		}
		if (source == null) {
			File file = new File(imageName);
			try {
				source = ImageIO.read(file);
			} catch (IOException e) {
				System.out.println(imageName+" not found in jar or file");
				source=null;
			}
		}
		if (source==null) return null;
		Image image = gc.createCompatibleImage(source.getWidth(), source.getHeight(), Transparency.BITMASK);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setComposite(AlphaComposite.Src);
		g.drawImage(source, 0, 0, null);
		g.dispose();
		source.flush();
		return image;
	}
	private static Image[][] cutImage(GraphicsConfiguration gc, String imageName, int xSize, int ySize) {
		Image source = getImage(gc, imageName);
		Image[][] images = new Image[source.getWidth(null) / xSize][source.getHeight(null) / ySize];
		for (int x = 0; x < source.getWidth(null) / xSize; x++)
		{
			for (int y = 0; y < source.getHeight(null) / ySize; y++)
			{
				Image image = gc.createCompatibleImage(xSize, ySize, Transparency.BITMASK);
				Graphics2D g = (Graphics2D) image.getGraphics();
				g.setComposite(AlphaComposite.Src);
				g.drawImage(source, -x * xSize, -y * ySize, null);
				g.dispose();
				images[x][y] = image;
			}
		}
		return images;
	}
	public static void main(String args[]) {
		demoSpriteImageAWT m=new demoSpriteImageAWT();
		m.setSize(400, 400);
		m.setVisible(true);

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		char k=arg0.getKeyChar();
		if (k=='d') {s.xv=4; s.image.xFlipPic=false; s.image.yFlipPic=false;}
		else if (k=='a') {s.xv=-4; s.image.xFlipPic=true; s.image.yFlipPic=false;}
		else if (k=='w') s.yv=-4;
		else if (k=='s') s.yv=4;
		else if (k=='l') {
			o.add(new ga_Line2D(m_MathUtils.random(this.getWidth()),m_MathUtils.random(this.getHeight()),m_MathUtils.random(this.getWidth()),m_MathUtils.random(this.getHeight())));
		} else  {s.xv=0; s.yv=0;}
	}
}
