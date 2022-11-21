package gps;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/* le graphe est représenté par des listes d'adjacence
 * 
 */

class Graph {
  private static final HashMap<Vertex, List<Edge>> adj =
      new HashMap<Vertex, List<Edge>>();
  private static int edges = 0;

  static void addEdge(Edge e) {
    List<Edge> l = adj.get(e.src);
    if (l == null)
      adj.put(e.src, l = new LinkedList<Edge>());
    l.add(e);
    edges++;
  }

  static Iterable<Edge> successors(Vertex p) {
    List<Edge> l = adj.get(p);
    if (l == null)
      return new LinkedList<Edge>();
    return l;
  }

  static Iterable<Vertex> vertices() {
    return adj.keySet();
  }

  static Iterable<List<Edge>> edges() {
    return adj.values();
  }

  // renvoie le sommet le plus proche du point de coordonnées (lat,lon)
  static Vertex closest(double lat, double lon) {
    Vertex v = new Vertex(lat, lon), best = null;
    double bestDist = Double.MAX_VALUE;
    for (Vertex w : adj.keySet()) {
      double dist = v.distance(w);
      if (dist < bestDist) {
        bestDist = dist;
        best = w;
      }
    }
    System.out.println("distance = " + bestDist);
    return best;
  }

  static void stat() {
    System.out.println(adj.size() + " nodes");
    System.out.println(edges + " edges");
  }

}
