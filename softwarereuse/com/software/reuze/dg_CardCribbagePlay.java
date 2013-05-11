package com.software.reuze;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;

/*  Cribbage Applet 
    Copyright (C) 1999   William Rossi

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/* Class to represent the play of a cribbage game. */

public class dg_CardCribbagePlay
{
	private dg_CardCribbageTally.Hand myHand, crib, opponentHand;
	private int myCardsAvail;
	dg_CardPile<dg_Card> deck=new dg_CardPile<dg_Card>(new dg_Card(0));
	private dg_CardPile<dg_Card> deckCards;  // Cards that are not known to be elsewhere
	private int total;
	private dg_Card cardsInPlay[];
	private int numCardsInPlay;

	public dg_CardCribbagePlay()
	{
		myCardsAvail = 4;
		total = 0; 
		cardsInPlay = new dg_Card[9];
		numCardsInPlay = 0;
		myHand = dg_CardCribbageTally.Hand.create();
		crib = dg_CardCribbageTally.Hand.create();
		opponentHand = dg_CardCribbageTally.Hand.create();
		deckCards = new dg_CardPile<dg_Card>(new dg_Card(0), 52);
	}

	private int checkRun(dg_Card c)
	{
		dg_Card runCards[];
		dg_Card tmp;
		int runLength;
		int runLengthDetected = 0;
		boolean swapped, isRun;
		int i;

		if (numCardsInPlay <= 1)
			return 0;

		runCards = new dg_Card[9];

		runCards[0] = c;
		for (i=1; i<=numCardsInPlay; i++)
			runCards[i] = cardsInPlay[numCardsInPlay-i];

		for (runLength=3; runLength<=(numCardsInPlay+1); runLength++)
		{
			/* Bubble sort runLength cards */
			do
			{
				swapped = false;
				for (i=0; i<(runLength-1); i++)
					if (runCards[i].getPoints() > runCards[i+1].getPoints())
					{
						swapped = true;
						tmp = runCards[i+1];
						runCards[i+1] = runCards[i];
						runCards[i] = tmp;
					}
			} while (swapped);

			isRun = true;
			for (i=0; i<(runLength-1); i++)
			{
				if ((runCards[i+1].getPoints() - runCards[i].getPoints()) != 1)
					isRun = false;
			}

			if (isRun)
				runLengthDetected = runLength;
		}

		return runLengthDetected;
	}

	/** Returns the score for a card, and -1 if play is illegal */

	public int score(dg_Card c)
	{
		int score = 0;

		if (numCardsInPlay == 0)  // Never any score for first card.
			return 0;

		/* Check to see if total exceeds 31 */
		if ((dg_CardCribbageTally.getValue(c)+total) > 31)
			return -1;

		/* Check for a fifteen */

		if ((dg_CardCribbageTally.getValue(c)+total) == 15)
			score+=2;

		/* Check for a 31 */

		if ((dg_CardCribbageTally.getValue(c)+total) == 31)
			score+=2;

		/* Check for a pair */
		if (c.getPoints() == cardsInPlay[numCardsInPlay-1].getPoints())
		{
			score+=2;

			/* Check for 3 of a kind */
			if (numCardsInPlay > 1 && 
					c.getPoints() == cardsInPlay[numCardsInPlay-2].getPoints())
			{
				score+=4;
				/* Check for 4 of a kind */
				if (numCardsInPlay > 2 && 
						c.getPoints() == cardsInPlay[numCardsInPlay-3].getPoints())
					score+=6;
			}
		}

		score += checkRun(c);
		return score;
	}

	/* Actually play the card, returns the score, -1 if not possible */
	public int myPlay(dg_Card c) 
	{
		int sc = score(c);
		dg_Card tmp;
		int i;

		if (sc == -1)
			return sc;

		cardsInPlay[numCardsInPlay++] = c;
		total += dg_CardCribbageTally.getValue(c);

		for (i=0; i<myCardsAvail; i++)
			if (myHand.hand[i].equals(c))
			{
				tmp = myHand.hand[myCardsAvail-1];
				myHand.hand[myCardsAvail-1] = myHand.hand[i];
				myHand.hand[i] = tmp;
				myCardsAvail --;
				break;
			}

		return sc;
	}

	/* Actually play the card, returns the score, -1 if not possible */
	public int opponentPlay(dg_Card c) 
	{
		int sc = score(c);
		dg_Card tmp;
		int i,j;

		if (sc == -1)
			return sc;

		cardsInPlay[numCardsInPlay++] = c;
		total += dg_CardCribbageTally.getValue(c);
		return sc;
	}

	public dg_Card autoPlay()
	{
		int scores[];
		int currentCardsInPlay;
		int i, j, k, max;
		int bestplay = 0;

		if (myCardsAvail == 0)
			return null;

		scores = new int[myCardsAvail];

		currentCardsInPlay = numCardsInPlay;
		int avail=deckCards.size();
		for (i=0; i<myCardsAvail; i++)
		{
			/* Get direct score */
			scores[i] = score(myHand.hand[i])*avail;
			if (scores[i] < 0)
				continue;

			/* Pretend for the moment we play this card */
			cardsInPlay[numCardsInPlay++] = myHand.hand[i];
			total += dg_CardCribbageTally.getValue(myHand.hand[i]);

			/* Compute probability of opponent score */
			for (j=0; j<avail; j++)
			{
				int s, t;

				s = score(deckCards.get(j));
				if (s == -1)
					continue;

				scores[i] -= s;
				cardsInPlay[numCardsInPlay++] = deckCards.get(j);
				total += dg_CardCribbageTally.getValue(deckCards.get(j));
				max = 0;
				for (k=0; k<myCardsAvail; k++)
					if (k!=i)
					{
						t = score(myHand.hand[k]);
						if (t == -1)
							continue;
						if (t > max)
							max = t;
					}

				scores[i] += max;
				//        System.out.println(myCards[i]+"  "+deckCards[j]+"  "+s+" "+max+" "+scores[i]);
				numCardsInPlay--;
				total -= dg_CardCribbageTally.getValue(deckCards.get(j));
			}
			numCardsInPlay--;
			total -= dg_CardCribbageTally.getValue(myHand.hand[i]);
		}

		/* Find the best scoring card */
		max = -1000;
		for (i=0; i<myCardsAvail; i++)
		{
			//      System.out.println(myCards[i]+"  "+scores[i]);
			if (scores[i] > max)
			{
				max = scores[i];
				bestplay = i;
			}
		}

		if (score(myHand.hand[bestplay]) < 0)
			return null;

		return myHand.hand[bestplay];
	}

	/** re-initializes count to zero, and clears cards in play */

	public void newCount()
	{
		total = 0;
		numCardsInPlay = 0;
	}
	/* This method handles the logic of the Play.  It enforces the rules */
	private void doPlay(boolean myDeal)
	{
		dg_Card c;
		int total = 0;
		int myCardsPlayed = 0,yourCardsPlayed=0;
		int s=0; 
		boolean myTurn = true;
		boolean IplayedLast = false;
		boolean go = false;
		String line;
		StringTokenizer st=null;
		int myPeg = 0;
		int yourPeg = 0;

		myTurn = !myDeal;        // Non-dealer plays first

		// Play until all cards are used or end of game
		while ((myCardsPlayed+yourCardsPlayed) < 8)
		{//System.out.println("crib="+crib);
			if (myTurn) // If it is my (the computer's) turn
			{
				myTurn = false;  // Assume it will not be my turn next time

				// Pick a good card to play.  This will return null
				// if I have no cards than can be played.

				c = autoPlay();  

				s = -1;
				if (c != null)
					s = myPlay(c);  // s is the number of points scored

				if (s < 0) /* Can't play any cards */
				{
					if (!go && total != 31)  /* If opponent hasn't said go as well */
					{
						System.out.println("I say Go!");
						go = true;
					}
					else  /* Opponent says go also */
					{  
						if (total != 31) /* Can't score 31 for 2 and the go */
						{
							System.out.println("I score one for the go");
							myPeg++;
							if ((myPeg+myTotal) >= 121)   // Check for end of game
								break;
						}

						/* Set up for a new count.  The player who's opponent played
                           last will start things off */

						total = 0;
						System.out.println("newCount");
						newCount();
						go = false;
						myTurn = !IplayedLast;
					}
				}
				else  // I do have some cards to play
				{
					total += dg_CardCribbageTally.getValue(c); // Increment count total
					myCardsPlayed++;         // Increment number of cards played
					if ((myCardsPlayed+yourCardsPlayed) == 8 && total != 31)  // Point for last card
						s++;
					System.out.println(total+": "+c+"  "+s+" points");
					myPeg+=s;
					if ((myPeg+myTotal) >= 121)  // Check for end of game
						break;

					/* If there is a go in effect, I keep playing */
					if (go)
						myTurn = true;
					IplayedLast = true;
				}
			}
			else /* Opponents turn */
			{
				myTurn = true;
				c=null;
				if ((yourCardsPlayed<4)&&(total < 31))  // Can't play a card if we are at 31
					do {
						line=null;
						try {
							System.out.print("Play > ");
							line = din.readLine();
							st = new StringTokenizer(line);
							if (st.countTokens()>0) {
								c = dg_Card.constant(st.nextToken());
								dg_Card handCards[]=opponentHand.hand;
								for (s=0; s<(4-yourCardsPlayed); s++) if (handCards[s].equals(c)&&((dg_CardCribbageTally.getValue(c)+total)<=31)) break;
								if (s>=(4-yourCardsPlayed)) {line=null; c=null;}
								else {
									int t=s;
									s = opponentPlay(c);
									if (s<0) line=null;
									else {
										dg_Card tmp=opponentHand.hand[t];
										opponentHand.hand[t]=opponentHand.hand[3-yourCardsPlayed];
										opponentHand.hand[3-yourCardsPlayed]=tmp;
									}
								}
							} 
						} catch (Exception e) {line=null;}
					} while (line==null);

				if (total == 31 || c==null) // A go
				{
					if (!go)  /* If computer didn't previously say go */
					{
						System.out.println("You say Go!");
						go = true;
					}
					else      /* If the computer did previously say go */
					{  
						if (total != 31)  /* Can't score a go and a 31 for 2 */
						{
							System.out.println("You score one for the go");
							yourPeg++;
							if ((yourPeg+yourTotal) >= 121)  // Check for end of game
								break;
						}

						/* Set up for a new count.  The player who's opponent played
               			   last will start things off */

						total = 0;
						System.out.println("newCount");
						newCount();
						go = false;
						myTurn = !IplayedLast;
					}
				}
				else  /* You do have a card you want to play */
				{
					total += dg_CardCribbageTally.getValue(c);
					yourCardsPlayed++;
					if ((myCardsPlayed+yourCardsPlayed) == 8 && total != 31)  // Point for last card
						s++;
					System.out.println(total+": "+c+"  "+s+" points");

					/* If playing on a go, it remains your turn */
					if (go)
						myTurn = false;

					yourPeg+=s;
					if ((yourPeg+yourTotal) >= 121)  // Check for end of game
						break;

					IplayedLast = false;
				}
			}
		}
		myTotal+=myPeg;
		yourTotal+=yourPeg;
		System.out.println("I pegged "+myPeg+" points."+" Total="+myTotal);
		System.out.println("You pegged "+yourPeg+" points."+" Total="+yourTotal);
	}
	public dg_Card deal() {
		return deckCards.remove(deckCards.size()-1);
	}
	public void getStartCard()
	{
		dg_Card sc = deal();

		myHand.setCut(sc);
		opponentHand.setCut(sc);
		crib.setCut(sc);
	}
	public void getOpponentHand()
	{
		dg_Card initCards[] = new dg_Card[6];
		dg_Card handCards[] = new dg_Card[4];
		String line = null;
		StringTokenizer st;
		int i, j;
		boolean done;

		for (i=0; i<6; i++)
			initCards[i] = deal();

		Arrays.sort(initCards, initCards[0]);

		do
		{
			done = false;
			System.out.println("Your cards");
			for (i=0; i<6; i++)
				System.out.print(initCards[i]+" ");
			System.out.println();
			System.out.println("Select 2 discards");

			try
			{
				line = din.readLine();
			}
			catch (IOException e)
			{
			}

			st = new StringTokenizer(line);

			if (st.countTokens() != 2)
				continue;

			try
			{
				crib.hand[0] = dg_Card.constant(st.nextToken());
				crib.hand[1] = dg_Card.constant(st.nextToken());
			}
			catch (Exception e)
			{
				continue;
			}
			if (crib.hand[0].equals(crib.hand[1])) continue;
			for (i=0; i<6; i++) if (initCards[i].equals(crib.hand[0])) break;
			if (i>=6) continue;
			for (i=0; i<6; i++) if (initCards[i].equals(crib.hand[1])) break;
			if (i>=6) continue;
			done = true;
			j = 0;
			for (i=0; i<6; i++)
			{
				if (initCards[i].equals(crib.hand[0]) == false &&
						initCards[i].equals(crib.hand[1]) == false)
					handCards[j++] = initCards[i];
			}

		} while (!done);

		opponentHand.set(handCards);
	}
	public void getMyHand() {
		dg_Card initCards[] = new dg_Card[6];
		dg_Card handCards[] = new dg_Card[4];
		dg_Card myDiscards[] = new dg_Card[2];
		for (int i=0; i<6; i++)
			initCards[i] = deal();

		Arrays.sort(initCards, initCards[0]);
		handCards = dg_CardCribbageTally.selectDiscards(initCards, myDiscards);
		crib.hand[2] = myDiscards[0];
		crib.hand[3] = myDiscards[1];
		Arrays.sort(crib.hand,0,3, initCards[0]);
		myHand.set(handCards);
		myCardsAvail=4;
	}
	public void play()
	{
		boolean myDeal=Math.random()>=0.5;
		do
		{
			System.out.println(myDeal?"My Deal":"Your deal");
			newCount();
			int i=0;
			if (deckCards.size()<13) {
				System.out.println("Shuffling");
				deck.shuffle(13);
				deckCards.clear();
				deckCards.addAll(deck);
			}
			getOpponentHand();
			getMyHand();
			getStartCard();
			System.out.println("cut= "+myHand.getCut()+" crib= "+crib+" "+myHand);
			if (myHand.getCut().getNumber() == dg_Card.Number.Jack)  // A jack was cut
			{
				if (myDeal)
				{
					System.out.println("I score two for Nibs");
					myTotal += 2;
				}
				else
				{
					System.out.println("You score two for Nibs");
					yourTotal += 2;
				}
			}

			if (myTotal >= 121 || yourTotal >= 121)
				break;

			System.out.println("Your hand: "+opponentHand);
			doPlay(myDeal);

			System.out.println("Your hand: "+opponentHand+"  "+dg_CardCribbageTally.score(opponentHand.hand, false));
			System.out.println("My hand:   "+myHand+"  "+dg_CardCribbageTally.score(myHand.hand, false));
			System.out.println("Crib:      "+crib+"  "+dg_CardCribbageTally.score(crib.hand, true));

			if (myDeal)
			{
				yourTotal += dg_CardCribbageTally.score(opponentHand.hand, false);
				if (yourTotal >= 121)
					break;
				myTotal += dg_CardCribbageTally.score(myHand.hand, false);
				myTotal += dg_CardCribbageTally.score(crib.hand, true);
			}
			else
			{
				myTotal += dg_CardCribbageTally.score(myHand.hand, false);
				if (myTotal >= 121)
					break;
				yourTotal += dg_CardCribbageTally.score(opponentHand.hand, false);
				yourTotal += dg_CardCribbageTally.score(crib.hand, true);
			}

			System.out.println("My total:   "+myTotal);
			System.out.println("Your total: "+yourTotal);
			myDeal = !myDeal;
		} while (myTotal < 121 && yourTotal < 121);

		if (myTotal >= 121)
			System.out.println("\nI win...  "+myTotal+" to "+yourTotal);
		else
			System.out.println("\nYou win...  "+yourTotal+" to "+myTotal);

	}
	int myTotal=0, yourTotal=0;
	DataInputStream din;
	public static void main(String args[])
	{
		dg_CardCribbagePlay p;
		p = new dg_CardCribbagePlay();
		p.din = new DataInputStream(System.in);
		p.play();
	}

}
