package arith;

// algorithme d'Euclide

public class Euclid {

  static int gcd(int u, int v) {
  	if (v < 0) v = -v;
  	if (u < 0) u = -u;
    while (v != 0) { // invariant gcd(u,v) = gcd(u0,v0)
      int tmp = v;
      v = u % v;
      u = tmp;
    }
    return u;
  }
  
  // renvoie {a,b,g} tels quel au+bv = g = gcd(u,v)
  static int[] extendedGcdPoly(int u0, int v0) {
    int[] u = { 1, 0, u0 }, v = { 0, 1, v0 };
    while (v[2] != 0) {
      int q = u[2] / v[2];
      int[] t = { u[0] - q * v[0], u[1] - q * v[1], u[2] - q * v[2] };
      u = v;
      v = t;
    }
    return u;
  }

  static int[] extendedGcd(int u, int v) {
    int u0 = 1, u1 = 0, u2 = u, v0 = 0, v1 = 1, v2 = v;
    while (v2 != 0) {
      int q = u2 / v2;
      int t0 = u0 - q * v0, t1 = u1 - q * v1, t2 = u2 - q * v2;
      u0 = v0; u1 = v1; u2 = v2;
      v0 = t0; v1 = t1; v2 = t2;
    }
    return new int[] { u0, u1, u2 };
  }

  // division de u par v modulo m, en supposant gcd(v,m) = 1
  static int divMod(int u, int v, int m) {
  	int[] g = extendedGcd(v, m);
  	assert g[2] == 1;
  	// g[0] * v + g[1] * m = g[2] = gcd(v,m) = 1
  	int r = (g[0] * u) % m;
  	return r < 0 ? r + m : r;
  }
  
  static void check(int u, int v, int g) {
    assert (u % g == 0);
    assert (v % g == 0);
    for (int d = 1; d <= Math.min(u,  v); d++)
      if (u % d == 0 && v % d == 0)
        assert (g % d == 0);
  }
  
  public static void main(String[] args) {
    assert (gcd(0, 0) == 0);
    assert (gcd(2, 1) == 1);
    assert (gcd(2, 2) == 2);
    assert (gcd(2, 3) == 1);
    assert (gcd(2, 4) == 2);
    assert (gcd(10, 25) == 5);
    assert (gcd(-10, 25) == 5);
    assert (gcd(-10, -25) == 5);
    assert (gcd(10, -25) == 5);
    
    for (int u = 1; u <= 100; u++)
      for (int v = 1; v <= 100; v++) {
        check(u, v, gcd(u, v));
        int[] e = extendedGcd(u,  v);
        assert (e[0] * u + e[1] * v == e[2]);
        check(u, v, e[2]);
      }
    
    int m = 19;
    for (int u = 0; u < m; u++)
    	for (int v = 1; v < m; v++) {
    		int d = divMod(u, v, m);
    		assert 0 <= d && d < m;
    		assert u % m == (v * d) % m;
    	}
    
    System.out.println("Test Euclid OK");
    
//    for (int u = -10; u <= 10; u++)
//      for (int v = -10; v <= 10; v++) {
//        if (v == 0) continue;
//        System.out.println(u + " % " + v + " = " + u % v);
//      }
  }
  
}
