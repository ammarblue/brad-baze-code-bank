package reuze.awt;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

/*
 * Copyright (c) 2008, Keith Woodward
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of Keith Woodward nor the names
 *    of its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

/**
 *
 * @author Keith
 */
public class ib_Accelerated {
	public static boolean useVolatileImage = false;
	BufferedImage bi;
	VolatileImage vi;
	public int width;
	public int height;

	public ib_Accelerated(int width, int height){
		this.width = width;
		this.height = height;
		//if (useVolatileImage){
			vi = getAndCheckVolatileImage();
		//}else{
			this.bi = createTransparentBufferedImage(width, height);
		//}
	}

	public ib_Accelerated(BufferedImage bi){
		this.bi = bi;
		width = bi.getWidth();
		height = bi.getHeight();
		//if (useVolatileImage){
			vi = getAndCheckVolatileImage();
		//}
	}

	public Image getImage(){
		if (useVolatileImage){
			return getAndCheckVolatileImage();
		}else{
			return bi;
		}
	}

	protected void drawOntoVolatileImage(VolatileImage voltaileImage, BufferedImage bufferedImage){
		Graphics2D g = voltaileImage.createGraphics();
		g.drawImage(bufferedImage, 0, 0, null);
		g.dispose();
	}
	protected VolatileImage getAndCheckVolatileImage(){
		//System.out.println(this.getClass().getSimpleName() + ": drawOntoImage(), "+this.getIndexX()+", "+this.getIndexY()+", time == "+v.getWorld().getTimeNowSeconds());
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
		if (vi == null || vi.validate(gc) != VolatileImage.IMAGE_OK) {
			vi = createTransparentVolatileImage(width, height);
			if (bi != null){
				drawOntoVolatileImage(vi, bi);
			}
		}
		do {
			int valid = vi.validate(gc);
			if (valid == VolatileImage.IMAGE_INCOMPATIBLE) {
				vi = createTransparentVolatileImage(width, height);
				if (bi != null){
					drawOntoVolatileImage(vi, bi);
				}
			}
		} while (vi.contentsLost());
		return vi;
	}


	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public static BufferedImage createTransparentBufferedImage(int width, int height) {
		BufferedImage image =  createBufferedImage(width, height, Transparency.TRANSLUCENT);
		Graphics2D g = (Graphics2D)image.getGraphics();
		g.setColor(new Color(0,0,0,0));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OUT));
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		return image;
	}

	public static BufferedImage createBufferedImage(int width, int height, int transparency) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
		BufferedImage image = null;

		image = gc.createCompatibleImage(width, height, transparency);
		//System.out.println(ImageBank.class.getSimpleName() + ": created new VolatileImage");
		return image;
	}

	public static VolatileImage createVolatileImage(int width, int height, int transparency) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
		VolatileImage image = null;

		image = gc.createCompatibleVolatileImage(width, height, transparency);

		int valid = image.validate(gc);

		if (valid == VolatileImage.IMAGE_INCOMPATIBLE) {
			image = createVolatileImage(width, height, transparency);
		}
		//System.out.println(ImageBank.class.getSimpleName() + ": created new VolatileImage");
		return image;
	}
	public static VolatileImage createTransparentVolatileImage(int width, int height) {
		VolatileImage image = createVolatileImage(width, height, Transparency.TRANSLUCENT);
		Graphics2D g = (Graphics2D)image.getGraphics();
		g.setColor(new Color(0,0,0,0));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OUT));
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		return image;
	}
}
