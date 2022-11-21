  package array;
  
  public class BinarySearch {
  
    static boolean binarySearch(int[] a, int v) {
      int lo = 0, hi = a.length; // on cherche dans [lo..hi[
      while (lo < hi) {
        int m = lo + (hi - lo) / 2;
        if (a[m] == v)
          return true;
        if (a[m] < v)
          lo = m + 1;
        else
          hi = m;
      }
      return false;
    }
    
    // une version générique
    // renvoie la position où l'élément se trouve / devrait être inséré
    
    static<E extends Comparable<E>> int binarySearch(E[] a, E v) {
      return binarySearch(a, v, 0, a.length);
    }
    
    // le code ci-dessous cherche dans l'intervalle ouvert [lo..hi[
    
    static<E extends Comparable<E>> int binarySearch(E[] a, E v, int lo, int hi) {
      while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        int c = a[mid].compareTo(v);
        if (c == 0)
          return mid;
        if (c < 0)
          lo = mid + 1;
        else
          hi = mid;
      }
      return lo;
    }
  
  }
  
  class TestBinarySearch {
    public static void main(String[] args) {
    	int[] a = {1, 2, 3, 5, 10};
    	assert !BinarySearch.binarySearch(a, 0);
    	assert BinarySearch.binarySearch(a, 1);
    	assert BinarySearch.binarySearch(a, 3);
    	assert !BinarySearch.binarySearch(a, 4);
    	assert BinarySearch.binarySearch(a, 10);
    	assert !BinarySearch.binarySearch(a, 11);
    	Integer[] b = {1, 2, 3, 5, 10};
    	assert BinarySearch.binarySearch(b, 0) == 0;
    	assert BinarySearch.binarySearch(b, 1) == 0;
    	assert BinarySearch.binarySearch(b, 3) == 2;
    	assert BinarySearch.binarySearch(b, 4) == 3;
    	assert BinarySearch.binarySearch(b, 10) == 4;
    	assert BinarySearch.binarySearch(b, 11) == 5;
      // System.out.println(BinarySearch.binarySearch(new Integer[] { 1,1,3 } , 1));
    }
  }
