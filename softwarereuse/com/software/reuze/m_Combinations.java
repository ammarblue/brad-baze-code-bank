package com.software.reuze;
import java.util.Iterator;
import java.util.Arrays;
public class m_Combinations implements Iterator {
private int a[];
private int n,r,numLeft,total;
private long fact(long n) {
    long s=1;
    while (n > 1) s*=n--;
    return s;
}
public m_Combinations(int n, int r) {
  assert(r <= n && r > 0 && n > 0);
  a=new int[r];
  this.n=n;  this.r=r;
  long s=1;
  int i=Math.max(r, n-r);
  int j=Math.min(r, n-r);
  for (; n>i; n--) s*=n;
  total=(int) ((int) s / fact(j));
}
public Iterator iterator() {
  for (int i=0; i<a.length; i++) a[i]=i;
  numLeft=total;
  return this;
}
public boolean hasNext() {
  return numLeft > 0;
}
public void remove() { }
public int[] next() {
  if (numLeft >= total) {
    numLeft--;
    return a;
  }
  int i=r-1;
  while (a[i] == (n-r+i)) i--;
  a[i]++;
  for (int j=i+1; j<r; j++) a[j]=a[i]+j-i;
  numLeft--;
  return a;
}
public static void main(String args[]) {
  m_Combinations x=new m_Combinations(5, 3);
  Iterator i=x.iterator();
  while (i.hasNext()) {
    int []a=(int[])i.next();
    System.out.println(Arrays.toString(a));
  }
}
}
