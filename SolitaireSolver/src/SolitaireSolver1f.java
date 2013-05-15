/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/65645*@* */
/* !do not delete the line above, required for linking your tweak if you re-upload */
// Main 
// peg Solitaire solver
// http://en.wikipedia.org/wiki/Peg_solitaire
//
// states for the program 
final int stateWelcomeScreen = 0;
final int statePlay = 1;
final int stateLearnMore = 2;
final int stateWaitForWelcomeScreen = 3;
// current state 
int state = stateWelcomeScreen;
//
// configurations for the Board
final int configurationFirePlaceBoard=0;
final int configurationCrossBoard=1;
final int configurationEnglishBoard=2;    
final String [] currentConfigurationName = 
{ 
  "Fire Place Board", 
  "Cross Board", 
  "English Board"
};
//
// current configuration
int currentConfiguration = configurationFirePlaceBoard;
//
// program end
boolean programEnd = false;
//
//
boolean showAllSolutionsWhenDone=true; 
//
// output stuff
final String middlePoint = char(183)+"";
//
// the initial Main Menu
int textXPos;
int[] textYPos = { 
  80+30+30+90, 
  80+30+30+250, 
  80+30+30+250+160, 
  80+30+30+250+160+150
};
//
// rectangles as buttons 
Rectangle rectFireplaceBoard; 
Rectangle rectCrossBoard; 
Rectangle rectEnglishBoard;
Rectangle rectLearnmore; 
//
boolean itIsFirstTime = true;

// Wie viele Kugeln in einer Reihe und Spalte (bei 7 Kugeln
// hier 6 angeben, Zählung startet bei 0); für Reihen und Spalten
// gleich.
// Mögl. Werte: 4,6,8...
// Und dann Sub Init1 anpassen
final int intConstEnde = 7 ; // 0..10, also 11 Felder breit und hoch: hier 10 eintragen

// Titel
final String strConstTitel = "Solitaire ";

// Belegungskonstanten des Feldes
final int constSpielfeldLeer = 2;            // Feld leer
final int constSpielfeldBesetzt = 1;         // Feld besetzt
final int constSpielfeldNichtVorhanden = 0;  // Eckfelder werden nicht bespielt

// Richtungskonstanten für die Zugrichtung;
// Zug-Richtung als 0=Nord 1=Osten 2=Sueden 3=Westen
final int  constNorden = 0;    // Auf einer Ebene
final int  constOsten = 1;
final int  constSueden = 2;
final int  constWesten = 3;

final int  constNordost = 6;      // neu: Diagonal in 2D;
final int  constSuedost = 7;      // neu: Diagonal in 2D;
final int  constSuedwest = 8;     // neu: Diagonal in 2D;
final int  constNordwest = 9;     // neu: Diagonal in 2D;
//
int intMaxRichtung;

// Spielfeld                             x               y          
int [][] SpielfeldGlobal = new int [intConstEnde] [intConstEnde];    // array

// Others
long intEndstellungenCounter;
int NumberSolutions;
String constZielStellung;
String Solutions="";
boolean boolOnlyHorizontalAndVerticalMovesRequired;

// ========================================================================================================

void setup () {
  size (1230, 800);
  textXPos = width/2-300;
  rectFireplaceBoard = new Rectangle(textXPos, textYPos[0], 500, 145, "", "Solve Fireplace Board (medium)");
  rectCrossBoard     = new Rectangle(textXPos, textYPos[1], 500, 145, "", "Solve Cross (easy)");
  rectEnglishBoard   = new Rectangle(textXPos, textYPos[2], 500, 145, "", "Solve English Board (hard)");  
  rectLearnmore      = new Rectangle(textXPos+400, textYPos[3], 100, 25, "Learn more", 
  "Learn more about the Game Solitaire." );  
  // noLoop();
}

void draw () {
  background(0);
  switch (state) {
  case stateWelcomeScreen:
    handleStateWelcomeScreen();
    programEnd = false;
    break;
  case statePlay:
    // to make sure handleStatePlay gets not 
    // called the 1st time, we use itIsFirstTime:
    if (!itIsFirstTime) {
      // not the 1st time 
      // wait a little
      waitALittle();
      handleStatePlay();
      itIsFirstTime = true; // for the next call
    } 
    else {
      // 1st time
      background(0);
      text ("working... (NOT working in Browser)", 210, 300);      
      itIsFirstTime=false; // set to false
    }
    break;
  case stateLearnMore:
    handleStateLearnMore();
    break;
  case stateWaitForWelcomeScreen:
    // shows solution etc. 
    handleStateWaitForWelcomeScreen();
    break;
  default:
    println("Unknown State (Error 33 in main tab): " + state+ ".");
    exit();
    break;
  } // switch
} // func 
// ========================================================================================================

