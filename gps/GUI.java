package gps;

// l'interface graphique

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

// fenêtre graphique

class GUI extends JFrame {
  private static final long serialVersionUID = 2465232426222345837L;

  GUI() {
    this.setTitle("navigateur GPS");
    this.add(new Window());
    this.setSize(800, 600);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.setResizable(false);
  }
}

// panneau graphique dans lequel on dessine

class Window extends JPanel {
  private static final long serialVersionUID = -6904944047037109623L;

  // le centre de la carte et son échelle
  private double centerLat = 0.851;
  private double centerLon = 0.040;
  private double scale = 800 / 0.008; // 800 pixels = scale radians

  Vertex select1, select2;
  private Iterable<Edge> path = null;

  Window() {
    // gestion des événements clavier
    KeyListener kl = new KeyListener() {
      @Override
      public void keyPressed(KeyEvent e) {
      }

      @Override
      public void keyReleased(KeyEvent e) {
      }

      @Override
      public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'q') System.exit(0);
        if (select1 == null || select2 == null) {
          System.out.println("choisir deux points svp");
          return;
        }
        switch (e.getKeyChar()) {
        case 'p':
          System.out.println("parcours en profondeur");
          path = FindPath.runDFS(select1, select2);
          FindPath.printPath(path);
          break;
        case 'l':
          System.out.println("parcours en largeur");
          path = FindPath.runBFS(select1, select2);
          FindPath.printPath(path);
          break;
        case 'd':
          System.out.println("algorithme de Dijkstra");
          path = FindPath.runDijkstra(select1, select2);
          FindPath.printPath(path);
          break;
        default:
          path = null;
          break;
        }
        repaint();
      }
    };
    this.setFocusable(true);
    this.requestFocus();
    this.addKeyListener(kl);

    // gestion des événements souris
    MouseAdapter ma = new MouseAdapter() {

      private Point lastMouse = null;

      @Override
      public void mousePressed(MouseEvent e) {
        //System.out.println("mouse pressed");
        lastMouse = e.getPoint();
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        lastMouse = null;
      }

      @Override
      public void mouseWheelMoved(MouseWheelEvent e) {
        scale *= (e.getWheelRotation() < 0 ? 1.2 : 0.8);
        System.out.println("new scale = " + scale);
        repaint();
      }

      @Override
      public void mouseClicked(MouseEvent e) {
        //System.out.println("mouse clicked");
        Point p = e.getPoint();
        double lon = lon(p.x);
        System.out.print("x = " + p.x);
        System.out.println(" lon = " + lon);
        double lat = lat(p.y);
        System.out.print("y = " + p.y);
        System.out.println(" lat = " + lat);
        if (select1 == null || select2 != null) {
          select1 = Graph.closest(lat, lon);
          select2 = null;
          path = null;
        } else {
          select2 = Graph.closest(lat, lon);
          path = null;
        }
        repaint();
      }

      @Override
      public void mouseDragged(MouseEvent e) {
        Point p = e.getPoint();
        if (lastMouse != null) {
          double dLat = lat(p.y) - lat(lastMouse.y);
          double dLon = lon(p.x) - lon(lastMouse.x);
          centerLat -= dLat;
          centerLon -= dLon;
          repaint();
        }
        lastMouse = p;
      }

    };
    this.addMouseListener(ma);
    this.addMouseMotionListener(ma);
    this.addMouseWheelListener(ma);
  }

  // dessin

  private int width = 800, height = 600;

  /* les méthodes suivantes convertissent les coordonnées (lat,lon)
   * en points à l'écran (x,y) et vice versa
   */
  private int x(double lon) {
    return (int) (width / 2 + (lon - centerLon) * scale);
  }

  private int x(Vertex v) {
    return x(v.lon);
  }

  private double lon(int x) {
    return centerLon + (x - width / 2) / scale;
  }

  private int y(double lat) {
    // une simple règle de trois
    return (int) (height / 2 - (lat - centerLat) * 2 * scale);
    // projection Mercator
//    double y0 = Math.log(Math.tan(Math.PI/4 + centerLat/2));
//    double y  = Math.log(Math.tan(Math.PI/4 +       lat/2));
//    return (int) (height / 2 - (y - y0) * scale);
  }

  private int y(Vertex v) {
    return y(v.lat);
  }

  private double lat(int y) {
    // avec la règle de trois
    return centerLat + (height / 2 - y) / scale / 2;
    // avec la projection Mercator
//    double y0 = Math.log(Math.tan(Math.PI/4 + centerLat/2));
//    double my = y0 + (height / 2 - y) / scale;
//    return 2 * Math.atan(Math.exp(my)) - Math.PI/2;
  }

  // dessin des sommets
  private void drawVertex(Graphics2D g, Vertex p, Color c, int radius) {
    g.setColor(c);
    g.fillOval(x(p) - radius/2, y(p) - radius/2, radius, radius);
  }

  private void drawVertex(Graphics2D g, Vertex p) {
    drawVertex(g, p, Color.black, 3);
  }

  // dessin des arcs
  
  // à partir de scaleLimit, on dessine les routes avec une épaisseur
  private static final double scaleLimit = 1000000;
  private static final BasicStroke pen1 = new BasicStroke(1);
  private static final BasicStroke pen5 = new BasicStroke(5);
  private static final BasicStroke pen7 = new BasicStroke(7);

  private void drawEdge(Graphics2D g, Edge e, BasicStroke stroke, Color color) {
    g.setColor(color);
    g.setStroke(stroke);
    g.drawLine(x(e.src), y(e.src), x(e.dst), y(e.dst));
  }

  private void drawEdge(Graphics2D g, Edge e, boolean firstStep) {
    if (!e.directed && e.src.id < e.dst.id)
      return;
    boolean road = scale >= scaleLimit;
    if (firstStep) {
      drawEdge(g, e, road ? pen7 : pen1, (e.directed ? Color.darkGray
          : Color.black));
    } else if (road) {
      drawEdge(g, e, pen5, Color.white);
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    width = this.getWidth();
    height = this.getHeight();
    for (Vertex p : Graph.vertices())
      drawVertex(g2d, p);
    for (List<Edge> l : Graph.edges())
      for (Edge e : l)
        drawEdge(g2d, e, true);
    for (List<Edge> l : Graph.edges())
      for (Edge e : l)
        drawEdge(g2d, e, false);
    // s'il y a des points sélectionnés, les montrer
    if (select1 != null)
      drawVertex(g2d, select1, Color.red, 10);
    if (select2 != null)
      drawVertex(g2d, select2, Color.blue, 10);
    // s'il y a un chemin, le dessiner
    if (path != null)
      for (Edge e : path)
        drawEdge(g2d, e, pen5, Color.red);
  }

}
