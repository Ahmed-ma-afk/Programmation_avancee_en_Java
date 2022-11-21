package sorting;

// tri par tas

public class Heapsort {

  // recursive version of moveDown
  static void moveDownRec(int[] a, int k, int v, int n) {
    int r = 2 * k + 1;
    if (r >= n)
      a[k] = v;
    else {
      if (r + 1 < n && a[r] < a[r + 1])
        r++;
      if (a[r] <= v)
        a[k] = v;
      else {
        a[k] = a[r];
        moveDownRec(a, r, v, n);
      }
    }
  }

  static void moveDown(int[] a, int i, int x, int n) {
    while (true) {
      int j = 2 * i + 1;
      if (j >= n) break;
      if (j + 1 < n && a[j] < a[j + 1]) j++;
      if (a[j] <= x) break;
      a[i] = a[j];
      i = j;
    }
    a[i] = x;
  }

  static void heapsort(int[] a) {
    int n = a.length;
    for (int k = n / 2 - 1; k >= 0; k--)
      moveDown(a, k, a[k], n);
    for (int k = n - 1; k >= 1; k--) {
      int v = a[k];
      a[k] = a[0];
      moveDown(a, 0, v, k);
    }
  }

}

class TestHeapsort {

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
    Heapsort.heapsort(a);
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

  public static void main(String[] args) {
    System.out.println("test de mergesort");
    for (int len = 0; len < 10; len++)
      for (int j = 0; j <= len; j++)
        test(random_array(len));
    System.out.println("SUCCÈS TestHeapsort");
  }

}
