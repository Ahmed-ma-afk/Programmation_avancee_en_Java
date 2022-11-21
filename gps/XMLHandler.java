package gps;

import java.util.LinkedList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* lecture du fichier XML (avec SAX)
 *   le principe est de donner du code qui est appelé
 *   quand une balise est ouverte (startElement)
 *   et quand elle est fermée (endElement)
 */

class XMLHandler extends DefaultHandler {

  private String wayName = null;
  private boolean oneway = false;
  private LinkedList<Vertex> way = null;

  @Override
  public void startElement(String uri, String localName, String qName,
      Attributes attributes) throws SAXException {
    if (qName.equals("node")) {
      int id = Integer.parseInt(attributes.getValue("id"));
      double lat = Double.parseDouble(attributes.getValue("lat"));
      double lon = Double.parseDouble(attributes.getValue("lon"));
      Vertex.create(id, lat, lon);
    } else if (qName.equals("way")) {
      way = new LinkedList<Vertex>();
    } else if (qName.equals("nd") && way != null) {
      int ref = Integer.parseInt(attributes.getValue("ref"));
      Vertex p = Vertex.fromId(ref);
      if (p != null)
        way.add(p);
    } else if (qName.equals("tag") && way != null) {
    	String k = attributes.getValue("k");
      if (k.equals("name"))
        wayName = attributes.getValue("v");
      if (k.equals("oneway"))
        oneway = attributes.getValue("v").equals("yes");
      if (k.equals("waterway"))
        way = null;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName)
      throws SAXException {
    if (qName.equals("way") && way != null && way.size() > 1) {
      Vertex v = way.removeFirst();
      for (Vertex w : way) {
        Graph.addEdge(new Edge(v, w, wayName, oneway));
        if (!oneway)
          Graph.addEdge(new Edge(w, v, wayName, oneway));
        v = w;
      }
      way = null;
      wayName = null;
      oneway = false;
    }
  }
}
