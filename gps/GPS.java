package gps;

/* un petit navigateur GPS
 * utilisant les données XML du site OpenStreetMap
 */

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class GPS {
  
  public static void main(String[] args) throws SAXException, IOException,
      ParserConfigurationException {
    // lire le fichier XML OpenStreetMap
    File file = new File("region-parisienne.osm");
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser parser = factory.newSAXParser();
    parser.parse(file, new XMLHandler());
    // afficher quelques informations sur le graphe
    Graph.stat();
    // puis lancer l'interface graphique
    GUI gui = new GUI();
    gui.setVisible(true);
  }
  
}
