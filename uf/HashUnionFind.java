package uf;

import java.util.HashMap;
import java.util.Map.Entry;

public class HashUnionFind<E> {

  private HashMap<E, E> link;
  private HashMap<E, Integer> rank;
  private int numClasses;
  
  public HashUnionFind() {
    this.link = new HashMap<>();
    this.rank = new HashMap<E, Integer>();
    this.numClasses = 0;
  }
  
  public int numClasses() {
    return this.numClasses;
  }
  
  public void add(E x) {
    this.numClasses++;
    this.link.put(x, x);
    this.rank.put(x, 0);
  }
  
  public E find(E x) {
    E p = this.link.get(x);
    if (p == null) throw new IllegalArgumentException();
    if (p == x) return x;
    E r = this.find(p);
    this.link.put(x,  r); // compression de chemin
    return r;
  }
  
  public void union(E i, E j) {
    E ri = this.find(i);
    E rj = this.find(j);
    if (ri == rj) return; // déjà dans la même classe
    this.numClasses--;
    if (this.rank.get(ri) < this.rank.get(rj))
      this.link.put(ri,  rj);
    else {
      this.link.put(rj, ri);
      if (this.rank.get(ri) == this.rank.get(rj))
        this.rank.put(ri, this.rank.get(ri) + 1);
    }
  }
  
  public boolean sameClass(E i, E j) {
    return this.find(i) == this.find(j);
  }
 
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(this.numClasses);
    for (Entry<E, E> e : this.link.entrySet())
      sb.append("[" + e.getKey() + "->" + e.getValue() + "]");
    return sb.toString();
  }
  
}

class TestHashUnionFind {
  
  public static void main(String[] args) {
    HashUnionFind<Integer> uf = new HashUnionFind<>();
    for (int i = 0; i < 8; i++) uf.add(i);
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
    System.out.println("TestHashUnionFind OK");
  }
  
}
