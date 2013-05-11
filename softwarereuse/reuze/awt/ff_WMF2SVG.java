package reuze.awt;
//Copyright 1997, 1998,1999 Carmen Delessio (carmen@blackdirt.com)
//Black Dirt Software http://www.blackdirt.com/graphics
//Free for non-commercial use

//revisions:
//May 12,1999 corrected bad URL http://www.w3.org 
//Corrected using <desc> as placeholders by embedding in their own <g>


import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.Math;
import java.lang.String;
import java.util.Enumeration;
import java.util.Vector;

import com.software.reuze.ff_WMFHandleTable;
import com.software.reuze.ff_WMFMetaRecord;

public class ff_WMF2SVG {


   private byte[] bytebuffer;
   private int i;
   private int j;
   private int bytes_read;

   private int rdSize;
   private int windowLong;
   private short windowInt;

   public short inch;

   private static ff_WMFMetaRecord mRecord;
   private Vector MetaRecordVector;
   private Enumeration MetaRecordInfo;
   private DataInputStream d;

   private int recordIndex;
   private int handleIndex;
   public ff_WMFHandleTable ht;
   private int[] ncount;
   
   byte[] byteData;                // Unpacked data: one byte per pixel
   byte[] rawData;                 // the raw unpacked data
   int[] intData;                  // Unpacked data: one int per pixel

   public ff_WMFToolkit cvtTool = new ff_WMFToolkit();
   private boolean drawFilled;
   private short oldx;
   private short oldy;

   private String svgGraphic = new String("");
   private String svgInit = new String("");


   private short numRectangles;
   private short numPolygons;
   private short numOvals;
   private short numLines;
   
   public short WMFWidth;
   public short WMFHeight;

   private short htmlWidth = 800;
   private short htmlHeight = 600;


   public short logExtX;
   public short logExtY;

   public short logOrgX = 0;
   public short logOrgY = 0;


   public short devExtX;
   public short devExtY;


   public int AppletWidth;
   public int AppletHeight;

   public  float d_x;
   public  float d_y;
   public  Graphics wmfGraphics;              
   public  Image wmfImageBuffer;                  

   public int fontStyle;
   public int fontWeight;
   public boolean fontItalic = false;

   public      int penRed =0;
   public      int penGreen =0;
   public      int penBlue =0;

   public      int textRed =0;
   public      int textGreen =0;
   public      int textBlue =0;
   String textColor = "000000";
   String penColor = "000000";
   String brushColor = "000000";
   int brushStyle=0;
   int brushHatch=0;
   int penStyle=0;
   int penWidth=0;
   public boolean styleSet = false;


public ff_WMF2SVG(InputStream is, PrintStream os) throws IOException
{


     mRecord = new ff_WMFMetaRecord( windowLong, windowInt, null);
     d = new DataInputStream(is);
     parseit();
     WMFPlay();
     if (styleSet){
       //svgGraphic += "</g>";
     }
     svgGraphic += "</svg>\n";
//     System.out.println(svgGraphic);
     os.println(svgGraphic);
}


// gets all wmf records into a hash table
public synchronized void  parseit()throws IOException {

    byte[] f_long =new byte[4];
    byte[] f_int = new byte[2];
    byte[] parmBuffer;
    short x;
    short y;
    short x2;
    short y2;
    short count = 0;
    ff_WMFMetaRecord locMetaRecord;

//begin metafile header

     windowLong = readLong(d); //key  4 bytes
     if (windowLong == -1698247209){
        windowInt = readInt(d); // unused
        x2 = readInt(d);
        y2 = readInt(d);
        x = readInt(d);
        y = readInt(d);
        WMFWidth = (short) Math.abs(x2-x);
        WMFHeight = (short) Math.abs(y2-y);

        windowInt = readInt(d); // inch
        inch = windowInt;

        x = (short)(inch/cvtTool.getScreenResolution());  //pixels per inch
        if (x < 1){
          x =1;
        }

        WMFWidth = (short)(WMFWidth /x);
        WMFHeight = (short)(WMFHeight/x);

        // based on bounded bo x- nnox consideration of map modes

/*
        svgGraphic = "<?xml version = \"1.0\" standalone = \"yes\"?>\n";
        svgGraphic = svgGraphic + "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG April 1999//EN\"\n \"http://www.w3.org/Graphics/SVG/svg-19990412.dtd\">\n";
        svgGraphic = svgGraphic + "<svg width = \"" + WMFWidth +"px\" height=\"" + WMFHeight+"px\">\n";
*/
        svgGraphic="<?xml version=\"1.0\" standalone=\"no\"?>\n"+
                   "<svg width=\"100%\" height=\"100%\" version=\"1.1\"\n"+
                   "xmlns=\"http://www.w3.org/2000/svg\">\n";


        htmlWidth = WMFWidth;
        htmlHeight = WMFHeight;

        devExtX = htmlWidth;
        devExtY = htmlHeight;

        x = cvtTool.twip2pixel(x);  // add inch stuff here
        y = cvtTool.twip2pixel(y);

        windowLong = readLong(d); // reserved
        windowInt = readInt(d);// checksum

        //metaheader

        for ( i = 0; i < 3; i++){
         windowInt = readInt(d);
        }
        windowLong = readLong(d);
        windowInt = readInt(d);
        windowLong = readLong(d);
        windowInt = readInt(d);
     }
     else {
     // not placeable- no metafile header
     // already read long to check for key

     windowInt = readInt(d);

     windowLong = readLong(d);
     windowInt = readInt(d);  //num objects??
     windowLong = readLong(d);
     windowInt = readInt(d);


     }


     MetaRecordVector = new Vector();
     while(true){
        count++;
        try{
              d.readFully(f_long);
           }
        catch(EOFException e){ System.out.println("**** eof");break;}

        if (bytes_read == -1)break;
        windowLong = flipLong(f_long);

        windowInt = readInt(d);

        if (windowInt == 0){
        break;}

        if (windowLong >= 3){
          windowLong = (windowLong*2) -6;
        }

        parmBuffer = null;
        parmBuffer = new byte[windowLong];
        d.readFully(parmBuffer);
        MetaRecordVector.addElement( new ff_WMFMetaRecord( windowLong, windowInt, parmBuffer));
     }
}


// interprets each record
public  void WMFPlay( ) {
//   AppletWidth = w;
//   AppletHeight = h;
//   devExtX = (short) w;
//   devExtY = (short) h;

   ht = new ff_WMFHandleTable();
   drawFilled = false;
   MetaRecordInfo = MetaRecordVector.elements();
   handleIndex = 0;
   recordIndex = 0;

   while(MetaRecordInfo.hasMoreElements()){
        mRecord = (ff_WMFMetaRecord) MetaRecordInfo.nextElement();
        WMFlistRecord(mRecord, false);

        recordIndex++;
   }
}

// works on a single metarecord
public synchronized  void WMFlistRecord(ff_WMFMetaRecord mRecord , boolean fromSelect){
  short x;
  short y;
  short x2;
  short y2;
  short numChars;
  short wOptions;
  int selColor;
  short w; 
  short h; 
  short lbhatch;
  short lbstyle;
  short numPoints;
  DataInputStream parmStream;
  ByteArrayInputStream  parmIn;
  String polyName;
  String shapeName;
  String tempBuffer;
  String colorBuffer;
  String currentFont = "Dialog";
  byte[] textBuffer;
  float fontHeight = 10;
  short fontHeightShort = 10;
  Polygon drawPoly = new Polygon();
  Polygon poly = new Polygon();

//  Image BMPimage = null;



  String this_one;


//   if (g == null) System.out.println(" graphics is null");

   parmIn = new ByteArrayInputStream(mRecord.getParm());
   parmStream = new DataInputStream(parmIn);

  int selector=mRecord.getFunction();
  switch(selector){
         case 0x2fa: // create pen indirect
              if (!fromSelect){
                ht.addObject(recordIndex,mRecord );
              } else {
                 penStyle = readInt(parmStream); //if 5 outline is off
                 penWidth = readInt(parmStream);
                 y = readInt(parmStream);
                 selColor = readLong(parmStream);
                 //cvtTool.setColors(selColor);
                 colorBuffer = cvtTool.getRGBColor( selColor);
                 penColor = colorBuffer;
                 if (styleSet){
                      //svgGraphic = svgGraphic +"</g> \n";
                 } else{
                    styleSet = true;        
                 }
                 //svgGraphic = svgGraphic + "<g style = \"stroke: #" + colorBuffer + "\" > \n";
              }
              break;
         case 0xf7: //createPalette
         	System.out.println("palette");
         	if (!fromSelect) {  // if not selecting it, just add it to table
                 ht.addObject(recordIndex,mRecord );
         	}
         	break;
         case 0x142: //createPatternBrush
         	System.out.println("patternBrush");
         	if (!fromSelect) {  // if not selecting it, just add it to table
                 ht.addObject(recordIndex,mRecord );
         	}
         	break;
         case 0x2fc: //createBrushIndirect

              if (!fromSelect) {  // if not selecting it, just add it to table
                ht.addObject(recordIndex,mRecord );
                lbstyle = readInt(parmStream);
                selColor = readLong(parmStream);
                //System.out.print("("+recordIndex+":"+Integer.toHexString(selColor)+")");
               }
               else {  // selected - use it
                 lbstyle = readInt(parmStream);
                 selColor = readLong(parmStream); //System.out.print(Integer.toHexString(selColor)+" ");
                 lbhatch = readInt(parmStream);
                 colorBuffer = cvtTool.getRGBColor( selColor);
                 brushColor=colorBuffer;
                 brushStyle=lbstyle;
                 brushHatch=lbhatch;
                 if (styleSet){
                      //svgGraphic = svgGraphic +"</g> \n";
                 } else{
                    styleSet = true;        
                 }
                 if (lbstyle > 0 ){
                    drawFilled = false;
                    //svgGraphic = svgGraphic + "<g style = \"stroke: #" + colorBuffer + "\" > \n";
                 }
                 else{
                    drawFilled = true; //filled
                    //svgGraphic = svgGraphic + "<g style = \"fill: #" + colorBuffer + "\" > \n";
                 }
              }
              break;

/*            case 0x6ff: // create region
              if (!fromSelect){
                ht.addObject(recordIndex ,mRecord );
              }
              break;
*/

         case 0x2fb: //createFontIndirect
              if (!fromSelect){  // if not selecting it, just add it to table
                ht.addObject(recordIndex,mRecord );
               } else{
                fontHeightShort = readInt(parmStream);
                fontHeight = fontHeightShort;
                fontHeightShort = (short)fontHeight;
                if (fontHeightShort < 0) {
                  fontHeightShort *= -1;
                  fontHeightShort = mapY(fontHeightShort);
                }
                else{
                  fontHeight = (fontHeight/inch);
                  fontHeight = (fontHeight*72);
                  fontHeightShort = (short)fontHeight;
                  if (fontHeightShort < 5){
                     fontHeightShort = 9;
                  }
                }
                x2 = readInt(parmStream); // width
                y2 = readInt(parmStream); //esc
                y2 = readInt(parmStream); // orientation
                y2 = readInt(parmStream); //weight
                fontWeight = y2;
                textBuffer = new byte[1];
                try{
                  parmStream.read(textBuffer);
                }
                catch(IOException e){ System.err.println(e);}
                  
                x = (short)textBuffer[0]; // italic
                fontItalic = false;
                if (x < 0){
                   fontItalic = true;
                }


                textBuffer = new byte[7];
                try{
                  parmStream.read(textBuffer);
                }
                catch(IOException e){ System.err.println(e);}
                tempBuffer = new String(textBuffer,0); 


                textBuffer = new byte[32];  // name of font
                try{
                  parmStream.read(textBuffer);
                }
                catch(IOException e){ System.err.println(e);}
                tempBuffer = new String(textBuffer,0); 

                currentFont = "Dialog";
                if (tempBuffer.startsWith("Courier")){
                  currentFont = "Courier";
                }
                else if (tempBuffer.startsWith("MS Sans Serif")) {
                  currentFont = "Dialog";
                }
                else if (tempBuffer.startsWith("Arial")) {
                  currentFont = "Helvetica";
                }
                else if (tempBuffer.startsWith("Arial Narrow")) {
                  currentFont = "Helvetica";
                }
                else if (tempBuffer.startsWith("Arial Black")) {
                  currentFont = "Helvetica";
                  fontWeight = 700;
                }
                else if (tempBuffer.startsWith("Times New Roman")) {
                  currentFont = "TimesRoman";
                }
                else if (tempBuffer.startsWith("Wingdings")) {
                  currentFont = "ZapfDingbats";
                }
                if (fontItalic) {
                    fontStyle = Font.ITALIC;
                    if (fontWeight >= 700){ // bold + italic
                      fontStyle = 3;
                    }
                }
                else{
                   fontStyle = Font.PLAIN;  // plain
                    if (fontWeight >= 700){ // bold
                      fontStyle = Font.BOLD;
                    }
                } 
               }
               svgGraphic = svgGraphic + "   <g>\n     <desc> Java Font definition:" + currentFont + " " + fontWeight + "</desc> \n   </g>\n";
               break;

         case 0x12d: //select object
              windowInt = readInt(parmStream);
              mRecord = ht.selectObject(windowInt);
              WMFlistRecord(mRecord, true);
              break;

         case 0x1f0:
              windowInt = readInt(parmStream);
              ht.deleteObject(windowInt);
              break;

         case 0x41b: //rectangle
         	 System.out.println("rect");
              numRectangles++;
              shapeName = "rectangle" + numRectangles;
              y2 = readInt(parmStream);
              x2 = readInt(parmStream);
              y = readInt(parmStream);
              x = readInt(parmStream);
              x = mapX(x);
              x2 = mapX(x2);
              y = mapY(y);
              y2 = mapY(y2);
              w = (short) Math.abs(x2-x);
              h = (short) Math.abs(y2-y);
              svgGraphic = svgGraphic + "   <rect fill=\""+brushColor+"\" x = " + "\"" + x + "\"" + " y = " + "\"" + y + "\"" + " width  = " + "\"" + w + "\"" + " height = " + "\"" + h + "\"" + "/>" + "\n";
              break;

         case 0x418: //Oval
         	System.out.println("oval");
              numOvals++;
              shapeName = "Oval" + numOvals;
              y2 = readInt(parmStream);
              x2 = readInt(parmStream);
              y = readInt(parmStream);
              x = readInt(parmStream);

              x = mapX(x);
              x2 = mapX(x2);
              y = mapY(y);
              y2 = mapY(y2);

              int major;
              int minor;
              int angle;
              int cx;
              int cy;

              w = (short) Math.abs(x2-x);
              h = (short) Math.abs(y2-y);
              if (w>h){
                major=w;
                minor=h;
                angle=0;
              }
              else{
                major=h;
                minor=w;
                angle = 90;
              }
              cx = (int)(x+ Math.round(0.5 * w));
              cy = (int)(y + Math.round(0.5*h));
              svgGraphic = svgGraphic + "   <ellipse "+"fill=\"none\" stroke=\""+((penStyle==5)?"black":penColor)+"\""+" cx = " + "\"" + cx + "\"" + " cy = " + "\"" + cy + "\"" + " rx  = " + "\"" + major/2 + "\"" + " ry = " + "\"" + minor/2 + "\"" +/* " angle = " + "\"" + angle + "\"" +*/ "/>" + "\n";
              break;
         case 0x325: //polyline
              numPoints = readInt(parmStream);
//              tempBuffer = "   <polyline verts = \"";
              tempBuffer = "   <path "+"fill=\"none\" stroke=\""+penColor+"\" d=\"M";
//read 1st point as move to segment
                 x = readInt(parmStream);
                 y = readInt(parmStream);
                 x = mapX(x);
                 y = mapY(y);
                 tempBuffer = tempBuffer + " " + x + "," + y ;                  

              for (i = 0; i < numPoints -1; i++){
                 x = readInt(parmStream);
                 y = readInt(parmStream);
                 x = mapX(x);
                 y = mapY(y);
                 tempBuffer = tempBuffer + "L" + x + "," + y ;                  

              }

                svgGraphic = svgGraphic + tempBuffer + "\"/> \n";
              break;


         case 0x324: //polygon
//              poly = new Polygon();
              numPoints = readInt(parmStream);

//              tempBuffer = "   <polyline verts = \"";
             

              oldx = readInt(parmStream);
              oldy = readInt(parmStream);

              oldx = mapX(oldx);
              oldy = mapY(oldy);
              tempBuffer = "   <path "+ " fill=\""+brushColor+"\" d  = \"M";
              tempBuffer = tempBuffer + " " + oldx + "," + oldy ;                    

              for (i = 0; i < numPoints-1; i++){
                 x = readInt(parmStream);
                 y = readInt(parmStream);
                 x = mapX(x);
                 y = mapY(y);
                 tempBuffer = tempBuffer + "L" + x + "," + y ; 
              }
              tempBuffer = tempBuffer + "L" + oldx + "," + oldy ;    
              svgGraphic = svgGraphic + tempBuffer + "\"/> \n";
             break;

         case 0x538: //polypolygon
         	System.out.println("polypoly");
              int numPolys = readInt(parmStream);

              ncount = new int[numPolys];
              for (j = 0; j < numPolys ; j++){
               ncount[j] = readInt(parmStream);
              }
             
              for (j = 0; j < numPolys ; j++){
                numPoints = (short) ncount[j];
                tempBuffer = " <path fill=\""+brushColor+"\" d= \"M";
                oldx = readInt(parmStream);
                oldy = readInt(parmStream);
                oldx = mapX(oldx);
                oldy = mapY(oldy);
                tempBuffer = tempBuffer + " " + oldx + "," + oldy ;    
                for (i = 0; i < numPoints-1; i++){
                   x = readInt(parmStream);
                   y = readInt(parmStream);
                   x = mapX(x);
                   y = mapY(y);
                   tempBuffer = tempBuffer + "L" + x + "," + y ;                    
                }
                tempBuffer = tempBuffer + "L" + oldx + "," + oldy ;
                svgGraphic = svgGraphic + tempBuffer + "\"/> \n";
              }
              break;

/*            case 0x214: //moveto
              oldy = readInt(parmStream);
              oldx = readInt(parmStream);
              oldx = mapX(oldx);
              oldy = mapY(oldy);
              svgGraphic = svgGraphic +  "   <path  d = \" M " + oldx + " " + oldy + " \"/> \n";
              break;

         case 0x213: //lineto
              numLines++;
              shapeName = "line" + numLines;
              y = readInt(parmStream);
              x = readInt(parmStream);
              x = mapX(x);
              y = mapY(y);
              svgGraphic = svgGraphic +  "   <path  d = \" L " + x + " " + y + " \"/> \n";
              break;
*/
         case 0x209://set text color
                    // save text color
                    // when writing text, switch to text colors
                    // when done writing, switch back

                 selColor = readLong(parmStream);
                 cvtTool.setColors(selColor);
                 textColor = cvtTool.getRGBColor( selColor);
                 break;

         //case 0x201://set BK color
                 //break;

         case 0xa32: // exttext...
                 if (styleSet){
                      svgGraphic = svgGraphic +"</g> \n";
                 } else{
                    styleSet = true;        
                 }


                 svgGraphic = svgGraphic + "<g style = \"stroke: #" + textColor + "\" > \n";
                 y = readInt(parmStream);
                 x = readInt(parmStream);

                 x = mapX(x);
                 y = mapY(y);
                 numChars = readInt(parmStream);
                 wOptions = readInt(parmStream);
                 textBuffer = new byte[numChars];
                 try{
                  parmStream.read(textBuffer);
                 }
                 catch(IOException e){ System.err.println(e);}
                 tempBuffer = new String(textBuffer,0); 
                 svgGraphic = svgGraphic + "   <text x = " + "\"" + x + "\"" + " y = " + "\"" + y + "\" >" + tempBuffer + "</text>\n";

                 svgGraphic = svgGraphic +"</g> \n";

                 svgGraphic = svgGraphic + "<g style = \"stroke: #" + penColor + "\" > \n";
                 break; 

         case 0x521: // TEXTOUT
                 if (styleSet){
                      svgGraphic = svgGraphic +"</g> \n";
                 } else{
                    styleSet = true;        
                 }

                 svgGraphic = svgGraphic + "<g style = \"stroke: #" + textColor + "\" > \n";
                 numChars = readInt(parmStream);
                 textBuffer = new byte[numChars+1];
                 try{
                  parmStream.read(textBuffer);
                 }
                 catch(IOException e){ System.err.println(e);}

                 tempBuffer = new String(textBuffer,0);

                 y = readInt(parmStream);
                 x = readInt(parmStream);

                 x = mapX(x);
                 y = mapY(y);
                 svgGraphic = svgGraphic + "   <text x = " + "\"" + x + "\"" + " y = " + "\"" + y + "\" >" + tempBuffer + "</text>\n";
                 svgGraphic = svgGraphic +"</g> \n";
                 svgGraphic = svgGraphic + "<g style = \"stroke: #" + penColor + "\" > \n";
                 break;

/*            case 0xF43: //stretch DIB
                 svgGraphic = svgGraphic + "  <desc> DIB - Device independent Bitmap - will convert to JPEG in next release </desc> \n";
                 break;
*/
         case 0x20B: //set_window_org
                 logOrgY = readInt(parmStream);
                 logOrgX = readInt(parmStream);
                 System.out.println("setWindowOrg "+"x="+logOrgX+" y="+logOrgY);
                 break;

         case 0x20C: //set_window_ext
                 logExtY = readInt(parmStream);
                 logExtX = readInt(parmStream);
                 System.out.println("setWindowExt "+"x="+logExtX+" y="+logExtY);
                 break;
                 
         case 0x104: //setrop2
             x = readInt(parmStream);
             if (x!=13) System.out.println("setrop2 "+Integer.toHexString(x));
             break;
             
         case 0x106: //setpolyfillmode
             x = readInt(parmStream);
             if (x!=1) System.out.println("setpolyfill "+Integer.toHexString(x));
             break;

         default:
         	System.out.println("not implemented "+Integer.toHexString(selector));
       } // end switch

       try {
			  parmStream.close();  parmIn.close();
                 }
       catch(IOException e){ System.err.println(e);}
}

public short mapX(short x){
    d_x = (float)devExtX/logExtX;
    x = (short)(x - logOrgX);
    x = (short) (x*d_x);
    return (x);
}
public short mapY(short y){
    d_y = (float)devExtY/logExtY;
    y = (short)(y - logOrgY);
    y = (short) (y*d_y);
    return (y);
}


public int flipLong( byte[] byteFlip){
      DataInputStream dl;
      ByteArrayInputStream  b_in;
      byte[] bytebuffer;
      bytebuffer = new byte[4];
      bytebuffer[0] = byteFlip[3];
      bytebuffer[1] = byteFlip[2];  
      bytebuffer[2] = byteFlip[1];  
      bytebuffer[3] = byteFlip[0];  

      b_in = new ByteArrayInputStream(bytebuffer);
      dl = new DataInputStream(b_in);
      try{
        return dl.readInt();
      }
       catch(IOException e){System.err.println(e);}
         return 0;

}


public int readLong( DataInputStream d){
    byte[] longBuf =new byte[4];

     try{
         d.read(longBuf);
//         d.readFully(longBuf);
         return flipLong(longBuf);
      }  catch(IOException e){
         System.err.println(e);
         return 99;
      }
}

public short readInt( DataInputStream d){
     byte[] intBuf =new byte[2];

     try{
         d.read(intBuf);
//         d.readFully(intBuf);
         return flipInt(intBuf);
      }  catch(IOException e){
         System.err.println(e);
         return 99;
      }
}

public short flipInt( byte[] byteFlip){
      DataInputStream d;
      ByteArrayOutputStream b_out;
      ByteArrayInputStream  b_in;
      byte[] bytebuffer;

      bytebuffer = new byte[2];
      bytebuffer[0] = byteFlip[1];
      bytebuffer[1] = byteFlip[0];  

      b_in = new ByteArrayInputStream(bytebuffer);
      d = new DataInputStream(b_in);
      try{
        return d.readShort();
      }
      catch(IOException e){ System.err.println(e);}
         return 0;
}

}
