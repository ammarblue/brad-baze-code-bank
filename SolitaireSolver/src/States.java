

// states for the program 

void handleStateWelcomeScreen() {
  //
  // headline 
  fill(255);  
  textSize(32);
  textAlign(CENTER);
  text ("Solitaire Solver", width/2, 40);
  //
  // intro text 
  textSize(19);
  textAlign(LEFT);
  text ("Solitaire is a peg game for one player. The goal is ", textXPos, 80);
  text ("to reach a certain configuration with your moves. ", textXPos, 80+30);    
  text ("You can't play here, the program finds a solution. ", textXPos, 80+30+30);    
  text ("Choose the task to solve. ", textXPos, 80+30+30+50);    
  //
  // 1st game option: Fireplace on an English Board 
  fill(255);
  textSize(19);
  //showFullGame1( textXPos+120, 80+30+30+90);    
  showFullGame3( textXPos+120, 80+30+30+90);    
  showArrow(textXPos+200, 80+30+30+90+70);    
  showEmptyGame1( textXPos+320, 80+30+30+90);    
  text ("Solve", textXPos + 460, 80+30+30+90+70);        
  rectFireplaceBoard.drawRect();
  //
  // 2nd game option: Figure Cross on an English Board  
  fill(255);
  textSize(19);
  showFullGame2( textXPos+120, 80+30+30+250);    
  showArrow(textXPos+200, 80+30+30+90+230);    
  showEmptyGame1( textXPos+320, 80+30+30+250);    
  text ("Solve", textXPos + 460, 80+30+30+90+230);   
  rectCrossBoard.drawRect();
  //
  // 3rd game option: Figure English Board on an English Board  
  fill(255);
  textSize(19);
  showFullGame1( textXPos+120, 80+30+30+250+160);    
  showArrow(textXPos+200, 80+30+30+90+230+160);    
  showEmptyGame1( textXPos+320, 80+30+30+250+160);    
  text ("Solve", textXPos + 460, 80+30+30+90+230+160);   
  rectEnglishBoard.drawRect();  
  //
  // learn more box
  rectLearnmore.drawRectText();
  rectLearnmore.drawRect();
  //
}
//
void handleStatePlay() {
  //
  MainSolitaire();
  state = stateWaitForWelcomeScreen;
}
//
void handleStateLearnMore() {
  textLearnMoreOnScreen();
}
//
void handleStateWaitForWelcomeScreen() {
  // shows the solution screen
  background(0);
  // headline 
  fill(255);  
  textSize(32);
  textAlign(CENTER);
  text ("Solitaire Solver", width/2, 40);
  //
  // intro text 
  textSize(19);
  textAlign(LEFT);
  text ("Solitaire configuration '" + 
    currentConfigurationName[currentConfiguration] + 
    "' has been solved. ", textXPos, 80);  
  text ("Possible Games in total:  " + 
    "" +
    intEndstellungenCounter + 
    ", solutions: " + 
    NumberSolutions + ".", textXPos, 80+30);  
  if (!showAllSolutionsWhenDone) 
    text("(Stopped after 1st solution)", textXPos, 80+30+30);  
  // show Solutions
  showSolutions ();
  //
} // func 
// 
void showSolutions () {
  //
  String [] arrGames1 = split (Solutions, '#');

  int currentX=-80;
  int x1;
  int y1;  
  int GameLine=0;
  int border=0;
  //
  fill(255);  
  textSize(11);  

  // show how many Games
  if (showAllSolutionsWhenDone)
    border=arrGames1.length-1;
  else
    border=1;
  // loop over games 
  for (int i=0; i <= border; i++) {

    String [] arrMoves1 = split (arrGames1[i], ',');

    // loop over positions in current game 
    for (int j=1;j<arrMoves1.length;j++) {
      if (arrMoves1[j].length()>1) {
        //new col
        currentX+=100;
        x1=currentX;
        y1=40 + GameLine;        
        arrMoves1[j] = arrMoves1[j].replaceAll(middlePoint, "*");
        // loop over chars (pegs/holes) in the current position
        for (int i_Letter=0; i_Letter < arrMoves1[j].length(); i_Letter++) {      
          if ((i_Letter % intConstEnde) == 0) {
            // new line within one position
            x1=currentX;
            y1+=13;
          } // if

          switch (arrMoves1[j].charAt(i_Letter)) {
          case '*':
            // next pos 
            x1+=10;
            textSize(11);
            text(middlePoint, x1, y1);
            break;
          case ' ':
            // next pos 
            x1+=10;
            break;
          case 'o':
            // next pos 
            x1+=8;
            text("o", x1, y1);
            x1+=2;
            break;
          default:
            println("Error 97 in tab states with " + 
              arrMoves1[j].charAt(i_Letter));
            // exit();
            break;
          } // switch
        } // for
      } // if
      if (currentX>1100) 
      {
        currentX=-80;
        GameLine+=120;        
        y1=40 + GameLine;
      }
    } // for
    currentX=-80;
    x1=20;
    y1=80+30+20+140;
    GameLine+=120;
  } // for
  textSize(19);
  text("Press mouse button.", 500, height-30);
}
//

