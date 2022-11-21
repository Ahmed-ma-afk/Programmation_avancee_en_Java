package graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

import uf.UnionFind;

class Kedge implements Comparable<Kedge> {
  final int src, dst;
  final double weight;
  
  Kedge(int src, int dst, double weight) {
    this.src = src;
    this.dst = dst;
    this.weight = weight;
  }
  
  @Override
  public String toString() {
    return src+"--"+dst+"("+weight+")";
  }

  @Override
  public int compareTo(Kedge that) {
    return this.weight < that.weight ? - 1 :
           this.weight > that.weight ? + 1 : 0;
  }
  
}

class Kgraph extends Graph<Integer> {
  final int V; // les sommets sont 0,1,...,V-1
  private LinkedList<Kedge> allEdges;
  
  Kgraph(int V) {
    this.V = V;
    this.allEdges = new LinkedList<>();
  }
  
  void addEdge(int src, int dst, double w) {
    assert 0 <= src && src < this.V;
    assert 0 <= dst && dst < this.V;
    this.addEdge(src, dst);
    this.addEdge(dst, src);
    this.allEdges.add(new Kedge(dst, src, w));
  }
  
  List<Kedge> allEdges() {
    return this.allEdges;
  }
  
}

public class Kruskal {

  static List<Kedge> kruskal(Kgraph g) {
    List<Kedge> mst = new LinkedList<>();
    UnionFind uf = new UnionFind(g.V);
    PriorityQueue<Kedge> q = new PriorityQueue<>();
    q.addAll(g.allEdges());
    while (mst.size() < g.V - 1) {
      Kedge e = q.remove();
      if (uf.sameClass(e.src, e.dst)) { System.out.println("on ignore " + e); continue; }
      System.out.println("on prend " + e);
      uf.union(e.src, e.dst);
      mst.add(e);
    }
    return mst;
  }
  
  // on suppose ici g connexe
  static boolean isSpanningTree(Kgraph g, List<Kedge> t) {
    if (t.size() != g.V - 1) return false;
    UnionFind uf = new UnionFind(g.V);
    for (Kedge e: t) {
      if (uf.find(e.src) == uf.find(e.dst)) return false;
      uf.union(e.src, e.dst);
    }
    return true;
  }
  
  public static void main(String[] args) throws FileNotFoundException {
    Scanner sc = new Scanner(new File("examEWG.txt"));
    int V = sc.nextInt();
    Kgraph g = new Kgraph(V);
    int E = sc.nextInt();
    while (E-- > 0)
      g.addEdge(sc.nextInt(), sc.nextInt(), sc.nextDouble());
    sc.close();
    List<Kedge> mst = kruskal(g);
    assert isSpanningTree(g, mst);
    System.out.println(mst);
    double w = 0.0;
    for (Kedge e: mst) w += e.weight;
    System.out.println(w);
  }
  
  
}
