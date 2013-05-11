package com.software.reuze;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.Scanner;


public class dg_CardSolitairePlay<T extends dg_Card> implements List<dg_CardPile<T>>, Iterable<dg_CardPile<T>>, RandomAccess {
	//properties 0,2-5 not set: alternate color, stack move from and to with only selected card 1 less and alternate color from target, any subset from top card can be moved 
	public static final int ACE=0;   //no move from, only consecutive order 2... move to and must match the suit, must be 1st four piles
	public static final int KING=1;  //only move K to empty pile flag
	public static final int DRAW3=2; //no move from or to, draw 3 at a time and place on top of TO3 pile, only 1 such pile allowed
	public static final int TO3=3;   //no move to, target of DRAW3, play only top card, top 3 visible

	public static final int ORDERED=4; //can move a stack if selected card 1 less than target and alternate color and the from-stack is all alternate colors and descending values, any subset from top card can be moved
	public static final int BUFFER1=5; //a one card buffer, can be used to hold any card
	public static final int READ_ONLY=6; //can only be a source flag
	public static final int INIT_ACES=7; //pick a random number A-K flag, set all aces to that number, can only build higher, wraps around

	public static final int MOVEALL_ORONE=8; //can only move whole stack or top card flag
	public static final int MOVE_ANY=9; //can move any card in stack flag
	public static final int COLOR_IGNORE=10; //ignore color on ordering flag
	static final int MOVE_LIMITED=11; //stack moves must flow thru buffer slots flag

	static final int SUIT_MATCH=12; //stack moves must match suit of selected card and target flag
	private dg_CardPile<T>[] piles;
	private int draw3,to3,aceval;
	public dg_CardSolitairePlay(String name, final int start[], final int showing[], int flags[], int seed) {
		set(name, start, showing, flags, seed, (T)new dg_Card(0));
	}
	public dg_CardSolitairePlay(String name, final int start[], final int showing[], int flags[], int seed, T card) {
		set(name, start, showing, flags, seed, card);
	}
	public void set(String name, final int start[], final int showing[], int flags[], int seed, T card) {

		int n=start.length;
		assert(n!=0 && n==showing.length && n==flags.length);
		final dg_CardPile<T> deck=new dg_CardPile<T>(card);
		deck.shuffle(seed);
		piles = new dg_CardPile[n];
		int sum=0;  int i=0;
		for (int j : start) {
			assert(j >= showing[i++]);
			sum+=j;
		}
		assert(sum==52);		
		int k=start.length-1;
		int m=showing[k]; i = k;
		n=start[k];
		piles[i]=new dg_CardPile<T>(card, n);
		ArrayList<T> v2=piles[i].cards;
		for (T c : deck.cards) {
			while (n==0) {
				n=start[--k];
				m=showing[k];
				piles[--i]=new dg_CardPile<T>(card, n);
				v2=piles[i].cards;
			}
			if (m>0) {c.setProperty(dg_Card.FACEUP, true); m--;}
			v2.add(c);
			n--;
		} //for
		while (i>0) piles[--i]=new dg_CardPile(card, n);
		draw3=-1;  to3=-1;
		for (i=0; i<flags.length; i++) {
			int f=flags[i];
			if ((f&(1<<ACE))!=0) piles[i].setProperty(ACE,true);
			if ((f&(1<<KING))!=0) piles[i].setProperty(KING,true);
			if ((f&(1<<BUFFER1))!=0) piles[i].setProperty(BUFFER1,true);
			if ((f&(1<<ORDERED))!=0) piles[i].setProperty(ORDERED,true);
			if ((f&(1<<READ_ONLY))!=0) piles[i].setProperty(READ_ONLY,true);
			if ((f&(1<<MOVEALL_ORONE))!=0) piles[i].setProperty(MOVEALL_ORONE,true);
			if ((f&(1<<MOVE_ANY))!=0) piles[i].setProperty(MOVE_ANY,true);
			if ((f&(1<<COLOR_IGNORE))!=0) piles[i].setProperty(COLOR_IGNORE,true);
			if ((f&(1<<MOVE_LIMITED))!=0) piles[i].setProperty(MOVE_LIMITED,true);
			if ((f&(1<<SUIT_MATCH))!=0) piles[i].setProperty(SUIT_MATCH,true);
			if ((f&(1<<INIT_ACES))!=0) {
				piles[i].setProperty(INIT_ACES,true);
				aceval=(seed%13)+1;
			}
			if ((f&(1<<DRAW3))!=0) {
				assert(draw3 == -1);
				piles[i].setProperty(DRAW3,true);
				draw3=i;
			}
			if ((f&(1<<TO3))!=0) {
				assert(to3 == -1);
				piles[i].setProperty(TO3,true);
				to3=i;
			}
		}
	}
	@Override
	public String toString() {
		StringBuilder s=new StringBuilder();
		int i=0;
		for (dg_CardPile d : piles) {
			s.append(i+" ");
			if (i<4 && d.isProperty(ACE)) {
				if (aceval>0) s.append(dg_Card.toString(aceval));				
				s.append("CDHS".substring(i,i+1)+"[");
			} else s.append(" [");
			for (Object cc : d) {
				dg_Card c = (dg_Card)cc;
				if (!c.isProperty(dg_Card.FACEUP)) s.append("XX");
				else s.append(c.toString());
				s.append(' ');
			}
			s.append("]\n"); i++;
		}
		return s.toString();
	}
	public void deal3() {
		assert(draw3>=0);
		dg_Card c;
		int i=Math.min(piles[draw3].size(), 3);
		if (to3<0) {
			if (i==0) return;
			piles[draw3].get(0).setProperty(dg_Card.FACEUP, true);
			piles[draw3].get(1).setProperty(dg_Card.FACEUP, true);
			piles[draw3].get(2).setProperty(dg_Card.FACEUP, true);
			piles[draw3+1].add(0, piles[draw3].get(0));
			piles[draw3+2].add(0, piles[draw3].get(1));
			piles[draw3+3].add(0, piles[draw3].get(2));
			piles[draw3].clear();
			return;
		}
		if (i==0) {
			i=piles[to3].size();
			while (--i>=0) {
				c = piles[to3].get(i);
				c.setProperty(dg_Card.FACEUP, false);
				piles[draw3].add(piles[to3].get(i));
			}
			piles[to3].clear();
			return;
		}
		int j=Math.min(piles[to3].size(), 3);
		while (--j>=0) {
			c = piles[to3].get(j);
			c.setProperty(dg_Card.FACEUP, false);
		}
		while (--i>=0) {
			c = piles[draw3].get(0);
			piles[to3].add(0, piles[draw3].get(0));
			c.setProperty(dg_Card.FACEUP, true);
			piles[draw3].remove(0);
		}
	}

	public void moveToAce() {
		boolean pass=true;
		dg_Card c;
		for (int i=0; i<4; i++) if (!piles[i].isProperty(ACE)) return;
		while (pass) {
			pass=false;
			for (int i=4; i<piles.length; i++) {
				dg_CardPile p=piles[i];
				if (p.isProperty(DRAW3)) continue;
				if (p.isProperty(ACE)) continue;
					if (piles[i].size()==0) continue;
					c = piles[i].get(0);
					int r=c.getSuit().ordinal();
					if (checkAndMove(i, r, 0)) pass=true;
				}
		} //while
	}
	private static boolean oneHigher(dg_Card c, dg_Card c1) { //wraps around at king
		if (c.getPoints()==1) return c1.getPoints()==13;
		return (c.getPoints()-c1.getPoints())==1;
	}
	public boolean checkAndMove(int fromSlot, int toSlot, int n) {
		if (n<0) return false;
		if (fromSlot==toSlot) return false;
		if (fromSlot<0 || fromSlot>=piles.length) return false;
		if (toSlot<0 || toSlot>=piles.length) return false;
		if (piles[fromSlot].isProperty(DRAW3)) return false;
		if (piles[toSlot].isProperty(DRAW3)) return false;
		if (piles[toSlot].isProperty(READ_ONLY)) return false;
		if (piles[toSlot].isProperty(TO3)) return false;
		//ArrayList<Card> vFrom=piles[fromSlot];
		if (n>=piles[fromSlot].size()) return false;
		if (piles[fromSlot].isProperty(MOVEALL_ORONE) && !(n==0 || n==piles[fromSlot].size()-1)) return false;
		if (piles[fromSlot].isProperty(ACE) && n!=0) return false;
		if (piles[fromSlot].isProperty(TO3) && n!=0) return false;
		if (piles[fromSlot].isProperty(BUFFER1) && n!=0) return false;
		if (piles[toSlot].isProperty(BUFFER1) && n!=0) return false;
		dg_Card c = piles[fromSlot].get(n);
		if (!c.isProperty(dg_Card.FACEUP)) return false;
		//ArrayList<Card> vTo=piles[toSlot];
		if (n>0 && piles[fromSlot].isProperty(ORDERED)) {
			if (!isOrderedAt(fromSlot, n, !piles[toSlot].isProperty(COLOR_IGNORE))) return false;
			if (piles[toSlot].size()!=0) {
				dg_Card c1 = piles[toSlot].get(0);
				if (!piles[toSlot].isProperty(COLOR_IGNORE) && (c.isRed()==c1.isRed())) return false;
				if (!oneHigher(c1, c)) return false;
			}
		}
		if (piles[toSlot].size()!=0) {
		  if (piles[toSlot].isProperty(BUFFER1)) return false;
		  dg_Card c1 = piles[toSlot].get(0);
		  if (piles[toSlot].isProperty(ACE)) {
			  if (c.getSuit().ordinal()!=toSlot) return false;
			  if (!oneHigher(c, c1)) return false;
		  } else {
			  if (piles[toSlot].isProperty(SUIT_MATCH)) {
				  if (c.getSuit()!=c1.getSuit()) return false;
			  } else if (!piles[toSlot].isProperty(COLOR_IGNORE) && !(c.isRed() ^ c1.isRed())) return false;
			  if (!oneHigher(c1, c)) return false;
		  }
		} else if (piles[toSlot].isProperty(ACE)) {
			if (c.getSuit().ordinal()!=toSlot) return false; 
			if (c.getPoints()!=((aceval==0)?1:aceval)) return false;
		} else if (piles[toSlot].isProperty(KING))
			if (c.getNumber()!=dg_Card.Number.King) return false;
		if (piles[fromSlot].isProperty(MOVE_ANY)) {
			piles[toSlot].add(0, piles[fromSlot].remove(n));
			return true;
		}
		if (n>0 && piles[toSlot].isProperty(MOVE_LIMITED)) {
			int i=getFreeCount();
			if (n>i) return false;
		}
		while (n>=0) {
			piles[toSlot].add(0, piles[fromSlot].remove(n--));
		}
		if (piles[fromSlot].size()!=0) {
			c = piles[fromSlot].get(0);
			c.setProperty(dg_Card.FACEUP, true);
		}
		return true;
	}
	public int getFreeCount() {
		int k=0;
		for (dg_CardPile p : piles) {
			if (!p.isEmpty()) continue;
			if (p.isProperty(BUFFER1)) k++;
			if (p.isProperty(MOVE_LIMITED)) k++;
		}
		return k;
	}
	public boolean isOrderedAt(int n, int k, boolean includeColor) {
		if (n<0||n>=piles.length) return false;
		//ArrayList<Card> p=piles[n];
		if (k<0||k>=piles[n].size()) return false;
		if (k==0) return true;
		dg_Card c=piles[n].get(k);
		boolean red=!c.isRed();
		int m=n;
		n=c.getPoints();
		while (--k>=0) {
			n--;
			c=piles[m].get(k);
			if (includeColor && (c.isRed()!=red)) return false;
			if (n!=c.getPoints()) return false;
			red = ! red;
		}
		return true;
	}
	public boolean add(dg_CardPile<T> o) {
		// TODO Auto-generated method stub
		return false;
	}
	public void add(int index, dg_CardPile<T> element) {
		// TODO Auto-generated method stub
		
	}
	public boolean addAll(Collection<? extends dg_CardPile<T>> c) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean addAll(int index, Collection<? extends dg_CardPile<T>> c) {
		// TODO Auto-generated method stub
		return false;
	}
	public void clear() {
		piles=null;
	}
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}
	public dg_CardPile<T> get(int index) {
		return piles[index];
	}
	public int indexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
	public Iterator<dg_CardPile<T>> iterator() {
		return Arrays.asList(piles).iterator();
	}
	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
	public ListIterator<dg_CardPile<T>> listIterator() {
		return Arrays.asList(piles).listIterator();
	}
	public ListIterator<dg_CardPile<T>> listIterator(int index) {
		return Arrays.asList(piles).listIterator(index);
	}
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}
	public dg_CardPile<T> remove(int index) {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}
	public dg_CardPile<T> set(int index, dg_CardPile<T> element) {
		// TODO Auto-generated method stub
		return null;
	}
	public int size() {
		return piles.length;
	}
	public List<dg_CardPile<T>> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	public Object[] toArray() {
		return piles;
	}
	public dg_CardPile<T>[] toArray(dg_CardPile<T>[] a) {
		return piles;
	}
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}
	public static void main(String[] args) {
		
		dg_CardSolitairePlay s=null;
		Scanner in=new Scanner(System.in);
		while (true) {
		System.out.println(s);
		System.out.print("action>");
		int fromSlot=0;;
		int action=in.nextInt();
		int toSlot=0;
		switch (action) {
		case 0: System.exit(0);
		case 1: //solitaire
			final int start[]= {0,0,0,0,24,0, 1, 2, 3, 4, 5, 6, 7};
			final int faceup[]={0,0,0,0, 0,0, 1, 1, 1, 1, 1, 1, 1};
			final int flags[]= {1,1,1,1, 4,8,18,18,18,18,18,18,18};
			fromSlot=in.nextInt();
			s=new dg_CardSolitairePlay("Solitaire",start,faceup,flags,fromSlot);
			break;
		case 2: //move from to N
			fromSlot=in.nextInt();
			toSlot=in.nextInt();
			int n=in.nextInt()-1;
			if (s.checkAndMove(fromSlot, toSlot, n)) break;
			break;
		case 3: //deal 3
			s.deal3();
			break;
		case 4: //yukon
			final int start2[]= {0,0,0,0, 1,6,7,8,9,10,11};
			final int faceup2[]={0,0,0,0, 1,5,5,5,5, 5, 5};
			final int flags2[]= {1,1,1,1, 2,2,2,2,2, 2, 2};
			fromSlot=in.nextInt();
			s=new dg_CardSolitairePlay("Yukon", start2,faceup2,flags2,fromSlot);
			break;
		case 5:
			s.moveToAce();
			break;
		case 6: //freecell
			final int start3[]= {0,0,0,0, 0,0,0,0,    7,7,7,7,6,6,6,6};
			final int faceup3[]={0,0,0,0, 0,0,0,0,    7,7,7,7,6,6,6,6};
			final int flags3[]= {1,1,1,1, 32,32,32,32, 0x810,0x810,0x810,0x810,0x810,0x810,0x810,0x810};
			fromSlot=in.nextInt();
			s=new dg_CardSolitairePlay("FreeCell",start3,faceup3,flags3,fromSlot);
			break;
		case 7: //canfield
			final int start4[]= {  0,  0,  0,  0, 12,36,0,    1,    1,    1,    1};
			final int faceup4[]={  0,  0,  0,  0,  1, 0,0,    1,    1,    1,    1};
			final int flags4[]= {129,129,129,129, 64, 4,8,0x110,0x110,0x110,0x110};
			fromSlot=in.nextInt();
			s=new dg_CardSolitairePlay("CanField",start4,faceup4,flags4,fromSlot);
			break;
		case 8: //flower garden
			final int start5[]= {  0,  0,  0,  0,    16,     6,     6,    6,    6,    6,    6};
			final int faceup5[]={  0,  0,  0,  0,    16,     6,     6,    6,    6,    6,    6};
			final int flags5[]= {  1,  1,  1,  1, 0x240, 0xD00, 0xD00,0xD00,0xD00,0xD00,0xD00};
			fromSlot=in.nextInt();
			s=new dg_CardSolitairePlay("Flower Garden",start5,faceup5,flags5,fromSlot);
			break;
		case 9: //king albert
			final int start6[]= {  0,  0,  0,  0,    7,     1,2,3,4,5,6,7,8,9};
			final int faceup6[]={  0,  0,  0,  0,    7,     1,2,3,4,5,6,7,8,9};
			final int flags6[]= {  1,  1,  1,  1, 0x240, 0x500,0x500,0x500,0x500,0x500,0x500,0x500,0x500,0x500};
			fromSlot=in.nextInt();
			s=new dg_CardSolitairePlay("King Albert",start6,faceup6,flags6,fromSlot);
			break;
		case 10: //scorpion
			final int start7[]= {   3, 7, 7, 7, 7, 7, 7, 7};
			final int faceup7[]={   0, 4, 4, 4, 4, 7, 7, 7};
			final int flags7[]= {0x44, 0x1002, 0x1002, 0x1002, 0x1002, 0x1002, 0x1002, 0x1002};
			fromSlot=in.nextInt();
			s=new dg_CardSolitairePlay("Scorpion",start7,faceup7,flags7,fromSlot);
			break;
		} //switch
		}
	}
}
