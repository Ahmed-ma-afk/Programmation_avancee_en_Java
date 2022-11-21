package gps;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

// trois algorithmes : DFS, BFS, Dijkstra

public class FindPath {

  public static Iterable<Edge> runDijkstra(Vertex source, Vertex target) {
    HashSet<Vertex> visited = new HashSet<Vertex>();
    HashMap<Vertex, Edge> path = new HashMap<Vertex, Edge>();
    path.put(source, null);
    HashMap<Vertex, Double> distance = new HashMap<Vertex, Double>();
    distance.put(source, 0.);
    PriorityQueue<VertexDist> pq = new PriorityQueue<VertexDist>();
    pq.add(new VertexDist(source, 0.));
    while (!pq.isEmpty()) {
      VertexDist n = pq.poll();
      if (visited.contains(n.v))
        continue;
      visited.add(n.v);
      for (Edge e : Graph.successors(n.v)) {
        double d = n.d + e.length();
        if (!distance.containsKey(e.dst) || d < distance.get(e.dst)) {
          distance.put(e.dst, d);
          pq.add(new VertexDist(e.dst, d));
          path.put(e.dst, e);
        }
      }
    }
    return buildPath(source, target, path);
  }

  public static Iterable<Edge> runBFS(Vertex source, Vertex target) {
    Queue<Vertex> queue = new LinkedList<Vertex>();
    HashMap<Vertex, Edge> visited = new HashMap<Vertex, Edge>();
    queue.add(source);
    visited.put(source, null);
    while (!queue.isEmpty()) {
      Vertex v = queue.poll();
      if (v.equals(target))
        break;
      for (Edge e : Graph.successors(v))
        if (!visited.containsKey(e.dst)) {
          visited.put(e.dst, e);
          queue.add(e.dst);
        }
    }
    return buildPath(source, target, visited);
  }

  public static Iterable<Edge> runDFS(Vertex source, Vertex target) {
    Stack<Vertex> stack = new Stack<Vertex>();
    HashMap<Vertex, Edge> visited = new HashMap<Vertex, Edge>();
    visited.put(source, null);
    stack.add(source);
    while (!stack.isEmpty()) {
      Vertex v = stack.pop();
      if (v.equals(target))
        break;
      for (Edge e : Graph.successors(v))
        if (!visited.containsKey(e.dst)) {
          visited.put(e.dst, e);
          stack.add(e.dst);
        }
    }
    return buildPath(source, target, visited);
  }

  static Iterable<Edge> buildPath(Vertex source, Vertex target,
      HashMap<Vertex, Edge> visited) {
    LinkedList<Edge> p = new LinkedList<Edge>();
    while (visited.containsKey(target) && !target.equals(source)) {
      Edge e = visited.get(target);
      p.addFirst(e);
      target = e.src;
    }
    return p;
  }

  static void printPath(Iterable<Edge> path) {
    if (path == null)
      return;
    double dist = 0.;
    int size = 0;
    for (Edge e : path) {
      dist += e.length();
      size++;
      // System.out.println(e);
      if (e.name != null)
        System.out.printf("  %s (%.2f km)\n", e.name, e.length());
    }
    System.out.println(size + " arcs");
    System.out.printf("distance totale = %.2f km\n", dist);
  }


}
