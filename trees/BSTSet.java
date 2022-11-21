package trees;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

// arbres binaires de recherche (contenant des entiers)

class BST {
  int value;
  BST left, right;

  BST(BST left, int value, BST right) {
    this.left = left;
    this.value = value;
    this.right = right;
  }

  BST(int value) {
    this(null, value, null);
  }

  static int size(BST t) {
    if (t == null)
      return 0;
    return 1 + size(t.left) + size(t.right);
  }

  static int height(BST t) {
    if (t == null)
      return 0;
    return 1 + Math.max(height(t.left), height(t.right));
  }

  static boolean contains(BST b, int x) {
    while (b != null) {
      if (x == b.value)
        return true;
      b = (x < b.value) ? b.left : b.right;
    }
    return false;
  }

  // version récursive
  static boolean containsRec(BST b, int x) {
    if (b == null) return false;
    if (x == b.value) return true;
    return containsRec(x < b.value ? b.left : b.right, x);
  }

  static BST add(BST b, int x) {
    if (b == null)
      return new BST(x);
    if (x < b.value)
      b.left = add(b.left, x);
    else if (x > b.value)
      b.right = add(b.right, x);
    return b;
  }

  // variante itérative
  static BST addIter(BST b, int x) {
    if (b == null) return new BST(x);
    BST t = b;
    while (true) { // invariant t != null
      if (x == t.value) return b;
      if (x < t.value)
        if (t.left != null) t = t.left;
        else { t.left = new BST(x); return b; }
      else
        if (t.right != null) t = t.right;
        else { t.right = new BST(x); return b; }
    }
  }

  // suppose b != null
  static int getMin(BST b) {
    while (b.left != null)
      b = b.left;
    return b.value;
  }

  // suppose b != null
  static BST removeMin(BST b) {
    if (b.left == null)
      return b.right;
    b.left = removeMin(b.left);
    return b;
  }

  static BST remove(BST b, int x) {
    if (b == null)
      return null;
    if (x < b.value)
      b.left = remove(b.left, x);
    else if (x > b.value) 
      b.right = remove(b.right, x);
    else { // x == b.value
      if (b.right == null)
        return b.left;
//      if (b.left == null) // optim
//        return b.right;
      b.value = getMin(b.right);
      b.right = removeMin(b.right);
    }
    return b;
  }

  static int floorrec(BST b, int x) {
    if (b == null) throw new NoSuchElementException();
    if (x < b.value) return floor(b.left, x);
    if (x == b.value) return x;
    try { return floor(b.right, x); }
    catch (NoSuchElementException e) { return b.value; }
  }
  
  static int floor(BST b, int x) {
    Integer c = null;
    while (b != null) {
      if (x == b.value) return x;
      if (x < b.value) b = b.left;
      else { c = b.value; b = b.right; }
    }
    if (c == null) throw new NoSuchElementException();
    return c;
  }
  
  static BST ofList(Queue<Integer> l, int n) {
    if (n == 0) return null;
    else { // n >= 1
      int n1 = (n - 1) / 2;
      BST left = ofList(l, n1);
      int v = l.poll();
      BST right = ofList(l, n - n1 - 1);
      return new BST(left, v, right);
    }
  }
  
  static BST ofList(Queue<Integer> l) {
    return ofList(l, l.size());
  }

  /* same fringe: do two BST contain the same values?
   * the left branch of the tree is built as a list of
   * pairs (value, right sub-tree), of type Enum
   * the list is bottom-up: the head is the leftmost,
   * innermost element of the tree
   */
  private static class Enum {
    final int root;
    final BST right;
    final Enum next;
    public Enum(int root, BST right, Enum next) {
      this.root = root;
      this.right = right;
      this.next = next;
    }
    static Enum build(BST t, Enum acc) {
      while (t != null) {
        acc = new Enum(t.value, t.right, acc);
        t = t.left;
      }
      return acc;
    }
    static boolean equals(Enum x, Enum y) {
      while (x != null || y != null) {
        if (x == null || y == null) return false;
        if (x.root != y.root) return false;
        x = build(x.right, x.next);
        y = build(y.right, y.next);
      }
      return true;
    }
  }
  
  static boolean equals(BST x, BST y) {
    return Enum.equals(Enum.build(x,  null), Enum.build(y, null));
  }
 
  /* subset */
  static boolean subset(BST s1, BST s2) {
    if (s1 == null) return true;
    if (s2 == null) return false;
    if (s1.value == s2.value)
      return subset(s1.left, s2.left) && subset(s1.right, s2.right);
    if (s1.value < s2.value)
      return subset(new BST(s1.left, s1.value, null), s2.left)
          && subset(s1.right, s2);
    else
      return subset(new BST(null, s1.value, s1.right), s2.right)
          && subset(s1.left, s2);
  }
  
  /* split(v,s) returns two trees, containing values
   * from s smaller and greater than s
   */
  static BST[] split(int x, BST s) {
    if (s == null) return new BST[] { null, null };
    if (x == s.value) return new BST [] { s.left, s.right };
    if (x < s.value) {
      BST[] spl = split(x, s.left);
      spl[1] = new BST(spl[1], s.value, s.right);
      return spl;
    } else {
      BST[] spl = split(x, s.right);
      spl[0] = new BST(s.left, s.value, spl[0]);
      return spl;
    }
  }
  
  /* union */
  static BST union(BST s1, BST s2) {
    if (s1 == null) return s2;
    if (s2 == null) return s1;
    BST[] spl = split(s1.value, s2);
    return new BST(union(s1.left, spl[0]), s1.value, union(s1.right, spl[1]));
  }

  static BST diff(BST s1, BST s2) {
    if (s1 == null) return null;
    if (s2 == null) return s1;
    BST[] spl = split(s1.value, s2);
    if (contains(s2, s1.value))
      return merge(diff(s1.left, spl[0]), diff(s1.right, spl[1]));
    else
      return new BST(diff(s1.left, spl[0]), s1.value, diff(s1.right, spl[1]));
  }
  
  /* merge(s1, s2) merges two trees, elements of s1 being
   * smaller than elements of s2 */
  static BST merge(BST s1, BST s2) {
      if (s1 == null) return s2;
      if (s2 == null) return s1;
      return new BST(s1, getMin(s2), removeMin(s2));
  }
  
  public String toString() {
    return "(" + this.left + "+" + this.value + "+" + this.right + ")";
  }
  
  static List<Integer> toList(BST s) {
    List<Integer> l = new LinkedList<Integer>();
    toList(l, s);
    return l;
  }
  
  private static void toList(List<Integer> l, BST s) {
    if (s == null) return;
    toList(l, s.left);
    l.add(s.value);
    toList(l, s.right);
  }
  
}

public class BSTSet implements intf.Set<Integer> {
  private BST root;

  public BSTSet() {
    this.root = null;
  }
  
  private BSTSet(BST s) {
    this.root = s;
  }
  
  public int height() {
    return BST.height(this.root);
  }
  
  public int size() {
    return BST.size(this.root);
  }
  
  public boolean isEmpty() {
    return this.root == null;
  }

  public boolean contains(Integer x) {
    return BST.contains(this.root, x);
  }

  public void add(Integer x) {
    this.root = BST.add(this.root, x);
  }

  public int getMin() {
    return BST.getMin(this.root);
  }

  public void removeMin() {
    this.root = BST.removeMin(this.root);
  }

  public void remove(Integer x) {
    this.root = BST.remove(this.root, x);
  }
  
  public List<Integer> toList() {
    return BST.toList(this.root);
  }

  public void clear() {
    this.root = null;
  }
   
  public boolean subset(BSTSet s) {
    return BST.subset(this.root, s.root);
  }
  
  public BSTSet diff(BSTSet s) {
    return new BSTSet(BST.diff(this.root, s.root));
  }
  
  public boolean equals(BSTSet s) {
    return BST.equals(this.root, s.root);
  }
  
}

class TestBSTSet {
  public static void main(String[] args) {
    BSTSet s = new BSTSet();
    for (int i = 0; i < 10; i++) {
      assert s.size() == i;
      assert !s.contains(i);
      s.add(i);
      assert s.contains(i);
    }
    BSTSet t = new BSTSet();
    for (int i = 9; i >= 0; i--)
      t.add(i);
    assert s.equals(t);
    assert t.equals(s);
    for (int i = 0; i < 10; i++) {
      assert s.contains(i);
      assert s.getMin() == i;
      s.remove(i);
      assert i == 9 || s.getMin() == i+1;
      assert !s.contains(i); 
    }
    s.clear();
    assert !s.equals(t);
    assert s.size() == 0;
    for (int i = 0; i < 100; i++) {
      int x = (int)(Math.random() * 100);
      s.add(x);
      assert s.contains(x);
      assert s.size() <= i+1;
    }
    System.out.println("TestBSTSet OK");
  }
}
