/*
 * Copyright 2009 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package reuze.awt;


//import javax.imageio.ImageIO;

import reuze.awt.ib_MatrixBitsToImageConfig;

import com.software.reuze.d_MatrixBits;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * Writes a {@link d_MatrixBits} to {@link BufferedImage},
 * file or stream. Provided here instead of core since it depends on
 * Java SE libraries.
 *
 * @author Sean Owen
 */
public final class ib_MatrixBitsToImage {

  private static final ib_MatrixBitsToImageConfig DEFAULT_CONFIG = new ib_MatrixBitsToImageConfig();

  private ib_MatrixBitsToImage() {}

  /**
   * Renders a {@link d_MatrixBits} as an image, where "false" bits are rendered
   * as white, and "true" bits are rendered as black.
   */
  public static BufferedImage toBufferedImage(d_MatrixBits matrix) {
    return toBufferedImage(matrix, DEFAULT_CONFIG);
  }

  /**
   * As {@link #toBufferedImage(d_MatrixBits)}, but allows customization of the output.
   */
  public static BufferedImage toBufferedImage(d_MatrixBits matrix, ib_MatrixBitsToImageConfig config) {
    int width = matrix.getWidth();
    int height = matrix.getHeight();
    BufferedImage image = new BufferedImage(width, height, config.getBufferedImageColorModel());
    int onColor = config.getPixelOnColor();
    int offColor = config.getPixelOffColor();
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        image.setRGB(x, y, matrix.get(x, y) ? onColor : offColor);
      }
    }
    return image;
  }

  /**
   * Writes a {@link d_MatrixBits} to a file.
   *
   * @see #toBufferedImage(d_MatrixBits)
   */
  public static void writeToFile(d_MatrixBits matrix, String format, File file) throws IOException {
    writeToFile(matrix, format, file, DEFAULT_CONFIG);
  }

  /**
   * As {@link #writeToFile(d_MatrixBits, String, File)}, but allows customization of the output.
   */
  public static void writeToFile(d_MatrixBits matrix, String format, File file, ib_MatrixBitsToImageConfig config) 
      throws IOException {  
    BufferedImage image = toBufferedImage(matrix, config);
    if (!ImageIO.write(image, format, file)) {
      throw new IOException("Could not write an image of format " + format + " to " + file);
    }
  }

  /**
   * Writes a {@link d_MatrixBits} to a stream.
   *
   * @see #toBufferedImage(d_MatrixBits)
   */
  public static void writeToStream(d_MatrixBits matrix, String format, OutputStream stream) throws IOException {
    writeToStream(matrix, format, stream, DEFAULT_CONFIG);
  }

  /**
   * As {@link #writeToStream(d_MatrixBits, String, OutputStream)}, but allows customization of the output.
   */
  public static void writeToStream(d_MatrixBits matrix, String format, OutputStream stream, ib_MatrixBitsToImageConfig config) 
      throws IOException {  
    BufferedImage image = toBufferedImage(matrix, config);
    if (!ImageIO.write(image, format, stream)) {
      throw new IOException("Could not write an image of format " + format);
    }
  }

}
