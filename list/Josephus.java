package list;

// le problème de Josephus

class Josephus {

  /* construction de la liste circulaire 1,2,...,n; l'élément renvoyé est celui
   * contenant 1
   */
  static Doubly circle(int n) {
    Doubly l1 = new Doubly(1);
    for (int i = n; i >= 2; i--) {
      l1.insertAfter(i);
      if (i == n) { l1.prev = l1.next; l1.next.next = l1; }
    }
    return l1;
  }

  // n joueurs, on compte de p en p
  static int josephus(int n, int p) {
    Doubly c = circle(n);
    while (c != c.next) { // tant qu'il reste au moins deux joueurs
      for (int i = 1; i < p; i++)
        c = c.next;
      c.remove(); // on élimine le p-ième
      System.out.println(c.element + " est éliminé");
      c = c.next;
    }
    System.out.println("le gagnant est " + c.element);
    return c.element;
  }

  public static void main(String args[]) {
    assert (josephus(7, 5) == 6);
    assert (josephus(5, 5) == 2);
    assert (josephus(5, 17) == 4);
    assert (josephus(13, 2) == 11);
    System.out.println("Josephus OK");
  }

}

// exercice
class JosephusSingly {

  /* construction de la liste circulaire 1,2,...,n;
   * l'élément renvoyé est celui contenant n
   */
  static Singly circle(int n) {
    Singly last = new Singly(n, null);
    Singly first = last;
    for (int i = n - 1; i >= 1; i--)
      first = new Singly(i, first);
    last.next = first;
    return last;
  }

  static int josephus(int n, int p) {
    Singly pred = circle(n);
    Singly c = pred.next;
    while (c != pred) { // tant qu'il reste au moins deux joueurs
      for (int i = 1; i < p; i++) {
      	pred = c;
        c = c.next;
      }
      System.out.println(c.element + " est éliminé");
      pred.next = c.next;
      c = c.next;
    }
    System.out.println("le gagnant est " + c.element);
    return c.element;
	}
	
  public static void main(String args[]) {
    assert (josephus(7, 5) == 6);
    assert (josephus(5, 5) == 2);
    assert (josephus(5, 17) == 4);
    assert (josephus(13, 2) == 11);
    System.out.println("JosephusSingly OK");
  }

}

//exercice
class JosephusArray {

	// attention : on utilise ici les indices 0,1,...,n-1
	// pour désigner les joueurs 1,2,...,n
	static int josephus(int n, int p) {
		int[] next = new int[n];
		for (int i = 0; i < n; i++)
			next[i] = (i + 1) % n;
		int pred = n - 1, c = 0;
		while (c != pred) { // tant qu'il reste au moins deux joueurs
			for (int i = 1; i < p; i++) {
				pred = c;
				c = next[c];
			}
			System.out.println((c+1) + " est éliminé");
			next[pred] = next[c];
			c = next[c];
		}
		System.out.println("le gagnant est " + (c+1));
		return c+1; // on renvoie le joueur, pas l'indice
	}

	public static void main(String args[]) {
		assert (josephus(7, 5) == 6);
		assert (josephus(5, 5) == 2);
		assert (josephus(5, 17) == 4);
		assert (josephus(13, 2) == 11);
		System.out.println("JosephusSingly OK");
	}

}
