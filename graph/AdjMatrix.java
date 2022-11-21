package graph;

// graphes par matrices d'adjacence

public class AdjMatrix {

  private final int n; // les sommets sont 0,...,n-1
  private final boolean[][] m;

  AdjMatrix(int n) {
    this.n = n;
    this.m = new boolean[n][n];
  }
  
  int size() { return this.n; }

  boolean hasEdge(int x, int y) {
    return this.m[x][y];
  }

  void addEdge(int x, int y) {
    this.m[x][y] = true;
    // this.m[y][x] = true; // pour un graphe non orienté
  }

  void removeEdge(int x, int y) {
    this.m[x][y] = false;
    // this.m[y][x] = false; // pour un graphe non orienté
  }

  // Floyd-Warshall
  void transitiveClosure() {
    for (int v = 0; v < this.n; v++)
      for (int p = 0; p < this.n; p++)
        if (this.m[p][v])
          for (int s = 0; s < this.n; s++)
            if (this.m[v][s])
              addEdge(p, s);
  }

}

// graphes étiquetés
class AdjMatrixLabel<L> {

  private final int n; // les sommets sont 0,...,n-1
  private L[][] m;

  @SuppressWarnings("unchecked")
	AdjMatrixLabel(int n) {
    this.n = n;
    this.m = (L[][])new Object[n][n];
  }
  
  int size() { return this.n; }

  boolean hasEdge(int x, int y) {
    return this.m[x][y] != null;
  }

  L getLabel(int x, int y) {
    return this.m[x][y];
  }

  void addEdge(int x, L label, int y) {
    this.m[x][y] = label;
  }

  void removeEdge(int x, int y) {
    this.m[x][y] = null;
  }

}

class TestAdjMatrix {
  public static void main(String[] args) {
    AdjMatrix g = new AdjMatrix(6);
    g.addEdge(1, 3);
    g.addEdge(3, 5);
    assert (g.hasEdge(1, 3));
    g.removeEdge(1, 3);
    assert (!g.hasEdge(1, 3));
    assert (!g.hasEdge(3, 1));
    assert (g.hasEdge(3, 5));
    
    AdjMatrixLabel<String> gl = new AdjMatrixLabel<>(6);
    gl.addEdge(1, "foo", 3);
    gl.addEdge(3, "bar", 5);
    assert (gl.hasEdge(1, 3));
    gl.removeEdge(1, 3);
    assert (!gl.hasEdge(1, 3));
    assert (!gl.hasEdge(3, 1));
    assert (gl.hasEdge(3, 5));
    
    System.out.println("TestAdjMatrix OK");
    
  }
}