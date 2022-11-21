package gps;

import java.util.HashMap;

/* sommets
 *   les sommets sont stockés dans une table
 *   et repérés par un entier unique (champ id)
 */

class Vertex {
  final int id;
  final double lon, lat; // en radians

  private Vertex(int id, double lat, double lon) {
    this.id = id;
    this.lon = Math.toRadians(lon);
    this.lat = Math.toRadians(lat);
  }

  // table contenant tous les sommets
  private static final HashMap<Integer, Vertex> nodes =
    new HashMap<Integer, Vertex>(101327);

  static void create(int id, double lat, double lon) {
    nodes.put(id, new Vertex(id, lat, lon));
  }

  static Vertex fromId(int id) {
    return nodes.get(id);
  }

  // distance orthodromique
  double distance(Vertex that) {
    return 6378 * Math.acos(Math.cos(this.lat) * Math.cos(that.lat)
        * Math.cos(this.lon - that.lon) + Math.sin(this.lat)
        * Math.sin(that.lat));
  }

  public String toString() {
    return "lat " + this.lat + ", long " + this.lon;
  }

  // utilisé dans Graph pour construire un point temporaire de coordonnées données
  Vertex(double lat, double lon) {
    this.lat = lat;
    this.lon = lon;
    this.id = -1;
  }

}
