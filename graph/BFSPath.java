package graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BFSPath<V> {
  
  private Graph<V> g;
  private HashMap<V, Integer> visited;
  private HashMap<V, V> pred;

  public BFSPath(Graph<V> g) {
    this.g = g;
    this.visited = new HashMap<V, Integer>();
    this.pred = new HashMap<V, V>();
  }
  
  public void bfs(V source) {
    Queue<V> q = new LinkedList<V>();
    q.add(source);
    visited.put(source, 0);
    pred.put(source, null);
    while (!q.isEmpty()) {
      V v = q.poll();
      int d = visited.get(v);
      for (V w : this.g.successors(v))
        if (!this.visited.containsKey(w)) {
          q.add(w);
          this.visited.put(w, d+1);
          this.pred.put(w, v);
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

  public boolean isVisited(V v) {
    return this.visited.containsKey(v);
  }
  
  public V getPred(V v) {
    return this.pred.get(v);
  }

  public List<V> getPath(V v) {
    LinkedList<V> path = new LinkedList<V>();
    while (v != null) {
      path.addFirst(v);
      v = getPred(v);
    }
    return path;
  }
  
}
