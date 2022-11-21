package gps;

// arc

class Edge {
  final Vertex src, dst;
  final String name; // peut être null (pas de nom)
  final boolean directed;

  public Edge(Vertex src, Vertex dst, String name, boolean directed) {
    this.src = src;
    this.dst = dst;
    this.name = name;
    this.directed = directed;
  }

  double length() {
    return src.distance(dst);
  }
  
  public String toString() {
    return (this.name == null ? "" : this.name + " ") + "(" + this.length() + ") " +
        this.src + "->" + this.dst; 
  }

}
