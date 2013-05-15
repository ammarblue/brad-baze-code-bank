
// Menu Show Games 

void showFullGame1( int textXPos, int textYPos ) {
  // starting point in Game 1
  //  = start configuration 
  int add1 = 20;
  textSize(17);
  textAlign(CENTER);  
  text ("  OOO  ", textXPos, textYPos+add1);
  text ("  OOO  ", textXPos, textYPos+2*add1);      
  text ("OOOOOOO", textXPos, textYPos+3*add1);
  text ("OOO "+middlePoint+" OOO", textXPos, textYPos+4*add1);
  text ("OOOOOOO", textXPos, textYPos+5*add1);            
  text ("  OOO  ", textXPos, textYPos+6*add1);
  text ("  OOO  ", textXPos, textYPos+7*add1);
}

void showEmptyGame1( int textXPos, int textYPos ) {
  // end point to reach in Game 1 and in Game 2
  //  = target configuration 
  int add1 = 20;
  String m = middlePoint+" ";
  textSize(17);  
  textAlign(CENTER);  
  text ("  "+m+m+m+"  ", textXPos, textYPos+add1);
  text ("  "+m+m+m+"  ", textXPos, textYPos+2*add1);
  //
  text (m+m+m+m+m+m+m, textXPos, textYPos+3*add1);
  text (m+m+trim(m)+"O"+ m+m+m, textXPos, textYPos+4*add1);
  text (m+m+m+m+m+m+m, textXPos, textYPos+5*add1);
  //
  text ("  "+m+m+m+"  ", textXPos, textYPos+6*add1); 
  text ("  "+m+m+m+"  ", textXPos, textYPos+7*add1);
}
//

void showFullGame2( int textXPos, int textYPos ) {
  // starting point in game 2
  //  = start configuration 2
  // Cross 
  int add1 = 20;
  String m = middlePoint+" ";
  textSize(17);
  textAlign(CENTER);  
  text ("  "+m+m+m+"  ", textXPos, textYPos+add1);
  text (" "+(m)+ "O" + m+" ", textXPos, textYPos+2*add1);      
  text (m+m+"OOO"+m+m, textXPos, textYPos+3*add1);
  text (m+(m)+m+"O"+(m)+m+m, textXPos, textYPos+4*add1);
  text (m+m+m+"O"+m+m+m, textXPos, textYPos+5*add1);            
  text ("  "+m+m+m+"  ", textXPos, textYPos+6*add1);
  text ("  "+m+m+m+"  ", textXPos, textYPos+7*add1);
}
//
// Fire place
void showFullGame3( int textXPos, int textYPos ) {
  // starting point in game 2
  //  = start configuration 
  int add1 = 20;
  String m = middlePoint+" ";
  textSize(17);  
  textAlign(CENTER);  
  text ("  OOO  ", textXPos, textYPos+add1);
  text ("  OOO  ", textXPos, textYPos+2*add1);      
  text (m+m+"OOO"+m+m, textXPos, textYPos+3*add1);
  text (m+(m)+"O "+(m)+"O"+m+m, textXPos, textYPos+4*add1);
  text (m+m+m+m+m+m+m, textXPos, textYPos+5*add1);            
  text ("  "+m+m+m+"  ", textXPos, textYPos+6*add1);
  text ("  "+m+m+m+"  ", textXPos, textYPos+7*add1);
}
//

