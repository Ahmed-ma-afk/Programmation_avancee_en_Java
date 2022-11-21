package uf;

// classes disjointes (union-find)

public class UnionFind {

  private int[] link;
  private int[] rank;
  private int numClasses;
  
  public UnionFind(int n) {
    if (n < 0) throw new IllegalArgumentException();
    this.link = new int[n];
    for (int i = 0; i < n; i++) this.link[i] = i;
    this.rank = new int[n];
    this.numClasses = n;
  }
  
  public int numClasses() {
    return this.numClasses;
  }
  
  public int find(int i) {
    if (i < 0 || i >= this.link.length)
      throw new ArrayIndexOutOfBoundsException(i);
    int p = this.link[i];
    if (p == i) return i;
    int r = this.find(p);
    this.link[i] = r; // compression de chemin
    return r;
  }
  
  public void union(int i, int j) {
    int ri = this.find(i);
    int rj = this.find(j);
    if (ri == rj) return; // déjà dans la même classe
    this.numClasses--;
    if (this.rank[ri] < this.rank[rj])
      this.link[ri] = rj;
    else {
      this.link[rj] = ri;
      if (this.rank[ri] == this.rank[rj])
        this.rank[ri]++;
    }
  }
  
  public boolean sameClass(int i, int j) {
    return this.find(i) == this.find(j);
  }
 
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(this.numClasses);
    for (int i = 0; i < this.link.length; i++)
      sb.append("[" + i + "->" + this.link[i] + "]");
    return sb.toString();
  }
  
}

class TestUnionFind {
  
  public static void main(String[] args) {
    UnionFind uf = new UnionFind(8);
    System.out.println(uf);
    assert (uf.find(3) == 3);
    uf.union(1, 5);
    System.out.println(uf);
    assert (uf.find(1) == uf.find(5));
    assert (uf.find(1) != uf.find(2));
    uf.union(0, 7);
    uf.union(6, 2);
    uf.union(2, 4);
    uf.union(0, 3);
    uf.union(1, 7);
    System.out.println(uf);
    for (int i = 0; i < 8; i++)
      assert (uf.sameClass(i, (i == 0 || i % 2 == 1) ? 0 : 2));
    System.out.println(uf);
    System.out.println("TestUnionFind OK");
  }
  
}
