package list;

// listes doublement chaînées

class Doubly {

  int element;
  Doubly next, prev;

  // liste réduite à un élément
  Doubly(int value) {
    this.element = value;
    this.next = this.prev = null;
  }

  // insertion après un élément donné
  void insertAfter(int v) {
    Doubly e = new Doubly(v);
    e.prev = this;
    if (this.next != null) {
      e.next = this.next;
      e.next.prev = e;
    }
    this.next = e;
  }

  // exercice
  void insertBefore(int v) {
    Doubly e = new Doubly(v);
    e.next = this;
    if (this.prev != null) {
      e.prev = this.prev;
      e.prev.next = e;
    }
    this.prev = e;
  }
  
  // suppression d'un élément donné
  void remove() {
    if (this.prev != null)
      this.prev.next = this.next;
    if (this.next != null)
      this.next.prev = this.prev;
  }

  // affichage
  static void println(Doubly c) {
    if (c != null) {
      System.out.print(c.element);
      c = c.next;
      for (; c != null;) {
        System.out.print(" <-> " + c.element);
        c = c.next;
      }
    }
    System.out.print("\n");
  }

}
