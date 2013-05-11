package reuze.awt;

import com.software.reuze.ga_Curve;
/*
** Copyright 2005 Huxtable.com. All rights reserved.
*/
import com.software.reuze.ib_a_FilterTransfer;

public class ib_FilterCurves extends ib_a_FilterTransfer {

      private ga_Curve[] curves = new ga_Curve[1];
      
    public ib_FilterCurves() {
        curves = new ga_Curve[3];
        curves[0] = new ga_Curve();
        curves[1] = new ga_Curve();
        curves[2] = new ga_Curve();
    }
    
      protected void initialize() {
            initialized = true;
            if ( curves.length == 1 )
            rTable = gTable = bTable = curves[0].makeTable();
        else {
            rTable = curves[0].makeTable();
            gTable = curves[1].makeTable();
            bTable = curves[2].makeTable();
        }
      }

      public void setCurve( ga_Curve curve ) {
        curves = new ga_Curve[] { curve };
            initialized = false;
      }
      
      public void setCurves( ga_Curve[] curves ) {
            if ( curves == null || (curves.length != 1 && curves.length != 3) )
            throw new IllegalArgumentException( "Curves must be length 1 or 3" );
        this.curves = curves;
            initialized = false;
      }
      
      public ga_Curve[] getCurves() {
            return curves;
      }

      public String toString() {
            return "Colors/Curves...";
      }

}
