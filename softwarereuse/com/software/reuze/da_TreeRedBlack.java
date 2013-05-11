package com.software.reuze;
/*http://www.cs.princeton.edu/~rs/talks/LLRB/LLRB.pdf*/
/*TODO N needs fixing for delete, whole code needs testing*/
public class da_TreeRedBlack<Key extends Comparable<Key>, Value>
{
private static final boolean RED = true;
private static final boolean BLACK = false;
private Node root=null;
private int N=0;
private class Node
{
private Key key;
private Value val;
private Node left, right;
private boolean color;
Node(Key key, Value val)
{
this.key = key;
this.val = val;
color = RED;
left=null;  right=null;
}
}
public int size() { return N; }
public Value search(Key key)
{
Node x = root;
while (x != null)
{
int cmp = key.compareTo(x.key);
if (cmp == 0) return x.val;
else if (cmp < 0) x = x.left;
else if (cmp > 0) x = x.right;
}
return null;
}

public boolean isRed(Node h) {
  if (h == null) return false;
  return h.color==RED;
}

void colorFlip(Node h)
{
h.color = !h.color;
h.left.color = !h.left.color;
h.right.color = !h.right.color;
}

public void insert(Key key, Value value)
{
N++;
root = insert(root, key, value);
root.color = BLACK;
}

private Node insert(Node h, Key key, Value value)
{
if (h == null) return new Node(key, value);
if (isRed(h.left) && isRed(h.right)) colorFlip(h);
int cmp = key.compareTo(h.key);
if (cmp == 0) h.val = value;
else if (cmp < 0) h.left = insert(h.left, key, value);
else h.right = insert(h.right, key, value);
if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
return h;
}

Node rotateLeft(Node h)
{
Node x = h.right;
h.right = x.left;
x.left = h;
x.color = h.color;
h.color = RED;
return x;
}

Node rotateRight(Node h)
{
Node x = h.left;
h.left= x.right;
x.right= h;
x.color = h.color;
h.color = RED;
return x;
}

public void deleteMin()
{
root = deleteMin(root);
root.color = BLACK;
}

private Node deleteMin(Node h)
{
if (h.left == null) return null;
if (!isRed(h.left) && !isRed(h.left.left))
h = moveRedLeft(h);
h.left = deleteMin(h.left);
return fixUp(h);
}

private Node moveRedLeft(Node h)
{
colorFlip(h);
if (isRed(h.right.left))
{
h.right = rotateRight(h.right);
h = rotateLeft(h);
colorFlip(h);
}
return h;
}
private Node moveRedRight(Node h)
{
colorFlip(h);
if (isRed(h.left.left))
{
h = rotateRight(h);
colorFlip(h);
}
return h;
}

public void delete(Key key)
{
root = delete(root, key);
root.color = BLACK;
}

private Node delete(Node h, Key key)
{
if (key.compareTo(h.key) < 0)
{
if (!isRed(h.left) && !isRed(h.left.left))
h = moveRedLeft(h);
h.left = delete(h.left, key);
}
else
{
if (isRed(h.left))
h = rotateRight(h);
if (key.compareTo(h.key) == 0 && (h.right == null))
return null;
if (!isRed(h.right) && !isRed(h.right.left))
h = moveRedRight(h);
if (key.compareTo(h.key) == 0)
{
/*If the node
to be deleted is an internal node, we replace its key and value fields with those in the minimum
node in its right subtree and then delete the minimum in the right subtree (or we could rearrange
pointers to use the node instead of copying fields).
*/
h.val = get(h.right, min(h.right).key);
h.key = min(h.right).key;
h.right = deleteMin(h.right);
}
else h.right = delete(h.right, key);
}
return fixUp(h);
}

private Node min(Node x)
{
   if (x.left == null) return x;
   return min(x.left);
}

private Node fixUp(Node h)
{
   if (isRed(h.right))
      h = rotateLeft(h);

   if (isRed(h.left) && isRed(h.left.left))
      h = rotateRight(h);

   if (isRed(h.left) && isRed(h.right))
      colorFlip(h);

   return h;
}

public boolean contains(Key key)
{  return (get(key) != null);  }

public Value get(Key key)
{  return get(root, key);  }

public void put(Key key, Value value)
   {
      root = insert(root, key, value);
      root.color = BLACK;
   }

private Value get(Node x, Key key)
{
   if (x == null)        return null;
   if (eq  (key, x.key)) return x.val;
   if (less(key, x.key)) return get(x.left,  key);
   else                  return get(x.right, key);
}

// Helper methods

    private boolean less(Key a, Key b) { return a.compareTo(b) <  0; }
    private boolean eq  (Key a, Key b) { return a.compareTo(b) == 0; }

public String toString()
   {  
      if (root == null) return "";
      return toString(root);  
   }

public String toString(Node x)
{  
   String s = "";
   if (x.left == null) s += " --"; else s += toString(x.left);
   if (x.right == null) s += " --"; else s += toString(x.right);
   s += " "+x.key;
   if (isRed(x)) s += "*";
   return s;
}

  public static void main(String[] args)
  {
      da_TreeRedBlack<Integer, Integer> st;
      st = new da_TreeRedBlack<Integer, Integer>();
      int[] a = { 3, 1, 4, 2, 5, 9, 6, 8, 7 };
      for (int i = 0; i < a.length; i++)
	  st.put(a[i], i);
      System.out.println(st);
  }
}
