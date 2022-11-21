package planar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

/* Planar Point Location
 * voir https://en.wikipedia.org/wiki/Point_location
 * 
 * Étant donnée une division du plan en polygones dont les
 * arêtes ne se croisent pas, on souhaite construire une
 * structure de données pour déterminer, en temps logarithmique,
 * celui des polygones qui contient un point donné. 
 */

public class PointLocation {

	public static void main(String[] args) throws FileNotFoundException {
		// Vector<Edge> edges = readFile(args[0]);
		Vector<Edge> edges = readFile("planar20.txt");
		System.out.println(edges.size() + " edges");
		Slabs slabs = new Slabs(edges);
		System.out.println(slabs.size() + " slabs");
		GUI gui = new GUI(edges, slabs);
		gui.setVisible(true);
	}

	static Vector<Edge> readFile(String file) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(file));
		sc.useDelimiter("[\\p{javaWhitespace}\\p{Punct}]+");
		Vector<Edge> v = new Vector<>();
		try {
			while (true) {
				int x1 = sc.nextInt();
				int y1 = sc.nextInt();
				int x2 = sc.nextInt();
				int y2 = sc.nextInt();
				v.add(new Edge(x1, y1, x2, y2));
			}
		} catch (NoSuchElementException e) {
		}
		sc.close();
		return v;
	}
}

/* interface graphique */

@SuppressWarnings("serial")
class GUI extends JFrame {
	GUI(Vector<Edge> edges, Slabs slabs) {
		this.setTitle("Planar Point Location");
		this.add(new Window(edges, slabs));
		this.setSize(Window.width, Window.height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}
}

@SuppressWarnings("serial")
class Window extends JPanel {
	private Vector<Edge> edges;
	private Slabs slabs;
	private Edge solution = null;

	final static int width = 800, height = 620;

	Window(Vector<Edge> edges, Slabs slabs) {
		this.edges = edges;
		this.slabs = slabs;
		this.setFocusable(true);
		this.requestFocus();
		MouseAdapter ma = new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("mouse clicked");
				Point p = e.getPoint();
				System.out.println("x,y = " + p.x + "," + p.y);
				solution = slabs.locate(p.x, p.y);
				repaint();
			}
		};

		this.addMouseListener(ma);

	}

	static final BasicStroke pen1 = new BasicStroke(1);
	static final BasicStroke pen2 = new BasicStroke(2);
	static final BasicStroke pen5 = new BasicStroke(5);
	
	void drawEdge(Graphics2D g, Edge e, BasicStroke stroke, Color color) {
		g.setColor(color);
		g.setStroke(stroke);
		// g.setStroke(pen2);
		g.drawLine(e.x1, e.y1, e.x2, e.y2);
	}

	void drawEdge(Graphics2D g, Edge e) {
		drawEdge(g, e, e == solution ? pen5 : pen2, e == solution ? Color.blue
				: Color.black);
		g.setColor(Color.red);
		int r = 6;
		g.fillOval(e.x1 - r/2, e.y1 - r/2, r, r);
		g.fillOval(e.x2 - r/2, e.y2 - r/2, r, r);
	}

	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		for (Edge e : this.edges)
			drawEdge(g2d, e);
		g2d.setStroke(pen1);
		g2d.setColor(Color.red);
		for (Slab s : this.slabs.slabs)
			g2d.drawLine(s.xmin, 0, s.xmin, height);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}

}

// les arêtes

class Edge {
	int x1, y1, x2, y2; // x1 <= x2

	public Edge(int x1, int y1, int x2, int y2) {
		super();
		if (x1 <= x2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		} else {
			this.x1 = x2;
			this.y1 = y2;
			this.x2 = x1;
			this.y2 = y1;
		}
	}

	// ordonnée à l'abscisse x sur cette arête
	int yAt(int x) {
		assert x1 <= x && x <= x2;
		double f = (double) (x - this.x1) / (this.x2 - this.x1);
		return (int) (this.y1 + f * (this.y2 - this.y1));
	}

	public String toString() {
		return this.x1 + "," + this.y1 + "->" + this.x2 + "," + this.y2;
	}
}

// une tranche verticale

class Slab implements Comparable<Slab> {
	final int xmin, xmax;
	BST bst; // toutes les arêtes de cette tranche, ordonnées selon y croissant

	public Slab(int xmin, int xmax) {
		super();
		assert (xmin <= xmax);
		this.xmin = xmin;
		this.xmax = xmax;
		this.bst = null;
	}

	int intersection(Edge e) {
		assert intersects(e);
		return e.yAt(this.xmin);

	}

	boolean intersects(Edge e) {
		return e.x1 <= xmin && e.x2 > xmin;
	}

	boolean contains(int x) {
		return xmin <= x && x < xmax;
	}

	@Override
	public int compareTo(Slab o) {
		return this.xmin < o.xmin ? -1 : this.xmin == o.xmin ? 0 : 1;
	}

	public String toString() {
		return this.xmin + ".." + this.xmax + " (" + this.size() + " = " + this.bst
				+ ")";
	}

	// compare deux arêtes dans cette tranche
	// note : on sait que les arêtes ne se croisent pas
	int compare(Edge u, Edge v) {
		if (u == v)
			return 0;
		int c = u.yAt(this.xmin) - v.yAt(this.xmin);
		if (c != 0)
			return c;
		return u.yAt(this.xmax) - v.yAt(this.xmax);
	}

	void add(Edge e) {
		this.bst = BST.add(this, this.bst, e);
	}

	void remove(Edge e) {
		this.bst = BST.remove(this, this.bst, e);
	}

	Edge find(int x, int y) {
		return BST.find(this.bst, x, y, null, null);
	}

	int size() {
		return BST.size(this.bst);
	}

}

/* arbres binaires de recherche, représentant les arêtes d'une même tranche
 * 
 * il s'agit d'une structure immuable
 * note : add et remove prennent en argument la tranche (Slab)
 * pour permettre la comparaison
 */

class BST {
	final Edge value;
	final BST left, right;

	BST(BST left, Edge value, BST right) {
		super();
		this.value = value;
		this.left = left;
		this.right = right;
	}

	BST(Edge value) {
		this(null, value, null);
	}

	static BST add(Slab s, BST b, Edge x) {
		if (b == null)
			return new BST(x);
		int cmp = s.compare(x, b.value);
		if (cmp < 0)
			return new BST(add(s, b.left, x), b.value, b.right);
		if (cmp > 0)
			return new BST(b.left, b.value, add(s, b.right, x));
		return b;
	}

	// suppose b != null
	static Edge getMin(BST b) {
		while (b.left != null)
			b = b.left;
		return b.value;
	}

	// suppose b != null
	static BST removeMin(BST b) {
		if (b.left == null)
			return b.right;
		return new BST(removeMin(b.left), b.value, b.right);
	}

	static BST remove(Slab s, BST b, Edge x) {
		if (b == null)
			return null;
		int cmp = s.compare(x, b.value);
		if (cmp < 0)
			return new BST(remove(s, b.left, x), b.value, b.right);
		if (cmp > 0)
			return new BST(b.left, b.value, remove(s, b.right, x));
		// x == b.value
		if (b.right == null)
			return b.left;
		if (b.left == null) // optim
			return b.right;
		return new BST(b.left, getMin(b.right), removeMin(b.right));
	}

	static Edge find(BST b, int x, int y, Edge lo, Edge hi) {
		if (b == null)
			return lo;
		Edge e = b.value;
		int ye = e.yAt(x);
		if (y == ye)
			return e;
		if (y < ye)
			return find(b.left, x, y, lo, e);
		else
			return find(b.right, x, y, e, hi);
	}

	static int size(BST b) {
		if (b == null)
			return 0;
		return 1 + size(b.left) + size(b.right);
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		toString(b, this);
		return b.toString();
	}

	private void toString(StringBuilder b, BST t) {
		if (t == null)
			return;
		toString(b, t.left);
		b.append(t.value).append(" ");
		toString(b, t.right);
	}

}

class BSTSize {
	private HashSet<BST> nodes;
	BSTSize() {
		this.nodes = new HashSet<>();
	}
	void add(BST b) {
		if (b == null || this.nodes.contains(b)) return;
		this.nodes.add(b);
		add(b.left);
		add(b.right);
	}
	int size() { return this.nodes.size(); }
	
  static int nodeCounter = 0;
  
  static String node(HashMap<BST, String> h, BST a) {
    String s = h.get(a);
    if (s != null) return s;
    s = "n" + nodeCounter++;
    h.put(a,  s);
    System.out.println("  " + s + "[label=\"" + s + "\"];");
    return s;
  }
  
  void toDot() {
    System.out.println("digraph G {");
    System.out.println("node [shape=box,margin=0];");
    HashMap<BST, String> h = new HashMap<BST, String>();
    for (BST a: nodes) {
      String na = node(h, a);
      if (a.left != null) {
        String nl = node(h, a.left);
        System.out.println(na + " -> " + nl + " [color=\"blue\"];");
      }
      if (a.right != null) {
        String nr = node(h, a.right);
        System.out.println("  " + na + " -> " + nr + " [color=\"red\"];");
      }
    }
    System.out.println("}");
  }

}

/*
 * La structure de données proprement dite
 * 
 * construit toutes les tranches (construteur)
 * et répond à la question (méthode locate)
 */

class Slabs {
	final Vector<Slab> slabs;

	Slabs(Vector<Edge> edges) {
		this.slabs = new Vector<>();
		
		// toutes les abscisses, triées
		Vector<Integer> x = new Vector<Integer>();
		for (Edge e : edges) {
			x.add(e.x1);
			x.add(e.x2);
		}
		Collections.sort(x);
		int n = x.size();

		// on construit toutes les tranches
		int xmin = Integer.MIN_VALUE;
		for (int i = 0; i < n; i++) {
			while (i < n && x.get(i) == xmin)
				i++; // saute les doublons
			if (i == n)
				break;
			int next = x.get(i);
			this.slabs.add(new Slab(xmin, next));
			xmin = next;
		}
		this.slabs.add(new Slab(xmin, Integer.MAX_VALUE));

		// pour chaque tranche, on construit son arbre
		// on le fait en reprenant l'arbre de la tranche précédente,
		// en y supprimant les arêtes qui se termine dans la tranche
		// précédente et en ajoutant les arêtes qui débutent dans
		// la nouvelle tranche
		for (int i = 1; i < this.slabs.size(); i++) {
			Slab previous = this.slabs.get(i - 1);
			// 1. on supprime les arcs qui se termine dans previous
			// on le fait dans un Slab temporaire (même tranche que
			// previous, mais autre BST)
			Slab tmp = new Slab(previous.xmin, previous.xmax);
			tmp.bst = previous.bst;
			for (Edge e : edges)
				if (e.x2 == tmp.xmax)
					tmp.remove(e);
			// 2. cela devient la nouvelle tranche
			Slab current = this.slabs.get(i);
			current.bst = tmp.bst;
			// 3. on ajoute les arcs qui commence dans la nouvelle tranche
			for (Edge e : edges) {
				if (e.x1 == current.xmin)
					current.add(e);
			}
			System.out.println(current);
		}
		assert this.slabs.get(this.slabs.size() - 1).bst == null;

		BSTSize bs = new BSTSize();
		for (Slab s: this.slabs) bs.add(s.bst);
		System.out.println("total size = " + bs.size() + " nodes");
		// bs.toDot();
		
	}

	Edge locate(int x, int y) {
		Slab s = new Slab(x, x);
		int i = Collections.binarySearch(this.slabs, s);
		if (i < 0)
			i = -i - 2;
		s = this.slabs.get(i);
		System.out.println("slab " + i + " = " + s);
		return s.find(x, y);
	}

	int size() {
		return slabs.size();
	}

}