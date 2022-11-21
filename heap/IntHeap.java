package heap;

import java.util.NoSuchElementException;

import array.ResizableArray;

// structure de tas, avec tableau redimensionnable

public class IntHeap {

  private ResizableArray elts;

  IntHeap() {
    this.elts = new ResizableArray(0);
  }

  public int size() {
    return this.elts.size();
  }

  public boolean isEmpty() {
    return this.elts.size() == 0;
  }

  private void moveUp(int x, int i) {
    if (i == 0) {
      this.elts.set(i, x);
    } else {
      int fi = (i - 1) / 2;
      int y = this.elts.get(fi);
      if (y > x) {
        this.elts.set(i, y);
        moveUp(x, fi);
      } else
        this.elts.set(i, x);
    }
  }

  public void add(int x) {
    int n = this.elts.size();
    this.elts.setSize(n + 1);
    moveUp(x, n);
  }

  public int getMin() {
    if (this.isEmpty()) throw new NoSuchElementException();
    return this.elts.get(0);
  }

  private void moveDown(int x, int i) {
    int n = this.elts.size();
    int j = 2 * i + 1;
    if (j + 1 < n && this.elts.get(j + 1) < this.elts.get(j))
      j++;
    if (j < n && this.elts.get(j) < x) {
      this.elts.set(i, this.elts.get(j));
      moveDown(x, j);
    } else
      this.elts.set(i, x);
  }

  public void removeMin() {
    if (this.isEmpty()) throw new NoSuchElementException();
    int n = this.elts.size() - 1;
    int x = this.elts.get(n);
    this.elts.setSize(n);
    if (n > 0)
      moveDown(x, 0);
  }

}

class Test {

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
    IntHeap h = new IntHeap();
    for (int x : a) h.add(x);
    for (int i = 0; i < a.length; i++) {
      a[i] = h.getMin();
      h.removeMin();
    }
  }

  public static void main(String[] args) {
    IntHeap h = new IntHeap();
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

  }
}