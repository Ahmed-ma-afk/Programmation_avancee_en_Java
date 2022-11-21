package sorting;

import java.util.Arrays;

public class Mergesort {

  // merge a1[l..m[ and a1[m..r[ into a2[l..r[
  static void merge(int[] a1, int[] a2, int l, int m, int r) {
    int i = l, j = m;
    for (int k = l; k < r; k++)
      if (i < m && (j == r || a1[i] <= a1[j]))
        a2[k] = a1[i++];
      else
        a2[k] = a1[j++];
  }

  // recursive, top-down mergesort
  static void mergesortrec(int[] a, int[] tmp, int l, int r) {
    if (l >= r-1) return;
    int m = l + (r - l) / 2;
    mergesortrec(a, tmp, l, m);
    mergesortrec(a, tmp, m, r);
    if (a[m-1] <= a[m]) return; // optim
    for (int i = l; i < r; i++) tmp[i] = a[i];
    merge(tmp, a, l, m, r);
  }
  
  static void mergesort(int[] a) {
    mergesortrec(a, new int[a.length], 0, a.length);
  }

  // exercice : éviter la copie de a vers tmp
  static void mergesort2rec(int[] a, int[] tmp, int l, int r, boolean inplace) {
    if (l >= r-1) return;
    int m = l + (r - l) / 2;
    mergesort2rec(a, tmp, l, m, !inplace);
    mergesort2rec(a, tmp, m, r, !inplace);
    if (inplace) merge(tmp, a, l, m, r); else merge(a, tmp, l, m, r);
  }
  
  static void mergesort2(int[] a) {
    mergesort2rec(a, Arrays.copyOf(a, a.length), 0, a.length, true);
  }
  
  // bottom-up mergesort
  static void bottomUpMergesort(int[] a) {
    int n = a.length;
    int[] tmp = new int[n];
    for (int len = 1; len < n; len *= 2)
      for (int lo = 0; lo < n-len; lo += 2*len) {
        int mid = lo + len;
        int hi = Math.min(n, mid + len);
        for (int i = lo; i < hi; i++) tmp[i] = a[i];
        merge(tmp, a, lo, mid, hi);
      }
  }
  
  // natural mergesort
  
  // returns hi such that a[lo..hi[ is sorted
  static int findRun(int[] a, int lo) {
    while (++lo < a.length && a[lo-1] <= a[lo]);
    return lo;
  }
  
  static void naturalMergesort(int[] a) {
    int n = a.length;
    if (n <= 1) return;
    int[] tmp = new int[n];
    while (true) {
      for (int lo = 0; lo < n-1; ) {
        // find first run a[lo..mid[
        int mid = findRun(a, lo);
        if (mid == n) { if (lo == 0) return; break; }
        // find second run a[mid..hi[
        int hi = findRun(a, mid);
        // merge
        for (int i = lo; i < hi; i++) tmp[i] = a[i];
        merge(tmp, a, lo, mid, hi);
        lo = hi;
      }
    }
  }
  
  static void merge2(int[] tmp, int[] a, int lo, int mid, int hi) {
    for (int i = lo; i < hi; i++) tmp[i] = a[i];
    merge(tmp, a, lo, mid, hi);
  }
  
  static void naturalMergeSort(int[] t) {
    if(t.length < 2)
        return;
    
    int b = 0;
    int[] bounds = new int[t.length];
    int count = 0;
    
    while(b < t.length){
        bounds[count] = b;
        count++;
        b = findRun(t, b);    
    }
    
    if(count < 2)
        return;
    
//    for (int i = 0; i < count; i++)
//      System.out.print(bounds[i] + " ");
//    System.out.println();

    int[] tmp = new int[t.length];
    
    for(int len = 1; len < count; len *= 2){
        for(int l = 0; l + len < count; l += 2*len){
            int m = l + len;
            if(m + len < count) {
                merge2(tmp, t, bounds[l], bounds[m], bounds[m+len]);
            } else {
                merge2(tmp, t, bounds[l], bounds[m], t.length);
            }
        }
    }
}

}

class TestMergesort {

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
    System.out.println("  test with       a = " + print(a));
    int[] occ1 = occurrences(a);
    Mergesort.mergesort2(a);
    int[] occ2 = occurrences(a);
    System.out.println("  mergesort(a) => a = " + print(a));
    if (!is_sorted(a)) {
      System.out.println("FAILURE: not sorted");
      System.exit(1);
    }
    if (!is_permut(occ1, occ2)) {
      System.out.println("FAILURE: elements are different");
      System.exit(1);
    }
  }

  public static void main(String[] args) {
    System.out.println("testing mergesort");
    for (int len = 0; len < 10; len++)
      for (int j = 0; j <= len; j++)
        test(random_array(len));
    System.out.println("SUCCESS");
    
    int[] a = new int[50000000];
    //for (int i = 0; i < a.length; i++) a[i] = (5003 * i) % 1000007;
    for (int i = 0; i < a.length; i++) a[i] = -i;
    double start = System.currentTimeMillis();
    Mergesort.naturalMergesort(a);
    System.out.println("BIG");
    System.out.println(System.currentTimeMillis() - start);
  }

}
