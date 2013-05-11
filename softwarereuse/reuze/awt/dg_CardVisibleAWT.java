package reuze.awt;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;

import com.software.reuze.dg_Card;
import com.software.reuze.dg_Card.Number;
import com.software.reuze.dg_Card.Suit;

/** Representation and display routines for cards <BR>
* Released under the GNU public license. Copyright (C) 1999-2000 
* @author  Andrew Pipkin <A HREF="mailto:apipkin@pagesz.net" apipkin@pagesz.net</A>
* @version 1.06 (2000/02/08) 
*/
public class dg_CardVisibleAWT extends dg_Card {  
    private static final int N_SIZES = 10;
 // Heights of card for various sizes
    private static final int[] heights = { 37, 37, 65, 100, 125, 152, 200, 250, 300, 400 };
/** Return # of vertical pixels in card */ 
	public static int height(int i) { i=i<0?0:i; i=i>GIANT2?GIANT2:i; return heights[i]; }
    
 // Width of card for various sizes 
    private static final int[] widths = { 21, 21, 40, 60, 75, 90, 120, 150, 180, 240 };
/** Return # of horizontal pixels in card */ 
	public static int width(int i) { i=i<0?0:i; i=i>GIANT2?GIANT2:i; return widths[i]; }

    private static final int[] topHeights = { 12, 12, 14, 18, 24, 30, 36, 48, 60, 72 };
/** Return number of horizontal pixels visible in partially covered cards in
 *  vertical stacks */ 
	public static int topHeight(int i) { i=i<0?0:i; i=i>GIANT2?GIANT2:i; return topHeights[i]; }
	
    private static final int[] sideWidths = { 9, 9, 14, 20, 25, 30, 40, 50, 60, 80 };
/** Return number of vertical pixels visible in partially covered cards in 
 *  horizontal stacks */ 
	public static int sideWidth(int i) { i=i<0?0:i; i=i>GIANT2?GIANT2:i; return sideWidths[i]; }
	
    private static final int[] downTopHeights = {4, 4, 7, 10, 13, 16, 20, 26, 32, 40 };
/** Return number of of vertical pixels visible for partially covered face 
 *  down cards in vertical stacks */
    public static int downTopHeight(int i) { i=i<0?0:i; i=i>GIANT2?GIANT2:i; return downTopHeights[i]; }

    private static int[] cornerCircs = { 6, 6, 10, 16, 20, 24, 32, 40, 48, 64};
/** Return circumference of round corners of card */
    public static int circ(int i) { i=i<0?0:i; i=i>GIANT2?GIANT2:i; return cornerCircs[i]; }

    private static int radius(int i) { return circ(i) / 2; }

    private static final int[] symbolHeights = { 5, 7, 11, 15, 19, 25, 31, 38, 50, 62 };
    private static final int[] symbolWidths  = { 5, 7, 11, 15, 19, 25, 31, 38, 50, 62 };
 // Y-coordinates of symbol columns in card 
    private static final int [][] colYs = new int[N_SIZES][3];
    
	private static final Image symbolImgs[][] = new Image[N_SIZES][N_SUITS * 2];
	private static final Image selSymbolImgs[][] = new Image[N_SIZES][N_SUITS * 2];;
    
 // Card Sizes - MINI represents 7x7 symbol which is used in top
 // of tiny cards 
    public static final int MICRO = 0, MINI = 1, SMALL = 2, MEDIUM = 3;
    public static final int LARGE = 4, HUGE = 5, GIANT = 6;
    public static final int LARGE2 = 7, HUGE2 = 8, GIANT2 = 9;
    
/** Color used for symbols on black cards */ 
    public static final Color blackColor = Color.black;
/** Color used for symbols on red cards */ 
    public static final Color redColor =  new Color(192, 0, 0);
/** Make red text at top of card darker on tiny cards to make it easier to 
 * read */ 
	public static final Color darkRedColor = new Color(128, 0, 0);
/** Color of front of card */ 
    public static final Color frontColor = Color.white;
    public static final Color backColor = null; 
/** Background color of selected card */ 
    public static final Color selectColor = Color.gray;
/** Color of card back */
    private static final Color backCol = new Color(0, 128, 192); 

 // first two colors will be found on edges of suit symbol 
    private static final Color antiAliasedColors[][] = {
        { new Color(192, 192, 192), new Color(128, 128, 128), blackColor } ,
        { new Color(216, 192, 192), new Color(192, 128, 128), redColor }
    };
 // Remove anti-aliasing in selected cards
    private static final Color noAAColors[][] = {
		{ new Color(100, 100, 100), new Color(80, 80, 80), blackColor },
		{ new Color(124, 128, 128), new Color(120, 128, 128), darkRedColor }, 
	};
    
    private static final Font[] fonts = new Font[N_SIZES];
    private static final FontMetrics[] FMs = new FontMetrics[N_SIZES];
    private static final Font[] bigFonts = new Font[N_SIZES];
    private static final FontMetrics[] bigFMs = new FontMetrics[N_SIZES];

    private static final int[] cardRows =
    {   0x0001000, 0x0100010, 0x0101010, 0x0200020, 0x0201020,
        0x0202020, 0x0202120, 0x0212120, 0x2021202, 0x2120212, 0, 0, 0
    };

    private static final long [][][] symbolBitmaps = {
{{0x30L, 0xb8L, 0x2feL, 0x3ffL, 0x74L, },
{0x1fdL, 0x1fdL, 0x30L, 0x3bbL, 0x2feL, },
{0x189L, 0x3dfL, 0x2eeL, 0xb8L, 0x30L, },
{0x30L, 0xb8L, 0x3ffL, 0xb8L, 0x30L, },
},
{{0xc0L, 0x3f0L, 0xbf8L, 0x2ffeL, 0x3fffL, 0x1bf9L, 0xc0L, },
{0x7f4L, 0xbf8L, 0x1d0L, 0x2c8eL, 0x3fffL, 0x2d9eL, },
{0x1e2dL, 0x3fbfL, 0x3fffL, 0x2ffeL, 0xffcL, 0x3f0L, 0xc0L, },
{0xc0L, 0x3f0L, 0xbf8L, 0x1ffdL, 0xbf8L, 0x3f0L, 0xc0L, },
},
{{0x800L, 0x2e00L, 0x7f40L, 0x1ffd0L, 0x3fff0L, 0xbfff8L, 0x1ffffdL, 0x2ffffeL, 0xbddf8L, 0x24c60L, 0x1d00L, },
{0x6e40L, 0xbf80L, 0xffc0L, 0xbf80L, 0x2e00L, 0xb8cb8L, 0x2feefeL, 0x3fffffL, 0x2feefeL, 0xb8cb8L, 0x1d00L, },
{0xfd1fcL, 0x2ff7feL, 0x3fffffL, 0x3fffffL, 0x2ffffeL, 0x1ffffdL, 0x7fff4L, 0x2ffe0L, 0xffc0L, 0x3f00L, 0xc00L, },
{0xc00L, 0x3f00L, 0xbf80L, 0x1ffd0L, 0x7fff4L, 0xffffcL, 0x7fff4L, 0x1ffd0L, 0xbf80L, 0x3f00L, 0xc00L, },
},
{{0xc000L, 0x2e000L, 0x7f400L, 0xffc00L, 0x3fff00L, 0xffffc0L, 0x3fffff0L, 0xbfffff8L, 0xffffffcL, 0xffeeffcL, 0xbfddff8L, 0x3fccff0L, 0xe0c2c0L, 0x1d000L, 0xbf800L, },
{0x19000L, 0xbf800L, 0x3fff00L, 0x7fff40L, 0x3fff00L, 0x2bfa00L, 0x1d000L, 0x2f8cbe0L, 0x1ffffffdL, 0x2ffffffeL, 0x3ffeefffL, 0x1ff9dbfdL, 0x6e0c2e4L, 0xc000L, 0x2e000L, },
{0x1d001d0L, 0x1bf91bf9L, 0x2fff7ffeL, 0x3fffbfffL, 0x3fffffffL, 0x2ffffffeL, 0x1ffffffdL, 0xffffffcL, 0x3fffff0L, 0xbfff80L, 0x3fff00L, 0xbf800L, 0x3f000L, 0x8000L, },
{0xc000L, 0x3f000L, 0xbf800L, 0xffc00L, 0x3fff00L, 0xbfff80L, 0x3fffff0L, 0xbfffff8L, 0x3fffff0L, 0xbfff80L, 0x3fff00L, 0xffc00L, 0xbf800L, 0x3f000L, 0xc000L, },
},
{{0xc0000L, 0x2e0000L, 0x3f0000L, 0xffc000L, 0x2ffe000L, 0x7fff400L, 0xffffc00L, 0x2ffffe00L, 0xffffffc0L, 0x3fffffff0L, 0xbfffffff8L, 0xffffffffcL, 0xffffffffcL, 0xbffddfff8L, 0x1ffccffd0L, 0x6e0c2e40L, 0xc0000L, 0x1d0000L, 0xbf8000L, },
{0x6e4000L, 0x1ffd000L, 0xbfff800L, 0x1ffffd00L, 0x1ffffd00L, 0x1bfff900L, 0x7fff400L, 0x1ffd000L, 0x2e0000L, 0x2fe1d2fe0L, 0x7ff8cbff4L, 0x2ffffffffeL, 0x3fffffffffL, 0x1ffffffffdL, 0x2fffccfffeL, 0x7ff8cbff4L, 0x1b90c1b90L, 0x1d0000L, 0x3f0000L, },
{0x7d001f40L, 0x2ffc0ffe0L, 0x7ffe2fff4L, 0xbfff7fff8L, 0xffff7fffcL, 0xffffbfffcL, 0xffffffffcL, 0xbfffffff8L, 0x3fffffff0L, 0x1ffffffd0L, 0xbfffff80L, 0x3fffff00L, 0x1ffffd00L, 0xbfff800L, 0x3fff000L, 0xffc000L, 0x3f0000L, 0xc0000L, },
{0xc0000L, 0x3f0000L, 0xffc000L, 0x3fff000L, 0x7fff400L, 0xffffc00L, 0x3fffff00L, 0xbfffff80L, 0xffffffc0L, 0x3fffffff0L, 0xffffffc0L, 0xbfffff80L, 0x3fffff00L, 0xffffc00L, 0x7fff400L, 0x3fff000L, 0xffc000L, 0x3f0000L, 0xc0000L, },
},
{{0x3000000L, 0x7400000L, 0x1fd00000L, 0x3ff00000L, 0x7ff40000L, 0x1fffd0000L, 0x3ffff0000L, 0xbffff8000L, 0x3ffffff000L, 0xbffffff800L, 0x2fffffffe00L, 0x7ffffffff40L, 0xfffffffffc0L, 0x2fffffffffe0L, 0x3ffffffffff0L, 0x7ffffffffff4L, 0xbffffffffff8L, 0x7ffffffffff4L, 0x7ffff77ffff4L, 0x2fffe32fffe0L, 0xfffc30fffc0L, 0x2fe0302fe00L, 0x1007401000L, 0xb800000L, 0x1fd00000L, },
{0x2000000L, 0x2fe00000L, 0xfffc0000L, 0x3ffff0000L, 0x7ffff4000L, 0xbffff8000L, 0x1fffffd000L, 0xbffff8000L, 0xbffff8000L, 0x2fffe0000L, 0xfffc0000L, 0x6e41b906e40L, 0x2fff4747ffe0L, 0xbfffd31ffff8L, 0x2fffff33ffffeL, 0x1fffffffffffdL, 0x2fffffffffffeL, 0x1fffffffffffdL, 0x2fffff23ffffeL, 0xbfffd31ffff8L, 0x2fff4307ffe0L, 0x6e407406e40L, 0xb800000L, 0x1fd00000L, },
{0x1f000003d00L, 0xffe0002ffc0L, 0x2fffc00fffe0L, 0xfffff03ffffcL, 0x1fffff8bffffdL, 0x2fffffcfffffeL, 0x3fffffeffffffL, 0x3ffffffffffffL, 0x3ffffffffffffL, 0x2fffffffffffeL, 0x1fffffffffffdL, 0xbffffffffff8L, 0x2fffffffffe0L, 0xfffffffffc0L, 0x3ffffffff00L, 0xbffffff800L, 0x3ffffff000L, 0xfffffc000L, 0x2fffe0000L, 0xfffc0000L, 0x3ff00000L, 0xfc00000L, 0x3000000L, },
{0x3000000L, 0xfc00000L, 0x3ff00000L, 0xbff80000L, 0xfffc0000L, 0x3ffff0000L, 0xbffff8000L, 0xfffffc000L, 0x3ffffff000L, 0xbffffff800L, 0xfffffffc00L, 0x3ffffffff00L, 0xfffffffffc0L, 0x3ffffffff00L, 0xfffffffc00L, 0xbffffff800L, 0x3ffffff000L, 0xfffffc000L, 0xbffff8000L, 0x3ffff0000L, 0xfffc0000L, 0xbff80000L, 0x3ff00000L, 0xfc00000L, 0x3000000L, },
},
{{0xc0000000L, 0xc0000000L, 0x2e0000000L, 0x7f4000000L, 0xbf8000000L, 0x3fff000000L, 0x7fff400000L, 0x2ffffe00000L, 0x7fffff40000L, 0x2ffffffe0000L, 0x7fffffff4000L, 0x2ffffffffe000L, 0xffffffffffc00L, 0x2ffffffffffe00L, 0x7fffffffffff40L, 0x1ffffffffffffd0L, 0x2ffffffffffffe0L, 0x7fffffffffffff4L, 0x7fffffffffffff4L, 0xbfffffffffffff8L, 0xbfffffbfbfffff8L, 0xbfffff2e3fffff8L, 0xbffffe1d2fffff8L, 0x6ffffc0c0ffffe4L, 0x2ffff00c03fffe0L, 0x7fe400c006ff40L, 0x2f4001d0007e00L, 0x7f4000000L, 0x2ffe000000L, 0xbfff800000L, },
{0x6e4000000L, 0x6ffe400000L, 0x2ffffe00000L, 0x7fffff40000L, 0x1ffffffd0000L, 0x2ffffffe0000L, 0x3fffffff0000L, 0x2ffffffe0000L, 0x1ffffffd0000L, 0x7fffff40000L, 0x2ffffe00000L, 0x7fff400000L, 0x6fd1bf91fe400L, 0xbfff82e0bfff80L, 0x3ffffd1d1fffff0L, 0x7fffff4c7fffff4L, 0x1ffffff8cbfffffdL, 0x2ffffffffffffffeL, 0x3fffffffffffffffL, 0x2ffffffffffffffeL, 0x1ffffffeeffffffdL, 0x7fffff4c7fffff4L, 0x3ffffd0c1fffff0L, 0xbfff80c0bfff80L, 0x1bf400c007f900L, 0xc0000000L, 0x3f0000000L, 0xbf8000000L, 0x2ffe000000L, },
{0x1400000005000L, 0x1bf4000007f900L, 0x6ffe8000affe40L, 0x1bfff9001bfff90L, 0x6fffff407ffffe4L, 0x1bfffff80bfffff9L, 0x2ffffffd1ffffffeL, 0x3ffffffe2fffffffL, 0x3ffffffe6fffffffL, 0x3fffffffbfffffffL, 0x2fffffffbffffffeL, 0x1ffffffffffffffdL, 0x1bfffffffffffff9L, 0xbfffffffffffff8L, 0x2ffffffffffffe0L, 0xbfffffffffff80L, 0x2ffffffffffe00L, 0xbfffffffff800L, 0x3fffffffff000L, 0xffffffffc000L, 0x3fffffff0000L, 0xffffffc0000L, 0x3fffff00000L, 0xffffc00000L, 0x3fff000000L, 0xffc000000L, 0x3f0000000L, 0xc0000000L, },
{0xc0000000L, 0x3f0000000L, 0xffc000000L, 0x3fff000000L, 0x7fff400000L, 0xbfff800000L, 0x1ffffd00000L, 0x7fffff40000L, 0xffffffc0000L, 0x1ffffffd0000L, 0x2ffffffe0000L, 0xbfffffff8000L, 0x1ffffffffd000L, 0x2ffffffffe000L, 0xbfffffffff800L, 0x1ffffffffffd00L, 0xbfffffffff800L, 0x2ffffffffe000L, 0x1ffffffffd000L, 0xbfffffff8000L, 0x2ffffffe0000L, 0x1ffffffd0000L, 0xffffffc0000L, 0x7fffff40000L, 0x1ffffd00000L, 0xbfff800000L, 0x7fff400000L, 0x3fff000000L, 0xffc000000L, 0x3f0000000L, 0xc0000000L, },
},
};

/** True if images can be scaled (Java 1.1) or higher */
	protected static boolean canScaleImages = true;
	
static 
	{
		for(int i = 0 ; i < symbolImgs.length; ++i)
		{	symbolImgs[i] = new Image[N_SUITS * 2];
			selSymbolImgs[i] = new Image[N_SUITS * 2];
		}
		for(int i = 0; i < N_SIZES; ++i)
		{	colYs[i] = new int[3];
			colYs[i][0] = width(i)/2 - symbolWidths[i];
			colYs[i][1] = width(i)/2;
			colYs[i][2] = width(i)/2 + symbolWidths[i];

			if(i >= 2)
			{   ++colYs[i][0];
				--colYs[i][2];
			}
		}
		try { Class.forName("java.awt.image.ReplicateScaleFilter"); }
		catch(ClassNotFoundException e) { canScaleImages = false; }
		//System.out.println(canScaleImages);
	}
	
/** construct a card with the value */ 
    public dg_CardVisibleAWT(int value)           { super(value); }
    public dg_CardVisibleAWT(dg_Card c) { super(c); }
/** Construct a card with the desired rank and suit */ 
    public dg_CardVisibleAWT(Number rank, Suit suit)  { super(rank, suit); }
    @Override public Object clone() {
    	return new dg_CardVisibleAWT(this);
    }
/** Return the font used to print the text at the top of all cards */   
	public static Font getFont(int i) {	i=i<0?0:i; i=i>GIANT2?GIANT2:i; return fonts[i]; }
	
/** Return the font metrics for the current card font */
	public static FontMetrics getFontMetrics(int i) { i=i<0?0:i; i=i>GIANT2?GIANT2:i; return FMs[i]; }
    
/** Draw a card with a white background color
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card */ 
    public void draw(Graphics g, int x, int y, int size, boolean singleSymbol)
	{ 
    	drawEmpty(g, x, y, frontColor, size & 0xff);
    	print(g, x, y, isRed() ? redColor : Color.black, size,
				  frontColor == selectColor, singleSymbol);
	}
    
/** Draw a card with a variable background color
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card 
 *  @param bkgColor background color of card, if null draw card back */ 
    public void draw(Graphics g, int x, int y, int size, Image backImage, Dimension imageSize)
    {
        drawEmpty(g, x, y, frontColor, size & 0xff);
        int w=999;
        int h=999;
        if (imageSize!=null) {w=imageSize.width; h=imageSize.height;}
        int imageWidth  = Math.min(dg_CardVisibleAWT.width(size) - (dg_CardVisibleAWT.circ(size) / 2),w);
		int imageHeight = Math.min(dg_CardVisibleAWT.height(size) - (dg_CardVisibleAWT.circ(size) / 2),h);
			int imgX = x + (dg_CardVisibleAWT.width(size) - imageWidth)/2;
			int imgY = y + (dg_CardVisibleAWT.height(size) - imageHeight)/2;
			g.drawImage(backImage, imgX, imgY, imgX + imageWidth, imgY +imageHeight,
						0, 0, imageWidth, imageHeight, null);
    }
     
/** Print the symbols and text of a card 
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card 
 *  @param c color of symbols and text 
 *  @param size size of card 
 *  @selected - if true card is selected */ 
    public void print(Graphics g, int x, int y, Color c, int size, 
					  boolean selected, boolean singleSymbol)
    {
		int rank = getPoints(); Suit suit = getSuit();
		size=size<0?0:size; size=size>GIANT2?GIANT2:size; 	
        g.setColor(selected && c == redColor ? darkRedColor : c);

     // If face card, print big letter indicating rank in center of card    
        if ((rank >= 10 && !singleSymbol) || suit==Suit.Jokers)
        {   
        	drawTopChars(g, x, y, size, selected);
        	g.setFont(bigFonts[size]);
            String ch = names[rank].substring(0,1);
            int chX = x + (width(size) - bigFMs[size].stringWidth(ch))/2;
            int asc = bigFMs[size].getAscent();
            int chY = y + asc + (height(size) - asc)/2;
            g.drawString(ch, chX, chY);
            g.setFont(getFont(size));
        }
		
     // Otherwise print n suit symbols where n is card rank
        else
        {   
        	g.setFont(getFont(size));
        	writeText(g, x+(size!=MINI?widths[size]/6:-2), y, size);
			Image [][] imgs = (selected) ? selSymbolImgs : symbolImgs;
            int rowI = 0;
			int rowY = topHeights[size] + symbolHeights[size] / 2 - 1;

            boolean flip = false;
            rank--;
			if(singleSymbol)
				rank = 0;   //ace
			for(int cardRow = cardRows[rank]; cardRow != 0;
                cardRow /= 16, rowY += height(size)/10, ++rowI)
            {   // turn suit symbols upside down in bottom 3 rows
				if(rowI == 4)
				{	flip = true;
					rowY += size + 1;
				}
                if((cardRow & 3) == 1)
				{	int symX = x + colYs[size][1], symY = y + rowY;
					int symSize = size;

					if(rank == 0 || singleSymbol)  //ace
					{	symSize += 2;
						if(suit == Suit.Spades && size != dg_CardVisibleAWT.MICRO) ++symSize;
						symSize = Math.min(symSize, dg_CardVisibleAWT.GIANT2);
						if(imgs[symSize][suit.ordinal() * 2] == null)
							makeSuitImgs(symSize, suit.ordinal());
						if (size>=LARGE)
						symX=x+widths[size]/3;
					}
					drawSymbol(symSize, suit.ordinal(), g, symX, symY, flip, imgs[symSize]);
				}
                else if((cardRow & 3) == 2)
                {   drawSymbol(size, suit.ordinal(), g, x + colYs[size][0],
							   y + rowY, flip, imgs[size]);
                    drawSymbol(size, suit.ordinal(), g, x + colYs[size][2], 
							   y + rowY, flip, imgs[size]);
                }
            }
        }
    }

    public void print(Graphics g, int x, int y, Color c, int size, 
					  boolean singleSymbol)
	{
		print(g, x, y, c, size, false, singleSymbol);
	}
	
	synchronized static void initCardSize(Graphics g, int size)
	{
		//long startInit = System.currentTimeMillis();
		size=size<0?0:size; size=size>GIANT2?GIANT2:size; 
		if(fonts[size] != null)
			return;
		
		//if(size != Card.MINI)
		{
			fonts[size] = new Font ("Helvetica", 
									Font.BOLD, topHeights[size] - 2);
			FMs[size] = g.getFontMetrics(fonts[size]);
			bigFonts[size] = new Font("TimesRoman", Font.BOLD, 
									  widths[size] - 2);
			bigFMs[size] = g.getFontMetrics(bigFonts[size]);
		}

		for(int suit = 0; suit < N_SUITS; ++suit)
			makeSuitImgs(size, suit);

		//System.out.println(System.currentTimeMillis() - startInit);
	}
	
	synchronized static void makeSuitImgs(int size, int suit)
	{
		// System.out.println("size " + size + " suit " + suit);

		if(size > dg_CardVisibleAWT.GIANT)
		{	if(dg_CardVisibleAWT.canScaleImages)
			{	int h = symbolHeights[size - 3] * 2;
				int w = symbolWidths[size - 3] * 3; 
				if(symbolImgs[size - 3][suit * 2] == null)
					makeSuitImgs(size - 3, suit);
				//System.out.println("Scaling " + size);
				symbolImgs[size][suit * 2]        = scaleImage(symbolImgs[size - 3][suit * 2], w, h);
				symbolImgs[size][suit * 2 + 1]    = scaleImage(symbolImgs[size - 3][suit * 2 + 1], w, h);;
				selSymbolImgs[size][suit * 2]     = scaleImage(selSymbolImgs[size - 3][suit * 2], w, h);;
				selSymbolImgs[size][suit * 2 + 1] = scaleImage(selSymbolImgs[size - 3][suit * 2 + 1], w, h);;
			}
			else
			{	makeSuitImgs(dg_CardVisibleAWT.GIANT, suit);
				for(int i = LARGE2; i <= GIANT2; ++i)
				{	symbolImgs[i][suit * 2] = symbolImgs[dg_CardVisibleAWT.GIANT][suit * 2];
					symbolImgs[i][suit * 2 + 1] = symbolImgs[dg_CardVisibleAWT.GIANT][suit * 2 + 1];
					selSymbolImgs[i][suit * 2] = selSymbolImgs[dg_CardVisibleAWT.GIANT][suit * 2];
					selSymbolImgs[i][suit * 2 + 1] = selSymbolImgs[dg_CardVisibleAWT.GIANT][suit * 2 + 1];
				}
			} 
		}  
		else
		{	symbolImgs[size][suit * 2] = makeSymbolImg(size, suit, false,
													   antiAliasedColors);
			symbolImgs[size][suit * 2 + 1] = makeSymbolImg(size, suit, true,
														   antiAliasedColors);
			selSymbolImgs[size][suit * 2] = makeSymbolImg(size, suit, false,
														  noAAColors);
			selSymbolImgs[size][suit * 2 + 1] = makeSymbolImg(size, suit, true,
															  noAAColors);
		}
	}

	public static Image scaleImage(Image img, int w, int h)
	{
		return (canScaleImages 
				? img.getScaledInstance(w, h, java.awt.Image.SCALE_DEFAULT)
				: img); 
	}  
	
/** Clear outline of card to background color */ 
    
/** Draw outline of card
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card */
    public static void drawOutline(Graphics g, int x, int y, int size)
    {   
        g.setColor(Color.black);
        g.drawRoundRect(x, y, dg_CardVisibleAWT.width(size) - 1, dg_CardVisibleAWT.height(size) - 1,
                        circ(size), circ(size));
    }
/** Draw card with no symbols or text 
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card 
 *  @param bkgColor color to fill empty card with (if null use card back color)*/
    public static void drawEmpty(Graphics g, int x, int y, 
								 Color bkgColor, int size)
    {
        g.setColor(bkgColor == null ? backCol : bkgColor);
        g.fillRoundRect(x, y, dg_CardVisibleAWT.width(size) - 1, dg_CardVisibleAWT.height(size) - 1, 
                        circ(size), circ(size));
        //drawEdge(g, x, y, size);
        drawOutline(g, x, y, size);
    }

/** Draw a black line around the top and left side of a card    
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card */ 
    /*private static void drawEdge(Graphics g, int x, int y, int size)
    {
        drawEdge(g, x, y, size, 0);
    }*/

/** Draw a black line around the top and left side of a card    
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card 
 *  @param corners, if 1 don't draw bottom corner, if -1 don't draw top corner
 */
    private static void drawEdge(Graphics g, int x, int y, int size, int corners)
    {
		int wid = width(size), hgt = height(size);
		int rad = radius(size), cir = circ(size);

        g.setColor(Color.black);
        if(corners != -1)
            g.drawArc(x + wid - cir - 1, y, cir, cir, 30, 60); 
        g.drawLine(x + rad, y, x + wid - 1 - rad, y);
        g.drawArc(x, y, cir, cir, 90, 90); 
        g.drawLine(x, y + rad, x, y + hgt - rad - 1);
        if(corners != 1) 
            g.drawArc(x, y + hgt - cir - 1, cir, cir, 180, 60); 
    }

/** Draw the appropriate suit symbol for a card
 *  @param size size of symbol
 *  @param suit suit to print
 *  @param g graphics context
 *  @param x horizontal coordinate of center of symbol
 *  @param y vertical coordinate of center of symbol 
 *  @param flip if set, draw symbol upside down 
 *  @param imgs array of symbol images - odd indices are upside down 
 */
	private static void drawSymbol(int size, int suit, Graphics g, int x,
								   int y, boolean flip, Image[] imgs)
	{
        int leftX = x - symbolWidths[size]/2, upY = y - symbolHeights[size]/2;
        Color col = g.getColor();

        if(col == blackColor || col == redColor) 
			g.drawImage(imgs[2 * suit + (flip ? 1 : 0)],
                        leftX, upY, null);
        else
            printSymbol(size, suit, g, leftX, upY, flip);
	}
    private static final int suitMap[]={1, 3, 2, 0};
    private static Image makeSymbolImg(int size, int suit, boolean flip, 
									   Color [] [] colors)
    {
        int w = symbolWidths[size], h = symbolBitmaps[size][suitMap[suit]].length;
        int pix[] = new int [w * h];
        int line = 0, inc = 1;

        if(flip)
        {   line = h - 1;
            inc  = -1;
        }
     // Set non symbol pixels to transparent    
        for(int y = 0; y < symbolBitmaps[size][suitMap[suit]].length; 
            ++y, line += inc)
        {   long row = symbolBitmaps[size][suitMap[suit]][line];
            for(int x = 0; row != 0 && x < w; ++x, row /= 4)
            {   if((row & 3) != 0)
                {   Color c = colors[suitMap[suit]/2][((int)row & 3) - 1];
					pix[y * w + x] = c.getRGB(); // | (0xff << 24);
                }
            }
        }
		Toolkit tk = Toolkit.getDefaultToolkit();
        return tk.createImage(new MemoryImageSource(w, h, pix, 0, w));
    }
    
 // Write the bitmap corresponding to the suit to graphics context g 
    private static void printSymbol(int size, int suit, Graphics g,
                                    int x, int y, boolean flip)
    {
//		System.out.print("Printing symbol at " + x + " : " + y);
		if(size > dg_CardVisibleAWT.GIANT)
			size = dg_CardVisibleAWT.GIANT;
		
        int rowI = 0, inc = 1, h = symbolBitmaps[size][suitMap[suit]].length;
     // If flip set, go from top to bottom     
        if(flip)
        {   rowI = h - 1;
            inc  = -1;
        }
     // Write symbol line by line   
        for(int rowNum = 0; rowNum < h; ++rowNum, rowI += inc)
        {   long rowBMP = symbolBitmaps[size][suitMap[suit]][rowI];
            int line_start = x;
            while(rowBMP != 0)
            {   while((rowBMP & 2) == 0 && rowBMP != 0)
                {   rowBMP /= 4;
                    ++line_start;
                }
				
                int line_end = line_start;
                while((rowBMP & 2) != 0)
                {   rowBMP /= 4;
                    ++line_end;
                }
                if(line_start != line_end)
                    g.drawLine(line_start, y + rowNum, 
                               line_end, y + rowNum);
                line_start = line_end;
            }
        }
    }
 
/** Draw the top of a card
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card 
 *  @param bkgColor background color, if null draw face down */ 
    public void drawTop(Graphics g, int x, int y, Color bkgColor, 
						int size, boolean facedown)
    {   
    	size=size<0?0:size; size=size>GIANT2?GIANT2:size;
    	g.setColor(bkgColor); // != null ? bkgColor : backCol);
        g.fillRoundRect(x, y, width(size) - 1, topHeight(size) + circ(size)/2,
                        circ(size), circ(size));
        g.setColor(Color.black);
        g.drawRoundRect(x, y, width(size) - 1, topHeight(size) + circ(size)/2,
                circ(size), circ(size));
        //drawEdge(g, x, y, size, 1);
        if(!facedown)
        {   if(isRed())
                g.setColor(redColor);
            drawTopChars(g, x, y, size, bkgColor == selectColor);
        }
    }

/** Draw the left side of a card
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card 
 *  @param bkgColor background color, if null draw face down */ 
    public void drawSide(Graphics g, int x, int y, Color bkgColor, 
						 int size, boolean facedown)
    { 
		boolean selected = (bkgColor == selectColor);
        g.setColor(bkgColor); // != null ? bkgColor : backCol);
        g.fillRoundRect(x, y, width(size)/2 + circ(size)/2, height(size) - 1,
                        circ(size), circ(size));
        drawEdge(g, x, y, size, -1);

        if(!facedown)
        {   if(isRed())
                g.setColor(selected ? darkRedColor : redColor);
            writeText(g, x, y, size);
			//int symSize = (size != Card.MICRO) ? size : Card.MINI;
			int symX = x + symbolWidths[size] / 2 + size + 2;
            drawSymbol(size, getSuit().ordinal(), g, symX, 
                       y + 3 + topHeights[size] + symbolHeights[size] / 2, false,
					   (!selected ? symbolImgs[size] : selSymbolImgs[size]));
        }
    } 
    
/** Print the text and symbol for the top of a card. The color must be set
 *  before calling this function. 
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card 
 *  @param noAntiAliasing - use symbols without anti-aliasing */
    public void drawTopChars(Graphics g, int x, int y, int size, boolean selected)
    {
		int origX = x, z;
		boolean darkRed = false;
		size=size<0?0:size; size=size>GIANT2?GIANT2:size;
		if(selected && g.getColor() == redColor)
		{	g.setColor(darkRedColor);
			darkRed = false;
		}
		if (size>=LARGE) g.setFont(bigFonts[size-4]);
		if (getPoints()==10 && size>1) z=size/2; else z=size;
		x = writeText(g, x+(size!=MINI?widths[z]/6:-2), y, size) + (size + 1) + symbolWidths[size] / 2;
        if (getSuit()==Suit.Jokers)return; 
		int symbolY = textY(y, size) - symbolHeights[size]/2 - (size == 0 ? 1 : 0);
		int symSize = size;
		if(size == dg_CardVisibleAWT.MICRO)
		{	--symbolY;
			symSize = dg_CardVisibleAWT.MINI;
			++x;
		}
		if(x + symbolWidths[symSize]/2 + 1 > origX + dg_CardVisibleAWT.width(size))
			x = origX + dg_CardVisibleAWT.width(size) - symbolWidths[symSize]/2 - 1;

		drawSymbol(symSize, getSuit().ordinal(), g, x, symbolY, false,
				   selected ? selSymbolImgs[symSize] : symbolImgs[symSize]);

		if(darkRed)
			g.setColor(redColor);
    }
	
	public void drawTopChars(Graphics g, int x, int y, int size)
	{
		drawTopChars(g, x, y, size, false);
	}
 
/** Print the text and indicating a card's rank. The color must be set
 *  before calling this function. 
 *  @param g graphics context
 *  @param x horizontal coordinate of left side of card
 *  @param y vertical coordinate of top of card */ 
    private int writeText(Graphics g, int x, int y, int size)
    {   
        int textX = x + (size + 1) * 2;
		boolean tinyRed = false;

		if(size == dg_CardVisibleAWT.MICRO && g.getColor() == redColor)
		{	g.setColor(darkRedColor);
			tinyRed = true;
		}
		int i=getPoints();
        String rank=(i==0)?names[i]:names[i].substring(0,1);
        if(i!=0 && size == dg_CardVisibleAWT.MICRO) {
			textX += (getPoints() < 10 ? -2 : -4);
			g.drawString(rank+suitChars[getSuit().ordinal()], textX, textY(y, size));
        } else g.drawString(rank, textX, textY(y, size));

		if(FMs[size] != null)
			textX += FMs[size].stringWidth(rank);

		if(tinyRed) 
			g.setColor(redColor);

        return textX; 
    }
    private int textY(int y, int size) { return y + topHeights[size] - 2; }

/** Find the largest font whose ascent is less than or equal to pixHeight
 *  @param name font name
 *  @param style font style
 *  @param pixHeight desired pixel height */ 
    public static Font findFont(Graphics g, String name, int style, int pixHeight)
    { 
        int size = pixHeight * 2;
        Font f; 
        for( ; ; )
        {   f = new Font(name, style, size);
            if(g.getFontMetrics(f).getAscent() <= pixHeight)
                break;
            
            size -= (size > 10) ? 2 : 1;
        } 
        return f;
    }
	public static Image getBackgroundImage()
	{
		if(symbolImgs[dg_CardVisibleAWT.GIANT][0] == null)
			makeSuitImgs(dg_CardVisibleAWT.GIANT, 0);
		return symbolImgs[dg_CardVisibleAWT.GIANT][0];
	}
	private static final char[] suitChars   = {'C', 'D', 'H', 'S'};

}
