package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

// plus court chemin (algorithme de Dijkstra)

interface Weight<V> {
  int weight(V x, V y);
}

class Node<V> implements Comparable<Node<V>> {

  final V node;
  final int dist;
  
  Node(V node, int dist) {
    this.node = node;
    this.dist = dist;
  }

  @Override
  public int compareTo(Node<V> that) {
    return this.dist - that.dist;
  }
  
}

public class Dijkstra<V> {
  
  private final Graph<V> g;
  private final HashMap<V, Integer> distance;
  
  Dijkstra(Graph<V> g) {
    this.g = g;
    this.distance = new HashMap<V, Integer>();
  }

  void shortestPaths(V source, Weight<V> w) {
    HashSet<V> visited = new HashSet<V>();
    PriorityQueue<Node<V>> pqueue = new PriorityQueue<Node<V>>();
    distance.put(source, 0);
    pqueue.add(new Node<V>(source, 0));
    while (!pqueue.isEmpty()) {
      Node<V> n = pqueue.poll();
      if (visited.contains(n.node)) continue;
      visited.add(n.node);
      for (V v: g.successors(n.node)) {
        int d = n.dist + w.weight(n.node, v);
        if (!distance.containsKey(v) || d < distance.get(v)) {
          distance.put(v, d);
          pqueue.add(new Node<V>(v, d));
        }
      }
    }
  }
  
  Integer distance(V v) {
    return distance.get(v);
  }
  
}

class TestDijkstra {
  
  public static void main(String[] args) {
    Graph<Integer> g = new Graph<Integer>();
    /* 1 -> 2    3
     * |  /^|  / |
     * V /  V V  V
     * 4 <- 5    6)
     */
    for (int i = 1; i <= 6; i++) g.addVertex(i);
    g.addEdge(1, 2); g.addEdge(1, 4); 
    g.addEdge(2, 5);
    g.addEdge(3, 5); g.addEdge(3, 6);
    g.addEdge(4, 2);
    g.addEdge(5, 4);
    g.addEdge(6, 6);
    
    Weight<Integer> w = new Weight<Integer>() {
      public int weight(Integer x, Integer y) {
        if (x == 1 && y == 2) return 10;
        return 1;
      }
    };
    Dijkstra<Integer> dij = new Dijkstra<Integer>(g);
    dij.shortestPaths(1, w);
    
    for (int v : g.vertices())
      System.out.println("dist(" + v + ")=" + ((dij.distance(v) == null) ? "inf" : dij.distance(v)));
    System.out.println();
    System.out.println("TestDijkstra OK");
  }
  
}
