
// ----------------------------------------------------------
// output for game 

void Output(String text1, int [][] Spielfeld) {
  // 
  print (text1); // "Start:  ");
  String strBuffer="";
  for (int y = 0; y < intConstEnde; y = y+1) { //     For y = 0 To intConstEnde
    for (int x = 0; x < intConstEnde; x = x+1) { //       For x = 0 To intConstEnde
      strBuffer = strBuffer + PrintFunktion(Spielfeld[x][y]);
    } // for
  } //  for
  println(strBuffer);
  // println("------------------------------------------");  
  print ("Target: ");
  println( constZielStellung );
  strBuffer="";
  println (text1); // "Target shown as Board:  ");
  for (int y = 0; y < intConstEnde; y = y+1) { //     For y = 0 To intConstEnde
    for (int x = 0; x < intConstEnde; x = x+1) { //       For x = 0 To intConstEnde
      strBuffer = strBuffer + PrintFunktion(Spielfeld[x][y]);
    } // for
    strBuffer = strBuffer + "\n";
  } //  for
  println(strBuffer);
}

void LoesungAusgeben (String strA) {
  // createOutput ()
  //
  // println("strA = "+strA);
  //
  String[] list1 = { 
    strA
  };
  list1=split(strA, ',');
  // now write the strings to a file, each on a separate line
  // saveStrings ("c:\\soft\\SolProcess\\result" + Date1() + "_" + Now1() + ".txt", list1);
  // saveStrings ("result" + Date1() + "_" + Now1() + ".txt", list1);
  saveStrings ("result.txt", list1);
}

