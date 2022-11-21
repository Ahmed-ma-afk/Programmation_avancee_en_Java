package arith;

// exponentiation rapide

public class Expo {

  static int exp(int x, int n) {
    if (n == 0) return 1;
    int r = exp(x * x, n / 2);
    return n % 2 == 0 ? r : x * r;
  }
  
  // variante
  static int expAlt(int x, int n) {
    if (n == 0) return 1;
    int r = exp(x, n / 2);
    return n % 2 == 0 ? r * r : x * r * r;
  }
 
  // variante itérative
  static int expIter(int x, int n) {
  	int r = 1;
    while (n != 0) { // invariant : r * x^n = x0 ^ n0
    	if (n % 2 == 1) r *= x;
    	x *= x;
    	n /= 2;
    }
    return r;
  }

  // version générique
  static<M extends Monoid<M>> M exp(M x, int n) {
    if (n == 0) return x.unit();
    M r = exp(x.mul(x), n / 2);
    return (n % 2 == 0) ? r : x.mul(r);
  }

  public static void main(String[] args) {
  	for (int x = 0; x < 10; x++)
    	for (int n = 0; n < 10; n++) {
    		assert exp(x, n) == (int)Math.pow(x, n);
    		assert expIter(x, n) == (int)Math.pow(x, n);
   	}
    System.out.println("Test Expo OK");
  }

}
