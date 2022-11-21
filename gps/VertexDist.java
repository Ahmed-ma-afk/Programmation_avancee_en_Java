package gps;

// paire (sommet, distance) pour Dijkstra
class VertexDist implements Comparable<VertexDist> {
  final Vertex v;
  final double d;

  VertexDist(Vertex v, double d) {
    this.v = v;
    this.d = d;
  }

  @Override
  public int compareTo(VertexDist that) {
    double c = this.d - that.d;
    return c < 0 ? -1 : c > 0 ? 1 : 0;
  }
}
