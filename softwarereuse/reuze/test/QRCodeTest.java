/**
 * Copyright 2008 ZXing authors
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

package reuze.test;


import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import com.software.reuze.c_QR;
import com.software.reuze.c_QRErrorCorrectionLevel;
import com.software.reuze.c_QRMode;
import com.software.reuze.c_QRVersion;
import com.software.reuze.d_MatrixBytes;

/**
 * @author satorux@google.com (Satoru Takabayashi) - creator
 * @author mysen@google.com (Chris Mysen) - ported from C++
 */
public final class QRCodeTest extends TestCase {

  @Test
  public void test() {
    c_QR qrCode = new c_QR();

    // First, test simple setters and getters.
    // We use numbers of version 7-H.
    qrCode.setMode(c_QRMode.BYTE);
    qrCode.setECLevel(c_QRErrorCorrectionLevel.H);
    qrCode.setVersion(c_QRVersion.getVersionForNumber(7));
    qrCode.setMaskPattern(3);

    assertSame(c_QRMode.BYTE, qrCode.getMode());
    assertSame(c_QRErrorCorrectionLevel.H, qrCode.getECLevel());
    assertEquals(7, qrCode.getVersion().getVersionNumber());
    assertEquals(3, qrCode.getMaskPattern());

    // Prepare the matrix.
    d_MatrixBytes matrix = new d_MatrixBytes(45, 45);
    // Just set bogus zero/one values.
    for (int y = 0; y < 45; ++y) {
      for (int x = 0; x < 45; ++x) {
        matrix.set(x, y, (y + x) % 2);
      }
    }

    // Set the matrix.
    qrCode.setMatrix(matrix);
    assertSame(matrix, qrCode.getMatrix());
  }

  @Test
  public void testToString1() {
    c_QR qrCode = new c_QR();
    String expected =
      "<<\n" +
      " mode: null\n" +
      " ecLevel: null\n" +
      " version: null\n" +
      " maskPattern: -1\n" +
      " matrix: null\n" +
      ">>\n";
    assertEquals(expected, qrCode.toString());
  }

  @Test
  public void testToString2() {
    c_QR qrCode = new c_QR();
    qrCode.setMode(c_QRMode.BYTE);
    qrCode.setECLevel(c_QRErrorCorrectionLevel.H);
    qrCode.setVersion(c_QRVersion.getVersionForNumber(1));
    qrCode.setMaskPattern(3);
    d_MatrixBytes matrix = new d_MatrixBytes(21, 21);
    for (int y = 0; y < 21; ++y) {
      for (int x = 0; x < 21; ++x) {
        matrix.set(x, y, (y + x) % 2);
      }
    }
    qrCode.setMatrix(matrix);
    String expected = "<<\n" +
        " mode: BYTE\n" +
        " ecLevel: H\n" +
        " version: 1\n" +
        " maskPattern: 3\n" +
        " matrix:\n" +
        " 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0\n" +
        " 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1\n" +
        " 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0\n" +
        " 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1\n" +
        " 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0\n" +
        " 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1\n" +
        " 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0\n" +
        " 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1\n" +
        " 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0\n" +
        " 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1\n" +
        " 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0\n" +
        " 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1\n" +
        " 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0\n" +
        " 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1\n" +
        " 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0\n" +
        " 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1\n" +
        " 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0\n" +
        " 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1\n" +
        " 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0\n" +
        " 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1\n" +
        " 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0\n" +
        ">>\n";
    assertEquals(expected, qrCode.toString());
  }

  @Test
  public void testIsValidMaskPattern() {
    assertFalse(c_QR.isValidMaskPattern(-1));
    assertTrue(c_QR.isValidMaskPattern(0));
    assertTrue(c_QR.isValidMaskPattern(7));
    assertFalse(c_QR.isValidMaskPattern(8));
  }

}
