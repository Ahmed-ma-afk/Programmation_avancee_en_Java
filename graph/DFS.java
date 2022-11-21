package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

// parcours en profondeur

public class DFS<V> {

  private final Graph<V> g;
  private final HashMap<V, Integer> visited;
  private int count;
  
  public DFS(Graph<V> g) {
    this.g = g;
    this.visited = new HashMap<V, Integer>();
    this.count = 0;
  }
  
  public void dfs(V v) {
    if (this.visited.containsKey(v)) return;
    this.visited.put(v, this.count++);
    for (V w : this.g.successors(v))
      dfs(w);
  }
  
  public void dfs() {
    for (V v : this.g.vertices())
      dfs(v);
  }
  
  public int getNum(V v) {
    return this.visited.get(v);
  }

}

class DFSset<V> {

  private final Graph<V> g;
  private final HashSet<V> visited;
  
  DFSset(Graph<V> g) {
    this.g = g;
    this.visited = new HashSet<V>();
  }
  
  void dfs(V v) {
    if (this.visited.contains(v)) return;
    this.visited.add(v);
    for (V w : this.g.successors(v))
      dfs(w);
  }
  
  void dfs() {
    for (V v : this.g.vertices())
      dfs(v);
  }
  
  boolean existsPath(V u, V v) {
    this.visited.clear();
    dfs(u);
    return this.visited.contains(v);
  }
  
  // test de connexité (pour un graphe non orienté)
  boolean isConnected() {
    Set<V> vs = this.g.vertices();
    if (vs.isEmpty()) return true;
    V v0 = vs.iterator().next(); // un sommet quelconque
    this.visited.clear();
    dfs(v0);
    for (V v: vs)
      if (!this.visited.contains(v))
        return false;
    return true;
  }
  
}

// avec une pile explicite, pour éviter StackOverflow
class DFSStack<V> {

  private final Graph<V> g;
  private final HashMap<V, Integer> visited;
  private int count;
  
  DFSStack(Graph<V> g) {
    this.g = g;
    this.visited = new HashMap<V, Integer>();
    this.count = 0;
  }
  
  void dfs(V v) {
    Stack<V> stack = new Stack<V>();
    stack.push(v);
    while (!stack.isEmpty()) {
      v = stack.pop();
      if (this.visited.containsKey(v)) continue;
      this.visited.put(v, this.count++);
      for (V w : this.g.successors(v))
        stack.push(w);
    }    
  }
  
  void dfs() {
    for (V v : this.g.vertices())
      dfs(v);
  }
  
  int getNum(V v) {
    return this.visited.get(v);
  }

}

class CycleDetection<V> {

  enum Color { White, Gray, Black};
  
  private final Graph<V> g;
  private final HashMap<V, Color> color;
  
  public CycleDetection(Graph<V> g) {
    this.g = g;
    this.color = new HashMap<V, Color>();
  }
  
  boolean dfs(V v) {
    if (this.color.get(v) == Color.Gray) return true;
    if (this.color.get(v) == Color.Black) return false;
    this.color.put(v, Color.Gray);
    for (V w : this.g.successors(v))
      if (dfs(w)) return true;
    this.color.put(v, Color.Black);
    return false;
  }
  
  boolean hasCycle() {
    for (V v : this.g.vertices())
      color.put(v, Color.White);
    for (V v : this.g.vertices())
      if (dfs(v)) return true;
    return false;
  }

}

class TestDFS {
  
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
    
    DFS<Integer> dfs = new DFS<Integer>(g);
    dfs.dfs();
    for (int v : g.vertices())
      System.out.println("dfs(" + v + ")=" + dfs.getNum(v));
    //System.out.println(dfs.getNum(7));
    
    System.out.println("---");
    
    DFSStack<Integer> dfss = new DFSStack<Integer>(g);
    dfss.dfs();
    for (int v : g.vertices())
      System.out.println("dfs(" + v + ")=" + dfss.getNum(v));
    
    System.out.println("---");
    CycleDetection<Integer> cd = new CycleDetection<Integer>(g);
    System.out.println(cd.hasCycle());
    
    System.out.println("Test DFS");
  }
  
}