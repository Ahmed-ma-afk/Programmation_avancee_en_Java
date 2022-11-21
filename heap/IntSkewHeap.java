package heap;

import java.util.NoSuchElementException;
import java.util.Vector;

import trees.Tree;

public class IntSkewHeap {

  private Tree root;
  private int size;
  
  public IntSkewHeap() {
    this.root = null;
    this.size = 0;
  }
  
  public boolean isEmpty() {
    return this.size == 0;
  }

  public int size() {
    return this.size;
  }

  private static Tree merge(Tree t1, Tree t2) {
    if (t1 == null) return t2;
    if (t2 == null) return t1;
    if (t1.value <= t2.value)
      return new Tree(merge(t1.right, t2), t1.value, t1.left);
    else
      return new Tree(merge(t2.right, t1), t2.value, t2.left);
  }
  
  public void add(int x) {
    this.root = merge(this.root, new Tree(null, x, null));
    this.size++;
  }

  public int getMin() {
    if (this.isEmpty()) throw new NoSuchElementException();
    return this.root.value;
  }

  public int removeMin() {
    if (this.isEmpty()) throw new NoSuchElementException();
    int res = this.root.value;
    this.root = merge(this.root.left, this.root.right);
    this.size--;
    return res;
  }

  public void merge(IntSkewHeap that) {
    this.root = merge(this.root, that.root);
    this.size += that.size;
  }
  
  @SuppressWarnings("unused")
  private static Tree nonRecursiveMerge(Tree t1, Tree t2) {
    if (t1 == null && t2 == null) return null;
    Vector<Tree> v = new Vector<Tree>();
    // sorts the right paths, using merge sort
    while (t1 != null || t2 != null) {
      if (t2 == null || (t1 != null && t1.value <= t2.value)) { // take from t1
        v.add(new Tree(t1.left, t1.value, null));
        t1 = t1.right;
      } else { // take from t2
        v.add(new Tree(t2.left, t2.value, null));
        t2 = t2.right;        
      }
    }
    // recombine the last two elements, until one is left
    while (v.size() > 1) {
      int i = v.size() - 2;
      Tree t = v.get(i);
      t.right = t.left;
      t.left = v.get(i + 1);
      v.setSize(i + 1); // drop last
    }
    return v.get(0);
  } 
    
  public String toString() {
    return Tree.stringOfTree(this.root);
  }
  
}

class TestIntSkewHeap {

  static boolean is_sorted(int[] a) {
    for (int i = 1; i < a.length; i++)
      if (!(a[i - 1] <= a[i]))
        return false;
    return true;
  }

  static final int M = 10; // les éléments sont dans 0..M-1

  static int[] occurrences(int[] a) {
    int[] occ = new int[M];
    for (int i = 0; i < a.length; i++)
      occ[a[i]]++;
    return occ;
  }

  static boolean is_permut(int[] occ1, int[] occ2) {
    for (int i = 0; i < M; i++)
      if (occ1[i] != occ2[i])
        return false;
    return true;
  }

  static String print(int[] a) {
    String s = "[";
    for (int i = 0; i < a.length; i++)
      s += (i == 0 ? "" : ", ") + a[i];
    return s + "]";
  }

  static int[] random_array(int len) {
    int[] a = new int[len];
    for (int i = 0; i < len; i++)
      a[i] = (int) (M * Math.random());
    return a;
  }

  static void test(int[] a) {
    System.out.println("  test avec       a = " + print(a));
    int[] occ1 = occurrences(a);
    heapsort(a);
    int[] occ2 = occurrences(a);
    System.out.println("  heapsort(a) => a = " + print(a));
    if (!is_sorted(a)) {
      System.out.println("ÉCHEC : le résultat n'est pas trié");
      System.exit(1);
    }
    if (!is_permut(occ1, occ2)) {
      System.out.println("ÉCHEC : les éléments diffèrent");
      System.exit(1);
    }
  }

  static void heapsort(int[] a) {
    IntSkewHeap h = new IntSkewHeap();
    for (int x : a) h.add(x);
    for (int i = 0; i < a.length; i++) {
      a[i] = h.getMin();
      h.removeMin();
    }
  }

  public static void main(String[] args) {
    IntSkewHeap h = new IntSkewHeap();
    h.add(1);
    h.add(2);
    h.add(2);
    h.add(3);
    assert (h.size() == 4);
    assert (h.getMin() == 1);
    h.removeMin();
    assert (h.size() == 3);
    assert (h.getMin() == 2);
    h.removeMin();
    assert (h.size() == 2);
    assert (h.getMin() == 2);
    h.removeMin();
    assert (h.size() == 1);
    assert (h.getMin() == 3);
    h.removeMin();
    assert (h.isEmpty());

    System.out.println("test de heapsort");
    for (int len = 0; len < 10; len++)
      for (int j = 0; j <= len; j++)
        test(random_array(len));
    System.out.println("SUCCÈS");
    
    IntSkewHeap p1 = new IntSkewHeap();
    IntSkewHeap p2 = new IntSkewHeap();
    for (int i = 0; i < 30; i++)
      (i % 2 == 0 ? p1 : p2).add(i);
    p1.merge(p2);
    System.out.println(p1);
    while (!p1.isEmpty()) System.out.print(p1.removeMin() + " ");
    System.out.println();
    
    System.out.println("TestIntSkewHeap");
}
}

