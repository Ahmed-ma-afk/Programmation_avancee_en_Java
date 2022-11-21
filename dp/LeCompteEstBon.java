package dp;

import java.util.HashMap;
import java.util.Map;

public class LeCompteEstBon {

  // calcule tous les entiers que l'on peut construire avec tout sous-ensemble de s
  // un sous-ensemble de S est un masque de 0..|s|-1
  static Map<Integer, Map<Integer, String>> computeAll(int[] s) {
    Map<Integer, Map<Integer, String>> res = new HashMap<>();
    // on commence par les singletons
    for (int i = 0; i < s.length; i++) {
      Map<Integer, String> m = new HashMap<>();
      m.put(s[i], "" + s[i]);
      res.put(1 << i, m);
    }
    // ensuite pour tout sous-ensemble u
    for (int u = 3; u < 1 << s.length; u++) {
      if (res.containsKey(u)) // déjà fait (u est un singleton)
        continue;
      Map<Integer, String> m = new HashMap<>();
      res.put(u, m);
      for (int left = 1; left < u; left++)
        if ((left & u) == left) { // left sous-ensemble de u
          int right = u ^ left;
          m.putAll(res.get(left));
          for (int x: res.get(left).keySet()) {
            String ex = res.get(left).get(x);
            for (int y: res.get(right).keySet()) {
              String ey = res.get(right).get(y);
              m.put(x + y, "("+ex+"+"+ey+")");
              m.put(x * y, "("+ex+"*"+ey+")");
              if (x >= y) m.put(x - y, "("+ex+"-"+ey+")");
              if (y != 0 && x % y == 0) m.put(x / y, "("+ex+"/"+ey+")");
            }
          }	
        }
    }
    return res;
  }

  static String solve(int[] s, int target) {
    Map<Integer, Map<Integer, String>> res = computeAll(s);
    Map<Integer, String> tous = res.get((1 << s.length) - 1);
    for (int d = 0; true; d++) {
      if (tous.containsKey(target-d))
        return (target-d) + " = " + tous.get(target-d);
      if (tous.containsKey(target+d)) 
        return (target+d) + " = " + tous.get(target+d);
    }
  }

  public static void main(String[] args) {
    double start = System.currentTimeMillis();
    int[] s = new int[] {2, 5, 7, 13, 17, 23};
    Map<Integer, Map<Integer, String>> res = computeAll(s);
    System.out.println((System.currentTimeMillis() - start) + " millisecondes");
    Map<Integer, String> tous = res.get((1 << s.length) - 1);
    // System.out.println(tous);
    System.out.println(tous.size() + " entiers au total");
    System.out.println(solve(s, 338));
    for (int x = 1; x < 1500; x++)
      if (!tous.containsKey(x))
        System.out.println(x+": " + solve(s, x));
    System.out.println("---");
    for (int x = 1; x < 3500; x++)
      if (!tous.containsKey(x) && !tous.containsKey(x-1) && !tous.containsKey(x+1))
        System.out.println(x+": " + solve(s, x));
  }

}
