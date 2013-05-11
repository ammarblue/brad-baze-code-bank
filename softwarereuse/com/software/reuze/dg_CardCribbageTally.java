package com.software.reuze;
import java.util.Arrays;

public class dg_CardCribbageTally {
	/** Computes the score of this hand.  Note that the
    cards and the cut must have been set prior to calling this */
	//Card cards[]=new Card[5];  //cards[4] is a temp spot to hold turned-over card
	//boolean scored=false;      //indicates that handScore is valid
	//int handScore;
	public final static int getValue(dg_Card c) { int i=c.getPoints(); return i<10?i:10; }
/*Fifteen (total of two or more cards = 15 (ex. 3, 5, 7) .............. ..2
 *  Pair (ex. Q,Q) ......................................................................... ..2
 *   Three of a kind (ex 3, 3, Four of a kind (ex. 4,4,4,4) ............................... ..12
 *   Runs of three or more cards (3, 4, 5) ................. ..1 for each card
 *   Double three-card run, including pairs (ex. 5, 5, 6, 7) ............ ..8
 *   Double four-card run, including pairs (A, A, 2, 3, 4) ............... ..10
 *   Triple run, including pairs (ex. A, A, A, 2, 3) ........ ........... ..15
 *   Quadruple run, including pairs (ex 5,5, 6, 6, 7) ..................... ..16
 *   Flush, 4 cards of a suit (ex 2H, 4H, JH, 8H & cut card not H)..4
 *   Flush, 5 cards of a suit (ex 2H, 4H, JH, 8H, & cut card KH)....5
 *    Note - a flush in the crib must include the cut card ................ ..5
 *   Jack as the cut card (His Heels) ........................ ..2 to the dealer
 *   Jack of the same suit as cut card (His nobs) ....................... ..1
 */
	public static int score(dg_Card cards[], boolean isCrib) //5 cards, cards[4] is a temp spot to hold turned-over card
	{
		int total = 0;
		boolean runoffive, runoffour;
		int i, j, k, l;

		/* First look for any one card that yields any points, that would
           only be "his nob" */

		for (i=0; i<4; i++)
		{
			if (cards[i].getNumber() == dg_Card.Number.Jack &&
					cards[i].getSuit() == cards[4].getSuit())
				total++;
		}

		/* Look for points in all combinations of 2 cards */

		for (i=0; i<4; i++)
			for (j=i+1; j<5; j++)
			{
				/* Pair */
				if (cards[i].getNumber() == cards[j].getNumber())
					total+=2;

				/* Fifteen */
				if ((getValue(cards[i]) + getValue(cards[j])) == 15)
					total+=2;
			}


		/* Look for a run of five, as that this precludes runs of 3 and 4 */

		runoffive = false;

		do
		{
			int runcards[] = new int[5];
			int t;

			for (i=0; i<5; i++)
				runcards[i] = cards[i].getPoints();

			t = checkRun(runcards);
			if (t != 0)
				runoffive = true;
			total += t;
		} while (false);

		/* Look for points in all combinations of 4 cards */

		runoffour = false;
		for (i=0; i<2; i++)
			for (j=i+1; j<3; j++)
				for (k=j+1; k<4; k++)
					for (l=k+1; l<5; l++)
					{
						/* Check for fifteens */
						if ( (getValue(cards[i]) +
								getValue(cards[j]) +
								getValue(cards[k]) +
								getValue(cards[l])) == 15)
							total+=2;

						/* Check for runs */

						if (!runoffive)
						{
							int runcards[] = new int[4];
							int t;
							runcards[0] = cards[i].getPoints();
							runcards[1] = cards[j].getPoints();
							runcards[2] = cards[k].getPoints();
							runcards[3] = cards[l].getPoints();
							t = checkRun(runcards);
							if (t != 0)
								runoffour = true;
							total += t;
						}
					} 


		/* Look for points in all combinations of 3 cards */

		for (i=0; i<3; i++)
			for (j=i+1; j<4; j++)
				for (k=j+1; k<5; k++)
				{
					/* Check for fifteens */
					if ( (getValue(cards[i]) +
							getValue(cards[j]) +
							getValue(cards[k])) == 15)
						total+=2;

					/* Check for runs */
					if (!runoffive && !runoffour)
					{
						int runcards[] = new int[3];
						runcards[0] = cards[i].getPoints();
						runcards[1] = cards[j].getPoints();
						runcards[2] = cards[k].getPoints();
						total += checkRun(runcards);
					}
				} 

		/* Check for flush */

		if (cards[0].getSuit() == cards[1].getSuit() &&
				cards[1].getSuit() == cards[2].getSuit() &&
				cards[2].getSuit() == cards[3].getSuit())
		{
			/* Four card flush doesn't count in crib. */
			if (isCrib)
			{
				if (cards[4].getSuit() == cards[0].getSuit())
					total+=5;
			}
			else
			{
				total += 4;
				if (cards[4].getSuit() == cards[0].getSuit())
					total++;
			}
		}

		/* Check for five card fifteens */
		if ( (getValue(cards[0]) +
				getValue(cards[1]) +
				getValue(cards[2]) +
				getValue(cards[3]) +
				getValue(cards[4])) == 15)
			total+=2;

		return total;
	}

	private static int checkRun(int cards[])  //1-13, A to K
	{
		int i, c;
		boolean run=true;
		boolean swap=true;
		/* First bubble sort the cards */

		while (swap)
		{
			swap = false;
			for (i=0; i<(cards.length-1); i++)
			{
				if (cards[i] > cards[i+1])
				{
					swap = true;
					c = cards[i+1];
					cards[i+1] = cards[i];
					cards[i] = c;
				}
			}
		}

		for (i=0; i<cards.length-1; i++)
			if ((cards[i+1] - cards[i]) != 1)
			{
				run = false;
				break;
			}

		if (run) 
			return cards.length;

		return 0;
	}
	/** Static method to create a hand from 6 initial cards.  This
	method takes the six cards and picks 2 discards and creates
	a Hand from the remaining four */
	public static dg_Card[] selectDiscards(dg_Card initCards[], dg_Card disCards[])
	{
		dg_Card handCards[];
		dg_Card cut;
		Hand hands[];
		int meanScore[];
		int maxScorer = 0;
		int bestHand = 0;
		int i, j, k, l, h;

		handCards = new dg_Card[4];
		hands = new Hand[15];
		meanScore = new int[15];

		/* Create the 15 different hands possible form this */

		h = 0;
		for (i=0; i<3; i++)
			for (j=i+1; j<4; j++)
				for (k=j+1; k<5; k++)
					for (l=k+1; l<6; l++)
					{
						handCards[0] = initCards[i];
						handCards[1] = initCards[j];
						handCards[2] = initCards[k];
						handCards[3] = initCards[l];
						hands[h] = new Hand();
						hands[h].set(handCards);
						h++;
					}

		/* Compute mean score for each of the 15 hands */

		for (i=0; i<15; i++)
		{
			meanScore[i] = 0;

			for (j=0; j<52; j++)
			{
				boolean used = false;
				for (k=0; k<6; k++)
					if (initCards[k].getNumber().ordinal() == j+1)
					{
						used = true;
						break;
					}
				if (used)
					continue;

				cut = new dg_Card(j);
				hands[i].setCut(cut);

				meanScore[i] += score(hands[i].hand, false);
			}

			if (meanScore[i] > maxScorer)
			{
				maxScorer = meanScore[i];
				bestHand = i;
			}
		}

		/* find the discards */

		handCards = hands[bestHand].hand;
		k = 0;
		for (i=0; i<6; i++)
		{
			boolean found=false;
			for (j=0; j<4; j++)
				if (initCards[i].equals(handCards[j]))
				{
					found = true;
					break;
				}

			if (!found)
				disCards[k++] = initCards[i];
		}
		hands[bestHand].setCut(null);
		return hands[bestHand].hand;
	}
	static class Hand {
		public dg_Card hand[];
		public Hand() { hand=new dg_Card[5]; }
		public void set(dg_Card c[]) {
			hand[0]=new dg_Card(c[0]);
			hand[1]=new dg_Card(c[1]);
			hand[2]=new dg_Card(c[2]);
			hand[3]=new dg_Card(c[3]);
		}
		/** Sets the cut, or start card for this hand 
	      @param c the start card */
	  public void setCut(dg_Card c)
	  {
	    hand[4] = c==null?null:new dg_Card(c);
	  }
	  public dg_Card getCut() { return hand[4]; }
	  public static Hand create() {
		return new Hand();
	}
	@Override public String toString() {
		return hand[0].toString()+" "+hand[1].toString()+" "+hand[2].toString()+" "+hand[3].toString()+" "+(hand[4]==null?"":hand[4]);
	}
	}
	public static void main(String args[]) {
		dg_CardPile<dg_Card> deck=new dg_CardPile<dg_Card>(new dg_Card(0));
		deck.shuffle(17);
		System.out.println(deck);
		dg_Card discards[]=new dg_Card[2];
		//Card six[]=new Card[6];
		dg_Card hand[]={dg_Card.constant("5s"),dg_Card.constant("6d"),dg_Card.constant("7d"),
				dg_Card.constant("6s"),dg_Card.constant("5s")};
		System.out.println(Arrays.toString(hand)+" :: "+score(hand,false)+"\n");
		/*for (int i=0; i<48; i+=6) {
			for (int j=0; j<6; j++) six[j]=deck.get(i+j);
			System.out.println(Arrays.toString(six));
			Card hand[]=selectDiscards(six, discards);
			System.out.println(Arrays.toString(hand)+" :: "+Arrays.toString(discards));
			hand[4]=deck.get(i+6);
			System.out.println(Arrays.toString(hand)+" :: "+score(hand,false)+"\n");
		}*/
	}
}
