

// Game: Smaller Tools 

String Join2DArray(int [][] LokalesFeld) { 

  // Speichert alle Spielpositionen ab, wobei ihre Position
  // (von links oben an linear hochgezählt, also von 1 bis 49)
  // der Position im string strBuffer enstpricht.
  // Dabei ist " " = nicht vorhanden, "o" = Kugel
  // und der "Mittelpunkt" strMidDot = leeres Feld.
  // Function ist ähnlich dem Befehl Join (Gegenteil
  // von Split).
  // 3D analog mit rund 135 Positionen.
  // 01 02 03 04 05 06 07
  // 08 09 10 11 12 13 14
  // 15 16 17 18 19 20 21
  // 22 23....

  String strBuffer = ""; 

  for (int y = 0; y < intConstEnde; y = y+1) { //     For y = 0 To intConstEnde
    for (int x = 0; x < intConstEnde; x = x+1) { //       For x = 0 To intConstEnde
      strBuffer = strBuffer + PrintFunktion(LokalesFeld[x][y]);
    } // for
  } //  for
  return (strBuffer);
}

String PrintFunktion(int byteMy) {

  String Buffer = ""; 

  switch (byteMy) {
  case constSpielfeldNichtVorhanden: //    ' Kein Spielfeld (sondern Ecken)
    Buffer = " ";  //  ' Leerzeichen
    break;
  case constSpielfeldBesetzt: //  ' Spielfeld besetzt
    Buffer = "o"; //     ' "o"
    break;
  case constSpielfeldLeer:  //   ' Spielfeld leer
    Buffer = middlePoint; //  ' "."
    break;
  default: 
    println ( "Fehler 65  " + byteMy);
    break;
  } // switch 
  return (Buffer);
}

void CheckconstZielStellung() {

  // verschiedene Tests für constZielStellung
  String DarstellungDesStartfeldesAlsString      ;

  if (constZielStellung.equals("")) {
    println ("constZielStellung ist leer. Programmfehler, daher Abbruch." ); 
    println ("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");    
    exit();
  }

  DarstellungDesStartfeldesAlsString = Join2DArray(SpielfeldGlobal);

  if (constZielStellung.length() != DarstellungDesStartfeldesAlsString.length() ) {
    println ( "Länge von constZielStellung ist ungleich der Länge des Startspielfeldes. " + 
      "Programmfehler, daher Abbruch.");
    exit() ;
  }

  if (CountString(constZielStellung, "o") != 1) {
    println ( "Mehr als eine Kugel (oder keine Kugel) im Zielfeld. " + 
      "Möglicher Programmfehler. Kein Abbruch." );
  }

  if (CountString(constZielStellung, " ") != CountString(DarstellungDesStartfeldesAlsString, " ")) {
    println ("Die Anzahl der nicht vorhandenen Felder ist zwischen Ziel-Stellung und dem Startspielfeld ungleich. " +
      "Möglicher Programmfehler. Kein Abbruch. "  + 
      "Solitaire - Check constZielStellung: nicht vorhandene Felder. ");
  }
}

// -----------------------------------------------------------------------

int CountString(String SearchIn, String SearchWhat) {
  // function can count the occurence of one String in another
  int Buffer = 0; 

  String[][] m1 = matchAll(SearchIn, SearchWhat);
  if (m1 != null) { 
    Buffer = m1.length;
  } 
  else { 
    // If the result is null, then the sequence did not match at all.
    Buffer = 0;
  }
  return ( Buffer );
}

// -----------------------------------------------------------------------

boolean IstZutreffend(int[][] Spielfeld, int xTest, int yTest, int Wert) {

  //    ' Prüft a) ob xTest, yTest ein gültiges Feld ist und
  //    ' wenn ja, b) ob dieses gültige Feld den Wert "Wert"
  //    ' besitzt. "Wert" kann also den Wert leer oder voll
  //    ' besitzen (constSpielfeldLeer oder constSpielfeldBesetzt).

  boolean Buffer = false;
  // println (xTest +","+ yTest);
  if (IstFeldPosition(xTest, yTest)) {
    if (Spielfeld[xTest][yTest] == Wert ) {
      Buffer = true;
    } // End If
  }
  return Buffer;
}

Boolean IstFeldPosition(int xTest, int yTest ) {

  //'    Dim Buffer  As Boolean
  //'    Buffer = True
  //'    If xTest < 0 Then Buffer = False
  //'    If yTest < 0 Then Buffer = False
  //'    If zTest < 0 Then Buffer = False
  //'    If xTest > intConstEnde Then Buffer = False
  //'    If yTest > intConstEnde Then Buffer = False
  //'    If zTest > intConstEnde Then Buffer = False
  //'    IstFeldPosition = Buffer

  boolean Buffer = true;

  if (xTest < 0 ) {
    Buffer = false;
  }
  else if (yTest < 0 ) {
    Buffer = false;
  }
  else if (xTest >= intConstEnde ) {
    Buffer = false;
  }
  else if (yTest >= intConstEnde ) {
    Buffer = false;
  }
  // return value: 
  /*
  if (!Buffer) 
   {
   print(xTest);
   println(yTest);
   }
   */
  return Buffer;
}

int[][] arrayCopy2D ( int[][] src ) // , int[][] dest ) 
{
  int[][] dest;
  dest=new int [intConstEnde][intConstEnde];
  for (int i=0; i<src.length; i++) {
    arraycopy(src[i], dest[i]);
  } // for
  return dest;
} // func
//
//
String Now1 () {

  int s = second();  // Values from 0 - 59
  int m = minute();  // Values from 0 - 59
  int h = hour();    // Values from 0 - 23
  return (h + ":" + m + ":" + s);
}

String Date1 () {

  int y = year();  // Values from
  int m = month();  // Values from
  int d = day();    // Values from

  return (y + "_" + m + "_" + d);
}
//
