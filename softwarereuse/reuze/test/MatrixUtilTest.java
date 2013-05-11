/*
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

import com.software.reuze.c_QRErrorCorrectionLevel;
import com.software.reuze.c_QRMatrixUtil;
import com.software.reuze.c_QRVersion;
import com.software.reuze.d_ArrayBits;
import com.software.reuze.d_MatrixBytes;
import com.software.reuze.l_ExceptionBarcodeWriter;

/**
 * @author satorux@google.com (Satoru Takabayashi) - creator
 * @author mysen@google.com (Chris Mysen) - ported from C++
 */
public final class MatrixUtilTest extends TestCase {

  @Test
  public void testToString() {
    d_MatrixBytes array = new d_MatrixBytes(3, 3);
    array.set(0, 0, 0);
    array.set(1, 0, 1);
    array.set(2, 0, 0);
    array.set(0, 1, 1);
    array.set(1, 1, 0);
    array.set(2, 1, 1);
    array.set(0, 2, -1);
    array.set(1, 2, -1);
    array.set(2, 2, -1);
    String expected = " 0 1 0\n" + " 1 0 1\n" + "      \n";
    assertEquals(expected, array.toString());
  }

  @Test
  public void testClearMatrix() {
    d_MatrixBytes matrix = new d_MatrixBytes(2, 2);
    c_QRMatrixUtil.clearMatrix(matrix);
    assertEquals(-1, matrix.get(0, 0));
    assertEquals(-1, matrix.get(1, 0));
    assertEquals(-1, matrix.get(0, 1));
    assertEquals(-1, matrix.get(1, 1));
  }

  @Test
  public void testEmbedBasicPatterns1() throws l_ExceptionBarcodeWriter {
    // Version 1.
    d_MatrixBytes matrix = new d_MatrixBytes(21, 21);
    c_QRMatrixUtil.clearMatrix(matrix);
    c_QRMatrixUtil.embedBasicPatterns(c_QRVersion.getVersionForNumber(1), matrix);
    String expected =
        " 1 1 1 1 1 1 1 0           0 1 1 1 1 1 1 1\n" +
        " 1 0 0 0 0 0 1 0           0 1 0 0 0 0 0 1\n" +
        " 1 0 1 1 1 0 1 0           0 1 0 1 1 1 0 1\n" +
        " 1 0 1 1 1 0 1 0           0 1 0 1 1 1 0 1\n" +
        " 1 0 1 1 1 0 1 0           0 1 0 1 1 1 0 1\n" +
        " 1 0 0 0 0 0 1 0           0 1 0 0 0 0 0 1\n" +
        " 1 1 1 1 1 1 1 0 1 0 1 0 1 0 1 1 1 1 1 1 1\n" +
        " 0 0 0 0 0 0 0 0           0 0 0 0 0 0 0 0\n" +
        "             1                            \n" +
        "             0                            \n" +
        "             1                            \n" +
        "             0                            \n" +
        "             1                            \n" +
        " 0 0 0 0 0 0 0 0 1                        \n" +
        " 1 1 1 1 1 1 1 0                          \n" +
        " 1 0 0 0 0 0 1 0                          \n" +
        " 1 0 1 1 1 0 1 0                          \n" +
        " 1 0 1 1 1 0 1 0                          \n" +
        " 1 0 1 1 1 0 1 0                          \n" +
        " 1 0 0 0 0 0 1 0                          \n" +
        " 1 1 1 1 1 1 1 0                          \n";
    assertEquals(expected, matrix.toString());
  }

  @Test
  public void testEmbedBasicPatterns2() throws l_ExceptionBarcodeWriter {
    // Version 2.  Position adjustment pattern should apppear at right
    // bottom corner.
    d_MatrixBytes matrix = new d_MatrixBytes(25, 25);
    c_QRMatrixUtil.clearMatrix(matrix);
    c_QRMatrixUtil.embedBasicPatterns(c_QRVersion.getVersionForNumber(2), matrix);
    String expected =
        " 1 1 1 1 1 1 1 0                   0 1 1 1 1 1 1 1\n" +
        " 1 0 0 0 0 0 1 0                   0 1 0 0 0 0 0 1\n" +
        " 1 0 1 1 1 0 1 0                   0 1 0 1 1 1 0 1\n" +
        " 1 0 1 1 1 0 1 0                   0 1 0 1 1 1 0 1\n" +
        " 1 0 1 1 1 0 1 0                   0 1 0 1 1 1 0 1\n" +
        " 1 0 0 0 0 0 1 0                   0 1 0 0 0 0 0 1\n" +
        " 1 1 1 1 1 1 1 0 1 0 1 0 1 0 1 0 1 0 1 1 1 1 1 1 1\n" +
        " 0 0 0 0 0 0 0 0                   0 0 0 0 0 0 0 0\n" +
        "             1                                    \n" +
        "             0                                    \n" +
        "             1                                    \n" +
        "             0                                    \n" +
        "             1                                    \n" +
        "             0                                    \n" +
        "             1                                    \n" +
        "             0                                    \n" +
        "             1                   1 1 1 1 1        \n" +
        " 0 0 0 0 0 0 0 0 1               1 0 0 0 1        \n" +
        " 1 1 1 1 1 1 1 0                 1 0 1 0 1        \n" +
        " 1 0 0 0 0 0 1 0                 1 0 0 0 1        \n" +
        " 1 0 1 1 1 0 1 0                 1 1 1 1 1        \n" +
        " 1 0 1 1 1 0 1 0                                  \n" +
        " 1 0 1 1 1 0 1 0                                  \n" +
        " 1 0 0 0 0 0 1 0                                  \n" +
        " 1 1 1 1 1 1 1 0                                  \n";
    assertEquals(expected, matrix.toString());
  }

  @Test
  public void testEmbedTypeInfo() throws l_ExceptionBarcodeWriter {
    // Type info bits = 100000011001110.
    d_MatrixBytes matrix = new d_MatrixBytes(21, 21);
    c_QRMatrixUtil.clearMatrix(matrix);
    c_QRMatrixUtil.embedTypeInfo(c_QRErrorCorrectionLevel.M, 5, matrix);
    String expected =
        "                 0                        \n" +
        "                 1                        \n" +
        "                 1                        \n" +
        "                 1                        \n" +
        "                 0                        \n" +
        "                 0                        \n" +
        "                                          \n" +
        "                 1                        \n" +
        " 1 0 0 0 0 0   0 1         1 1 0 0 1 1 1 0\n" +
        "                                          \n" +
        "                                          \n" +
        "                                          \n" +
        "                                          \n" +
        "                                          \n" +
        "                 0                        \n" +
        "                 0                        \n" +
        "                 0                        \n" +
        "                 0                        \n" +
        "                 0                        \n" +
        "                 0                        \n" +
        "                 1                        \n";
    assertEquals(expected, matrix.toString());
  }

  @Test
  public void testEmbedVersionInfo() throws l_ExceptionBarcodeWriter {
    // Version info bits = 000111 110010 010100
    // Actually, version 7 QR Code has 45x45 matrix but we use 21x21 here
    // since 45x45 matrix is too big to depict.
    d_MatrixBytes matrix = new d_MatrixBytes(21, 21);
    c_QRMatrixUtil.clearMatrix(matrix);
    c_QRMatrixUtil.maybeEmbedVersionInfo(c_QRVersion.getVersionForNumber(7), matrix);
    String expected =
        "                     0 0 1                \n" +
        "                     0 1 0                \n" +
        "                     0 1 0                \n" +
        "                     0 1 1                \n" +
        "                     1 1 1                \n" +
        "                     0 0 0                \n" +
        "                                          \n" +
        "                                          \n" +
        "                                          \n" +
        "                                          \n" +
        " 0 0 0 0 1 0                              \n" +
        " 0 1 1 1 1 0                              \n" +
        " 1 0 0 1 1 0                              \n" +
        "                                          \n" +
        "                                          \n" +
        "                                          \n" +
        "                                          \n" +
        "                                          \n" +
        "                                          \n" +
        "                                          \n" +
        "                                          \n";
    assertEquals(expected, matrix.toString());
  }

  @Test
  public void testEmbedDataBits() throws l_ExceptionBarcodeWriter {
    // Cells other than basic patterns should be filled with zero.
    d_ArrayBits bits = new d_ArrayBits();
    d_MatrixBytes matrix = new d_MatrixBytes(21, 21);
    c_QRMatrixUtil.clearMatrix(matrix);
    c_QRMatrixUtil.embedBasicPatterns(c_QRVersion.getVersionForNumber(1), matrix);
    c_QRMatrixUtil.embedDataBits(bits, -1, matrix);
    String expected =
        " 1 1 1 1 1 1 1 0 0 0 0 0 0 0 1 1 1 1 1 1 1\n" +
        " 1 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 1\n" +
        " 1 0 1 1 1 0 1 0 0 0 0 0 0 0 1 0 1 1 1 0 1\n" +
        " 1 0 1 1 1 0 1 0 0 0 0 0 0 0 1 0 1 1 1 0 1\n" +
        " 1 0 1 1 1 0 1 0 0 0 0 0 0 0 1 0 1 1 1 0 1\n" +
        " 1 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 1\n" +
        " 1 1 1 1 1 1 1 0 1 0 1 0 1 0 1 1 1 1 1 1 1\n" +
        " 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
        " 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
        " 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
        " 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
        " 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
        " 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
        " 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0\n" +
        " 1 1 1 1 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
        " 1 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
        " 1 0 1 1 1 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
        " 1 0 1 1 1 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
        " 1 0 1 1 1 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
        " 1 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
        " 1 1 1 1 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n";
    assertEquals(expected, matrix.toString());
  }

  @Test
  public void testBuildMatrix() throws l_ExceptionBarcodeWriter {
    // From http://www.swetake.com/qr/qr7.html
    char[] bytes = {32, 65, 205, 69, 41, 220, 46, 128, 236,
        42, 159, 74, 221, 244, 169, 239, 150, 138,
        70, 237, 85, 224, 96, 74, 219 , 61};
    d_ArrayBits bits = new d_ArrayBits();
    for (char c: bytes) {
      bits.appendBits(c, 8);
    }
    d_MatrixBytes matrix = new d_MatrixBytes(21, 21);
    c_QRMatrixUtil.buildMatrix(bits,
                           c_QRErrorCorrectionLevel.H,
                           c_QRVersion.getVersionForNumber(1),  // Version 1
                           3,  // Mask pattern 3
                           matrix);
    String expected =
        " 1 1 1 1 1 1 1 0 0 1 1 0 0 0 1 1 1 1 1 1 1\n" +
        " 1 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 1\n" +
        " 1 0 1 1 1 0 1 0 0 0 0 1 0 0 1 0 1 1 1 0 1\n" +
        " 1 0 1 1 1 0 1 0 0 1 1 0 0 0 1 0 1 1 1 0 1\n" +
        " 1 0 1 1 1 0 1 0 1 1 0 0 1 0 1 0 1 1 1 0 1\n" +
        " 1 0 0 0 0 0 1 0 0 0 1 1 1 0 1 0 0 0 0 0 1\n" +
        " 1 1 1 1 1 1 1 0 1 0 1 0 1 0 1 1 1 1 1 1 1\n" +
        " 0 0 0 0 0 0 0 0 1 1 0 1 1 0 0 0 0 0 0 0 0\n" +
        " 0 0 1 1 0 0 1 1 1 0 0 1 1 1 1 0 1 0 0 0 0\n" +
        " 1 0 1 0 1 0 0 0 0 0 1 1 1 0 0 1 0 1 1 1 0\n" +
        " 1 1 1 1 0 1 1 0 1 0 1 1 1 0 0 1 1 1 0 1 0\n" +
        " 1 0 1 0 1 1 0 1 1 1 0 0 1 1 1 0 0 1 0 1 0\n" +
        " 0 0 1 0 0 1 1 1 0 0 0 0 0 0 1 0 1 1 1 1 1\n" +
        " 0 0 0 0 0 0 0 0 1 1 0 1 0 0 0 0 0 1 0 1 1\n" +
        " 1 1 1 1 1 1 1 0 1 1 1 1 0 0 0 0 1 0 1 1 0\n" +
        " 1 0 0 0 0 0 1 0 0 0 0 1 0 1 1 1 0 0 0 0 0\n" +
        " 1 0 1 1 1 0 1 0 0 1 0 0 1 1 0 0 1 0 0 1 1\n" +
        " 1 0 1 1 1 0 1 0 1 1 0 1 0 0 0 0 0 1 1 1 0\n" +
        " 1 0 1 1 1 0 1 0 1 1 1 1 0 0 0 0 1 1 1 0 0\n" +
        " 1 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 1 0 1 0 0\n" +
        " 1 1 1 1 1 1 1 0 0 0 1 1 1 1 1 0 1 0 0 1 0\n";
    assertEquals(expected, matrix.toString());
  }

  @Test
  public void testFindMSBSet() {
    assertEquals(0, c_QRMatrixUtil.findMSBSet(0));
    assertEquals(1, c_QRMatrixUtil.findMSBSet(1));
    assertEquals(8, c_QRMatrixUtil.findMSBSet(0x80));
    assertEquals(32, c_QRMatrixUtil.findMSBSet(0x80000000));
  }

  @Test
  public void testCalculateBCHCode() {
    // Encoding of type information.
    // From Appendix C in JISX0510:2004 (p 65)
    assertEquals(0xdc, c_QRMatrixUtil.calculateBCHCode(5, 0x537));
    // From http://www.swetake.com/qr/qr6.html
    assertEquals(0x1c2, c_QRMatrixUtil.calculateBCHCode(0x13, 0x537));
    // From http://www.swetake.com/qr/qr11.html
    assertEquals(0x214, c_QRMatrixUtil.calculateBCHCode(0x1b, 0x537));

    // Encoding of version information.
    // From Appendix D in JISX0510:2004 (p 68)
    assertEquals(0xc94, c_QRMatrixUtil.calculateBCHCode(7, 0x1f25));
    assertEquals(0x5bc, c_QRMatrixUtil.calculateBCHCode(8, 0x1f25));
    assertEquals(0xa99, c_QRMatrixUtil.calculateBCHCode(9, 0x1f25));
    assertEquals(0x4d3, c_QRMatrixUtil.calculateBCHCode(10, 0x1f25));
    assertEquals(0x9a6, c_QRMatrixUtil.calculateBCHCode(20, 0x1f25));
    assertEquals(0xd75, c_QRMatrixUtil.calculateBCHCode(30, 0x1f25));
    assertEquals(0xc69, c_QRMatrixUtil.calculateBCHCode(40, 0x1f25));
  }

  // We don't test a lot of cases in this function since we've already
  // tested them in TEST(calculateBCHCode).
  @Test
  public void testMakeVersionInfoBits() throws l_ExceptionBarcodeWriter {
    // From Appendix D in JISX0510:2004 (p 68)
    d_ArrayBits bits = new d_ArrayBits();
    c_QRMatrixUtil.makeVersionInfoBits(c_QRVersion.getVersionForNumber(7), bits);
    assertEquals(" ...XXXXX ..X..X.X ..", bits.toString());
  }

  // We don't test a lot of cases in this function since we've already
  // tested them in TEST(calculateBCHCode).
  @Test
  public void testMakeTypeInfoInfoBits() throws l_ExceptionBarcodeWriter {
    // From Appendix C in JISX0510:2004 (p 65)
    d_ArrayBits bits = new d_ArrayBits();
    c_QRMatrixUtil.makeTypeInfoBits(c_QRErrorCorrectionLevel.M, 5, bits);
    assertEquals(" X......X X..XXX.", bits.toString());
  }
}
