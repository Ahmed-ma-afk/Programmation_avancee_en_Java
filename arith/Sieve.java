package arith;

import java.util.Arrays;
import java.util.BitSet;

// crible d'Ératosthène

public class Sieve {

  // crible
  static boolean[] sieve(int max) {
    boolean[] prime = new boolean[max + 1];
    for (int i = 2; i <= max; i++)
      prime[i] = true;
    for (int n = 2; n * n <= max; n++)
      if (prime[n])
        for (int m = n * n; m <= max; m += n)
          prime[m] = false;
    return prime;
  }

  // optim
  static boolean[] sieveOpt(int max) {
    boolean[] prime = new boolean[max + 1];
    prime[2] = true;
    for (int i = 3; i <= max; i += 2)
      prime[i] = true;
    for (int n = 3; n * n <= max; n += 2)
      if (prime[n])
        for (int m = n * n; m <= max; m += n)
          prime[m] = false;
    return prime;
  }

  static BitSet sieveBitSet(int max) {
    BitSet prime = new BitSet(max + 1);
    for (int i = 2; i <= max; i++)
      prime.set(i);
    for (int n = 2; n * n <= max; n++)
      if (prime.get(n))
        for (int m = n * n; m <= max; m += n)
          prime.clear(m);
    return prime;
  }
  
  static int[] firstPrimesUpto(int max) {
  	boolean[] prime = sieve(max);
  	int count = 0;
  	for (boolean b: prime) if (b) count++;
  	int[] res = new int[count];
  	for (int i = 2, next = 0; i <= max; i++)
  		if (prime[i])
  			res[next++] = i;
  	return res;
  }
  
  static int[] firstPrimesUptoOpt(int max) {
    boolean[] prime = new boolean[max + 1];
    for (int i = 2; i <= max; i++)
      prime[i] = true;
    int count = 0;
    for (int n = 2; n <= max; n++)
      if (prime[n]) {
        count++;
        for (int m = n * n; m <= max; m += n)
          prime[m] = false;
      }
    int[] res = new int[count];
    for (int i = 0, j = 0; i <= max; i++)
      if (prime[i]) res[j++] = i;
    return res;
  }

  static int[] firstNPrimes(int n) {
  	int m = Math.max(6, n);
  	int max = (int)(m * (Math.log(m) + Math.log(Math.log(m))));
  	boolean[] prime = sieve(max);
  	int[] res = new int[n];
  	for (int i = 2, next = 0; next < n; i++)
  		if (prime[i])
  			res[next++] = i;
  	return res;
  }

 public static void main(String[] args) {
   System.out.println(Math.sqrt(20));

   boolean prime[] = sieve(100);
    int count = 0;
    for (int i = 0; i < 100; i++)
      if (prime[i]) {
        System.out.print(i + ",");
        count++;
      }
    System.out.println();
    System.out.println(count + " primes");
    assert count == 25;

    BitSet primebs = sieveBitSet(100);
    count = 0;
    for (int i = 0; i < 100; i++)
      if (primebs.get(i)) {
        System.out.print(i + ",");
        count++;
      }
    System.out.println();
    System.out.println(count + " primes");
    assert count == 25;

    int[] primes = firstPrimesUpto(100);
    for (int p : primes)
      System.out.print(p + ",");
    System.out.println();
    System.out.println(primes.length + " primes");
    assert count == 25;

    int[] primes2 = firstNPrimes(25);
    assert Arrays.equals(primes, primes2);
    
    System.out.println("Sieve OK");
  }

}
