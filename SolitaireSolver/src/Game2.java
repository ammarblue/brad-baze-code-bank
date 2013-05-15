
// Game : Main Game solver

String a1;
int[][] SpielfeldKopie;

void SpielRekursiv(int[][] LokalesSpielfeld, int LokaleZugNr, String strLogEinzelnesSpiel1) {
  // Spielt rekursiv, also mit Rekursion.
  // Backtracking.
  // Parameter: Spielfeld, ZugNr des aktuellen Feldes und
  // strLösungsweg. 
  //
  if (!programEnd) {
    // Is it solved? 
    if (constZielStellung.equals(Join2DArray(LokalesSpielfeld))) {
      // solved! 
      NumberSolutions = NumberSolutions + 1;
      strLogEinzelnesSpiel1 += "," + Join2DArray(LokalesSpielfeld);
      Solutions += "#" + strLogEinzelnesSpiel1;
      LoesungAusgeben (strLogEinzelnesSpiel1);
      if (!showAllSolutionsWhenDone)
        programEnd = true;
      // exit();
    } // if 
    //
    // search further 
    if (IstEinWeitererZugMoeglich(LokalesSpielfeld)) {

      for (int x1 = 0; x1 < intConstEnde; x1 = x1+1) {
        for (int y1 = 0; y1 < intConstEnde; y1 = y1+1) {
          if (LokalesSpielfeld [x1][y1] == constSpielfeldBesetzt) {
            for (int Richtung = constNorden; Richtung < intMaxRichtung+1; Richtung = Richtung+1) {
              if (ZugMoeglich(LokalesSpielfeld, x1, y1, Richtung)) {
                // make String
                a1 = strLogEinzelnesSpiel1 + "," + Join2DArray(LokalesSpielfeld);
                // Kopie erstellen
                SpielfeldKopie = arrayCopy2D ( LokalesSpielfeld );
                // make move
                SpielfeldKopie = ZugAusfuehren(SpielfeldKopie, x1, y1, Richtung);
                // Rekursiver Aufruf
                SpielRekursiv (SpielfeldKopie, 
                LokaleZugNr + 1, 
                a1);
              } // End if
            } // Next
          } // End if
        } // Next
      } // Next
    } // if 
    else 
    {
      // game over: not solved and no further move possible
      // println("This branch is over");
      intEndstellungenCounter++;
    } // if else
  } // if
} // function
//

