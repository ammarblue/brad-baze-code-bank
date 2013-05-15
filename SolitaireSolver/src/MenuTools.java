

// Tools for Menu & Help 

void showArrow(int textXPos, int textYPos ) {
  rect( textXPos, textYPos, 30, 10);
  triangle ( textXPos+30, textYPos+5-20, 
  textXPos+60, textYPos+5, 
  textXPos+30, textYPos+5+20 );
}

void waitALittle() {
  int getTime; 
  getTime=millis();
  while (millis ()-500<=getTime) {
    // wait
  }
}

void textLearnMoreOnScreen() {
  String a = textLearnMore();
  a = a.replaceAll("#", "\n");
  a += "                          - Press mouse button to continue.";
  textLeading(25);
  text(a, 9, 10);
}
//
void printLearnMore() {
  String a = textLearnMore();
  String[] list = split(a, '#');
  for (int i=0; i<list.length;i++) {
    println(list[i]);
  }
}
//
String textLearnMore() {
  String a=""; 
  a+="#"+"Solitaire (does not run in browser)";
  a+="#"+"";
  a+="#"+"Solitaire is a game to play alone, hence the name.";
  a+="#"+"Not to be confused with the card game of the same name.";
  a+="#"+"";
  a+="#"+"There are a lot of variations, but one of the popular ones";
  a+="#"+"is the english board with 32 pegs and a hole in the middle (only "
    +"\nthe first solution is shown, too many solutions).";
  a+="#"+"The goal is to make moves and at the end only one peg remains ";
  a+="#"+"(when possible in the middle).";
  a+="#"+"A move is made by taking one peg, jump vertically or horizontally";
  a+="#"+"over an existing peg (which is then removed) on an empty field (hole).";

  a+="#"+"Example: ";
  a+="#"+"OO"+middlePoint+ " is a valid start configuration for a move (read: peg-peg-hole).";    
  a+="#"+middlePoint+middlePoint+"O" + " is the configuration after the legal move to the right (read: hole-hole-peg).";        
  a+="#"+"Or in short: OO"+middlePoint+ " -> "+ middlePoint+middlePoint+"O" ;
  a+="#"+"The left peg jumped over the peg in the middle to the hole on the \nright and the middle peg got removed.";
  a+="#"+"Likewise this is possible to the left ("
    +middlePoint
    +"OO -> O"
    +middlePoint
    +middlePoint+") or up and down, \nbut not diagonal (on these boards).";
  //
  a+="#"+"The target field must be empty. You must jump over a peg. Other \nmoves are not allowed.";
  a+="#"+"";
  a+="#"+"Since the stone that has been jumped over gets removed, the board ";    
  a+="#"+"gets empty during the game until only a few pegs are left.";
  a+="#"+"The goal is to have only one peg left, preferably in the middle.";
  a+="#"+"";
  a+="#"+"The options show the Fireplace board, the Cross and the English Board.";
  a+="#"+"They all have the same target configuration.";
  a+="#"+"In this program you can not play but the program finds a solution \nfor the problem.";
  a+=""+"";
  return (a);
}
//

