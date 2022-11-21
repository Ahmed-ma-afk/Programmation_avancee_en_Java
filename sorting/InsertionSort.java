package sorting;

// tri par insertion

public class InsertionSort {
  
  static void insertionSort(int[] a) {
    for (int i = 1; i < a.length; i++) {
      int v = a[i], j = i;
      for (; 0 < j && v < a[j-1]; j--)
        a[j] = a[j-1];
      a[j] = v;
    }
  }
  
  // trie a[l..r[
  static void insertionSort(int[] a, int l, int r) {
    for (int i = l+1; i < r; i++) {
      int v = a[i], j = i;
      for (; l < j && v < a[j-1]; j--)
        a[j] = a[j-1];
      a[j] = v;
    }
  }

}

class TestInsertionSort {
  
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
    InsertionSort.insertionSort(a);
    int[] occ2 = occurrences(a);
    System.out.println("  insertionSort(a) => a = " + print(a));
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
     System.out.println("test de insertionSort");
    for (int len = 0; len < 10; len++)
      for (int j = 0; j <= len; j++)
        test(random_array(len));
    System.out.println("SUCCÈS");
  }

}
