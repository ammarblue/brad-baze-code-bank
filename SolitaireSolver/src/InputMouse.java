
// Inputs Mouse

void mousePressed() {
  switch (state) {
  case stateWelcomeScreen:
    mousePressedstateWelcomeScreen();
    break;
  case statePlay:
    //
    break;
  case stateLearnMore:
    state = stateWelcomeScreen;
    break;
  case stateWaitForWelcomeScreen:
    state = stateWelcomeScreen;
    break;  
  default:
    println("Unknown State (Error 18 in tab InputMouse): " + state+ ".");
    exit();
    break;
  } // switch
} // func 
//
void mousePressedstateWelcomeScreen() {
  // Mouse pressed in state Welcome Screen / Main Screen.
  // Evaluate three main buttons. 
  if (rectFireplaceBoard.overRect()) {
    intEndstellungenCounter=0;
    Solutions="";
    // define Fireplace
    currentConfiguration = configurationFirePlaceBoard;    
    state = statePlay;
  }
  else if (rectCrossBoard.overRect()) {
    println("Cross");
    intEndstellungenCounter=0;    
    Solutions="";
    // define Cross
    currentConfiguration = configurationCrossBoard;    
    state = statePlay;
  }
  else if (rectEnglishBoard.overRect()) {
    println("English");
    intEndstellungenCounter=0;    
    Solutions="";
    // define English Board
    currentConfiguration = configurationEnglishBoard;    
    state = statePlay;
  }  
  else if (rectLearnmore.overRect()) {
    printLearnMore();
    state=stateLearnMore;
  }
  else {
    //
  }
}
//

