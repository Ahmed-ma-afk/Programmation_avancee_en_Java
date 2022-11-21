package graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

// parcours en largeur

public class BFS<V> {

  private Graph<V> g;
  private HashMap<V, Integer> visited;

  public BFS(Graph<V> g) {
    this.g = g;
    this.visited = new HashMap<V, Integer>();
  }
  
  public void bfs(V source) {
    Queue<V> q = new LinkedList<V>();
    q.add(source);
    visited.put(source, 0);
    while (!q.isEmpty()) {
      V v = q.poll();
      int d = visited.get(v);
      for (V w : this.g.successors(v))
        if (!this.visited.containsKey(w)) {
          q.add(w);
          this.visited.put(w, d+1);
        }
    }
  }

  public void bfs() {
    for (V v : this.g.vertices())
      if (!this.visited.containsKey(v))
          bfs(v);
  }

  public int getDistance(V v) {
    Integer d = this.visited.get(v);
    return (d == null) ? -1 : d;
  }
  
}

class TestBFS {
  
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
    
    // g.makeUndirected();
    
    BFS<Integer> dfs = new BFS<Integer>(g);
    dfs.bfs(1);
    
    for (int v : g.vertices())
      System.out.println("dist(" + v + ")=" + dfs.getDistance(v));

    System.out.println("Test BFS");
  }
  
}