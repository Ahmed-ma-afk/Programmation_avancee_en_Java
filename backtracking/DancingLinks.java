package backtracking;

import java.util.LinkedList;

class DLXNode {
  DLXNode left, right, up, down, col;
  String name; // seulement pour les entêtes, null sinon
  int size;
  
  // crée une nouvelle colonne
  public DLXNode(String name) {
    this.left = this;
    this.right = this;
    this.up = this;
    this.down = this;
    this.col = null;
    this.name = name;
    this.size = 0;
  } 
  
  // crée un noeud, en bas de la colonne c
  public DLXNode(DLXNode c) {
    this((String)null); // this(null) serait ambigu
    this.col = c;
    c.size++;
    this.down = c.down;
    c.down = this;
    this.up = c;
    this.down.up = this;
  }
  
  boolean isColumn() { return this.name != null; }
  boolean isNode() { return this.name == null; }
  
  // lie les noeuds de l horizontalement
  static void makeLRlist(LinkedList<DLXNode> l) {
    DLXNode first = l.poll();
    DLXNode prev = first;
    for (DLXNode n: l) {
      prev.right = n;
      n.left = prev;
      prev = n;
    }
    prev.right = first;
    first.left = prev;
  }
  
  static void printLR(DLXNode n) {
    System.err.print(n.name);
    for (DLXNode x = n.right; x != n; x = x.right)
      System.err.print(" " + x.name);
    System.err.println();
  }
  
  static void printSolution(LinkedList<DLXNode> sol) {
    if (sol == null) return;
    for (DLXNode row: sol) {
      System.out.print(row.col.name);
      for (DLXNode x = row.right; x != row; x = x.right)
        System.out.print(" " + x.col.name);
      System.out.println();
    }
  }
  
  // supprime et réinsère horizontalement
  void removeLR() {
    this.left.right = this.right;
    this.right.left = this.left;
  }
  void restoreLR() {
    this.right.left = this;
    this.left.right = this;
  }
  // supprime et réinsère verticalement
  void removeUD() {
    this.up.down = this.down;
    this.down.up = this.up;
  }
  void restoreUD() {
    this.down.up = this;
    this.up.down = this;
  }
  
}

public class DancingLinks {
  DLXNode header;
  
  DancingLinks(LinkedList<DLXNode> columns) {
    this.header = new DLXNode("header");
    columns.add(this.header);
    DLXNode.makeLRlist(columns);
    DLXNode.printLR(header);
  }
  
  void addRow(LinkedList<DLXNode> row) {
    LinkedList<DLXNode> nodes = new LinkedList<>();
    for (DLXNode c: row) {
      assert c.isColumn();
      nodes.add(new DLXNode(c));
    }
    DLXNode.makeLRlist(nodes);
  }
  
  void cover(DLXNode c) {
    assert c.isColumn();
    c.removeLR();
    for (DLXNode x = c.down; x != c; x = x.down)
      for (DLXNode y = x.right; y != x; y = y.right) {
        y.removeUD();
        y.col.size--;
    }
  }
  
  void uncover(DLXNode c) {
    assert c.isColumn();
    for (DLXNode x = c.up; x != c; x = x.up)
      for (DLXNode y = x.left; y != x; y = y.left) {
        y.col.size++;
        y.restoreUD();
      }
    c.restoreLR();
  }
  
  // la colonne minimisant size
  DLXNode minColumn() {
    DLXNode best = header.right;
    for (DLXNode c = header.right.right; c != header; c = c.right)
      if (c.size < best.size)
        best = c;
    //System.err.println("best = " + best.name);
    return best;
  }
  
  // trouver une solution
  LinkedList<DLXNode> sol;
  
  boolean solveRec() {
    if (header.right == header) return true;
    DLXNode best = minColumn();
    cover(best);
    for (DLXNode row = best.down; row != best; row = row.down) {
      sol.addLast(row);
      for (DLXNode x = row.right; x != row; x = x.right)
        cover(x.col);
      if (solveRec()) return true;
      for (DLXNode x = row.left; x != row; x = x.left)
        uncover(x.col);
      sol.removeLast();
    }
    uncover(best);
    return false;
  }
  
  LinkedList<DLXNode> solve() {
    sol = new LinkedList<>();
    return solveRec() ? sol : null;
  }
  
  // compter les solutions
  
  int count = 0;
  
  void countRec() {
    if (header.right == header) {
      count++;
      return;
    }
    DLXNode best = minColumn();
    cover(best);
    for (DLXNode row = best.down; row != best; row = row.down) {
      for (DLXNode x = row.right; x != row; x = x.right)
        cover(x.col);
      countRec();
      for (DLXNode x = row.left; x != row; x = x.left)
        uncover(x.col);
    }
    uncover(best);
  }
  
  int count() {
    count = 0;
    countRec();
    return count;
  }
  
}
