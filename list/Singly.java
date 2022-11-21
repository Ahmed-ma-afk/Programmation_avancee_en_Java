package list;

// listes simplement chaînées

public class Singly {
  public int element;
  public Singly next;
  
  public Singly(int element, Singly next) {
    this.element = element;
    this.next = next;
  }
  
  public static int length(Singly s) {
    int len = 0;
    for ( ; s != null; s = s.next)
      len++;
    return len;
  }
  
  public static boolean contains(Singly s, int x) {
    while (s != null) {
      if (s.element == x) return true;
      s = s.next;
    }
    return false;
  }
  
  public static int get(Singly s, int i) {
    for (; s != null; s = s.next)
      if (i-- == 0) return s.element;
    throw new IllegalArgumentException();
  }
  
  /* algorithme de détection de cycle de Floyd
   * (dit algorithme du lièvre et de la tortue)
   */
  public static boolean hasCycle(Singly s) {
    if (s == null) return false;
    Singly tortoise = s, hare = s.next;
    while (tortoise != hare) {
      if (hare == null) return false;
      hare = hare.next;
      if (hare == null) return false;
      hare = hare.next;
      tortoise = tortoise.next;
    }
    return true;
  }
    
  /* tirage aléatoire dans une liste
   * (en un seul parcours)
   */
  public static int randomElement(Singly s) {
    if (s == null) throw new IllegalArgumentException();
    int candidate = 0, index = 1;
    while (s != null) {
      if ((int)(index * Math.random()) == 0) candidate = s.element;
      index++;
      s = s.next;
    }
    return candidate;
  }
    
  public static String listToString(Singly s) {
    StringBuffer sb = new StringBuffer("[");
    while (s != null) {
      sb.append(s.element);
      if (s.next != null) sb.append(" -> ");
      s = s.next;
    }
    return sb.append("]").toString();
  }
  
  public String toString() {
    return listToString(this);
  }
  
  public static void main(String[] args) {
    Singly x = new Singly(1, new Singly(2, new Singly(3, null)));
    System.out.println(listToString(x));
    System.out.println(x);
    assert (Singly.get(x, 0) == 1);
    assert (Singly.get(x, 1) == 2);
    assert (Singly.get(x, 2) == 3);
    assert Singly.length(x) == 3;
    try { Singly.get(x, 3); assert false; } catch (IllegalArgumentException e) {};
    try { Singly.get(x, -1); assert false; } catch (IllegalArgumentException e) {};
    System.out.println("Singly OK");
  }
}
