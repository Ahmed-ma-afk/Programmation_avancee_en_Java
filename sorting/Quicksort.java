
package sorting;

// tri rapide

public class Quicksort {

  static void swap(int[] a, int i, int j) {
    int tmp = a[i];
    a[i] = a[j];
    a[j] = tmp;
  }

  static int partition(int[] a, int l, int r) {
    // on suppose l < r i.e. au moins un élément
    int p = a[l], m = l;
    for (int i = l + 1; i < r; i++)
      if (a[i] < p)
        swap(a, i, ++m);
    swap(a, l, m);
    return m;
  }

  static void quickrec(int[] a, int l, int r) {
    if (l >= r - 1)
      return;
    int m = partition(a, l, r);
    quickrec(a, l, m);
    quickrec(a, m + 1, r);
  }

  static void quicksort(int[] a) {
    quickrec(a, 0, a.length);
  }

  /* améliorations (en exercices)
   * - un des deux appels récursifs remplacé par une boucle
   * - un mélange avant de commencer
   */
  
  static void quickrec2(int[] a, int l, int r) {
    while (l < r - 1) {
      int m = partition(a, l, r);
      if (m - l < r - m - 1) {
        quickrec2(a, l, m);
        l = m + 1;
      } else {
        quickrec2(a, m + 1, r);
        r = m;
      }
    }
  }

  static void quicksort2(int[] a) {
    KnuthShuffle.shuffle(a);
    quickrec2(a, 0, a.length);
  }

  // deux améliorations de plus (en exercices)
  // - 3-way partition
  
  /*   l      lo      i       hi    r
   *  +------+-------+-------+-----+
   *  | < v  |  = v  | ????? | > v | 
   *  +------+-------+-------+-----+
   */
 
  static void quickrec3(int[] a, int l, int r) {
    if (l >= r - 1) return;
    int p = a[l], lo = l, hi = r;
    // on partitionne a[l..r[ en trois
    for (int i = l+1; i < hi; ) {
      if (a[i] == p)
        i++;
      else if (a[i] < p)
        swap(a, i++, lo++);
      else // p < a[i] 
        swap(a, i, --hi);
    }
    quickrec3(a, l, lo);
    quickrec3(a, hi, r);
  }
  
  static void quicksort3(int[] a) {
    KnuthShuffle.shuffle(a);
    quickrec3(a, 0, a.length);
  }

  // - tri par insertion sur les petits tableaux
  
  private static final int cutoff = 5;
  
  static void quickrec4(int[] a, int l, int r) {
    while (l < r - 1) {
      if (r - l < cutoff) { InsertionSort.insertionSort(a, l, r); return; }
      // on partitionne a[l..r[ en trois
      // (c'est le drapeau hollandais)
      int p = a[l], lo = l, hi = r;
      //        lo     i     hi
      // ..<p.. ..=p.. ..?.. ..>p..
      for (int i = l+1; i < hi; ) {
        if (a[i] == p)
          i++;
        else if (a[i] < p)
          swap(a, i++, lo++);
        else // p < a[i] 
          swap(a, i, --hi);
      }
      // il faut maintenant trier a[l..lo[ et a[hi..r[
      if (lo - l < r - hi) {
        quickrec4(a, l, lo);
        l = hi;
      } else {
        quickrec4(a, hi, r);
        r = lo;
      }
    }
  }

  static void quicksort4(int[] a) {
    KnuthShuffle.shuffle(a);
    quickrec4(a, 0, a.length);
  }
  
}

class TestQuicksort {

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

  static void test_partition(int[] a, int l, int r) {
    int v = a[l];
    System.out.println("  test avec      a = " + print(a) + " v = " + v);
    int[] occ1 = occurrences(a);
    int m = Quicksort.partition(a, l, r);
    System.out.println("  partition(a," + l + "," + r + ") = " + print(a)
        + " m = " + m);
    int[] occ2 = occurrences(a);
    if (!is_permut(occ1, occ2)) {
      System.out.println("ÉCHEC : les éléments diffèrent");
      System.exit(1);
    }
    for (int i = l; i < r; i++)
      if (!(i < m ? a[i] < v : a[i] >= v)) {
        System.out.println("ÉCHEC : mauvais partitionnement");
        System.exit(1);
      }
  }

  static void test(int[] a) {
    System.out.println("  test avec       a = " + print(a));
    int[] occ1 = occurrences(a);
    Quicksort.quicksort3(a);
    int[] occ2 = occurrences(a);
    System.out.println("  quicksort(a) => a = " + print(a));
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
    System.out.println("test de partition");
    for (int len = 0; len < 10; len++)
      for (int l = 0; l < len; l++)
        for (int r = l + 1; r < len; r++)
          test_partition(random_array(len), l, r);
    System.out.println("test de quicksort");
    for (int len = 0; len < 10; len++)
      for (int j = 0; j <= len; j++)
        test(random_array(len));
    System.out.println("SUCCÈS");

    double start = System.currentTimeMillis();
    int[] large = random_array(100000000);
    Quicksort.quicksort4(large);
    System.out.println("time = " + (System.currentTimeMillis() - start));
    assert is_sorted(large);
  }

}
