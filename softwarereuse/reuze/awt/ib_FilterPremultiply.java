package reuze.awt;

import com.software.reuze.ib_a_FilterPoint;
/*
** Copyright 2005 Huxtable.com. All rights reserved.
*/

/**
 * A filter which pre-multiplies an image's alpha. 
 * Note: this does not change the image type of the BufferedImage
 */
 public class ib_FilterPremultiply extends ib_a_FilterPoint {

      public ib_FilterPremultiply() {
      }

      public int filterRGB(int x, int y, int rgb) {
            int a = (rgb >> 24) & 0xff;
            int r = (rgb >> 16) & 0xff;
            int g = (rgb >> 8) & 0xff;
            int b = rgb & 0xff;
            float f = a * (1.0f / 255.0f);
            r *= f;
            g *= f;
            b *= f;
            return (a << 24) | (r << 16) | (g << 8) | b;
      }

      public String toString() {
            return "Alpha/Premultiply";
      }
}