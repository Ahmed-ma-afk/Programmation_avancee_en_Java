package trees;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class AVL {
  int value;
  AVL left, right;
  int height;

  private AVL(int value) {
    this.left = this.right = null;
    this.value = value;
    this.height = 1;
  }

  private AVL(AVL left, int value, AVL right) {
    this.left = left;
    this.value = value;
    this.right = right;
    this.height = 1 + Math.max(height(left), height(right));
  }

  static int height(AVL a) {
    return (a == null) ? 0 : a.height;
  }

  static boolean contains(AVL a, int x) {
    while (a != null) {
      if (x == a.value) return true;
      a = (x < a.value) ? a.left : a.right;
    }
    return false;
  }
  
  static AVL add(AVL b, int x) {
    if (b == null)
      return new AVL(x);
    if (x < b.value)
      b.left = add(b.left, x);
    else if (x > b.value)
      b.right = add(b.right, x);
    return balance(b);
  }

  // suppose b != null
  static int getMin(AVL b) {
    while (b.left != null)
      b = b.left;
    return b.value;
  }

  // suppose b != null
  static AVL removeMin(AVL b) {
    if (b.left == null)
      return b.right;
    b.left = removeMin(b.left);
    return balance(b);
  }

  static AVL remove(AVL b, int x) {
    if (b == null)
      return null;
    if (x < b.value)
      b.left = remove(b.left, x);
    else if (x > b.value) 
      b.right = remove(b.right, x);
    else { // x == b.value
      if (b.right == null)
        return b.left;
      if (b.left == null) // optim
        return b.right;
      b.value = getMin(b.right);
      b.right = removeMin(b.right);
    }
    return balance(b);
  }

  static LinkedList<Integer> toList(AVL t) {
    LinkedList<Integer> l = new LinkedList<Integer>();
    toList(l, t);
    return l;
  }
  
  private static void toList(LinkedList<Integer> l, AVL t) {
    if (t == null) return;
    toList(l, t.left);
    l.add(t.value);
    toList(l, t.right);
  }
 
  static int size(AVL t) {
    if (t == null)
      return 0;
    return 1 + size(t.left) + size(t.right);
  }

  /* équilibrage
   * 
   * tout se passe ici : le code ci-dessus est le même que dans BSTSet
   * si on omet les occurrences de balance
   */
  
  // rotation droite (simple)
  private static AVL rotateRight(AVL t) {
    assert t != null && t.left != null;
    AVL l = t.left;
    t.left = l.right;
    l.right = t;
    t.height = 1 + Math.max(height(t.left), height(t.right));
    l.height = 1 + Math.max(height(l.left), height(l.right));
    return l;
  }

  // rotation gauche (simple)
  private static AVL rotateLeft(AVL t) {
    assert t != null && t.right != null;
    AVL r = t.right;
    t.right = r.left;
    r.left = t;
    t.height = 1 + Math.max(height(t.left), height(t.right));
    r.height = 1 + Math.max(height(r.left), height(r.right));
    return r;
  }

  private static AVL balance(AVL t) {
    assert t != null;
    AVL l = t.left, r = t.right;
    int hl = height(l), hr = height(r);
    if (hl > hr + 1) {
      AVL ll = l.left, lr = l.right;
      if (height(ll) >= height(lr))
        return rotateRight(t);
      else {
        t.left = rotateLeft(t.left);
        return rotateRight(t);
      }
    } else if (hr > hl + 1) {
      AVL rl = r.left, rr = r.right;
      if (height(rr) >= height(rl))
        return rotateLeft(t);
      else {
        t.right = rotateRight(t.right);
        return rotateLeft(t);
      }
    } else {
      t.height = 1 + Math.max(hl, hr);
      return t;
    }
  }
  
  // file -> AVL en temps linéaire
  
  static AVL ofList(Queue<Integer> l, int n) {
    if (n == 0) return null;
    int n1 = (n - 1) / 2;
    AVL left = ofList(l, n1);
    int v = l.poll();
    AVL right = ofList(l, n - n1 - 1);
    return new AVL(left, v, right);
  }
  
  static AVL ofList(Queue<Integer> l) {
    return ofList(l, l.size());
  }

  // on en déduit des opérations ensemblistes
  static AVL union(AVL t1, AVL t2) {
    return ofList(listUnion(toList(t1), toList(t2)));
  }
  private static LinkedList<Integer> listUnion(LinkedList<Integer> l1, LinkedList<Integer> l2) {
  	LinkedList<Integer> l = new LinkedList<Integer>();
  	while (!l1.isEmpty() || !l2.isEmpty()) {
  		if (l2.isEmpty() || !l1.isEmpty() && l1.peek() <= l2.peek())
  			l.add(l1.remove());
  		else
  			l.add(l2.remove());
  	}
  	return l;
  }
  
  private static void stringOfTree(StringBuffer sb, AVL t) {
    if (t == null) return;
    sb.append("(");
    stringOfTree(sb, t.left);
    sb.append("" + t.value + "[" + t.height + "]");
    stringOfTree(sb, t.right);
    sb.append(")");
  }

  public static String stringOfTree(AVL t) {
    StringBuffer sb = new StringBuffer();
    stringOfTree(sb, t);
    return sb.toString();
  }
  
  public String toString() {
    return stringOfTree(this);
  }

  // quelques fonctions pour vérifier qu'il s'agit bien d'un AVL
  
  static boolean isAVL(AVL t) {
    return isBST(t) && isBalanced(t);
  }
  
  private static boolean isBST(AVL t) {
    return isBST(t, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  private static boolean isBST(AVL t, int min, int max) {
    if (t == null)
      return true;
    if (t.value <= min)
      return false;
    if (t.value >= max)
      return false;
    return isBST(t.left, min, t.value) && isBST(t.right, t.value, max);
  }
  
  private static boolean isBalanced(AVL t) {
    if (t == null)
      return true;
    if (Math.abs(height(t.left) - height(t.right)) > 1)
      return false;
    return isBalanced(t.left) && isBalanced(t.right);
  }
}

class TestAVL {
	public static void main(String[] args) {
	  {
	    AVL a = null;
	    for (int i = 0; i < 100; i++) {
	      a = AVL.add(a, i);
	      assert AVL.isAVL(a);
	      System.out.println(a);
	      System.out.println(AVL.height(a));
	    }
	  }
	  System.exit(0);
	  
		Queue<Integer> l = new LinkedList<Integer>();
		for (int len = 0; len < 100; len++) {
			for (int i = 0; i < len; i++) l.add(i);
			System.out.print(l.size() + " ");
			AVL a = AVL.ofList(l);
			System.out.println(AVL.height(a));
		}
		
		Queue<Integer> l1 = new LinkedList<Integer>();
		Queue<Integer> l2 = new LinkedList<Integer>();
		for (int i = 0; i < 100; i++) { if (i % 2 == 0) l1.add(i); else l2.add(i); }
		AVL a = AVL.union(AVL.ofList(l1), AVL.ofList(l2));
		assert AVL.isAVL(a);
		assert AVL.size(a) == 100;
		
		System.out.println("TESTAVL OK");
	}
}

// encapsulation dans une structure mutable avec méthodes dynamiques

public class AVLSet  implements intf.Set<Integer> {
  private AVL root;
  private int size;

  public AVLSet() {
    this.root = null;
  }
  
  public boolean isEmpty() {
    return this.root == null;
  }

  public void clear() {
    this.root = null;
    this.size = 0;
  }
   
  public boolean contains(Integer x) {
    return AVL.contains(this.root, x);
  }

  public void add(Integer x) {
  	if (!AVL.contains(this.root, x)) this.size++;
    this.root = AVL.add(this.root, x);
  }

  public int getMin() {
    return AVL.getMin(this.root);
  }

  public void removeMin() {
  	if (isEmpty()) throw new IllegalArgumentException();
  	this.size--;
    this.root = AVL.removeMin(this.root);
  }

  public void remove(Integer x) {
  	if (AVL.contains(this.root, x)) this.size--;
  	this.root = AVL.remove(this.root, x);
  }
  
  public List<Integer> toList() {
    return AVL.toList(this.root);
  }
  
  public int size() {
    return this.size;
  }
  
  public boolean check() {
    return AVL.isAVL(this.root);
  }

}

class TestAVLSet {
  public static void main(String[] args) {
    AVLSet s = new AVLSet();
    for (int i = 0; i < 10; i++) {
      assert s.size() == i;
      assert !s.contains(i);
      s.add(i);
      assert s.contains(i);
      assert s.check();
    }
    for (int i = 0; i < 10; i++) {
      assert s.contains(i);
      assert s.getMin() == i;
      s.remove(i);
      assert i == 9 || s.getMin() == i+1;
      assert !s.contains(i);
      assert s.check();
    }
    s.clear();
    assert s.size() == 0;
    for (int i = 0; i < 100; i++) {
      int x = (int)(Math.random() * 100);
      s.add(x);
      assert s.contains(x);
      assert s.size() <= i+1;
      assert s.check();
    }
    System.out.println("TestAVLSet OK");
  }
}
