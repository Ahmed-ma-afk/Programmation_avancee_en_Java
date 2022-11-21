package graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class DFSPath<V> {

  private final Graph<V> g;
  private final HashMap<V, Integer> visited;
  private final HashMap<V, V> pred;
  private int count;
  
  public DFSPath(Graph<V> g) {
    this.g = g;
    this.visited = new HashMap<V, Integer>();
    this.pred = new HashMap<V, V>();
    this.count = 0;
  }
  
  public void dfs(V from, V v) {
    if (this.visited.containsKey(v)) return;
    this.visited.put(v, this.count++);
    this.pred.put(v, from);
    for (V w : this.g.successors(v))
      dfs(v, w);
  }
  
  public void dfs(V v) {
    dfs(null, v);
  }

  // avec une pile explicite
  public void dfsStack(V v) {
    Stack<V> stack = new Stack<V>();
    stack.push(v);
    this.pred.put(v, null);
    while (!stack.isEmpty()) {
      v = stack.pop();
      if (this.visited.containsKey(v)) continue;
      this.visited.put(v, this.count++);
      for (V w : this.g.successors(v))
        if (!this.visited.containsKey(w)) {
          this.pred.put(w, v);
          stack.push(w);
        }
    }    

  }
  
  public void dfs() {
    for (V v : this.g.vertices())
      dfs(v);
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


class TestDFSPath {
  
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
    
    DFSPath<Integer> dfs = new DFSPath<Integer>(g);
    dfs.dfs(3);
    System.out.println(dfs.getPath(2));

    System.out.println("Test DFSPath");
  }
  
}
