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

package com.software.reuze;

import com.software.reuze.c_QRErrorCorrectionLevel;
import com.software.reuze.c_QRMode;
import com.software.reuze.c_QRVersion;

/**
 * @author satorux@google.com (Satoru Takabayashi) - creator
 * @author dswitkin@google.com (Daniel Switkin) - ported from C++
 */
public final class c_QR {

  public static final int NUM_MASK_PATTERNS = 8;

  private c_QRMode mode;
  private c_QRErrorCorrectionLevel ecLevel;
  private c_QRVersion version;
  private int maskPattern;
  private d_MatrixBytes matrix;

  public c_QR() {
    maskPattern = -1;
  }

  public c_QRMode getMode() {
    return mode;
  }

  public c_QRErrorCorrectionLevel getECLevel() {
    return ecLevel;
  }

  public c_QRVersion getVersion() {
    return version;
  }

  public int getMaskPattern() {
    return maskPattern;
  }

  public d_MatrixBytes getMatrix() {
    return matrix;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder(200);
    result.append("<<\n");
    result.append(" mode: ");
    result.append(mode);
    result.append("\n ecLevel: ");
    result.append(ecLevel);
    result.append("\n version: ");
    result.append(version);
    result.append("\n maskPattern: ");
    result.append(maskPattern);
    if (matrix == null) {
      result.append("\n matrix: null\n");
    } else {
      result.append("\n matrix:\n");
      result.append(matrix.toString());
    }
    result.append(">>\n");
    return result.toString();
  }

  public void setMode(c_QRMode value) {
    mode = value;
  }

  public void setECLevel(c_QRErrorCorrectionLevel value) {
    ecLevel = value;
  }

  public void setVersion(c_QRVersion version) {
    this.version = version;
  }

  public void setMaskPattern(int value) {
    maskPattern = value;
  }

  public void setMatrix(d_MatrixBytes value) {
    matrix = value;
  }

  // Check if "mask_pattern" is valid.
  public static boolean isValidMaskPattern(int maskPattern) {
    return maskPattern >= 0 && maskPattern < NUM_MASK_PATTERNS;
  }

}
