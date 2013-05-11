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

import com.software.reuze.c_QRMaskPenalty;
import com.software.reuze.d_MatrixBytes;


/**
 * @author satorux@google.com (Satoru Takabayashi) - creator
 * @author mysen@google.com (Chris Mysen) - ported from C++
 */

public final class MaskUtilTest extends TestCase {

  @Test
  public void testApplyMaskPenaltyRule1() {
    {
      d_MatrixBytes matrix = new d_MatrixBytes(4, 1);
      matrix.set(0, 0, 0);
      matrix.set(1, 0, 0);
      matrix.set(2, 0, 0);
      matrix.set(3, 0, 0);
      assertEquals(0, c_QRMaskPenalty.applyMaskPenaltyRule1(matrix));
    }
    {  // Horizontal.
      d_MatrixBytes matrix = new d_MatrixBytes(6, 1);
      matrix.set(0, 0, 0);
      matrix.set(1, 0, 0);
      matrix.set(2, 0, 0);
      matrix.set(3, 0, 0);
      matrix.set(4, 0, 0);
      matrix.set(5, 0, 1);
      assertEquals(3, c_QRMaskPenalty.applyMaskPenaltyRule1(matrix));
      matrix.set(5, 0, 0);
      assertEquals(4, c_QRMaskPenalty.applyMaskPenaltyRule1(matrix));
    }
    {  // Vertical.
      d_MatrixBytes matrix = new d_MatrixBytes(1, 6);
      matrix.set(0, 0, 0);
      matrix.set(0, 1, 0);
      matrix.set(0, 2, 0);
      matrix.set(0, 3, 0);
      matrix.set(0, 4, 0);
      matrix.set(0, 5, 1);
      assertEquals(3, c_QRMaskPenalty.applyMaskPenaltyRule1(matrix));
      matrix.set(0, 5, 0);
      assertEquals(4, c_QRMaskPenalty.applyMaskPenaltyRule1(matrix));
    }
  }

  @Test
  public void testApplyMaskPenaltyRule2() {
    {
      d_MatrixBytes matrix = new d_MatrixBytes(1, 1);
      matrix.set(0, 0, 0);
      assertEquals(0, c_QRMaskPenalty.applyMaskPenaltyRule2(matrix));
    }
    {
      d_MatrixBytes matrix = new d_MatrixBytes(2, 2);
      matrix.set(0, 0, 0);
      matrix.set(1, 0, 0);
      matrix.set(0, 1, 0);
      matrix.set(1, 1, 1);
      assertEquals(0, c_QRMaskPenalty.applyMaskPenaltyRule2(matrix));
    }
    {
      d_MatrixBytes matrix = new d_MatrixBytes(2, 2);
      matrix.set(0, 0, 0);
      matrix.set(1, 0, 0);
      matrix.set(0, 1, 0);
      matrix.set(1, 1, 0);
      assertEquals(3, c_QRMaskPenalty.applyMaskPenaltyRule2(matrix));
    }
    {
      d_MatrixBytes matrix = new d_MatrixBytes(3, 3);
      matrix.set(0, 0, 0);
      matrix.set(1, 0, 0);
      matrix.set(2, 0, 0);
      matrix.set(0, 1, 0);
      matrix.set(1, 1, 0);
      matrix.set(2, 1, 0);
      matrix.set(0, 2, 0);
      matrix.set(1, 2, 0);
      matrix.set(2, 2, 0);
      // Four instances of 2x2 blocks.
      assertEquals(3 * 4, c_QRMaskPenalty.applyMaskPenaltyRule2(matrix));
    }
  }

  @Test
  public void testApplyMaskPenaltyRule3() {
    {
      // Horizontal 00001011101.
      d_MatrixBytes matrix = new d_MatrixBytes(11, 1);
      matrix.set(0, 0, 0);
      matrix.set(1, 0, 0);
      matrix.set(2, 0, 0);
      matrix.set(3, 0, 0);
      matrix.set(4, 0, 1);
      matrix.set(5, 0, 0);
      matrix.set(6, 0, 1);
      matrix.set(7, 0, 1);
      matrix.set(8, 0, 1);
      matrix.set(9, 0, 0);
      matrix.set(10, 0, 1);
      assertEquals(40, c_QRMaskPenalty.applyMaskPenaltyRule3(matrix));
    }
    {
      // Horizontal 10111010000.
      d_MatrixBytes matrix = new d_MatrixBytes(11, 1);
      matrix.set(0, 0, 1);
      matrix.set(1, 0, 0);
      matrix.set(2, 0, 1);
      matrix.set(3, 0, 1);
      matrix.set(4, 0, 1);
      matrix.set(5, 0, 0);
      matrix.set(6, 0, 1);
      matrix.set(7, 0, 0);
      matrix.set(8, 0, 0);
      matrix.set(9, 0, 0);
      matrix.set(10, 0, 0);
      assertEquals(40, c_QRMaskPenalty.applyMaskPenaltyRule3(matrix));
    }
    {
      // Vertical 00001011101.
      d_MatrixBytes matrix = new d_MatrixBytes(1, 11);
      matrix.set(0, 0, 0);
      matrix.set(0, 1, 0);
      matrix.set(0, 2, 0);
      matrix.set(0, 3, 0);
      matrix.set(0, 4, 1);
      matrix.set(0, 5, 0);
      matrix.set(0, 6, 1);
      matrix.set(0, 7, 1);
      matrix.set(0, 8, 1);
      matrix.set(0, 9, 0);
      matrix.set(0, 10, 1);
      assertEquals(40, c_QRMaskPenalty.applyMaskPenaltyRule3(matrix));
    }
    {
      // Vertical 10111010000.
      d_MatrixBytes matrix = new d_MatrixBytes(1, 11);
      matrix.set(0, 0, 1);
      matrix.set(0, 1, 0);
      matrix.set(0, 2, 1);
      matrix.set(0, 3, 1);
      matrix.set(0, 4, 1);
      matrix.set(0, 5, 0);
      matrix.set(0, 6, 1);
      matrix.set(0, 7, 0);
      matrix.set(0, 8, 0);
      matrix.set(0, 9, 0);
      matrix.set(0, 10, 0);
      assertEquals(40, c_QRMaskPenalty.applyMaskPenaltyRule3(matrix));
    }
  }

  @Test
  public void testApplyMaskPenaltyRule4() {
    {
      // Dark cell ratio = 0%
      d_MatrixBytes matrix = new d_MatrixBytes(1, 1);
      matrix.set(0, 0, 0);
      assertEquals(100, c_QRMaskPenalty.applyMaskPenaltyRule4(matrix));
    }
    {
      // Dark cell ratio = 5%
      d_MatrixBytes matrix = new d_MatrixBytes(2, 1);
      matrix.set(0, 0, 0);
      matrix.set(0, 0, 1);
      assertEquals(0, c_QRMaskPenalty.applyMaskPenaltyRule4(matrix));
    }
    {
      // Dark cell ratio = 66.67%
      d_MatrixBytes matrix = new d_MatrixBytes(6, 1);
      matrix.set(0, 0, 0);
      matrix.set(1, 0, 1);
      matrix.set(2, 0, 1);
      matrix.set(3, 0, 1);
      matrix.set(4, 0, 1);
      matrix.set(5, 0, 0);
      assertEquals(30, c_QRMaskPenalty.applyMaskPenaltyRule4(matrix));
    }
  }

  private static boolean TestGetDataMaskBitInternal(int maskPattern,
                                         int[][] expected) {
    for (int x = 0; x < 6; ++x) {
      for (int y = 0; y < 6; ++y) {
        if ((expected[y][x] == 1) !=
            c_QRMaskPenalty.getDataMaskBit(maskPattern, x, y)) {
          return false;
        }
      }
    }
    return true;
  }

  // See mask patterns on the page 43 of JISX0510:2004.
  @Test
  public void testGetDataMaskBit() {
    int[][] mask0 = {
      {1, 0, 1, 0, 1, 0},
      {0, 1, 0, 1, 0, 1},
      {1, 0, 1, 0, 1, 0},
      {0, 1, 0, 1, 0, 1},
      {1, 0, 1, 0, 1, 0},
      {0, 1, 0, 1, 0, 1},
    };
    assertTrue(TestGetDataMaskBitInternal(0,  mask0));
    int[][] mask1 = {
      {1, 1, 1, 1, 1, 1},
      {0, 0, 0, 0, 0, 0},
      {1, 1, 1, 1, 1, 1},
      {0, 0, 0, 0, 0, 0},
      {1, 1, 1, 1, 1, 1},
      {0, 0, 0, 0, 0, 0},
    };
    assertTrue(TestGetDataMaskBitInternal(1,  mask1));
    int[][] mask2 = {
      {1, 0, 0, 1, 0, 0},
      {1, 0, 0, 1, 0, 0},
      {1, 0, 0, 1, 0, 0},
      {1, 0, 0, 1, 0, 0},
      {1, 0, 0, 1, 0, 0},
      {1, 0, 0, 1, 0, 0},
    };
    assertTrue(TestGetDataMaskBitInternal(2, mask2));
    int[][] mask3 = {
      {1, 0, 0, 1, 0, 0},
      {0, 0, 1, 0, 0, 1},
      {0, 1, 0, 0, 1, 0},
      {1, 0, 0, 1, 0, 0},
      {0, 0, 1, 0, 0, 1},
      {0, 1, 0, 0, 1, 0},
    };
    assertTrue(TestGetDataMaskBitInternal(3, mask3));
    int[][] mask4 = {
      {1, 1, 1, 0, 0, 0},
      {1, 1, 1, 0, 0, 0},
      {0, 0, 0, 1, 1, 1},
      {0, 0, 0, 1, 1, 1},
      {1, 1, 1, 0, 0, 0},
      {1, 1, 1, 0, 0, 0},
    };
    assertTrue(TestGetDataMaskBitInternal(4, mask4));
    int[][] mask5 = {
      {1, 1, 1, 1, 1, 1},
      {1, 0, 0, 0, 0, 0},
      {1, 0, 0, 1, 0, 0},
      {1, 0, 1, 0, 1, 0},
      {1, 0, 0, 1, 0, 0},
      {1, 0, 0, 0, 0, 0},
    };
    assertTrue(TestGetDataMaskBitInternal(5, mask5));
    int[][] mask6 = {
      {1, 1, 1, 1, 1, 1},
      {1, 1, 1, 0, 0, 0},
      {1, 1, 0, 1, 1, 0},
      {1, 0, 1, 0, 1, 0},
      {1, 0, 1, 1, 0, 1},
      {1, 0, 0, 0, 1, 1},
    };
    assertTrue(TestGetDataMaskBitInternal(6, mask6));
    int[][] mask7 = {
      {1, 0, 1, 0, 1, 0},
      {0, 0, 0, 1, 1, 1},
      {1, 0, 0, 0, 1, 1},
      {0, 1, 0, 1, 0, 1},
      {1, 1, 1, 0, 0, 0},
      {0, 1, 1, 1, 0, 0},
    };
    assertTrue(TestGetDataMaskBitInternal(7, mask7));
  }
}
