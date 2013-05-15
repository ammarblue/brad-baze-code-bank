
// Menu: Rectangle 

class Rectangle {

  int x, y, w, h; 
  String textRect="";
  String textStatusBar="";

  // constr
  Rectangle( int _x, int _y, 
  int _w, int _h, 
  String _text, 
  String _textStatusBar) {
    x=_x;
    y=_y;
    w=_w;
    h=_h;
    textRect=_text;
    textStatusBar=_textStatusBar;
  }  // constr
  //
  void drawRect() {
    noFillOrLightColorDependingOnMouseOver();
    stroke(255);
    rect(x, y, w, h);
    if (overRect()) {
      fill(255);
      textSize(17);      
      textAlign(LEFT);      
      text(textStatusBar, 15, height-15);
    }
  } // method 

  void drawRectText() {
    if (!textRect.equals("")) {
      fill(255);
      textSize(17);      
      textAlign(CENTER);  
      text(textRect, x, y, w, h);
    }
  } // method  

  boolean overRect() {
    if (mouseX>x && mouseX<x+w && 
      mouseY>y && mouseY<y+h) 
    {
      return true;
    } 
    else 
    {
      return false;
    } // if else
  } // method  
  //
  void noFillOrLightColorDependingOnMouseOver() {
    if (overRect()) 
      fill(255, 2, 2, 52);
    else   
      noFill();
  } // method
  //
} // class
//

