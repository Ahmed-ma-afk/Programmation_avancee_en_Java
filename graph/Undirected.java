package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import uf.Elt;

// un graphe non orienté est représenté comme un graphe orienté
// avec l'invariant que x->y si et seulement si y->x

public class Undirected<V> extends Graph<V> {

  @Override
  public void addEdge(V x, V y) {
    super.addEdge(x, y);
    super.addEdge(y, x);
  }

  @Override
  public void removeEdge(V x, V y) {
    super.removeEdge(x, y);
    super.removeEdge(y, x);
  }

  // tous les arcs, une seule fois chacun (i.e. x->y mais pas y->x)
  public Set<Edge<V>> allEdges() {
    Set<Edge<V>> s = new HashSet<>();
    Set<V> seen = new HashSet<>();
    for (V u: this.vertices()) {
      for (V v: this.successors(u)) {
        if (seen.contains(v)) continue;
        s.add(new Edge<>(u, v));
      }
      seen.add(u); // on l'ajoute après, au cas où il y ait des boucles
    }
    return s;
  }
  
  // test d'acyclicité avec union-find
  public boolean isAcyclic() {
    if (this.vertices().isEmpty()) return true;
    // on construit les classes d'équivalence
    Map<V, Elt<V>> elt = new HashMap<>();
    for (V u: this.vertices())
      elt.put(u, new Elt<>(u));
    for (Edge<V> e: this.allEdges()) {
      Elt<V> es = elt.get(e.src), ed = elt.get(e.dst);
      if (es.find() == ed.find()) return false;
      es.union(ed);
    }
    return true;
  }    

  // test de connexité avec union-find
  public boolean isConnected() {
    if (this.vertices().isEmpty()) return true;
    // on construit les classes d'équivalence
    Map<V, Elt<V>> elt = new HashMap<>();
    for (V u: this.vertices())
      elt.put(u, new Elt<>(u));
    for (V u: this.vertices()) {
      Elt<V> eu = elt.get(u);
      for (V v: this.successors(u))
        eu.union(elt.get(v));
    }
    // puis on vérifie que tous les sommets sont dans la même classe
    V v0 = this.vertices().iterator().next();
    Elt<V> c0 = elt.get(v0);
    for (V u: this.vertices())
      if (elt.get(u).find() != c0)
        return false;
    return true;
  }    
}
