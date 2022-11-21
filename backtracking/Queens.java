package backtracking;

// le problème des N reines

public class Queens {

  static int count = 0;
  
  // la ligne r est-elle cohérente avec les précédentes ?
  static boolean check(int[] cols, int r) {
    for (int q = 0; q < r; q++)
      if (cols[q] == cols[r] || Math.abs(cols[q] - cols[r]) == r - q)
        return false;
    return true;
  }

  static boolean findSolutionRec(int[] cols, int r) {
    if (r == cols.length)
      return true;
    for (int c = 0; c < cols.length; c++) {
      cols[r] = c;
      count++;
      if (check(cols, r) && findSolutionRec(cols, r + 1))
        return true;
    }
    return false;
  }

  static int[] findSolution(int n) {
    int[] cols = new int[n];
    if (findSolutionRec(cols, 0))
      return cols;
    throw new Error("no solution for n=" + n);
  }

  static int naiveCountRec(int[] cols, int r) {
    if (r == cols.length)
      return 1;
    int f = 0;
    for (int c = 0; c < cols.length; c++) {
      cols[r] = c;
      count++;
      if (check(cols, r))
        f += naiveCountRec(cols, r + 1);
    }
    return f;
  }

  static int naiveCount(int n) {
    int[] cols = new int[n];
    return naiveCountRec(cols, 0);
  }

  static int countSolutionsRec(int a, int b, int c) {
    if (a == 0) return 1;
    int e = a & ~b & ~c, f = 0;
    while (e != 0) {
      int d = e & -e;
      count++;
      f += countSolutionsRec(a - d, (b + d) << 1, (c + d) >> 1);
      e -= d;
    }
    return f;
  }
  
  static int countSolutions(int n) {
    return countSolutionsRec(~(~0 << n), 0, 0);
  }

  public static void main(String[] args) {
    for (int n = 1; n <= 14; n++) {
      System.out.print("n = " + n + " : ");
      try {
        double start = System.currentTimeMillis();
        findSolution(n);
        double time = System.currentTimeMillis() - start;
        System.out.println("solution found " + time);

        start = System.currentTimeMillis();
        count = 0;
        int fn = naiveCount(n);
        double time1 = System.currentTimeMillis() - start;
        System.out.println("  f("+n+")="+ fn + " " + count + " (" + time1 + "ms)");
        
        start = System.currentTimeMillis();
        count = 0;
        int on = countSolutions(n);
        double time2 = System.currentTimeMillis() - start;
        System.out.println("  f("+n+")="+ on + " " + count + " (" + time2 + "ms " + (time1 / time2) + ")");
        
     } catch (Error e) {
        System.out.println("no solution");
      }
    }
  }
}
