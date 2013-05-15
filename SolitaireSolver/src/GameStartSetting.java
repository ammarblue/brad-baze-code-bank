
// Game: define start configuration

void funcStartStellungEnglish() {

  // Neues Spiel - 
  // Spielfeld Befüllen (= Füllen).

  final int L = 1 ;   // peg
  final int B = 2 ;   // hole
  final int N = 0 ;   // non existing
  // Ziel für 2D englisches Brett
  showAllSolutionsWhenDone=false; 
  constZielStellung = 
    "  ···    ···  ··········o··········  ···    ···  ";
  Fuelle (SpielfeldGlobal, 0, MakeArr (N, N, L, L, L, N, N));
  Fuelle (SpielfeldGlobal, 1, MakeArr (N, N, L, L, L, N, N));
  Fuelle (SpielfeldGlobal, 2, MakeArr (L, L, L, L, L, L, L));
  Fuelle (SpielfeldGlobal, 3, MakeArr (L, L, L, B, L, L, L));
  Fuelle (SpielfeldGlobal, 4, MakeArr (L, L, L, L, L, L, L));
  Fuelle (SpielfeldGlobal, 5, MakeArr (N, N, L, L, L, N, N));
  Fuelle (SpielfeldGlobal, 6, MakeArr (N, N, L, L, L, N, N));
}

void funcStartStellungCross() {

  // Neues Spiel - 
  // Spielfeld Befüllen (= Füllen).

  final int P = 1 ;   // peg
  final int H = 2 ;   // hole
  final int N = 0 ;   // non existing
  //
  showAllSolutionsWhenDone=true; 
  // Ziel für 2D englisches Brett
  constZielStellung = 
    "  ···    ···  ··········o··········  ···    ···  ";
  Fuelle (SpielfeldGlobal, 0, MakeArr (N, N, H, H, H, N, N));
  Fuelle (SpielfeldGlobal, 1, MakeArr (N, N, H, P, H, N, N));
  Fuelle (SpielfeldGlobal, 2, MakeArr (H, H, P, P, P, H, H));
  Fuelle (SpielfeldGlobal, 3, MakeArr (H, H, H, P, H, H, H));
  Fuelle (SpielfeldGlobal, 4, MakeArr (H, H, H, P, H, H, H));
  Fuelle (SpielfeldGlobal, 5, MakeArr (N, N, H, H, H, N, N));
  Fuelle (SpielfeldGlobal, 6, MakeArr (N, N, H, H, H, N, N));
}

void funcStartStellungFireplace() {

  // Neues Spiel -
  // Spielfeld Befüllen (= Füllen).

  final int P = 1 ;   // peg
  final int H = 2 ;   // hole
  final int N = 0 ;   // non existing
  //
  showAllSolutionsWhenDone=true; 
  // Ziel für 2D englisches Brett
  constZielStellung = 
    "  ···    ···  ··········o··········  ···    ···  ";
  Fuelle (SpielfeldGlobal, 0, MakeArr (N, N, P, P, P, N, N));
  Fuelle (SpielfeldGlobal, 1, MakeArr (N, N, P, P, P, N, N));
  Fuelle (SpielfeldGlobal, 2, MakeArr (H, H, P, P, P, H, H));
  Fuelle (SpielfeldGlobal, 3, MakeArr (H, H, P, H, P, H, H));
  Fuelle (SpielfeldGlobal, 4, MakeArr (H, H, H, H, H, H, H));
  Fuelle (SpielfeldGlobal, 5, MakeArr (N, N, H, H, H, N, N));
  Fuelle (SpielfeldGlobal, 6, MakeArr (N, N, H, H, H, N, N));
}

void Fuelle(int [][] LokalesFeld, int Zeile, int[] arrWerte) {
  //     Befüllt ein Spielfeld.
  //     Parameter: Spielfeld,
  //     Zeile des Spielfeldes, Z_Wert und ParamArray
  //     (Parameter-Datenfeld) arrWerte
  //     mit den einzutragenden Werten.

  // If UBound(arrWerte) <> intConstEnde Then MsgBox "Fehler 610"
  // If Zeile > intConstEnde Then MsgBox "Fehler 610a"

  for (int i = 0; i < arrWerte.length; i++ ) {
    LokalesFeld[i][Zeile] = arrWerte[i];
  }
} // func 

int[] MakeArr(int n0, int n1, int n2, int n3, int n4, int n5, int n6) {
  // returns an array from 7 single ints
  int[] arrN = new int [7]; 
  arrN[0] = n0;
  arrN[1] = n1;
  arrN[2] = n2;
  arrN[3] = n3;
  arrN[4] = n4;
  arrN[5] = n5;
  arrN[6] = n6;
  return(arrN);
}

