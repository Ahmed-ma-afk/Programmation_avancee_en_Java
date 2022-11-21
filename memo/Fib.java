package memo;

import java.util.HashMap;

// suite de Fibonacci

public class Fib {

  static long fib(int n) {
    if (n <= 1)
      return n;
    return fib(n - 2) + fib(n - 1);
  }

  // mémoïsation
  static HashMap<Integer, Long> memo = new HashMap<Integer, Long>();
  // static { memo.put(0, 0L); memo.put(1, 1L); }

  static long fibMemo(int n) {
    //if (n <= 1) return n;
    Long l = memo.get(n);
    if (l != null) return l;
    if (n <= 1) l = (long)n; else l = fibMemo(n - 2) + fibMemo(n - 1);
    memo.put(n, l);
    return l;
  }

  // programmation dynamique
  static long fibDP(int n) {
    long[] f = new long[n + 1];
    f[1] = 1;
    for (int i = 2; i <= n; i++)
      f[i] = f[i - 2] + f[i - 1];
    return f[n];
  }

 // et avec deux entiers seulement
  static long fibDP2(int n) {
    int a = 0, b = 1; // a = F(k), b = F(k+1)
    while (n-- > 0) {
      b = b + a;
      a = b - a;
    }
    return a;
  }

  public static void main(String[] args) {
  	assert fibDP(10) == 55;
  	assert fibDP2(10) == 55;
    long start = System.currentTimeMillis();
    System.out.println(fibMemo(80));
    System.out.println((System.currentTimeMillis() - start) + " ms");
    // 42 2s
    // 43 3s
    // 44 5s
    // 45 8s
    // 46 13s
    // 50 = F11s = 89s
  }

}
