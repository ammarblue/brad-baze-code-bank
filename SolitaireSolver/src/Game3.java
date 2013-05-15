

// --------------------------------------------------------------------------------
// Help funcs for Game


boolean IstEinWeitererZugMoeglich(int[][] Spielfeld) {

  // Prüft, ob ein weiterer Zug möglich ist
  // oder ob das Spielende eingetreten ist
  // (erfolgreich oder erfolglos ist hier egal).

  String strA;
  Boolean boolZugMoeglich;

  strA = "";
  boolZugMoeglich = false;

  for (int x = 0; x < intConstEnde; x = x+1) {
    for (int y = 0; y < intConstEnde; y = y+1) {
      if (Spielfeld[x][y] == constSpielfeldBesetzt) { 
        // Nord Osten Sueden Westen
        for (int Richtung = constNorden; Richtung < intMaxRichtung+1; Richtung = Richtung+1) { 
          if (ZugMoeglich(Spielfeld, x, y, Richtung)) {
            boolZugMoeglich = true;
            break;
          }
        } // for
      } // End if
    } // for
    if (boolZugMoeglich) { 
      break;
    }
  } // for
  return (boolZugMoeglich);
}

// -------------------------------------------------------------------------

boolean ZugMoeglich (int [][] Spielfeld, int x, int y, int Richtung) {

  // Zug möglich?
  // Richtung wird fest übergeben.
  // Richtung als 0=Nord 1=Osten 2=Sueden 3=Westen etc. 

  int xTest;
  int yTest;
  int zTest;

  Boolean Buffer = false;
  //  int Dummy;

  if (IstZutreffend(Spielfeld, x, y, constSpielfeldBesetzt)) {

    // if ZugMoeglichFesteReihenfolge(Spielfeld, x, y, z, Dummy) Then

    switch ( Richtung ) {
    case constNorden:
      xTest = x;
      yTest = y - 2;
      if (IstZutreffend(Spielfeld, xTest, yTest, constSpielfeldLeer) ) {
        if (IstZutreffend(Spielfeld, xTest, y - 1, constSpielfeldBesetzt) ) {
          Buffer = true; //  Exit For
        } // End if
      } // End if
      break; 
    case constOsten:
      xTest = x + 2;
      yTest = y;
      if (IstZutreffend(Spielfeld, xTest, yTest, constSpielfeldLeer)  ) {
        if (IstZutreffend(Spielfeld, x + 1, y, constSpielfeldBesetzt)  ) {
          Buffer = true;
        } // End if
      } // End if
      break; 
    case constSueden:
      xTest = x;
      yTest = y + 2;
      if (IstZutreffend(Spielfeld, xTest, yTest, constSpielfeldLeer)  ) {
        if (IstZutreffend(Spielfeld, xTest, y + 1, constSpielfeldBesetzt)  ) { 
          Buffer = true;
        } // End if
      } // End if
      break; 
    case constWesten:
      xTest = x - 2;
      yTest = y;
      if (IstZutreffend(Spielfeld, xTest, yTest, constSpielfeldLeer)  ) {
        if (IstZutreffend(Spielfeld, x - 1, y, constSpielfeldBesetzt)  ) { 
          Buffer = true;
        } // End if
      } // End if
      break; 
      // ____________________________________________________________________________

    case constNordost:
      xTest = x + 2;
      yTest = y - 2;
      if (IstZutreffend(Spielfeld, xTest, yTest, constSpielfeldLeer)  ) {
        if (IstZutreffend(Spielfeld, x + 1, y - 1, constSpielfeldBesetzt)  ) {
          Buffer = true;
        } // End if
      } // End if
      break; 
    case constSuedost:
      xTest = x + 2;
      yTest = y + 2;
      if (IstZutreffend(Spielfeld, xTest, yTest, constSpielfeldLeer)  ) {
        if (IstZutreffend(Spielfeld, x + 1, y + 1, constSpielfeldBesetzt)  ) {
          Buffer = true;
        } // End if
      } // End if
      break; 
    case constSuedwest:
      xTest = x - 2;
      yTest = y + 2;
      if (IstZutreffend(Spielfeld, xTest, yTest, constSpielfeldLeer)  ) {
        if (IstZutreffend(Spielfeld, x - 1, y + 1, constSpielfeldBesetzt)  ) {
          Buffer = true;
        } // End if
      } // End if
      break; 
    case constNordwest:
      xTest = x - 2;
      yTest = y - 2;
      if (IstZutreffend(Spielfeld, xTest, yTest, constSpielfeldLeer)  ) {
        if (IstZutreffend(Spielfeld, x - 1, y - 1, constSpielfeldBesetzt)  ) {
          Buffer = true;
        } // End if
      } // End if
      break; 
    default: 
      println ("Fehler 714  ++++++++++++++++++++++++++++++++++++++++++++");
      break;
    } // End switch

    //        Else
    //            MsgBox "Test: "
    //        End if
  } 

  // ZugMoeglich = Buffer
  return (Buffer);
}

int[][] ZugAusfuehren(int[][] LokalesFeld, int x, int y, int Richtung) {
  // ByVal!

  // Den Zug ausführen.
  // Beim rekursiven Backtracking ist unbedingt
  // Aufruf per ByVal nötig!

  int intZielX;
  int intZielY;
  int intZielZ;

  // Richtungen in der Reihenfolge:
  // Nord Osten Sueden Westen.
  // Prüft Zielfeld und zu
  // überspringendes Feld
  // und setzt dann.

  switch (Richtung) {
  case constNorden:
    intZielX = x;
    intZielY = y - 2;
    if (IstFeldPosition(intZielX, intZielY )) {
      LokalesFeld[intZielX][intZielY] = constSpielfeldBesetzt;
    } // End if
    LokalesFeld [x] [y] = constSpielfeldLeer;
    LokalesFeld [x][y - 1] = constSpielfeldLeer;
    break; 
  case constOsten:
    intZielX = x + 2;
    intZielY = y;
    if (IstFeldPosition(intZielX, intZielY )) {
      LokalesFeld [intZielX][ intZielY] = constSpielfeldBesetzt;
    } // End if
    LokalesFeld [x] [y] = constSpielfeldLeer;
    LokalesFeld [x + 1][ y] = constSpielfeldLeer;
    break; 
  case constSueden:
    intZielX = x;
    intZielY = y + 2;
    if (IstFeldPosition(intZielX, intZielY )) {
      LokalesFeld [intZielX][ intZielY]  = constSpielfeldBesetzt;
    } // End if
    LokalesFeld[x][ y] = constSpielfeldLeer;
    LokalesFeld[x][ y + 1] = constSpielfeldLeer;
    break; 
  case constWesten:
    intZielX = x - 2;
    intZielY = y;
    if (IstFeldPosition(intZielX, intZielY)) {
      LokalesFeld[intZielX][ intZielY] = constSpielfeldBesetzt;
    } // End if
    LokalesFeld[x][ y] = constSpielfeldLeer;
    LokalesFeld[x - 1][ y] = constSpielfeldLeer;
    break; 

    // ___________________________________________________________________________

  case constNordost:
    intZielX = x + 2;
    intZielY = y - 2;
    if (IstFeldPosition(intZielX, intZielY )) {
      LokalesFeld[intZielX][ intZielY] = constSpielfeldBesetzt;
    } // End if
    LokalesFeld[x][ y] = constSpielfeldLeer;
    LokalesFeld[x + 1][ y - 1] = constSpielfeldLeer;
    break; 
  case constSuedost:
    intZielX = x + 2;
    intZielY = y + 2;
    if (IstFeldPosition(intZielX, intZielY )) {
      LokalesFeld[intZielX][ intZielY] = constSpielfeldBesetzt;
    } // End if
    LokalesFeld[x][ y] = constSpielfeldLeer;
    LokalesFeld[x + 1][ y + 1] = constSpielfeldLeer;
    break; 
  case constSuedwest:
    intZielX = x - 2;
    intZielY = y + 2;
    if (IstFeldPosition(intZielX, intZielY )) {
      LokalesFeld[intZielX][ intZielY] = constSpielfeldBesetzt;
    } // End if
    LokalesFeld[x][ y] = constSpielfeldLeer;
    LokalesFeld[x - 1][ y + 1] = constSpielfeldLeer;
    break; 
  case constNordwest:
    intZielX = x - 2;
    intZielY = y - 2;
    if (IstFeldPosition(intZielX, intZielY )) {
      LokalesFeld[intZielX][ intZielY] = constSpielfeldBesetzt;
    } // End if
    LokalesFeld[x][ y] = constSpielfeldLeer;
    LokalesFeld[x - 1][ y - 1] = constSpielfeldLeer;
    break; 

  default:
    println ( "Fehler 55 ++++++++++++++++++++++++++++++++++++++" ) ; 
    break;
  } // End Switch

  return (LokalesFeld);
}

